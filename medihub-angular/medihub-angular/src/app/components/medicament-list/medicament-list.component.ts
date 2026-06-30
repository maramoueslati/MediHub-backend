import { Component, OnInit } from '@angular/core';
import { CommonModule, CurrencyPipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { MedicamentService } from '../../services/medicament.service';
import { Medicament } from '../../models/medicament.model';

@Component({
  selector: 'app-medicament-list',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink, CurrencyPipe],
  templateUrl: './medicament-list.component.html',
  styleUrls: ['./medicament-list.component.css']
})
export class MedicamentListComponent implements OnInit {

  medicaments: Medicament[] = [];
  filteredMedicaments: Medicament[] = [];
  searchTerm = '';
  filter = 'tous';
  loading = false;
  error = '';
  medicamentASupprimer: Medicament | null = null;

  get disponiblesCount() {
    return this.medicaments.filter(m => m.disponible).length;
  }

  get ruptureCount() {
    return this.medicaments.filter(m => !m.disponible).length;
  }

  constructor(private medicamentService: MedicamentService) {}

  ngOnInit(): void {
    this.charger();
  }

  charger(): void {
    this.loading = true;
    this.error = '';
    this.medicamentService.getAll().subscribe({
      next: (data) => {
        this.medicaments = data;
        this.appliquerFiltres();
        this.loading = false;
      },
      error: () => {
        this.error = 'Impossible de charger les médicaments. Vérifiez que le serveur est démarré.';
        this.loading = false;
      }
    });
  }

  onSearch(): void {
    this.appliquerFiltres();
  }

  setFilter(f: string): void {
    this.filter = f;
    this.appliquerFiltres();
  }

  appliquerFiltres(): void {
    let result = [...this.medicaments];
    if (this.searchTerm.trim()) {
      const term = this.searchTerm.toLowerCase();
      result = result.filter(m =>
        m.nom.toLowerCase().includes(term) ||
        (m.categorie && m.categorie.toLowerCase().includes(term)) ||
        (m.fabricant && m.fabricant.toLowerCase().includes(term))
      );
    }
    if (this.filter === 'disponibles') result = result.filter(m => m.disponible);
    if (this.filter === 'rupture') result = result.filter(m => !m.disponible);
    this.filteredMedicaments = result;
  }

  getStockClass(med: Medicament): string {
    if (med.quantiteStock === 0) return 'stock-zero';
    if (med.quantiteStock <= (med.seuilAlerte || 10)) return 'stock-low';
    return 'stock-ok';
  }

  ajusterStock(med: Medicament, delta: number): void {
    if (!med.id) return;
    this.medicamentService.ajusterStock(med.id, delta).subscribe({
      next: (updated) => {
        const index = this.medicaments.findIndex(m => m.id === med.id);
        if (index !== -1) {
          this.medicaments[index] = updated;
          this.appliquerFiltres();
        }
      },
      error: (err) => {
        this.error = err.error?.message || 'Stock insuffisant';
        setTimeout(() => this.error = '', 3000);
      }
    });
  }

  confirmerSuppression(med: Medicament): void {
    this.medicamentASupprimer = med;
  }

  supprimer(): void {
    if (!this.medicamentASupprimer?.id) return;
    this.medicamentService.delete(this.medicamentASupprimer.id).subscribe({
      next: () => {
        this.medicaments = this.medicaments.filter(m => m.id !== this.medicamentASupprimer!.id);
        this.appliquerFiltres();
        this.medicamentASupprimer = null;
      },
      error: () => {
        this.error = 'Erreur lors de la suppression.';
        this.medicamentASupprimer = null;
      }
    });
  }
}
