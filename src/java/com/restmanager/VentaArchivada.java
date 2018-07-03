/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.restmanager;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * FirstDream
 * @author Jorge
 * 
 */
@Entity
@Table(name = "venta_archivada")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "VentaArchivada.findAll", query = "SELECT v FROM VentaArchivada v")
    , @NamedQuery(name = "VentaArchivada.findByFecha", query = "SELECT v FROM VentaArchivada v WHERE v.fecha = :fecha")
    , @NamedQuery(name = "VentaArchivada.findByVentaTotal", query = "SELECT v FROM VentaArchivada v WHERE v.ventaTotal = :ventaTotal")
    , @NamedQuery(name = "VentaArchivada.findByVentagastosEninsumos", query = "SELECT v FROM VentaArchivada v WHERE v.ventagastosEninsumos = :ventagastosEninsumos")})
public class VentaArchivada implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha")
    @Temporal(TemporalType.DATE)
    private Date fecha;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "venta_total")
    private Double ventaTotal;
    @Column(name = "ventagastos_eninsumos")
    private Double ventagastosEninsumos;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ventafecha")
    private List<OrdenArchivada> ordenArchivadaList;

    public VentaArchivada() {
    }

    public VentaArchivada(Date fecha) {
        this.fecha = fecha;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Double getVentaTotal() {
        return ventaTotal;
    }

    public void setVentaTotal(Double ventaTotal) {
        this.ventaTotal = ventaTotal;
    }

    public Double getVentagastosEninsumos() {
        return ventagastosEninsumos;
    }

    public void setVentagastosEninsumos(Double ventagastosEninsumos) {
        this.ventagastosEninsumos = ventagastosEninsumos;
    }

    @XmlTransient
    public List<OrdenArchivada> getOrdenArchivadaList() {
        return ordenArchivadaList;
    }

    public void setOrdenArchivadaList(List<OrdenArchivada> ordenArchivadaList) {
        this.ordenArchivadaList = ordenArchivadaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (fecha != null ? fecha.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof VentaArchivada)) {
            return false;
        }
        VentaArchivada other = (VentaArchivada) object;
        if ((this.fecha == null && other.fecha != null) || (this.fecha != null && !this.fecha.equals(other.fecha))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.restmanager.VentaArchivada[ fecha=" + fecha + " ]";
    }

}
