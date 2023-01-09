package monprojet.dao;

import monprojet.dto.PopPays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import monprojet.entity.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.jdbc.Sql;

@Log4j2 // Génère le 'logger' pour afficher les messages de trace
@DataJpaTest
public class CountryRepositoryTest {

    @Autowired
    private CountryRepository countryDAO;

    @Test
    void lesNomsDePaysSontTousDifferents() {
        log.info("On vérifie que les noms de pays sont tous différents ('unique') dans la table 'Country'");
        
        Country paysQuiExisteDeja = new Country("XX", "France");
        try {
            countryDAO.save(paysQuiExisteDeja); // On essaye d'enregistrer un pays dont le nom existe   

            fail("On doit avoir une violation de contrainte d'intégrité (unicité)");
        } catch (DataIntegrityViolationException e) {
            // Si on arrive ici c'est normal, l'exception attendue s'est produite
        }
    }

    @Test
    @Sql("test-data.sql") // On peut charger des donnnées spécifiques pour un test
    void onSaitCompterLesEnregistrements() {
        log.info("On compte les enregistrements de la table 'Country'");
        int combienDePaysDansLeJeuDeTest = 3 + 1; // 3 dans data.sql, 1 dans test-data.sql
        long nombre = countryDAO.count();
        assertEquals(combienDePaysDansLeJeuDeTest, nombre, "On doit trouver 4 pays" );
    }

    @Test
    @Sql("test-data.sql")
    void testPopParPays(){
        assertEquals(countryDAO.populationPourPays(1),12);
    }

    @Test
    @Sql("test-data.sql")
    void testPopPourTousLesPays(){
        PopPays testFr = new PopPays() {
            @Override
            public String getNom() {
                return "France";
            }

            @Override
            public Integer getPop() {
                return 12;
            }
        };
        PopPays testUk = new PopPays() {
            @Override
            public String getNom() {
                return "United Kingdom";
            }

            @Override
            public Integer getPop() {
                return 18;
            }
        };
        PopPays testUs = new PopPays() {
            @Override
            public String getNom() {
                return "United States of America";
            }

            @Override
            public Integer getPop() {
                return 27;
            }
        };
        assertEquals(testFr.getNom(), countryDAO.listPopPays().get(0).getNom());
        assertEquals(testFr.getPop(), countryDAO.listPopPays().get(0).getPop());

        assertEquals(testUk.getNom(), countryDAO.listPopPays().get(1).getNom());
        assertEquals(testUk.getPop(), countryDAO.listPopPays().get(1).getPop());

        assertEquals(testUs.getNom(), countryDAO.listPopPays().get(2).getNom());
        assertEquals(testUs.getPop(), countryDAO.listPopPays().get(2).getPop());
    }

}
