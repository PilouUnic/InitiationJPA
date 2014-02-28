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
// Pas tr�s bien car query hors DAO.
@NamedQueries({
	@NamedQuery(name="Employee.findAll", query="Select e from Employee e")
})

public class Employee {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	// generatedValue : 
	// AUTO demande la cl� autoincr�ment�e. ID classiques MySQL etc
	// IDENTITY : soit comme AUTO si l'autoincr�mentation existe, soit cr�e de lui-m�me 1 s�quence par table. 
	// SEQUENCE : variable s�quence, on lui donne le nomd de la variable par annotation SequenceGenerator. cf Oracle
	// TABLE : cr�e une table pour faire des ID (sp�cial BDD archaiques)

	@Column(name="nom", length=50)
	private String lastName;

	@Column(name="prenom", length=50)
	// = on parle du champ prenom, qui s'il est cr�� aura une taille 50.
	private String firstName;

	@Temporal(TemporalType.DATE)
	private Date startDate;
	//TemporalType.DATE = On veut cr�er automatiquement une table qui ne prend que le YYYY MM DD et non les secondes etc.
	// Avec �a, le type ne sera pas datetime mais date.

	@OneToOne(mappedBy="employee", fetch=FetchType.LAZY, cascade={CascadeType.MERGE, CascadeType.REMOVE})
	// Phone en variable car on veut acc�der directement au t�l�phone de l'employ�, mais sans qu'il y ait de cl� �trang�re phone dans 
	// la table Employee.
	// En effet la cl� �trang�re est dans la table Phone et non Employee (c'est du 1 pour 1 donc on a le choix).
	// D'o� le mappedBy. Le "employee" est la valeur de la variable Employee dans Phone. 
	// --------
	// Le find (find Employee) sort tout, (son adresse, son tel, objets priv�s ...) SAUF les List. M�me si c'est dans d'autres tables.
	// C'est le "fetch", qui est par d�finition eager.
	// Pour les List il est en lazy d'origine.
	// (Pb : peut peut-�tre impacter 10 tables !)
	
	// Cascade : "est-ce que quand j'enregistre un Employee en BDD, j'enregistre d'office son t�l�phone ?"
	// "est-ce que je d�tache son tel du cache (contexte) quand je d�tache l'Employee ?"
	// etc (voir les significations des valeurs)
	// D�conseill� car �a se fera par d�faut (plusieurs requ�tes) + peut potentiellement mettre � jour 
	// voire suppr les phone des employ�s alors qu'on ne l'a pas indiqu� dans le code.
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
		// Subtilit� car 1 pour 1
		// (question de propri�taire de la relation et de cascade ...)
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