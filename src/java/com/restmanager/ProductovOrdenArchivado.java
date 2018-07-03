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
@Table(name = "productov_orden_archivado")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ProductovOrdenArchivado.findAll", query = "SELECT p FROM ProductovOrdenArchivado p")
    , @NamedQuery(name = "ProductovOrdenArchivado.findByProductoVentapCod", query = "SELECT p FROM ProductovOrdenArchivado p WHERE p.productovOrdenArchivadoPK.productoVentapCod = :productoVentapCod")
    , @NamedQuery(name = "ProductovOrdenArchivado.findByOrdencodOrden", query = "SELECT p FROM ProductovOrdenArchivado p WHERE p.productovOrdenArchivadoPK.ordencodOrden = :ordencodOrden")
    , @NamedQuery(name = "ProductovOrdenArchivado.findByCantidad", query = "SELECT p FROM ProductovOrdenArchivado p WHERE p.cantidad = :cantidad")
    , @NamedQuery(name = "ProductovOrdenArchivado.findByEnviadosacocina", query = "SELECT p FROM ProductovOrdenArchivado p WHERE p.enviadosacocina = :enviadosacocina")
    , @NamedQuery(name = "ProductovOrdenArchivado.findByNumeroComensal", query = "SELECT p FROM ProductovOrdenArchivado p WHERE p.numeroComensal = :numeroComensal")})
public class ProductovOrdenArchivado implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ProductovOrdenArchivadoPK productovOrdenArchivadoPK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cantidad")
    private int cantidad;
    @Column(name = "enviadosacocina")
    private Integer enviadosacocina;
    @Column(name = "numero_comensal")
    private Integer numeroComensal;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "productovOrdenArchivado")
    private NotaArchivada notaArchivada;
    @JoinColumn(name = "ordencod_orden", referencedColumnName = "cod_orden", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private OrdenArchivada ordenArchivada;
    @JoinColumn(name = "producto_ventap_cod", referencedColumnName = "p_cod", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private ProductoVenta productoVenta;

    public ProductovOrdenArchivado() {
    }

    public ProductovOrdenArchivado(ProductovOrdenArchivadoPK productovOrdenArchivadoPK) {
        this.productovOrdenArchivadoPK = productovOrdenArchivadoPK;
    }

    public ProductovOrdenArchivado(ProductovOrdenArchivadoPK productovOrdenArchivadoPK, int cantidad) {
        this.productovOrdenArchivadoPK = productovOrdenArchivadoPK;
        this.cantidad = cantidad;
    }

    public ProductovOrdenArchivado(String productoVentapCod, String ordencodOrden) {
        this.productovOrdenArchivadoPK = new ProductovOrdenArchivadoPK(productoVentapCod, ordencodOrden);
    }

    public ProductovOrdenArchivadoPK getProductovOrdenArchivadoPK() {
        return productovOrdenArchivadoPK;
    }

    public void setProductovOrdenArchivadoPK(ProductovOrdenArchivadoPK productovOrdenArchivadoPK) {
        this.productovOrdenArchivadoPK = productovOrdenArchivadoPK;
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

    public NotaArchivada getNotaArchivada() {
        return notaArchivada;
    }

    public void setNotaArchivada(NotaArchivada notaArchivada) {
        this.notaArchivada = notaArchivada;
    }

    public OrdenArchivada getOrdenArchivada() {
        return ordenArchivada;
    }

    public void setOrdenArchivada(OrdenArchivada ordenArchivada) {
        this.ordenArchivada = ordenArchivada;
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
        hash += (productovOrdenArchivadoPK != null ? productovOrdenArchivadoPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProductovOrdenArchivado)) {
            return false;
        }
        ProductovOrdenArchivado other = (ProductovOrdenArchivado) object;
        if ((this.productovOrdenArchivadoPK == null && other.productovOrdenArchivadoPK != null) || (this.productovOrdenArchivadoPK != null && !this.productovOrdenArchivadoPK.equals(other.productovOrdenArchivadoPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.restmanager.ProductovOrdenArchivado[ productovOrdenArchivadoPK=" + productovOrdenArchivadoPK + " ]";
    }

}
