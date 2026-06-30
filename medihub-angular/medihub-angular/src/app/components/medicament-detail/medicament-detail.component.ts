import { Component, OnInit } from '@angular/core';
import { CommonModule, CurrencyPipe, DatePipe } from '@angular/common';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { MedicamentService } from '../../services/medicament.service';
import { Medicament } from '../../models/medicament.model';

@Component({
  selector: 'app-medicament-detail',
  standalone: true,
  imports: [CommonModule, RouterLink, CurrencyPipe, DatePipe],
  templateUrl: './medicament-detail.component.html',
  styleUrls: ['./medicament-detail.component.css']
})
export class MedicamentDetailComponent implements OnInit {
  medicament: Medicament | null = null;
  loading = true;
  showModal = false;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private medicamentService: MedicamentService
  ) {}

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.medicamentService.getById(id).subscribe({
      next: (data) => { this.medicament = data; this.loading = false; },
      error: () => { this.loading = false; this.router.navigate(['/medicaments']); }
    });
  }

  getStockClass(): string {
    if (!this.medicament) return '';
    if (this.medicament.quantiteStock === 0) return 'stock-zero';
    if (this.medicament.quantiteStock <= (this.medicament.seuilAlerte || 10)) return 'stock-low';
    return 'stock-ok';
  }

  confirmerSuppression(): void { this.showModal = true; }

  supprimer(): void {
    if (!this.medicament?.id) return;
    this.medicamentService.delete(this.medicament.id).subscribe({
      next: () => this.router.navigate(['/medicaments'])
    });
  }
}
