package stateshifter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import stateshifter.model.Pizza;

@RepositoryRestResource(path="pizza")
public interface PizzaRepository extends JpaRepository<Pizza, Long>{

}
