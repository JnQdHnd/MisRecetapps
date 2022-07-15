package miRecetApp.app.model.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import miRecetApp.app.model.entity.GastoDivisible;

/**
 * @author Julián Quenard *
 * 01-09-2021
 * @see {@link CrudRepository}
 */
public interface IGastoDivisibleDao extends PagingAndSortingRepository<GastoDivisible, Long> {

	public GastoDivisible findByNombreLikeIgnoreCase(String term);
}
