package Modelos;
import Enum.Rol;
public class Administrador extends Empleado {
    private static final double encargado=0.30;

    public Administrador() {

    }

    public Administrador(int id, String nombre, String apellido, String dni, boolean activo, String usuario, String contraseña, double sueldoBase) {
        super(id, nombre, apellido, dni, activo, usuario, contraseña, sueldoBase);

    }

    public Administrador(String nombre, String apellido, String dni, String usuario, String contraseña, double sueldoBase) {
        super(nombre, apellido, dni, usuario, contraseña, sueldoBase);

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
        return  super.toString() +
                "encargado=" + encargado +
                '}';
    }
}
