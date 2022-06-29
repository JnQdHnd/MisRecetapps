package miRecetApp.app.service;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import miRecetApp.app.model.entity.Preparacion;

public interface IPreparacionService {
	/**
	 * Método que trae todos los elementos de la BD.
	 * @return List<Preparacion>
	 */
	public List<Preparacion> findAll();
	
	/**
	 * Método que trae todos los elementos de la BD y los ordena según el criterio indicado "sort".
	 * @param sort
	 * @return List<Preparacion>
	 */
	public List<Preparacion> findAll(Sort sort); 
	
	/**
	 * Método que trae el item que corresponda al Id de la BD.
	 * @param id
	 * @return Preparacion
	 */
	public Preparacion findOne(Long id);
	
	/**
	 * Método que trae el item que corresponda al Id de la BD.
	 * @param id
	 * @return Preparacion
	 */
	public Preparacion findByRecetaId(Long recetaId);
	
	/**
	 * Método que elimina el item que corresponda al id de la BD.
	 * @param id
	 */
	public void delete(Long id);
	
	/**
	 * Método que guarda en la DB el objeto pasado como parámetro.
	 * @param Preparacion
	 */
	public void save(Preparacion preparacion);

}