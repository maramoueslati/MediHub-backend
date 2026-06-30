import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Medicament } from '../models/medicament.model';

@Injectable({
  providedIn: 'root'
})
export class MedicamentService {

  private apiUrl = '/api/medicaments';

  constructor(private http: HttpClient) {}

  getAll(): Observable<Medicament[]> {
    return this.http.get<Medicament[]>(this.apiUrl);
  }

  getById(id: number): Observable<Medicament> {
    return this.http.get<Medicament>(`${this.apiUrl}/${id}`);
  }

  create(medicament: Medicament): Observable<Medicament> {
    return this.http.post<Medicament>(this.apiUrl, medicament);
  }

  update(id: number, medicament: Medicament): Observable<Medicament> {
    return this.http.put<Medicament>(`${this.apiUrl}/${id}`, medicament);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  ajusterStock(id: number, delta: number): Observable<Medicament> {
    return this.http.patch<Medicament>(`${this.apiUrl}/${id}/stock`, null, {
      params: new HttpParams().set('delta', delta.toString())
    });
  }

  getDisponibles(): Observable<Medicament[]> {
    return this.http.get<Medicament[]>(`${this.apiUrl}/disponibles`);
  }

  getRuptureStock(): Observable<Medicament[]> {
    return this.http.get<Medicament[]>(`${this.apiUrl}/rupture-stock`);
  }

  rechercher(nom: string): Observable<Medicament[]> {
    return this.http.get<Medicament[]>(`${this.apiUrl}/recherche`, {
      params: new HttpParams().set('nom', nom)
    });
  }
}
