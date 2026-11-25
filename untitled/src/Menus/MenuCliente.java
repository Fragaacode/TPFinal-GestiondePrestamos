package Menus;

import Modelos.Cliente;
import Servicio.ClienteServicio;
import Servicio.PrestamoServicio;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MenuCliente {
private ClienteServicio servicio;
private PrestamoServicio prestamos;

    public MenuCliente(ClienteServicio servicio, PrestamoServicio prestamos) {
        this.servicio = servicio;
        this.prestamos = prestamos;
    }

    public void menu()
    {
        int opcion;
        do
        {
            System.out.println("------GESTION DE CLIENTES-------");
            System.out.println("1- Ingresar Cliente");
            System.out.println("2-Modificar CLiente");
            System.out.println("3-Dar de baja un Cliente");
            System.out.println("4-Reactivar Cliente");
            System.out.println("5-Lista de Clientes");
            System.out.println("6-Lista de Clientes Activos");
            System.out.println("7-Lista de Clientes Inactivos");
            System.out.println("8-Historial Crediticio de un Cliente");
            System.out.println("9-Eliminar Cliente");
            System.out.println("0-Volver");
            opcion = Consola.leerentero("Opcion: ");

            try {
                switch (opcion)
                {
                    case 1:
                    {
                        ingresar();
                        break;
                    }
                    case 2:
                    {
                        modificar();
                        break;
                    }
                    case 3:
                    {
                        DardeBaja();
                        break;
                    }
                    case 4:
                    {
                        reactivar();
                        break;
                    }
                    case 5:
                    {
                        listar();
                        break;
                    }
                    case 6:
                    {
                        listarActivos();
                        break;
                    }
                    case 7:
                    {
                        listarInactivos();
                        break;
                    }
                    case 8:
                    {
                        historial();
                        break;
                    }
                    case 9:
                    {
                        eliminar();
                        break;
                    }
                    case 0:
                    {
                        break;
                    }
                    default:
                    {
                        System.out.println("Opcion invalida");
                    }
                }


            }
            catch (SQLException e)
            {
                System.err.println("Error con la Bdd "+ e.getMessage());
            }
        }while (opcion!=0);
    }

    public void ingresar()throws SQLException
    {
        String nombre = Consola.leerTexto("Nombre: ");
        String apellido = Consola.leerTexto("Apellido: ");
        String dni = Consola.leerTexto("DNI (sin puntos): ");
        String telefono = Consola.leerTexto("Teléfono: ");
        String email = Consola.leerTexto("Email: ");
        String direccion = Consola.leerTexto("Dirección: ");
        Cliente nuevo = new Cliente (nombre, apellido, dni, telefono, email, direccion);
        nuevo= servicio.ingresar(nuevo);
        System.out.println("Cliente creado con id " + nuevo.getId());
    }

    public void modificar()throws SQLException
    {
        Cliente modificado=servicio.buscarDni(Consola.leerTexto("Ingrese el DNI del cliente que desea modificar"));
        System.out.println("Ponga Enter si no quiere cambiar el campo:");
        String nombre =Consola.cambiarTexto("Nombre", modificado.getNombre());
        String apellido= Consola.cambiarTexto("Apellido", modificado.getApellido());
        String telefono= Consola.cambiarTexto("Teléfono", modificado.getTelefono());
        String mail= Consola.cambiarTexto("Email", modificado.getEmail());
        String direccion= Consola.cambiarTexto("Dirección", modificado.getDireccion());
        Cliente cambios = new Cliente(modificado.getId(), nombre, apellido, modificado.getDni(), modificado.isActivo(),
                telefono, mail, direccion);
        boolean modificado2= servicio.modificar(cambios);
        if(modificado2)
        {
            System.out.println("Cliente modificado.");
        }
    }

    public void DardeBaja()throws SQLException
    {

        listarActivos();
        Cliente bajar =servicio.buscarDni(Consola.leerTexto("Ingrese el DNI del cliente que desee dar de baja"));
       boolean bajo= servicio.baja(bajar.getId());

       if(bajo)
       {
           System.out.println("Cliente dado de baja");
       }
    }

    public void reactivar()throws SQLException
    {
        listarInactivos();
        Cliente activar = servicio.buscarDni(Consola.leerTexto("Ingrese el DNI del cliente que desee reactivar"));
        boolean reactivo= servicio.reactivar(activar.getId());

        if(reactivo)
        {
            System.out.println("Cliente reactivado");
        }
    }

    public void listar()
    {
        List<Cliente> todos= servicio.listacompleta();
        Consola.mostrarLista(todos);
    }

    public void listarActivos()
    {
        List<Cliente> activos= servicio.listaactiva();
        Consola.mostrarLista(activos);
    }

    public void listarInactivos()
    {
        List<Cliente> inactivos= servicio.listainactiva();
        Consola.mostrarLista(inactivos);
    }

    public void historial()
    {
        ///HACER METODO EN PRESTAMOSERVICIO
    }

    public void eliminar()throws SQLException
    {
        listar();
        int id= Consola.leerentero("Ingrese el id del cliente que desea eliminar definitivamente");
        boolean eliminado= servicio.eliminar(id);
        if(eliminado)
        {
            System.out.println("Cliente eliminado de la BDD");
        }
    }


}
