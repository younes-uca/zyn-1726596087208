import {HttpClient, HttpErrorResponse, HttpHeaders} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Router} from '@angular/router';

import {environment} from 'src/environments/environment';

import {BehaviorSubject, catchError, map, Observable, throwError} from 'rxjs';


import {TokenService} from './Token.service';
import {UserDto} from '../model/User.model';
import {RoleDto} from '../model/Role.model';
import {RoleUserDto} from '../model/RoleUser.model';
import {MessageService} from 'primeng/api';
import { UserService } from './User.service';
import {ChangePasswordDto} from "../model/ChangePassword.model";

@Injectable({
    providedIn: 'root'
})
export class AuthService {
    readonly API = environment.loginUrl;
    readonly registerUrl = environment.registerUrl;
    public _user = new UserDto();
    private _adminConnected = false;
    private _authenticatedUser = new UserDto();
    private _authenticated = (JSON.parse(localStorage.getItem('autenticated')) as boolean) || false;
    public _loggedIn = new BehaviorSubject<boolean>(false);
    public loggedIn$ = this._loggedIn.asObservable();
    public error: string = null;
    private _sharedUserName = '';
    private _sharedPassword = '';
    private _sharedEmail = '';


    constructor(private http: HttpClient, private tokenService: TokenService, private router: Router, private messageService: MessageService, private userService: UserService) {
    }




    // @ts-ignore
	public login(username: string, password: string) {
        console.log(username, password);
        this.http.post<any>(this.API + 'login', {username, password}, {observe: 'response'}).subscribe(
            resp => {
                console.log(resp);
                this.error = null;
                const jwt = 'Bearer ' + resp.body.accessToken;
                jwt != null ? this.tokenService.saveToken(jwt) : false;
                this.loadInfos();
                if (this._authenticatedUser.roleUsers.length === 1) {
                    const roleAuthority = this._authenticatedUser.roleUsers[0].role.authority;
                    if (roleAuthority === 'ROLE_ADMIN') {
                        this.adminConnected = true;
                    }
                }
                this._authenticatedUser.roleUsers.forEach(item => {
                    const roleAuthority = item.role.authority.replace('ROLE_', '').toLowerCase();
                    this.router.navigate(['/' + environment.rootAppUrl + '/' + roleAuthority]);
                });
            }, (errorException: HttpErrorResponse) => {
                let errorMessage = '';
                if (errorException.error && errorException.error.message) {
                    errorMessage = errorException.error.message;
                } else {
                    errorMessage = 'An error occurred';
                }

                if (errorException.status === 401) {
                    if (errorMessage.toLowerCase().includes('user is disabled')) {
                        this.messageService.add({
                            severity: 'error',
                            summary: 'Error ' + errorException.status,
                            detail: 'Unauthorized: User is disabled'
                        });
                    } else {
                        this.messageService.add({
                            severity: 'error',
                            summary: 'Error ' + errorException.status,
                            detail: 'Invalid credentials'
                        });
                    }
                } else if (errorException.status === 423) {
                    const match = errorMessage.match(/(\d+) seconds/);
                    let remainingTime = 'unknown';
                    if (match) {
                        const seconds = parseInt(match[1], 10);
                        const minutes = Math.floor(seconds / 60);
                        const remainingSeconds = seconds % 60;
                        remainingTime = `${minutes} minutes and ${remainingSeconds} seconds`;
                    }
                    this.messageService.add({
                        severity: 'error',
                        summary: 'Error ' + errorException.status,
                        detail: `Account is locked. Please try again in ${remainingTime}.`
                    });
                } else if (errorException.status === 405) {
                    this.messageService.add({
                        severity: 'error',
                        summary: 'Error ' + errorException.status,
                        detail: 'Method Not Allowed: Please check your request method'
                    });
                } else {
                    this.messageService.add({
                        severity: 'error',
                        summary: 'Error ' + errorException.status,
                        detail: 'An unexpected error occurred'
                    });
                }
            }
        );
    }

