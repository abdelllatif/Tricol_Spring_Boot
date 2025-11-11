package com.tricol.Tricol.integration;

import com.tricol.Tricol.model.Fournisseur;
import com.tricol.Tricol.repository.FournisseurRepository;
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
public class FournisseurRepositoryIntegrationTest {

    @Autowired
    private FournisseurRepository fournisseurRepository;

    @Test
    void testSaveFournisseur() {
        Fournisseur fournisseur = new Fournisseur();
        fournisseur.setSociete("Tricol SARL");
        fournisseur.setAdresse("Casablanca");

        Fournisseur saved = fournisseurRepository.save(fournisseur);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getSociete()).isEqualTo("Tricol SARL");
        assertThat(saved.getAdresse()).isEqualTo("Casablanca");
    }
}
