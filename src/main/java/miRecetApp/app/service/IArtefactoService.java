package miRecetApp.app.service;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import miRecetApp.app.model.entity.Artefacto;

public interface IArtefactoService {
	/**
	 * Método que trae todos los elementos de la BD.
	 * @return List<Artefacto>
	 */
	public List<Artefacto> findAll();
	
	/**
	 * Método que trae todos los elementos de la BD y los ordena según el criterio indicado "sort".
	 * @param sort
	 * @return List<Artefacto>
	 */
	public List<Artefacto> findAll(Sort sort); 
	
	/**
	 * Método que trae el item que corresponda al Id de la BD.
	 * @param id
	 * @return Artefacto
	 */
	public Artefacto findOne(Long id);
	
	/**
	 * Método que trae el elemento de la BD cuyo nombre coincida con el parámetro "term".
	 * @param term
	 * @return List<Artefacto>
	 */
	public Artefacto findByNombre(String term);
	
	/**
	 * Método que elimina el item que corresponda al id de la BD.
	 * @param id
	 */
	public void delete(Long id);
	
	/**
	 * Método que guarda en la DB el objeto pasado como parámetro.
	 * @param Artefacto
	 */
	public void save(Artefacto artefacto);
	
	/**
	 * Método que trae todos los elementos de la BD.
	 * @param pageable
	 * @return Page<Artefacto>
	 */
	public Page<Artefacto> findAll(Pageable pageable);
}

