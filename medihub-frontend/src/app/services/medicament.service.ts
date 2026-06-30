import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Medicament } from '../models/models';

@Injectable({ providedIn: 'root' })
export class MedicamentService {
  private api = '/api/medicaments';

  constructor(private http: HttpClient) {}

  getAll(): Observable<Medicament[]> { return this.http.get<Medicament[]>(this.api); }
  getById(id: number): Observable<Medicament> { return this.http.get<Medicament>(`${this.api}/${id}`); }
  create(m: Medicament): Observable<Medicament> { return this.http.post<Medicament>(this.api, m); }
  update(id: number, m: Medicament): Observable<Medicament> { return this.http.put<Medicament>(`${this.api}/${id}`, m); }
  delete(id: number): Observable<void> { return this.http.delete<void>(`${this.api}/${id}`); }
  ajusterStock(id: number, delta: number): Observable<Medicament> {
    return this.http.patch<Medicament>(`${this.api}/${id}/stock`, null, { params: new HttpParams().set('delta', delta) });
  }
  getDisponibles(): Observable<Medicament[]> { return this.http.get<Medicament[]>(`${this.api}/disponibles`); }
  getRuptureStock(): Observable<Medicament[]> { return this.http.get<Medicament[]>(`${this.api}/rupture-stock`); }
  rechercher(nom: string): Observable<Medicament[]> {
    return this.http.get<Medicament[]>(`${this.api}/recherche`, { params: new HttpParams().set('nom', nom) });
  }
}
