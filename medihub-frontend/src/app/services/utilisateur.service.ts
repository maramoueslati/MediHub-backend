import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Utilisateur {
  id?: number;
  nom: string;
  prenom: string;
  email: string;
  telephone?: string;
  role: string;
  dateNaissance?: string;
  adresse?: string;
  specialite?: string;
  actif?: boolean;
  keycloakId?: string;
}

export interface CreateUtilisateurRequest {
  nom: string;
  prenom: string;
  email: string;
  password: string;        // ← new required field
  role: string;
  telephone?: string;
  adresse?: string;
  specialite?: string;
  numeroLicence?: string;
}

@Injectable({ providedIn: 'root' })
export class UtilisateurService {
  private api = '/api/users';

  constructor(private http: HttpClient) {}

  getAll(): Observable<Utilisateur[]> {
    return this.http.get<Utilisateur[]>(this.api);
  }

  getById(id: number): Observable<Utilisateur> {
    return this.http.get<Utilisateur>(`${this.api}/${id}`);
  }

  getByRole(role: string): Observable<Utilisateur[]> {
    return this.http.get<Utilisateur[]>(`${this.api}/role/${role}`);
  }

  create(request: CreateUtilisateurRequest): Observable<Utilisateur> {
    return this.http.post<Utilisateur>(this.api, request);
  }

  update(id: number, u: Utilisateur): Observable<Utilisateur> {
    return this.http.put<Utilisateur>(`${this.api}/${id}`, u);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.api}/${id}`);
  }

  desactiver(id: number): Observable<void> {
    return this.http.patch<void>(`${this.api}/${id}/desactiver`, null);
  }
}