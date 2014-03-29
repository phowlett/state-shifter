package springvaadin.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import springvaadin.model.Pizza;

public interface PizzaRepository extends JpaRepository<Pizza, Long>{

}
