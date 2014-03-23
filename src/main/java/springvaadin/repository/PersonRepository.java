package springvaadin.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import springvaadin.model.Person;

public interface PersonRepository extends JpaRepository<Person, Long>{

}
