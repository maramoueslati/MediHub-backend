import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { DossierService } from '../../../services/dossier.service';
import { DossierMedical } from '../../../models/models';

@Component({
  selector: 'app-dossier-form',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './dossier-form.component.html',
  styleUrls: ['./dossier-form.component.css']
})
export class DossierFormComponent implements OnInit {
  dossier: DossierMedical = { patientId: 0, statut: 'ACTIF' };
  isEditing = false;
  loading = false;
  error = '';
  success = '';
  id: number | null = null;

  constructor(private route: ActivatedRoute, private router: Router, private service: DossierService) {}

  ngOnInit(): void {
    this.id = Number(this.route.snapshot.paramMap.get('id'));
    if (this.id) {
      this.isEditing = true;
      this.service.getById(this.id).subscribe({ next: (d) => this.dossier = d });
    }
  }

  soumettre(): void {
    this.loading = true;
    const action = this.isEditing ? this.service.update(this.id!, this.dossier) : this.service.create(this.dossier);
    action.subscribe({
      next: () => { this.success = this.isEditing ? 'Dossier modifié !' : 'Dossier créé !'; this.loading = false; setTimeout(() => this.router.navigate(['/dossiers']), 1500); },
      error: (err) => { this.error = err.error?.message || 'Erreur.'; this.loading = false; }
    });
  }
}
