package miRecetApp.app.model.dao;


import org.springframework.data.repository.PagingAndSortingRepository;
import miRecetApp.app.model.entity.Preparacion;

/**
 * @author Julián Quenard *
 * 01-09-2021
 * @see {@link PagingAndSortingRepository}
 */
public interface IPreparacionDao extends PagingAndSortingRepository<Preparacion, Long> {
	
	public Preparacion findByRecetaId(Long recetaId);
	
}
