package Servicio;

import Excepciones.ElementoNoEncontradoException;
import Excepciones.PrestamoInvalidoException;
import Modelos.Cuota;
import Persistencia.CuotaDAO;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CuotaServicio {
private CuotaDAO dao;
    private ArrayList<Cuota> cuotas;

    public CuotaServicio()throws SQLException {
        this.dao = new CuotaDAO();
        this.cuotas =  new ArrayList<>();
        cargarcuotas();
    }

    public void cargarcuotas() throws SQLException {
        List<Cuota> lista = dao.listar();
        cuotas.clear();
        cuotas.addAll(lista);
    }

    public void ingresarCuotas(List<Cuota> nuevas) throws SQLException {
        for (Cuota c : nuevas) {
            if (dao.insertar(c) > 0) {
                cuotas.add(c);
            }
        }
    }

    public Cuota registrarPago(int idCuota)throws SQLException, ElementoNoEncontradoException, PrestamoInvalidoException
    {
        Cuota cuota = buscarId(idCuota);
        if (cuota.isPagada()) {
            throw new PrestamoInvalidoException("La cuota #" + idCuota + " ya fue pagada el " + cuota.getFechaPago());
        }
        LocalDate hoy = LocalDate.now();
        Cuota pagada = new Cuota(cuota.getIdCuota(), cuota.getIdPrestamo(), cuota.getNumero(), cuota.getMonto(),
                cuota.getFechaVencimiento(), true, hoy, cuota.isActivo());

        if (dao.modificar(pagada)) {
            cuota.registrarPago(hoy);
        }
        return cuota;
    }

    public boolean baja(int idCuota) throws SQLException,ElementoNoEncontradoException {
        Cuota anular = buscarId(idCuota);
        boolean anulada = dao.DardeBaja(anular);
        if (anulada) {
            anular.setActivo(false);
        }
        return anulada;
    }

    public List<Cuota> listarPorPrestamo(int idPrestamo) {
        List<Cuota> resultado = new ArrayList<>();
        for (Cuota c : cuotas) {
            if (c.getIdPrestamo() == idPrestamo && c.isActivo()) {
                resultado.add(c);
            }
        }
        return resultado;
    }

    public List<Cuota> listaactiva() {
        List<Cuota> activas = new ArrayList<>();
        for (Cuota c : cuotas) {
            if (c.isActivo()) {
                activas.add(c);
            }
        }
        return activas;
    }

    public Cuota buscarId(int id)throws ElementoNoEncontradoException {
        for (Cuota c : cuotas) {
            if (c.getIdCuota() == id && c.isActivo()) {
                return c;
            }
        }
        throw new ElementoNoEncontradoException("No existe una cuota activa con id " + id);
    }

}
