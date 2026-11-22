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

    public ClienteServicio(ClienteDAO dao, ArrayList<Cliente> clientes, HashSet<String> dnis) {
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


}
