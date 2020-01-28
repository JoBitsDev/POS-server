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
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * FirstDream
 * @author Jorge
 * 
 */
@Entity
@Table(name = "transaccion_traspaso")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TransaccionTraspaso.findAll", query = "SELECT t FROM TransaccionTraspaso t")
    , @NamedQuery(name = "TransaccionTraspaso.findByTransaccionnoTransaccion", query = "SELECT t FROM TransaccionTraspaso t WHERE t.transaccionnoTransaccion = :transaccionnoTransaccion")})
public class TransaccionTraspaso implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "transaccionno_transaccion")
    private Integer transaccionnoTransaccion;
    @JoinColumn(name = "almacen_destino", referencedColumnName = "cod_almacen")
    @ManyToOne(optional = false)
    private Almacen almacenDestino;
    @JoinColumn(name = "transaccionno_transaccion", referencedColumnName = "no_transaccion", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Transaccion transaccion;

    public TransaccionTraspaso() {
    }

    public TransaccionTraspaso(Integer transaccionnoTransaccion) {
        this.transaccionnoTransaccion = transaccionnoTransaccion;
    }

    public Integer getTransaccionnoTransaccion() {
        return transaccionnoTransaccion;
    }

    public void setTransaccionnoTransaccion(Integer transaccionnoTransaccion) {
        this.transaccionnoTransaccion = transaccionnoTransaccion;
    }

    public Almacen getAlmacenDestino() {
        return almacenDestino;
    }

    public void setAlmacenDestino(Almacen almacenDestino) {
        this.almacenDestino = almacenDestino;
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
        if (!(object instanceof TransaccionTraspaso)) {
            return false;
        }
        TransaccionTraspaso other = (TransaccionTraspaso) object;
        if ((this.transaccionnoTransaccion == null && other.transaccionnoTransaccion != null) || (this.transaccionnoTransaccion != null && !this.transaccionnoTransaccion.equals(other.transaccionnoTransaccion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.restmanager.TransaccionTraspaso[ transaccionnoTransaccion=" + transaccionnoTransaccion + " ]";
    }

}
