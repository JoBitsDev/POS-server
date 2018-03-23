/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.restmanager.printservice;

import com.restmanager.Carta;
import com.restmanager.Cocina;
import com.restmanager.Orden;
import com.restmanager.ProductovOrden;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
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
    private String nombreRest = null;
    private boolean monedaCUC = false;
    private float cambio = 24;
    private String 
            CABECERA = "Restaurante",
            COCINA = "Cocina: ",
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
            SYNC = "Sale con: ";
            
    
    SimpleDateFormat Format = new SimpleDateFormat("dd'/'MM'/'yy ' ' ");
    SimpleDateFormat TimaFormat = new SimpleDateFormat(" hh ':' mm ' ' a ");
    
   
 
    
    private static void feedPrinter(byte[] b) throws PrintException {
        
        //DocPrintJob job = PrintServiceLookup.lookupPrintServices(null, attrSet)[0].createPrintJob();       
        DocPrintJob job = PrintServiceLookup.lookupDefaultPrintService().createPrintJob();  
        DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
        Doc doc = new SimpleDoc(b, flavor, null);

        job.print(doc, null);
        
        
    
   
}

    public Impresion(Carta m) {
        this.nombreRest = m.getNombreCarta();
        
    }

    public Impresion(Carta m , boolean monedaCUC, float cambio) {
        nombreRest = m.getNombreCarta();
        this.cambio = cambio;
        if(this.monedaCUC = monedaCUC)
            MONEDA = CUC;
        else
            MONEDA = MN;
        
    }
    
    public Impresion(Carta m, String footer) {
        this.nombreRest = m.getNombreCarta();
        PIE = footer;
    }

    public Impresion(Carta m, String footer, boolean monedaCUC, float cambio) {
        this.nombreRest = m.getNombreCarta();
        this.cambio = cambio;
        PIE = footer;
        if(this.monedaCUC = monedaCUC)
            MONEDA = CUC;
        else
            MONEDA = MN;
    }

    public void print(Orden o) throws PrintException {

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
        if(o.getDeLaCasa()){
            p.doubleStrik(true);
            p.setText(DELACASA);
            p.doubleStrik(false);
            p.newLine();
        }
        p.addLineSeperator();
        p.newLine();
        p.alignRight();
        p.setText(FECHA + this.Format.format(o.getVentafecha().getFecha()) + TimaFormat.format(o.getHoraComenzada()));
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
            p.setText(x.getCantidad()*x.getProductoVenta().getPrecioVenta()+ MONEDA);
            p.newLine();
            total+=x.getCantidad()*x.getProductoVenta().getPrecioVenta();
        }
        
        p.alignRight();
        p.newLine();
        p.setText(SUBTOTAL + total + MONEDA);
        p.newLine();
        p.addLineSeperator();
        
        p.setText(TOTAL + total + MONEDA);
        p.newLine();
        
        if(monedaCUC){
        p.setText(TOTAL + total*cambio + MN); 
        }
        else{
        p.setText(TOTAL + Math.rint((total/cambio)*100)/100 + CUC);
        }
        
        p.newLine();
        p.newLine();
        
        p.alignCenter();
        p.setText(this.PIE);
        p.newLine();
        p.feed((byte)3);
        p.finit();
        
     
            feedPrinter(p.finalCommandSet().getBytes());
      
    }

    public Orden printKitchen(Orden o) throws PrintException {
        
        
        
        Ticket p = new Ticket();
        p.resetAll();
        p.initialize();
        //p.feedBack((byte)2);
        p.alignCenter();
        p.setText(this.nombreRest);
        p.newLine();
        p.addLineSeperator();
        p.newLine();
        p.alignRight();
        p.setText(FECHA + this.Format.format(o.getVentafecha().getFecha()) + 
                TimaFormat.format(new Date()));
        p.newLine();
        p.setText(ORDEN + o.getCodOrden());
        p.newLine();
        p.setText(MESA + o.getMesacodMesa().getCodMesa());
        p.newLine();
        p.addLineSeperator();
        p.newLine();
        p.alignLeft();
        
        List<Cocina> cocinasExistentesEnLaOrden = new ArrayList<>();
        for (ProductovOrden x : o.getProductovOrdenList()) {
            if(!cocinasExistentesEnLaOrden.contains(x.getProductoVenta().getCocinacodCocina()) && 
                    !x.getProductoVenta().getCocinacodCocina().getNombreCocina().equals("Barra") &&
                    x.getEnviadosacocina()<x.getCantidad()){
                cocinasExistentesEnLaOrden.add(x.getProductoVenta().getCocinacodCocina());
            }
        }
        if(cocinasExistentesEnLaOrden.size()>1){
           for (int i = 0; i < cocinasExistentesEnLaOrden.size(); i++) {
            String sync = SYNC;
            for (int j = 0; j < cocinasExistentesEnLaOrden.size(); j++) {
                if(i == j){
                    continue;
                }
                sync += cocinasExistentesEnLaOrden.get(j).getNombreCocina()+" ";
            }
               printKitchen(o, cocinasExistentesEnLaOrden.get(i), sync);
        } 
        }else{
            if(cocinasExistentesEnLaOrden.size() > 0){
                printKitchen(o, cocinasExistentesEnLaOrden.get(0), p.newLine());
            }
            
        }
        
   
      
    
    return o;
    }
    
    
    /**
     * imprime una orden por la impresora predeterminada
     * @param o la orden que se va a imprimir
     * @param c la cocina hacia donde se va a imprimir
     * @param sync es string de sincronizacion. ej: si los productos 
     * van a salir con los de otra cocina
     * @return
     * @throws PrintException 
     */
    public Orden printKitchen(Orden o,Cocina c,String sync) throws PrintException {
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
        p.setText(FECHA + this.Format.format(o.getVentafecha().getFecha()) + TimaFormat.format(o.getHoraComenzada()));
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
            if(x.getEnviadosacocina()<x.getCantidad() &&
              !x.getProductoVenta().getCocinacodCocina().getNombreCocina().equals("Barra") && 
                    x.getProductoVenta().getCocinacodCocina().equals(c)){
            p.setText(x.getCantidad()-x.getEnviadosacocina() + " " + x.getProductoVenta().getNombre());
            p.newLine();
            
            p.alignRight();
            total += (x.getCantidad()-x.getEnviadosacocina())*x.getProductoVenta().getPrecioVenta();
            p.setText((x.getCantidad()-x.getEnviadosacocina())*x.getProductoVenta().getPrecioVenta() + " " + MONEDA);
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
        p.feed((byte)3);
        p.finit();
        
            if(!ordenSinPlatos){
            feedPrinter(p.finalCommandSet().getBytes());
            }
            else{
                System.out.println("No existen platos de la cocina "
                        +c.getNombreCocina()+" de la orden"+ o.getCodOrden()+" para imprimir");
            }
    
    return o;
    }

    

    
    
    
   
   
}
    
    


    
    

