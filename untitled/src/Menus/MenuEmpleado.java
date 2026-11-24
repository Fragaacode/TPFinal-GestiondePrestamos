package Menus;

import Modelos.Administrador;
import Modelos.Asesor;
import Modelos.Empleado;
import Servicio.EmpleadoServicio;

import java.sql.SQLException;

public class MenuEmpleado {
    private EmpleadoServicio servicio;
    private Empleado empleado;

    public MenuEmpleado(EmpleadoServicio servicio, Empleado empleado) {
        this.servicio = servicio;
        this.empleado = empleado;
    }

    public void mostrar() {
        int opcion;
        do {
            System.out.println("\n===== GESTIÓN DE EMPLEADOS =====");
            System.out.println("1. Alta de empleado");
            System.out.println("2. Modificar empleado");
            System.out.println("3. Baja de empleado");
            System.out.println("4. Consultar empleado por id");
            System.out.println("5. Listar empleados");
            System.out.println("6. Listar empleados dados de baja");
            System.out.println("7. Reactivar empleado");
            System.out.println("8. Eliminar empleado definitivamente");
            System.out.println("0. Volver");
            opcion = Consola.leerentero("Opción: ");

            try {
                switch (opcion) {
                    case 1:
                        ingresar();
                        break;
                    case 2:
                        modificar();
                        break;
                    case 3:
                        baja();
                        break;
                    case 4:
                        System.out.println(empleadoServicio.buscarPorId(Consola.leerEntero("Id: ")));
                        break;
                    case 5:
                        Consola.mostrarLista(empleadoServicio.listar(), "No hay empleados.");
                        break;
                    case 6:
                        Consola.mostrarLista(empleadoServicio.listarInactivos(), "No hay empleados dados de baja.");
                        break;
                    case 7:
                        empleadoServicio.reactivar(Consola.leerEntero("Id del empleado a reactivar: "));
                        System.out.println("Empleado reactivado.");
                        break;
                    case 8:
                        eliminar();
                        break;
                    case 0:
                        break;
                    default:
                        System.out.println("Opción inválida.");
                }
            } catch (SQLException e) {
                System.out.println("Error de base de datos: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        } while (opcion != 0);
    }

    public void ingresar()throws SQLException
    {
        int rol = Consola.leerentero("Ingrese 1-Rol Administrador o 2-Rol Asesor");
        if (rol != 1 && rol != 2) {
                System.out.println("Rol inexistente.");
                return;
            }
        String nombre = Consola.leerTexto("Nombre: ");
        String apellido = Consola.leerTexto("Apellido: ");
        String dni = Consola.leerTexto("DNI: ");
        String usuario = Consola.leerTexto("Usuario: ");
        String contraseña = Consola.leerTexto("Contraseña: ");
        double sueldo = Consola.leerDouble("Sueldo base: ");

        Empleado nuevo;
        if (rol == 1) {
            nuevo = new Administrador(nombre, apellido, dni, usuario, contraseña, sueldo);
        } else {
            nuevo = new Asesor(nombre, apellido, dni, usuario, contraseña, sueldo);
        }

        servicio

    }


}
