package miRecetApp.app.model.dao;

import org.springframework.data.repository.CrudRepository;
import miRecetApp.app.model.entity.Ingrediente;

public interface IIngredienteDao extends CrudRepository<Ingrediente, Long> {
}
