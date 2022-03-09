package miRecetApp.app.model.dao;

import org.springframework.data.repository.PagingAndSortingRepository;
import miRecetApp.app.model.entity.Producto;

public interface IProductoDao extends PagingAndSortingRepository<Producto, Long> {

	public Producto findByNombreLikeIgnoreCase(String term);
	
	public Producto findByCodigoDeBarra(String codigoDeBarra);
}
