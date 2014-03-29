package springvaadin.ui;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.VaadinUI;
import org.vaadin.teemu.clara.Clara;
import org.vaadin.teemu.clara.binder.annotation.UiDataSource;
import org.vaadin.teemu.clara.binder.annotation.UiField;
import org.vaadin.teemu.clara.binder.annotation.UiHandler;

import springvaadin.model.Person;
import springvaadin.repository.PersonRepository;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;

@VaadinUI
public class PersonUI extends UI {

	private static final long serialVersionUID = 1L;
	
	private static final String[] fieldNames = new String[] { "firstname", "lastname" };

	@Autowired
	private PersonRepository personRepository;
	
	@UiField("personTable")
    private Table personTable;
	
	@UiField("editorLayout")
    private FormLayout editorLayout;
	
	private FieldGroup binder = new FieldGroup();
	
	@Override
	protected void init(VaadinRequest request) {
		setContent(Clara.create("PersonUI.xml", this));
		personTable.setVisibleColumns("id", "firstname", "lastname");
		
		for (Object propertyId : binder.getUnboundPropertyIds()) {
			editorLayout.addComponent(binder.buildAndBind(propertyId));
        }
//		
//		int index = 0;
//        for (String fieldName : fieldNames) {
//            TextField field = new TextField(fieldName);
//            editorLayout.addComponent(field, index++);
//            field.setWidth("100%");
//            binder.bind(field, fieldName);
//        }
//        binder.setBuffered(true);
	}
	
	@UiDataSource("personTable")
	public BeanItemContainer<Person> createPersonDataSource() {
		return new BeanItemContainer<>(Person.class, personRepository.findAll());		 
	}
	
	@UiHandler("personTable")
    public void onPersonSelected(ValueChangeEvent event) {
        Object personId = personTable.getValue();

        if (personId != null) {
            binder.setItemDataSource(personTable.getItem(personId));
        }
        editorLayout.setVisible(personId != null);
    }
	
	@UiHandler("submitButton")
	public void onSubmit(ClickEvent event) {
		try {
			binder.commit();
			@SuppressWarnings("unchecked")
			BeanItem<Person> personBeanItem = (BeanItem<Person>) personTable.getItem(personTable.getValue());
			personRepository.saveAndFlush(personBeanItem.getBean());
			Notification.show("Saved");
		} catch (CommitException e) {
			Notification.show("Error");
		}
	}

}