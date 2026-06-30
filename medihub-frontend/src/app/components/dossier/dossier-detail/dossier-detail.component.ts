import { Component, OnInit } from '@angular/core';
import { CommonModule, DatePipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { DossierService } from '../../../services/dossier.service';
import { DossierMedical, Ordonnance, Analyse, Traitement } from '../../../models/models';

@Component({
  selector: 'app-dossier-detail',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink, DatePipe],
  templateUrl: './dossier-detail.component.html',
  styleUrls: ['./dossier-detail.component.css']
})
export class DossierDetailComponent implements OnInit {
  dossier: DossierMedical | null = null;
  ordonnances: Ordonnance[] = [];
  analyses: Analyse[] = [];
  traitements: Traitement[] = [];
  activeTab = 'ordonnances';

  showOrdonnanceForm = false;
  showAnalyseForm = false;
  showTraitementForm = false;

  newOrdonnance: Ordonnance = { description: '' };
  newAnalyse: Analyse = { typeAnalyse: '' };
  newTraitement: Traitement = { nom: '' };

  constructor(private route: ActivatedRoute, private service: DossierService) {}

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.service.getById(id).subscribe({ next: (d) => { this.dossier = d; this.chargerDetails(id); } });
  }

  chargerDetails(id: number): void {
    this.service.getOrdonnances(id).subscribe({ next: (data) => this.ordonnances = data });
    this.service.getAnalyses(id).subscribe({ next: (data) => this.analyses = data });
    this.service.getTraitements(id).subscribe({ next: (data) => this.traitements = data });
  }

  ajouterOrdonnance(): void {
    if (!this.dossier?.id || !this.newOrdonnance.description) return;
    this.service.addOrdonnance(this.dossier.id, this.newOrdonnance).subscribe({
      next: (o) => { this.ordonnances.push(o); this.newOrdonnance = { description: '' }; this.showOrdonnanceForm = false; }
    });
  }

  supprimerOrdonnance(id: number): void {
    this.service.deleteOrdonnance(id).subscribe({ next: () => this.ordonnances = this.ordonnances.filter(o => o.id !== id) });
  }

  ajouterAnalyse(): void {
    if (!this.dossier?.id || !this.newAnalyse.typeAnalyse) return;
    this.service.addAnalyse(this.dossier.id, this.newAnalyse).subscribe({
      next: (a) => { this.analyses.push(a); this.newAnalyse = { typeAnalyse: '' }; this.showAnalyseForm = false; }
    });
  }

  supprimerAnalyse(id: number): void {
    this.service.deleteAnalyse(id).subscribe({ next: () => this.analyses = this.analyses.filter(a => a.id !== id) });
  }

  ajouterTraitement(): void {
    if (!this.dossier?.id || !this.newTraitement.nom) return;
    this.service.addTraitement(this.dossier.id, this.newTraitement).subscribe({
      next: (t) => { this.traitements.push(t); this.newTraitement = { nom: '' }; this.showTraitementForm = false; }
    });
  }

  supprimerTraitement(id: number): void {
    this.service.deleteTraitement(id).subscribe({ next: () => this.traitements = this.traitements.filter(t => t.id !== id) });
  }
}
