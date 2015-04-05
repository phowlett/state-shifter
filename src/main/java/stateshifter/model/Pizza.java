package stateshifter.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Pizza {
	
	@Id 
	private String id;
	
	private String description;

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	@Override
	public String toString() {
		return description;
	}
	@Override
	public boolean equals(Object obj) {
		return obj != null && id.equals(((Pizza)obj).getId());
	}
	
}
