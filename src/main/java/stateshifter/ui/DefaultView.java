package stateshifter.ui;

import javax.annotation.PostConstruct;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
@SpringView(name = DefaultView.VIEW_NAME)
public class DefaultView extends VerticalLayout implements View {
	
	public static final String VIEW_NAME = "";
	
	Panel mainPanel;
	
	@PostConstruct
    void init() {
		addComponent(new Label("This is the main view"));
    }
	
	@Override
	public void enter(ViewChangeEvent event) {
		// the view is constructed in the init() method()
	}

}
