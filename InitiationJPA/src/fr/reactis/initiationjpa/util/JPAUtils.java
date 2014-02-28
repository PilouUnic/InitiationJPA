package fr.reactis.initiationjpa.util;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

public class JPAUtils {

	// Singleton EntityManager
	// Il ne doit y avoir qu'un seul EntityManager par appli.
	// Sans quoi les transactions ne servent � rien. Il y a 1 connection � la BDD par EntityManager.

	private static EntityManager entityManager;

	public static EntityManager getEntityManager(){

		if(entityManager == null || !entityManager.isOpen()) {
			// "EmployeeManager" = nom de l'unit� de persistence du persistence.xml
			entityManager = Persistence.createEntityManagerFactory("EmployeeManager").createEntityManager();
		}
		return entityManager;
	}



}
