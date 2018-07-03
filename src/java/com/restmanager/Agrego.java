/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.restmanager;

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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * FirstDream
 * @author Jorge
 * 
 */
@Entity
@Table(name = "agrego")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Agrego.findAll", query = "SELECT a FROM Agrego a")
    , @NamedQuery(name = "Agrego.findByCodAgrego", query = "SELECT a FROM Agrego a WHERE a.agregoPK.codAgrego = :codAgrego")
    , @NamedQuery(name = "Agrego.findByProductoVentapCod", query = "SELECT a FROM Agrego a WHERE a.agregoPK.productoVentapCod = :productoVentapCod")
    , @NamedQuery(name = "Agrego.findByProductoVentapCodAgrego", query = "SELECT a FROM Agrego a WHERE a.productoVentapCodAgrego = :productoVentapCodAgrego")
    , @NamedQuery(name = "Agrego.findByCantidad", query = "SELECT a FROM Agrego a WHERE a.cantidad = :cantidad")})
public class Agrego implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected AgregoPK agregoPK;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 7)
    @Column(name = "producto_ventap_cod_agrego")
    private String productoVentapCodAgrego;
    @Column(name = "cantidad")
    private Integer cantidad;
    @JoinColumn(name = "producto_ventap_cod", referencedColumnName = "p_cod", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private ProductoVenta productoVenta;

    public Agrego() {
    }

    public Agrego(AgregoPK agregoPK) {
        this.agregoPK = agregoPK;
    }

    public Agrego(AgregoPK agregoPK, String productoVentapCodAgrego) {
        this.agregoPK = agregoPK;
        this.productoVentapCodAgrego = productoVentapCodAgrego;
    }

    public Agrego(int codAgrego, String productoVentapCod) {
        this.agregoPK = new AgregoPK(codAgrego, productoVentapCod);
    }

    public AgregoPK getAgregoPK() {
        return agregoPK;
    }

    public void setAgregoPK(AgregoPK agregoPK) {
        this.agregoPK = agregoPK;
    }

    public String getProductoVentapCodAgrego() {
        return productoVentapCodAgrego;
    }

    public void setProductoVentapCodAgrego(String productoVentapCodAgrego) {
        this.productoVentapCodAgrego = productoVentapCodAgrego;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
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
        hash += (agregoPK != null ? agregoPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Agrego)) {
            return false;
        }
        Agrego other = (Agrego) object;
        if ((this.agregoPK == null && other.agregoPK != null) || (this.agregoPK != null && !this.agregoPK.equals(other.agregoPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.restmanager.Agrego[ agregoPK=" + agregoPK + " ]";
    }

}
