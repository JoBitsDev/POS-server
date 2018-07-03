/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.restmanager;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * FirstDream
 * @author Jorge
 * 
 */
@Embeddable
public class AgregoPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "cod_agrego")
    private int codAgrego;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 7)
    @Column(name = "producto_ventap_cod")
    private String productoVentapCod;

    public AgregoPK() {
    }

    public AgregoPK(int codAgrego, String productoVentapCod) {
        this.codAgrego = codAgrego;
        this.productoVentapCod = productoVentapCod;
    }

    public int getCodAgrego() {
        return codAgrego;
    }

    public void setCodAgrego(int codAgrego) {
        this.codAgrego = codAgrego;
    }

    public String getProductoVentapCod() {
        return productoVentapCod;
    }

    public void setProductoVentapCod(String productoVentapCod) {
        this.productoVentapCod = productoVentapCod;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) codAgrego;
        hash += (productoVentapCod != null ? productoVentapCod.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AgregoPK)) {
            return false;
        }
        AgregoPK other = (AgregoPK) object;
        if (this.codAgrego != other.codAgrego) {
            return false;
        }
        if ((this.productoVentapCod == null && other.productoVentapCod != null) || (this.productoVentapCod != null && !this.productoVentapCod.equals(other.productoVentapCod))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.restmanager.AgregoPK[ codAgrego=" + codAgrego + ", productoVentapCod=" + productoVentapCod + " ]";
    }

}
