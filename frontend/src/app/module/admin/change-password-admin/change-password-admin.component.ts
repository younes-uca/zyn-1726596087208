import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {Message, MessageService} from 'primeng/api';
import {AuthService} from 'src/app/zynerator/security/shared/service/Auth.service';
import {LayoutService} from 'src/app/layout/service/app.layout.service';
import { ChangePasswordDto } from 'src/app/zynerator/security/shared/model/ChangePassword.model';
import { HttpErrorResponse } from '@angular/common/http';


@Component({
  selector: 'app-change-password-admin',
  templateUrl: './change-password-admin.component.html',
  styleUrls: ['./change-password-admin.component.css']
})
export class ChangePasswordAdminComponent implements OnInit {
    changePasswordDto: ChangePasswordDto = new ChangePasswordDto();
    messages: Message[] = [];
    active: boolean = false;

    constructor(
        private authService: AuthService,
        public layoutService: LayoutService,
        private router: Router,
        private messageService: MessageService
    ) {
    }

    ngOnInit(): void {
        this.messages = [];
        this.changePasswordDto.email = this.authService.sharedEmail;
    }


    submit() {
        if (this.changePasswordDto.password !== this.changePasswordDto.confirmPassword) {
            console.error("Passwords don't match");
            this.messageService.add({
                severity: 'error',
                summary: 'Error',
                detail: "Passwords don't match"
            });
            return;
        }

        this.authService.resetPassword(this.changePasswordDto).subscribe({
            next: () => {
                console.log('Password changed successfully');
                this.messageService.add({
                    severity: 'success',
                    summary: 'Password Changed',
                    detail: 'Your password has been successfully changed.'
                });
                // Optionally, you can navigate to another page or perform other actions upon success
            },
            error: (error: HttpErrorResponse) => {
                let errorMessage = 'Error resetting password';

                // Handle specific error scenarios from the backend
                if (error.status === 400 && error.error.message.includes('Max password reset requests reached')) {
                    if (error.error.retryAfterSeconds) {
                        const retryAfterSeconds = error.error.retryAfterSeconds;
                        const hours = Math.floor(retryAfterSeconds / 3600);
                        const minutes = Math.floor((retryAfterSeconds % 3600) / 60);
                        const seconds = retryAfterSeconds % 60;
                        errorMessage = `Max password reset requests reached. Please try again in ${hours} hours, ${minutes} minutes, and ${seconds} seconds.`;
                    } else {
                        errorMessage = 'Max password reset requests reached. Please try again later.';
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

    login() {
        this.router.navigate(['/admin/login']);
    }

    forgetPassword() {
        this.router.navigate(['/admin/forget-password'])
    }

    clearMessages() {
        this.messageService.clear();
    }



}
