package fr.reactis.initiationjpa.dao;

import fr.reactis.initiationjpa.dao.impl.EmployeeJPADAO;
import fr.reactis.initiationjpa.dao.impl.ProjectJPADAO;

public class DAOFactory {

	public static EmployeeDAO getEmployeeDAO() {
		return new EmployeeJPADAO();		
	}

	public static ProjectDAO getProjectDAO() {

		return new ProjectJPADAO();
	}
}
