package Interfaces;

import java.sql.SQLException;
import java.util.List;

public interface DAO <T> {

void insertar(T e)throws SQLException;

void modificar(T e)throws SQLException;

void DardeBaja(T e)throws SQLException;

List<T> listar()throws SQLException;
}
