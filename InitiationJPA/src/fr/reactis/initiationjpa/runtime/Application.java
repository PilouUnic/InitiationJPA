package fr.reactis.initiationjpa.runtime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.reactis.initiationjpa.dao.DAOFactory;
import fr.reactis.initiationjpa.dao.EmployeeDAO;
import fr.reactis.initiationjpa.dao.ProjectDAO;
import fr.reactis.initiationjpa.model.Employee;
import fr.reactis.initiationjpa.model.Project;
import fr.reactis.initiationjpa.util.JPAUtils;

public class Application {

	/**
	 * @param args
	 * @throws ParseException 
	 */
	public static void main(String[] args) throws ParseException {

		JPAUtils.getEntityManager().getTransaction().begin();

		EmployeeDAO employeeDAO = DAOFactory.getEmployeeDAO();
		List<Employee> listeEmpl = employeeDAO.findAll();
		// NOTE : 
		// Le find, par ex find Employee, sort tout, (son adresse, son tel, objets privés ...) SAUF les List. 
		// Tout sauf List, même si l'objet privé non List est dans d'autres tables il sera recherché.
		// C'est le "fetch" qui est par définition.
		// Pour les List le fetch est en lazy, pour les autres non.
		// Pb : peut peut-être impacter 10 tables !
		// Cf dans le cas présent, avec getDepartement sur l'objet Employee, 
		// on a déjà le département, qui est pourtant dans une table différente.
		// Fetch se modifie dans une annotation.
		// CEPENDANT
		// Si avec l'objet on fait un getDepartement, alors qu'on était en lazy,
		// le Departement ne sera pas null !!!!
		// Hibernate VA ALLER AUTOMATIQUEMENT faire la requête (car il sait que l'entrée employé a un ID département)
		// Cela est dû au design pattern proxy de Hibernate.
		// En fait l'objet Employee croit posséder un objet Departement, mais il possède un 
		// objet Hibernate "qui se fait passer pour département"
		// Lors de l'appel, une requête oneshot est lancée. (ne sera pas lancée une 2e fois si on refait le get)
		// Attention cela n'est possible QUE SI L'OBJET EST ATTACHE AU CONTEXTE
		// Sinon LazyLoadingException.
		// "Un objet est rattaché au contexte Hibernate qu'au niveau du service et de la DAO.
		// En dehors (interface graphique) les Entity redeviennent des POJO."
		// C'est un parti pris. L'entityManager vide son cache quand l'objet est hors service ou DAO.
		// Sans quoi, la présentation commence à faire des get qui font des requêtes ce qui n'est pas bien.
		// Les objets doivent donc être prêts à être affichés avec les objets souhaités
		// quand rendus à la présentation.
		for(Employee empl : listeEmpl) {
			System.out.println(empl.getFirstName() + " " + empl.getLastName() + " " + empl.getDepartment().getNom());
		}
		// En d'autres termes : Lazy Loading = "mets l'objet proxy"


		// On veut les employés qui sont dans le projet "release1"
		System.out.println("==== Employés de Release1 ====");
		List<Employee> listeEmplFromRelease1 = employeeDAO.findByProjectName("release1");
		for(Employee empl : listeEmplFromRelease1) {
			System.out.println(empl.getFirstName() + " " + empl.getLastName());
		}

		List<Employee> listeEmplaaa = employeeDAO.findAllWithoutProject();
		Employee eee = employeeDAO.findLastEmployee();

		ProjectDAO prodao = DAOFactory.getProjectDAO();
		List<Project> pros = prodao.findByDepartmentName("QA");
		for(Project p : pros) 		{
			System.out.println(p.getNom());
		}


		System.out.println("==== Test requête générée par Map ====");
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("firstName", "John");
		Date d = new SimpleDateFormat("yyyy-MM-dd").parse("2001-01-01");
		param.put("startDate", d);
		List<Employee> list = employeeDAO.findByParameter(param);





		JPAUtils.getEntityManager().getTransaction().commit();
	}
}
