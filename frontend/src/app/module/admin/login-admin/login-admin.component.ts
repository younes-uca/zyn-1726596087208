import { Component, OnInit } from '@angular/core';
import {Router} from '@angular/router';
import {  MessageService} from 'primeng/api';
import {AuthService} from 'src/app/zynerator/security/shared/service/Auth.service';
import {LayoutService} from 'src/app/layout/service/app.layout.service';
import {UserDto} from "src/app/zynerator/security/shared/model/User.model";
import {environment} from "src/environments/environment";
import { LoginDto } from 'src/app/zynerator/security/shared/model/Login.model';



@Component({
  selector: 'app-login-admin',
  templateUrl: './login-admin.component.html',
  styleUrls: ['./login-admin.component.css']
})
export class LoginAdminComponent implements OnInit {
    readonly API = environment.loginUrl;
    googleLoginOptions = {
      scope: 'profile email',
      plugin_name: 'sample_login'
    };

    private _authenticatedUser = new UserDto();
    loginDto: LoginDto = new LoginDto();


    constructor(private authService: AuthService, public layoutService: LayoutService, private router: Router, private messageService: MessageService) { }

    ngOnInit(): void {
    }

    submit() {
      if (this.loginDto.username && this.loginDto.password) {
        this.authService.login(this.loginDto.username, this.loginDto.password);
      } else {
        this.messageService.add({
          severity: 'error',
          summary: 'Error',
          detail: 'Fill in all fields'
        });
      }
    }

    public signInWithGoogle(): void {
      /*
       this.oauthService.signIn(GoogleLoginProvider.PROVIDER_ID,this.googleLoginOptions).then(
          data => {
            this.socialUser = data;
          const role = new RoleDto();
          role.authority = 'ROLE_COLLABORATOR' ;
          const roleUser = new RoleUserDto();
          roleUser.role = role;
          this.user.accountNonExpired=true;
          this.user.accountNonLocked=true;
          this.user.credentialsNonExpired=true;
          this.user.enabled=false;
          this.user.passwordChanged=false;
          this.user.firstName = data.firstName;
          this.user.lastName = data.lastName;
          this.user.phone = null;
          this.user.username = data.email;
          this.user.password = Date.now()+"-"+data.email;
          this.user.email = data.email;
          this.user.roleUsers = new Array<RoleUserDto>();
          this.user.roleUsers.push(roleUser);
            this.authService.loginUsingGoogle();
          }
        ).catch(
          err => {
            console.log(err);
          }
        );
    */
    }

    register() {
      this.router.navigate(['/admin/register']);
    }

    forget() {
        this.router.navigate(['/admin/forget-password']);
    }


    get user(): UserDto {
      return this.authService.user;
    }

    set user(value: UserDto) {
      this.authService.user = value;
    }
  }
