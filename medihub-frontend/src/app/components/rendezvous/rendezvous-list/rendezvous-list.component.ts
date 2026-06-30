import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { RendezVousService } from '../../../services/rendezvous.service';
import { RendezVous } from '../../../models/models';

@Component({
  selector: 'app-rendezvous-list',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './rendezvous-list.component.html',
  styleUrls: ['./rendezvous-list.component.css']
})
export class RendezVousListComponent implements OnInit {
  rendezvous: RendezVous[] = [];
  filteredRdv: RendezVous[] = [];
  filter = 'tous';
  loading = false;
  error = '';
  rdvASupprimer: RendezVous | null = null;

  get totalCount() { return this.rendezvous.length; }
  get confirmeCount() { return this.rendezvous.filter(r => r.statut === 'CONFIRME').length; }
  get planifieCount() { return this.rendezvous.filter(r => r.statut === 'PLANIFIE').length; }
  get annuleCount() { return this.rendezvous.filter(r => r.statut === 'ANNULE').length; }

  constructor(private service: RendezVousService) {}

  ngOnInit(): void { this.charger(); }

  charger(): void {
    this.loading = true;
    this.service.getAll().subscribe({
      next: (data) => {
        this.rendezvous = data;
        this.appliquerFiltre();
        this.loading = false;
      },
      error: () => {
        this.error = 'Impossible de charger les rendez-vous.';
        this.loading = false;
      }
    });
  }

  setFilter(f: string): void { this.filter = f; this.appliquerFiltre(); }

  appliquerFiltre(): void {
    this.filteredRdv = this.filter === 'tous'
      ? [...this.rendezvous]
      : this.rendezvous.filter(r => r.statut === this.filter);
  }

  getStatutClass(statut: string): string {
    const map: Record<string, string> = {
      'PLANIFIE': 'status-pending',
      'CONFIRME': 'status-ok',
      'ANNULE': 'status-cancelled',
      'TERMINE': 'status-done',
      'REPORTE': 'status-report'
    };
    return map[statut] || '';
  }

  // MEDECIN only
  confirmerRdv(r: RendezVous): void {
    this.service.changerStatut(r.id!, 'CONFIRME', 'MEDECIN').subscribe({
      next: (updated) => {
        const i = this.rendezvous.findIndex(x => x.id === r.id);
        if (i !== -1) this.rendezvous[i] = updated;
        this.appliquerFiltre();
      }
    });
  }

  // MEDECIN only
  terminerRdv(r: RendezVous): void {
    this.service.changerStatut(r.id!, 'TERMINE', 'MEDECIN').subscribe({
      next: (updated) => {
        const i = this.rendezvous.findIndex(x => x.id === r.id);
        if (i !== -1) this.rendezvous[i] = updated;
        this.appliquerFiltre();
      }
    });
  }

  // PATIENT or MEDECIN
  annulerRdv(r: RendezVous): void {
    const acteur = prompt('Annulé par? (PATIENT / MEDECIN)') || 'PATIENT';
    this.service.changerStatut(r.id!, 'ANNULE', acteur).subscribe({
      next: (updated) => {
        const i = this.rendezvous.findIndex(x => x.id === r.id);
        if (i !== -1) this.rendezvous[i] = updated;
        this.appliquerFiltre();
      }
    });
  }

  // PATIENT or MEDECIN
  reporterRdv(r: RendezVous): void {
    const acteur = prompt('Reporté par? (PATIENT / MEDECIN)') || 'PATIENT';
    this.service.changerStatut(r.id!, 'REPORTE', acteur).subscribe({
      next: (updated) => {
        const i = this.rendezvous.findIndex(x => x.id === r.id);
        if (i !== -1) this.rendezvous[i] = updated;
        this.appliquerFiltre();
      }
    });
  }

  confirmerSuppression(r: RendezVous): void { this.rdvASupprimer = r; }

  supprimer(): void {
    if (!this.rdvASupprimer?.id) return;
    this.service.delete(this.rdvASupprimer.id).subscribe({
      next: () => {
        this.rendezvous = this.rendezvous.filter(r => r.id !== this.rdvASupprimer!.id);
        this.appliquerFiltre();
        this.rdvASupprimer = null;
      }
    });
  }
}