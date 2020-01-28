/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jobits.pos.persistence;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * FirstDream
 * @author Jorge
 * 
 */
@Entity
@Table(name = "producto_insumo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ProductoInsumo.findAll", query = "SELECT p FROM ProductoInsumo p")
    , @NamedQuery(name = "ProductoInsumo.findByProductoVentapCod", query = "SELECT p FROM ProductoInsumo p WHERE p.productoInsumoPK.productoVentapCod = :productoVentapCod")
    , @NamedQuery(name = "ProductoInsumo.findByInsumocodInsumo", query = "SELECT p FROM ProductoInsumo p WHERE p.productoInsumoPK.insumocodInsumo = :insumocodInsumo")
    , @NamedQuery(name = "ProductoInsumo.findByCantidad", query = "SELECT p FROM ProductoInsumo p WHERE p.cantidad = :cantidad")
    , @NamedQuery(name = "ProductoInsumo.findByCosto", query = "SELECT p FROM ProductoInsumo p WHERE p.costo = :costo")})
public class ProductoInsumo implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ProductoInsumoPK productoInsumoPK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cantidad")
    private float cantidad;
    @Basic(optional = false)
    @NotNull
    @Column(name = "costo")
    private float costo;
    @JoinColumn(name = "insumocod_insumo", referencedColumnName = "cod_insumo", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Insumo insumo;
    @JoinColumn(name = "producto_ventap_cod", referencedColumnName = "p_cod", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private ProductoVenta productoVenta;

    public ProductoInsumo() {
    }

    public ProductoInsumo(ProductoInsumoPK productoInsumoPK) {
        this.productoInsumoPK = productoInsumoPK;
    }

    public ProductoInsumo(ProductoInsumoPK productoInsumoPK, float cantidad, float costo) {
        this.productoInsumoPK = productoInsumoPK;
        this.cantidad = cantidad;
        this.costo = costo;
    }

    public ProductoInsumo(String productoVentapCod, String insumocodInsumo) {
        this.productoInsumoPK = new ProductoInsumoPK(productoVentapCod, insumocodInsumo);
    }

    public ProductoInsumoPK getProductoInsumoPK() {
        return productoInsumoPK;
    }

    public void setProductoInsumoPK(ProductoInsumoPK productoInsumoPK) {
        this.productoInsumoPK = productoInsumoPK;
    }

    public float getCantidad() {
        return cantidad;
    }

    public void setCantidad(float cantidad) {
        this.cantidad = cantidad;
    }

    public float getCosto() {
        return costo;
    }

    public void setCosto(float costo) {
        this.costo = costo;
    }

    public Insumo getInsumo() {
        return insumo;
    }

    public void setInsumo(Insumo insumo) {
        this.insumo = insumo;
    }

    public ProductoVenta getProductoVenta() {
        return productoVenta;
    }

    public void setProductoVenta(ProductoVenta productoVenta) {
        this.productoVenta = productoVenta;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (productoInsumoPK != null ? productoInsumoPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProductoInsumo)) {
            return false;
        }
        ProductoInsumo other = (ProductoInsumo) object;
        if ((this.productoInsumoPK == null && other.productoInsumoPK != null) || (this.productoInsumoPK != null && !this.productoInsumoPK.equals(other.productoInsumoPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.restmanager.ProductoInsumo[ productoInsumoPK=" + productoInsumoPK + " ]";
    }

}
