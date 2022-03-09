package miRecetApp.app.model.dao;

import org.springframework.data.repository.PagingAndSortingRepository;
import miRecetApp.app.model.entity.GastoIndivisible;

public interface IGastoIndivisibleDao extends PagingAndSortingRepository<GastoIndivisible, Long> {

	public GastoIndivisible findByNombreLikeIgnoreCase(String term);
}
