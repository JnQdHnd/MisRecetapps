package miRecetApp.app.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import miRecetApp.app.model.entity.Producto;

/**
 * @author Julián Quenard *
 * 15 jul. 2022
 */
public interface IProductoService {
	/**
	 * Método que trae todos los elementos de la BD.
	 * @return List<Producto>
	 */
	public List<Producto> findAll();
	
	/**
	 * Método que trae todos los elementos de la BD y los ordena según el criterio indicado "sort".
	 * @param sort
	 * @return List<Producto>
	 */
	public List<Producto> findAll(Sort sort); 
	
	/**
	 * Método que trae el item que corresponda al Id de la BD.
	 * @param id
	 * @return Producto
	 */
	public Producto findOne(Long id);
	
	/**
	 * Método que trae el elemento de la BD cuyo nombre coincida con el parámetro "term".
	 * @param term
	 * @return List<Producto>
	 */
	public Producto findByNombre(String term);
	
	/**
	 * Método que elimina el item que corresponda al id de la BD.
	 * @param id
	 */
	public void delete(Long id);
	
	/**
	 * Método que guarda en la DB el objeto pasado como parámetro.
	 * @param Producto
	 */
	public void save(Producto producto);
	
	/**
	 * Método que trae un elemento de la BD, 
	 * cuando coincidan: el Codigo De Barras con el parámetro "codigoDeBarra".
	 * 
	 * @param codigoDeBarra
	 * @return Producto
	 */
	public Producto findByCodigoDeBarra(String codigoDeBarra);
	
	/**
	 * Método que trae todos los elementos de la BD.
	 * @param pageable
	 * @return Page<Producto>
	 */
	public Page<Producto> findAll(Pageable pageable);
	
	/**
	 * Método que verifica la existencia de un elemento en la BD que tenga el nombre indicado.
	 * @param nombre
	 * @return boolean
	 */
	public boolean existsByNombre(String nombre);
	
}
