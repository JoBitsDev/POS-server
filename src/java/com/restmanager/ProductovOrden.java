/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.restmanager;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
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
@Table(name = "productov_orden")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ProductovOrden.findAll", query = "SELECT p FROM ProductovOrden p")
    , @NamedQuery(name = "ProductovOrden.findByProductoVentapCod", query = "SELECT p FROM ProductovOrden p WHERE p.productovOrdenPK.productoVentapCod = :productoVentapCod")
    , @NamedQuery(name = "ProductovOrden.findByOrdencodOrden", query = "SELECT p FROM ProductovOrden p WHERE p.productovOrdenPK.ordencodOrden = :ordencodOrden")
    , @NamedQuery(name = "ProductovOrden.findByCantidad", query = "SELECT p FROM ProductovOrden p WHERE p.cantidad = :cantidad")
    , @NamedQuery(name = "ProductovOrden.findByEnviadosacocina", query = "SELECT p FROM ProductovOrden p WHERE p.enviadosacocina = :enviadosacocina")
    , @NamedQuery(name = "ProductovOrden.findByNumeroComensal", query = "SELECT p FROM ProductovOrden p WHERE p.numeroComensal = :numeroComensal")})
public class ProductovOrden implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ProductovOrdenPK productovOrdenPK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cantidad")
    private int cantidad;
    @Column(name = "enviadosacocina")
    private Integer enviadosacocina;
    @Column(name = "numero_comensal")
    private Integer numeroComensal;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "productovOrden")
    private Nota nota;
    @JoinColumn(name = "ordencod_orden", referencedColumnName = "cod_orden", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Orden orden;
    @JoinColumn(name = "producto_ventap_cod", referencedColumnName = "p_cod", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private ProductoVenta productoVenta;

    public ProductovOrden() {
    }

    public ProductovOrden(ProductovOrdenPK productovOrdenPK) {
        this.productovOrdenPK = productovOrdenPK;
    }

    public ProductovOrden(ProductovOrdenPK productovOrdenPK, int cantidad) {
        this.productovOrdenPK = productovOrdenPK;
        this.cantidad = cantidad;
    }

    public ProductovOrden(String productoVentapCod, String ordencodOrden) {
        this.productovOrdenPK = new ProductovOrdenPK(productoVentapCod, ordencodOrden);
    }

    public ProductovOrdenPK getProductovOrdenPK() {
        return productovOrdenPK;
    }

    public void setProductovOrdenPK(ProductovOrdenPK productovOrdenPK) {
        this.productovOrdenPK = productovOrdenPK;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public Integer getEnviadosacocina() {
        return enviadosacocina;
    }

    public void setEnviadosacocina(Integer enviadosacocina) {
        this.enviadosacocina = enviadosacocina;
    }

    public Integer getNumeroComensal() {
        return numeroComensal;
    }

    public void setNumeroComensal(Integer numeroComensal) {
        this.numeroComensal = numeroComensal;
    }

    public Nota getNota() {
        return nota;
    }

    public void setNota(Nota nota) {
        this.nota = nota;
    }

    public Orden getOrden() {
        return orden;
    }

    public void setOrden(Orden orden) {
        this.orden = orden;
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
        hash += (productovOrdenPK != null ? productovOrdenPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProductovOrden)) {
            return false;
        }
        ProductovOrden other = (ProductovOrden) object;
        if ((this.productovOrdenPK == null && other.productovOrdenPK != null) || (this.productovOrdenPK != null && !this.productovOrdenPK.equals(other.productovOrdenPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.restmanager.ProductovOrden[ productovOrdenPK=" + productovOrdenPK + " ]";
    }

}
