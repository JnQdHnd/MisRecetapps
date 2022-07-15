package miRecetApp.app.model.dao;

import org.springframework.data.repository.PagingAndSortingRepository;
import miRecetApp.app.model.entity.Producto;

/**
 * @author Julián Quenard *
 * 01-09-2021
 * @see {@link PagingAndSortingRepository}
 */
public interface IProductoDao extends PagingAndSortingRepository<Producto, Long> {

	public Producto findByNombreLikeIgnoreCase(String term);
	
	public Producto findByCodigoDeBarra(String codigoDeBarra);
	
	public boolean existsByNombre(String nombre);
}
