package miRecetApp.app.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import miRecetApp.app.model.entity.ManoDeObra;

/**
 * @author Julián Quenard *
 * 15 jul. 2022
 */
public interface IManoDeObraService {
	/**
	 * Método que trae todos los elementos de la BD.
	 * @return List<ManoDeObra>
	 */
	public List<ManoDeObra> findAll();
	
	/**
	 * Método que trae todos los elementos de la BD y los ordena según el criterio indicado "sort".
	 * @param sort
	 * @return List<ManoDeObra>
	 */
	public List<ManoDeObra> findAll(Sort sort); 
	
	/**
	 * Método que trae el item que corresponda al Id de la BD.
	 * @param id
	 * @return ManoDeObra
	 */
	public ManoDeObra findOne(Long id);
	
	/**
	 * Método que trae el elemento de la BD cuyo nombre coincida con el parámetro "term".
	 * @param term
	 * @return List<ManoDeObra>
	 */
	public ManoDeObra findByNombre(String term);
	
	/**
	 * Método que elimina el item que corresponda al id de la BD.
	 * @param id
	 */
	public void delete(Long id);
	
	/**
	 * Método que guarda en la DB el objeto pasado como parámetro.
	 * @param ManoDeObra
	 */
	public void save(ManoDeObra manoDeObra);
	
	/**
	 * Método que trae todos los elementos de la BD.
	 * @param pageable
	 * @return Page<ManoDeObra>
	 */
	public Page<ManoDeObra> findAll(Pageable pageable);
	
}
