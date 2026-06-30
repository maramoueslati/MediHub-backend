import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { DossierMedical, Ordonnance, Analyse, Traitement } from '../models/models';

@Injectable({ providedIn: 'root' })
export class DossierService {
  private api = '/api/dossiers';

  constructor(private http: HttpClient) {}

  // Dossiers
  getAll(): Observable<DossierMedical[]> { return this.http.get<DossierMedical[]>(this.api); }
  getById(id: number): Observable<DossierMedical> { return this.http.get<DossierMedical>(`${this.api}/${id}`); }
  getByPatient(patientId: number): Observable<DossierMedical[]> { return this.http.get<DossierMedical[]>(`${this.api}/patient/${patientId}`); }
  create(d: DossierMedical): Observable<DossierMedical> { return this.http.post<DossierMedical>(this.api, d); }
  update(id: number, d: DossierMedical): Observable<DossierMedical> { return this.http.put<DossierMedical>(`${this.api}/${id}`, d); }
  delete(id: number): Observable<void> { return this.http.delete<void>(`${this.api}/${id}`); }

  // Ordonnances
  getOrdonnances(dossierId: number): Observable<Ordonnance[]> { return this.http.get<Ordonnance[]>(`${this.api}/${dossierId}/ordonnances`); }
  addOrdonnance(dossierId: number, o: Ordonnance): Observable<Ordonnance> { return this.http.post<Ordonnance>(`${this.api}/${dossierId}/ordonnances`, o); }
  deleteOrdonnance(id: number): Observable<void> { return this.http.delete<void>(`${this.api}/ordonnances/${id}`); }

  // Analyses
  getAnalyses(dossierId: number): Observable<Analyse[]> { return this.http.get<Analyse[]>(`${this.api}/${dossierId}/analyses`); }
  addAnalyse(dossierId: number, a: Analyse): Observable<Analyse> { return this.http.post<Analyse>(`${this.api}/${dossierId}/analyses`, a); }
  deleteAnalyse(id: number): Observable<void> { return this.http.delete<void>(`${this.api}/analyses/${id}`); }

  // Traitements
  getTraitements(dossierId: number): Observable<Traitement[]> { return this.http.get<Traitement[]>(`${this.api}/${dossierId}/traitements`); }
  addTraitement(dossierId: number, t: Traitement): Observable<Traitement> { return this.http.post<Traitement>(`${this.api}/${dossierId}/traitements`, t); }
  deleteTraitement(id: number): Observable<void> { return this.http.delete<void>(`${this.api}/traitements/${id}`); }
}
