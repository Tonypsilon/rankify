import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: '',
    loadChildren: () => import('./features/home/home.routes').then(m => m.HOME_ROUTES)
  },
  {
    path: 'create-poll',
    loadComponent: () => import('./features/poll-creation/poll-creation.component').then(m => m.PollCreationComponent)
  },
  {
    path: 'polls/:pollName',
    loadComponent: () => import('./features/poll-management/poll-management.component').then(m => m.PollManagementComponent)
  },
  {
    path: 'cast-ballot/:pollName',
    loadComponent: () => import('./features/ballot-casting/ballot-casting.component').then(m => m.BallotCastingComponent)
  },
  {
    path: '**',
    redirectTo: ''
  }
];
