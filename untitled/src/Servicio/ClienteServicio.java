package Servicio;

import Modelos.Cliente;
import Persistencia.ClienteDAO;

import java.sql.SQLException;
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

    public Cliente ingresar(Cliente c)throws SQLException
    {
        Validaciones.Vacio(c.getNombre(),"Nombre");
        Validaciones.Vacio(c.getApellido(),"Apellido");
        Validaciones.Vacio(c.getDni(),"Dni");
        Validaciones.Vacio(c.getTelefono(),"Telefono");
        Validaciones.email(c.getEmail());
        Validaciones.Vacio(c.getDireccion(),"Direccion");

        if(dnis.contains(c.getDni()))
        {
            ///Exceptcion personalizada agregar-------------------------------------------------------------------
        }
        int idnuevo= dao.insertar(c);
        if(idnuevo> 0)
        {
            clientes.add(c);
            dnis.add(c.getDni());
        }
        return c;
    }

    public boolean modificar(Cliente c)throws SQLException
    {///Exceptcion personalizada agregar-----------------------------------------------------------------
        Validaciones.Vacio(c.getNombre(),"Nombre");
        Validaciones.Vacio(c.getApellido(),"Apellido");
        Validaciones.Vacio(c.getTelefono(),"Telefono");
        Validaciones.email(c.getEmail());
        Validaciones.Vacio(c.getDireccion(),"Direccion");

        boolean modificado=dao.modificar(c);

        if(modificado)
        {
            for(int i=0;i<clientes.size();i++)
            {
                if(clientes.get(i).getId()==c.getId())
                {
                    clientes.set(i,c);
                }
            }
        }

    }

    public boolean baja(int id)throws SQLException
    {///Exceptcion personalizada agregar-----------------------------------------------------------------
        Cliente bajar = buscarId(id);
        if (dao.DardeBaja(bajar)) {
            bajar.setActivo(false);
        }
    }

    public boolean reactivar(int id)throws SQLException
    {///Exceptcion personalizada agregar-----------------------------------------------------------------
        Cliente activar = buscarId(id);
        if (dao.reactivar(activar)) {
            activar.setActivo(true);
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


    public Cliente buscarId(int id)
    {///Exceptcion personalizada agregar-----------------------------------------------------------------
        for(Cliente c : clientes)
        {
            if(c.getId()==id)
            {
                return c;
            }
        }
        return null;
    }

    public Cliente buscarDni(String dni)
    {///Exceptcion personalizada agregar-----------------------------------------------------------------
        for (Cliente c : clientes) {
            if (c.getDni().equals(dni) && c.isActivo()) {
                return c;
            }
        }
        return null;
    }
}
