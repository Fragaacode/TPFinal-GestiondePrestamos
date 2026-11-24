package Menus;

import Modelos.Empleado;
import Servicio.EmpleadoServicio;

public class MenuEmpleado {
    private EmpleadoServicio servicio;
    private Empleado empleado;

    public MenuEmpleado(EmpleadoServicio servicio, Empleado empleado) {
        this.servicio = servicio;
        this.empleado = empleado;
    }


}
