/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.restmanager;

import java.io.Serializable;
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
@Table(name = "nota")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Nota.findAll", query = "SELECT n FROM Nota n")
    , @NamedQuery(name = "Nota.findByProductovOrdenproductoVentapCod", query = "SELECT n FROM Nota n WHERE n.notaPK.productovOrdenproductoVentapCod = :productovOrdenproductoVentapCod")
    , @NamedQuery(name = "Nota.findByProductovOrdenordencodOrden", query = "SELECT n FROM Nota n WHERE n.notaPK.productovOrdenordencodOrden = :productovOrdenordencodOrden")
    , @NamedQuery(name = "Nota.findByDescripcion", query = "SELECT n FROM Nota n WHERE n.descripcion = :descripcion")})
public class Nota implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected NotaPK notaPK;
    @Size(max = 255)
    @Column(name = "descripcion")
    private String descripcion;
    @JoinColumns({
        @JoinColumn(name = "productov_ordenproducto_ventap_cod", referencedColumnName = "producto_ventap_cod", insertable = false, updatable = false)
        , @JoinColumn(name = "productov_ordenordencod_orden", referencedColumnName = "ordencod_orden", insertable = false, updatable = false)})
    @OneToOne(optional = false)
    private ProductovOrden productovOrden;

    public Nota() {
    }

    public Nota(NotaPK notaPK) {
        this.notaPK = notaPK;
    }

    public Nota(String productovOrdenproductoVentapCod, String productovOrdenordencodOrden) {
        this.notaPK = new NotaPK(productovOrdenproductoVentapCod, productovOrdenordencodOrden);
    }

    public NotaPK getNotaPK() {
        return notaPK;
    }

    public void setNotaPK(NotaPK notaPK) {
        this.notaPK = notaPK;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public ProductovOrden getProductovOrden() {
        return productovOrden;
    }

    public void setProductovOrden(ProductovOrden productovOrden) {
        this.productovOrden = productovOrden;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (notaPK != null ? notaPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Nota)) {
            return false;
        }
        Nota other = (Nota) object;
        if ((this.notaPK == null && other.notaPK != null) || (this.notaPK != null && !this.notaPK.equals(other.notaPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.restmanager.Nota[ notaPK=" + notaPK + " ]";
    }

}
