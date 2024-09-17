import {Component, OnInit} from '@angular/core';
import gsap from 'gsap';

@Component({
    selector: 'app-contact-us',
    templateUrl: './contact-us.component.html',
    styleUrls: ['./contact-us.component.scss']
})
export class ContactUsComponent implements OnInit {

    ngOnInit() {
        const send = document.getElementById('send');
        send.addEventListener("click", () => {
            gsap.to("#send-logo", {duration: 1.2, translateX: 65, width: 0})
        })
    }
}
