import { Routes } from '@angular/router';
import { MedicamentListComponent } from './components/medicament-list/medicament-list.component';
import { MedicamentFormComponent } from './components/medicament-form/medicament-form.component';
import { MedicamentDetailComponent } from './components/medicament-detail/medicament-detail.component';

export const routes: Routes = [
  { path: '', redirectTo: '/medicaments', pathMatch: 'full' },
  { path: 'medicaments', component: MedicamentListComponent },
  { path: 'medicaments/nouveau', component: MedicamentFormComponent },
  { path: 'medicaments/:id', component: MedicamentDetailComponent },
  { path: 'medicaments/:id/modifier', component: MedicamentFormComponent },
  { path: 'disponibles', component: MedicamentListComponent },
  { path: 'rupture-stock', component: MedicamentListComponent },
  { path: '**', redirectTo: '/medicaments' }
];
