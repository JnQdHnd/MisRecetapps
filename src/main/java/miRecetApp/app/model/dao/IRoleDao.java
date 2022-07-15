package miRecetApp.app.model.dao;


import org.springframework.data.repository.CrudRepository;

import miRecetApp.app.model.entity.Role;


/**
 * @author Julián Quenard *
 * 01-09-2021
 * @see {@link CrudRepository}
 */
public interface IRoleDao extends CrudRepository<Role, Long> {
	public Role findByAuthority(String authority);
}
