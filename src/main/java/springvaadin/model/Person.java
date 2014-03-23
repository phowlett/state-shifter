package springvaadin.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "person")
public class Person implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id 
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id", unique=true, nullable=false)
	private Long id;
	
	@Column(name="firstname", nullable=false)
	private String firstname;
	
	@Column(name="lastname", nullable=false)
	private String lastname;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	
	
}
