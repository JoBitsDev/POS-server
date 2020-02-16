/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jobits.pos.persistence;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
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
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "insumo_almacen")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "InsumoAlmacen.findAll", query = "SELECT i FROM InsumoAlmacen i")
    , @NamedQuery(name = "InsumoAlmacen.findByInsumocodInsumo", query = "SELECT i FROM InsumoAlmacen i WHERE i.insumoAlmacenPK.insumocodInsumo = :insumocodInsumo")
    ,@NamedQuery(name = "InsumoAlmacen.findByAlmacenInsumo", query = "SELECT i FROM InsumoAlmacen i "
            + "WHERE i.insumoAlmacenPK.almacencodAlmacen = :almacencodAlmacen AND i.insumoAlmacenPK.insumocodInsumo = :insumo")
    , @NamedQuery(name = "InsumoAlmacen.findByAlmacencodAlmacen", query = "SELECT i FROM InsumoAlmacen i WHERE i.insumoAlmacenPK.almacencodAlmacen = :almacencodAlmacen")
    , @NamedQuery(name = "InsumoAlmacen.findByCantidad", query = "SELECT i FROM InsumoAlmacen i WHERE i.cantidad = :cantidad")
    , @NamedQuery(name = "InsumoAlmacen.findByValorMonetario", query = "SELECT i FROM InsumoAlmacen i WHERE i.valorMonetario = :valorMonetario")})
public class InsumoAlmacen implements Serializable,Comparable<InsumoAlmacen> {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected InsumoAlmacenPK insumoAlmacenPK;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "cantidad")
    private Float cantidad;
    @Column(name = "valor_monetario")
    private Float valorMonetario;
    @JoinColumn(name = "almacencod_almacen", referencedColumnName = "cod_almacen", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    @JsonIgnore
    private Almacen almacen;
    @JoinColumn(name = "insumocod_insumo", referencedColumnName = "cod_insumo", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Insumo insumo;

    public InsumoAlmacen() {
    }

    public InsumoAlmacen(InsumoAlmacenPK insumoAlmacenPK) {
        this.insumoAlmacenPK = insumoAlmacenPK;
    }

    public InsumoAlmacen(String insumocodInsumo, String almacencodAlmacen) {
        this.insumoAlmacenPK = new InsumoAlmacenPK(insumocodInsumo, almacencodAlmacen);
    }

    public InsumoAlmacenPK getInsumoAlmacenPK() {
        return insumoAlmacenPK;
    }

    public void setInsumoAlmacenPK(InsumoAlmacenPK insumoAlmacenPK) {
        this.insumoAlmacenPK = insumoAlmacenPK;
    }

    public Float getCantidad() {
        return cantidad;
    }

    public void setCantidad(Float cantidad) {
        this.cantidad = cantidad;
    }

    public Float getValorMonetario() {
        return valorMonetario;
    }

    public void setValorMonetario(Float valorMonetario) {
        this.valorMonetario = valorMonetario;
    }

    public Almacen getAlmacen() {
        return almacen;
    }

    public void setAlmacen(Almacen almacen) {
        this.almacen = almacen;
    }

    public Insumo getInsumo() {
        return insumo;
    }

    public void setInsumo(Insumo insumo) {
        this.insumo = insumo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (insumoAlmacenPK != null ? insumoAlmacenPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof InsumoAlmacen)) {
            return false;
        }
        InsumoAlmacen other = (InsumoAlmacen) object;
        if ((this.insumoAlmacenPK == null && other.insumoAlmacenPK != null) || (this.insumoAlmacenPK != null && !this.insumoAlmacenPK.equals(other.insumoAlmacenPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return getInsumo().toString();
    }

    @Override
    public int compareTo(InsumoAlmacen o) {
        return getInsumo().getNombre().compareToIgnoreCase(o.getInsumo().getNombre());
    }

}
