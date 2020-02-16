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
 *
 * @author Jorge
 *
 */
@Entity
@Table(name = "insumo_elaborado")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "InsumoElaborado.findAll", query = "SELECT i FROM InsumoElaborado i")
    , @NamedQuery(name = "InsumoElaborado.findByInsumocodNombre", query = "SELECT i FROM InsumoElaborado i WHERE i.insumoElaboradoPK.insumocodNombre = :insumocodNombre")
    , @NamedQuery(name = "InsumoElaborado.findByInsumocodInsumo", query = "SELECT i FROM InsumoElaborado i WHERE i.insumoElaboradoPK.insumocodInsumo = :insumocodInsumo")
    , @NamedQuery(name = "InsumoElaborado.findByCantidad", query = "SELECT i FROM InsumoElaborado i WHERE i.cantidad = :cantidad")
    , @NamedQuery(name = "InsumoElaborado.findByCosto", query = "SELECT i FROM InsumoElaborado i WHERE i.costo = :costo")})
public class InsumoElaborado implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected InsumoElaboradoPK insumoElaboradoPK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cantidad")
    private float cantidad;
    @Basic(optional = false)
    @NotNull
    @Column(name = "costo")
    private float costo;
    @JoinColumn(name = "insumocod_nombre", referencedColumnName = "cod_insumo", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Insumo derivante;
    @JoinColumn(name = "insumocod_insumo", referencedColumnName = "cod_insumo", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Insumo insumo;

    public InsumoElaborado() {
    }

    public InsumoElaborado(InsumoElaboradoPK insumoElaboradoPK) {
        this.insumoElaboradoPK = insumoElaboradoPK;
    }

    public InsumoElaborado(InsumoElaboradoPK insumoElaboradoPK, float cantidad, float costo) {
        this.insumoElaboradoPK = insumoElaboradoPK;
        this.cantidad = cantidad;
        this.costo = costo;
    }

    public InsumoElaborado(String insumocodNombre, String insumocodInsumo) {
        this.insumoElaboradoPK = new InsumoElaboradoPK(insumocodNombre, insumocodInsumo);
    }

    public InsumoElaboradoPK getInsumoElaboradoPK() {
        return insumoElaboradoPK;
    }

    public void setInsumoElaboradoPK(InsumoElaboradoPK insumoElaboradoPK) {
        this.insumoElaboradoPK = insumoElaboradoPK;
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

    public Insumo getDerivante() {
        return derivante;
    }

    public void setDerivante(Insumo derivante) {
        this.derivante = derivante;
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
        hash += (insumoElaboradoPK != null ? insumoElaboradoPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Insumo)) {
            return false;
        }
        Insumo other = (Insumo) object;
        return getInsumo().equals(other);
    }

    @Override
    public String toString() {
        return "com.restmanager.InsumoElaborado[ insumoElaboradoPK=" + insumoElaboradoPK + " ]";
    }

}
