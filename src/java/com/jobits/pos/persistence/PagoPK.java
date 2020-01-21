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

/**
 * FirstDream
 * @author Jorge
 * 
 */
@Embeddable
public class PagoPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "id_pago")
    private int idPago;
    @Basic(optional = false)
    @NotNull
    @Column(name = "facturaid_factura")
    private int facturaidFactura;

    public PagoPK() {
    }

    public PagoPK(int idPago, int facturaidFactura) {
        this.idPago = idPago;
        this.facturaidFactura = facturaidFactura;
    }

    public int getIdPago() {
        return idPago;
    }

    public void setIdPago(int idPago) {
        this.idPago = idPago;
    }

    public int getFacturaidFactura() {
        return facturaidFactura;
    }

    public void setFacturaidFactura(int facturaidFactura) {
        this.facturaidFactura = facturaidFactura;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idPago;
        hash += (int) facturaidFactura;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PagoPK)) {
            return false;
        }
        PagoPK other = (PagoPK) object;
        if (this.idPago != other.idPago) {
            return false;
        }
        if (this.facturaidFactura != other.facturaidFactura) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.restmanager.PagoPK[ idPago=" + idPago + ", facturaidFactura=" + facturaidFactura + " ]";
    }

}
