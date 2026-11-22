package Interfaces;

import java.sql.SQLException;
import java.util.List;

public interface DAO <T> {

int insertar(T e)throws SQLException;

boolean modificar(T e)throws SQLException;

boolean DardeBaja(T e)throws SQLException;

boolean reactivar(T e)throws SQLException;

boolean eliminar(T e)throws SQLException;

List<T> listar()throws SQLException;
}
