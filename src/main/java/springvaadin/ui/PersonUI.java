package springvaadin.ui;

import org.springframework.beans.factory.annotation.Autowired;

import springvaadin.model.Person;
import springvaadin.repository.PersonRepository;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;

import org.vaadin.spring.VaadinUI;

@VaadinUI
public class PersonUI extends UI {

	@Autowired
	private PersonRepository personRepository;
	
	@Override
	protected void init(VaadinRequest request) {
		BeanItemContainer<Person> personBeanItemContainer = new BeanItemContainer<>(Person.class, personRepository.findAll());
		
		Table personTable = new Table("Person Table");
		personTable.setContainerDataSource(personBeanItemContainer);
		personTable.setVisibleColumns("id", "firstname", "lastname");
		personTable.setSizeFull();
		setContent(personTable);
	}

}
