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
public class NotaPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 7)
    @Column(name = "productov_ordenproducto_ventap_cod")
    private String productovOrdenproductoVentapCod;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 11)
    @Column(name = "productov_ordenordencod_orden")
    private String productovOrdenordencodOrden;

    public NotaPK() {
    }

    public NotaPK(String productovOrdenproductoVentapCod, String productovOrdenordencodOrden) {
        this.productovOrdenproductoVentapCod = productovOrdenproductoVentapCod;
        this.productovOrdenordencodOrden = productovOrdenordencodOrden;
    }

    public String getProductovOrdenproductoVentapCod() {
        return productovOrdenproductoVentapCod;
    }

    public void setProductovOrdenproductoVentapCod(String productovOrdenproductoVentapCod) {
        this.productovOrdenproductoVentapCod = productovOrdenproductoVentapCod;
    }

    public String getProductovOrdenordencodOrden() {
        return productovOrdenordencodOrden;
    }

    public void setProductovOrdenordencodOrden(String productovOrdenordencodOrden) {
        this.productovOrdenordencodOrden = productovOrdenordencodOrden;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (productovOrdenproductoVentapCod != null ? productovOrdenproductoVentapCod.hashCode() : 0);
        hash += (productovOrdenordencodOrden != null ? productovOrdenordencodOrden.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof NotaPK)) {
            return false;
        }
        NotaPK other = (NotaPK) object;
        if ((this.productovOrdenproductoVentapCod == null && other.productovOrdenproductoVentapCod != null) || (this.productovOrdenproductoVentapCod != null && !this.productovOrdenproductoVentapCod.equals(other.productovOrdenproductoVentapCod))) {
            return false;
        }
        if ((this.productovOrdenordencodOrden == null && other.productovOrdenordencodOrden != null) || (this.productovOrdenordencodOrden != null && !this.productovOrdenordencodOrden.equals(other.productovOrdenordencodOrden))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.restmanager.NotaPK[ productovOrdenproductoVentapCod=" + productovOrdenproductoVentapCod + ", productovOrdenordencodOrden=" + productovOrdenordencodOrden + " ]";
    }

}
