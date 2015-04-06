package stateshifter.ui;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.MongoRepository;

import stateshifter.model.Bug;
import stateshifter.repository.BugRepository;

import com.vaadin.annotations.DesignRoot;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.declarative.Design;

@DesignRoot
@SuppressWarnings("serial")
@SpringView(name = BugView.VIEW_NAME)
public class BugView extends EditView<Bug, String> {
	
	public static final String VIEW_NAME = "bug";
	
	@Autowired
	private BugRepository bugRepository;
		
	protected Table bugTable;
	protected FormLayout bugForm;
	protected Button addButton;
	protected Button deleteButton;
	protected Button submitButton;
	
	protected TextField bugDescription;
		
	@Override
	protected MongoRepository<Bug, String> getRepository() {
		return bugRepository;
	}
	
	@Override
	protected Class<Bug> getItemClass() {
		return Bug.class;
	}
	
	@Override
	protected Table getTable() {
		return bugTable;
	}

	@Override
	protected FormLayout getForm() {
		return bugForm;
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
		bugTable.setVisibleColumns("description");
		bugTable.setColumnHeaders("Description");
	}

	@Override
	protected void bindFormFields() {
		fieldGroup.bind(bugDescription, "description");
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
        editItem(new Bug("Description"));        
    }

	
}