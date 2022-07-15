package miRecetApp.app.model.dao;

import org.springframework.data.repository.PagingAndSortingRepository;
import miRecetApp.app.model.entity.ManoDeObra;

/**
 * @author Julián Quenard *
 * 01-09-2021
 * @see {@link PagingAndSortingRepository}
 */
public interface IManoDeObraDao extends PagingAndSortingRepository<ManoDeObra, Long> {

	public ManoDeObra findByNombreLikeIgnoreCase(String term);
}
