import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {SelectItem} from 'primeng/api';
import {LayoutService} from 'src/app/layout/service/app.layout.service';
import {AppComponent} from 'src/app/app.component';
import {AuthService} from "src/app/zynerator/security/shared/service/Auth.service";
import {TranslateService} from '@ngx-translate/core';
import {Router} from '@angular/router';
import gsap from 'gsap';

@Component({
  selector: 'app-top',
  templateUrl: './top.component.html',
  styleUrls: ['./top.component.scss']
})
export class TopComponent implements OnInit{
    roleAdmin = false;
    editDialog = false ;
    languageOptions: SelectItem[];
    selectedLanguage: string;




    @ViewChild('menubutton') menuButton!: ElementRef;

    @ViewChild('topbarmenubutton') topbarMenuButton!: ElementRef;

    @ViewChild('topbarmenu') menu!: ElementRef;



    public hideEditDialog() {
        this.editDialog = false;
    }



    constructor(public  layoutService:LayoutService , public app: AppComponent, private authService: AuthService, private translateService: TranslateService,private router: Router) {
        this.languageOptions = [
            { label: 'English', value: 'en' },
            { label: 'Français', value: 'fr' },
            { label: 'العربية', value: 'ar' }
        ];
    }

    useLanguage(language: string): void {
        this.translateService.use(language);
    }
    ngOnInit(): void {
        this.authService.loadInfos();
        if ( this.authService.authenticatedUser.roleUsers[0].role.authority === 'ROLE_ADMIN') {
            this.roleAdmin = true;
        }


        window.addEventListener('DOMContentLoaded', function() {
            const navLinks = document.querySelectorAll('.nav-link');
            const currentUrl = window.location.pathname;

            navLinks.forEach(link => {
                if(link.getAttribute('href') === currentUrl) {
                    link.classList.add('active');
                }
            });
        });

    }

    logout(){
        this.authService.logout();
    }



    navigate() {
        this.router.navigate(['/contact']);
    }

}
