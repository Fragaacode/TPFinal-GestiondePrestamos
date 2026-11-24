package Persistencia;

import Conexion.ConexionBD;
import Interfaces.DAO;
import Modelos.Cuota;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CuotaDAO implements DAO<Cuota> {

    public int insertar(Cuota cuota)throws SQLException
    {
        String sql="INSERT INTO cuota (id_prestamo,numero,monto,fechaVenciomento,pagada,fechaPago,activo) VALUES(?,?,?,?,?,?,TRUE)";
        String sqlid="SELECT LAST_INSERT_ID()";

        try(Connection c = ConexionBD.conectar();
            PreparedStatement ps= c.prepareStatement(sql);
            PreparedStatement psid = c.prepareStatement(sqlid))
        {
            ps.setInt(1, cuota.getIdPrestamo());
            ps.setInt(2, cuota.getNumero());
            ps.setDouble(3, cuota.getMonto());
            ps.setDate(4, Date.valueOf(cuota.getFechaVencimiento()));
            ps.setBoolean(5, cuota.isPagada());
            ps.setDate(6, cuota.getFechaPago() != null ? Date.valueOf(cuota.getFechaPago()) : null);
            ps.executeUpdate();

            try (ResultSet rs = psid.executeQuery()) {
                if (rs.next()) {
                    int ultimoId = rs.getInt(1);
                    cuota.setIdCuota(ultimoId);
                    return ultimoId;
                }
            }
        }
        return -1;
    }

    public boolean modificar(Cuota cuota)throws SQLException
    {
        String sql = "UPDATE cuota SET monto = ?, fechaVenciomento = ?, pagada = ?, fechaPago = ? WHERE id_cuota = ?";
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDouble(1, cuota.getMonto());
            ps.setDate(2, Date.valueOf(cuota.getFechaVencimiento()));
            ps.setBoolean(3, cuota.isPagada());
            ps.setDate(4, cuota.getFechaPago() != null ? Date.valueOf(cuota.getFechaPago()) : null);
            ps.setInt(5, cuota.getIdCuota());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean DardeBaja(Cuota cuota)throws SQLException
    {
        String sql="UPDATE cuota SET activo=? WHERE id_cuota=?";
        try(Connection c = ConexionBD.conectar();
            PreparedStatement ps = c.prepareStatement(sql))
        {
            ps.setBoolean(1,false);
            ps.setInt(2,cuota.getIdCuota());

            return ps.executeUpdate()>0;
        }
    }

    @Override
    public boolean reactivar(Cuota cuota) throws SQLException {
        String sql="UPDATE cuota SET activo=? WHERE id_cuota=?";
        try(Connection c = ConexionBD.conectar();
            PreparedStatement ps = c.prepareStatement(sql))
        {
            ps.setBoolean(1,true);
            ps.setInt(2,cuota.getIdCuota());

            return ps.executeUpdate()>0;
        }
    }

    @Override
    public boolean eliminar(Cuota cuota) throws SQLException {
        String sql = "DELETE FROM cuota WHERE id_cuota = ?";
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, cuota.getIdCuota());
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public List<Cuota> listar() throws SQLException {
        List<Cuota> cuotas = new ArrayList<>();
        String sql = "SELECT * FROM cuota ORDER BY id_prestamo, numero";
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Date fechaPago = rs.getDate("fechaPago");
                Cuota cuota = new Cuota(
                        rs.getInt("id_cuota"),
                        rs.getInt("id_prestamo"),
                        rs.getInt("numero"),
                        rs.getDouble("monto"),
                        rs.getDate("fechaVenciomento").toLocalDate(),
                        rs.getBoolean("pagada"),
                        fechaPago != null ? fechaPago.toLocalDate() : null,
                        rs.getBoolean("activo"));
                cuotas.add(cuota);
            }
        }
        return cuotas;
    }
}
