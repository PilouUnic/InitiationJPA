package fr.reactis.initiationjpa.dao.impl;

import java.util.List;

import javax.persistence.Query;
import javax.persistence.TypedQuery;

import fr.reactis.initiationjpa.dao.ProjectDAO;
import fr.reactis.initiationjpa.model.ITProject;
import fr.reactis.initiationjpa.model.Project;
import fr.reactis.initiationjpa.model.QualityProject;

public class ProjectJPADAO extends GenericJPADAO<Project, Integer> implements ProjectDAO {

	public ProjectJPADAO() {
		super(Project.class);
	}

	@Override
	public List<Project> findByDepartmentName(String name) {
		// distinct assure l'unicit� du projet.
		TypedQuery<Project> query = entityManager.createQuery("Select distinct p from Project p join p.employees e join e.department d Where d.nom = :name", Project.class);
		query.setParameter("name", name);
		return query.getResultList();
	}

	@Override
	public List<QualityProject> findAllQualityProject() {
		// Le langage �tant sur les objets, on se fout de comment est fait un QualityProject en BDD
		// (en r�alit�, en base il s'agit d'une entr�e de la table Project avec certains champs sp�cifiques)
		// Cela aurait pu tenir sur plusieurs tables ou autre, �a ne change rien ici
		TypedQuery<QualityProject> query = entityManager.createQuery("Select p from QualityProject p", QualityProject.class);
		return query.getResultList();
	}
	
	// NativeQuery : SQL pur d�pendant donc de la base.
	// On peut s'en servir par exemple pour les proc�dures stock�es (call procedure etc ..) 
	
}
