/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.restmanager.XMLservice;

/**
 * Clase para representar una etiqueta en un archivo XML. 
 * la clase es inmutable (se puede usar el == para comparar)
 * 
 * FirstDream
 * @author Jorge
 * 
 */
public class Tag {
    
    private static final String 
            /**
             * Angular con que abre el inicio de una etiqueta
             */
            S_ANGULAR = "<",
            /**
             * Angular con que terminan se abren las etiquetas que terminan
             */
            F_ANGULAR = "</",
            /**
             * Angular con que cierran todas las etiquetas
             */
             CLOSE_ANGULAR = ">";
                  
            /**
             * Nombre de la etiqueta
             */
    private final String tagName,
            /**
             * etiqueta de entrada
             */
            startTag,
            /**
             * etiqueta de salida
             */
            endTag;

    private Tag(String tagName) {
        this.tagName = tagName;
        startTag =S_ANGULAR+tagName+CLOSE_ANGULAR;
        endTag = F_ANGULAR+tagName+CLOSE_ANGULAR;
    }
    
    /**
     * Constructor 
     * @param name - nombre de la etiqueta. 
     * el nombre debe empezar con minuscula, no puede ser null ni vacio 
     * en caso de empezar con mayúscula 
     * se pasa la primera letra a minúscula automaticamente.
     * @return Tag - devuelve una instancia de la clase Tag
     */
    public static Tag getInstance(String name){
        if(name == null || name.isEmpty()){
            throw new NullPointerException("La cadena name esta vacia");
        }
        if(Character.isHighSurrogate(name.charAt(0))){
           name = name.replaceFirst(""+name.charAt(0),""+
                   Character.toLowerCase(name.charAt(0)));
        }
        return new Tag(name);
    }
    /**
     * 
     * @param regex - el string para chequear si es un tag
     * @return true si y solo si regex es el tag que se quiere
     */
    public boolean isStartTag(String regex){
        return startTag.equals(regex);
    }
    
    /**
     * 
     * @param regex - el String a chequear
     * @return devuelve true si y solo si la cadena empieza con el angular izquierdo
     * y termina con el angular derecho
     */
    public static boolean isTag(String regex){
        return(S_ANGULAR.equals(regex.charAt(0)))&&
            (F_ANGULAR.equals(regex.charAt(regex.length()-1)));
     
    }

    public String getTagName() {
        return tagName;
    }

    public String getStartTag() {
        return startTag;
    }

    public String getEndTag() {
        return endTag;
    }
    
    
    
    
    
            
    
           
   

}
