package stateshifter.ui;

import org.springframework.beans.factory.annotation.Autowired;

import stateshifter.model.Person;
import stateshifter.model.Pizza;
import stateshifter.repository.PersonRepository;
import stateshifter.repository.PizzaRepository;

import com.vaadin.annotations.DesignRoot;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.declarative.Design;

@SpringUI
@Title("Pizza")
@Theme("stateshifter")
public class PersonUI extends UI {
	

	private static final long serialVersionUID = 1L;
	
	@Autowired
	private PersonRepository personRepository;
	
	@Autowired
	private PizzaRepository pizzaRepository;
		
	private BeanFieldGroup<Person> personFieldGroup;
	private BeanItemContainer<Person> personContainer;
	protected PersonDesign design;
	
	@DesignRoot
	@SuppressWarnings("serial")
	protected class PersonDesign extends HorizontalSplitPanel {
		VerticalLayout verticalLayout;
		Button addButton;
		Table personTable;
		Button submitButton;
		FormLayout personEditor;
		TextField firstname;
		TextField lastname;
		ComboBox selectPizza;
	}
	
	/* (non-Javadoc)
	 * @see com.vaadin.ui.UI#init(com.vaadin.server.VaadinRequest)
	 */
	@Override
	protected void init(VaadinRequest request) {
		design = new PersonDesign();
		Design.read(design);
		setContent(design);		
		initTable();
		initEditor();
	}
	
	/**
	 * Initialise the table
	 */
	private void initTable() {
		personContainer = new BeanItemContainer<>(Person.class, personRepository.findAll());
		design.personTable.setContainerDataSource(personContainer);
		design.personTable.setSelectable(true);
		design.personTable.addValueChangeListener(event -> onPersonSelected(event));
		design.personTable.setVisibleColumns("firstname", "lastname", "pizza");
		design.personTable.setColumnHeaders("First Name", "Last Name", "Pizza");
		design.addButton.addClickListener(event -> onAddPerson(event));
	}

	/**
	 * Initialise the person editor and form fields
	 */
	private void initEditor() {
		// bind text fields for name
		personFieldGroup = new BeanFieldGroup<Person>(Person.class);
		personFieldGroup.bind(design.firstname, "firstname");
		personFieldGroup.bind(design.lastname, "lastname");
		
		// Combo box for pizza selection, backed by pizza repository
		design.selectPizza.setContainerDataSource(new BeanItemContainer<>(Pizza.class, pizzaRepository.findAll()));
		design.selectPizza.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		design.selectPizza.setItemCaptionPropertyId("description");
		personFieldGroup.bind(design.selectPizza, "pizza");
		
		// Buffered mode, so person will not be updated until submit and commit
        personFieldGroup.setBuffered(true);
		design.submitButton.addClickListener(event -> onSubmit(event));
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
		design.personEditor.setVisible(personItem != null);
	}
	
	
	/**
	 * Show the editor when person is selected in the table
	 * @param event
	 */
	public void onPersonSelected(ValueChangeEvent event) {
        Object personId = design.personTable.getValue();
        if (personId != null) {
        	editPerson(design.personTable.getItem(personId));
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

}