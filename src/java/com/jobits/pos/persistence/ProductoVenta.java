/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jobits.pos.persistence;

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
import javax.persistence.Table;
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
@Table(name = "producto_venta")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ProductoVenta.findAll", query = "SELECT p FROM ProductoVenta p")
    , @NamedQuery(name = "ProductoVenta.findByPCod", query = "SELECT p FROM ProductoVenta p WHERE p.pCod = :pCod")
    , @NamedQuery(name = "ProductoVenta.findByNombre", query = "SELECT p FROM ProductoVenta p WHERE p.nombre = :nombre")
    , @NamedQuery(name = "ProductoVenta.findByPrecioVenta", query = "SELECT p FROM ProductoVenta p WHERE p.precioVenta = :precioVenta")
    , @NamedQuery(name = "ProductoVenta.findByGanancia", query = "SELECT p FROM ProductoVenta p WHERE p.ganancia = :ganancia")
    , @NamedQuery(name = "ProductoVenta.findByGasto", query = "SELECT p FROM ProductoVenta p WHERE p.gasto = :gasto")
    , @NamedQuery(name = "ProductoVenta.findByDescripcion", query = "SELECT p FROM ProductoVenta p WHERE p.descripcion = :descripcion")
    , @NamedQuery(name = "ProductoVenta.findByVisible", query = "SELECT p FROM ProductoVenta p WHERE p.visible = :visible")
    , @NamedQuery(name = "ProductoVenta.findByPagoPorVenta", query = "SELECT p FROM ProductoVenta p WHERE p.pagoPorVenta = :pagoPorVenta")})
public class ProductoVenta implements Serializable {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "productoVenta")
    private List<IpvVentaRegistro> ipvVentaRegistroList;

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 7)
    @Column(name = "p_cod")
    private String pCod;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @NotNull
    @Column(name = "precio_venta")
    private float precioVenta;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "ganancia")
    private Float ganancia;
    @Column(name = "gasto")
    private Float gasto;
    @Size(max = 255)
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "visible")
    private Boolean visible;
    @Column(name = "pago_por_venta")
    private Float pagoPorVenta;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "productoVenta")
    private List<ProductoInsumo> productoInsumoList;
    @JoinColumn(name = "cocinacod_cocina", referencedColumnName = "cod_cocina")
    @ManyToOne
    private Cocina cocinacodCocina;
    @JoinColumn(name = "seccionnombre_seccion", referencedColumnName = "nombre_seccion")
    @ManyToOne
    private Seccion seccionnombreSeccion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "productoVenta")
    private List<ProductovOrden> productovOrdenList;

    public ProductoVenta() {
    }

    public ProductoVenta(String pCod) {
        this.pCod = pCod;
    }

    public ProductoVenta(String pCod, String nombre, float precioVenta) {
        this.pCod = pCod;
        this.nombre = nombre;
        this.precioVenta = precioVenta;
    }

    public String getPCod() {
        return pCod;
    }

    public void setPCod(String pCod) {
        this.pCod = pCod;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public float getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(float precioVenta) {
        this.precioVenta = precioVenta;
    }

    public Float getGanancia() {
        return ganancia;
    }

    public void setGanancia(Float ganancia) {
        this.ganancia = ganancia;
    }

    public Float getGasto() {
        return gasto;
    }

    public void setGasto(Float gasto) {
        this.gasto = gasto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Boolean getVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    public Float getPagoPorVenta() {
        return pagoPorVenta;
    }

    public void setPagoPorVenta(Float pagoPorVenta) {
        this.pagoPorVenta = pagoPorVenta;
    }

    @XmlTransient
    public List<ProductoInsumo> getProductoInsumoList() {
        return productoInsumoList;
    }

    public void setProductoInsumoList(List<ProductoInsumo> productoInsumoList) {
        this.productoInsumoList = productoInsumoList;
    }

    public Cocina getCocinacodCocina() {
        return cocinacodCocina;
    }

    public void setCocinacodCocina(Cocina cocinacodCocina) {
        this.cocinacodCocina = cocinacodCocina;
    }

    public Seccion getSeccionnombreSeccion() {
        return seccionnombreSeccion;
    }

    public void setSeccionnombreSeccion(Seccion seccionnombreSeccion) {
        this.seccionnombreSeccion = seccionnombreSeccion;
    }

    @XmlTransient
    public List<ProductovOrden> getProductovOrdenList() {
        return productovOrdenList;
    }

    public void setProductovOrdenList(List<ProductovOrden> productovOrdenList) {
        this.productovOrdenList = productovOrdenList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (pCod != null ? pCod.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProductoVenta)) {
            return false;
        }
        ProductoVenta other = (ProductoVenta) object;
        if ((this.pCod == null && other.pCod != null) || (this.pCod != null && !this.pCod.equals(other.pCod))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.restmanager.ProductoVenta[ pCod=" + pCod + " ]";
    }

    @XmlTransient
    public List<IpvVentaRegistro> getIpvVentaRegistroList() {
        return ipvVentaRegistroList;
    }

    public void setIpvVentaRegistroList(List<IpvVentaRegistro> ipvVentaRegistroList) {
        this.ipvVentaRegistroList = ipvVentaRegistroList;
    }

}
