package Persistencia;

import Conexion.ConexionBD;
import Interfaces.DAO;
import Modelos.Administrador;
import Modelos.Asesor;
import Modelos.Empleado;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Enum.Rol;

public class EmpleadoDAO implements DAO<Empleado> {
    public EmpleadoDAO() {
        super();
    }

    @Override
    public int insertar(Empleado empleado) throws SQLException {
        String sql="INSERT INTO empleado (nombre,apellido,dni,usuario,contraseña,sueldoBase,rol,cantidadSolicitudes,activo) VALUES (?,?,?,?,?,?,?,?,TRUE)";
        String sqlid="SELECT LAST_INSERT_ID()";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             PreparedStatement psId = con.prepareStatement(sqlid)) {

            ps.setString(1, empleado.getNombre());
            ps.setString(2, empleado.getApellido());
            ps.setString(3, empleado.getDni());
            ps.setString(4, empleado.getUsuario());
            ps.setString(5, empleado.getContraseña());
            ps.setDouble(6, empleado.getSueldoBase());
            ps.setString(7, empleado.getRol().name());
            ps.setInt(8, cantidadSolicitudes(empleado));
            ps.executeUpdate();

            try (ResultSet rs = psId.executeQuery()) {
                if (rs.next()) {
                    int ultimoId = rs.getInt(1);
                    empleado.setId(ultimoId);
                    return ultimoId;
                }
            }
        }
        return -1;
    }

    private int cantidadSolicitudes(Empleado empleado)
    {
        if (empleado instanceof Asesor asesor) {
            return asesor.getCantidadSolicitudes();
        }
        return 0;
    }


    @Override
    public boolean modificar(Empleado empleado) throws SQLException {
        String sql = "UPDATE empleado SET nombre = ?, apellido = ?, contraseña = ?, sueldoBase = ?,cantidadSolicitudes = ? WHERE id_empleado = ?";
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, empleado.getNombre());
            ps.setString(2, empleado.getApellido());
            ps.setString(3, empleado.getContraseña());
            ps.setDouble(4, empleado.getSueldoBase());
            ps.setInt(5, cantidadSolicitudes(empleado));
            ps.setInt(6, empleado.getId());
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean DardeBaja(Empleado empleado) throws SQLException {
        String sql="UPDATE empleado SET activo=? WHERE id_empleado=?";
        try(Connection c = ConexionBD.conectar();
        PreparedStatement ps = c.prepareStatement(sql))
        {
            ps.setBoolean(1, false);
            ps.setInt(2, empleado.getId());
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean reactivar(Empleado empleado) throws SQLException {
        String sql="UPDATE empleado SET activo=? WHERE id_empleado=?";
        try(Connection c = ConexionBD.conectar();
            PreparedStatement ps = c.prepareStatement(sql))
        {
            ps.setBoolean(1, true);
            ps.setInt(2, empleado.getId());
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean eliminar(Empleado empleado) throws SQLException {
        String sql = "DELETE FROM empleado WHERE id_empleado = ?";
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, empleado.getId());
            return ps.executeUpdate() > 0;
    }
    }
    @Override
    public List<Empleado> listar() throws SQLException {
        List<Empleado> empleados = new ArrayList<>();
        String sql = "SELECT * FROM empleado ORDER BY rol, apellido";
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                empleados.add(crearEmpleado(rs));
            }
        }
        return empleados;
    }
    private Empleado crearEmpleado(ResultSet rs) throws SQLException {
        int id = rs.getInt("id_empleado");
        String nombre = rs.getString("nombre");
        String apellido = rs.getString("apellido");
        String dni = rs.getString("dni");
        boolean activo = rs.getBoolean("activo");
        String usuario = rs.getString("usuario");
        String contraseña = rs.getString("contraseña");
        double sueldo = rs.getDouble("sueldoBase");
        Rol rol = Rol.valueOf(rs.getString("rol"));

        if (rol == Rol.ADMINISTRADOR) {
            return new Administrador(id, nombre, apellido, dni, activo, usuario, contraseña, sueldo);
        }
        return new Asesor(id, nombre, apellido, dni, activo, usuario, contraseña, sueldo,
                rs.getInt("cantidadSolicitudes"));
    }
}



