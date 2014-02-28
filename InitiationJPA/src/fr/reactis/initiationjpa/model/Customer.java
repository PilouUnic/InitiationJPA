package fr.reactis.initiationjpa.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Customer {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer Id;
	private String name;
	
	// "la variable customer qui est dans la variable pk"
	@OneToMany(mappedBy="pk.customer")
	private List<ProjectCustomer> projectCustomers;
	
	public Customer(){}

	public Customer(Integer id, String name) {
		Id = id;
		this.name = name;
	}

	public Integer getId() {
		return Id;
	}

	public void setId(Integer id) {
		Id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
	
	
}
