package Persistencia;

import Conexion.ConexionBD;
import Interfaces.DAO;
import Modelos.Cliente;

import javax.xml.transform.Result;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO implements DAO<Cliente>{

public int insertar(Cliente cliente)throws SQLException
{
    String sql="INSERT INTO clientes (nombre,apellido,dni,telefono,email,direccion,activo) VALUES(?,?,?,?,?,?,TRUE)";
    String sqlget="SELECT LAST_INSERT_ID()";

    try(Connection c = ConexionBD.conectar();
        PreparedStatement ps = c.prepareStatement(sql);
        PreparedStatement psid= c.prepareStatement(sqlget))
    {
        ps.setString(1,cliente.getNombre());
        ps.setString(2,cliente.getApellido());
        ps.setString(3,cliente.getDni());
        ps.setString(4,cliente.getTelefono());
        ps.setString(5,cliente.getEmail());
        ps.setString(6,cliente.getDireccion());

        ps.executeUpdate();

        try(ResultSet rs = psid.executeQuery())
        {
            if(rs.next())
            {
                int ultimoID=rs.getInt(1);
                cliente.setId(ultimoID);
                return ultimoID;
            }
        }


    }
    return -1;
}

public boolean modificar(Cliente cliente)throws SQLException
{
    String sql="UPDATE clientes SET nombre=?,apellido=?,telefono=?,email=?,direccion=? WHERE id=?";

    try(Connection c = ConexionBD.conectar();
    PreparedStatement ps = c.prepareStatement(sql))
    {
        ps.setString(1,cliente.getNombre());
        ps.setString(2,cliente.getApellido());
        ps.setString(3,cliente.getTelefono());
        ps.setString(4,cliente.getEmail());
        ps.setString(5,cliente.getDireccion());
        ps.setInt(6,cliente.getId());

        return ps.executeUpdate()>0;
    }
}


public List<Cliente> listar()throws SQLException
{
List <Cliente> clientes =new ArrayList<>();

String sql="SELECT * FROM clientes";
try(Connection c = ConexionBD.conectar();
    PreparedStatement ps = c.prepareStatement(sql);
    ResultSet rs = ps.executeQuery())
 {
    while(rs.next())
    {
        Cliente nuevo =new Cliente(
                rs.getInt("id"),
                rs.getString("nombre"),
                rs.getString("apellido"),
                rs.getString("dni"),
                rs.getBoolean("activo"),
                rs.getString("telefono"),
                rs.getString("email"),
                rs.getString("direccion")
        );

        clientes.add(nuevo);
    }
 }
return clientes;
}

public boolean DardeBaja(Cliente cliente)throws SQLException
{
    String sql="UPDATE clientes SET activo=? WHERE id=?";
    try(Connection c = ConexionBD.conectar();
        PreparedStatement ps = c.prepareStatement(sql))
    {
        ps.setBoolean(1,false);
        ps.setInt(2,cliente.getId());

        return ps.executeUpdate()>0;
    }

}

public boolean reactivar(Cliente cliente)throws SQLException
    {
        String sql="UPDATE clientes SET activo=? WHERE id=?";
        try(Connection c = ConexionBD.conectar();
            PreparedStatement ps = c.prepareStatement(sql))
        {
            ps.setBoolean(1,true);
            ps.setInt(2,cliente.getId());

            return ps.executeUpdate()>0;
        }

    }

    public boolean eliminar(Cliente cliente)throws SQLException
    {
        String sql="DELETE FROM clientes WHERE id=?";
        try(Connection c = ConexionBD.conectar();
            PreparedStatement ps = c.prepareStatement(sql))
        {
            ps.setInt(1,cliente.getId());

            return ps.executeUpdate()>0;
        }
    }
}
