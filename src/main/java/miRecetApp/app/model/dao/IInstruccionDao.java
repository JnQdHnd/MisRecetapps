package miRecetApp.app.model.dao;

import org.springframework.data.repository.CrudRepository;

import miRecetApp.app.model.entity.Instruccion;

public interface IInstruccionDao extends CrudRepository<Instruccion, Long> {
}
