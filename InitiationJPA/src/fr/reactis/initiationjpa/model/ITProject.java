package fr.reactis.initiationjpa.model;

import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("I")
public class ITProject extends Project {


	private String techName;


	public ITProject(Integer id, String nom, List<Employee> employees,
			String techName) {
		super(id, nom, employees);
		this.techName = techName;
	}

	public ITProject() {
		super();
	}

	public ITProject(Integer id, String nom, List<Employee> employees) {
		super(id, nom, employees);
	}

	public String getTechName() {
		return techName;
	}

	public void setTechName(String techName) {
		this.techName = techName;
	}

}
