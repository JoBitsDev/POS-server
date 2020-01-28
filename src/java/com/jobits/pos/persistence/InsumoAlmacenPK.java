/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jobits.pos.persistence;

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
public class InsumoAlmacenPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 6)
    @Column(name = "insumocod_insumo")
    private String insumocodInsumo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 4)
    @Column(name = "almacencod_almacen")
    private String almacencodAlmacen;

    public InsumoAlmacenPK() {
    }

    public InsumoAlmacenPK(String insumocodInsumo, String almacencodAlmacen) {
        this.insumocodInsumo = insumocodInsumo;
        this.almacencodAlmacen = almacencodAlmacen;
    }

    public String getInsumocodInsumo() {
        return insumocodInsumo;
    }

    public void setInsumocodInsumo(String insumocodInsumo) {
        this.insumocodInsumo = insumocodInsumo;
    }

    public String getAlmacencodAlmacen() {
        return almacencodAlmacen;
    }

    public void setAlmacencodAlmacen(String almacencodAlmacen) {
        this.almacencodAlmacen = almacencodAlmacen;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (insumocodInsumo != null ? insumocodInsumo.hashCode() : 0);
        hash += (almacencodAlmacen != null ? almacencodAlmacen.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof InsumoAlmacenPK)) {
            return false;
        }
        InsumoAlmacenPK other = (InsumoAlmacenPK) object;
        if ((this.insumocodInsumo == null && other.insumocodInsumo != null) || (this.insumocodInsumo != null && !this.insumocodInsumo.equals(other.insumocodInsumo))) {
            return false;
        }
        if ((this.almacencodAlmacen == null && other.almacencodAlmacen != null) || (this.almacencodAlmacen != null && !this.almacencodAlmacen.equals(other.almacencodAlmacen))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.restmanager.InsumoAlmacenPK[ insumocodInsumo=" + insumocodInsumo + ", almacencodAlmacen=" + almacencodAlmacen + " ]";
    }

}
