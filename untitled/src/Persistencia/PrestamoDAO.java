package Persistencia;

import Conexion.ConexionBD;
import Interfaces.DAO;
import Modelos.Cliente;
import Modelos.Prestamo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import Enum.EstadoPrestamo;

public class PrestamoDAO implements DAO <Prestamo> {
    @Override
    public int insertar(Prestamo prestamo) throws SQLException {
        String sql = "INSERT INTO prestamo (id_cliente, id_asesor, montoSolicitado, cantidadCuotas,fechaSolicitud, estado, activo) VALUES (?, ?, ?, ?, ?, ?, TRUE)";
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
    public boolean modificar(Prestamo prestamo) throws SQLException {
        String sql = "UPDATE prestamo SET montoSolicitado = ?, cantidadCuotas = ?, estado = ? WHERE id_prestamo = ?";
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDouble(1, prestamo.getMontoSolicitado());
            ps.setInt(2, prestamo.getCantidadCuotas());
            ps.setString(3, prestamo.getEstado().name());
            ps.setInt(4, prestamo.getIdPrestamo());
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean DardeBaja(Prestamo prestamo) throws SQLException {
        String sql = "UPDATE prestamo SET activo = ? WHERE id_prestamo = ?";
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setBoolean(1, false);
            ps.setInt(2, prestamo.getIdPrestamo());
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean reactivar(Prestamo prestamo) throws SQLException {
        String sql = "UPDATE prestamo SET activo = ? WHERE id_prestamo = ?";
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setBoolean(1, true);
            ps.setInt(2, prestamo.getIdPrestamo());
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean eliminar(Prestamo prestamo) throws SQLException {
        String sql = "DELETE FROM prestamo WHERE id_prestamo = ?";
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, prestamo.getIdPrestamo());
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public List<Prestamo> listar() throws SQLException {
        List<Prestamo> prestamos = new ArrayList<>();
        String sql = "SELECT * FROM prestamo ORDER BY fechaSolicitud DESC";
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Cliente soloId = new Cliente();
                soloId.setId(rs.getInt("id_cliente"));

                Prestamo prestamo = new Prestamo(
                        rs.getInt("id_prestamo"),
                        soloId,
                        rs.getInt("id_asesor"),
                        rs.getDouble("montoSolicitado"),
                        rs.getInt("cantidadCuotas"),
                        rs.getDate("fechaSolicitud").toLocalDate(),
                        EstadoPrestamo.valueOf(rs.getString("estado")),
                        rs.getBoolean("activo"));
                prestamos.add(prestamo);
            }
        }
        return prestamos;
    }
}
