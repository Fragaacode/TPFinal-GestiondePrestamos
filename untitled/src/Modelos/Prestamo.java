package Modelos;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import Enum.EstadoPrestamo;

public class Prestamo {
    public static final double MONTO_MINIMO = 100000;
    public static final double MONTO_MAXIMO =15000000 ;


    private static final Map<Integer, Double> CuotaseIntereses = Map.of(
            3, 15.0,
            6, 30.0,
            12, 60.0,
            18, 90.0,
            24, 120.0
    );
    private int idPrestamo;
    private Cliente cliente;
    private int idAsesor;
    private double montoSolicitado;
    private int cantidadCuotas;
    private LocalDate fechaSolicitud;
    private EstadoPrestamo estado;
    private boolean activo = true;

    public Prestamo() {
    }

    public Prestamo(int idPrestamo, Cliente cliente, int idAsesor, double montoSolicitado, int cantidadCuotas, LocalDate fechaSolicitud, EstadoPrestamo estado, boolean activo) {
        this.idPrestamo = idPrestamo;
        this.cliente = cliente;
        this.idAsesor = idAsesor;
        this.montoSolicitado = montoSolicitado;
        this.cantidadCuotas = cantidadCuotas;
        this.fechaSolicitud = fechaSolicitud;
        this.estado = estado;
        this.activo = activo;
    }

    public Prestamo(Cliente cliente, int idAsesor, double montoSolicitado, int cantidadCuotas) {
        this.cliente = cliente;
        this.idAsesor = idAsesor;
        this.montoSolicitado = montoSolicitado;
        this.cantidadCuotas = cantidadCuotas;
        this.fechaSolicitud = LocalDate.now();
        this.estado = EstadoPrestamo.PENDIENTE;
    }

    public static Set<Integer> cuotasposibles()
    {
        return CuotaseIntereses.keySet();
    }

public static double calcularinteres(int cuotas)
{
return CuotaseIntereses.getOrDefault(cuotas,0.0);
}

public double montototal()
{
    return montoSolicitado+(montoSolicitado*(calcularinteres(cantidadCuotas)/100));
}
public double valorCuota()
{
    return Math.round(montototal() / cantidadCuotas * 100) / 100.0;
}

    public List<Cuota> generarCuotas(LocalDate fechaAprobacion) {
        List<Cuota> cuotas = new ArrayList<>();
        for (int i = 1; i <= cantidadCuotas; i++) {
            cuotas.add(new Cuota(idPrestamo, i, valorCuota(), fechaAprobacion.plusMonths(i)));
        }
        return cuotas;
    }

    public int getIdPrestamo() {
        return idPrestamo;
    }

    public void setIdPrestamo(int idPrestamo) {
        this.idPrestamo = idPrestamo;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public int getIdAsesor() {
        return idAsesor;
    }

    public void setIdAsesor(int idAsesor) {
        this.idAsesor = idAsesor;
    }

    public double getMontoSolicitado() {
        return montoSolicitado;
    }

    public void setMontoSolicitado(double montoSolicitado) {
        this.montoSolicitado = montoSolicitado;
    }

    public int getCantidadCuotas() {
        return cantidadCuotas;
    }

    public void setCantidadCuotas(int cantidadCuotas) {
        this.cantidadCuotas = cantidadCuotas;
    }

    public LocalDate getFechaSolicitud() {
        return fechaSolicitud;
    }

    public void setFechaSolicitud(LocalDate fechaSolicitud) {
        this.fechaSolicitud = fechaSolicitud;
    }

    public EstadoPrestamo getEstado() {
        return estado;
    }

    public void setEstado(EstadoPrestamo estado) {
        this.estado = estado;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    @Override
    public String toString() {
        return String.format("Prestamo #%-3d | %-10s %-10s (DNI %s) | $%,13.2f en %2d cuotas | Total $%,13.2f | %-9s | %s",
                idPrestamo, cliente.getNombre(), cliente.getApellido(), cliente.getDni(),
                montoSolicitado, cantidadCuotas, montototal(), estado, fechaSolicitud);
    }
}
