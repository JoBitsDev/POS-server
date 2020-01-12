package com.restmanager.models;

import com.restmanager.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import restmanager.resources.*;

/**
 * FirstDream
 *
 * @author Jorge
 *
 */
/**
 * Clase para hacer los calculos con un dia de ventas en tiempo real
 *
 * @author Jorge
 */
public class VentaCalculator {

    //******************************************************************************************************************
    //******************************************************************************************************************
    // ********************************Resumenes en objetos**********************************************************
    //******************************************************************************************************************
    //******************************************************************************************************************
    public static ArrayList<ProductovOrden> getResumenVentas(Venta v) {
        ArrayList<ProductovOrden> ret = new ArrayList<>();
        ArrayList<Orden> aux = new ArrayList(v.getOrdenList());

        //llenando l array
        llenarArrayProductoVOrden(ret, aux, null, false, false);

        Collections.sort(ret, (o1, o2) -> {
            return o1.getProductoVenta().getNombre().compareTo(o2.getProductoVenta().getNombre());
        });
        return ret;
    }

    public static List<ProductovOrden> getResumenVentaPorArea(Venta v, Area a) {
        //inicializando los datos
        ArrayList<ProductovOrden> ret = new ArrayList<>();
        ArrayList<Orden> aux = new ArrayList(v.getOrdenList());

        //llenando l array
        for (Orden o : aux) {
            if (o.getMesacodMesa().getAreacodArea().equals(a) && !o.getDeLaCasa()) {
                joinListsProductovOrdenByCocina(ret, new ArrayList<>(o.getProductovOrdenList()), null);
            }

        }//nˆ3

        return ret;
    }

    public static List<ProductovOrden> getResumenVentasCamarero(Venta v, Personal p) {

        //inicializando los datos
        ArrayList<ProductovOrden> ret = new ArrayList<>();
        ArrayList<Orden> aux = new ArrayList(v.getOrdenList());

        //llenando l array
        for (Orden o : aux) {
            if (o.getPersonalusuario() != null) {
                if (!o.getDeLaCasa() && o.getPersonalusuario().equals(p)) {
                    joinListsProductovOrdenByCocina(ret,
                            new ArrayList(o.getProductovOrdenList()), null);
                }
            }
        }//nˆ3

        return ret;
    }

    public static List<ProductovOrden> getResumenVentasCocina(Venta v, Cocina c) {
        //inicializando los datos
        ArrayList<ProductovOrden> ret = new ArrayList<>();
        ArrayList<Orden> aux = new ArrayList(v.getOrdenList());

        //llenando l array
        for (Orden o : aux) {
            if (!o.getDeLaCasa()) {
                joinListsProductovOrdenByCocina(ret,
                        new ArrayList<>(o.getProductovOrdenList()), c);
            }

        }//nˆ3

        return ret;
    }

    public static List<ProductovOrden> getResumenVentasCasa(Venta v) {
        //inicializando los datos
        ArrayList<ProductovOrden> ret = new ArrayList<>();
        ArrayList<Orden> aux = new ArrayList(v.getOrdenList());

        //llenando l array
        llenarArrayProductoVOrden(ret, aux, null, true, false);

        return ret;
    }

    //
    //Métodos referentes a los resumenes del consumo de insumos
    //
    public static List<ProductoInsumo> getResumenGastosCocina(Cocina c, Venta v) {

        //inicializando los datos
        ArrayList<ProductoInsumo> ret = new ArrayList<>();
        ArrayList<Orden> aux = new ArrayList(v.getOrdenList());

        //llenando l array
        for (Orden o : aux) {
            for (ProductovOrden p : o.getProductovOrdenList()) {
                if (p.getProductoVenta().getCocinacodCocina().equals(c)) {
                    joinListsProductoInsumos(ret,
                            new ArrayList<>(p.getProductoVenta().
                                    getProductoInsumoList()), p.getCantidad());
                }
            }
        }
        return ret;
    }

    //******************************************************************************************************************
    //******************************************************************************************************************
    // ********************************Metodos para estadisticas de ventas*******************************************
    //******************************************************************************************************************
    //******************************************************************************************************************
    public static float getValorTotalVentas(Venta v) {
        float total = 0;
        for (Orden x : v.getOrdenList()) {
            if (!x.getDeLaCasa() && x.getHoraTerminada() != null) {
                total += x.getOrdenvalorMonetario();
            }
        }
        return total;
    }

