package springvaadin.ui;

import org.springframework.beans.factory.annotation.Autowired;

import springvaadin.model.Person;
import springvaadin.repository.PersonRepository;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;

import org.vaadin.spring.VaadinUI;
import org.vaadin.teemu.clara.Clara;
import org.vaadin.teemu.clara.binder.annotation.UiDataSource;
import org.vaadin.teemu.clara.binder.annotation.UiField;

@VaadinUI
public class PersonUI extends UI {

	private static final long serialVersionUID = 1L;

	@Autowired
	private PersonRepository personRepository;
	
	@UiField("personTable")
    private Table personTable;
	
	@Override
	protected void init(VaadinRequest request) {
		setContent(Clara.create("PersonUI.xml", this));
		personTable.setVisibleColumns("id", "firstname", "lastname");
	}
	
	@UiDataSource("personTable")
	public BeanItemContainer<Person> createPersonDataSource() {
		return new BeanItemContainer<>(Person.class, personRepository.findAll());		 
	}

}
