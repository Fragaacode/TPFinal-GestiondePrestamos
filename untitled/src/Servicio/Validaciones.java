package Servicio;

public class Validaciones {

    public static void Vacio(String ingreso,String lugar)
    {
        if(ingreso==null || ingreso.isBlank())
        {
            throw new IllegalArgumentException("En "+lugar+" no puede estar vacio");
        }
    }

    public static void email(String mail)
    {
        Vacio(mail,"email");
        if (!mail.matches("^[\\w.+-]+@[\\w-]+\\.[\\w.]+$")) {
            throw new IllegalArgumentException("El email no tiene un formato válido.");
        }
    }

    public static void montos(double valor,String lugar)
    {
        if(valor<=0)
        {
            throw new IllegalArgumentException("En '" + lugar + "' el monto debe ser mayor a 0.");
        }
    }

}
