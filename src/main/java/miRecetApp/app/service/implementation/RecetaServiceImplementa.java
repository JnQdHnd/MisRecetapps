package miRecetApp.app.service.implementation;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import miRecetApp.app.model.dao.IRecetaDao;
import miRecetApp.app.model.entity.Receta;
import miRecetApp.app.service.IRecetaService;

@Service
public class RecetaServiceImplementa implements IRecetaService {
	
	@Autowired
	IRecetaDao recetaDao;
	
	@Transactional(readOnly = true)	
	@Override
	public List<Receta> findAll() {
		
		return (List<Receta>) recetaDao.findAll();
	}
	
	@Transactional(readOnly = true)
	@Override
	public List<Receta> findAll(Sort sort) {
		return (List<Receta>) recetaDao.findAll(sort);
	}
	
	@Transactional(readOnly = true)
	@Override
	public Receta findOne(Long id) {
		return recetaDao.findById(id).orElse(null);
	}
	
	@Transactional(readOnly = true)
	@Override
	public Receta findByNombre(String term) {
		return recetaDao.findByNombreLikeIgnoreCase("%" + term + "%");
	}
	@Transactional
	@Override
	public void delete(Long id) {
		recetaDao.deleteById(id);
	}
	@Transactional
	@Override
	public void save(Receta receta) {
		recetaDao.save(receta);
	}
	
	@Transactional(readOnly = true)
	@Override
	public Page<Receta> findAll(Pageable pageable) {
		return recetaDao.findAll(pageable);
	}
	
	@Transactional(readOnly = true)
	@Override
	public List<Receta> findAllByAutor(String autor) {
		return recetaDao.findAllByAutor(autor);
	}

	@Transactional(readOnly = true)
	@Override
	public List<Receta> findAllByAutor(String autor, Sort sort) {
		return recetaDao.findAllByAutor(autor, sort);
	}

	@Transactional(readOnly = true)
	@Override
	public Page<Receta> findAllByAutor(String autor, Pageable pageable) {
		return recetaDao.findAllByAutor(autor, pageable);
	}

	@Transactional(readOnly = true)
	@Override
	public List<Receta> findByIdIn(List<Long> recetasIds) {
		return recetaDao.findByIdIn(recetasIds);
	}

	@Transactional(readOnly = true)
	@Override
	public List<Receta> findAllByEsPublicaTrue() {
		return recetaDao.findAllByEsPublicaTrue();
	}

	@Transactional(readOnly = true)
	@Override
	public List<Receta> findAllByEsPublicaFalse() {
		return recetaDao.findAllByEsPublicaFalse();
	}

	@Transactional(readOnly = true)
	@Override
	public List<Receta> findAllByEsPublicaTrueOrAutorOrIdIn(String autor, List<Long> recetasIds) {
		return recetaDao.findAllByEsPublicaTrueOrAutorOrIdIn(autor, recetasIds);
	}

	@Transactional(readOnly = true)
	@Override
	public Page<Receta> findAllByEsPublicaTrueOrAutorOrIdIn(String autor, List<Long> recetasIds, Pageable pageable) {
		return recetaDao.findAllByEsPublicaTrueOrAutorOrIdIn(autor, recetasIds, pageable);
	}
	
	@Transactional(readOnly = true)
	@Override
	public Page<Receta> findByIdIn(List<Long> recetasIds, Pageable pageable) {
		return recetaDao.findByIdIn(recetasIds, pageable);
	}

	@Override
	public Long findTop1ById() {
		// TODO Auto-generated method stub
		return null;
	}
	

}
