/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.restmanager;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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
    @NamedQuery(name = "Seccion.findAll", query = "SELECT s FROM Seccion s")
    , @NamedQuery(name = "Seccion.findByNombreSeccion", query = "SELECT s FROM Seccion s WHERE s.nombreSeccion = :nombreSeccion")
    , @NamedQuery(name = "Seccion.findByDescripcion", query = "SELECT s FROM Seccion s WHERE s.descripcion = :descripcion")
    , @NamedQuery(name = "Seccion.findByCartacodCarta", query = "SELECT s FROM Seccion s WHERE s.cartacodCarta = :cartacodCarta")})
public class Seccion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "nombre_seccion")
    private String nombreSeccion;
    @Size(max = 255)
    private String descripcion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 8)
    @Column(name = "cartacod_carta")
    private String cartacodCarta;
    @OneToMany(mappedBy = "seccionnombreSeccion")
    private List<ProductoVenta> productoVentaList;

    public Seccion() {
    }

    public Seccion(String nombreSeccion) {
        this.nombreSeccion = nombreSeccion;
    }

    public Seccion(String nombreSeccion, String cartacodCarta) {
        this.nombreSeccion = nombreSeccion;
        this.cartacodCarta = cartacodCarta;
    }

    public String getNombreSeccion() {
        return nombreSeccion;
    }

    public void setNombreSeccion(String nombreSeccion) {
        this.nombreSeccion = nombreSeccion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCartacodCarta() {
        return cartacodCarta;
    }

    public void setCartacodCarta(String cartacodCarta) {
        this.cartacodCarta = cartacodCarta;
    }

    @XmlTransient
    public List<ProductoVenta> getProductoVentaList() {
        return productoVentaList;
    }

    public void setProductoVentaList(List<ProductoVenta> productoVentaList) {
        this.productoVentaList = productoVentaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (nombreSeccion != null ? nombreSeccion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Seccion)) {
            return false;
        }
        Seccion other = (Seccion) object;
        if ((this.nombreSeccion == null && other.nombreSeccion != null) || (this.nombreSeccion != null && !this.nombreSeccion.equals(other.nombreSeccion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.restmanager.Seccion[ nombreSeccion=" + nombreSeccion + " ]";
    }
    
}
