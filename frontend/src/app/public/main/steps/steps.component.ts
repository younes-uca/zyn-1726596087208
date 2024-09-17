import {Component, OnInit} from '@angular/core';

@Component({
  selector: 'app-steps',
  templateUrl: './steps.component.html',
  styleUrls: ['./steps.component.scss']
})
export class StepsComponent implements OnInit {
    ngOnInit() {
        const titre = document.getElementById('titre');
        titre.classList.add('hide')
        window.addEventListener("scroll", function (event) {
            if(window.scrollY > 0) {
                titre.classList.remove('hide');
                titre.classList.add('show');
            }else{
                titre.classList.remove('show');
                titre.classList.add('hide')
                titre.style.transition = 'opacity 1s ease';
            }
        })
    }
}
