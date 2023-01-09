package monprojet.dao;

import java.util.List;

import monprojet.dto.PopPays;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import monprojet.entity.City;
import monprojet.entity.Country;

// This will be AUTO IMPLEMENTED by Spring 

public interface CountryRepository extends JpaRepository<Country, Integer> {

    @Query(value = "SELECT SUM(population) "
    + "FROM City "
    + "WHERE country_id = :id "
    + "GROUP BY country_id" ,
    nativeQuery = true)
    public int populationPourPays(Integer id);

    @Query(value = "SELECT co.name as nom, SUM(c.population) as pop "
            + "FROM City c "
            + "JOIN Country co ON co.id = c.country_id "
            + "GROUP BY country_id" ,
            nativeQuery = true)
    public List<PopPays> listPopPays();
}
