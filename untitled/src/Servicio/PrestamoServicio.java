package Servicio;


import Excepciones.ElementoNoEncontradoException;
import Excepciones.PrestamoInvalidoException;
import Modelos.Asesor;
import Modelos.Cliente;
import Modelos.Cuota;
import Modelos.Prestamo;
import Persistencia.PrestamoDAO;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

import Enum.EstadoPrestamo;

public class PrestamoServicio {
    public static final int maxprestamos = 2;
    private PrestamoDAO dao;
    private ClienteServicio clienteServicio;
    private EmpleadoServicio empleadoServicio;
    private CuotaServicio cuotaServicio;
    private ArrayList<Prestamo> prestamos;

    public PrestamoServicio(ClienteServicio clienteServicio, EmpleadoServicio empleadoServicio, CuotaServicio cuotaServicio)throws SQLException,ElementoNoEncontradoException {
        this.dao = new PrestamoDAO();
        this.clienteServicio = clienteServicio;
        this.empleadoServicio = empleadoServicio;
        this.cuotaServicio = cuotaServicio;
        this.prestamos = new ArrayList<>();
        cargarprestamos();
    }

    public void cargarprestamos() throws SQLException,ElementoNoEncontradoException {
        List<Prestamo> lista = dao.listar();
        for (Prestamo p : lista) {

                p.setCliente(clienteServicio.buscarId(p.getCliente().getId()));

            }

        prestamos.clear();
        prestamos.addAll(lista);
    }

    public void validarSolicitud(Prestamo p)throws PrestamoInvalidoException {
        if (!p.getCliente().isActivo()) {
            throw new PrestamoInvalidoException("El cliente está dado de baja.");
        }
        if (p.getMontoSolicitado() < Prestamo.MONTO_MINIMO || p.getMontoSolicitado() > Prestamo.MONTO_MAXIMO) {
            throw new PrestamoInvalidoException(String.format("El monto debe estar entre $%.0f y $%.0f.",Prestamo.MONTO_MINIMO, Prestamo.MONTO_MAXIMO));
        }
        if (!Prestamo.cuotasposibles().contains(p.getCantidadCuotas())) {
            throw new PrestamoInvalidoException("Cantidad de cuotas no permitida. Opciones: " +new TreeSet<>(Prestamo.cuotasposibles()));

        }

        int enCurso = 0;
        for (Prestamo otro : prestamos) {
            if (otro.getCliente().getId() == p.getCliente().getId() && otro.isActivo() && (otro.getEstado() == EstadoPrestamo.PENDIENTE || otro.getEstado() == EstadoPrestamo.APROBADO)) {
                enCurso++;
            }
        }
        if (enCurso >= maxprestamos) {
            throw new PrestamoInvalidoException("El cliente ya tiene " + enCurso + " prestamos en curso (El maximo es: " + maxprestamos + ")");
        }


    }

    public void guardarSolicitud(Prestamo p, Asesor asesor) throws SQLException {
        if (dao.insertar(p) > 0) {
            prestamos.add(0, p);
            empleadoServicio.registrarSolicitud(asesor);
        }
    }


    public boolean modificarSolicitud(Prestamo cambios) throws SQLException,PrestamoInvalidoException,ElementoNoEncontradoException{
        Prestamo existente = buscarPendiente(cambios.getIdPrestamo());
        if (cambios.getMontoSolicitado() < Prestamo.MONTO_MINIMO || cambios.getMontoSolicitado() > Prestamo.MONTO_MAXIMO) {
            throw new PrestamoInvalidoException(String.format("El monto debe estar entre $%.0f y $%.0f.",Prestamo.MONTO_MINIMO, Prestamo.MONTO_MAXIMO));
        }
        if (!Prestamo.cuotasposibles().contains(cambios.getCantidadCuotas())) {
            throw new PrestamoInvalidoException("Cantidad de cuotas no permitida. Opciones: "+ new TreeSet<>(Prestamo.cuotasposibles()));
        }

        boolean modificado = dao.modificar(cambios);
        if (modificado) {
            existente.setMontoSolicitado(cambios.getMontoSolicitado());
            existente.setCantidadCuotas(cambios.getCantidadCuotas());
        }
        return modificado;
    }

    public List<Cuota> aprobar(int idPrestamo) throws SQLException, PrestamoInvalidoException, ElementoNoEncontradoException {
        Prestamo p = buscarPendiente(idPrestamo);
        Prestamo cambios = new Prestamo(p.getIdPrestamo(), p.getCliente(), p.getIdAsesor(), p.getMontoSolicitado(),
                p.getCantidadCuotas(), p.getFechaSolicitud(), EstadoPrestamo.APROBADO, p.isActivo());
        if (!dao.modificar(cambios)) {
            throw new SQLException("No se pudo aprobar el prestamo " + idPrestamo + " en la base de datos.");
        }
        p.setEstado(EstadoPrestamo.APROBADO);
        List<Cuota> nuevas = p.generarCuotas(LocalDate.now());
        cuotaServicio.ingresarCuotas(nuevas);
        return nuevas;
    }

