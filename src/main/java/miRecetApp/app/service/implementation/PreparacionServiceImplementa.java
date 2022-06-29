package miRecetApp.app.service.implementation;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import miRecetApp.app.model.dao.IPreparacionDao;
import miRecetApp.app.model.entity.Preparacion;
import miRecetApp.app.service.IPreparacionService;

@Service
public class PreparacionServiceImplementa implements IPreparacionService {
	
	@Autowired
	IPreparacionDao preparacionDao;
	
	@Transactional(readOnly = true)
	@Override
	public List<Preparacion> findAll() {
		
		return (List<Preparacion>) preparacionDao.findAll();
	}

	@Transactional(readOnly = true)
	@Override
	public List<Preparacion> findAll(Sort sort) {
		return (List<Preparacion>) preparacionDao.findAll(sort);
	}

	@Transactional(readOnly = true)
	@Override
	public Preparacion findOne(Long id) {
		return preparacionDao.findById(id).orElse(null);
	}
	
	@Transactional
	@Override
	public void delete(Long id) {
		preparacionDao.deleteById(id);
	}
	
	@Transactional
	@Override
	public void save(Preparacion preparacion) {
		preparacionDao.save(preparacion);
	}
	
	@Transactional(readOnly = true)
	@Override
	public Preparacion findByRecetaId(Long recetaId) {
		return preparacionDao.findByRecetaId(recetaId);
	}

}
