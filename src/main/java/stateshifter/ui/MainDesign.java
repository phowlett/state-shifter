package stateshifter.ui;

import com.vaadin.annotations.DesignRoot;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.declarative.Design;

@SuppressWarnings("serial")
@DesignRoot
public class MainDesign extends HorizontalLayout {
	protected Panel viewPanel;
	protected Button mainButton;
	protected Button personButton;
	
	public MainDesign() {
		Design.read(this);
		
		mainButton.addClickListener(event -> getUI().getNavigator().navigateTo(DefaultView.VIEW_NAME));
        personButton.addClickListener(event -> getUI().getNavigator().navigateTo(PersonView.VIEW_NAME));
	}
	
	public Panel getViewPanel() {
		return viewPanel;
	}


}
