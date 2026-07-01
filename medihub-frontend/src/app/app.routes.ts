import { Routes } from '@angular/router';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { MedicamentListComponent } from './components/pharmacie/medicament-list/medicament-list.component';
import { MedicamentFormComponent } from './components/pharmacie/medicament-form/medicament-form.component';
import { MedicamentDetailComponent } from './components/pharmacie/medicament-detail/medicament-detail.component';
import { DossierListComponent } from './components/dossier/dossier-list/dossier-list.component';
import { DossierFormComponent } from './components/dossier/dossier-form/dossier-form.component';
import { DossierDetailComponent } from './components/dossier/dossier-detail/dossier-detail.component';
import { RendezVousListComponent } from './components/rendezvous/rendezvous-list/rendezvous-list.component';
import { RendezVousFormComponent } from './components/rendezvous/rendezvous-form/rendezvous-form.component';
import { DisponibiliteFormComponent } from './components/rendezvous/disponibilite-form/disponibilite-form.component';
import { UserListComponent } from './components/users/user-list/user-list.component';

export const routes: Routes = [
  { path: '', redirectTo: '/dashboard', pathMatch: 'full' },
  { path: 'dashboard', component: DashboardComponent },

  // Pharmacie
  { path: 'medicaments', component: MedicamentListComponent },
  { path: 'medicaments/nouveau', component: MedicamentFormComponent },
  { path: 'medicaments/:id', component: MedicamentDetailComponent },
  { path: 'medicaments/:id/modifier', component: MedicamentFormComponent },

  // Dossiers
  { path: 'dossiers', component: DossierListComponent },
  { path: 'dossiers/nouveau', component: DossierFormComponent },
  { path: 'dossiers/:id', component: DossierDetailComponent },
  { path: 'dossiers/:id/modifier', component: DossierFormComponent },

  // Rendez-vous ← specific routes BEFORE :id
  { path: 'rendezvous', component: RendezVousListComponent },
  { path: 'rendezvous/nouveau', component: RendezVousFormComponent },          // ← 1st
  { path: 'rendezvous/disponibilites', component: DisponibiliteFormComponent },// ← 2nd
  { path: 'rendezvous/:id', component: RendezVousFormComponent },              // ← 3rd (last!)

  // Utilisateurs
  { path: 'users', component: UserListComponent },

  { path: '**', redirectTo: '/dashboard' }
];