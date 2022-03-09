package miRecetApp.app.service.implementation;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import miRecetApp.app.model.dao.IGastoIndivisibleDao;
import miRecetApp.app.model.entity.GastoIndivisible;
import miRecetApp.app.service.IGastoIndivisibleService;

@Service
public class GastoIndivisibleServiceImplementa implements IGastoIndivisibleService {
	
	@Autowired
	IGastoIndivisibleDao gastosIndivisiblesDao;
	
	@Transactional(readOnly = true)	
	@Override
	public List<GastoIndivisible> findAll() {
		
		return (List<GastoIndivisible>) gastosIndivisiblesDao.findAll();
	}
	
	@Transactional(readOnly = true)
	@Override
	public List<GastoIndivisible> findAll(Sort sort) {
		return (List<GastoIndivisible>) gastosIndivisiblesDao.findAll(sort);
	}
	
	@Transactional(readOnly = true)
	@Override
	public GastoIndivisible findOne(Long id) {
		return gastosIndivisiblesDao.findById(id).orElse(null);
	}
	
	@Transactional(readOnly = true)
	@Override
	public GastoIndivisible findByNombre(String term) {
		return gastosIndivisiblesDao.findByNombreLikeIgnoreCase("%" + term + "%");
	}
	
	@Transactional
	@Override
	public void delete(Long id) {
		gastosIndivisiblesDao.deleteById(id);
	}
	
	@Transactional
	@Override
	public void save(GastoIndivisible gastosIndivisibles) {
		gastosIndivisiblesDao.save(gastosIndivisibles);
	}
	
	@Transactional(readOnly = true)
	@Override
	public Page<GastoIndivisible> findAll(Pageable pageable) {
		return gastosIndivisiblesDao.findAll(pageable);
	}

}
