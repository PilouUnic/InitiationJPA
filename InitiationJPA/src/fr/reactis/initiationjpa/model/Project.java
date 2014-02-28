package fr.reactis.initiationjpa.model;

import java.util.List;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

@Entity
@Inheritance (strategy=InheritanceType.SINGLE_TABLE)
// Cette classe a des hériters. QualityProject etc.
// Hibernate peut enregistrer les héritiers dans des classes totalement différentes (en effet il y a des champs en plus)
// Ou dans la même table à tel ou tel champ. Si peu de champs différents on peut tout mettre dans la même table.
// Dans ce cas les champs non concernés (refQuality pour un Project de base par ex) seront null.
// cf valeurs possibles pour @Inheritance(strategy= xxx )
// Stratégie par défaut : SINGLE_TABLE.
@DiscriminatorValue("P")
//On ajoute un champ qui contient "P" pour Project, "Q" pour entrée QualityProject, etc.
// Par défaut ce champ se nomme DTYPE et varchar 31 mais on peut changer avec les annotations DiscriminatorColumn, etc.
@DiscriminatorColumn(name="TypeProject")

public class Project {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	private String nom;
	
	// n projets pour n employés. Il y aura une table de jointure créée avec les 2 ID dedans.
	@ManyToMany
	// Renomme la table de jointure et ses entrées
	@JoinTable(name="ProjectEmployee", joinColumns=@JoinColumn(name="projectId"), inverseJoinColumns=@JoinColumn(name="employeeId"))
	// Si on veut ajouter un champ "date à laquelle un employé est entré sur un projet" il faudra créer une classe pour ça.
	private List<Employee> employees;

	public Project(Integer id, String nom, List<Employee> employees) {
		super();
		this.id = id;
		this.nom = nom;
		this.employees = employees;
	}
	public Project() {
		super();
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
	public List<Employee> getEmployees() {
		return employees;
	}
	public void setEmployees(List<Employee> employees) {
		this.employees = employees;
	}
	
	
}