    public boolean rechazar(int idPrestamo) throws SQLException,PrestamoInvalidoException,ElementoNoEncontradoException{
        Prestamo p = buscarPendiente(idPrestamo);
        Prestamo cambios = new Prestamo(p.getIdPrestamo(), p.getCliente(), p.getIdAsesor(), p.getMontoSolicitado(),
                p.getCantidadCuotas(), p.getFechaSolicitud(), EstadoPrestamo.RECHAZADO, p.isActivo());
        boolean rechazado = dao.modificar(cambios);
        if (rechazado) {
            p.setEstado(EstadoPrestamo.RECHAZADO);
        }
        return rechazado;
    }

    public boolean cancelar(int idPrestamo) throws SQLException,PrestamoInvalidoException,ElementoNoEncontradoException{
        Prestamo p = buscarPendiente(idPrestamo);
        Prestamo cambios = new Prestamo(p.getIdPrestamo(), p.getCliente(), p.getIdAsesor(), p.getMontoSolicitado(),
                p.getCantidadCuotas(), p.getFechaSolicitud(), EstadoPrestamo.CANCELADO, p.isActivo());
        boolean cancelado = dao.modificar(cambios) && dao.DardeBaja(p);
        if (cancelado) {
            p.setEstado(EstadoPrestamo.CANCELADO);
            p.setActivo(false);
        }
        return cancelado;
    }

    public boolean registrarPagoCuota(int idCuota)throws SQLException,PrestamoInvalidoException,ElementoNoEncontradoException {
        Cuota cuota = cuotaServicio.registrarPago(idCuota);
        for (Cuota c : cuotaServicio.listarPorPrestamo(cuota.getIdPrestamo())) {
            if (!c.isPagada()) {
                return false;
            }
        }
        Prestamo p = buscarId(cuota.getIdPrestamo());
        Prestamo cambios = new Prestamo(p.getIdPrestamo(), p.getCliente(), p.getIdAsesor(), p.getMontoSolicitado(),
                p.getCantidadCuotas(), p.getFechaSolicitud(), EstadoPrestamo.PAGADO, p.isActivo());
        if (dao.modificar(cambios)) {
            p.setEstado(EstadoPrestamo.PAGADO);
        }
        return true;
    }

    public Prestamo buscarId(int id)throws ElementoNoEncontradoException {
        for (Prestamo p : prestamos) {
            if (p.getIdPrestamo() == id && p.isActivo()) {
                return p;
            }
        }
        throw new ElementoNoEncontradoException("No existe un préstamo activo con id " + id);
    }

    public Prestamo buscarPendiente(int id) throws PrestamoInvalidoException, ElementoNoEncontradoException{
        Prestamo prestamo = buscarId(id);
        if (prestamo.getEstado() != EstadoPrestamo.PENDIENTE) {
            throw new PrestamoInvalidoException("El préstamo Nº" + id + " esta " + prestamo.getEstado() + ", solo se puede operar si esta en PENDIENTE.");
        }
        return prestamo;
    }

    public List<Prestamo> listaactiva() {
        List<Prestamo> activos = new ArrayList<>();
        for (Prestamo p : prestamos) {
            if (p.isActivo()) {
                activos.add(p);
            }
        }
        return activos;
    }

    public List<Prestamo> listarPorEstado(EstadoPrestamo estado) {
        List<Prestamo> resultado = new ArrayList<>();
        for (Prestamo p : prestamos) {
            if (p.isActivo() && p.getEstado() == estado) {
                resultado.add(p);
            }
        }
        return resultado;
    }

    public List<Prestamo> historial(int idCliente) {
        List<Prestamo> resultado = new ArrayList<>();
        for (Prestamo p : prestamos) {
            if (p.getCliente().getId() == idCliente) {
                resultado.add(p);
            }
        }
        return resultado;
    }

    public List<Cuota> cuotasImpagas(String dni)throws ElementoNoEncontradoException {
        Cliente cliente = clienteServicio.buscarDni(dni);
        List<Cuota> impagas = new ArrayList<>();
        for (Prestamo p : historial(cliente.getId())) {
            if (p.isActivo()) {
                for (Cuota c : cuotaServicio.listarPorPrestamo(p.getIdPrestamo())) {
                    if (!c.isPagada()) {
                        impagas.add(c);
                    }
                }
            }
        }
        return impagas;
    }

    public Map<EstadoPrestamo, List<Prestamo>> agruparPorEstado() {
        Map<EstadoPrestamo, List<Prestamo>> porEstado = new HashMap<>();
        for (Prestamo p : listaactiva()) {
            if (!porEstado.containsKey(p.getEstado())) {
                porEstado.put(p.getEstado(), new ArrayList<>());
            }
            porEstado.get(p.getEstado()).add(p);
        }
        return porEstado;
    }


}
