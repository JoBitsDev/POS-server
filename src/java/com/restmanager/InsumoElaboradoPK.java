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
 *
 * @author Jorge
 */
@Embeddable
public class InsumoElaboradoPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 6)
    @Column(name = "insumocod_nombre")
    private String insumocodNombre;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 6)
    @Column(name = "insumocod_insumo")
    private String insumocodInsumo;

    public InsumoElaboradoPK() {
    }

    public InsumoElaboradoPK(String insumocodNombre, String insumocodInsumo) {
        this.insumocodNombre = insumocodNombre;
        this.insumocodInsumo = insumocodInsumo;
    }

    public String getInsumocodNombre() {
        return insumocodNombre;
    }

    public void setInsumocodNombre(String insumocodNombre) {
        this.insumocodNombre = insumocodNombre;
    }

    public String getInsumocodInsumo() {
        return insumocodInsumo;
    }

    public void setInsumocodInsumo(String insumocodInsumo) {
        this.insumocodInsumo = insumocodInsumo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (insumocodNombre != null ? insumocodNombre.hashCode() : 0);
        hash += (insumocodInsumo != null ? insumocodInsumo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof InsumoElaboradoPK)) {
            return false;
        }
        InsumoElaboradoPK other = (InsumoElaboradoPK) object;
        if ((this.insumocodNombre == null && other.insumocodNombre != null) || (this.insumocodNombre != null && !this.insumocodNombre.equals(other.insumocodNombre))) {
            return false;
        }
        if ((this.insumocodInsumo == null && other.insumocodInsumo != null) || (this.insumocodInsumo != null && !this.insumocodInsumo.equals(other.insumocodInsumo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.restmanager.InsumoElaboradoPK[ insumocodNombre=" + insumocodNombre + ", insumocodInsumo=" + insumocodInsumo + " ]";
    }
    
}
