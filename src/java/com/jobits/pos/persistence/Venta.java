/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jobits.pos.persistence;

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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * FirstDream
 * @author Jorge
 *
 */
@Entity
@Table(name = "venta")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Venta.findAll", query = "SELECT v FROM Venta v")
    , @NamedQuery(name = "Venta.findByFecha", query = "SELECT v FROM Venta v WHERE v.fecha = :fecha")
    , @NamedQuery(name = "Venta.findByVentaTotal", query = "SELECT v FROM Venta v WHERE v.ventaTotal = :ventaTotal")
    , @NamedQuery(name = "Venta.findByVentagastosEninsumos", query = "SELECT v FROM Venta v WHERE v.ventagastosEninsumos = :ventagastosEninsumos")
    , @NamedQuery(name = "Venta.findByHoraPico", query = "SELECT v FROM Venta v WHERE v.horaPico = :horaPico")
    , @NamedQuery(name = "Venta.findByVentagastosPagotrabajadores", query = "SELECT v FROM Venta v WHERE v.ventagastosPagotrabajadores = :ventagastosPagotrabajadores")
    , @NamedQuery(name = "Venta.findByVentagastosGastos", query = "SELECT v FROM Venta v WHERE v.ventagastosGastos = :ventagastosGastos")
    , @NamedQuery(name = "Venta.findByCambioTurno1", query = "SELECT v FROM Venta v WHERE v.cambioTurno1 = :cambioTurno1")
    , @NamedQuery(name = "Venta.findByCambioTurno2", query = "SELECT v FROM Venta v WHERE v.cambioTurno2 = :cambioTurno2")
    , @NamedQuery(name = "Venta.findByVentapropina", query = "SELECT v FROM Venta v WHERE v.ventapropina = :ventapropina")})
public class Venta implements Serializable {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "venta1")
    private List<IpvVentaRegistro> ipvVentaRegistroList;

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
    @Column(name = "hora_pico")
    private Integer horaPico;
    @Column(name = "ventagastos_pagotrabajadores")
    private Float ventagastosPagotrabajadores;
    @Column(name = "ventagastos_gastos")
    private Float ventagastosGastos;
    @Size(max = 11)
    @Column(name = "cambio_turno1")
    private String cambioTurno1;
    @Size(max = 11)
    @Column(name = "cambio_turno2")
    private String cambioTurno2;
    @Column(name = "ventapropina")
    private Float ventapropina;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ventafecha")
    private List<Orden> ordenList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "venta")
    private List<GastoVenta> gastoVentaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "venta")
    private List<AsistenciaPersonal> asistenciaPersonalList;

    public Venta() {
    }

    public Venta(Date fecha) {
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

    public Integer getHoraPico() {
        return horaPico;
    }

    public void setHoraPico(Integer horaPico) {
        this.horaPico = horaPico;
    }

    public Float getVentagastosPagotrabajadores() {
        return ventagastosPagotrabajadores;
    }

    public void setVentagastosPagotrabajadores(Float ventagastosPagotrabajadores) {
        this.ventagastosPagotrabajadores = ventagastosPagotrabajadores;
    }

    public Float getVentagastosGastos() {
        return ventagastosGastos;
    }

    public void setVentagastosGastos(Float ventagastosGastos) {
        this.ventagastosGastos = ventagastosGastos;
    }

    public String getCambioTurno1() {
        return cambioTurno1;
    }

    public void setCambioTurno1(String cambioTurno1) {
        this.cambioTurno1 = cambioTurno1;
    }

    public String getCambioTurno2() {
        return cambioTurno2;
    }

    public void setCambioTurno2(String cambioTurno2) {
        this.cambioTurno2 = cambioTurno2;
    }

    public Float getVentapropina() {
        return ventapropina == null ? 0 : ventapropina;
    }

    public void setVentapropina(Float ventapropina) {
        this.ventapropina = ventapropina;
    }

    @XmlTransient
    public List<Orden> getOrdenList() {
        return ordenList;
    }

    public void setOrdenList(List<Orden> ordenList) {
        this.ordenList = ordenList;
    }

    @XmlTransient
    public List<GastoVenta> getGastoVentaList() {
        return gastoVentaList;
    }

    public void setGastoVentaList(List<GastoVenta> gastoVentaList) {
        this.gastoVentaList = gastoVentaList;
    }

    @XmlTransient
    public List<AsistenciaPersonal> getAsistenciaPersonalList() {
        return asistenciaPersonalList;
    }

    public void setAsistenciaPersonalList(List<AsistenciaPersonal> asistenciaPersonalList) {
        this.asistenciaPersonalList = asistenciaPersonalList;
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
        if (!(object instanceof Venta)) {
            return false;
        }
        Venta other = (Venta) object;
        if ((this.fecha == null && other.fecha != null) || (this.fecha != null && !this.fecha.equals(other.fecha))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.restmanager.Venta[ fecha=" + fecha + " ]";
    }

    @XmlTransient
    public List<IpvVentaRegistro> getIpvVentaRegistroList() {
        return ipvVentaRegistroList;
    }

    public void setIpvVentaRegistroList(List<IpvVentaRegistro> ipvVentaRegistroList) {
        this.ipvVentaRegistroList = ipvVentaRegistroList;
    }

}
