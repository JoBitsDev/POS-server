package com.restmanager.XMLservice;

import com.restmanager.Mesa;
import com.restmanager.Orden;
import com.restmanager.Venta;
import java.util.ArrayList;

/**
 * FirstDream
 * @author Jorge
 * 
 */
public class OrdenXMLExport {
    
    private static final Tag
            ENTITY =Tag.getInstance("orden"),
            ENTITIES =Tag.getInstance("ordens"),
            CODORDEN = Tag.getInstance("codOrden"),
            DELACASA = Tag.getInstance("deLaCasa"),
            TAGMESA = Tag.getInstance("mesacodMesa"),
            TAGPERSONAL = Tag.getInstance("personalusuario"),
            TAGVENTA = Tag.getInstance("ventafecha");
    
    private static final Tag
            MESA_ENTITY = Tag.getInstance("mesa"),
            MESA_ENTITIES = Tag.getInstance("mesas"),
            CAPACIDAD_MAX = Tag.getInstance("capacidadMax"),
            COD_MESA = Tag.getInstance("codMesa"),
            ESTADO = Tag.getInstance("estado");
    
    
    public static String exportSingleEntity (Orden o){
        String ret = "";
        ret += ENTITY.getStartTag();
        
        ret += CODORDEN.getStartTag();
        ret += o.getCodOrden();
        ret += CODORDEN.getEndTag();
        
        ret += DELACASA.getStartTag();
        ret += o.getDeLaCasa();
        ret += DELACASA.getEndTag();
        
        ret += exportSingleEntity(o.getMesacodMesa());
        //ret += exportSingleEntity(o.getVentafecha());
         
        ret += ENTITY.getEndTag();
        
        return ret;
    }
    
     public static String exportEntities (ArrayList<Orden> ordenes){
        String ret = "";
        ret += ENTITIES.getStartTag();
        
        for (Orden x : ordenes) {
           ret += exportSingleEntity(x);
        }
        
        ret += ENTITIES.getEndTag();
                
        return ret;
    }
    
    private static String exportSingleEntity(Mesa m){
        String ret = "";
        ret += MESA_ENTITY.getStartTag();
        
        ret += COD_MESA.getStartTag();
        ret += m.getCodMesa();
        ret += COD_MESA.getEndTag();
        
        ret += ESTADO.getStartTag();
        ret += m.getEstado();
        ret += ESTADO.getEndTag();
        
        ret += CAPACIDAD_MAX.getStartTag();
        ret += m.getCapacidadMax();
        ret += CAPACIDAD_MAX.getEndTag();
        
        ret += MESA_ENTITY.getEndTag();
        
        return ret;
    }
    
    private static String exportSingleEntity(Venta s){
        throw new UnsupportedOperationException("Not implemented");
        
    }

}
