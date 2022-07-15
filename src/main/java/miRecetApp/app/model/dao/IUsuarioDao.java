package miRecetApp.app.model.dao;

import org.springframework.data.repository.CrudRepository;

import miRecetApp.app.model.entity.Usuario;

/**
 * Interfaz para gestionar el CRUD a través de CrudRepository
 * @author Julian Quenard
 * 01-09-2021
 * @See {@link CrudRepository}
 *
 */

public interface IUsuarioDao extends CrudRepository<Usuario, Long>{
	public Usuario findByUsername(String username);
}
