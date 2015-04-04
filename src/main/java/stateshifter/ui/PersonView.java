package stateshifter.ui;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import stateshifter.model.Person;
import stateshifter.model.Pizza;
import stateshifter.repository.PersonRepository;
import stateshifter.repository.PizzaRepository;

import com.vaadin.annotations.DesignRoot;
import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.declarative.Design;

@DesignRoot
@SuppressWarnings("serial")
@SpringView(name = PersonView.VIEW_NAME)
public class PersonView extends HorizontalSplitPanel implements View {
	
	public static final String VIEW_NAME = "person";
	
	@Autowired
	private PersonRepository personRepository;
	
	@Autowired
	private PizzaRepository pizzaRepository;
		
	private BeanFieldGroup<Person> personFieldGroup;
	private BeanItemContainer<Person> personContainer;
	protected VerticalLayout verticalLayout;
	protected Button addButton;
	protected Table personTable;
	protected Button submitButton;
	protected FormLayout personEditor;
	protected TextField firstname;
	protected TextField lastname;
	protected ComboBox selectPizza;
	
	@PostConstruct
    void init() {
		Design.read(this);
		initTable();
		initEditor();
	}
	
	/**
	 * Initialise the table
	 */
	private void initTable() {
		personContainer = new BeanItemContainer<>(Person.class, personRepository.findAll());
		personTable.setContainerDataSource(personContainer);
		personTable.setSelectable(true);
		personTable.addValueChangeListener(event -> onPersonSelected(event));
		personTable.setVisibleColumns("firstname", "lastname", "pizza");
		personTable.setColumnHeaders("First Name", "Last Name", "Pizza");
		addButton.addClickListener(event -> onAddPerson(event));
	}

	/**
	 * Initialise the person editor and form fields
	 */
	private void initEditor() {
		// bind text fields for name
		personFieldGroup = new BeanFieldGroup<Person>(Person.class);
		personFieldGroup.bind(firstname, "firstname");
		personFieldGroup.bind(lastname, "lastname");
		
		// Combo box for pizza selection, backed by pizza repository
		selectPizza.setContainerDataSource(new BeanItemContainer<>(Pizza.class, pizzaRepository.findAll()));
		selectPizza.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		selectPizza.setItemCaptionPropertyId("description");
		personFieldGroup.bind(selectPizza, "pizza");
		
		// Buffered mode, so person will not be updated until submit and commit
        personFieldGroup.setBuffered(true);
		submitButton.addClickListener(event -> onSubmit(event));
	}
	
	/**
	 * Update the table data from the person repository
	 */
	private void updateTableData() {
		personContainer.removeAllItems();
		personContainer.addAll(personRepository.findAll());
	}
	
	/**
	 * Edit a person by binding the person item to the editor field group
	 * @param personItem
	 */
	private void editPerson(Item personItem) {
		personFieldGroup.setItemDataSource(personItem);
		personEditor.setVisible(personItem != null);
	}
	
	
	/**
	 * Show the editor when person is selected in the table
	 * @param event
	 */
	public void onPersonSelected(ValueChangeEvent event) {
        Object personId = personTable.getValue();
        if (personId != null) {
        	editPerson(personTable.getItem(personId));
        }        
    }
	
	/**
	 * Add a new person and show the editor when the add button is clicked
	 * @param event
	 */
	public void onAddPerson(ClickEvent event) {
        BeanItem<Person> personBean = new BeanItem<Person>(new Person("New", "Person"));
        editPerson(personBean);        
    }
	
	/**
	 * Save the person on editor form submission
	 * @param event
	 */
	public void onSubmit(ClickEvent event) {
		if (!personFieldGroup.isValid()) {
			Notification.show("Errors in form");
		} else {
			try {
				personFieldGroup.commit();
				personRepository.saveAndFlush(personFieldGroup.getItemDataSource().getBean());
				Notification.show("Saved");
				updateTableData();
			} catch (CommitException e) {
				Notification.show("Commit error");
			}
		}
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// the view is constructed in the init() method()
	}

}