package miRecetApp.app.service.implementation;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import miRecetApp.app.model.dao.IManoDeObraDao;
import miRecetApp.app.model.entity.ManoDeObra;
import miRecetApp.app.service.IManoDeObraService;

/**
 * @author Julián Quenard *
 * 15 jul. 2022
 */
@Service
public class ManoDeObraServiceImplementa implements IManoDeObraService {
	
	@Autowired
	IManoDeObraDao manoDeObraDao;
	
	@Transactional(readOnly = true)	
	@Override
	public List<ManoDeObra> findAll() {
		
		return (List<ManoDeObra>) manoDeObraDao.findAll();
	}
	
	@Transactional(readOnly = true)
	@Override
	public List<ManoDeObra> findAll(Sort sort) {
		return (List<ManoDeObra>) manoDeObraDao.findAll(sort);
	}
	
	@Transactional(readOnly = true)
	@Override
	public ManoDeObra findOne(Long id) {
		return manoDeObraDao.findById(id).orElse(null);
	}
	
	@Transactional(readOnly = true)
	@Override
	public ManoDeObra findByNombre(String term) {
		return manoDeObraDao.findByNombreLikeIgnoreCase("%" + term + "%");
	}
	@Transactional
	@Override
	public void delete(Long id) {
		manoDeObraDao.deleteById(id);
	}
	@Transactional
	@Override
	public void save(ManoDeObra manoDeObra) {
		manoDeObraDao.save(manoDeObra);
	}

	@Transactional(readOnly = true)
	@Override
	public Page<ManoDeObra> findAll(Pageable pageable) {
		return manoDeObraDao.findAll(pageable);
	}

}
