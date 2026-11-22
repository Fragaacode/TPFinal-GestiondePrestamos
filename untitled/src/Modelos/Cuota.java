package Modelos;

import java.time.LocalDate;

public class Cuota {
    private int idCuota;
    private int idPrestamo;
    private int numero;
    private double monto;
    private LocalDate fechaVencimiento;
    private boolean pagada=false;
    private LocalDate fechaPago=null;
    private boolean activo = true;

    public Cuota() {
    }
    public Cuota(int idCuota, int idPrestamo, int numero, double monto, LocalDate fechaVencimiento,
                 boolean pagada, LocalDate fechaPago, boolean activo) {
        this.idCuota = idCuota;
        this.idPrestamo = idPrestamo;
        this.numero = numero;
        this.monto = monto;
        this.fechaVencimiento = fechaVencimiento;
        this.pagada = pagada;
        this.fechaPago = fechaPago;
        this.activo = activo;
    }

    public Cuota(int idPrestamo, int numero, double monto, LocalDate fechaVencimiento) {
        this.idPrestamo = idPrestamo;
        this.numero = numero;
        this.monto = monto;
        this.fechaVencimiento = fechaVencimiento;
    }

    public void registrarPago(LocalDate fecha) {
        this.pagada = true;
        this.fechaPago = fecha;
    }

    public boolean estaVencida() {
        return !pagada && LocalDate.now().isAfter(fechaVencimiento);
    }

    public int getIdCuota() {
        return idCuota;
    }

    public void setIdCuota(int idCuota) {
        this.idCuota = idCuota;
    }

    public int getIdPrestamo() {
        return idPrestamo;
    }

    public void setIdPrestamo(int idPrestamo) {
        this.idPrestamo = idPrestamo;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public boolean isPagada() {
        return pagada;
    }

    public void setPagada(boolean pagada) {
        this.pagada = pagada;
    }

    public LocalDate getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(LocalDate fechaPago) {
        this.fechaPago = fechaPago;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    @Override
    public String toString() {
        return "Cuota{" +
                "idCuota=" + idCuota +
                ", idPrestamo=" + idPrestamo +
                ", numero=" + numero +
                ", monto=" + monto +
                ", fechaVencimiento=" + fechaVencimiento +
                ", pagada=" + pagada +
                ", fechaPago=" + fechaPago +
                ", activo=" + activo +
                '}';
    }
}