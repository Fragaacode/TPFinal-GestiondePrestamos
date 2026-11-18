package Modelos;

public class Cliente extends Persona {
    private String telefono;
    private String email;
    private String direccion;

    public Cliente() {
    }

    public Cliente(int id, String nombre, String apellido, String dni, boolean activo,String telefono, String email, String direccion) {
        super(id, nombre, apellido, dni, activo);
        this.telefono = telefono;
        this.email = email;
        this.direccion = direccion;
    }

    public Cliente(String nombre, String apellido, String dni,String telefono, String email, String direccion) {
        super(nombre, apellido, dni);
        this.telefono = telefono;
        this.email = email;
        this.direccion = direccion;
    }

    @Override
    public String getTipo() {
        return "Cliente";
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    @Override
    public String toString() {
        return "Cliente{" +
                "telefono='" + telefono + '\'' +
                ", email='" + email + '\'' +
                ", direccion='" + direccion + '\'' +
                '}';
    }
}