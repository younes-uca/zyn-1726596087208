// src/app/spinner/spinner.component.ts
import { Component, OnInit } from '@angular/core';
import { LoadingService } from './loading.service';
import { Observable } from 'rxjs';

@Component({
    selector: 'app-spinner',
    templateUrl: './spinner.component.html',
    styles: [`
        .fullscreen-overlay {
            position: fixed;
            top: 0;
            left: 0;
            right: 0;
            bottom: 0;
            background: rgba(0, 0, 0, 0.5);
            display: flex;
            justify-content: center;
            align-items: center;
            z-index: 10000;
        }
    `]
})
export class SpinnerComponent implements OnInit {
    isLoading: Observable<boolean>;

    constructor(private loadingService: LoadingService) {
        this.isLoading = this.loadingService.loading$;
    }

    ngOnInit(): void {}
}
