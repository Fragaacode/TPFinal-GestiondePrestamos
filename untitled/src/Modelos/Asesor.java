package Modelos;
import Enum.Rol;

public class Asesor extends Empleado{
    private static final double comision=100000;
private int cantidadSolicitudes;
    public Asesor() {
    }

    public Asesor(int cantidadSolicitudes) {
        this.cantidadSolicitudes = cantidadSolicitudes;
    }

    public Asesor(int id, String nombre, String apellido, String dni, boolean activo, String usuario, String contraseña, double sueldoBase,int cantidadSolicitudes) {
        super(id, nombre, apellido, dni, activo, usuario, contraseña, sueldoBase);
        this.cantidadSolicitudes = cantidadSolicitudes;
    }

    public Asesor(String nombre, String apellido, String dni, String usuario, String contraseña, double sueldoBase int cantidadSolicitudes) {
        super(nombre, apellido, dni, usuario, contraseña, sueldoBase);
        this.cantidadSolicitudes = 0;
    }

    public double getComision() {
        return comision;
    }


    public int getCantidadSolicitudes() {
        return cantidadSolicitudes;
    }

    public void setCantidadSolicitudes(int cantidadSolicitudes) {
        this.cantidadSolicitudes = cantidadSolicitudes;
    }

    public void registrarSolicitud() {
        cantidadSolicitudes++;
    }

public Rol getRol()
{
    return Rol.ASESOR;
}

    @Override
    public double calcularSueldo() {
        return getSueldoBase()+comision*cantidadSolicitudes;
    }

    @Override
    public String toString() {
        return super.toString()+" Comision=" + comision +
                ", cantidadSolicitudes=" + cantidadSolicitudes +
                '}';
    }
}
