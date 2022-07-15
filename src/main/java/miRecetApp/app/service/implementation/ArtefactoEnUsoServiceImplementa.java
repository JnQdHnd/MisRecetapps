package miRecetApp.app.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import miRecetApp.app.model.dao.IArtefactoEnUsoDao;
import miRecetApp.app.service.IArtefactoEnUsoService;

/**
 * @author Julián Quenard *
 * 15 jul. 2022
 */
@Service
public class ArtefactoEnUsoServiceImplementa implements IArtefactoEnUsoService {
	
	@Autowired
	IArtefactoEnUsoDao artefactoEnUsoDao;
	
	@Override
	public void delete(Long id) {
		artefactoEnUsoDao.deleteById(id);		
	}


}
