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
@Table(name = "nota_archivada")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "NotaArchivada.findAll", query = "SELECT n FROM NotaArchivada n")
    , @NamedQuery(name = "NotaArchivada.findByProductovOrdenArchivadoproductoVentapCod", query = "SELECT n FROM NotaArchivada n WHERE n.notaArchivadaPK.productovOrdenArchivadoproductoVentapCod = :productovOrdenArchivadoproductoVentapCod")
    , @NamedQuery(name = "NotaArchivada.findByProductovOrdenArchivadoordencodOrden", query = "SELECT n FROM NotaArchivada n WHERE n.notaArchivadaPK.productovOrdenArchivadoordencodOrden = :productovOrdenArchivadoordencodOrden")
    , @NamedQuery(name = "NotaArchivada.findByDescripcion", query = "SELECT n FROM NotaArchivada n WHERE n.descripcion = :descripcion")})
public class NotaArchivada implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected NotaArchivadaPK notaArchivadaPK;
    @Size(max = 255)
    @Column(name = "descripcion")
    private String descripcion;
    @JoinColumns({
        @JoinColumn(name = "productov_orden_archivadoproducto_ventap_cod", referencedColumnName = "producto_ventap_cod", insertable = false, updatable = false)
        , @JoinColumn(name = "productov_orden_archivadoordencod_orden", referencedColumnName = "ordencod_orden", insertable = false, updatable = false)})
    @OneToOne(optional = false)
    private ProductovOrdenArchivado productovOrdenArchivado;

    public NotaArchivada() {
    }

    public NotaArchivada(NotaArchivadaPK notaArchivadaPK) {
        this.notaArchivadaPK = notaArchivadaPK;
    }

    public NotaArchivada(String productovOrdenArchivadoproductoVentapCod, String productovOrdenArchivadoordencodOrden) {
        this.notaArchivadaPK = new NotaArchivadaPK(productovOrdenArchivadoproductoVentapCod, productovOrdenArchivadoordencodOrden);
    }

    public NotaArchivadaPK getNotaArchivadaPK() {
        return notaArchivadaPK;
    }

    public void setNotaArchivadaPK(NotaArchivadaPK notaArchivadaPK) {
        this.notaArchivadaPK = notaArchivadaPK;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public ProductovOrdenArchivado getProductovOrdenArchivado() {
        return productovOrdenArchivado;
    }

    public void setProductovOrdenArchivado(ProductovOrdenArchivado productovOrdenArchivado) {
        this.productovOrdenArchivado = productovOrdenArchivado;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (notaArchivadaPK != null ? notaArchivadaPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof NotaArchivada)) {
            return false;
        }
        NotaArchivada other = (NotaArchivada) object;
        if ((this.notaArchivadaPK == null && other.notaArchivadaPK != null) || (this.notaArchivadaPK != null && !this.notaArchivadaPK.equals(other.notaArchivadaPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.restmanager.NotaArchivada[ notaArchivadaPK=" + notaArchivadaPK + " ]";
    }

}
