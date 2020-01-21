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
@Table(name = "almacen")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Almacen.findAll", query = "SELECT a FROM Almacen a")
    , @NamedQuery(name = "Almacen.findByCodAlmacen", query = "SELECT a FROM Almacen a WHERE a.codAlmacen = :codAlmacen")
    , @NamedQuery(name = "Almacen.findByNombre", query = "SELECT a FROM Almacen a WHERE a.nombre = :nombre")
    , @NamedQuery(name = "Almacen.findByCantidadInsumos", query = "SELECT a FROM Almacen a WHERE a.cantidadInsumos = :cantidadInsumos")
    , @NamedQuery(name = "Almacen.findByValorMonetario", query = "SELECT a FROM Almacen a WHERE a.valorMonetario = :valorMonetario")
    , @NamedQuery(name = "Almacen.findByCentroElaboracion", query = "SELECT a FROM Almacen a WHERE a.centroElaboracion = :centroElaboracion")})
public class Almacen implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 4)
    @Column(name = "cod_almacen")
    private String codAlmacen;
    @Size(max = 30)
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "cantidad_insumos")
    private Integer cantidadInsumos;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valor_monetario")
    private Float valorMonetario;
    @Column(name = "centro_elaboracion")
    private Boolean centroElaboracion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "almacenDestino")
    private List<TransaccionTraspaso> transaccionTraspasoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "almacencodAlmacen")
    private List<Transaccion> transaccionList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "almacen")
    private List<InsumoAlmacen> insumoAlmacenList;

    public Almacen() {
    }

    public Almacen(String codAlmacen) {
        this.codAlmacen = codAlmacen;
    }

    public String getCodAlmacen() {
        return codAlmacen;
    }

    public void setCodAlmacen(String codAlmacen) {
        this.codAlmacen = codAlmacen;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getCantidadInsumos() {
        return cantidadInsumos;
    }

    public void setCantidadInsumos(Integer cantidadInsumos) {
        this.cantidadInsumos = cantidadInsumos;
    }

    public Float getValorMonetario() {
        return valorMonetario;
    }

    public void setValorMonetario(Float valorMonetario) {
        this.valorMonetario = valorMonetario;
    }

    public Boolean getCentroElaboracion() {
        return centroElaboracion;
    }

    public void setCentroElaboracion(Boolean centroElaboracion) {
        this.centroElaboracion = centroElaboracion;
    }

    @XmlTransient
    public List<TransaccionTraspaso> getTransaccionTraspasoList() {
        return transaccionTraspasoList;
    }

    public void setTransaccionTraspasoList(List<TransaccionTraspaso> transaccionTraspasoList) {
        this.transaccionTraspasoList = transaccionTraspasoList;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codAlmacen != null ? codAlmacen.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Almacen)) {
            return false;
        }
        Almacen other = (Almacen) object;
        if ((this.codAlmacen == null && other.codAlmacen != null) || (this.codAlmacen != null && !this.codAlmacen.equals(other.codAlmacen))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.restmanager.Almacen[ codAlmacen=" + codAlmacen + " ]";
    }

}
