package Menus;

import Modelos.Administrador;
import Modelos.Asesor;
import Modelos.Empleado;
import Servicio.EmpleadoServicio;

import java.sql.SQLException;
import java.util.List;

public class MenuEmpleado {
    private EmpleadoServicio servicio;
    private Empleado empleado;

    public MenuEmpleado(EmpleadoServicio servicio, Empleado empleado) {
        this.servicio = servicio;
        this.empleado = empleado;
    }

    public void menu() {
        int opcion;
        do {
            System.out.println("\n------------GESTIÓN DE EMPLEADOS ------------");
            System.out.println("1. Alta de empleado");
            System.out.println("2. Modificar empleado");
            System.out.println("3. Baja de empleado");
            System.out.println("4. Consultar empleado por id");
            System.out.println("5. Listar empleados");
            System.out.println("6. Listar empleados Inactivos");
            System.out.println("7. Reactivar empleado");
            System.out.println("8. Eliminar empleado definitivamente");
            System.out.println("9- Buscar empleado por el id");
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
                        DardeBaja();
                        break;
                    case 4:
                        reactivar();
                        break;
                    case 5:
                        listar();
                        break;
                    case 6:
                        listarActivos();
                        break;
                    case 7:
                        listarInactivos();
                        break;
                    case 8:
                        eliminar();
                        break;
                    case 9:
                        System.out.println(servicio.buscarPorId(Consola.leerentero("Ingrese el id: ")));
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

    public void ingresar() throws Exception
    {
        int rol = Consola.leerentero("Ingrese el Rol (1-Rol Administrador  2-Rol Asesor): ");
        if (rol != 1 && rol != 2)
        {
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
        if (rol == 1)
        {
            nuevo = new Administrador(nombre, apellido, dni, usuario, contraseña, sueldo);
        }
        else
        {
            nuevo = new Asesor(nombre, apellido, dni, usuario, contraseña, sueldo);
        }
        nuevo = servicio.alta(nuevo);
        System.out.println("Empleado creado con id " + nuevo.getId());
    }

    public void modificar() throws Exception
    {
        Empleado actual = servicio.buscarPorId(Consola.leerentero("Ingrese el id del empleado que desea modificar: "));
        System.out.println(actual);
        System.out.println("Ponga Enter si no quiere cambiar el campo:");
        String nombre = Consola.cambiarTexto("Nombre", actual.getNombre());
        String apellido = Consola.cambiarTexto("Apellido", actual.getApellido());
        String contraseña = Consola.leerTexto("Nueva contraseña (Enter para no cambiarla): ");
        if (contraseña.isEmpty())
        {
            contraseña = actual.getContraseña();
        }
        double sueldo = Double.parseDouble(Consola.cambiarTexto("Sueldo base", String.valueOf(actual.getSueldoBase())));

        Empleado cambios;
        if (actual instanceof Asesor asesor)
        {
            cambios = new Asesor(actual.getId(), nombre, apellido, actual.getDni(), actual.isActivo(),
                    actual.getUsuario(), contraseña, sueldo, asesor.getCantidadSolicitudes());
        }
        else
        {
            cambios = new Administrador(actual.getId(), nombre, apellido, actual.getDni(), actual.isActivo(),
                    actual.getUsuario(), contraseña, sueldo);
        }
        boolean modificado = servicio.modificar(cambios);
        if (modificado)
        {
            System.out.println("Empleado modificado.");
        }
    }

    public void DardeBaja() throws Exception
    {
        listarActivos();
        int id = Consola.leerentero("Ingrese el id del empleado que desee dar de baja: ");
            boolean bajo = servicio.baja(id, empleado.getId());
            if (bajo)
            {
                System.out.println("Empleado dado de baja");
            }

    }
    public void reactivar() throws Exception
    {
        listarInactivos();
        boolean reactivo = servicio.reactivar(Consola.leerentero("Ingrese el id del empleado que desee reactivar: "));
        if (reactivo)
        {
            System.out.println("Empleado reactivado");
        }
    }

    public void listar()
    {
        List<Empleado> todos = servicio.listacompleta();
        Consola.mostrarLista(todos);
    }

    public void listarActivos()
    {
        List<Empleado> activos = servicio.listaactiva();
        Consola.mostrarLista(activos);
    }

    public void listarInactivos()
    {
        List<Empleado> inactivos = servicio.listainactiva();
        Consola.mostrarLista(inactivos);
    }

    public void eliminar() throws Exception
    {
        listar();
        int id = Consola.leerentero("Ingrese el id del empleado que desea eliminar definitivamente: ");
            boolean eliminado = servicio.eliminar(id, empleado.getId());
            if (eliminado)
            {
                System.out.println("Empleado eliminado de la BDD");
            }

    }


}
