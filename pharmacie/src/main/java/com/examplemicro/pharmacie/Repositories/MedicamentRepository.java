package com.examplemicro.pharmacie.Repositories;

import com.examplemicro.pharmacie.Entities.MedicamentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MedicamentRepository extends JpaRepository<MedicamentEntity, Long> {

    List<MedicamentEntity> findByQuantiteStockGreaterThan(int quantite);

    List<MedicamentEntity> findByNomContainingIgnoreCase(String nom);

    List<MedicamentEntity> findByCategorieIgnoreCase(String categorie);

    @Query("SELECT m FROM MedicamentEntity m WHERE m.quantiteStock <= m.seuilAlerte")
    List<MedicamentEntity> findMedicamentsEnRupture();
}