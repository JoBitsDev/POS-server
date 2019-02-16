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
public class NotificacionEnvioCocinaPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 5)
    @Column(name = "cocinacod_cocina")
    private String cocinacodCocina;
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

    public NotificacionEnvioCocinaPK() {
    }

    public NotificacionEnvioCocinaPK(String cocinacodCocina, String productovOrdenproductoVentapCod, String productovOrdenordencodOrden) {
        this.cocinacodCocina = cocinacodCocina;
        this.productovOrdenproductoVentapCod = productovOrdenproductoVentapCod;
        this.productovOrdenordencodOrden = productovOrdenordencodOrden;
    }

    public String getCocinacodCocina() {
        return cocinacodCocina;
    }

    public void setCocinacodCocina(String cocinacodCocina) {
        this.cocinacodCocina = cocinacodCocina;
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
        hash += (cocinacodCocina != null ? cocinacodCocina.hashCode() : 0);
        hash += (productovOrdenproductoVentapCod != null ? productovOrdenproductoVentapCod.hashCode() : 0);
        hash += (productovOrdenordencodOrden != null ? productovOrdenordencodOrden.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof NotificacionEnvioCocinaPK)) {
            return false;
        }
        NotificacionEnvioCocinaPK other = (NotificacionEnvioCocinaPK) object;
        if ((this.cocinacodCocina == null && other.cocinacodCocina != null) || (this.cocinacodCocina != null && !this.cocinacodCocina.equals(other.cocinacodCocina))) {
            return false;
        }
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
        return "com.restmanager.NotificacionEnvioCocinaPK[ cocinacodCocina=" + cocinacodCocina + ", productovOrdenproductoVentapCod=" + productovOrdenproductoVentapCod + ", productovOrdenordencodOrden=" + productovOrdenordencodOrden + " ]";
    }

}
