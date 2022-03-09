package miRecetApp.app.service.implementation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import miRecetApp.app.model.dao.IIngredienteDao;
import miRecetApp.app.model.entity.Ingrediente;
import miRecetApp.app.service.IIngredienteService;

@Service
public class IngredienteServiceImplementa implements IIngredienteService {	
	@Autowired
	IIngredienteDao ingredienteDao;

	@Override
	public void delete(Long id) {
		ingredienteDao.deleteById(id);		
	}

	@Override
	public Ingrediente findById(Long id) {
		
		return ingredienteDao.findById(id).orElse(null);
	}
}