    /*
    public loginUsingGoogle(){
         this.http.post<any>(this.API + 'loginwithGoogle', this.user, {observe: 'response'}).subscribe(
            resp => {
                console.log(resp);
                this.error = null;
                const jwt = 'Bearer '+resp.body.accessToken;
                jwt != null ? this.tokenService.saveToken(jwt) : false;
                this.loadInfos();
                this._authenticatedUser.roleUsers.forEach(item => {
                    const roleAuthority = item.role.authority.replace('ROLE_', '').toLowerCase();
                    this.router.navigate(['/' + environment.rootAppUrl + '/'+roleAuthority]);
                })
            }, (error: HttpErrorResponse) => {
                this.error = error.error.message;
                if (error.status === 401) {
                    let errorMessage = '';
                    if (this.error && error.message) {
                        errorMessage = error.error.message;
                    } else {
                        errorMessage = 'Unauthorized: Invalid credentials';
                    }
                    if (errorMessage.toLowerCase().includes('user is disabled')) {
                        this.messageService.add({
                            severity: 'error',
                            summary: 'Error ' + error.status,
                            detail: 'Unauthorized: User is disabled'
                        });
                    } else {
                        this.messageService.add({severity: 'error', summary: 'Error ' + error.status, detail: errorMessage});
                    }
                }else if (error.status === 405) {
                    this.messageService.add({
                        severity: 'error',
                        summary: 'Error ' + error.status,
                        detail: 'Method Not Allowed: Please check your request method'
                    });
                } else {
                    this.messageService.add({severity: 'error', summary: 'Error ' + error.status, detail: 'An unexpected error occurred'});
                }
            }
        );
    }
    */

    public loadInfos() {
        const tokenDecoded = this.tokenService.decode();
        const username = tokenDecoded.sub;
        const roles = tokenDecoded.roles;
        const email = tokenDecoded.email;
        const firstName = tokenDecoded.firstName;
        const lastName = tokenDecoded.lastName;
        const phone = tokenDecoded.phone;
        const passwordChanged = tokenDecoded.passwordChanged;
        this._authenticatedUser.passwordChanged = passwordChanged;
        this._authenticatedUser.username = username;
        this._authenticatedUser.phone = phone;
        this._authenticatedUser.firstName = firstName;
        this._authenticatedUser.lastName = lastName;
        this._authenticatedUser.email = email;
        roles.forEach(role => {
            const roleUser = new RoleUserDto();
            roleUser.role.authority = role;
            this._authenticatedUser.roleUsers.push(roleUser);
        });
        const roleAuthority = this._authenticatedUser.roleUsers[0].role.authority;
        localStorage.setItem('autenticated', JSON.stringify(true));
        this.authenticated = true;
        this._loggedIn.next(true);

    }

    public hasRole(role: RoleDto): boolean {
        const index = this._authenticatedUser.roleUsers.findIndex(r => r.role.authority === role.authority);
        return index > -1 ? true : false;
    }
    public registerAdmin() {
        this.register(this.registerUrl + 'admin', 'ROLE_ADMIN', '/admin/activation')
    }

    private register(urlRegister: string, roleName: string, redirectPath: string) {
        console.log(this.user);
        this.http.post<any>(urlRegister, this.user, {observe: 'response'}).subscribe(
            resp => {
                this.messageService.add({
                    severity: 'success',
                    summary: 'Success',
                    detail: ' * Check your email to activate your account *'
                });
                if (this.user.roleUsers[0].role.authority === roleName) {
                    this.router.navigate([redirectPath]);
                }
            }, (error: HttpErrorResponse) => {
                console.log(error);
                this.error = error.error.error;
                if (error.status === 401) {
                    let errorMessage = '';
                    if (this.error && error.message) {
                        errorMessage = error.error.message;
                    } else {
                        errorMessage = 'Unauthorized: Invalid credentials';
                    }
                    if (errorMessage.toLowerCase().includes('user is disabled')) {
                        this.messageService.add({
                            severity: 'error',
                            summary: 'Error ' + error.status,
                            detail: 'Unauthorized: User is disabled'
                        });
                    } else {
                        this.messageService.add({
                            severity: 'error',
                            summary: 'Error ' + error.status,
                            detail: errorMessage
                        });
                    }
                } else if (error.status === 405) {
                    this.messageService.add({
                        severity: 'error',
                        summary: 'Error ' + error.status,
                        detail: 'Method Not Allowed: Please check your request method'
                    });
                } else {
                    this.messageService.add({
                        severity: 'error',
                        summary: 'Error ' + error.status,
                        detail: error.error.error
                    });
                }
            }
        );
    }

