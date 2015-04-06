package stateshifter.ui;

import java.io.Serializable;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;

@SuppressWarnings("serial")
public abstract class EditView<T,ID extends Serializable> extends HorizontalSplitPanel implements View {

	protected BeanFieldGroup<T> fieldGroup;
	protected BeanItemContainer<T> itemContainer;
	
	protected abstract MongoRepository<T,ID> getRepository();
	
	protected abstract Class<T> getItemClass();
	
	protected abstract Table getTable();
	
	protected abstract FormLayout getForm();
	
	protected abstract Button getAddButton();
	
	protected abstract Button getDeleteButton(); 
	
	protected abstract Button getSubmitButton();
	
	protected abstract void initTableColumns();
	
	protected abstract void bindFormFields();
	
	public void init() {
		itemContainer = new BeanItemContainer<>(getItemClass(), getRepository().findAll());
		Table table = getTable();
		table.setContainerDataSource(itemContainer);
		table.setSelectable(true);
		table.addValueChangeListener(event -> onItemSelected(event));
		initTableColumns();
		getAddButton().addClickListener(event -> onAddItem(event));
		getDeleteButton().addClickListener(event -> onDeleteItem(event));
		getDeleteButton().setEnabled(false);
		
		getForm().setVisible(false);
		// bind text fields
		fieldGroup = new BeanFieldGroup<T>(getItemClass());
		// Buffered mode, so item will not be updated until submit and commit
		fieldGroup.setBuffered(true);
		bindFormFields();
		getSubmitButton().addClickListener(event -> onSubmit(event));		
	}
	
	
	/**
	 * Edit by binding the item to the editor field group
	 * @param item
	 */
	protected void editItem(T item) {
		fieldGroup.setItemDataSource(item);
		getForm().setVisible(item != null);
	}
	
	
	/**
	 * Update the table data from the repository
	 */
	private void updateTableData() {
		itemContainer.removeAllItems();
		itemContainer.addAll(getRepository().findAll());
	}
	
	/**
	 * Show the editor when item is selected in the table
	 * @param event
	 */
	public void onItemSelected(ValueChangeEvent event) {
        @SuppressWarnings("unchecked")
		T item = (T)getTable().getValue();
        getForm().setVisible(item != null);
        getDeleteButton().setEnabled(item != null);
        if (item != null) {
        	editItem(item);
        }        
    }
	
	/**
	 * Save the editor form submission
	 * @param event
	 */
	public void onSubmit(ClickEvent event) {
		if (!fieldGroup.isValid()) {
			Notification.show("Errors in form");
		} else {
			try {
				fieldGroup.commit();
				getRepository().save(fieldGroup.getItemDataSource().getBean());				
				Notification.show("Saved");
			} catch (CommitException e) {
				Notification.show("Commit error");
			} finally {
				updateTableData();
				getForm().setVisible(false);
				getDeleteButton().setEnabled(false);
			}
		}
	}
	
	/**
	 * Add a new item and show the editor when the add button is clicked
	 * @param event
	 */
	public abstract void onAddItem(ClickEvent event);
	
	public void onDeleteItem(ClickEvent event) {
		@SuppressWarnings("unchecked")
		T item = (T)getTable().getValue();
        if (item != null) {
        	try {
        		getRepository().delete(item);
        		Notification.show("Deleted");
        	} finally {
        		updateTableData();
				getForm().setVisible(false);
				getDeleteButton().setEnabled(false);
			}
        }
	}

	
	@Override
	public void enter(ViewChangeEvent event) {
		// the view is constructed in the init() method()
	}

}
