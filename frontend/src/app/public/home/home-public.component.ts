import {Component, OnInit} from '@angular/core';
import {LayoutService} from "../../layout/service/app.layout.service";
import {AppComponent} from "../../app.component";
import gsap from "gsap";


@Component({
    selector: 'app-home',
    templateUrl: './home-public.component.html',
    styleUrls: ['./home-public.component.scss']
})
export class HomePublicComponent implements OnInit {
    onSearchInput(event: any) {
        const searchTerm = event.target.value;
        // Handle search input here
        console.log('Search term:', searchTerm);
    }


    constructor(public layoutService: LayoutService, public app: AppComponent ) {

    }


    loading = true;
    bool: boolean = true;

    ngOnInit(): void {
        gsap.to(".spring", { rotation: 360, duration: 8, repeat: -1,ease: "linear" });
        gsap.fromTo(".spring", {x: 500 , y: 10}, {x: 0 , y:0, duration: 5, ease: "circ"});
        gsap.to(".dotnet", { rotation: 360, duration: 8, repeat: -1,ease: "linear" });
        gsap.fromTo(".dotnet", {x: -500 , y: 10}, {x: 0 , y:0, duration: 5, ease: "circ"});
        gsap.to(".react", { rotation: 360, duration: 8, repeat: -1,ease: "linear" });
        gsap.fromTo(".react", {x: 500 , y: 10}, {x: 0 , y:0, duration: 5, ease: "circ"});
        gsap.to(".angular", { rotation: 360, duration: 8, repeat: -1,ease: "linear" });
        gsap.fromTo(".angular", {x: -500 , y: 10}, {x: 0 , y:0, duration: 5, ease: "circ"});
        gsap.to(".title", { translate: 1, y: 50, duration: 3,ease: "linear" });


        const tl = gsap.timeline()
        if(this.bool){
            tl.fromTo(".body1", {opacity: 0}, {opacity: 1, duration: 3})
            this.bool = false
        }


    }

}
