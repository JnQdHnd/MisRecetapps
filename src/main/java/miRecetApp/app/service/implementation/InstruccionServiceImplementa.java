package miRecetApp.app.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import miRecetApp.app.model.dao.IInstruccionDao;
import miRecetApp.app.service.IInstruccionService;

@Service
public class InstruccionServiceImplementa implements IInstruccionService {	
	@Autowired
	IInstruccionDao instruccionDao;

	@Override
	public void delete(Long id) {
		instruccionDao.deleteById(id);
	}
}
