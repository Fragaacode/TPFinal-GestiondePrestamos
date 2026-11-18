package Modelos;
import Enum.Rol;
public class Administrador extends Empleado {
    private double encargado=0.30;

    public Administrador(double encargado) {
        this.encargado = encargado;
    }

    public Administrador(int id, String nombre, String apellido, String dni, boolean activo, String usuario, String contraseña, double sueldoBase, double encargado) {
        super(id, nombre, apellido, dni, activo, usuario, contraseña, sueldoBase);
        this.encargado = encargado;
    }

    public Administrador(String nombre, String apellido, String dni, String usuario, String contraseña, double sueldoBase, double encargado) {
        super(nombre, apellido, dni, usuario, contraseña, sueldoBase);
        this.encargado = encargado;
    }


    @Override
    public Rol getRol() {
        return Rol.ADMINISTRADOR;
    }

    @Override
    public double calcularSueldo() {
        return getSueldoBase()+(getSueldoBase()*encargado);
    }

    @Override
    public String toString() {
        return "Administrador{" +
                "encargado=" + encargado +
                '}';
    }
}
