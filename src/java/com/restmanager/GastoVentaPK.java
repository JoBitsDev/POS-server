/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.restmanager;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * FirstDream
 * @author Jorge
 * 
 */
@Embeddable
public class GastoVentaPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "gastocod_gasto")
    private String gastocodGasto;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ventafecha")
    @Temporal(TemporalType.DATE)
    private Date ventafecha;

    public GastoVentaPK() {
    }

    public GastoVentaPK(String gastocodGasto, Date ventafecha) {
        this.gastocodGasto = gastocodGasto;
        this.ventafecha = ventafecha;
    }

    public String getGastocodGasto() {
        return gastocodGasto;
    }

    public void setGastocodGasto(String gastocodGasto) {
        this.gastocodGasto = gastocodGasto;
    }

    public Date getVentafecha() {
        return ventafecha;
    }

    public void setVentafecha(Date ventafecha) {
        this.ventafecha = ventafecha;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (gastocodGasto != null ? gastocodGasto.hashCode() : 0);
        hash += (ventafecha != null ? ventafecha.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GastoVentaPK)) {
            return false;
        }
        GastoVentaPK other = (GastoVentaPK) object;
        if ((this.gastocodGasto == null && other.gastocodGasto != null) || (this.gastocodGasto != null && !this.gastocodGasto.equals(other.gastocodGasto))) {
            return false;
        }
        if ((this.ventafecha == null && other.ventafecha != null) || (this.ventafecha != null && !this.ventafecha.equals(other.ventafecha))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.restmanager.GastoVentaPK[ gastocodGasto=" + gastocodGasto + ", ventafecha=" + ventafecha + " ]";
    }

}
