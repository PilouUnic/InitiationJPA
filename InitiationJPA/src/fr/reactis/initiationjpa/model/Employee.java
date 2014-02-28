package fr.reactis.initiationjpa.model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
//@Table(name="T_Employee")
// NamedQuery : query en langage JPQL
// Pas très bien car query hors DAO.
@NamedQueries({
	@NamedQuery(name="Employee.findAll", query="Select e from Employee e")
})

public class Employee {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	// generatedValue : 
	// AUTO demande la clé autoincrémentée. ID classiques MySQL etc
	// IDENTITY : soit comme AUTO si l'autoincrémentation existe, soit crée de lui-même 1 séquence par table. 
	// SEQUENCE : variable séquence, on lui donne le nomd de la variable par annotation SequenceGenerator. cf Oracle
	// TABLE : crée une table pour faire des ID (spécial BDD archaiques)

	@Column(name="nom", length=50)
	private String lastName;

	@Column(name="prenom", length=50)
	// = on parle du champ prenom, qui s'il est créé aura une taille 50.
	private String firstName;

	@Temporal(TemporalType.DATE)
	private Date startDate;
	//TemporalType.DATE = On veut créer automatiquement une table qui ne prend que le YYYY MM DD et non les secondes etc.
	// Avec ça, le type ne sera pas datetime mais date.

	@OneToOne(mappedBy="employee", fetch=FetchType.LAZY, cascade={CascadeType.MERGE, CascadeType.REMOVE})
	// Phone en variable car on veut accéder directement au téléphone de l'employé, mais sans qu'il y ait de clé étrangère phone dans 
	// la table Employee.
	// En effet la clé étrangère est dans la table Phone et non Employee (c'est du 1 pour 1 donc on a le choix).
	// D'où le mappedBy. Le "employee" est la valeur de la variable Employee dans Phone. 
	// --------
	// Le find (find Employee) sort tout, (son adresse, son tel, objets privés ...) SAUF les List. Même si c'est dans d'autres tables.
	// C'est le "fetch", qui est par définition eager.
	// Pour les List il est en lazy d'origine.
	// (Pb : peut peut-être impacter 10 tables !)
	
	// Cascade : "est-ce que quand j'enregistre un Employee en BDD, j'enregistre d'office son téléphone ?"
	// "est-ce que je détache son tel du cache (contexte) quand je détache l'Employee ?"
	// etc (voir les significations des valeurs)
	// Déconseillé car ça se fera par défaut (plusieurs requêtes) + peut potentiellement mettre à jour 
	// voire suppr les phone des employés alors qu'on ne l'a pas indiqué dans le code.
	private Phone phone;
	
	@ManyToOne(fetch=FetchType.LAZY)
	private Department department;
	
	@ManyToMany(mappedBy="employees")
	// Relation bidirectionnelle many to many
	private List<Project> projects;
	

	public Employee() {

	}

	public Employee(String lastName, String firstName) {
		super();
		this.lastName = lastName;
		this.firstName = firstName;
	}

	public Employee(Integer id, String lastName, String firstName, Date startDate) {
		this.id = id;
		this.lastName = lastName;
		this.firstName = firstName;
		this.startDate = startDate;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Phone getPhone() {
		return phone;
	}

	public void setPhone(Phone phone) {
		// Subtilité car 1 pour 1
		// (question de propriétaire de la relation et de cascade ...)
		phone.setEmployee(this);
		this.phone = phone;
	}
	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}
	
	public List<Project> getProjects() {
		return projects;
	}

	public void setProjects(List<Project> projects) {
		this.projects = projects;
	}

}