package fr.reactis.initiationjpa.model;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

@Embeddable
// = n'est pas une Entity, mais embarqué dans une Entity.
// Si on avait fait par ex un objet Adresse (qui peut convenir à un Client ou à un Employé)
// On aurait fait Embeddable aussi.
public class PKProjectCustomer implements Serializable {
	
	// Objet qui représente le duo Projet + Customer.
	// (concrètement on a une table de jointure en BDD)

	private static final long serialVersionUID = 1L;

	@ManyToOne
	private Project project;

	@ManyToOne
	private Customer customer;
	
	public PKProjectCustomer() {
		
	}
	
	public PKProjectCustomer(Project project, Customer customer) {
		super();
		this.project = project;
		this.customer = customer;
	}
	
	

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}


	

}
