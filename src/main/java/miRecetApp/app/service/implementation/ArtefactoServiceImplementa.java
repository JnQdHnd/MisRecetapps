package miRecetApp.app.service.implementation;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import miRecetApp.app.model.dao.IArtefactoDao;
import miRecetApp.app.model.entity.Artefacto;
import miRecetApp.app.service.IArtefactoService;

@Service
public class ArtefactoServiceImplementa implements IArtefactoService {
	
	@Autowired
	IArtefactoDao artefactoDao;
	
	@Transactional(readOnly = true)
	@Override
	public List<Artefacto> findAll() {
		
		return (List<Artefacto>) artefactoDao.findAll();
	}

	@Transactional(readOnly = true)
	@Override
	public List<Artefacto> findAll(Sort sort) {
		return (List<Artefacto>) artefactoDao.findAll(sort);
	}

	@Transactional(readOnly = true)
	@Override
	public Artefacto findOne(Long id) {
		return artefactoDao.findById(id).orElse(null);
	}
	
	@Transactional(readOnly = true)
	@Override
	public Artefacto findByNombre(String term) {
		return artefactoDao.findByNombreLikeIgnoreCase("%" + term + "%");
	}
	
	@Transactional
	@Override
	public void delete(Long id) {
		artefactoDao.deleteById(id);
	}
	
	@Transactional
	@Override
	public void save(Artefacto artefacto) {
		artefactoDao.save(artefacto);
	}

	@Transactional(readOnly = true)
	@Override
	public Page<Artefacto> findAll(Pageable pageable) {
		return artefactoDao.findAll(pageable);
	}
	
	

}
