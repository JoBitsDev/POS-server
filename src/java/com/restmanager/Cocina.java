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
    @NamedQuery(name = "Cocina.findAll", query = "SELECT c FROM Cocina c")
    , @NamedQuery(name = "Cocina.findByCodCocina", query = "SELECT c FROM Cocina c WHERE c.codCocina = :codCocina")
    , @NamedQuery(name = "Cocina.findByNombreCocina", query = "SELECT c FROM Cocina c WHERE c.nombreCocina = :nombreCocina")})
public class Cocina implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 5)
    @Column(name = "cod_cocina")
    private String codCocina;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "nombre_cocina")
    private String nombreCocina;
    @OneToMany(mappedBy = "cocinacodCocina")
    private List<ProductoVenta> productoVentaList;

    public Cocina() {
    }

    public Cocina(String codCocina) {
        this.codCocina = codCocina;
    }

    public Cocina(String codCocina, String nombreCocina) {
        this.codCocina = codCocina;
        this.nombreCocina = nombreCocina;
    }

    public String getCodCocina() {
        return codCocina;
    }

    public void setCodCocina(String codCocina) {
        this.codCocina = codCocina;
    }

    public String getNombreCocina() {
        return nombreCocina;
    }

    public void setNombreCocina(String nombreCocina) {
        this.nombreCocina = nombreCocina;
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
        hash += (codCocina != null ? codCocina.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Cocina)) {
            return false;
        }
        Cocina other = (Cocina) object;
        if ((this.codCocina == null && other.codCocina != null) || (this.codCocina != null && !this.codCocina.equals(other.codCocina))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.restmanager.Cocina[ codCocina=" + codCocina + " ]";
    }
    
}
