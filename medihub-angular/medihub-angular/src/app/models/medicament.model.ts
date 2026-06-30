export interface Medicament {
  id?: number;
  nom: string;
  description?: string;
  categorie?: string;
  fabricant?: string;
  prix: number;
  quantiteStock: number;
  seuilAlerte?: number;
  dateExpiration?: string;
  dateAjout?: string;
  disponible?: boolean;
}
