package stateshifter.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import stateshifter.model.Pizza;

@RepositoryRestResource(path="pizza")
public interface PizzaRepository extends MongoRepository<Pizza, Long>{

}
