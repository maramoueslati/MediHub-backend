import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { MedicamentService } from '../../services/medicament.service';
import { DossierService } from '../../services/dossier.service';
import { RendezVousService } from '../../services/rendezvous.service';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
  totalMedicaments = 0;
  ruptureCount = 0;
  totalDossiers = 0;
  totalRendezVous = 0;

  constructor(
    private medicamentService: MedicamentService,
    private dossierService: DossierService,
    private rendezVousService: RendezVousService
  ) {}

  ngOnInit(): void {
    this.medicamentService.getAll().subscribe({
      next: (data) => {
        this.totalMedicaments = data.length;
        this.ruptureCount = data.filter(m => !m.disponible).length;
      }
    });
    this.dossierService.getAll().subscribe({
      next: (data) => this.totalDossiers = data.length
    });
    this.rendezVousService.getAll().subscribe({
      next: (data) => this.totalRendezVous = data.length
    });
  }
}
