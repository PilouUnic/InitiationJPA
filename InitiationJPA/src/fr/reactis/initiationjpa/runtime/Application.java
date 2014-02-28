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
		// Le find, par ex find Employee, sort tout, (son adresse, son tel, objets priv�s ...) SAUF les List. 
		// Tout sauf List, m�me si l'objet priv� non List est dans d'autres tables il sera recherch�.
		// C'est le "fetch" qui est par d�finition.
		// Pour les List le fetch est en lazy, pour les autres non.
		// Pb : peut peut-�tre impacter 10 tables !
		// Cf dans le cas pr�sent, avec getDepartement sur l'objet Employee, 
		// on a d�j� le d�partement, qui est pourtant dans une table diff�rente.
		// Fetch se modifie dans une annotation.
		// CEPENDANT
		// Si avec l'objet on fait un getDepartement, alors qu'on �tait en lazy,
		// le Departement ne sera pas null !!!!
		// Hibernate VA ALLER AUTOMATIQUEMENT faire la requ�te (car il sait que l'entr�e employ� a un ID d�partement)
		// Cela est d� au design pattern proxy de Hibernate.
		// En fait l'objet Employee croit poss�der un objet Departement, mais il poss�de un 
		// objet Hibernate "qui se fait passer pour d�partement"
		// Lors de l'appel, une requ�te oneshot est lanc�e. (ne sera pas lanc�e une 2e fois si on refait le get)
		// Attention cela n'est possible QUE SI L'OBJET EST ATTACHE AU CONTEXTE
		// Sinon LazyLoadingException.
		// "Un objet est rattach� au contexte Hibernate qu'au niveau du service et de la DAO.
		// En dehors (interface graphique) les Entity redeviennent des POJO."
		// C'est un parti pris. L'entityManager vide son cache quand l'objet est hors service ou DAO.
		// Sans quoi, la pr�sentation commence � faire des get qui font des requ�tes ce qui n'est pas bien.
		// Les objets doivent donc �tre pr�ts � �tre affich�s avec les objets souhait�s
		// quand rendus � la pr�sentation.
		for(Employee empl : listeEmpl) {
			System.out.println(empl.getFirstName() + " " + empl.getLastName() + " " + empl.getDepartment().getNom());
		}
		// En d'autres termes : Lazy Loading = "mets l'objet proxy"


		// On veut les employ�s qui sont dans le projet "release1"
		System.out.println("==== Employ�s de Release1 ====");
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


		System.out.println("==== Test requ�te g�n�r�e par Map ====");
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("firstName", "John");
		Date d = new SimpleDateFormat("yyyy-MM-dd").parse("2001-01-01");
		param.put("startDate", d);
		List<Employee> list = employeeDAO.findByParameter(param);





		JPAUtils.getEntityManager().getTransaction().commit();
	}
}
