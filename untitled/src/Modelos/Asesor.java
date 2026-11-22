package Modelos;
import Enum.Rol;

public class Asesor extends Empleado{
private double comision=100000;
private int cantidadSolicitudes;
    public Asesor() {
    }

    public Asesor(double comision, int cantidadSolicitudes) {
        this.comision = comision;
        this.cantidadSolicitudes = cantidadSolicitudes;
    }

    public Asesor(int id, String nombre, String apellido, String dni, boolean activo, String usuario, String contraseña, double sueldoBase, double comision, int cantidadSolicitudes) {
        super(id, nombre, apellido, dni, activo, usuario, contraseña, sueldoBase);
        this.comision = comision;
        this.cantidadSolicitudes = cantidadSolicitudes;
    }

    public Asesor(String nombre, String apellido, String dni, String usuario, String contraseña, double sueldoBase, double comision, int cantidadSolicitudes) {
        super(nombre, apellido, dni, usuario, contraseña, sueldoBase);
        this.comision = comision;
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
