package springvaadin.ui;

import java.util.Comparator;
import java.util.Map;
import java.util.stream.Collectors;

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

import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.ChartModel;
import com.vaadin.addon.charts.model.ChartType;
import com.vaadin.addon.charts.model.Configuration;
import com.vaadin.addon.charts.model.DataSeries;
import com.vaadin.addon.charts.model.DataSeriesItem;
import com.vaadin.annotations.Theme;
import com.vaadin.data.Item;
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
	
	@UiField("chart")
	private Chart chart;
	
	private BeanFieldGroup<Person> personFieldGroup;
	private BeanItemContainer<Person> personContainer;
	private Configuration configuration;
	
	/* (non-Javadoc)
	 * @see com.vaadin.ui.UI#init(com.vaadin.server.VaadinRequest)
	 */
	@Override
	protected void init(VaadinRequest request) {
		setContent(Clara.create("PersonUI.xml", this));
		personTable.setVisibleColumns("firstname", "lastname", "pizza");
		initEditor();
        initChart();
	}

	/**
	 * Initialise the person editor and form fields
	 */
	private void initEditor() {
		personFieldGroup = new BeanFieldGroup<Person>(Person.class);
		// Create and bind text fields for name
		personEditor.addComponent(personFieldGroup.buildAndBind("First Name", "firstname"), 0);
		personEditor.addComponent(personFieldGroup.buildAndBind("Last Name", "lastname"), 1);
		
		// Combo box for pizza selection, backed by pizza repository
		ComboBox selectPizza = new ComboBox("Pizza", new BeanItemContainer<>(Pizza.class, pizzaRepository.findAll()));
		selectPizza.setItemCaptionMode(ItemCaptionMode.ITEM);
		personFieldGroup.bind(selectPizza, "pizza");
		personEditor.addComponent(selectPizza, 2);
		// Buffered mode, so person will not be updated until submit and commit
        personFieldGroup.setBuffered(true);
	}

	/**
	 * Initialise the pie chart
	 */
	private void initChart() {
		configuration = chart.getConfiguration();
        configuration.setTitle("Pizza");
        configuration.setChart(new ChartModel(configuration, ChartType.PIE));
        updateChartData();
	}
	
	/**
	 * update the chart data based on the count of each pizza choice
	 */
	private void updateChartData() {
		// Data series for the pizza pie chart
		DataSeries series = new DataSeries("Pizza");		
		// Group and count the pizzas
        Map<Pizza, Long> countByPizza = personRepository.findAll().stream()
        	.collect(Collectors.groupingBy(Person::getPizza, Collectors.counting()));
        // Map the pizza counts to a data series for the pie chart
        countByPizza.entrySet().stream()
        	.map(e -> new DataSeriesItem(e.getKey().toString(), e.getValue()))
        	.sorted(Comparator.comparing(d -> d.getName()))
        	.forEach(d -> series.add(d));
        // Set the chart series and redraw
        configuration.setSeries(series);
        chart.drawChart();
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
	 * Datasource for the table of people
	 * @return
	 */
	@UiDataSource("personTable")
	public BeanItemContainer<Person> getPersonDataSource() {
		personContainer = new BeanItemContainer<>(Person.class, personRepository.findAll()); 
		return personContainer;
	}
	
	/**
	 * Show the editor when person is selected in the table
	 * @param event
	 */
	@UiHandler("personTable")
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
	@UiHandler("addButton")
    public void onAddPerson(ClickEvent event) {
        BeanItem<Person> personBean = new BeanItem<Person>(new Person("New", "Person"));
        editPerson(personBean);        
    }
	
	/**
	 * Save the person on editor form submission
	 * @param event
	 */
	@UiHandler("submitButton")
	public void onSubmit(ClickEvent event) {
		if (!personFieldGroup.isValid()) {
			Notification.show("Errors in form");
		} else {
			try {
				personFieldGroup.commit();
				personRepository.saveAndFlush(personFieldGroup.getItemDataSource().getBean());
				Notification.show("Saved");
				updateTableData();
				updateChartData();
			} catch (CommitException e) {
				Notification.show("Commit error");
			}
		}
	}

}