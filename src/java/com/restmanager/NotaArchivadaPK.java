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
public class NotaArchivadaPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 7)
    @Column(name = "productov_orden_archivadoproducto_ventap_cod")
    private String productovOrdenArchivadoproductoVentapCod;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 11)
    @Column(name = "productov_orden_archivadoordencod_orden")
    private String productovOrdenArchivadoordencodOrden;

    public NotaArchivadaPK() {
    }

    public NotaArchivadaPK(String productovOrdenArchivadoproductoVentapCod, String productovOrdenArchivadoordencodOrden) {
        this.productovOrdenArchivadoproductoVentapCod = productovOrdenArchivadoproductoVentapCod;
        this.productovOrdenArchivadoordencodOrden = productovOrdenArchivadoordencodOrden;
    }

    public String getProductovOrdenArchivadoproductoVentapCod() {
        return productovOrdenArchivadoproductoVentapCod;
    }

    public void setProductovOrdenArchivadoproductoVentapCod(String productovOrdenArchivadoproductoVentapCod) {
        this.productovOrdenArchivadoproductoVentapCod = productovOrdenArchivadoproductoVentapCod;
    }

    public String getProductovOrdenArchivadoordencodOrden() {
        return productovOrdenArchivadoordencodOrden;
    }

    public void setProductovOrdenArchivadoordencodOrden(String productovOrdenArchivadoordencodOrden) {
        this.productovOrdenArchivadoordencodOrden = productovOrdenArchivadoordencodOrden;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (productovOrdenArchivadoproductoVentapCod != null ? productovOrdenArchivadoproductoVentapCod.hashCode() : 0);
        hash += (productovOrdenArchivadoordencodOrden != null ? productovOrdenArchivadoordencodOrden.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof NotaArchivadaPK)) {
            return false;
        }
        NotaArchivadaPK other = (NotaArchivadaPK) object;
        if ((this.productovOrdenArchivadoproductoVentapCod == null && other.productovOrdenArchivadoproductoVentapCod != null) || (this.productovOrdenArchivadoproductoVentapCod != null && !this.productovOrdenArchivadoproductoVentapCod.equals(other.productovOrdenArchivadoproductoVentapCod))) {
            return false;
        }
        if ((this.productovOrdenArchivadoordencodOrden == null && other.productovOrdenArchivadoordencodOrden != null) || (this.productovOrdenArchivadoordencodOrden != null && !this.productovOrdenArchivadoordencodOrden.equals(other.productovOrdenArchivadoordencodOrden))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.restmanager.NotaArchivadaPK[ productovOrdenArchivadoproductoVentapCod=" + productovOrdenArchivadoproductoVentapCod + ", productovOrdenArchivadoordencodOrden=" + productovOrdenArchivadoordencodOrden + " ]";
    }

}
