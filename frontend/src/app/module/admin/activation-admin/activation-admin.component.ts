import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Message, MessageService } from 'primeng/api';
import { AuthService } from 'src/app/zynerator/security/shared/service/Auth.service';
import { LayoutService } from 'src/app/layout/service/app.layout.service';
import { UserDto } from 'src/app/zynerator/security/shared/model/User.model';
import { environment } from 'src/environments/environment';
import { HttpErrorResponse } from '@angular/common/http';
import { ActivationDto } from 'src/app/zynerator/security/shared/model/Activation.model';


@Component({
  selector: 'app-activation-admin',
  templateUrl: './activation-admin.component.html',
  styleUrls: ['./activation-admin.component.css']
})
export class ActivationAdminComponent implements OnInit {
      public error: string = null;
    readonly API = environment.loginUrl;
    activationDto = new ActivationDto();
    clicked = false;
    messages: Message[] = [];

    constructor(private authService: AuthService, public layoutService: LayoutService, private router: Router, private messageService: MessageService) {
    }

    ngOnInit(): void {
        this.messages = [];
        const storedUsername = this.authService.sharedUserName;
        if (storedUsername) {
            this.activationDto.username = storedUsername;
        }
    }

    activateAccount() {
        this.clicked = true;
        this.activationDto = this.activationDto;

        if (this.activationDto.username && this.activationDto.activationCode) {
            this.authService.activateAccount(this.activationDto.activationCode, this.activationDto.username).subscribe({
                next: () => {
                    this.messageService.add({
                        severity: 'success',
                        summary: 'Success',
                        detail: 'Account activated successfully'
                    });
                },
                error: err => {
                    this.messageService.add({
                        severity: 'error',
                        summary: 'Error',
                        detail: err.error.message || 'Activation failed'
                    });
                }
            });
        } else {
            this.messageService.add({
                severity: 'error',
                summary: 'Error',
                detail: 'Fill in all fields'
            });
        }
    }

    // Méthode pour renvoyer le code d'activation
    resendActivationCode() {
        this.authService.resendActivationCode(this.activationDto.username).subscribe({
            next: () => {
                this.messageService.add({
                    severity: 'success',
                    summary: 'Success',
                    detail: 'Activation code resent successfully'
                });
            },
            error: (error: HttpErrorResponse) => {
                let errorMessage = 'Error resending activation code';
                if (error.status === 400 && error.error.message.includes('Max activation requests reached')) {
                    if (error.error.retryAfterSeconds) {
                        const retryAfterSeconds = error.error.retryAfterSeconds;
                        const hours = Math.floor(retryAfterSeconds / 3600);
                        const minutes = Math.floor((retryAfterSeconds % 3600) / 60);
                        const seconds = retryAfterSeconds % 60;
                        errorMessage = `Max activation requests reached. Please try again in ${hours} hours, ${minutes} minutes, and ${seconds} seconds.`;
                    } else {
                        errorMessage = 'Max activation requests reached. Please try again later.';
                    }
                } else if (error.error && error.error.message) {
                    errorMessage = error.error.message;
                }
                this.messageService.add({
                    severity: 'error',
                    summary: 'Error',
                    detail: errorMessage
                });
            }
        });
    }

    register() {
        this.router.navigate(['/admin/register']);
    }

    forget() {
        this.router.navigate(['/admin/changePassword']);
    }

    get user(): UserDto {
        return this.authService.user;
    }

    set user(value: UserDto) {
        this.authService.user = value;
    }
}
