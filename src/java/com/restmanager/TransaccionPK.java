/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.restmanager;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * FirstDream
 * @author Jorge
 * 
 */
@Embeddable
public class TransaccionPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 6)
    @Column(name = "insumocod_insumo")
    private String insumocodInsumo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 4)
    @Column(name = "almacencod_almacen")
    private String almacencodAlmacen;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha")
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @Basic(optional = false)
    @NotNull
    @Column(name = "hora")
    @Temporal(TemporalType.TIME)
    private Date hora;

    public TransaccionPK() {
    }

    public TransaccionPK(String insumocodInsumo, String almacencodAlmacen, Date fecha, Date hora) {
        this.insumocodInsumo = insumocodInsumo;
        this.almacencodAlmacen = almacencodAlmacen;
        this.fecha = fecha;
        this.hora = hora;
    }

    public String getInsumocodInsumo() {
        return insumocodInsumo;
    }

    public void setInsumocodInsumo(String insumocodInsumo) {
        this.insumocodInsumo = insumocodInsumo;
    }

    public String getAlmacencodAlmacen() {
        return almacencodAlmacen;
    }

    public void setAlmacencodAlmacen(String almacencodAlmacen) {
        this.almacencodAlmacen = almacencodAlmacen;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Date getHora() {
        return hora;
    }

    public void setHora(Date hora) {
        this.hora = hora;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (insumocodInsumo != null ? insumocodInsumo.hashCode() : 0);
        hash += (almacencodAlmacen != null ? almacencodAlmacen.hashCode() : 0);
        hash += (fecha != null ? fecha.hashCode() : 0);
        hash += (hora != null ? hora.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TransaccionPK)) {
            return false;
        }
        TransaccionPK other = (TransaccionPK) object;
        if ((this.insumocodInsumo == null && other.insumocodInsumo != null) || (this.insumocodInsumo != null && !this.insumocodInsumo.equals(other.insumocodInsumo))) {
            return false;
        }
        if ((this.almacencodAlmacen == null && other.almacencodAlmacen != null) || (this.almacencodAlmacen != null && !this.almacencodAlmacen.equals(other.almacencodAlmacen))) {
            return false;
        }
        if ((this.fecha == null && other.fecha != null) || (this.fecha != null && !this.fecha.equals(other.fecha))) {
            return false;
        }
        if ((this.hora == null && other.hora != null) || (this.hora != null && !this.hora.equals(other.hora))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.restmanager.TransaccionPK[ insumocodInsumo=" + insumocodInsumo + ", almacencodAlmacen=" + almacencodAlmacen + ", fecha=" + fecha + ", hora=" + hora + " ]";
    }

}
