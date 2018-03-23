/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.restmanager;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Jorge
 */
@Entity
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Insumo.findAll", query = "SELECT i FROM Insumo i")
    , @NamedQuery(name = "Insumo.findByCodInsumo", query = "SELECT i FROM Insumo i WHERE i.codInsumo = :codInsumo")
    , @NamedQuery(name = "Insumo.findByNombre", query = "SELECT i FROM Insumo i WHERE i.nombre = :nombre")
    , @NamedQuery(name = "Insumo.findByUm", query = "SELECT i FROM Insumo i WHERE i.um = :um")
    , @NamedQuery(name = "Insumo.findByElaborado", query = "SELECT i FROM Insumo i WHERE i.elaborado = :elaborado")
    , @NamedQuery(name = "Insumo.findByCostoPorUnidad", query = "SELECT i FROM Insumo i WHERE i.costoPorUnidad = :costoPorUnidad")
    , @NamedQuery(name = "Insumo.findByCantidadExistente", query = "SELECT i FROM Insumo i WHERE i.cantidadExistente = :cantidadExistente")})
public class Insumo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 6)
    @Column(name = "cod_insumo")
    private String codInsumo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 60)
    private String nombre;
    @Size(max = 3)
    private String um;
    private Boolean elaborado;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "costo_por_unidad")
    private Float costoPorUnidad;
    @Column(name = "cantidad_existente")
    private Double cantidadExistente;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "insumo")
    private List<ProductoInsumo> productoInsumoList;
    @JoinColumn(name = "almacencod_almacen", referencedColumnName = "cod_almacen")
    @ManyToOne
    private Almacen almacencodAlmacen;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "insumo")
    private List<InsumoElaborado> insumoElaboradoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "insumo1")
    private List<InsumoElaborado> insumoElaboradoList1;

    public Insumo() {
    }

    public Insumo(String codInsumo) {
        this.codInsumo = codInsumo;
    }

    public Insumo(String codInsumo, String nombre) {
        this.codInsumo = codInsumo;
        this.nombre = nombre;
    }

    public String getCodInsumo() {
        return codInsumo;
    }

    public void setCodInsumo(String codInsumo) {
        this.codInsumo = codInsumo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUm() {
        return um;
    }

    public void setUm(String um) {
        this.um = um;
    }

    public Boolean getElaborado() {
        return elaborado;
    }

    public void setElaborado(Boolean elaborado) {
        this.elaborado = elaborado;
    }

    public Float getCostoPorUnidad() {
        return costoPorUnidad;
    }

    public void setCostoPorUnidad(Float costoPorUnidad) {
        this.costoPorUnidad = costoPorUnidad;
    }

    public Double getCantidadExistente() {
        return cantidadExistente;
    }

    public void setCantidadExistente(Double cantidadExistente) {
        this.cantidadExistente = cantidadExistente;
    }

    @XmlTransient
    public List<ProductoInsumo> getProductoInsumoList() {
        return productoInsumoList;
    }

    public void setProductoInsumoList(List<ProductoInsumo> productoInsumoList) {
        this.productoInsumoList = productoInsumoList;
    }

    public Almacen getAlmacencodAlmacen() {
        return almacencodAlmacen;
    }

    public void setAlmacencodAlmacen(Almacen almacencodAlmacen) {
        this.almacencodAlmacen = almacencodAlmacen;
    }

    @XmlTransient
    public List<InsumoElaborado> getInsumoElaboradoList() {
        return insumoElaboradoList;
    }

    public void setInsumoElaboradoList(List<InsumoElaborado> insumoElaboradoList) {
        this.insumoElaboradoList = insumoElaboradoList;
    }

    @XmlTransient
    public List<InsumoElaborado> getInsumoElaboradoList1() {
        return insumoElaboradoList1;
    }

    public void setInsumoElaboradoList1(List<InsumoElaborado> insumoElaboradoList1) {
        this.insumoElaboradoList1 = insumoElaboradoList1;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codInsumo != null ? codInsumo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Insumo)) {
            return false;
        }
        Insumo other = (Insumo) object;
        if ((this.codInsumo == null && other.codInsumo != null) || (this.codInsumo != null && !this.codInsumo.equals(other.codInsumo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.restmanager.Insumo[ codInsumo=" + codInsumo + " ]";
    }
    
}
