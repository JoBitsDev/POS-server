/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.restmanager;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * FirstDream
 * @author Jorge
 * 
 */
@Entity
@Table(name = "factura")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Factura.findAll", query = "SELECT f FROM Factura f")
    , @NamedQuery(name = "Factura.findByIdFactura", query = "SELECT f FROM Factura f WHERE f.idFactura = :idFactura")
    , @NamedQuery(name = "Factura.findByNoSerieFactura", query = "SELECT f FROM Factura f WHERE f.noSerieFactura = :noSerieFactura")
    , @NamedQuery(name = "Factura.findByNombre", query = "SELECT f FROM Factura f WHERE f.nombre = :nombre")
    , @NamedQuery(name = "Factura.findByMontoAPagar", query = "SELECT f FROM Factura f WHERE f.montoAPagar = :montoAPagar")
    , @NamedQuery(name = "Factura.findByMontoPagado", query = "SELECT f FROM Factura f WHERE f.montoPagado = :montoPagado")
    , @NamedQuery(name = "Factura.findByFecha", query = "SELECT f FROM Factura f WHERE f.fecha = :fecha")
    , @NamedQuery(name = "Factura.findByDescripcion", query = "SELECT f FROM Factura f WHERE f.descripcion = :descripcion")
    , @NamedQuery(name = "Factura.findByEsGasto", query = "SELECT f FROM Factura f WHERE f.esGasto = :esGasto")})
public class Factura implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_factura")
    private Integer idFactura;
    @Size(max = 100)
    @Column(name = "no_serie_factura")
    private String noSerieFactura;
    @Size(max = 100)
    @Column(name = "nombre")
    private String nombre;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "monto_a_pagar")
    private Float montoAPagar;
    @Column(name = "monto_pagado")
    private Float montoPagado;
    @Column(name = "fecha")
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @Size(max = 100)
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "es_gasto")
    private Boolean esGasto;
    @JoinColumn(name = "id_contabilidad_cuenta", referencedColumnName = "id_cuenta")
    @ManyToOne(optional = false)
    private ContabilidadCuenta idContabilidadCuenta;
    @JoinColumn(name = "cuenta_afectada", referencedColumnName = "id_cuenta")
    @ManyToOne(optional = false)
    private ContabilidadCuenta cuentaAfectada;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "factura")
    private List<Pago> pagoList;

    public Factura() {
    }

    public Factura(Integer idFactura) {
        this.idFactura = idFactura;
    }

    public Integer getIdFactura() {
        return idFactura;
    }

    public void setIdFactura(Integer idFactura) {
        this.idFactura = idFactura;
    }

    public String getNoSerieFactura() {
        return noSerieFactura;
    }

    public void setNoSerieFactura(String noSerieFactura) {
        this.noSerieFactura = noSerieFactura;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Float getMontoAPagar() {
        return montoAPagar;
    }

    public void setMontoAPagar(Float montoAPagar) {
        this.montoAPagar = montoAPagar;
    }

    public Float getMontoPagado() {
        return montoPagado;
    }

    public void setMontoPagado(Float montoPagado) {
        this.montoPagado = montoPagado;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Boolean getEsGasto() {
        return esGasto;
    }

    public void setEsGasto(Boolean esGasto) {
        this.esGasto = esGasto;
    }

    public ContabilidadCuenta getIdContabilidadCuenta() {
        return idContabilidadCuenta;
    }

    public void setIdContabilidadCuenta(ContabilidadCuenta idContabilidadCuenta) {
        this.idContabilidadCuenta = idContabilidadCuenta;
    }

    public ContabilidadCuenta getCuentaAfectada() {
        return cuentaAfectada;
    }

    public void setCuentaAfectada(ContabilidadCuenta cuentaAfectada) {
        this.cuentaAfectada = cuentaAfectada;
    }

    @XmlTransient
    public List<Pago> getPagoList() {
        return pagoList;
    }

    public void setPagoList(List<Pago> pagoList) {
        this.pagoList = pagoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idFactura != null ? idFactura.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Factura)) {
            return false;
        }
        Factura other = (Factura) object;
        if ((this.idFactura == null && other.idFactura != null) || (this.idFactura != null && !this.idFactura.equals(other.idFactura))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.restmanager.Factura[ idFactura=" + idFactura + " ]";
    }

}
