package stateshifter.ui;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.MongoRepository;

import stateshifter.model.Person;
import stateshifter.model.Pizza;
import stateshifter.repository.PersonRepository;
import stateshifter.repository.PizzaRepository;

import com.vaadin.annotations.DesignRoot;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.declarative.Design;

@DesignRoot
@SuppressWarnings("serial")
@SpringView(name = PersonView.VIEW_NAME)
public class PersonView extends EditView<Person, String> {
	
	public static final String VIEW_NAME = "person";
	
	@Autowired
	private PersonRepository personRepository;
	
	@Autowired
	private PizzaRepository pizzaRepository;
		
	protected Table personTable;
	protected FormLayout personForm;
	protected Button addButton;
	protected Button deleteButton;
	protected Button submitButton;
	
	protected TextField firstname;
	protected TextField lastname;
	protected ComboBox selectPizza;
	
	@Override
	protected MongoRepository<Person, String> getRepository() {
		return personRepository;
	}
	
	@Override
	protected Class<Person> getItemClass() {
		return Person.class;
	}
	
	@Override
	protected Table getTable() {
		return personTable;
	}

	@Override
	protected FormLayout getForm() {
		return personForm;
	}

	@Override
	protected Button getAddButton() {
		return addButton;
	}

	@Override
	protected Button getDeleteButton() {
		return deleteButton;
	}

	@Override
	protected Button getSubmitButton() {
		return submitButton;
	}
	
	@Override
	protected void initTableColumns() {
		personTable.setVisibleColumns("firstname", "lastname", "pizza");
		personTable.setColumnHeaders("First Name", "Last Name", "Pizza");
	}

	@Override
	protected void bindFormFields() {
		// bind text fields for name
		fieldGroup.bind(firstname, "firstname");
		fieldGroup.bind(lastname, "lastname");
		
		// Combo box for pizza selection, backed by pizza repository
		selectPizza.setContainerDataSource(new BeanItemContainer<>(Pizza.class, pizzaRepository.findAll()));
		selectPizza.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		selectPizza.setItemCaptionPropertyId("description");
		fieldGroup.bind(selectPizza, "pizza");
	}

	@PostConstruct
	@Override
	public void init() {
		Design.read(this);
		super.init();
	}
		
	/**
	 * Add a new person and show the editor when the add button is clicked
	 * @param event
	 */
	@Override
	public void onAddItem(ClickEvent event) {
        editItem(new Person("New", "Person"));        
    }

	
}