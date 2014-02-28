package fr.reactis.initiationjpa.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class Phone {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	private String number;
	@Enumerated(EnumType.STRING)
	private PhoneType phoneType;

	@OneToOne
	// = Hibernate sait donc que c'est du 1 pour 1 en BDD
	// Au lieu de "employee_id" on peut renommer la colonne qui va être créée
	@JoinColumn(name="fk_employee")
	private Employee employee;

	public enum PhoneType {
		// Sera enregistré par défaut en BDD sous forme de int. (0 1 2).
		// On peut changer en mettant le NOM de l'enum.
		// Pour cela, annotation @Enumerated(EnumType.STRING) dans la variable privée.
		Office, Home, Cell;
	}

	public Phone() {
		super();
	}

	public Phone(Integer id, String number, PhoneType phoneType) {
		super();
		this.id = id;
		this.number = number;
		this.phoneType = phoneType;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public PhoneType getPhoneType() {
		return phoneType;
	}

	public void setPhoneType(PhoneType phoneType) {
		this.phoneType = phoneType;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}



}
