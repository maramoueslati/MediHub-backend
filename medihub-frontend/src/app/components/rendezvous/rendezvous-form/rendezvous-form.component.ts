import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { RendezVousService } from '../../../services/rendezvous.service';
import { RendezVous } from '../../../models/models';

@Component({
  selector: 'app-rendezvous-form',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './rendezvous-form.component.html',
  styleUrls: ['./rendezvous-form.component.css']
})
export class RendezVousFormComponent implements OnInit {
  rdv: RendezVous = { patientId: 0, medecinId: 0, dateRendezVous: '', heureRendezVous: '', createur: 'PATIENT' };
  isEditing = false;
  loading = false;
  error = '';
  success = '';
  id: number | null = null;

  constructor(private route: ActivatedRoute, private router: Router, private service: RendezVousService) {}

  ngOnInit(): void {
    this.id = Number(this.route.snapshot.paramMap.get('id'));
    if (this.id) { this.isEditing = true; this.service.getById(this.id).subscribe({ next: (r) => this.rdv = r }); }
  }

  soumettre(): void {
    this.loading = true;
    // Format time with seconds
    if (this.rdv.heureRendezVous && !this.rdv.heureRendezVous.includes(':00', 4)) {
      this.rdv.heureRendezVous = this.rdv.heureRendezVous + ':00';
    }
    const action = this.isEditing ? this.service.update(this.id!, this.rdv) : this.service.create(this.rdv);
    action.subscribe({
      next: () => { this.success = this.isEditing ? 'RDV modifié !' : 'RDV créé !'; this.loading = false; setTimeout(() => this.router.navigate(['/rendezvous']), 1500); },
      error: (err) => { this.error = err.error?.message || 'Erreur.'; this.loading = false; }
    });
  }
}
