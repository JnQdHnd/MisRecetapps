package miRecetApp.app.service.implementation;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import miRecetApp.app.model.dao.IGastoDivisibleDao;
import miRecetApp.app.model.entity.GastoDivisible;

import miRecetApp.app.service.IGastoDivisibleService;

/**
 * @author Julián Quenard *
 * 15 jul. 2022
 */
@Service
public class GastoDivisibleServiceImplementa implements IGastoDivisibleService {
	
	@Autowired
	IGastoDivisibleDao gastosDivisiblesDao;
	
	@Transactional(readOnly = true)
	@Override
	public List<GastoDivisible> findAll() {
		
		return (List<GastoDivisible>) gastosDivisiblesDao.findAll();
	}
	
	@Transactional(readOnly = true)
	@Override
	public List<GastoDivisible> findAll(Sort sort) {
		return (List<GastoDivisible>) gastosDivisiblesDao.findAll(sort);
	}
	
	@Transactional(readOnly = true)
	@Override
	public GastoDivisible findOne(Long id) {
		return gastosDivisiblesDao.findById(id).orElse(null);
	}
	
	@Transactional(readOnly = true)
	@Override
	public GastoDivisible findByNombre(String term) {
		return gastosDivisiblesDao.findByNombreLikeIgnoreCase("%" + term + "%");
	}
	
	@Transactional
	@Override
	public void delete(Long id) {
		gastosDivisiblesDao.deleteById(id);
	}
	
	@Transactional
	@Override
	public void save(GastoDivisible gastosDivisibles) {
		gastosDivisiblesDao.save(gastosDivisibles);
	}
	
	@Transactional(readOnly = true)
	@Override
	public Page<GastoDivisible> findAll(Pageable pageable) {
		return gastosDivisiblesDao.findAll(pageable);
	}

}