    public static float getValorVentasCocina(Venta v, Cocina c) {
        //inicializando los datos
        ArrayList<ProductovOrden> ret = new ArrayList<>();
        ArrayList<Orden> aux = new ArrayList(v.getOrdenList());

        llenarArrayProductoVOrden(ret, aux, c, false, false);

        float valor = 0;
        for (ProductovOrden x : ret) {
            valor += x.getCantidad() * x.getProductoVenta().getPrecioVenta();
        }

        return valor;
    }

    public static List<Orden> getOrdenesActivas(Venta ventas) {

        ArrayList<Orden> ordenes = new ArrayList<>(ventas.getOrdenList());

        Collections.sort(ordenes, (Orden o1, Orden o2) -> {
            int idO1, idO2;
            idO1 = Integer.parseInt(o1.getCodOrden().substring(2));
            idO2 = Integer.parseInt(o2.getCodOrden().substring(2));
            return -1 * Integer.compare(idO1, idO2);
        });

        List<Orden> retOrd = new ArrayList<>();
        List<String> existingMesasName = new ArrayList<>();

        ordenes.forEach((o) -> {
            String codMesa = o.getMesacodMesa().getCodMesa();

            if (o.getHoraTerminada() == null) {
                retOrd.add(o);
                existingMesasName.add(codMesa);

            } else {
                if (!existingMesasName.contains(codMesa)) {
                    //if(o.getHoraTerminada().)
                    retOrd.add(o);
                    existingMesasName.add(codMesa);
                }
            }
        });
        return retOrd;
    }

    public static float getValorTotalGastosInsumo(Venta instance) {
        float total = 0;
        for (Orden x : instance.getOrdenList()) {
            if (x.getHoraTerminada() != null) {
                total += x.getOrdengastoEninsumos();
            }
        }
        return total;
    }

    /**
     *
     * @param v - la Venta a calcular la hora pico
     * @return un entero del 0-23 con la hora pico del dia de ventas
     */
    public static int getPickHour(Venta v) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(v.getFecha().getTime());
        int current_day = c.get(Calendar.DAY_OF_MONTH);
        float monto_hora_pico = 0;
        int hora_pico = 0,
                hora = 0;

