package Persistencia;

import Conexion.ConexionBD;
import Interfaces.DAO;
import Modelos.Prestamo;

import java.sql.*;
import java.util.List;

public class PrestamoDAO implements DAO <Prestamo> {
    @Override
    public int insertar(Prestamo prestamo) throws SQLException {
        String sql = "INSERT INTO prestamo (id_cliente, id_asesor, monto_solicitado, cantidad_cuotas, " +
                "fecha_solicitud, estado, activo) VALUES (?, ?, ?, ?, ?, ?, TRUE)";
        String sqlId = "SELECT LAST_INSERT_ID()";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             PreparedStatement psId = con.prepareStatement(sqlId)) {

            ps.setInt(1, prestamo.getCliente().getId());
            ps.setInt(2, prestamo.getIdAsesor());
            ps.setDouble(3, prestamo.getMontoSolicitado());
            ps.setInt(4, prestamo.getCantidadCuotas());
            ps.setDate(5, Date.valueOf(prestamo.getFechaSolicitud()));
            ps.setString(6, prestamo.getEstado().name());
            ps.executeUpdate();

            try (ResultSet rs = psId.executeQuery()) {
                if (rs.next()) {
                    int ultimoId = rs.getInt(1);
                    prestamo.setIdPrestamo(ultimoId);
                    return ultimoId;
                }
            }
        }
        return -1;
    }

    @Override
    public boolean modificar(Prestamo e) throws SQLException {
        return false;
    }

    @Override
    public boolean DardeBaja(Prestamo e) throws SQLException {
        return false;
    }

    @Override
    public boolean reactivar(Prestamo e) throws SQLException {
        return false;
    }

    @Override
    public boolean eliminar(Prestamo e) throws SQLException {
        return false;
    }

    @Override
    public List<Prestamo> listar() throws SQLException {
        return List.of();
    }
}
