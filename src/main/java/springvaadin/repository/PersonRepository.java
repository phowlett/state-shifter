package springvaadin.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import springvaadin.model.Person;

@RepositoryRestResource(path="people")
public interface PersonRepository extends JpaRepository<Person, Long>{
	
	Collection<Person> findByLastname(@Param("lastname") String lastname);

}
