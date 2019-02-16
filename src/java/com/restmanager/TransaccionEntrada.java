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
import javax.xml.bind.annotation.XmlRootElement;

/**
 * FirstDream
 * @author Jorge
 * 
 */
@Entity
@Table(name = "transaccion_entrada")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TransaccionEntrada.findAll", query = "SELECT t FROM TransaccionEntrada t")
    , @NamedQuery(name = "TransaccionEntrada.findByTransaccioninsumocodInsumo", query = "SELECT t FROM TransaccionEntrada t WHERE t.transaccionEntradaPK.transaccioninsumocodInsumo = :transaccioninsumocodInsumo")
    , @NamedQuery(name = "TransaccionEntrada.findByTransaccionfecha", query = "SELECT t FROM TransaccionEntrada t WHERE t.transaccionEntradaPK.transaccionfecha = :transaccionfecha")
    , @NamedQuery(name = "TransaccionEntrada.findByTransaccionhora", query = "SELECT t FROM TransaccionEntrada t WHERE t.transaccionEntradaPK.transaccionhora = :transaccionhora")
    , @NamedQuery(name = "TransaccionEntrada.findByTransaccionalmacencodAlmacen", query = "SELECT t FROM TransaccionEntrada t WHERE t.transaccionEntradaPK.transaccionalmacencodAlmacen = :transaccionalmacencodAlmacen")
    , @NamedQuery(name = "TransaccionEntrada.findByConsumido", query = "SELECT t FROM TransaccionEntrada t WHERE t.consumido = :consumido")
    , @NamedQuery(name = "TransaccionEntrada.findByPrecioPorUnidad", query = "SELECT t FROM TransaccionEntrada t WHERE t.precioPorUnidad = :precioPorUnidad")
    , @NamedQuery(name = "TransaccionEntrada.findByValorTotal", query = "SELECT t FROM TransaccionEntrada t WHERE t.valorTotal = :valorTotal")})
public class TransaccionEntrada implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected TransaccionEntradaPK transaccionEntradaPK;
    @Column(name = "consumido")
    private Boolean consumido;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "precio_por_unidad")
    private Float precioPorUnidad;
    @Column(name = "valor_total")
    private Float valorTotal;
    @JoinColumns({
        @JoinColumn(name = "transaccioninsumocod_insumo", referencedColumnName = "insumocod_insumo", insertable = false, updatable = false)
        , @JoinColumn(name = "transaccionalmacencod_almacen", referencedColumnName = "almacencod_almacen", insertable = false, updatable = false)
        , @JoinColumn(name = "transaccionfecha", referencedColumnName = "fecha", insertable = false, updatable = false)
        , @JoinColumn(name = "transaccionhora", referencedColumnName = "hora", insertable = false, updatable = false)})
    @OneToOne(optional = false)
    private Transaccion transaccion;

    public TransaccionEntrada() {
    }

    public TransaccionEntrada(TransaccionEntradaPK transaccionEntradaPK) {
        this.transaccionEntradaPK = transaccionEntradaPK;
    }

    public TransaccionEntrada(String transaccioninsumocodInsumo, Date transaccionfecha, Date transaccionhora, String transaccionalmacencodAlmacen) {
        this.transaccionEntradaPK = new TransaccionEntradaPK(transaccioninsumocodInsumo, transaccionfecha, transaccionhora, transaccionalmacencodAlmacen);
    }

    public TransaccionEntradaPK getTransaccionEntradaPK() {
        return transaccionEntradaPK;
    }

    public void setTransaccionEntradaPK(TransaccionEntradaPK transaccionEntradaPK) {
        this.transaccionEntradaPK = transaccionEntradaPK;
    }

    public Boolean getConsumido() {
        return consumido;
    }

    public void setConsumido(Boolean consumido) {
        this.consumido = consumido;
    }

    public Float getPrecioPorUnidad() {
        return precioPorUnidad;
    }

    public void setPrecioPorUnidad(Float precioPorUnidad) {
        this.precioPorUnidad = precioPorUnidad;
    }

    public Float getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(Float valorTotal) {
        this.valorTotal = valorTotal;
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
        hash += (transaccionEntradaPK != null ? transaccionEntradaPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TransaccionEntrada)) {
            return false;
        }
        TransaccionEntrada other = (TransaccionEntrada) object;
        if ((this.transaccionEntradaPK == null && other.transaccionEntradaPK != null) || (this.transaccionEntradaPK != null && !this.transaccionEntradaPK.equals(other.transaccionEntradaPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.restmanager.TransaccionEntrada[ transaccionEntradaPK=" + transaccionEntradaPK + " ]";
    }

}
