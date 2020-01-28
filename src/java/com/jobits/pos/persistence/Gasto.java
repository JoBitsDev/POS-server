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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "gasto")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Gasto.findAll", query = "SELECT g FROM Gasto g")
    , @NamedQuery(name = "Gasto.findByCodGasto", query = "SELECT g FROM Gasto g WHERE g.codGasto = :codGasto")
    , @NamedQuery(name = "Gasto.findByNombre", query = "SELECT g FROM Gasto g WHERE g.nombre = :nombre")
    , @NamedQuery(name = "Gasto.findByFrecuenciaPago", query = "SELECT g FROM Gasto g WHERE g.frecuenciaPago = :frecuenciaPago")
    , @NamedQuery(name = "Gasto.findByUltimoPago", query = "SELECT g FROM Gasto g WHERE g.ultimoPago = :ultimoPago")})
public class Gasto implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "cod_gasto")
    private String codGasto;
    @Size(max = 100)
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "frecuencia_pago")
    private Integer frecuenciaPago;
    @Column(name = "ultimo_pago")
    @Temporal(TemporalType.DATE)
    private Date ultimoPago;
    @JoinColumn(name = "tipo_gastoid_gasto", referencedColumnName = "id_gasto")
    @ManyToOne(optional = false)
    private TipoGasto tipoGastoidGasto;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "gasto")
    private List<GastoVenta> gastoVentaList;

    public Gasto() {
    }

    public Gasto(String codGasto) {
        this.codGasto = codGasto;
    }

    public String getCodGasto() {
        return codGasto;
    }

    public void setCodGasto(String codGasto) {
        this.codGasto = codGasto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getFrecuenciaPago() {
        return frecuenciaPago;
    }

    public void setFrecuenciaPago(Integer frecuenciaPago) {
        this.frecuenciaPago = frecuenciaPago;
    }

    public Date getUltimoPago() {
        return ultimoPago;
    }

    public void setUltimoPago(Date ultimoPago) {
        this.ultimoPago = ultimoPago;
    }

    public TipoGasto getTipoGastoidGasto() {
        return tipoGastoidGasto;
    }

    public void setTipoGastoidGasto(TipoGasto tipoGastoidGasto) {
        this.tipoGastoidGasto = tipoGastoidGasto;
    }

    @XmlTransient
    public List<GastoVenta> getGastoVentaList() {
        return gastoVentaList;
    }

    public void setGastoVentaList(List<GastoVenta> gastoVentaList) {
        this.gastoVentaList = gastoVentaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codGasto != null ? codGasto.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Gasto)) {
            return false;
        }
        Gasto other = (Gasto) object;
        if ((this.codGasto == null && other.codGasto != null) || (this.codGasto != null && !this.codGasto.equals(other.codGasto))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.restmanager.Gasto[ codGasto=" + codGasto + " ]";
    }

}
