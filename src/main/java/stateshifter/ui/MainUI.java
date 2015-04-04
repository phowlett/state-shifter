package stateshifter.ui;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.annotations.DesignRoot;
import com.vaadin.annotations.Theme;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.UI;

@DesignRoot
@SpringUI
@Theme("stateshifter")
@SuppressWarnings("serial")
public class MainUI extends UI {
	
	@Autowired
    private SpringViewProvider viewProvider;

	@Override
	protected void init(VaadinRequest request) {
		MainDesign design = new MainDesign();
		setContent(design);
		
		Navigator navigator = new Navigator(this, design.getViewPanel());
        navigator.addProvider(viewProvider);
	}

}
