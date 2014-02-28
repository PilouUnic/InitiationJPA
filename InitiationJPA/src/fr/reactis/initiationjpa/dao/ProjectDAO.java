package fr.reactis.initiationjpa.dao;

import java.util.List;

import fr.reactis.initiationjpa.model.Project;
import fr.reactis.initiationjpa.model.QualityProject;

public interface ProjectDAO extends GenericDAO<Project, Integer> {

	List<Project> findByDepartmentName(String depName);
	List<QualityProject> findAllQualityProject();
}
