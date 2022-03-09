package miRecetApp.app.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import miRecetApp.app.model.entity.Receta;

public interface IRecetaService {
	/**
	 * Método que trae todos los elementos de la BD.
	 * @return List<Receta>
	 */
	public List<Receta> findAll();
	
	/**
	 * Método que trae todos los elementos de la BD y los ordena según el criterio indicado "sort".
	 * @param sort
	 * @return List<Receta>
	 */
	public List<Receta> findAll(Sort sort); 
	
	/**
	 * Método que trae todos los elementos de la BD.
	 * @return List<Receta>
	 */
	public List<Receta> findAllByAutor(String autor);
	
	/**
	 * Método que trae todos los elementos de la BD y los ordena según el criterio indicado "sort".
	 * @param sort
	 * @return List<Receta>
	 */
	public List<Receta> findAllByAutor(String autor, Sort sort); 
	
	/**
	 * Método que trae todas las recetas de la BD en la que el autor está invitado.
	 * @param sort
	 * @return List<Receta>
	 */
	public List<Receta> findByIdIn(List<Long> recetasIds); 
	
	/**
	 * Método que trae el item que corresponda al Id de la BD.
	 * @param id
	 * @return Receta
	 */
	public Receta findOne(Long id);
	
	/**
	 * Método que trae el elemento de la BD cuyo nombre coincida con el parámetro "term".
	 * @param term
	 * @return List<Receta>
	 */
	public Receta findByNombre(String term);
	
	/**
	 * Método que elimina el item que corresponda al id de la BD.
	 * @param id
	 */
	public void delete(Long id);
	
	/**
	 * Método que guarda en la DB el objeto pasado como parámetro.
	 * @param Receta
	 */
	public void save(Receta receta);
	
	/**
	 * Método que trae todos los elementos de la BD.
	 * @param pageable
	 * @return Page<Receta>
	 */
	public Page<Receta> findAll(Pageable pageable);
	
	/**
	 * Método que trae todos los elementos de la BD que corresponden al usuario.
	 * @param autorId
	 *  * @param pageable
	 * @return Page<Receta>
	 */
	public Page<Receta> findAllByAutor(String autorId, Pageable pageable);
	
	/**
	 * Método que trae todos los elementos de la BD catalogados como públicos.
	 * @return List<Receta>
	 */
	public List<Receta> findAllByEsPublicaTrue();
	
	
	/**
	 * Método que trae todos los elementos de la BD catalogados como públicos o que pertenecen al usuario.
	 * @return List<Receta>
	 */
	public List<Receta> findAllByEsPublicaTrueOrAutorOrIdIn(String autor, List<Long> recetasIds);
	
	/**
	 * Método que trae todos los elementos de la BD catalogados como públicos o que pertenecen al usuario.
	 * @return List<Receta>
	 */
	public Page<Receta> findAllByEsPublicaTrueOrAutorOrIdIn(String autor, List<Long> recetasIds, Pageable pageable);
	
	/**
	 * Método que trae todos los elementos de la BD catalogados como privados.
	 * @return List<Receta>
	 */
	public List<Receta> findAllByEsPublicaFalse();
	
	/**
	 * Método que trae todas las recetas de la BD en la que el autor está invitado.
	 * @param sort
	 * @return Page<Receta>
	 */
	public Page<Receta> findByIdIn(List<Long> recetasIds, Pageable pageable); 
	
	public Long findTop1ById();
	
}
