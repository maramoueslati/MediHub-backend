import { Component, OnInit } from '@angular/core';
import { CommonModule, DatePipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { DossierService } from '../../../services/dossier.service';
import { DossierMedical } from '../../../models/models';

@Component({
  selector: 'app-dossier-list',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink, DatePipe],
  templateUrl: './dossier-list.component.html',
  styleUrls: ['./dossier-list.component.css']
})
export class DossierListComponent implements OnInit {
  dossiers: DossierMedical[] = [];
  filteredDossiers: DossierMedical[] = [];
  searchPatientId = '';
  loading = false;
  error = '';
  dossierASupprimer: DossierMedical | null = null;

  constructor(private dossierService: DossierService) {}

  ngOnInit(): void { this.charger(); }

  charger(): void {
    this.loading = true;
    this.dossierService.getAll().subscribe({
      next: (data) => { this.dossiers = data; this.filteredDossiers = data; this.loading = false; },
      error: () => { this.error = 'Impossible de charger les dossiers.'; this.loading = false; }
    });
  }

  onSearch(): void {
    const term = this.searchPatientId.trim();
    this.filteredDossiers = term
      ? this.dossiers.filter(d => d.patientId.toString().includes(term))
      : this.dossiers;
  }

  confirmer(d: DossierMedical): void { this.dossierASupprimer = d; }

  supprimer(): void {
    if (!this.dossierASupprimer?.id) return;
    this.dossierService.delete(this.dossierASupprimer.id).subscribe({
      next: () => {
        this.dossiers = this.dossiers.filter(d => d.id !== this.dossierASupprimer!.id);
        this.filteredDossiers = this.filteredDossiers.filter(d => d.id !== this.dossierASupprimer!.id);
        this.dossierASupprimer = null;
      }
    });
  }
}
