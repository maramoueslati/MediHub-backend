import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { MedicamentService } from '../../services/medicament.service';
import { Medicament } from '../../models/medicament.model';

@Component({
  selector: 'app-medicament-form',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './medicament-form.component.html',
  styleUrls: ['./medicament-form.component.css']
})
export class MedicamentFormComponent implements OnInit {

  medicament: Medicament = {
    nom: '',
    prix: 0,
    quantiteStock: 0,
    seuilAlerte: 10
  };

  isEditing = false;
  loading = false;
  error = '';
  success = '';
  id: number | null = null;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private medicamentService: MedicamentService
  ) {}

  ngOnInit(): void {
    this.id = Number(this.route.snapshot.paramMap.get('id'));
    if (this.id) {
      this.isEditing = true;
      this.medicamentService.getById(this.id).subscribe({
        next: (data) => this.medicament = data,
        error: () => this.error = 'Médicament introuvable.'
      });
    }
  }

  soumettre(): void {
    this.loading = true;
    this.error = '';
    this.success = '';

    const action = this.isEditing
      ? this.medicamentService.update(this.id!, this.medicament)
      : this.medicamentService.create(this.medicament);

    action.subscribe({
      next: () => {
        this.success = this.isEditing
          ? 'Médicament modifié avec succès !'
          : 'Médicament créé avec succès !';
        this.loading = false;
        setTimeout(() => this.router.navigate(['/medicaments']), 1500);
      },
      error: (err) => {
        this.error = err.error?.message || 'Une erreur est survenue.';
        this.loading = false;
      }
    });
  }
}
