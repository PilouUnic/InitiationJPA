package fr.reactis.initiationjpa.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Department {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	private String nom;
	
	@OneToMany(mappedBy="department")
	// Si on avait laissé le OneToMany sans mappedBy, la création de BDD aurait été fausse. Création d'une table en trop et incohérence en pratique.
	// ( = un employé est dans 1 département, mais le département possède n employés sans tenir compte des ID employés !) 
	private List<Employee> employees;
	
	
	public Department(Integer id, String nom) {
		this.id = id;
		this.nom = nom;
	}

	public Department() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

}
