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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
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
@Table(name = "menu_especial")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MenuEspecial.findAll", query = "SELECT m FROM MenuEspecial m")
    , @NamedQuery(name = "MenuEspecial.findByCodMenuEspecial", query = "SELECT m FROM MenuEspecial m WHERE m.codMenuEspecial = :codMenuEspecial")
    , @NamedQuery(name = "MenuEspecial.findByNombre", query = "SELECT m FROM MenuEspecial m WHERE m.nombre = :nombre")
    , @NamedQuery(name = "MenuEspecial.findByPrecio", query = "SELECT m FROM MenuEspecial m WHERE m.precio = :precio")
    , @NamedQuery(name = "MenuEspecial.findByVisible", query = "SELECT m FROM MenuEspecial m WHERE m.visible = :visible")})
public class MenuEspecial implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "cod_menu_especial")
    private Integer codMenuEspecial;
    @Size(max = 50)
    @Column(name = "nombre")
    private String nombre;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "precio")
    private Float precio;
    @Column(name = "visible")
    private Boolean visible;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "menuEspecialcodMenuEspecial")
    private List<Sector> sectorList;

    public MenuEspecial() {
    }

    public MenuEspecial(Integer codMenuEspecial) {
        this.codMenuEspecial = codMenuEspecial;
    }

    public Integer getCodMenuEspecial() {
        return codMenuEspecial;
    }

    public void setCodMenuEspecial(Integer codMenuEspecial) {
        this.codMenuEspecial = codMenuEspecial;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Float getPrecio() {
        return precio;
    }

    public void setPrecio(Float precio) {
        this.precio = precio;
    }

    public Boolean getVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    @XmlTransient
    public List<Sector> getSectorList() {
        return sectorList;
    }

    public void setSectorList(List<Sector> sectorList) {
        this.sectorList = sectorList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codMenuEspecial != null ? codMenuEspecial.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MenuEspecial)) {
            return false;
        }
        MenuEspecial other = (MenuEspecial) object;
        if ((this.codMenuEspecial == null && other.codMenuEspecial != null) || (this.codMenuEspecial != null && !this.codMenuEspecial.equals(other.codMenuEspecial))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.restmanager.MenuEspecial[ codMenuEspecial=" + codMenuEspecial + " ]";
    }

}
