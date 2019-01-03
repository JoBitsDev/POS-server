/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.restmanager.printservice;

import com.restmanager.Carta;
import com.restmanager.Cocina;
import com.restmanager.CocinaJpaController;
import com.restmanager.Orden;
import com.restmanager.ProductovOrden;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;

/**
 *
 * @author Jorge
 */
public class Impresion {

    /**
     * @param args the command line arguments
     */
    protected EntityManagerFactory e = Persistence.createEntityManagerFactory("Restaurant_Manager_Web_ServicePU");
    protected EntityManager em1 = e.createEntityManager();

    private String nombreRest = null;
    private boolean monedaCUC = false;
    private static float cambio = 25;
    private String CABECERA = "Restaurante",
            COCINA = "",
            DELACASA = "(Pedido por la casa)",
            ORDEN = "Orden No: ",
            MESA = "Mesa: ",
            FECHA = "Fecha: ",
            CAMARERO = "Camarero(a): ",
            SUBTOTAL = "SubTotal: ",
            TOTAL = "Total: ",
            PIE = "Vuelva Pronto",
            MONEDA = "",
            CUC = " CUC",
            MN = " MN",
            SYNC = "Sale con: ",
            CANCELACION = "CANCELACION";

    SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd'/'MM'/'yy ' ' ");
    SimpleDateFormat TIME_FORMAT = new SimpleDateFormat(" hh ':' mm ' ' a ");

    private final String DEFAULT_KITCHEN_PRINTER_LOCATION = "Cocina";
    private final String DEFAULT_PRINT_LOCATION = null;
    private static int cantidadCopias = 0;

    ArrayList<CopiaTicket> RAM = new ArrayList<>();

