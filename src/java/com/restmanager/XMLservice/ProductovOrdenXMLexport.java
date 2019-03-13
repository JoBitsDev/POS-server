/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.restmanager.XMLservice;

import com.restmanager.ProductovOrden;
import java.util.List;

/**
 * FirstDream
 *
 * @author Jorge
 *
 */
public class ProductovOrdenXMLexport extends abstractXMLExport{



    private static final Tag 
            TAGENTITI = Tag.getInstance("productovOrden"),
            TAGENTITIES = Tag.getInstance("productovOrdens"),
            TAGCANTIDAD = Tag.getInstance("cantidad"),
            TAGENVIADOSACOCINA = Tag.getInstance("enviadosacocina"),
            TAGPRODUCTOVENTA = Tag.getInstance("productoVenta"),
            TAGNUMEROCOMENSAL = Tag.getInstance("numeroComensal"),
            TAGNOTA = Tag.getInstance("nota"),
            TAGORDEN = Tag.getInstance("orden");

    public static String exportToXML(List<ProductovOrden> po) {
        String ret = "";
        ret += VERSION_ENCODING;
        ret += TAGENTITIES.getStartTag();
        for (ProductovOrden x : po) {
            ret += exportSingleEntity(x);
        }
        ret += TAGENTITIES.getEndTag();

        return ret;
    }

    public static String exportSingleEntity(ProductovOrden p) {
        String ret = "";
        ret += TAGENTITI.getStartTag();

        ret += TAGCANTIDAD.getStartTag();
        ret += p.getCantidad();
        ret += TAGCANTIDAD.getEndTag();

        ret += TAGENVIADOSACOCINA.getStartTag();
        ret += p.getEnviadosacocina();
        ret += TAGENVIADOSACOCINA.getEndTag();
        
        ret += TAGNUMEROCOMENSAL.getStartTag();
        ret += p.getNumeroComensal();
        ret += TAGNUMEROCOMENSAL.getEndTag();
        
        if(p.getNota() != null){
            ret += TAGNOTA.getStartTag();
            ret += p.getNota().getDescripcion();
            ret += TAGNOTA.getEndTag();
        }
        
        ret += ProductoVentaXMLExport.exportSingleEntity(p.getProductoVenta());
        ret += OrdenXMLExport.exportSingleEntity(p.getOrden());

        ret += TAGENTITI.getEndTag();

        return ret;

    }

}
