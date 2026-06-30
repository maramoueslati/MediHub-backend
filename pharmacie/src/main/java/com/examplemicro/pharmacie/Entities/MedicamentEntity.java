package com.examplemicro.pharmacie.Entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
        import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

        import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "medicaments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MedicamentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le nom du médicament est obligatoire")
    @Column(nullable = false)
    private String nom;

    private String description;

    private String categorie;

    private String fabricant;

    @NotNull(message = "Le prix est obligatoire")
    @PositiveOrZero(message = "Le prix ne peut pas être négatif")
    private Double prix;

    @NotNull(message = "La quantité en stock est obligatoire")
    @PositiveOrZero(message = "La quantité ne peut pas être négative")
    @Column(name = "quantite_stock")
    private Integer quantiteStock;

    @Column(name = "seuil_alerte")
    private Integer seuilAlerte = 10;

    @Column(name = "date_expiration")
    private LocalDate dateExpiration;

    @Column(name = "date_ajout", updatable = false)
    private LocalDateTime dateAjout;

    @PrePersist
    protected void onCreate() {
        this.dateAjout = LocalDateTime.now();
        if (this.seuilAlerte == null) this.seuilAlerte = 10;
    }

    @JsonProperty("disponible")
    public boolean isDisponible() {
        return quantiteStock != null && quantiteStock > 0;
    }
}