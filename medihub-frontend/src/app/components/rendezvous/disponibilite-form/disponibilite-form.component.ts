import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { RendezVousService } from '../../../services/rendezvous.service';
import { DisponibiliteMedecin } from '../../../models/models';

@Component({
  selector: 'app-disponibilite-form',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './disponibilite-form.component.html',
  styleUrls: ['./disponibilite-form.component.css']
})
export class DisponibiliteFormComponent {
  dispo: DisponibiliteMedecin = { medecinId: 0, dateDisponibilite: '', heureDebut: '', heureFin: '', disponible: true };
  disponibilites: DisponibiliteMedecin[] = [];
  searchDate = '';
  success = '';

  constructor(private service: RendezVousService) {}

  ajouter(): void {
    if (this.dispo.heureDebut && !this.dispo.heureDebut.includes(':00', 4)) this.dispo.heureDebut += ':00';
    if (this.dispo.heureFin && !this.dispo.heureFin.includes(':00', 4)) this.dispo.heureFin += ':00';
    this.service.addDisponibilite(this.dispo).subscribe({
      next: (d) => {
        this.disponibilites.push(d);
        this.success = 'Disponibilité ajoutée !';
        this.dispo = { medecinId: 0, dateDisponibilite: '', heureDebut: '', heureFin: '', disponible: true };
        setTimeout(() => this.success = '', 3000);
      }
    });
  }

  rechercherParDate(): void {
    if (!this.searchDate) return;
    this.service.getDisponibilitesDate(this.searchDate).subscribe({ next: (data) => this.disponibilites = data });
  }

  supprimer(id: number): void {
    this.service.deleteDisponibilite(id).subscribe({ next: () => this.disponibilites = this.disponibilites.filter(d => d.id !== id) });
  }
}
