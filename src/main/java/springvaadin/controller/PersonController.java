package springvaadin.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import springvaadin.model.Person;
import springvaadin.repository.PersonRepository;

@RestController
public class PersonController {
	
	@Autowired
	private PersonRepository personRepository;
	
	@RequestMapping(value="/people", method=RequestMethod.GET)
	public List<Person> getAll() {
		return personRepository.findAll();
	}
}
