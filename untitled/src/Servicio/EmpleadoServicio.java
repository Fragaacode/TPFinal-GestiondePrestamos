package Servicio;

import Excepciones.DatosInvalidosException;
import Excepciones.DuplicadoException;
import Excepciones.ElementoNoEncontradoException;
import Modelos.Asesor;
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

    public Empleado login(String usuario,String contraseña)throws DatosInvalidosException
    {
        Empleado empleado= UsuarioEmpleado.get(usuario.trim().toLowerCase());
        if (empleado == null || !empleado.isActivo() || !empleado.validarContrasena(contraseña)) {
            throw new DatosInvalidosException("Usuario o contraseña incorrectos.");
        }
        return empleado;
    }

    public Empleado alta(Empleado empleado) throws SQLException, DuplicadoException {
        Validaciones.Vacio(empleado.getNombre(), "nombre");
        Validaciones.Vacio(empleado.getApellido(), "apellido");
        Validaciones.Vacio(empleado.getUsuario(), "usuario");
        Validaciones.Vacio(empleado.getContraseña(), "contraseña");
        Validaciones.montos(empleado.getSueldoBase(), "sueldo");

        for (Empleado e : empleados) {
            if (e.getDni().equals(empleado.getDni())) {
                throw new DuplicadoException("Ya existe un empleado con DNI " + e.getDni());
            }
        }
        if (UsuarioEmpleado.containsKey(empleado.getUsuario().toLowerCase())) {
            throw new DuplicadoException("El usuario '" + empleado.getUsuario() + "' ya está en uso.");
        }
        if (dao.insertar(empleado) > 0) {
            empleados.add(empleado);
            UsuarioEmpleado.put(empleado.getUsuario().toLowerCase(), empleado);
        }
        return empleado;
    }

    public boolean modificar(Empleado empleado)throws SQLException,ElementoNoEncontradoException
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

    public Empleado buscarPorId(int id)throws ElementoNoEncontradoException  {
        for (Empleado e : empleados) {
            if (e.getId() == id) {
                return e;
            }
        }
        throw new ElementoNoEncontradoException("No existe un empleado con id " + id);

    }

    public boolean baja(int id, int idLogueado) throws SQLException,ElementoNoEncontradoException {
        if (id == idLogueado) {
            throw new IllegalArgumentException("No podés darte de baja a vos mismo.");
        }
        Empleado bajar = buscarPorId(id);
        if (!bajar.isActivo()) {
            throw new IllegalStateException("El empleado ya está dado de baja.");
        }
        boolean bajo = dao.DardeBaja(bajar);
        if (bajo) {
            bajar.setActivo(false);
        }
        return bajo;
    }
    public boolean reactivar(int id) throws SQLException,ElementoNoEncontradoException{
        Empleado activar = buscarPorId(id);
        if (activar.isActivo()) {
            throw new IllegalStateException("El empleado no está dado de baja.");
        }
        boolean reactivado = dao.reactivar(activar);
        if (reactivado) {
            activar.setActivo(true);
        }
        return reactivado;
    }

    public boolean eliminar(int id, int idLogueado) throws SQLException,ElementoNoEncontradoException {
        if (id == idLogueado) {
            throw new IllegalArgumentException("No podés eliminarte a vos mismo.");
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
            throw new IllegalStateException("No se puede eliminar: el asesor tiene préstamos cargados. Usar la baja lógica.");
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

    public void registrarSolicitud(Asesor asesor) throws SQLException {
        Asesor cambios = new Asesor(asesor.getId(), asesor.getNombre(), asesor.getApellido(), asesor.getDni(),
                asesor.isActivo(), asesor.getUsuario(), asesor.getContraseña(), asesor.getSueldoBase(),
                asesor.getCantidadSolicitudes() + 1);
        if (dao.modificar(cambios)) {
            asesor.registrarSolicitud();
        }
    }
}
