import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { RendezVous, DisponibiliteMedecin, HistoriqueRendezVous } from '../models/models';

@Injectable({ providedIn: 'root' })
export class RendezVousService {
  private api = '/api/rendezvous';

  constructor(private http: HttpClient) {}

  getAll(): Observable<RendezVous[]> { return this.http.get<RendezVous[]>(this.api); }
  getById(id: number): Observable<RendezVous> { return this.http.get<RendezVous>(`${this.api}/${id}`); }
  getByPatient(patientId: number): Observable<RendezVous[]> { return this.http.get<RendezVous[]>(`${this.api}/patient/${patientId}`); }
  getByMedecin(medecinId: number): Observable<RendezVous[]> { return this.http.get<RendezVous[]>(`${this.api}/medecin/${medecinId}`); }
  getProchainsByMedecin(medecinId: number): Observable<RendezVous[]> { return this.http.get<RendezVous[]>(`${this.api}/medecin/${medecinId}/prochains`); }
  getByDate(date: string): Observable<RendezVous[]> { return this.http.get<RendezVous[]>(`${this.api}/date/${date}`); }
  create(r: RendezVous): Observable<RendezVous> { return this.http.post<RendezVous>(this.api, r); }
  update(id: number, r: RendezVous): Observable<RendezVous> { return this.http.put<RendezVous>(`${this.api}/${id}`, r); }
  delete(id: number): Observable<void> { return this.http.delete<void>(`${this.api}/${id}`); }
  changerStatut(id: number, statut: string, acteur: string): Observable<RendezVous> {
    return this.http.patch<RendezVous>(`${this.api}/${id}/statut`, null, {
      params: new HttpParams().set('statut', statut).set('acteur', acteur)
    });
  }
  getHistorique(id: number): Observable<HistoriqueRendezVous[]> { return this.http.get<HistoriqueRendezVous[]>(`${this.api}/${id}/historique`); }

  // Disponibilités
  getDisponibilitesMedecin(medecinId: number): Observable<DisponibiliteMedecin[]> { return this.http.get<DisponibiliteMedecin[]>(`${this.api}/disponibilites/medecin/${medecinId}`); }
  getDisponibilitesDate(date: string): Observable<DisponibiliteMedecin[]> { return this.http.get<DisponibiliteMedecin[]>(`${this.api}/disponibilites/date/${date}`); }
  addDisponibilite(d: DisponibiliteMedecin): Observable<DisponibiliteMedecin> { return this.http.post<DisponibiliteMedecin>(`${this.api}/disponibilites`, d); }
  deleteDisponibilite(id: number): Observable<void> { return this.http.delete<void>(`${this.api}/disponibilites/${id}`); }
}