    public activateAccount(activationCode: string, username: string): Observable<any> {
        return this.http.post<any>(this.API + 'activateAccount', { activationCode, username }, { observe: 'response' })
            .pipe(
                map(resp => {
                    if (this.user.roleUsers[0].role.authority === 'ADMIN') {
                        this.router.navigate(['/admin/login']);
                    }
                        else if (this.user.roleUsers[0].role.authority === 'ADMIN') {
                        this.router.navigate(['/admin/login']);
                        }
                    return resp;
                }),
                catchError((error: HttpErrorResponse) => {
                    this.error = error.error.error;
                    if (error.status === 401) {
                        let errorMessage = '';
                        if (this.error && error.message) {
                            errorMessage = error.error.message;
                        } else {
                            errorMessage = 'Unauthorized: Invalid credentials';
                        }
                        if (errorMessage.toLowerCase().includes('user is disabled')) {
                            this.messageService.add({
                                severity: 'error',
                                summary: 'Error ' + error.status,
                                detail: 'Unauthorized: User is disabled'
                            });
                        } else {
                            this.messageService.add({
                                severity: 'error',
                                summary: 'Error ' + error.status,
                                detail: errorMessage
                            });
                        }
                    } else if (error.status === 405) {
                        this.messageService.add({
                            severity: 'error',
                            summary: 'Error ' + error.status,
                            detail: 'Method Not Allowed: Please check your request method'
                        });
                    } else {
                        this.messageService.add({
                            severity: 'error',
                            summary: 'Error ' + error.status,
                            detail: error.error.message
                        });
                    }
                    throw error;
                })
            );
    }

    // Méthode pour obtenir le jeton depuis le stockage local
    getToken(): string | null {
        return localStorage.getItem('token');
    }

    resendActivationCode(username: string): Observable<any> {
        const headers = new HttpHeaders().set('Authorization', 'Bearer ' + this.getToken());
        return this.http.post(`${this.API}api/user/resend-activation-code?username=${username}`, {}, { headers });
    }


    public forgetPassword(email: string, password: string) {
        return new Promise((resolve, reject) => {
         this.http.put<any>(`${this.API}api/user/send-forget?email=${email}`, {observe: 'response'}).subscribe(
                resp => {
                    resolve(resp.body);
                },
                error => {
                    console.log(error.error);
                    reject(error.error);
                }
            );
        });
    }



    sendForgetPassword(email: string): Observable<any> {
        const headers = new HttpHeaders().set('Authorization', 'Bearer ' + this.getToken());
        return this.http.post(`${this.API}api/user/reset-password?username=${email}`, {}, { headers });
    }

    sendForgetPasswordActivation(email: string): Observable<any> {
        const url = `${this.API}api/api/user/send-forget-password-code`;
        const body = { email };

        const token = localStorage.getItem('jwtToken');
        const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);

        return this.http.post(url, body, { headers});
    }


    private jwtToken = '';
    resetPassword(changePasswordDto : ChangePasswordDto) {
        const url = `${this.API}api/user/reset-password`;

        const formData = new FormData();
        formData.append('email', changePasswordDto.email);
        formData.append('activationCode', changePasswordDto.activationCode);
        formData.append('newPassword', changePasswordDto.password);


        const headers = new HttpHeaders({
                'Authorization': `Bearer ${this.jwtToken}`
        });

        return this.http.post(url, formData, { headers })
            .pipe(
                catchError((error: HttpErrorResponse) => {
                    let errorMessage = 'An unknown error occurred';
                    if (error.error instanceof ErrorEvent) {
                    errorMessage = `Error: ${error.error.message}`;
                    } else {
                errorMessage = `Error Code: ${error.status}\nMessage: ${error.message}`;
                    }
                    console.error(errorMessage);
                    return throwError(errorMessage);
                })
            );
    }


    public logout() {
        this.tokenService.removeToken();
        this.adminConnected = false;
        localStorage.setItem('autenticated', JSON.stringify(false));
        this.authenticated = false;
        this._loggedIn.next(false);
        this._authenticatedUser = new UserDto();
        this.router.navigate(['']);
    }

    get user(): UserDto {
        return this._user;
    }

    set user(value: UserDto) {
        this._user = value;
    }

    get authenticated(): boolean {

        return this._authenticated;
    }

    set authenticated(value: boolean) {
        this._authenticated = value;
    }

    get authenticatedUser(): UserDto {
        return this._authenticatedUser;
    }

    set authenticatedUser(value: UserDto) {
        this._authenticatedUser = value;
    }
    get adminConnected(): boolean {
        return this._adminConnected;
    }

    set adminConnected(value: boolean) {
        this._adminConnected = value;
    }


    get sharedUserName(): string {
        return this._sharedUserName;
    }

    set sharedUserName(value: string) {
        this._sharedUserName = value;
    }

    get sharedEmail(): string {
        return this._sharedEmail;
    }

    set sharedEmail(value: string) {
        this._sharedEmail = value;
    }
}
