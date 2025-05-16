/**
 * Importação dos módulos necessários para configuração das rotas
 */
import { Routes } from '@angular/router';
import { LayoutComponent } from './layout/layout.component';
import { HomeComponent } from './pages/home/home.component';
import { LoginComponent } from './pages/login/login/login.component';
import { RegisterComponent } from './pages/register/register/register.component';
import { PacientesListaComponent } from './pages/pacientes-lista/pacientes-lista/pacientes-lista.component';
import { QuartosListaComponent } from './pages/quartos-lista/quartos-lista/quartos-lista.component';

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
            },
            {
                path: 'paciente',
                component: PacientesListaComponent
            },
            {
                path: 'quartos',
                component: QuartosListaComponent
            }
            // Adicione aqui as rotas filhas que serão renderizadas no RouterOutlet
            // Exemplo:
            // {
            //     path: 'dashboard',
            //     loadComponent: () => import('./pages/dashboard/dashboard.component').then(m => m.DashboardComponent)
            // }
        ]
    },
    {
        path: 'login',
        component: LoginComponent
    },
    {
        path: 'register',
        component: RegisterComponent
    }
];