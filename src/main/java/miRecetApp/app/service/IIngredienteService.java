package miRecetApp.app.service;

import miRecetApp.app.model.entity.Ingrediente;

/**
 * @author Julián Quenard *
 * 15 jul. 2022
 */
public interface IIngredienteService {
	/**
	 * Método que elimina el item que corresponda al id de la BD.
	 * @param id
	 */
	public void delete(Long id);
	
	public Ingrediente findById(Long id);
}
