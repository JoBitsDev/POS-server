/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.restmanager.XMLservice;

import com.restmanager.Cocina;
import com.restmanager.ProductoVenta;
import com.restmanager.Seccion;

/**
 * FirstDream
 * @author Jorge
 * 
 */
public class ProductoVentaXMLExport {
    
     private static final String VERSION_ENCODING = 
            "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>";

    private static final Tag 
            TAGENTITI = Tag.getInstance("productoVenta"),
            TAGENTITIES = Tag.getInstance("productoVentas"),
            TAGPCOD = Tag.getInstance("PCod"),
            TAGNOMBRE = Tag.getInstance("nombre"),
            TAGCOCINA = Tag.getInstance("cocinacodCocina"),
            TAGPRECIOVENTA = Tag.getInstance("precioVenta"),
            TAGNOMBRECOCINA = Tag.getInstance("nombreCocina"),
            TAGSECCION = Tag.getInstance("seccionnombreSeccion"),
            TAGNOMBRESECCION = Tag.getInstance("nombreSeccion");
    
    
   public static String exportSingleEntity(ProductoVenta p){
       String ret = "";
       ret += TAGENTITI.getStartTag();
       
       ret += TAGPCOD.getStartTag();
       ret += p.getPCod();
       ret += TAGPCOD.getEndTag();
       
       ret += TAGNOMBRE.getStartTag();
       ret += p.getNombre();
       ret += TAGNOMBRE.getEndTag();
       
       ret += TAGPRECIOVENTA.getStartTag();
       ret += p.getPrecioVenta();
       ret += TAGPRECIOVENTA.getEndTag();
       
       ret += exportSingleEntity(p.getSeccionnombreSeccion());
       ret += exportSingleEntity(p.getCocinacodCocina());
       
       ret += TAGENTITI.getEndTag();
       
       return ret;
        
   }
    
   
    private static String exportSingleEntity (Seccion s){
           String ret = "";
           ret += TAGSECCION.getStartTag();
           
           ret += TAGNOMBRESECCION.getStartTag();
           ret += s.getNombreSeccion();
           ret += TAGNOMBRESECCION.getEndTag();
           
           ret += TAGSECCION.getEndTag();
           
           return ret;
       }
   
    private static String exportSingleEntity (Cocina c){
           String ret = "";
           ret += TAGCOCINA.getStartTag();
           
           ret += TAGNOMBRECOCINA.getStartTag();
           ret += c.getNombreCocina();
           ret += TAGNOMBRECOCINA.getEndTag();
           
           ret += TAGCOCINA.getEndTag();
           
           return ret;
       }
  

}


