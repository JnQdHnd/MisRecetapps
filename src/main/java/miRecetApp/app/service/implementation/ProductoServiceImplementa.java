package miRecetApp.app.service.implementation;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import miRecetApp.app.model.dao.IProductoDao;
import miRecetApp.app.model.entity.Producto;
import miRecetApp.app.service.IProductoService;

/**
 * @author Julián Quenard *
 * 15 jul. 2022
 */
@Service
public class ProductoServiceImplementa implements IProductoService {
	
	@Autowired
	IProductoDao productoDao;
	
	@Transactional(readOnly = true)	
	@Override
	public List<Producto> findAll() {
		
		return (List<Producto>) productoDao.findAll();
	}
	
	@Transactional(readOnly = true)
	@Override
	public List<Producto> findAll(Sort sort) {
		return (List<Producto>) productoDao.findAll(sort);
	}
	
	@Transactional(readOnly = true)
	@Override
	public Producto findOne(Long id) {
		return productoDao.findById(id).orElse(null);
	}
	
	@Transactional(readOnly = true)
	@Override
	public Producto findByNombre(String term) {
		return productoDao.findByNombreLikeIgnoreCase(term);
	}
	@Transactional
	@Override
	public void delete(Long id) {
		productoDao.deleteById(id);
	}
	@Transactional
	@Override
	public void save(Producto producto) {
		productoDao.save(producto);
	}
	
	@Transactional(readOnly = true)
	@Override
	public Producto findByCodigoDeBarra(String codigoDeBarra) {
		return productoDao.findByCodigoDeBarra(codigoDeBarra);
	}
	
	@Transactional(readOnly = true)
	@Override
	public Page<Producto> findAll(Pageable pageable) {
		return productoDao.findAll(pageable);
	}

	@Override
	public boolean existsByNombre(String nombre) {
		return productoDao.existsByNombre(nombre);
	}
}
