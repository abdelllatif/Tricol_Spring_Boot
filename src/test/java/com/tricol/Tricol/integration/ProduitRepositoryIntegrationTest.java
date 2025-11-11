package com.tricol.Tricol.integration;

import com.tricol.Tricol.model.Produit;
import com.tricol.Tricol.repository.ProduitRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@ActiveProfiles("test")

public class ProduitRepositoryIntegrationTest {

    @Autowired
    private ProduitRepository produitRepository;

    @Test
    void testSaveProduit() {
        Produit produit = new Produit();
        produit.setNom("Chemise Pro");
        produit.setDescription("Chemise pour travail");
        produit.setPrixUnitaire(150.0);
        produit.setStockActuel(100);

        Produit saved = produitRepository.save(produit);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getNom()).isEqualTo("Chemise Pro");
        assertThat(saved.getPrixUnitaire()).isEqualTo(150.0);
        assertThat(saved.getStockActuel()).isEqualTo(100);
    }
}
