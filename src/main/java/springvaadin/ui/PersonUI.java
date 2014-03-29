package springvaadin.ui;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.VaadinUI;
import org.vaadin.teemu.clara.Clara;
import org.vaadin.teemu.clara.binder.annotation.UiDataSource;
import org.vaadin.teemu.clara.binder.annotation.UiField;
import org.vaadin.teemu.clara.binder.annotation.UiHandler;

import springvaadin.model.Person;
import springvaadin.model.Pizza;
import springvaadin.repository.PersonRepository;
import springvaadin.repository.PizzaRepository;

import com.vaadin.annotations.Theme;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;

@VaadinUI
@Theme("reindeer")
public class PersonUI extends UI {

	private static final long serialVersionUID = 1L;
	
	@Autowired
	private PersonRepository personRepository;
	
	@Autowired
	private PizzaRepository pizzaRepository;
	
	@UiField("personTable")
    private Table personTable;
	
	@UiField("personEditor")
    private FormLayout personEditor;
	
	private BeanFieldGroup<Person> personFieldGroup = new BeanFieldGroup<Person>(Person.class);
	private BeanItemContainer<Person> personContainer;
	
	@Override
	protected void init(VaadinRequest request) {
		setContent(Clara.create("PersonUI.xml", this));
		personTable.setVisibleColumns("id", "firstname", "lastname", "pizza");
		
		personEditor.addComponent(personFieldGroup.buildAndBind("First Name", "firstname"), 0);
		personEditor.addComponent(personFieldGroup.buildAndBind("Last Name", "lastname"), 1);
		
		ComboBox selectPizza = new ComboBox("Pizza", new BeanItemContainer<>(Pizza.class, pizzaRepository.findAll()));
		selectPizza.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		selectPizza.setItemCaptionPropertyId("description");
		
		personFieldGroup.bind(selectPizza, "pizza");
		personEditor.addComponent(selectPizza, 2);
		
        personFieldGroup.setBuffered(true);
	}
	
	@UiDataSource("personTable")
	public BeanItemContainer<Person> createPersonDataSource() {
		personContainer = new BeanItemContainer<>(Person.class, personRepository.findAll()); 
		return personContainer;
	}
	
	@UiHandler("personTable")
    public void onPersonSelected(ValueChangeEvent event) {
        Object personId = personTable.getValue();

        if (personId != null) {
            personFieldGroup.setItemDataSource(personTable.getItem(personId));
        }
        personEditor.setVisible(personId != null);
    }
	
	@UiHandler("submitButton")
	public void onSubmit(ClickEvent event) {
		try {
			personFieldGroup.commit();
			BeanItem<Person> personBeanItem = personContainer.getItem(personTable.getValue());
			personRepository.saveAndFlush(personBeanItem.getBean());
			Notification.show("Saved");
		} catch (CommitException e) {
			Notification.show("Error");
		}
	}

}