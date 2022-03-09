package miRecetApp.app.model.dao;

import org.springframework.data.repository.PagingAndSortingRepository;
import miRecetApp.app.model.entity.ManoDeObra;

public interface IManoDeObraDao extends PagingAndSortingRepository<ManoDeObra, Long> {

	public ManoDeObra findByNombreLikeIgnoreCase(String term);
}
