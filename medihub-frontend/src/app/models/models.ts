// ─── PHARMACIE ──────────────────────────────────────────────────────────────
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

// ─── DOSSIER MEDICAL ────────────────────────────────────────────────────────
export interface DossierMedical {
  id?: number;
  patientId: number;
  medecinId?: number;
  rendezVousId?: number;
  numeroDossier?: string;
  dateCreation?: string;
  dateMiseAJour?: string;
  groupeSanguin?: string;
  allergies?: string;
  antecedents?: string;
  statut?: string;
  ordonnances?: Ordonnance[];
  analyses?: Analyse[];
  traitements?: Traitement[];
}

export interface Ordonnance {
  id?: number;
  description: string;
  medicaments?: string;
  posologie?: string;
  datePrescription?: string;
  dateExpiration?: string;
  medecinPrescripteur?: string;
  statut?: string;
}

export interface Analyse {
  id?: number;
  typeAnalyse: string;
  description?: string;
  resultats?: string;
  valeursNormales?: string;
  dateDemande?: string;
  dateResultat?: string;
  laboratoire?: string;
  statut?: string;
}

export interface Traitement {
  id?: number;
  nom: string;
  description?: string;
  medicament?: string;
  posologie?: string;
  duree?: string;
  dateDebut?: string;
  dateFin?: string;
  statut?: string;
  observations?: string;
}

// ─── RENDEZ-VOUS ────────────────────────────────────────────────────────────
export interface RendezVous {
  id?: number;
  patientId: number;
  medecinId: number;
  dateRendezVous: string;
  heureRendezVous: string;
  motif?: string;
  notes?: string;
  statut?: string;
  createur?: string;
  dateCreation?: string;
}

export interface DisponibiliteMedecin {
  id?: number;
  medecinId: number;
  dateDisponibilite: string;
  heureDebut: string;
  heureFin: string;
  disponible?: boolean;
}

export interface HistoriqueRendezVous {
  id?: number;
  rendezVousId: number;
  action: string;
  ancienStatut?: string;
  nouveauStatut?: string;
  details?: string;
  effectuePar?: string;
  dateAction?: string;
}
