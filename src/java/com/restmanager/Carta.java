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
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
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
@Table(name = "carta")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Carta.findAll", query = "SELECT c FROM Carta c")
    , @NamedQuery(name = "Carta.findByCodCarta", query = "SELECT c FROM Carta c WHERE c.codCarta = :codCarta")
    , @NamedQuery(name = "Carta.findByNombreCarta", query = "SELECT c FROM Carta c WHERE c.nombreCarta = :nombreCarta")
    , @NamedQuery(name = "Carta.findByMonedaPrincipal", query = "SELECT c FROM Carta c WHERE c.monedaPrincipal = :monedaPrincipal")})
public class Carta implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 5)
    @Column(name = "cod_carta")
    private String codCarta;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "nombre_carta")
    private String nombreCarta;
    @Size(max = 3)
    @Column(name = "moneda_principal")
    private String monedaPrincipal;
    @JoinTable(name = "carta_area", joinColumns = {
        @JoinColumn(name = "cartacod_carta", referencedColumnName = "cod_carta")}, inverseJoinColumns = {
        @JoinColumn(name = "areacod_area", referencedColumnName = "cod_area")})
    @ManyToMany
    private List<Area> areaList;

    public Carta() {
    }

    public Carta(String codCarta) {
        this.codCarta = codCarta;
    }

    public Carta(String codCarta, String nombreCarta) {
        this.codCarta = codCarta;
        this.nombreCarta = nombreCarta;
    }

    public String getCodCarta() {
        return codCarta;
    }

    public void setCodCarta(String codCarta) {
        this.codCarta = codCarta;
    }

    public String getNombreCarta() {
        return nombreCarta;
    }

    public void setNombreCarta(String nombreCarta) {
        this.nombreCarta = nombreCarta;
    }

    public String getMonedaPrincipal() {
        return monedaPrincipal;
    }

    public void setMonedaPrincipal(String monedaPrincipal) {
        this.monedaPrincipal = monedaPrincipal;
    }

    @XmlTransient
    public List<Area> getAreaList() {
        return areaList;
    }

    public void setAreaList(List<Area> areaList) {
        this.areaList = areaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codCarta != null ? codCarta.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Carta)) {
            return false;
        }
        Carta other = (Carta) object;
        if ((this.codCarta == null && other.codCarta != null) || (this.codCarta != null && !this.codCarta.equals(other.codCarta))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.restmanager.Carta[ codCarta=" + codCarta + " ]";
    }

}
