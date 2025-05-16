/**
 * Componente de Layout Principal
 * Responsável por definir a estrutura base da aplicação, incluindo:
 * - Barra de navegação superior (toolbar)
 * - Menu lateral (sidenav)
 * - Área de conteúdo principal
 */
import { Component } from '@angular/core';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatIconModule, MatIconRegistry } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatSidenavModule } from '@angular/material/sidenav';
import { RouterModule, RouterOutlet } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-layout',
  standalone: true,
  imports: [
    CommonModule,        // Módulo para diretivas comuns (ngIf, ngFor, etc)
    MatToolbarModule,    // Módulo para a barra de navegação superior
    MatIconModule,       // Módulo para ícones do Material
    MatButtonModule,     // Módulo para botões do Material
    MatSidenavModule,    // Módulo para o menu lateral
    RouterModule,        // Módulo para navegação e rotas
    RouterOutlet        // Componente para renderização das rotas
  ],
  templateUrl: './layout.component.html',
  styleUrl: './layout.component.css'
})
export class LayoutComponent {
  // Aqui podem ser adicionadas propriedades e métodos específicos do layout
  // Por exemplo: controle de estado do menu, autenticação, etc.
}

