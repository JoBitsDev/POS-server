/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.restmanager;

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
 * @author Jorge
 * 
 */
@Entity
@Table(name = "asistencia_personal")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AsistenciaPersonal.findAll", query = "SELECT a FROM AsistenciaPersonal a")
    , @NamedQuery(name = "AsistenciaPersonal.findByVentafecha", query = "SELECT a FROM AsistenciaPersonal a WHERE a.asistenciaPersonalPK.ventafecha = :ventafecha")
    , @NamedQuery(name = "AsistenciaPersonal.findByPersonalusuario", query = "SELECT a FROM AsistenciaPersonal a WHERE a.asistenciaPersonalPK.personalusuario = :personalusuario")
    , @NamedQuery(name = "AsistenciaPersonal.findByPago", query = "SELECT a FROM AsistenciaPersonal a WHERE a.pago = :pago")
    , @NamedQuery(name = "AsistenciaPersonal.findByAMayores", query = "SELECT a FROM AsistenciaPersonal a WHERE a.aMayores = :aMayores")
    , @NamedQuery(name = "AsistenciaPersonal.findByPropina", query = "SELECT a FROM AsistenciaPersonal a WHERE a.propina = :propina")})
public class AsistenciaPersonal implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected AsistenciaPersonalPK asistenciaPersonalPK;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "pago")
    private Float pago;
    @Column(name = "a_mayores")
    private Float aMayores;
    @Column(name = "propina")
    private Float propina;
    @JoinColumn(name = "personalusuario", referencedColumnName = "usuario", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Personal personal;
    @JoinColumn(name = "ventafecha", referencedColumnName = "fecha", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Venta venta;

    public AsistenciaPersonal() {
    }

    public AsistenciaPersonal(AsistenciaPersonalPK asistenciaPersonalPK) {
        this.asistenciaPersonalPK = asistenciaPersonalPK;
    }

    public AsistenciaPersonal(Date ventafecha, String personalusuario) {
        this.asistenciaPersonalPK = new AsistenciaPersonalPK(ventafecha, personalusuario);
    }

    public AsistenciaPersonalPK getAsistenciaPersonalPK() {
        return asistenciaPersonalPK;
    }

    public void setAsistenciaPersonalPK(AsistenciaPersonalPK asistenciaPersonalPK) {
        this.asistenciaPersonalPK = asistenciaPersonalPK;
    }

    public Float getPago() {
        return pago;
    }

    public void setPago(Float pago) {
        this.pago = pago;
    }

    public Float getAMayores() {
        return aMayores;
    }

    public void setAMayores(Float aMayores) {
        this.aMayores = aMayores;
    }

    public Float getPropina() {
        return propina;
    }

    public void setPropina(Float propina) {
        this.propina = propina;
    }

    public Personal getPersonal() {
        return personal;
    }

    public void setPersonal(Personal personal) {
        this.personal = personal;
    }

    public Venta getVenta() {
        return venta;
    }

    public void setVenta(Venta venta) {
        this.venta = venta;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (asistenciaPersonalPK != null ? asistenciaPersonalPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AsistenciaPersonal)) {
            return false;
        }
        AsistenciaPersonal other = (AsistenciaPersonal) object;
        if ((this.asistenciaPersonalPK == null && other.asistenciaPersonalPK != null) || (this.asistenciaPersonalPK != null && !this.asistenciaPersonalPK.equals(other.asistenciaPersonalPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.restmanager.AsistenciaPersonal[ asistenciaPersonalPK=" + asistenciaPersonalPK + " ]";
    }

}
