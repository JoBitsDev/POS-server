/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jobits.pos.persistence;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * FirstDream
 *
 * @author Jorge
 *
 */
@Entity
@Table(name = "insumo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Insumo.findAll", query = "SELECT i FROM Insumo i")
    , @NamedQuery(name = "Insumo.findByCodInsumo", query = "SELECT i FROM Insumo i WHERE i.codInsumo = :codInsumo")
    , @NamedQuery(name = "Insumo.findByNombre", query = "SELECT i FROM Insumo i WHERE i.nombre = :nombre")
    , @NamedQuery(name = "Insumo.findByUm", query = "SELECT i FROM Insumo i WHERE i.um = :um")
    , @NamedQuery(name = "Insumo.findByElaborado", query = "SELECT i FROM Insumo i WHERE i.elaborado = :elaborado")
    , @NamedQuery(name = "Insumo.findByCostoPorUnidad", query = "SELECT i FROM Insumo i WHERE i.costoPorUnidad = :costoPorUnidad")
    , @NamedQuery(name = "Insumo.findByStockEstimation", query = "SELECT i FROM Insumo i WHERE i.stockEstimation = :stockEstimation")
    , @NamedQuery(name = "Insumo.findByCantidadCreada", query = "SELECT i FROM Insumo i WHERE i.cantidadCreada = :cantidadCreada")})
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
    @Column(name = "nombre")
    private String nombre;
    @Size(max = 3)
    @Column(name = "um")
    private String um;
    @Column(name = "elaborado")
    @JsonIgnore
    private Boolean elaborado;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "costo_por_unidad")
    private Float costoPorUnidad;
    @Column(name = "stock_estimation")
    private Float stockEstimation;
    @Column(name = "cantidad_creada")
    @JsonIgnore
    private Float cantidadCreada;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "insumo")
    @JsonIgnore
    private List<ProductoInsumo> productoInsumoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "insumocodInsumo")
    @JsonIgnore
    private List<Transaccion> transaccionList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "insumo")
    @JsonIgnore
    private List<InsumoAlmacen> insumoAlmacenList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "insumo")
    @JsonIgnore
    private List<Ipv> ipvList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "insumo")
    @JsonIgnore
    private List<TransaccionTransformacion> transaccionTransformacionList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "derivante")
    @JsonIgnore
    private List<InsumoElaborado> productosDerivados;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "insumo")
    @JsonIgnore
    private List<InsumoElaborado> productosDerivantes;

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

    public Float getStockEstimation() {
        return stockEstimation;
    }

    public void setStockEstimation(Float stockEstimation) {
        this.stockEstimation = stockEstimation;
    }

    public Float getCantidadCreada() {
        return cantidadCreada;
    }

    public void setCantidadCreada(Float cantidadCreada) {
        this.cantidadCreada = cantidadCreada;
    }

    @XmlTransient
    public List<ProductoInsumo> getProductoInsumoList() {
        return productoInsumoList;
    }

    public void setProductoInsumoList(List<ProductoInsumo> productoInsumoList) {
        this.productoInsumoList = productoInsumoList;
    }

    @XmlTransient
    public List<Transaccion> getTransaccionList() {
        return transaccionList;
    }

    public void setTransaccionList(List<Transaccion> transaccionList) {
        this.transaccionList = transaccionList;
    }

    @XmlTransient
    public List<InsumoAlmacen> getInsumoAlmacenList() {
        return insumoAlmacenList;
    }

    public void setInsumoAlmacenList(List<InsumoAlmacen> insumoAlmacenList) {
        this.insumoAlmacenList = insumoAlmacenList;
    }

    @XmlTransient
    public List<Ipv> getIpvList() {
        return ipvList;
    }

    public void setIpvList(List<Ipv> ipvList) {
        this.ipvList = ipvList;
    }

    @XmlTransient
    public List<TransaccionTransformacion> getTransaccionTransformacionList() {
        return transaccionTransformacionList;
    }

    public void setTransaccionTransformacionList(List<TransaccionTransformacion> transaccionTransformacionList) {
        this.transaccionTransformacionList = transaccionTransformacionList;
    }

    @XmlTransient
    public List<InsumoElaborado> getProductosDerivados() {
        return productosDerivados;
    }

    public void setProductosDerivados(List<InsumoElaborado> productosDerivados) {
        this.productosDerivados = productosDerivados;
    }

    @XmlTransient
    public List<InsumoElaborado> getProductosDerivantes() {
        return productosDerivantes;
    }

    public void setProductosDerivantes(List<InsumoElaborado> productosDerivantes) {
        this.productosDerivantes = productosDerivantes;
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
        return "("+codInsumo +")" +"("+um +")" + nombre;
    }

}
