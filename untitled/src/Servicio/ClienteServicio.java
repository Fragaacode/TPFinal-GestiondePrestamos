package Servicio;

import Excepciones.DatosInvalidosException;
import Excepciones.DuplicadoException;
import Excepciones.ElementoNoEncontradoException;
import Modelos.Cliente;
import Persistencia.ClienteDAO;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class ClienteServicio {
    private ClienteDAO dao;
    private ArrayList<Cliente> clientes;
    private HashSet<String> dnis;

    public ClienteServicio()throws SQLException {
        this.dao = new ClienteDAO();
        this.clientes = new ArrayList<>();
        this.dnis = new HashSet<>();
        cargarclientes();
    }

    public void cargarclientes()throws SQLException
    {
        List<Cliente> lista = dao.listar();
        clientes.clear();
        clientes.addAll(lista);
        dnis.clear();
        for (Cliente c : lista) {
            dnis.add(c.getDni());
        }
    }

    public Cliente ingresar(Cliente c)throws SQLException,DuplicadoException
    {
        Validaciones.Vacio(c.getNombre(),"Nombre");
        Validaciones.Vacio(c.getApellido(),"Apellido");
        Validaciones.Vacio(c.getDni(),"Dni");
        Validaciones.Vacio(c.getTelefono(),"Telefono");
        Validaciones.email(c.getEmail());
        Validaciones.Vacio(c.getDireccion(),"Direccion");

        if(dnis.contains(c.getDni()))
        {
            throw new DuplicadoException("Ya existe un cliente con el DNI " + c.getDni());
        }
        int idnuevo= dao.insertar(c);
        if(idnuevo> 0)
        {
            clientes.add(c);
            dnis.add(c.getDni());
        }
        return c;
    }

    public boolean modificar(Cliente c)throws SQLException, ElementoNoEncontradoException
    {Cliente existente = buscarId(c.getId());
        Validaciones.Vacio(c.getNombre(), "Nombre");
        Validaciones.Vacio(c.getApellido(), "Apellido");
        Validaciones.email(c.getEmail());
        Validaciones.Vacio(c.getDireccion(), "Direccion");

        boolean modificado = dao.modificar(c);
        if (modificado) {
            existente.setNombre(c.getNombre());
            existente.setApellido(c.getApellido());
            existente.setTelefono(c.getTelefono());
            existente.setEmail(c.getEmail());
            existente.setDireccion(c.getDireccion());
        }
        return modificado;
    }

    public boolean baja(int id)throws SQLException, ElementoNoEncontradoException
    {
        Cliente bajar = buscarId(id);
        if (!bajar.isActivo()) {
            throw new IllegalStateException("El cliente ya está dado de baja.");
        }
        boolean bajo = dao.DardeBaja(bajar);
        if (bajo) {
            bajar.setActivo(false);
        }
        return bajo;
    }

    public boolean reactivar(int id)throws SQLException,ElementoNoEncontradoException
    {
        Cliente activar = buscarId(id);
        if (activar.isActivo()) {
            throw new IllegalStateException("El cliente no está dado de baja.");
        }
        if (dao.reactivar(activar)) {
            activar.setActivo(true);
            return true;
        }
        return false;
    }

    public boolean eliminar(int id)throws SQLException,ElementoNoEncontradoException
    {
        Cliente sacar = buscarId(id);
        try {
            boolean eliminado = dao.eliminar(sacar);
            if (eliminado) {
                clientes.remove(sacar);
                dnis.remove(sacar.getDni());
            }
            return eliminado;
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new IllegalStateException("No se puede eliminar: el cliente tiene préstamos. Usar baja lógica.");
        }
    }

    public List<Cliente> listaactiva()
    {
        List <Cliente> activos = new ArrayList<>();
        for(Cliente c : clientes)
        {
            if(c.isActivo())
            {
                activos.add(c);
            }
        }
        return activos;
    }

    public List<Cliente> listainactiva()
    {
        List <Cliente> inactivos = new ArrayList<>();
        for(Cliente c : clientes)
        {
            if(!c.isActivo())
            {
                inactivos.add(c);
            }
        }
        return inactivos;
    }

    public List<Cliente> listacompleta()
    {
        List <Cliente> todos = new ArrayList<>();

        todos.addAll(clientes);

        return todos;

    }


    public Cliente buscarId(int id)throws ElementoNoEncontradoException
    {
        for(Cliente c : clientes)
        {
            if(c.getId()==id)
            {
                return c;
            }
        }
        throw new ElementoNoEncontradoException("No existe un cliente con id " + id);
    }

    public Cliente buscarDni(String dni)throws ElementoNoEncontradoException
    {///Exceptcion personalizadda agregar-----------------------------------------------------------------
        for (Cliente c : clientes) {
            if (c.getDni().equals(dni)) {
                return c;
            }
        }
        throw new ElementoNoEncontradoException("No existe un cliente con DNI " + dni);
    }
}
