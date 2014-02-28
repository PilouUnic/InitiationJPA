package fr.reactis.initiationjpa.dao;

import java.util.Date;
import java.util.List;

import fr.reactis.initiationjpa.model.Employee;

// On connaît ici la valeur des types génériques : Employee et Integer.
public interface EmployeeDAO extends GenericDAO<Employee, Integer> {

	List<Employee> findWithProject();
	List<Employee> findByProjectName(String name);
	List<Employee> findByStartDate(Date from, Date to);
	List<Employee> findAllWithoutProject();
	Employee findLastEmployee();
	

}
