package fr.reactis.initiationjpa.model;

import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

// Classe qui extend Project. Sert à voir l'héritage

@Entity
@DiscriminatorValue("Q")
public class QualityProject extends Project {

	private String refQuality;

	public QualityProject() {

	}


	public QualityProject(String refQuality) {
		this.refQuality = refQuality;
	}



	public QualityProject(Integer id, String nom, List<Employee> employees, String refQuality) {
		super(id, nom, employees);
		this.refQuality = refQuality;
	}



	public String getRefQuality() {
		return refQuality;
	}

	public void setRefQuality(String refQuality) {
		this.refQuality = refQuality;
	}


}
