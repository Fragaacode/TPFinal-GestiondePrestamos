package Menus;

import Excepciones.DuplicadoException;
import Excepciones.ElementoNoEncontradoException;
import Excepciones.PrestamoInvalidoException;
import Modelos.Asesor;
import Modelos.Cliente;
import Modelos.Cuota;
import Modelos.Prestamo;
import Servicio.ClienteServicio;
import Servicio.CuotaServicio;
import Servicio.PrestamoServicio;

import java.sql.SQLException;
import java.util.List;
import java.util.TreeSet;
import Enum.EstadoPrestamo;

public class MenuAsesor {
    private Asesor asesor;
    private ClienteServicio clienteServicio;
    private PrestamoServicio prestamoServicio;
    private CuotaServicio cuotaServicio;
    private MenuCliente menuCliente;

    public MenuAsesor(Asesor asesor, ClienteServicio clienteServicio, PrestamoServicio prestamoServicio, CuotaServicio cuotaServicio) {
        this.asesor = asesor;
        this.clienteServicio = clienteServicio;
        this.prestamoServicio = prestamoServicio;
        this.cuotaServicio = cuotaServicio;
        this.menuCliente = new MenuCliente(clienteServicio, prestamoServicio);
    }

    public void menu()
    {
        int opcion = 1;
        while (opcion != 0)
        {
            System.out.println("\n------MENU ASESOR (" + asesor.getNombre() + " " + asesor.getApellido() + ")-------");
            System.out.println("1- Gestion de Clientes");
            System.out.println("2- Ver Condiciones (cuotas e intereses)");
            System.out.println("3- Cargar Solicitud de Prestamo");
            System.out.println("4- Modificar Solicitud Pendiente");
            System.out.println("5- Cancelar Solicitud Pendiente");
            System.out.println("6- Lista de Solicitudes Pendientes");
            System.out.println("7- Ver Cuotas Impagas de un Cliente");
            System.out.println("8- Registrar Pago de Cuota");
            System.out.println("0- Cerrar sesion");
            opcion = Consola.leerentero("Opcion: ");

            try {
                switch (opcion)
                {
                    case 1:
                    {
                        menuCliente.menu();
                        break;
                    }
                    case 2:
                    {
                        condiciones();
                        break;
                    }
                    case 3:
                    {
                        solicitar();
                        break;
                    }
                    case 4:
                    {
                        modificar();
                        break;
                    }
                    case 5:
                    {
                        cancelar();
                        break;
                    }
                    case 6:
                    {
                        Consola.mostrarLista(prestamoServicio.listarPorEstado(EstadoPrestamo.PENDIENTE));
                        break;
                    }
                    case 7:
                    {
                        cuotasImpagas();
                        break;
                    }
                    case 8:
                    {
                        pagarCuota();
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
                System.out.println("No se ah encontrado " + e.getMessage());
            }
            catch (PrestamoInvalidoException e)
            {
                System.out.println("Prestamo invalido: " + e.getMessage());
            }
            catch (Exception e)
            {
                System.err.println("ERROR = " + e.getMessage());
            }
        }
    }

    public void condiciones()
    {
        System.out.println("Monto: entre $" + String.format("%.0f", Prestamo.MONTO_MINIMO)
                + " y $" + String.format("%.0f", Prestamo.MONTO_MAXIMO));
        for (int c : new TreeSet<>(Prestamo.cuotasposibles()))
        {
            System.out.println("  " + c + " cuotas -> " + Prestamo.calcularinteres(c) + "% de interes total");
        }
    }

    public void solicitar() throws SQLException, ElementoNoEncontradoException, PrestamoInvalidoException
    {
        Cliente cliente = clienteServicio.buscarDni(Consola.leerTexto("Ingrese el DNI del cliente: "));
        System.out.println("Cliente: " + cliente.getNombre() + " " + cliente.getApellido());
        double monto = Consola.leerDouble("Monto solicitado: ");
        int cantCuotas = Consola.leerentero("Cantidad de cuotas " + new TreeSet<>(Prestamo.cuotasposibles()) + ": ");

        Prestamo prestamo = new Prestamo(cliente, asesor.getId(), monto, cantCuotas);
        prestamoServicio.validarSolicitud(prestamo);
        System.out.println("Total a devolver: $" + String.format("%.2f", prestamo.montototal()) + " en " + cantCuotas + " cuotas de $" + String.format("%.2f", prestamo.valorCuota()) + " (interes " + Prestamo.calcularinteres(cantCuotas) + "%)");

            prestamoServicio.guardarSolicitud(prestamo, asesor);
            System.out.println("Solicitud #" + prestamo.getIdPrestamo() + " cargada. Queda PENDIENTE de aprobacion.");
    }

    public void modificar() throws SQLException, ElementoNoEncontradoException, PrestamoInvalidoException
    {
        Prestamo actual = prestamoServicio.buscarPendiente(Consola.leerentero("Ingrese el id del prestamo: "));
        System.out.println(actual);
        double monto = Consola.leerDouble("Nuevo monto: ");
        int cantCuotas = Consola.leerentero("Nuevas cuotas: ");

        Prestamo cambios = new Prestamo(actual.getIdPrestamo(), actual.getCliente(), actual.getIdAsesor(), monto,
                cantCuotas, actual.getFechaSolicitud(), actual.getEstado(), actual.isActivo());
        boolean modificado = prestamoServicio.modificarSolicitud(cambios);
        if (modificado)
        {
            System.out.println("Solicitud modificada.");
        }
    }

    public void cancelar() throws SQLException, ElementoNoEncontradoException, PrestamoInvalidoException
    {
        int id = Consola.leerentero("Ingrese el id del prestamo a cancelar: ");
        System.out.println(prestamoServicio.buscarPendiente(id));
            boolean cancelado = prestamoServicio.cancelar(id);
            if (cancelado)
            {
                System.out.println("Solicitud cancelada.");
            }
    }

    public void cuotasImpagas() throws ElementoNoEncontradoException
    {
        List<Cuota> impagas = prestamoServicio.cuotasImpagas(Consola.leerTexto("Ingrese el DNI del cliente: "));
        Consola.mostrarLista(impagas);
        double deuda = 0;
        for (Cuota c : impagas)
        {
            deuda += c.getMonto();
        }
        System.out.println("Deuda total: $" + String.format("%.2f", deuda));
    }

    public void pagarCuota()throws SQLException, ElementoNoEncontradoException, PrestamoInvalidoException
    {
        int id = Consola.leerentero("Ingrese el id de la cuota: ");
        Cuota cuota = cuotaServicio.buscarId(id);
        System.out.println(cuota);
        if (cuota.isPagada())
        {
            System.out.println("Esa cuota ya esta pagada.");
            return;
        }

            boolean terminado = prestamoServicio.registrarPagoCuota(id);
            System.out.println("Pago registrado.");
            if (terminado)
            {
                System.out.println("Esta era la ultima cuota, La deuda quedo Pagada");
            }
    }
}
