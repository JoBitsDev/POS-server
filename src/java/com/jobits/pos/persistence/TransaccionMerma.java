/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jobits.pos.persistence;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * FirstDream
 * @author Jorge
 * 
 */
@Entity
@Table(name = "transaccion_merma")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TransaccionMerma.findAll", query = "SELECT t FROM TransaccionMerma t")
    , @NamedQuery(name = "TransaccionMerma.findByTransaccionnoTransaccion", query = "SELECT t FROM TransaccionMerma t WHERE t.transaccionnoTransaccion = :transaccionnoTransaccion")
    , @NamedQuery(name = "TransaccionMerma.findByRazon", query = "SELECT t FROM TransaccionMerma t WHERE t.razon = :razon")})
public class TransaccionMerma implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "transaccionno_transaccion")
    private Integer transaccionnoTransaccion;
    @Size(max = 255)
    @Column(name = "razon")
    private String razon;
    @JoinColumn(name = "transaccionno_transaccion", referencedColumnName = "no_transaccion", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Transaccion transaccion;

    public TransaccionMerma() {
    }

    public TransaccionMerma(Integer transaccionnoTransaccion) {
        this.transaccionnoTransaccion = transaccionnoTransaccion;
    }

    public Integer getTransaccionnoTransaccion() {
        return transaccionnoTransaccion;
    }

    public void setTransaccionnoTransaccion(Integer transaccionnoTransaccion) {
        this.transaccionnoTransaccion = transaccionnoTransaccion;
    }

    public String getRazon() {
        return razon;
    }

    public void setRazon(String razon) {
        this.razon = razon;
    }

    public Transaccion getTransaccion() {
        return transaccion;
    }

    public void setTransaccion(Transaccion transaccion) {
        this.transaccion = transaccion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (transaccionnoTransaccion != null ? transaccionnoTransaccion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TransaccionMerma)) {
            return false;
        }
        TransaccionMerma other = (TransaccionMerma) object;
        if ((this.transaccionnoTransaccion == null && other.transaccionnoTransaccion != null) || (this.transaccionnoTransaccion != null && !this.transaccionnoTransaccion.equals(other.transaccionnoTransaccion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.restmanager.TransaccionMerma[ transaccionnoTransaccion=" + transaccionnoTransaccion + " ]";
    }

}
