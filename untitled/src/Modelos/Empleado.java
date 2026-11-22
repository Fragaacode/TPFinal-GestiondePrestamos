package Modelos;
import Enum.Rol;

import java.util.Objects;

public abstract class Empleado extends Persona {
    private String usuario;
    private String contraseña;
    private double sueldoBase;

    public Empleado() {
    }

    public Empleado(int id, String nombre, String apellido, String dni, boolean activo, String usuario, String contraseña, double sueldoBase) {
        super(id, nombre, apellido, dni, activo);
        this.usuario = usuario;
        this.contraseña = contraseña;
        this.sueldoBase = sueldoBase;
    }

    public Empleado(String nombre, String apellido,String dni, String usuario, String contraseña, double sueldoBase) {
        super(nombre, apellido,dni);
        this.usuario = usuario;
        this.contraseña = contraseña;
        this.sueldoBase = sueldoBase;
    }

    public abstract Rol getRol();

    @Override
    public String getTipo() {
        return getRol().name();
    }

    public abstract double calcularSueldo();

    public boolean validarContrasena(String validar) {
        return contraseña != null && contraseña.equals(validar);
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public double getSueldoBase() {
        return sueldoBase;
    }

    public void setSueldoBase(double sueldoBase) {
        this.sueldoBase = sueldoBase;
    }

    @Override
    public String toString() {
        return super.toString()+
                "usuario='" + usuario + '\'' +
                ", contraseña='" + contraseña + '\'' +
                ", sueldoBase=" + sueldoBase +
                '}';
    }

}