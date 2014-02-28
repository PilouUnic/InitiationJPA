package fr.reactis.initiationjpa.dao;

import java.util.List;
import java.util.Map;

public interface GenericDAO<T, K> {

	// T aurait pu s'appeller Z. Norme : T
	T save(T entite);
	// Find by ID : on aurait pu mettre Integer. Mais non car pour ProjectCustomer la clé n'est pas
	// un ID mais un objet PKProjectCustomer. (qui lui même représente une table de jointure càd 2 ID)
	// Donc on ne connaît pas à l'avance. D'où le K.
	T findById(K id);
	List<T> findAll();
	// On aurait pu faire un remove sur l'objet mais pas forcément rattaché
	// Il aurait fallu faire un find et remove pour passer l'objet en param.
	void remove(K id);
	// Génère une requête avec des AND en se servant des clés valeurs
	List<T> findByParameter(Map<String, Object> param);


}