    private static void feedPrinter(byte[] b, String printerName) {

        PrintService[] prints = PrintServiceLookup.lookupPrintServices(null, null);
        DocPrintJob job = PrintServiceLookup.lookupDefaultPrintService().createPrintJob();

        for (int i = 0; i < prints.length; i++) {
            if (prints[i].getName().equals(printerName)) {
                job = prints[i].createPrintJob();
            }
        }

        DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
        Doc doc = new SimpleDoc(b, flavor, null);

        try {
            job.print(doc, null);
        } catch (PrintException ex) {
            Logger.getLogger(Impresion.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Constructor por defecto
     *
     * @param m una instancia de una carta especifica
     */
    public Impresion(Carta m) {
        this(m, m.getMonedaPrincipal().equals("CUC"), cambio);

    }

    /**
     *
     * @param m
     * @param monedaCUC
     * @param cambio
     */
    public Impresion(Carta m, boolean monedaCUC, float cambio) {
        this(m, null, monedaCUC, cambio, cantidadCopias);

    }

    /**
     *
     * @param m
     * @param footer
     */
    public Impresion(Carta m, String footer) {
        this(m, m.getMonedaPrincipal().equals("CUC"), cambio);
        PIE = footer;
    }

    public Impresion(Carta m, String footer, boolean monedaCUC, float cambio, int cantidadCopias) {
        this.nombreRest = m.getNombreCarta();
        Impresion.cambio = cambio;
        Impresion.cantidadCopias = cantidadCopias;
        if (footer != null) {
            PIE = footer;
        }
        if (this.monedaCUC = monedaCUC) {
            MONEDA = CUC;
        } else {
            MONEDA = MN;
        }
    }

    public void print(Orden o, boolean preview) throws PrintException {

        float total = 0;

        Ticket p = new Ticket();
        p.resetAll();
        p.initialize();
        //p.feedBack((byte)2);
        p.alignCenter();
        p.setText(CABECERA);
        p.newLine();
        p.setText(this.nombreRest);
        p.newLine();

        if (o.getDeLaCasa()) {
            p.doubleStrik(true);
            p.setText(DELACASA);
            p.doubleStrik(false);
            p.newLine();
        }
        p.addLineSeperator();
        p.newLine();
        p.alignRight();
        p.setText(FECHA + this.DATE_FORMAT.format(o.getVentafecha().getFecha()) + TIME_FORMAT.format(o.getHoraTerminada()));
        p.newLine();
        p.setText(ORDEN + o.getCodOrden());
        p.newLine();
        p.setText(MESA + o.getMesacodMesa().getCodMesa());
        p.newLine();
        p.alignLeft();
        p.setText(CAMARERO);
        p.newLine();
        p.alignRight();
        p.setText(o.getPersonalusuario().getDatosPersonales().getNombre());

        p.newLine();
        p.addLineSeperator();
        p.newLine();

        for (ProductovOrden x : o.getProductovOrdenList()) {
            p.alignLeft();
            p.setText(x.getCantidad() + " " + x.getProductoVenta().getNombre());
            p.newLine();
            p.alignRight();
            p.setText(redondeoDeMonedaMN_CUC((int) (x.getCantidad() * x.getProductoVenta().getPrecioVenta() * 100)) + MONEDA);
            p.newLine();
            total += x.getCantidad() * x.getProductoVenta().getPrecioVenta();
        }

        String subTotalPrint = redondeoDeMonedaMN_CUC((int) (total * 100));
        String sumaPorciento = redondeoDeMonedaMN_CUC((int) ((Float.valueOf(subTotalPrint) / o.getPorciento()) * 100));
        String totalPrint = subTotalPrint;
        p.alignRight();
        p.newLine();
        // p.setText(SUBTOTAL + subTotalPrint + MONEDA);
        if (o.getPorciento() != 0) {
            p.newLine();
            p.setText("+ " + o.getPorciento() + "% : " + sumaPorciento + MONEDA);
            totalPrint = redondeoDeMonedaMN_CUC((int) ((Float.valueOf(subTotalPrint) + Float.valueOf(sumaPorciento)) * 100));

        }
        p.newLine();
        p.addLineSeperator();
        p.newLine();
        p.alignRight();
        p.setText(TOTAL + totalPrint + MONEDA);
        p.newLine();

        if (monedaCUC) {
            p.setText(TOTAL + redondeoDeMonedaMN_CUC((int) (Float.valueOf(totalPrint) * cambio * 100)) + MN);
        } else {
            p.setText(TOTAL + redondeoDeMonedaMN_CUC((int) (100 * Float.valueOf(totalPrint) / cambio)) + CUC);
        }

        p.newLine();
        p.newLine();

        p.alignCenter();
        p.setText(this.PIE);
        p.newLine();
        p.feed((byte) 3);
        if (preview) {
            p.finit();
        } else {
            p.finit();
            //p.finitAndDrawerKick();
        }

        feedPrinter(p.finalCommandSet().getBytes(), DEFAULT_PRINT_LOCATION);

    }

    public Orden printKitchen(Orden o) throws PrintException {

        return printKitchenForced(printKitchen(o, new CocinaJpaController(e).findCocina("C-2"), ""));
//
//        Ticket p = new Ticket();
//        p.resetAll();
//        p.initialize();
//        //p.feedBack((byte)2);
//        p.alignCenter();
//        p.setText(this.nombreRest);
//        p.newLine();
//        p.addLineSeperator();
//        p.newLine();
//        p.alignRight();
//        p.setText(FECHA + this.DATE_FORMAT.format(o.getVentafecha().getFecha())
//                + TIME_FORMAT.format(new Date()));
//        p.newLine();
//        p.setText(ORDEN + o.getCodOrden());
//        p.newLine();
//        p.setText(MESA + o.getMesacodMesa().getCodMesa());
//        p.newLine();
//        p.addLineSeperator();
//        p.newLine();
//        p.alignLeft();
//
//        List<Cocina> cocinasExistentesEnLaOrden = new ArrayList<>();
//        for (ProductovOrden x : o.getProductovOrdenList()) {
//            if (!cocinasExistentesEnLaOrden.contains(x.getProductoVenta().getCocinacodCocina())
//                    && x.getEnviadosacocina() < x.getCantidad()) {
//                cocinasExistentesEnLaOrden.add(x.getProductoVenta().getCocinacodCocina());
//            }
//        }
//        if (cocinasExistentesEnLaOrden.size() > 1) {
//            for (int i = 0; i < cocinasExistentesEnLaOrden.size(); i++) {
//                String sync = SYNC;
//                for (int j = 0; j < cocinasExistentesEnLaOrden.size(); j++) {
//                    if (i == j) {
//                        continue;
//                    }
//                    sync += cocinasExistentesEnLaOrden.get(j).getNombreCocina() + " ";
//                }
//                printKitchen(o, cocinasExistentesEnLaOrden.get(i), sync);
//            }
//        } else {
//            if (cocinasExistentesEnLaOrden.size() > 0) {
//                printKitchen(o, cocinasExistentesEnLaOrden.get(0), p.newLine());
//            }
//
//        }
//
//        cleanAndPrintRAM();
//
//        return o;
    }

    /**
     * imprime una orden por la impresora predeterminada
     *
     * @param o la orden que se va a imprimir
     * @param c la cocina hacia donde se va a imprimir
     * @param sync es string de sincronizacion. ej: si los productos van a salir
     * con los de otra cocina
     * @return
     * @throws PrintException
     */
    public Orden printKitchen(Orden o, Cocina c, String sync) throws PrintException {
        boolean ordenSinPlatos = true;

        Ticket p = new Ticket();
        p.resetAll();
        p.initialize();
        //p.feedBack((byte)2);
        p.alignCenter();
        p.setText(this.nombreRest);
        p.newLine();
        p.emphasized(true);
        p.setText(COCINA + c.getNombreCocina());
        p.emphasized(false);
        p.newLine();
        p.addLineSeperator();
        p.newLine();
        p.alignRight();
        p.setText(FECHA + this.DATE_FORMAT.format(o.getVentafecha().getFecha()) + TIME_FORMAT.format(new Date()));
        p.newLine();
        p.setText(ORDEN + o.getCodOrden());
        p.newLine();
        p.setText(MESA + o.getMesacodMesa().getCodMesa());
        p.newLine();
        p.setText(o.getPersonalusuario().getDatosPersonales().getNombre());
        p.newLine();
        //p.doubleHeight(true);
        p.addLineSeperator();
        p.newLine();
        p.alignLeft();
        int total = 0;
        for (ProductovOrden x : o.getProductovOrdenList()) {
            if (x.getEnviadosacocina() < x.getCantidad()
                    && x.getProductoVenta().getCocinacodCocina().equals(c)) {
                if (x.getNota() != null) {
                    p.alignCenter();
                    p.emphasized(true);
                    p.setText(x.getNota().getDescripcion().replace('%', ' '));
                    p.newLine();
                    p.alignLeft();
                    p.setText("*NOTA* " + (x.getCantidad() - x.getEnviadosacocina()) + " " + x.getProductoVenta().getNombre());
                } else {
                    p.setText(x.getCantidad() - x.getEnviadosacocina() + " " + x.getProductoVenta().getNombre());
                }
                p.newLine();
                if (x.getNumeroComensal() != 0) {
                    p.alignCenter();
                    p.setText("N.C (" + x.getNumeroComensal() + ")");
                    p.newLine();
                }

                p.alignRight();
                total += (x.getCantidad() - x.getEnviadosacocina()) * x.getProductoVenta().getPrecioVenta();
                p.setText((x.getCantidad() - x.getEnviadosacocina()) * x.getProductoVenta().getPrecioVenta() + " " + MONEDA);
                p.newLine();
                p.alignLeft();
                x.setEnviadosacocina(x.getCantidad());
                ordenSinPlatos = false;
            }
        }

        p.addLineSeperator();
        p.newLine();
        p.setText(total + " " + MONEDA);
        p.newLine();
        p.alignCenter();
        p.newLine();
        p.setText(sync);
        p.newLine();
        p.feed((byte) 3);
        p.finit();

        if (!ordenSinPlatos) {
            for (int i = 0; i < cantidadCopias; i++) {
                RAM.add(new CopiaTicket(c.getNombreCocina(), p.finalCommandSet().getBytes()));
            }

            feedPrinter(p.finalCommandSet().getBytes(), c.getNombreCocina());

        } else {
            System.out.println("No existen platos de la cocina "
                    + c.getNombreCocina() + " de la orden" + o.getCodOrden() + " para imprimir");
        }

        return o;
    }

    public Orden printKitchenForced(Orden o) throws PrintException {

        Ticket t = new Ticket();
        boolean ordenSinPlatos = true;

        addHeader(t);

        addMetaData(t, o, new Date());

        ArrayList<String> entrantes = new ArrayList<>();
        entrantes.add("Entrantes Calientes");
        entrantes.add("Entrantes Frios");

        ArrayList<ProductovOrden> items = new ArrayList<>(o.getProductovOrdenList());
        items.sort((ProductovOrden o1, ProductovOrden o2) -> {
            ArrayList<String> entrantes1 = new ArrayList<>();
            entrantes1.add("Entrantes Calientes");
            entrantes1.add("Entrantes Frios");
            if (entrantes1.contains(o1.getProductoVenta().getSeccionnombreSeccion().getNombreSeccion())) {
                return -1;
            }
            if (entrantes1.contains(o2.getProductoVenta().getSeccionnombreSeccion().getNombreSeccion())) {
                return 1;
            }
            if (o1.getProductoVenta().getSeccionnombreSeccion().getNombreSeccion().matches("Postres")) {
                return 1;
            }
            if (o2.getProductoVenta().getSeccionnombreSeccion().getNombreSeccion().matches("Postres")) {
                return -1;
            }
            return 0;
        });

        t.alignLeft();

        boolean entrante = false;
        boolean postre = false;

        for (ProductovOrden x : items) {
            if (x.getEnviadosacocina() < x.getCantidad()) {
                if (!entrantes.contains(x.getProductoVenta().getSeccionnombreSeccion().getNombreSeccion()) && !entrante) {
                    t.addLineSeperator();
                    t.newLine();
                    entrante = true;
                }
                if (x.getProductoVenta().getSeccionnombreSeccion().getNombreSeccion().equals("Postres") && !postre) {
                    t.addLineSeperator();
                    t.newLine();
                    postre = true;
                }
                if (x.getNota() != null) {
                    t.alignCenter();
                    t.emphasized(true);
                    t.setText(x.getNota().getDescripcion().replace('%', ' '));
                    t.newLine();
                    t.alignLeft();
                    t.setText("*NOTA* " + (x.getCantidad() - x.getEnviadosacocina()) + " " + x.getProductoVenta().getNombre());
                } else {
                    t.setText(x.getCantidad() - x.getEnviadosacocina() + " " + x.getProductoVenta().getNombre());
                }
                t.newLine();
                t.alignRight();
                t.setText((x.getCantidad() - x.getEnviadosacocina()) * x.getProductoVenta().getPrecioVenta() + " " + MONEDA);
                t.newLine();
                t.alignLeft();
                x.setEnviadosacocina(x.getCantidad());

                ordenSinPlatos = false;
            }
        }

        t.addLineSeperator();
        t.alignCenter();
        t.newLine();
        t.feed((byte) 3);
        t.finit();

        if (!ordenSinPlatos) {
            feedPrinter(t.finalCommandSet().getBytes(), "Cocina");
        }
        cleanAndPrintRAM();

        return o;
    }

    /**
     * redondea por exceso las cuentas en moneda nacional a CUC
     *
     * @param valorARedondear el valor a redondear en entero (multiplicando el
     * float por 100)
     * @return un string con el valor a imprimir o usar
     */
    public static String redondeoDeMonedaMN_CUC(int valorARedondear) {
        int ref = valorARedondear % 5;

        if (ref != 0) {
            valorARedondear += 5 - ref;
        }
        float valorConvertido = (float) valorARedondear / 100;
        String ret = String.valueOf(valorConvertido);

        int decimales = 0;
        for (int i = 0; decimales == 0; i++) {
            if (ret.charAt(i) == 46) {
                decimales = ret.length() - 1 - i;
            }
        }
        if (decimales != 2) {
            ret += "0";
        }
        return ret;
    }

    public static String setDosLugaresDecimales(int valorARedondear) {

        int decimales = 0;

        float valorConvertido = (float) valorARedondear / 100;
        String ret = String.valueOf(valorConvertido);

        for (int i = 0; decimales == 0 && i < ret.length(); i++) {
            if (ret.charAt(i) == 46) {
                decimales = ret.length() - 1 - i;
            }
        }

        while (decimales != 2) {
            ret += "0";
            decimales++;
        }
        return ret;
    }

    private Orden printCancelationKitchenForced(Orden o) {

        ArrayList<String> entrantes = new ArrayList<>();
        entrantes.add("Entrantes Calientes");
        entrantes.add("Entrantes Frios");

        ArrayList<ProductovOrden> items = new ArrayList<>(o.getProductovOrdenList());
        items.sort((ProductovOrden o1, ProductovOrden o2) -> {
            ArrayList<String> entrantes1 = new ArrayList<>();
            entrantes1.add("Entrantes Calientes");
            entrantes1.add("Entrantes Frios");
            if (entrantes1.contains(o1.getProductoVenta().getSeccionnombreSeccion().getNombreSeccion())) {
                return -1;
            }
            if (entrantes1.contains(o2.getProductoVenta().getSeccionnombreSeccion().getNombreSeccion())) {
                return 1;
            }
            if (o1.getProductoVenta().getSeccionnombreSeccion().getNombreSeccion().matches("Postres")) {
                return 1;
            }
            if (o2.getProductoVenta().getSeccionnombreSeccion().getNombreSeccion().matches("Postres")) {
                return -1;
            }
            return 0;
        });

        Ticket t = new Ticket();
        boolean ordenSinPlatos = true;

        addHeader(t);

        addMetaData(t, o, new Date());

        addFocusedMessage(t, CANCELACION);

        t.alignLeft();

        boolean entrante = false;
        boolean postre = false;

        for (ProductovOrden x : items) {

            if (x.getCantidad() < x.getEnviadosacocina()) {
                if (!entrantes.contains(x.getProductoVenta().getSeccionnombreSeccion().getNombreSeccion()) && !entrante) {
                    t.addLineSeperator();
                    t.newLine();
                    entrante = true;
                }
                if (x.getProductoVenta().getSeccionnombreSeccion().getNombreSeccion().equals("Postres") && !postre) {
                    t.addLineSeperator();
                    t.newLine();
                    postre = true;
                }
                if (x.getNota() != null) {
                    t.alignCenter();
                    t.emphasized(true);
                    t.setText(x.getNota().getDescripcion().replace('%', ' '));
                    t.newLine();
                    t.alignLeft();
                    t.setText("*NOTA* " + (x.getCantidad() - x.getEnviadosacocina()) + " " + x.getProductoVenta().getNombre());
                } else {
                    t.setText(x.getCantidad() - x.getEnviadosacocina() + " " + x.getProductoVenta().getNombre());
                }
                t.newLine();
                t.alignRight();
                t.setText((x.getCantidad() - x.getEnviadosacocina()) * x.getProductoVenta().getPrecioVenta() + " " + MONEDA);
                t.newLine();
                t.alignLeft();
                x.setEnviadosacocina(x.getCantidad());
                ordenSinPlatos = false;
                x.setEnviadosacocina(x.getCantidad());
            }
        }

        addFocusedMessage(t, "");
        t.addLineSeperator();
        t.alignCenter();
        t.newLine();
        t.feed((byte) 3);
        t.finit();

        if (!ordenSinPlatos) {
            feedPrinter(t.finalCommandSet().getBytes(), DEFAULT_KITCHEN_PRINTER_LOCATION);
        }
        cleanAndPrintRAM();

        return o;
    }

    public Orden printCancelationTicket(Orden o) {

        return printCancelationKitchenForced(printCancelationKitchen(o,"Cocina"));
//        Ticket t = new Ticket();
//
//        addHeader(t);
//
//        addMetaData(t, o, new Date());
//
//        List<Cocina> cocinasExistentesEnLaOrden = new ArrayList<>();
//        for (ProductovOrden x : o.getProductovOrdenList()) {
//            if (!cocinasExistentesEnLaOrden.contains(x.getProductoVenta().getCocinacodCocina())) {
//                cocinasExistentesEnLaOrden.add(x.getProductoVenta().getCocinacodCocina());
//            }
//        }
//        if (cocinasExistentesEnLaOrden.size() > 1) {
//            for (int i = 0; i < cocinasExistentesEnLaOrden.size(); i++) {
//                String sync = SYNC;
//                for (int j = 0; j < cocinasExistentesEnLaOrden.size(); j++) {
//                    if (i == j) {
//                        continue;
//                    }
//                    sync += cocinasExistentesEnLaOrden.get(j).getNombreCocina() + " ";
//                }
//                printKitchen(o, cocinasExistentesEnLaOrden.get(i), sync);
//            }
//        } else {
//            if (cocinasExistentesEnLaOrden.size() > 0) {
//                printKitchen(o, cocinasExistentesEnLaOrden.get(0), "");
//            }
//
//        }
//
//        cleanAndPrintRAM();
//
//        return o;
//    }
//
//    public Orden printKitchenForced(Orden o) throws PrintException {
//
//        Ticket t = new Ticket();
//        boolean ordenSinPlatos = true;
//
//        addHeader(t);
//
//        addMetaData(t, o, new Date());
//
//        ArrayList<String> entrantes = new ArrayList<>();
//        entrantes.add("Entrantes Calientes");
//        entrantes.add("Entrantes Frios");
//
//        ArrayList<ProductovOrden> items = new ArrayList<>(o.getProductovOrdenList());
//        items.sort((ProductovOrden o1, ProductovOrden o2) -> {
//            ArrayList<String> entrantes1 = new ArrayList<>();
//            entrantes1.add("Entrantes Calientes");
//            entrantes1.add("Entrantes Frios");
//            if (entrantes1.contains(o1.getProductoVenta().getSeccionnombreSeccion().getNombreSeccion())) {
//                return -1;
//            }
//            if (entrantes1.contains(o2.getProductoVenta().getSeccionnombreSeccion().getNombreSeccion())) {
//                return 1;
//            }
//            if (o1.getProductoVenta().getSeccionnombreSeccion().getNombreSeccion().matches("Postres")) {
//                return 1;
//            }
//            if (o2.getProductoVenta().getSeccionnombreSeccion().getNombreSeccion().matches("Postres")) {
//                return -1;
//            }
//            return 0;
//        });
//
//        t.alignLeft();
//
//        boolean entrante = false;
//        boolean postre = false;
//
//        for (ProductovOrden x : items) {
//            if (x.getEnviadosacocina() < x.getCantidad()) {
//                if (!entrantes.contains(x.getProductoVenta().getSeccionnombreSeccion().getNombreSeccion()) && !entrante) {
//                    t.addLineSeperator();
//                    t.newLine();
//                    entrante = true;
//                }
//                if (x.getProductoVenta().getSeccionnombreSeccion().getNombreSeccion().equals("Postres") && !postre) {
//                    t.addLineSeperator();
//                    t.newLine();
//                    postre = true;
//                }
//                if (x.getNota() != null) {
//                    t.alignCenter();
//                    t.emphasized(true);
//                    t.setText(x.getNota().getDescripcion().replace('%', ' '));
//                    t.newLine();
//                    t.alignLeft();
//                    t.setText("*NOTA* " + (x.getCantidad() - x.getEnviadosacocina()) + " " + x.getProductoVenta().getNombre());
//                } else {
//                    t.setText(x.getCantidad() - x.getEnviadosacocina() + " " + x.getProductoVenta().getNombre());
//                }
//                t.newLine();
//                t.alignRight();
//                t.setText((x.getCantidad() - x.getEnviadosacocina()) * x.getProductoVenta().getPrecioVenta() + " " + MONEDA);
//                t.newLine();
//                t.alignLeft();
//                x.setEnviadosacocina(x.getCantidad());
//                try {
//                    staticContent.productovOrdenJpa.edit(x);
//                } catch (Exception ex) {
//                    Logger.getLogger(Impresion.class.getName()).log(Level.SEVERE, null, ex);
//                }
//                ordenSinPlatos = false;
//            }
//        }
//
//        t.addLineSeperator();
//        t.alignCenter();
//        t.newLine();
//        t.feed((byte) 3);
//        t.finit();
//
//        if (!ordenSinPlatos) {
//            feedPrinter(t.finalCommandSet().getBytes(), DEFAULT_KITCHEN_PRINTER_LOCATION);
//        }
//        cleanAndPrintRAM();
//
//        return o;
    }

    private void addFocusedMessage(Ticket t, String sms) {
        t.addLineSeperator();
        t.newLine();
        t.addLineSeperator();
        t.newLine();
        t.alignCenter();
        t.setText(sms);
        t.newLine();
        t.addLineSeperator();
        t.newLine();
    }

    /**
     *
     * @param o the value of o
     * @param c the value of c
     */
    public Orden printCancelationKitchen(Orden o, String c) {
        boolean ordenSinPlatos = true;

        Ticket t = new Ticket();

        addHeader(t);

        t.emphasized(true);
        t.setText(COCINA + c);
        t.emphasized(false);
        t.newLine();

        addMetaData(t, o, new Date());

        addFocusedMessage(t, CANCELACION);

        t.alignLeft();

        for (ProductovOrden x : o.getProductovOrdenList()) {

            if (x.getEnviadosacocina() > x.getCantidad()
                    && x.getProductoVenta().getNombre().equals(c)) {
                t.setText(x.getCantidad() - x.getEnviadosacocina() + " " + x.getProductoVenta().getNombre());
                t.newLine();
                t.alignRight();
                t.setText((x.getCantidad() - x.getEnviadosacocina()) * x.getProductoVenta().getPrecioVenta() + " " + MONEDA);
                t.newLine();
                t.alignLeft();

                ordenSinPlatos = false;
                x.setEnviadosacocina(x.getCantidad());
            }
        }

        addFocusedMessage(t, "");

        t.feed((byte) 3);
        t.finit();

        if (!ordenSinPlatos) {
            for (int i = 0; i < cantidadCopias; i++) {
                RAM.add(new CopiaTicket(c, t.finalCommandSet().getBytes()));
            }

            feedPrinter(t.finalCommandSet().getBytes(), c);

        } else {
            System.out.println("No existen platos de la cocina "
                    + c + " de la orden " + o.getCodOrden() + " para cancelar");
            t.resetAll();
        }

        return o;
    }

    public void printMenuInfantil(Orden o, String entrante, String platoFuerte,
            String postre, String liquido, String nota) {

        Ticket p = new Ticket();
        p.resetAll();
        p.initialize();
        //p.feedBack((byte)2);
        p.alignCenter();
        p.setText(this.nombreRest);
        p.newLine();
        p.emphasized(true);
        p.setText(COCINA + "Cocina");
        p.emphasized(false);
        p.newLine();
        p.addLineSeperator();
        p.newLine();
        p.alignRight();
        p.setText(FECHA + this.DATE_FORMAT.format(o.getVentafecha().getFecha()) + TIME_FORMAT.format(o.getHoraComenzada()));
        p.newLine();
        p.setText(ORDEN + o.getCodOrden());
        p.newLine();
        p.setText(MESA + o.getMesacodMesa().getCodMesa());
        p.newLine();
        p.setText(o.getPersonalusuario().getDatosPersonales().getNombre());
        p.newLine();
        //p.doubleHeight(true);
        p.addLineSeperator();
        p.newLine();
        p.alignLeft();
        int total = 0;
        if (!nota.isEmpty()) {
            p.alignCenter();
            p.emphasized(true);
            p.setText(nota.replace('%', ' '));
            p.newLine();
            p.alignLeft();
            p.setText("*NOTA*  Menu Infantil  *NOTA*");
        } else {
            p.setText("Menu Infantil");
        }
        p.newLine();
        p.newLine();

        p.setText("Entrante: ");
        p.newLine();
        p.alignRight();
        p.setText(entrante);

        p.newLine();

        p.alignLeft();
        p.setText("Plato Principal: ");
        p.newLine();
        p.alignRight();
        p.setText(platoFuerte);

        p.newLine();

        p.alignLeft();
        p.setText("Postre: ");
        p.newLine();
        p.alignRight();
        p.setText(postre);

        p.newLine();

        p.alignLeft();
        p.setText("Liquido: ");
        p.newLine();
        p.alignRight();
        p.setText(liquido);

        p.newLine();
        p.addLineSeperator();
        p.newLine();
        p.newLine();

        p.feed((byte) 3);
        p.finit();

   
            feedPrinter(p.finalCommandSet().getBytes(), DEFAULT_KITCHEN_PRINTER_LOCATION);
            feedPrinter(p.finalCommandSet().getBytes(), DEFAULT_KITCHEN_PRINTER_LOCATION);
        

    }

    private void cleanAndPrintRAM() {
        while (!RAM.isEmpty()) {
                feedPrinter(RAM.get(0).getImpresionData(), RAM.get(0).getNombreImpresora());
                RAM.remove(0);
        }
    }

    private void addHeader(Ticket t) {
        t.resetAll();
        t.initialize();
        //p.feedBack((byte)2);
        t.alignCenter();
        t.setText(CABECERA);
        t.newLine();
        t.setText(this.nombreRest);
        t.newLine();
    }

    private void addMetaData(Ticket t, Orden o, Date date) {
        t.addLineSeperator();
        t.newLine();
        t.alignRight();
        t.setText(FECHA + this.DATE_FORMAT.format(o.getVentafecha().getFecha()) + TIME_FORMAT.format(date));
        t.newLine();
        t.setText(ORDEN + o.getCodOrden());
        t.newLine();
        t.setText(MESA + o.getMesacodMesa().getCodMesa());
        t.newLine();
        t.alignLeft();
        t.setText(CAMARERO);
        t.newLine();
        t.alignRight();
        t.setText(o.getPersonalusuario().getDatosPersonales().getNombre());
        t.newLine();
        t.addLineSeperator();
        t.newLine();

    }

    private class CopiaTicket {

        private final String nombreImpresora;
        private final byte[] impresionData;

        public CopiaTicket(String nombreImpresora, byte[] impresionData) {
            this.nombreImpresora = nombreImpresora;
            this.impresionData = impresionData;
        }

        public String getNombreImpresora() {
            return nombreImpresora;
        }

        public byte[] getImpresionData() {
            return impresionData;
        }

    }

}
