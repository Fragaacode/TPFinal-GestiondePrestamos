package Menus;

import Modelos.Cliente;
import Servicio.ClienteServicio;
import Servicio.PrestamoServicio;

import java.sql.SQLException;
import java.util.Scanner;

public class MenuCliente {
private ClienteServicio servicio;
private PrestamoServicio prestamos;

    public MenuCliente(ClienteServicio servicio, PrestamoServicio prestamos) {
        this.servicio = servicio;
        this.prestamos = prestamos;
    }
Scanner sc = new Scanner(System.in);

    public void menu()
    {
        int opcion=1;
        while(opcion!=0)
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
            opcion=sc.nextInt();

            try {
                switch (opcion)
                {
                    case 1:
                    {
                        break;
                    }
                    case 2:
                    {

                    }
                    case 3:
                    {
                        break;
                    }
                    case 4:
                    {

                    }
                    case 5:
                    {
                        break;
                    }
                    case 6:
                    {

                    }
                    case 7:
                    {
                        break;
                    }
                    case 8:
                    {

                    }
                    case 9:
                    {
                        break;
                    }
                    case 0:
                    {

                    }
                }


            }
            catch (SQLException e)
            {

            }
        }
    }
}
