package miRecetApp.app.model.dao;

import org.springframework.data.repository.PagingAndSortingRepository;

import miRecetApp.app.model.entity.Artefacto;

/**
 * @author Julián Quenard *
 * 01-09-2021
 * @see {@link PagingAndSortingRepository}
 */
public interface IArtefactoDao extends PagingAndSortingRepository<Artefacto, Long> {
	public Artefacto findByNombreLikeIgnoreCase(String term);
	public boolean existsByNombre(String nombre);
}
