package miRecetApp.app.model.dao;

import org.springframework.data.repository.PagingAndSortingRepository;

import miRecetApp.app.model.entity.GastoDivisible;

public interface IGastoDivisibleDao extends PagingAndSortingRepository<GastoDivisible, Long> {

	public GastoDivisible findByNombreLikeIgnoreCase(String term);
}
