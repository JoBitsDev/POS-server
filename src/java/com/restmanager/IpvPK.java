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
public class IpvPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 6)
    @Column(name = "insumocod_insumo")
    private String insumocodInsumo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 5)
    @Column(name = "cocinacod_cocina")
    private String cocinacodCocina;

    public IpvPK() {
    }

    public IpvPK(String insumocodInsumo, String cocinacodCocina) {
        this.insumocodInsumo = insumocodInsumo;
        this.cocinacodCocina = cocinacodCocina;
    }

    public String getInsumocodInsumo() {
        return insumocodInsumo;
    }

    public void setInsumocodInsumo(String insumocodInsumo) {
        this.insumocodInsumo = insumocodInsumo;
    }

    public String getCocinacodCocina() {
        return cocinacodCocina;
    }

    public void setCocinacodCocina(String cocinacodCocina) {
        this.cocinacodCocina = cocinacodCocina;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (insumocodInsumo != null ? insumocodInsumo.hashCode() : 0);
        hash += (cocinacodCocina != null ? cocinacodCocina.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof IpvPK)) {
            return false;
        }
        IpvPK other = (IpvPK) object;
        if ((this.insumocodInsumo == null && other.insumocodInsumo != null) || (this.insumocodInsumo != null && !this.insumocodInsumo.equals(other.insumocodInsumo))) {
            return false;
        }
        if ((this.cocinacodCocina == null && other.cocinacodCocina != null) || (this.cocinacodCocina != null && !this.cocinacodCocina.equals(other.cocinacodCocina))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.restmanager.IpvPK[ insumocodInsumo=" + insumocodInsumo + ", cocinacodCocina=" + cocinacodCocina + " ]";
    }

}
