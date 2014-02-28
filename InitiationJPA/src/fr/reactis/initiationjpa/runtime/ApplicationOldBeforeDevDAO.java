package fr.reactis.initiationjpa.runtime;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import fr.reactis.initiationjpa.model.Employee;

public class ApplicationOldBeforeDevDAO {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		// Note : il n'y a pas de try catch ! C'est un parti pris. On peut les catch ou pas (PersistenceException).
		// Par ex on catch les PersistenceException et on en fait des DAOException.

		// Il est conseillé de ne faire qu'un seul EntityManager par appli
		EntityManager entityManager = Persistence.createEntityManagerFactory("EmployeeManager").createEntityManager();
		Employee employee = new Employee(null, "LAST", "First", new Date());
		// On démarre une transaction (ensemble de requetes qui soit passent soit ne passent pas, totalement)
		entityManager.getTransaction().begin();

		// Ajoute l'employé. Du fait de la config du SQL, crée la BDD ! 
		// En fait, actuellement seulement un ID est ajouté à l'objet. (pas encore de sauvegarde BDD)
		// Le cache Hibernate le prend en compte.
		// = l'objet est "rattaché au contexte Hibernate"
		entityManager.persist(employee);

		// Sauvegarde en BDD
		entityManager.getTransaction().commit();

		// Retrait du cache Hibernate. Ainsi lors d'un find, Hibernate ne trouvera pas l'objet en cache
		// Et ira donc chercher en BDD. (on fait ça afin de voir la requête SQL passer)
		// Rarement utilisé. Car à l'inverse on cherche plutôt à rattacher les objets qu'à les détacher ...
		entityManager.clear();

		// Recherche ( ==> prioritairement dans les objets "attachés au contexte Hibernate" <=== )
		// Where ID = 
		Employee employee1 = entityManager.find(Employee.class, 1);
		System.out.println("trouvé : " + employee1.getFirstName() + " " + employee1.getLastName());

		// ---- modif -----
		entityManager.getTransaction().begin();
		employee1.setLastName("NEW NAME");

		// Dans ce cas, l'update se fait sans qu'on ait demandé exactement d'update sur le Last Name.
		// En fait Hibernate vérifie quels objets rattachés dans son contexte (les objets sont 
		// rattachés après un find ou un persist) ont été modifiés. 
		// Si on fait 
		// employee1.setLastName("NEW NAME");
		// puis 
		// employee1.setLastName("[nom original]");
		// Hibernate ne fera pas d'update !
		// -----------------

		entityManager.merge(employee1);
		// merge : "rattache cet objet au contexte Hibernate"
		// Le merge fait sois un insert, soit un update. (selon que l'objet ait un ID ou pas)
		// Peut faire un select pour faire une comparaison avec ce qui est en base, si l'objet n'est pas 
		// connu dans le contexte Hibernate.
		// L'objet donné en merge peut très bien être créé à la main, ne pas provenir d'un find.
		// exemple (il va faire un insert direct car null)
		entityManager.merge(new Employee(null, "employeeMerge", "employeeMerge", new Date()));
		// Autre exemple (il va faire un SELECT puis un INSERT car ID 2, il ne trouve rien, il insert)
		entityManager.merge(new Employee(2, "employeeMerge", "employeeMerge", new Date()));

		// remove : supprime un objet attaché dans la base. Il ne fait pas de select avant : 
		// On devra faire un find d'abord.
		entityManager.remove(employee1);

		// La NamedQuery est définie au niveau de la classe. Lors de la compilation il va verif 
		// Si les requêtes sont bien codées. Compilation KO si la requête est mal faite
		// Peu utilisé car du coup les requêtes sont hors DAO.
		// Cf string "findAll" classe Employee.
		TypedQuery<Employee> query = entityManager.createNamedQuery("Employee.findAll", Employee.class);
		List<Employee> employees = query.getResultList();

		// Requete JPQL donnee en param
		TypedQuery<Employee> query2 = entityManager.createQuery("Select e from Employee e where e.lastName = :name", Employee.class);
		query2.setParameter("name", "nom");
		List<Employee> listeEmployee = query.getResultList();

		// N'obtenir que le nom et prénom 
		List<Object[]> result = entityManager.createQuery("Select e.lastName, e.firstName from Employee e").getResultList();
		for(Object[] obj : result) {
			System.out.println(obj[0] + " " + obj[1]);
		}

		// On peut même créer les objets à la volée (le constructeur doit bien sûr exister)
		// Faire un new avec l'objet qu'on veut
		List<Employee> result2 = entityManager.createQuery("Select new fr.reactis.initiationjpa.model.Employee(e.lastName, e.firstName) from Employee e").getResultList();

		// nativeQuery : requête dan sle langage de la BDD. On se détache de Hibernate
		// Par ex gérer une procédure stockée etc (Hibernate le fait, pas JPA)
		// entityManager.createNativeQuery("Select * etc ");

		entityManager.getTransaction().commit();


	}
}