        Collections.sort(v.getOrdenList(), (o1, o2) -> {
            return o1.getCodOrden().compareTo(o2.getCodOrden());
        });
        while (hora < 24) {
            float aux_hora_pico = 0;
            for (Orden o : v.getOrdenList()) {
                if (o.getHoraComenzada().getHours() == hora && !o.getDeLaCasa()) {
                    aux_hora_pico += o.getOrdenvalorMonetario();
                }
            }
            if (aux_hora_pico > monto_hora_pico) {
                monto_hora_pico = aux_hora_pico;
                hora_pico = hora;
            }
            hora++;
        }
        return hora_pico;
    }

    public static int getModalPickHour(List<Venta> ventas) {
        int[] modas = new int[24];
        ventas.forEach((v) -> {
            modas[getPickHour(v)]++;
        });
        int mayor_moda = 0, cantidad_repeticiones = 0;

        for (int i = 0; i < modas.length; i++) {
            if (modas[i] > cantidad_repeticiones) {
                cantidad_repeticiones = modas[i];
                mayor_moda = i;
            }
        }
        return mayor_moda;
    }

    public static float getValorTotalVentasNeta(Venta v) {
        float total = 0;
        for (Orden x : v.getOrdenList()) {
            if (!x.getDeLaCasa() && x.getHoraTerminada() != null) {
                for (ProductovOrden p : x.getProductovOrdenList()) {
                    total += p.getCantidad() * p.getProductoVenta().getPrecioVenta();
                }
            }
        }
        return total;
    }

    public static float getValorTotalPagoTrabajadores(Venta instance) {
        float pagoTotal = 0;
        for (AsistenciaPersonal a : instance.getAsistenciaPersonalList()) {
            pagoTotal += a.getPago();
        }
        return utils.setDosLugaresDecimalesFloat(pagoTotal);
    }

    public static float getValorPagoPorVentas(Venta instance) {
        ArrayList<ProductovOrden> list = getResumenVentas(instance);
        float ret = 0;
        for (ProductovOrden x : list) {
            ProductoVenta v = x.getProductoVenta();
            ret += v.getPagoPorVenta() != null ? v.getPagoPorVenta() * x.getCantidad() : 0;
        }
        return utils.setDosLugaresDecimalesFloat(ret);
    }

    public static float getValorTotalPorcientoVenta(Venta v) {
        return getValorTotalVentas(v) - getValorTotalVentasNeta(v);
    }

    public static float getValorTotalOtrosGastos(Venta v) {
        float total = 0;
        for (GastoVenta gasto : v.getGastoVentaList()) {
            total += gasto.getImporte();
        }
        return total;
    }

    public static float getValorTotalVentasCasa(Venta v) {
        return getResumenVentasCasa(v).stream().map((pd) -> pd.getCantidad() * pd.getProductoVenta().getPrecioVenta()).reduce(total, (accumulator, _item) -> accumulator + _item);
    }

    //******************************************************************************************************************
    //******************************************************************************************************************
    // ********************************Metodos privados de la clase **************************************************
    //******************************************************************************************************************
    //******************************************************************************************************************
    /**
     * agrega a un arreglo de ProductosvOrdenes una nueva orden
     *
     * @param pivot - el arreglo al que agregar la nueva orden
     * @param b - la orden a agregar
     *
     */
    private static void llenarArrayProductoVOrden(ArrayList<ProductovOrden> ret, ArrayList<Orden> aux, Cocina c, boolean condicionAutorizo, boolean aceptarOrdenesAbiertas) {
        for (Orden o : aux) {
            if (o.getDeLaCasa() == condicionAutorizo) {
                if (aceptarOrdenesAbiertas || o.getHoraTerminada() != null) {
                    joinListsProductovOrdenByCocina(ret,
                            new ArrayList(o.getProductovOrdenList()), c);
                }
            }
        }
    }

    private static float convertProductoOrdenToRowData(ArrayList[] rowData, ArrayList<ProductovOrden> ret) {
        float total = 0;
        Collections.sort(ret, (ProductovOrden o1, ProductovOrden o2) -> o1.getProductoVenta().getNombre().compareTo(o2.getProductoVenta().getNombre()));
        for (ProductovOrden x : ret) {
            rowData[0].add(x.getProductoVenta().getNombre());
            rowData[1].add(x.getProductoVenta().getPrecioVenta());
            rowData[2].add(x.getCantidad());
            rowData[3].add(utils.setDosLugaresDecimalesFloat(x.getProductoVenta().getPrecioVenta() * x.getCantidad()));

            total += x.getProductoVenta().getPrecioVenta() * x.getCantidad();
        }

        return total;
    }

    private static void joinListsProductovOrdenByCocina(ArrayList<ProductovOrden> pivot,
            ArrayList<ProductovOrden> b, Cocina c) {

        while (!b.isEmpty()) {
            boolean founded = false;
            for (int j = 0; j < pivot.size() && !founded; j++) {
                if (b.get(0).getProductoVenta().getPCod().equals(
                        pivot.get(j).getProductoVenta().getPCod())) {
                    founded = true;
                    pivot.get(j).setCantidad(pivot.get(j).getCantidad() + b.get(0).getCantidad());
                }
            }
            if (!founded && (c == null || b.get(0).getProductoVenta().getCocinacodCocina().equals(c))) {
                ProductovOrden po = new ProductovOrden(b.get(0).getProductovOrdenPK());
                po.setCantidad(b.get(0).getCantidad());
                po.setEnviadosacocina(b.get(0).getEnviadosacocina());
                po.setOrden(b.get(0).getOrden());
                po.setProductoVenta(b.get(0).getProductoVenta());
                pivot.add(po);
            }
            b.remove(0);

        }

    }

    /**
     * este metodo es para acumular la relacion producto insumo en un solo
     * arreglo
     *
     * @param pivot - el arreglo que se tiene para acumular
     * @param b - el nuevo productos que se va a incluir
     * @param cant - la cantidad de ese producto
     */
    private static void joinListsProductoInsumos(
            ArrayList<ProductoInsumo> pivot,
            ArrayList<ProductoInsumo> b, float cant) {

        for (ProductoInsumo x : b) {
            boolean founded = false;

            for (int j = 0; j < pivot.size() && !founded; j++) {
                if (x.getInsumo().getCodInsumo().
                        equals(pivot.get(j).getInsumo().getCodInsumo())) {
                    founded = true;
                    pivot.get(j).setCantidad(pivot.get(j).getCantidad() + (x.getCantidad() * cant));
                    pivot.get(j).setCosto(pivot.get(j).getCosto() + (x.getCosto() * cant));
                }
            }
            if (!founded) {
                ProductoInsumo pi = new ProductoInsumo(x.getProductoInsumoPK());
                pi.setCantidad(x.getCantidad() * cant);
                pi.setCosto(x.getCosto() * cant);
                pi.setInsumo(x.getInsumo());
                pi.setProductoVenta(x.getProductoVenta());
                pivot.add(pi);

            }
        }

    }

}
