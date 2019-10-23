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
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * FirstDream
 * @author Jorge
 * 
 */
@Entity
@Table(name = "gasto_venta")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GastoVenta.findAll", query = "SELECT g FROM GastoVenta g")
    , @NamedQuery(name = "GastoVenta.findByGastocodGasto", query = "SELECT g FROM GastoVenta g WHERE g.gastoVentaPK.gastocodGasto = :gastocodGasto")
    , @NamedQuery(name = "GastoVenta.findByVentafecha", query = "SELECT g FROM GastoVenta g WHERE g.gastoVentaPK.ventafecha = :ventafecha")
    , @NamedQuery(name = "GastoVenta.findByImporte", query = "SELECT g FROM GastoVenta g WHERE g.importe = :importe")
    , @NamedQuery(name = "GastoVenta.findByDescripcion", query = "SELECT g FROM GastoVenta g WHERE g.descripcion = :descripcion")})
public class GastoVenta implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected GastoVentaPK gastoVentaPK;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "importe")
    private Float importe;
    @Size(max = 100)
    @Column(name = "descripcion")
    private String descripcion;
    @JoinColumn(name = "gastocod_gasto", referencedColumnName = "cod_gasto", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Gasto gasto;
    @JoinColumn(name = "ventafecha", referencedColumnName = "fecha", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Venta venta;

    public GastoVenta() {
    }

    public GastoVenta(GastoVentaPK gastoVentaPK) {
        this.gastoVentaPK = gastoVentaPK;
    }

    public GastoVenta(String gastocodGasto, Date ventafecha) {
        this.gastoVentaPK = new GastoVentaPK(gastocodGasto, ventafecha);
    }

    public GastoVentaPK getGastoVentaPK() {
        return gastoVentaPK;
    }

    public void setGastoVentaPK(GastoVentaPK gastoVentaPK) {
        this.gastoVentaPK = gastoVentaPK;
    }

    public Float getImporte() {
        return importe;
    }

    public void setImporte(Float importe) {
        this.importe = importe;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Gasto getGasto() {
        return gasto;
    }

    public void setGasto(Gasto gasto) {
        this.gasto = gasto;
    }

    public Venta getVenta() {
        return venta;
    }

    public void setVenta(Venta venta) {
        this.venta = venta;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (gastoVentaPK != null ? gastoVentaPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GastoVenta)) {
            return false;
        }
        GastoVenta other = (GastoVenta) object;
        if ((this.gastoVentaPK == null && other.gastoVentaPK != null) || (this.gastoVentaPK != null && !this.gastoVentaPK.equals(other.gastoVentaPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.restmanager.GastoVenta[ gastoVentaPK=" + gastoVentaPK + " ]";
    }

}
