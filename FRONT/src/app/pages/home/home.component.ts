import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';

@Component({
    selector: 'app-home',
    standalone: true,
    imports: [CommonModule, MatCardModule, MatIconModule],
    templateUrl: './home.component.html',
    styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit, OnDestroy {
    currentSlide = 0;
    private slideInterval: any;

    ngOnInit() {
        // Inicia o carrossel automático
        this.startSlideShow();
    }

    ngOnDestroy() {
        // Limpa o intervalo quando o componente é destruído
        if (this.slideInterval) {
            clearInterval(this.slideInterval);
        }
    }

    private startSlideShow() {
        this.slideInterval = setInterval(() => {
            this.currentSlide = (this.currentSlide + 1) % 3;
        }, 5000);
    }
} 