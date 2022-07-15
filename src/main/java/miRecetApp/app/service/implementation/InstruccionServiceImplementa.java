package miRecetApp.app.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import miRecetApp.app.model.dao.IInstruccionDao;
import miRecetApp.app.service.IInstruccionService;

/**
 * @author Julián Quenard *
 * 15 jul. 2022
 */
@Service
public class InstruccionServiceImplementa implements IInstruccionService {	
	@Autowired
	IInstruccionDao instruccionDao;

	@Override
	public void delete(Long id) {
		instruccionDao.deleteById(id);
	}
}
