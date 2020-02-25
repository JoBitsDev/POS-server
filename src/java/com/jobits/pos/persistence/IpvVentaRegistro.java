/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jobits.pos.persistence;

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
import javax.xml.bind.annotation.XmlRootElement;

/**
 * FirstDream
 *
 * @author Jorge
 *
 */
@Entity
@Table(name = "ipv_venta_registro")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "IpvVentaRegistro.findAll", query = "SELECT i FROM IpvVentaRegistro i")
    , @NamedQuery(name = "IpvVentaRegistro.findByVentafecha", query = "SELECT i FROM IpvVentaRegistro i WHERE i.ipvVentaRegistroPK.ventafecha = :ventafecha")
    , @NamedQuery(name = "IpvVentaRegistro.findByProductoVentapCod", query = "SELECT i FROM IpvVentaRegistro i WHERE i.ipvVentaRegistroPK.productoVentapCod = :productoVentapCod")
    , @NamedQuery(name = "IpvVentaRegistro.findByPtoElab", query = "SELECT i FROM IpvVentaRegistro i WHERE i.productoVenta.cocinacodCocina.codCocina = :ptoElab AND i.venta1.fecha = :fecha")
    , @NamedQuery(name = "IpvVentaRegistro.findByInicio", query = "SELECT i FROM IpvVentaRegistro i WHERE i.inicio = :inicio")
    , @NamedQuery(name = "IpvVentaRegistro.findByEntrada", query = "SELECT i FROM IpvVentaRegistro i WHERE i.entrada = :entrada")
    , @NamedQuery(name = "IpvVentaRegistro.findByDisponible", query = "SELECT i FROM IpvVentaRegistro i WHERE i.disponible = :disponible")
    , @NamedQuery(name = "IpvVentaRegistro.findByAutorizos", query = "SELECT i FROM IpvVentaRegistro i WHERE i.autorizos = :autorizos")
    , @NamedQuery(name = "IpvVentaRegistro.findByVenta", query = "SELECT i FROM IpvVentaRegistro i WHERE i.venta = :venta")
    , @NamedQuery(name = "IpvVentaRegistro.findByPrecioVenta", query = "SELECT i FROM IpvVentaRegistro i WHERE i.precioVenta = :precioVenta")
    , @NamedQuery(name = "IpvVentaRegistro.findByFinal1", query = "SELECT i FROM IpvVentaRegistro i WHERE i.final1 = :final1")})
public class IpvVentaRegistro implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected IpvVentaRegistroPK ipvVentaRegistroPK;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "inicio")
    private Float inicio;
    @Column(name = "entrada")
    private Float entrada;
    @Column(name = "disponible")
    private Float disponible;
    @Column(name = "autorizos")
    private Float autorizos;
    @Column(name = "venta")
    private Float venta;
    @Column(name = "precio_venta")
    private Float precioVenta;
    @Column(name = "final")
    private Float final1;
    @JoinColumn(name = "producto_ventap_cod", referencedColumnName = "p_cod", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private ProductoVenta productoVenta;
    @JoinColumn(name = "ventafecha", referencedColumnName = "fecha", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Venta venta1;

    public IpvVentaRegistro() {
    }

    public IpvVentaRegistro(IpvVentaRegistroPK ipvVentaRegistroPK) {
        this.ipvVentaRegistroPK = ipvVentaRegistroPK;
    }

    public IpvVentaRegistro(Date ventafecha, String productoVentapCod) {
        this.ipvVentaRegistroPK = new IpvVentaRegistroPK(ventafecha, productoVentapCod);
    }

    public IpvVentaRegistroPK getIpvVentaRegistroPK() {
        return ipvVentaRegistroPK;
    }

    public void setIpvVentaRegistroPK(IpvVentaRegistroPK ipvVentaRegistroPK) {
        this.ipvVentaRegistroPK = ipvVentaRegistroPK;
    }

    public Float getInicio() {
        return inicio;
    }

    public void setInicio(Float inicio) {
        this.inicio = inicio;
    }

    public Float getEntrada() {
        return entrada;
    }

    public void setEntrada(Float entrada) {
        this.entrada = entrada;
    }

    public Float getDisponible() {
        return disponible;
    }

    public void setDisponible(Float disponible) {
        this.disponible = disponible;
    }

    public Float getAutorizos() {
        return autorizos;
    }

    public void setAutorizos(Float autorizos) {
        this.autorizos = autorizos;
    }

    public Float getVenta() {
        return venta;
    }

    public void setVenta(Float venta) {
        this.venta = venta;
    }

    public Float getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(Float precioVenta) {
        this.precioVenta = precioVenta;
    }

    public Float getFinal1() {
        return final1;
    }

    public void setFinal1(Float final1) {
        this.final1 = final1;
    }

    public ProductoVenta getProductoVenta() {
        return productoVenta;
    }

    public void setProductoVenta(ProductoVenta productoVenta) {
        this.productoVenta = productoVenta;
    }

    public Venta getVenta1() {
        return venta1;
    }

    public void setVenta1(Venta venta1) {
        this.venta1 = venta1;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ipvVentaRegistroPK != null ? ipvVentaRegistroPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof IpvVentaRegistro)) {
            return false;
        }
        IpvVentaRegistro other = (IpvVentaRegistro) object;
        if ((this.ipvVentaRegistroPK == null && other.ipvVentaRegistroPK != null) || (this.ipvVentaRegistroPK != null && !this.ipvVentaRegistroPK.equals(other.ipvVentaRegistroPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.jobits.pos.persistence.IpvVentaRegistro[ ipvVentaRegistroPK=" + ipvVentaRegistroPK + " ]";
    }

}
