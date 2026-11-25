package Menus;

import Excepciones.ElementoNoEncontradoException;
import Excepciones.PrestamoInvalidoException;
import Modelos.Administrador;
import Modelos.Cuota;
import Modelos.Empleado;
import Modelos.Prestamo;
import Servicio.ClienteServicio;
import Servicio.CuotaServicio;
import Servicio.EmpleadoServicio;
import Servicio.PrestamoServicio;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import Enum.EstadoPrestamo;

public class MenuAdministrador {
    private Empleado empleado;
    private EmpleadoServicio empleadoServicio;
    private PrestamoServicio prestamoServicio;
    private CuotaServicio cuotaServicio;
    private MenuCliente menuCliente;
    private MenuEmpleado menuEmpleado;

    public MenuAdministrador(Administrador administrador, ClienteServicio clienteServicio, EmpleadoServicio empleadoServicio, PrestamoServicio prestamoServicio, CuotaServicio cuotaServicio) {
        this.empleado = administrador;
        this.empleadoServicio = empleadoServicio;
        this.prestamoServicio = prestamoServicio;
        this.cuotaServicio = cuotaServicio;
        this.menuCliente = new MenuCliente(clienteServicio, prestamoServicio);
        this.menuEmpleado = new MenuEmpleado(empleadoServicio, empleado);
    }

    public void menu()
    {
        int opcion = 1;
        while (opcion != 0)
        {
            System.out.println("\n----------MENU ADMINISTRADOR (" + empleado.getNombre() + " " + empleado.getApellido() + ")-------");
            System.out.println("1- Gestion de Empleados");
            System.out.println("2- Gestion de Clientes");
            System.out.println("3- Lista de Solicitudes Pendientes");
            System.out.println("4- Aprobar Solicitud");
            System.out.println("5- Rechazar Solicitud");
            System.out.println("6- Lista de Prestamos");
            System.out.println("7- Lista de Prestamos por Estado");
            System.out.println("8- Ver Cuotas de un Prestamo");
            System.out.println("9- Anular Cuota");
            System.out.println("10- Lista de Sueldos");
            System.out.println("0- Cerrar sesion");
            opcion = Consola.leerentero("Opcion: ");

            try {
                switch (opcion)
                {
                    case 1:
                    {
                        menuEmpleado.menu();
                        break;
                    }
                    case 2:
                    {
                        menuCliente.menu();
                        break;
                    }
                    case 3:
                    {
                        Consola.mostrarLista(prestamoServicio.listarPorEstado(EstadoPrestamo.PENDIENTE));
                        break;
                    }
                    case 4:
                    {
                        aprobar();
                        break;
                    }
                    case 5:
                    {
                        rechazar();
                        break;
                    }
                    case 6:
                    {
                        Consola.mostrarLista(prestamoServicio.listaactiva());
                        break;
                    }
                    case 7:
                    {
                        listaPorEstado();
                        break;
                    }
                    case 8:
                    {
                        Consola.mostrarLista(cuotaServicio.listarPorPrestamo(Consola.leerentero("Ingrese el id del prestamo: ")));
                        break;
                    }
                    case 9:
                    {
                        anularCuota();
                        break;
                    }
                    case 10:
                    {
                        listaSueldos();
                        break;
                    }
                    case 0:
                    {
                        break;
                    }
                    default:
                    {
                        System.out.println("Opcion invalida");
                    }
                }
            }
            catch (SQLException e)
            {
                System.err.println("Error con la Bdd " + e.getMessage());
            }
            catch (ElementoNoEncontradoException e)
            {
                System.err.println("No se ah encontrado " + e.getMessage());
            }
            catch (PrestamoInvalidoException e)
            {
                System.err.println("Prestamo invalido: " + e.getMessage());
            }
            catch (Exception e)
            {
                System.err.println("ERROR = " + e.getMessage());
            }
        }
    }

    public void aprobar()  throws SQLException, ElementoNoEncontradoException, PrestamoInvalidoException
    {
        int id = Consola.leerentero("Ingrese el id de la solicitud a aprobar: ");
        Prestamo prestamo = prestamoServicio.buscarPendiente(id);
        System.out.println(prestamo);
        System.out.println("Historial del cliente:");
        Consola.mostrarLista(prestamoServicio.historial(prestamo.getCliente().getId()));
        List<Cuota> nuevas = prestamoServicio.aprobar(id);
            System.out.println("Prestamo aprobado. Se generaron " + nuevas.size() + " cuotas:");
            for (Cuota c : nuevas)
            {
                System.out.println("  N° " + c.getNumero() + "  $" + String.format("%.2f", c.getMonto()) + "  vence " + c.getFechaVencimiento());
            }

    }

    public void rechazar() throws SQLException, ElementoNoEncontradoException, PrestamoInvalidoException
    {
        int id = Consola.leerentero("Ingrese el id de la solicitud a rechazar: ");
        System.out.println(prestamoServicio.buscarPendiente(id));
        boolean rechazado = prestamoServicio.rechazar(id);
        if (rechazado)
        {
            System.out.println("Solicitud rechazada.");
        }

    }
    public void anularCuota() throws SQLException, ElementoNoEncontradoException
    {
        int id = Consola.leerentero("Ingrese el id de la cuota: ");
        System.out.println(cuotaServicio.buscarId(id));
        boolean anulada = cuotaServicio.baja(id);
        if (anulada)
        {
            System.out.println("Cuota anulada.");
        }

    }

    public void listaPorEstado()
    {
        Map<EstadoPrestamo, List<Prestamo>> porEstado = prestamoServicio.agruparPorEstado();
        for (EstadoPrestamo estado : EstadoPrestamo.values())
        {
            List<Prestamo> lista = porEstado.getOrDefault(estado, List.of());
            double total = 0;
            for (Prestamo p : lista)
            {
                total += p.getMontoSolicitado();
            }
            System.out.println(estado + ": " + lista.size() + " prestamos | capital $" + String.format("%.2f", total));
        }
    }

    public void listaSueldos()
    {
        double total = 0;
        for (Empleado e : empleadoServicio.listaactiva())
        {
            double sueldo = e.calcularSueldo();
            total += sueldo;
            System.out.println(e.getRol() + " - " + e.getNombre() + " " + e.getApellido()
                    + " | base $" + String.format("%.2f", e.getSueldoBase())
                    + " -> final $" + String.format("%.2f", sueldo));
        }
        System.out.println("TOTAL A PAGAR: $" + String.format("%.2f", total));
    }

}
