package Conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {
    private static final String url="";
    private static final String user="";
    private static final String password="159753";

    public static Connection conectar()throws SQLException
    {
        return DriverManager.getConnection(url,user,password);
    }

}
