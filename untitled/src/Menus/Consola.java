package Menus;

import java.util.List;
import java.util.Scanner;

public class Consola {
    private static final Scanner sc = new Scanner(System.in);

    private Consola() {
    }

    public static String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return sc.nextLine().trim();
    }

    public static String cambiarTexto(String mensaje, String puesto)
    {
        String ingresado = leerTexto(mensaje + " [" + puesto + "]: ");
        return ingresado.isEmpty() ? puesto : ingresado;
    }

    public static int leerentero(String mensaje)
    {
        while(true)
        {
            try
            {
                return Integer.parseInt(leerTexto(mensaje));
            }
            catch (NumberFormatException e)
            {
                System.out.println("Ingrese un numero entero");
            }
        }
    }

    public static double leerDouble(String mensaje)
    {
        while (true)
        {
            try
            {
                return Double.parseDouble(leerTexto(mensaje));
            }
            catch (NumberFormatException e)
            {
                System.out.println("Ingrese un numero valido(Usar . no ,)");
            }
        }
    }

    public static void mostrarLista(List<?> lista) {
        for (Object elemento : lista) {
            System.out.println(elemento);
        }
        System.out.println("Total: " + lista.size());
    }
}
