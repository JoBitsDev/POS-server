/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.restmanager;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
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
    , @NamedQuery(name = "TransaccionMerma.findByTransaccioninsumocodInsumo", query = "SELECT t FROM TransaccionMerma t WHERE t.transaccionMermaPK.transaccioninsumocodInsumo = :transaccioninsumocodInsumo")
    , @NamedQuery(name = "TransaccionMerma.findByTransaccionalmacencodAlmacen", query = "SELECT t FROM TransaccionMerma t WHERE t.transaccionMermaPK.transaccionalmacencodAlmacen = :transaccionalmacencodAlmacen")
    , @NamedQuery(name = "TransaccionMerma.findByTransaccionfecha", query = "SELECT t FROM TransaccionMerma t WHERE t.transaccionMermaPK.transaccionfecha = :transaccionfecha")
    , @NamedQuery(name = "TransaccionMerma.findByTransaccionhora", query = "SELECT t FROM TransaccionMerma t WHERE t.transaccionMermaPK.transaccionhora = :transaccionhora")
    , @NamedQuery(name = "TransaccionMerma.findByRazon", query = "SELECT t FROM TransaccionMerma t WHERE t.razon = :razon")})
public class TransaccionMerma implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected TransaccionMermaPK transaccionMermaPK;
    @Size(max = 255)
    @Column(name = "razon")
    private String razon;
    @JoinColumns({
        @JoinColumn(name = "transaccioninsumocod_insumo", referencedColumnName = "insumocod_insumo", insertable = false, updatable = false)
        , @JoinColumn(name = "transaccionalmacencod_almacen", referencedColumnName = "almacencod_almacen", insertable = false, updatable = false)
        , @JoinColumn(name = "transaccionfecha", referencedColumnName = "fecha", insertable = false, updatable = false)
        , @JoinColumn(name = "transaccionhora", referencedColumnName = "hora", insertable = false, updatable = false)})
    @OneToOne(optional = false)
    private Transaccion transaccion;

    public TransaccionMerma() {
    }

    public TransaccionMerma(TransaccionMermaPK transaccionMermaPK) {
        this.transaccionMermaPK = transaccionMermaPK;
    }

    public TransaccionMerma(String transaccioninsumocodInsumo, String transaccionalmacencodAlmacen, Date transaccionfecha, Date transaccionhora) {
        this.transaccionMermaPK = new TransaccionMermaPK(transaccioninsumocodInsumo, transaccionalmacencodAlmacen, transaccionfecha, transaccionhora);
    }

    public TransaccionMermaPK getTransaccionMermaPK() {
        return transaccionMermaPK;
    }

    public void setTransaccionMermaPK(TransaccionMermaPK transaccionMermaPK) {
        this.transaccionMermaPK = transaccionMermaPK;
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
        hash += (transaccionMermaPK != null ? transaccionMermaPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TransaccionMerma)) {
            return false;
        }
        TransaccionMerma other = (TransaccionMerma) object;
        if ((this.transaccionMermaPK == null && other.transaccionMermaPK != null) || (this.transaccionMermaPK != null && !this.transaccionMermaPK.equals(other.transaccionMermaPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.restmanager.TransaccionMerma[ transaccionMermaPK=" + transaccionMermaPK + " ]";
    }

}
