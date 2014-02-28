package fr.reactis.initiationjpa.dao.impl;

import java.util.Date;
import java.util.List;

import javax.persistence.TypedQuery;

import fr.reactis.initiationjpa.dao.EmployeeDAO;
import fr.reactis.initiationjpa.model.Employee;

public class EmployeeJPADAO extends GenericJPADAO<Employee, Integer> implements EmployeeDAO {

	public EmployeeJPADAO() {
		// dans la classe sup�rieure 
		// On d�finit Employee.class
		super(Employee.class);
	}

	@Override
	// Charge les Employ�s ET leurs projets. M�me ceux qui n'ont pas de projet.
	public List<Employee> findWithProject() {
		// Rappel : dans la requ�te on parle d'objets et de leurs variables (m�me priv�es)
		// Les noms des tables osef.
		// Le terme "fetch" signifie : remplis l'objet Employee avec leurs projets 
		// Sans quoi c'est juste une jointure.
		// left pour avoir les Employee m�me s'ils n'ont pas de projet.
		TypedQuery<Employee> query = entityManager.createQuery("Select e from Employee e left join fetch e.projects p ", Employee.class);
		return query.getResultList();
	}

	@Override
	public List<Employee> findByProjectName(String name) {
		// Pas de terme "fetch" dans la requ�te : on ne veut pas les projets avec
		TypedQuery<Employee> query = entityManager.createQuery("Select e from Employee e join e.projects p Where p.nom=:nomProjet", Employee.class);
		// Autre requ�te qui donne la m�me chose (puisque c'est une relation bidirectionnelle)
		// TypedQuery<Employee> query = entityManager.createQuery("Select e from Project p join p.employees e Where p.nom=:nomProjet", Employee.class);
		query.setParameter("nomProjet", name);
		return query.getResultList();
	}

	@Override
	public List<Employee> findByStartDate(Date from, Date to) {
		TypedQuery<Employee> query = entityManager.createQuery("Select e from Employee e Where e.startDate Between :from And :to", Employee.class);
		query.setParameter("from", from);
		query.setParameter("to", to);
		return query.getResultList();
	}

	@Override
	public List<Employee> findAllWithoutProject() {
		// Fait automatiquement la requ�te imbriqu�e
		TypedQuery<Employee> query = entityManager.createQuery("Select e from Employee e Where e.projects IS EMPTY", Employee.class);
		return query.getResultList();
	}

	@Override
	public Employee findLastEmployee() {
		TypedQuery<Employee> query = entityManager.createQuery("Select e from Employee e Where e.startDate = (Select max(emp.startDate) From Employee emp)", Employee.class);
		return query.getSingleResult();
		// trouver les 4 derniers employ�s :
		// Select e From Employee e Order By e.startDate Desc
		// + query.setFirstResult(0);
		// + query.setMaxResult(4);
		// M�me s'il y en a 2 c'est OK.
	}


}
