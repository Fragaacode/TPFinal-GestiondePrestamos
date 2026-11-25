import Excepciones.DatosInvalidosException;
import Menus.Consola;
import Menus.MenuAdministrador;
import Menus.MenuAsesor;
import Modelos.Administrador;
import Modelos.Asesor;
import Modelos.Empleado;
import Servicio.ClienteServicio;
import Servicio.CuotaServicio;
import Servicio.EmpleadoServicio;
import Servicio.PrestamoServicio;
import Enum.Rol;

import java.sql.SQLException;

public class Main {
    private static ClienteServicio clienteServicio;
    private static EmpleadoServicio empleadoServicio;
    private static CuotaServicio cuotaServicio;
    private static PrestamoServicio prestamoServicio;


    public static void main(String[] args)
    {
        try
        {
            clienteServicio = new ClienteServicio();
            empleadoServicio = new EmpleadoServicio();
            cuotaServicio = new CuotaServicio();
            prestamoServicio = new PrestamoServicio(clienteServicio, empleadoServicio, cuotaServicio);
        } catch (SQLException e) {
            System.err.println("ERROR al cargar los datos de la BDD" + e.getMessage());
            return;
        }

        int opcion = 1;
        while (opcion != 0) {
            System.out.println("\n====== SISTEMA DE GESTION DE PRESTAMOS ======");
            System.out.println("1- Iniciar sesion");
            System.out.println("0- Salir");
            opcion = Consola.leerentero("Opcion: ");

            if (opcion == 1) {
                Empleado logueado = login();
                if (logueado.getRol() == Rol.ADMINISTRADOR) {
                    new MenuAdministrador((Administrador) logueado, clienteServicio, empleadoServicio, prestamoServicio, cuotaServicio).menu();
                } else if(logueado.getRol() == Rol.ASESOR){
                    new MenuAsesor((Asesor) logueado, clienteServicio, prestamoServicio, cuotaServicio).menu();
                }
                System.out.println("Sesion cerrada.");
            } else if (opcion != 0) {
                System.out.println("Opcion invalida");
            }
        }

    }

    private static Empleado login() {
        while (true) {
            String usuario = Consola.leerTexto("Usuario: ");
            String contraseña = Consola.leerTexto("Contraseña: ");
            try {
                Empleado empleado = empleadoServicio.login(usuario, contraseña);
                System.out.println("Bienvenido al Sistema de Gestion de prestamos" + empleado.getNombre());
                return empleado;
            } catch (DatosInvalidosException e) {
                System.err.println(e.getMessage());
            }
        }
    }



}

