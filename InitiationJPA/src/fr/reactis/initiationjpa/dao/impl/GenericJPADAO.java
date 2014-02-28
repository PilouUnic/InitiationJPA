package fr.reactis.initiationjpa.dao.impl;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import fr.reactis.initiationjpa.dao.GenericDAO;
import fr.reactis.initiationjpa.util.JPAUtils;
// Note : pour manipuler T et K, il faut OBLIGATOIREMENT les citer après le nom
// de la classe : ici GenericJPADAO<T, K> 
public class GenericJPADAO<T, K> implements GenericDAO<T, K> {

	protected EntityManager entityManager = JPAUtils.getEntityManager();
	// Afin d'obtenir la classe du T. Car on ne peut rien faire comme méthode sur le T et on a parfois besoin de faire
	// un "T.getClass".
	private Class<T> type;

	public GenericJPADAO(Class<T> type)	{
		this.type = type;		
	}


	@Override
	public T save(T entite) {
		// Ici on aurait pu faire :
		// entityManager.getTransaction.begin
		// .persist
		// .commit
		// Et répéter le begin commit pour chaque méthode. 
		// Pb : la TRANSACTION n'est pas bonne. Par ex on va enregistrer un Customer, puis son Phone, pas dans la même
		// transaction. Le concept de transaction ne sert plus à rien.
		// Il faut donc faire remonter ces méthodes begin et commit, elles n'ont pas leur place ici.

		// persist ou merge.

		//  ------ persist -----
		//entityManager.persist(entite);
		// L'ID a été attribué. On pourrait ne pas return.
		// (norme : return l'objet quand il est modifié, sans quoi il faut aller voir dans le code
		// ce qui se passe)
		//return entite;
		// -------------------


		//------- merge -------
		// merge ne change pas l'objet, il rend un nouvel objet avec ID modifié
		return entityManager.merge(entite);

	}



	@Override
	public T findById(K id) {
		// On ne peut pas faire T.getClass.
		// Raison pour laquelle on a "type"
		return entityManager.find(type, id);
	}

	@Override
	public List<T> findAll() {
		// Langage JPQL (on s'en fout des noms des tables)
		// Norme de JPA. HQL (Hibernate) est exactement pareil
		TypedQuery<T> query = entityManager.createQuery("Select e From " + type.getName() + " e", type);
		// (Ne rend jamais de liste nulle)
		return query.getResultList();
	}

	@Override
	public void remove(K id) {
		// On a décidé dans le code que la variable privée "id" était toujours l'ID dans TOUTES les classes.
		// Convention fréquente pour faire des requêtes génériques.
		Query query = entityManager.createQuery("Delete From " + type.getName() + " e Where e.id = :id");
		query.setParameter("id", id);
		query.executeUpdate();

	}


	@Override
	public List<T> findByParameter(Map<String, Object> param) {
		// Create String QUERY
		StringBuilder queryBuilder = new StringBuilder();
		Set<String> keySet = param.keySet();
		for (String paramName : keySet) {
			queryBuilder.append(" e.").append(paramName).append("=:")
			.append(paramName).append(" AND ");
		}

		String query = queryBuilder.toString();

		// REMOVE LAST AND
		query = query.substring(0, query.lastIndexOf("AND"));

		// Final Query
		query = "Select e From " + type.getName() + " e Where " + query;

		TypedQuery<T> typedQuery = JPAUtils.getEntityManager().createQuery(query, type);
		for (String paramName : keySet) {
			typedQuery.setParameter(paramName, param.get(paramName));
		}			


		return typedQuery.getResultList();


	}

}
