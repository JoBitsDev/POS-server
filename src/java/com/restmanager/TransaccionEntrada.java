/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.restmanager;

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
    , @NamedQuery(name = "TransaccionEntrada.findByTransaccionnoTransaccion", query = "SELECT t FROM TransaccionEntrada t WHERE t.transaccionnoTransaccion = :transaccionnoTransaccion")
    , @NamedQuery(name = "TransaccionEntrada.findByJustificado", query = "SELECT t FROM TransaccionEntrada t WHERE t.justificado = :justificado")
    , @NamedQuery(name = "TransaccionEntrada.findByPrecioPorUnidad", query = "SELECT t FROM TransaccionEntrada t WHERE t.precioPorUnidad = :precioPorUnidad")
    , @NamedQuery(name = "TransaccionEntrada.findByValorTotal", query = "SELECT t FROM TransaccionEntrada t WHERE t.valorTotal = :valorTotal")})
public class TransaccionEntrada implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "transaccionno_transaccion")
    private Integer transaccionnoTransaccion;
    @Column(name = "justificado")
    private Boolean justificado;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "precio_por_unidad")
    private Float precioPorUnidad;
    @Column(name = "valor_total")
    private Float valorTotal;
    @JoinColumn(name = "transaccionno_transaccion", referencedColumnName = "no_transaccion", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Transaccion transaccion;

    public TransaccionEntrada() {
    }

    public TransaccionEntrada(Integer transaccionnoTransaccion) {
        this.transaccionnoTransaccion = transaccionnoTransaccion;
    }

    public Integer getTransaccionnoTransaccion() {
        return transaccionnoTransaccion;
    }

    public void setTransaccionnoTransaccion(Integer transaccionnoTransaccion) {
        this.transaccionnoTransaccion = transaccionnoTransaccion;
    }

    public Boolean getJustificado() {
        return justificado;
    }

    public void setJustificado(Boolean justificado) {
        this.justificado = justificado;
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
        hash += (transaccionnoTransaccion != null ? transaccionnoTransaccion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TransaccionEntrada)) {
            return false;
        }
        TransaccionEntrada other = (TransaccionEntrada) object;
        if ((this.transaccionnoTransaccion == null && other.transaccionnoTransaccion != null) || (this.transaccionnoTransaccion != null && !this.transaccionnoTransaccion.equals(other.transaccionnoTransaccion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.restmanager.TransaccionEntrada[ transaccionnoTransaccion=" + transaccionnoTransaccion + " ]";
    }

}
