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
public class TransaccionTransformacionPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "transaccionno_transaccion")
    private int transaccionnoTransaccion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 6)
    @Column(name = "insumocod_insumo")
    private String insumocodInsumo;

    public TransaccionTransformacionPK() {
    }

    public TransaccionTransformacionPK(int transaccionnoTransaccion, String insumocodInsumo) {
        this.transaccionnoTransaccion = transaccionnoTransaccion;
        this.insumocodInsumo = insumocodInsumo;
    }

    public int getTransaccionnoTransaccion() {
        return transaccionnoTransaccion;
    }

    public void setTransaccionnoTransaccion(int transaccionnoTransaccion) {
        this.transaccionnoTransaccion = transaccionnoTransaccion;
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
        hash += (int) transaccionnoTransaccion;
        hash += (insumocodInsumo != null ? insumocodInsumo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TransaccionTransformacionPK)) {
            return false;
        }
        TransaccionTransformacionPK other = (TransaccionTransformacionPK) object;
        if (this.transaccionnoTransaccion != other.transaccionnoTransaccion) {
            return false;
        }
        if ((this.insumocodInsumo == null && other.insumocodInsumo != null) || (this.insumocodInsumo != null && !this.insumocodInsumo.equals(other.insumocodInsumo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.restmanager.TransaccionTransformacionPK[ transaccionnoTransaccion=" + transaccionnoTransaccion + ", insumocodInsumo=" + insumocodInsumo + " ]";
    }

}
