package miRecetApp.app.model.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import miRecetApp.app.model.entity.Receta;

public interface IRecetaDao extends PagingAndSortingRepository<Receta, Long>{
	
	public Receta findByNombreLikeIgnoreCase(String term);
	
	public List<Receta> findAllByAutor(String autor);
	
	public List<Receta> findAllByEsPublicaTrueOrAutorOrIdIn(String autor, List<Long> recetasIds);
	
	public List<Receta> findAllByEsPublicaTrue();
	
	public List<Receta> findAllByEsPublicaFalse();
	
	public List<Receta> findByIdIn(List<Long> recetasIds);
	
	public Page<Receta> findByIdIn(List<Long> recetasIds, Pageable pageable);
	
	public List<Receta> findAllByAutor(String autor, Sort sort);
	
	public Page<Receta> findAllByAutor(String autor, Pageable pageable);
	
	public Page<Receta> findAllByEsPublicaTrueOrAutorOrIdIn(String autor, List<Long> recetasIds, Pageable pageable);
	
	

}
