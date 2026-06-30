import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { UtilisateurService, Utilisateur, CreateUtilisateurRequest } from '../../../services/utilisateur.service';

@Component({
  selector: 'app-user-list',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.css']
})
export class UserListComponent implements OnInit {
  utilisateurs: Utilisateur[] = [];
  filteredUsers: Utilisateur[] = [];
  filter = 'tous';
  loading = false;
  error = '';
  success = '';
  showForm = false;
  userASupprimer: Utilisateur | null = null;

  newUser: CreateUtilisateurRequest = {
    nom: '', prenom: '', email: '', password: '', role: 'PATIENT'
  };

  constructor(private service: UtilisateurService) {}

  ngOnInit(): void { this.charger(); }

  charger(): void {
    this.loading = true;
    this.service.getAll().subscribe({
      next: (data) => {
        this.utilisateurs = data;
        this.appliquerFiltre();
        this.loading = false;
      },
      error: () => { this.error = 'Impossible de charger les utilisateurs.'; this.loading = false; }
    });
  }

  countByRole(role: string): number {
    return this.utilisateurs.filter(u => u.role === role).length;
  }

  setFilter(f: string): void { this.filter = f; this.appliquerFiltre(); }

  appliquerFiltre(): void {
    this.filteredUsers = this.filter === 'tous'
      ? [...this.utilisateurs]
      : this.utilisateurs.filter(u => u.role === this.filter);
  }

  creer(): void {
    this.loading = true;
    this.error = '';
    this.service.create(this.newUser).subscribe({
      next: (created) => {
        this.utilisateurs.push(created);
        this.appliquerFiltre();
        this.success = `${created.nom} ${created.prenom} créé avec succès dans MediHub et Keycloak!`;
        this.showForm = false;
        this.newUser = { nom: '', prenom: '', email: '', password: '', role: 'PATIENT' };
        this.loading = false;
        setTimeout(() => this.success = '', 4000);
      },
      error: (err) => {
        this.error = err.error?.message || 'Erreur lors de la création.';
        this.loading = false;
      }
    });
  }

  desactiver(u: Utilisateur): void {
    if (!u.id) return;
    this.service.desactiver(u.id).subscribe({
      next: () => { u.actif = false; }
    });
  }

  confirmer(u: Utilisateur): void { this.userASupprimer = u; }

  supprimer(): void {
    if (!this.userASupprimer?.id) return;
    this.service.delete(this.userASupprimer.id).subscribe({
      next: () => {
        this.utilisateurs = this.utilisateurs.filter(u => u.id !== this.userASupprimer!.id);
        this.appliquerFiltre();
        this.userASupprimer = null;
      }
    });
  }
}