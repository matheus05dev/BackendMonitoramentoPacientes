/**
 * Importação dos módulos necessários para configuração das rotas
 */
import { Routes } from '@angular/router';
import { LayoutComponent } from './layout/layout.component';
import { HomeComponent } from './pages/home/home.component';

/**
 * Definição das rotas da aplicação
 * Estrutura base com layout principal
 * O RouterOutlet no LayoutComponent irá renderizar o conteúdo das rotas filhas
 */
export const routes: Routes = [
    {
        path: '',
        redirectTo: 'home',
        pathMatch: 'full'
    },
    {
        path: '',
        component: LayoutComponent,
        children: [
            {
                path: 'home',
                component: HomeComponent   
            }
            // Adicione aqui as rotas filhas que serão renderizadas no RouterOutlet
            // Exemplo:
            // {
            //     path: 'dashboard',
            //     loadComponent: () => import('./pages/dashboard/dashboard.component').then(m => m.DashboardComponent)
            // }
        ]
    }
];