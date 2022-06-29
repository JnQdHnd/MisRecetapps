package miRecetApp.app.model.dao;

import org.springframework.data.repository.PagingAndSortingRepository;

import miRecetApp.app.model.entity.Preparacion;
import miRecetApp.app.model.entity.Receta;

public interface IPreparacionDao extends PagingAndSortingRepository<Preparacion, Long> {
	
	public Preparacion findByRecetaId(Long recetaId);
	
}
