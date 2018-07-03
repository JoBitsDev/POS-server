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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * FirstDream
 * @author Jorge
 * 
 */
@Entity
@Table(name = "sector")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Sector.findAll", query = "SELECT s FROM Sector s")
    , @NamedQuery(name = "Sector.findByCodSector", query = "SELECT s FROM Sector s WHERE s.codSector = :codSector")
    , @NamedQuery(name = "Sector.findByNombre", query = "SELECT s FROM Sector s WHERE s.nombre = :nombre")
    , @NamedQuery(name = "Sector.findByCantidadUsos", query = "SELECT s FROM Sector s WHERE s.cantidadUsos = :cantidadUsos")})
public class Sector implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "cod_sector")
    private Integer codSector;
    @Size(max = 50)
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "cantidad_usos")
    private Integer cantidadUsos;
    @ManyToMany(mappedBy = "sectorList")
    private List<ProductoVenta> productoVentaList;
    @JoinColumn(name = "menu_especialcod_menu_especial", referencedColumnName = "cod_menu_especial")
    @ManyToOne(optional = false)
    private MenuEspecial menuEspecialcodMenuEspecial;

    public Sector() {
    }

    public Sector(Integer codSector) {
        this.codSector = codSector;
    }

    public Integer getCodSector() {
        return codSector;
    }

    public void setCodSector(Integer codSector) {
        this.codSector = codSector;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getCantidadUsos() {
        return cantidadUsos;
    }

    public void setCantidadUsos(Integer cantidadUsos) {
        this.cantidadUsos = cantidadUsos;
    }

    @XmlTransient
    public List<ProductoVenta> getProductoVentaList() {
        return productoVentaList;
    }

    public void setProductoVentaList(List<ProductoVenta> productoVentaList) {
        this.productoVentaList = productoVentaList;
    }

    public MenuEspecial getMenuEspecialcodMenuEspecial() {
        return menuEspecialcodMenuEspecial;
    }

    public void setMenuEspecialcodMenuEspecial(MenuEspecial menuEspecialcodMenuEspecial) {
        this.menuEspecialcodMenuEspecial = menuEspecialcodMenuEspecial;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codSector != null ? codSector.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Sector)) {
            return false;
        }
        Sector other = (Sector) object;
        if ((this.codSector == null && other.codSector != null) || (this.codSector != null && !this.codSector.equals(other.codSector))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.restmanager.Sector[ codSector=" + codSector + " ]";
    }

}
