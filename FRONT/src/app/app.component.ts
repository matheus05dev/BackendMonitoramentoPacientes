/**
 * Componente principal da aplicação
 * Responsável por inicializar a aplicação e fornecer o container para o roteamento
 */
import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    RouterOutlet  // Necessário para renderizar as rotas no componente raiz
  ],
  template: `
    <!-- Container principal da aplicação -->
    <router-outlet></router-outlet>
  `,
  styles: [`
    :host {
      display: block;
      height: 100vh;
      width: 100vw;
    }
  `]
})
export class AppComponent {
  title = 'Sistema de Monitoramento de Pacientes';
}
