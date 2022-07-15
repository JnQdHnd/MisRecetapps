package miRecetApp.app.model.dao;

import org.springframework.data.repository.PagingAndSortingRepository;
import miRecetApp.app.model.entity.GastoIndivisible;

/**
 * @author Julián Quenard *
 * 01-09-2021
 * @see {@link PagingAndSortingRepository}
 */
public interface IGastoIndivisibleDao extends PagingAndSortingRepository<GastoIndivisible, Long> {

	public GastoIndivisible findByNombreLikeIgnoreCase(String term);
}
