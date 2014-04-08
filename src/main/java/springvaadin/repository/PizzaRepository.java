package springvaadin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import springvaadin.model.Pizza;

@RepositoryRestResource(path="pizza")
public interface PizzaRepository extends JpaRepository<Pizza, Long>{

}
