package miRecetApp.app.model.dao;

import org.springframework.data.repository.CrudRepository;
import miRecetApp.app.model.entity.ArtefactoEnUso;

/**
 * @author Julián Quenard *
 * 01-09-2021
 * @see {@link CrudRepository}
 */
public interface IArtefactoEnUsoDao extends CrudRepository<ArtefactoEnUso, Long> {
}
