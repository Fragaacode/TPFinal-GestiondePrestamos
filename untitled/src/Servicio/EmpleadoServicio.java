package Servicio;

import Modelos.Empleado;
import Persistencia.EmpleadoDAO;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EmpleadoServicio {
private EmpleadoDAO dao;
private ArrayList<Empleado> empleados;
private HashMap<String, Empleado> UsuarioEmpleado;

    public EmpleadoServicio() throws SQLException {
        dao = new EmpleadoDAO();
        empleados = new ArrayList<>();
        UsuarioEmpleado = new HashMap<>();
        cargarEmpleados();
    }

    public void cargarEmpleados() throws SQLException {
        List<Empleado> lista = dao.listar();
        empleados.clear();
        empleados.addAll(lista);
        UsuarioEmpleado.clear();
        for (Empleado e : lista) {
            UsuarioEmpleado.put(e.getUsuario().toLowerCase(), e);
        }
    }

    public Empleado login(String usuario,String contraseña)
    {
        Empleado empleado= UsuarioEmpleado.get(usuario.trim().toLowerCase());
        if (empleado == null || !empleado.isActivo() || !empleado.validarContrasena(contraseña)) {
///AGREGAR EXCEPCION PEROSNALIXADA----------------------------------------------------------------------
        }
        return empleado;
    }

    public void alta(Empleado empleado) throws SQLException {
        Validaciones.Vacio(empleado.getNombre(), "nombre");
        Validaciones.Vacio(empleado.getApellido(), "apellido");
        Validaciones.Vacio(empleado.getUsuario(), "usuario");
        Validaciones.Vacio(empleado.getContraseña(), "contraseña");
        Validaciones.montos(empleado.getSueldoBase(), "sueldo");

        for (Empleado e : empleados) {
            if (e.getDni().equals(empleado.getDni())) {
                ///AGREGAR EXCEPCION PEROSNALIXADA----------------------------------------------------------------------
            }
        }
        if (UsuarioEmpleado.containsKey(empleado.getUsuario().toLowerCase())) {
            ///AGREGAR EXCEPCION PEROSNALIXADA------------------------------------------------------------
        }

        if (dao.insertar(empleado) > 0) {
            empleados.add(empleado);
            UsuarioEmpleado.put(empleado.getUsuario().toLowerCase(), empleado);
        }
    }

    public boolean modificar(Empleado empleado)throws SQLException
    {
        Empleado viejo = buscarPorId(empleado.getId());
        Validaciones.Vacio(empleado.getNombre(), "Nombre");
        Validaciones.Vacio(empleado.getApellido(), "Apellido");
        Validaciones.Vacio(empleado.getContraseña(), "Contraseña");
        Validaciones.montos(empleado.getSueldoBase(), "Sueldo");

        boolean modificado = dao.modificar(empleado);
        if (modificado) {
            viejo.setNombre(empleado.getNombre());
            viejo.setApellido(empleado.getApellido());
            viejo.setContraseña(empleado.getContraseña());
            viejo.setSueldoBase(empleado.getSueldoBase());
        }
        return modificado;

    }

    public Empleado buscarPorId(int id)  {
        for (Empleado e : empleados) {
            if (e.getId() == id) {
                return e;
            }
        }
        ///AGREGAR EXCEPCION PEROSNALIXADA------------------------------------------------------------

    }

    public boolean baja(int id, int idLogueado) throws SQLException {
        if (id == idLogueado) {
            throw new IllegalArgumentException("No podés darte de baja a vos mismo.");
        }
        Empleado bajar = buscarPorId(id);
        if (!bajar.isActivo()) {
            ///AGREGAR EXCEPCION PEROSNALIXADA------------------------------------------------------------
        }
        boolean bajo = dao.DardeBaja(bajar);
        if (bajo) {
            bajar.setActivo(false);
        }
        return bajo;
    }
    public boolean reactivar(int id) throws SQLException{
        Empleado activar = buscarPorId(id);
        if (activar.isActivo()) {
            ///AGREGAR EXCEPCION PEROSNALIXADA------------------------------------------------------------
        }
        boolean reactivado = dao.reactivar(activar);
        if (reactivado) {
            activar.setActivo(true);
        }
        return reactivado;
    }

    public boolean eliminar(int id, int idLogueado) throws SQLException {
        if (id == idLogueado) {
            ///AGREGAR EXCEPCION PEROSNALIXADA------------------------------------------------------------
        }
        Empleado sacar = buscarPorId(id);
        try {
            boolean eliminado = dao.eliminar(sacar);
            if (eliminado) {
                empleados.remove(sacar);
                UsuarioEmpleado.remove(sacar.getUsuario().toLowerCase());
            }
            return eliminado;
        } catch (SQLIntegrityConstraintViolationException e) {
            ///AGREGAR EXCEPCION PEROSNALIXADA------------------------------------------------------------ el asesor tiene préstamos cargados. Usá la baja lógica.
        }
    }

    public List<Empleado> listaactiva() {
        List<Empleado> activos = new ArrayList<>();
        for (Empleado e : empleados) {
            if (e.isActivo()) {
                activos.add(e);
            }
        }
        return activos;
    }

    public List<Empleado> listainactiva() {
        List<Empleado> inactivos = new ArrayList<>();
        for (Empleado e : empleados) {
            if (!e.isActivo()) {
                inactivos.add(e);
            }
        }
        return inactivos;
    }

    public List<Empleado> listacompleta() {
        List<Empleado> todos = new ArrayList<>();
        todos.addAll(empleados);
        return todos;
    }
}
