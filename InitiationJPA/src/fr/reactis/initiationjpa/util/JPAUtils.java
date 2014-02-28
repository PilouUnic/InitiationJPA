package fr.reactis.initiationjpa.util;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

public class JPAUtils {

	// Singleton EntityManager
	// Il ne doit y avoir qu'un seul EntityManager par appli.
	// Sans quoi les transactions ne servent à rien. Il y a 1 connection à la BDD par EntityManager.

	private static EntityManager entityManager;

	public static EntityManager getEntityManager(){

		if(entityManager == null || !entityManager.isOpen()) {
			// "EmployeeManager" = nom de l'unité de persistence du persistence.xml
			entityManager = Persistence.createEntityManagerFactory("EmployeeManager").createEntityManager();
		}
		return entityManager;
	}



}
