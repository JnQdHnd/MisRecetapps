package miRecetApp.app.model.dao;


import org.springframework.data.repository.CrudRepository;

import miRecetApp.app.model.entity.Role;

public interface IRoleDao extends CrudRepository<Role, Long> {
	public Role findByAuthority(String authority);
}
