import {Component, OnInit} from '@angular/core';
import gsap from 'gsap';

@Component({
  selector: 'app-preloadpage',
  templateUrl: './preloadpage.component.html',
  styleUrls: ['./preloadpage.component.scss']
})
export class PreloadpageComponent implements OnInit {

    ngOnInit() {

        const tl = gsap.timeline({});

        gsap.fromTo("#z", {y: -300, opacity: 0}, {y: 0, opacity: 1, duration: 5, ease: 'elastic'})
        gsap.fromTo("#y", {y: 300, opacity: 0}, {y: 0, opacity: 1, duration: 5, ease: 'elastic'})
        gsap.fromTo("#n", {y: -300, opacity: 0}, {y: 0, opacity: 1, duration: 5, ease: 'elastic'})
        gsap.fromTo("#e", {y: 300, opacity: 0}, {y: 0, opacity: 1, duration: 5, ease: 'elastic'})
        gsap.fromTo("#r", {y: -300, opacity: 0}, {y: 0, opacity: 1, duration: 5, ease: 'elastic'})
        gsap.fromTo("#a", {y: 300, opacity: 0}, {y: 0, opacity: 1, duration: 5, ease: 'elastic'})
        gsap.fromTo("#t", {y: -300, opacity: 0}, {y: 0, opacity: 1, duration: 5, ease: 'elastic'})
        gsap.fromTo("#r1", {y: -300, opacity: 0}, {y: 0, opacity: 1, duration: 5, ease: 'elastic'})
        tl.fromTo("#o", {x: 300, opacity: 0}, {x: 0, opacity: 1, duration: 2, ease: 'elastic'}, "=+1")
            .fromTo("#l", {x: -300, opacity: 0}, {rotation: 360, x: 0, opacity: 1, duration: 3, ease: 'elastic'}, ">")
            .fromTo(".preload-page", {
                opacity: 1,
            }, {
                duration: 3,
                opacity: 0,
                onComplete: function () {
                    setTimeout(function () {
                        window.location.href = "/home";
                    }, 500); // Slight delay before redirecting
                }
            });
    }
}
