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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "contabilidad_cuenta")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ContabilidadCuenta.findAll", query = "SELECT c FROM ContabilidadCuenta c")
    , @NamedQuery(name = "ContabilidadCuenta.findByIdCuenta", query = "SELECT c FROM ContabilidadCuenta c WHERE c.idCuenta = :idCuenta")
    , @NamedQuery(name = "ContabilidadCuenta.findByNombre", query = "SELECT c FROM ContabilidadCuenta c WHERE c.nombre = :nombre")
    , @NamedQuery(name = "ContabilidadCuenta.findByTipoCuenta", query = "SELECT c FROM ContabilidadCuenta c WHERE c.tipoCuenta = :tipoCuenta")
    , @NamedQuery(name = "ContabilidadCuenta.findByNumeroCuenta", query = "SELECT c FROM ContabilidadCuenta c WHERE c.numeroCuenta = :numeroCuenta")})
public class ContabilidadCuenta implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_cuenta")
    private Integer idCuenta;
    @Size(max = 100)
    @Column(name = "nombre")
    private String nombre;
    @Size(max = 7)
    @Column(name = "tipo_cuenta")
    private String tipoCuenta;
    @Size(max = 100)
    @Column(name = "numero_cuenta")
    private String numeroCuenta;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idContabilidadCuenta")
    private List<Factura> facturaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cuentaAfectada")
    private List<Factura> facturaList1;
    @OneToMany(mappedBy = "cuentaEnlazada")
    private List<ContabilidadCuenta> contabilidadCuentaList;
    @JoinColumn(name = "cuenta_enlazada", referencedColumnName = "id_cuenta")
    @ManyToOne
    private ContabilidadCuenta cuentaEnlazada;
    @OneToMany(mappedBy = "idCuentaPadre")
    private List<ContabilidadCuenta> contabilidadCuentaList1;
    @JoinColumn(name = "id_cuenta_padre", referencedColumnName = "id_cuenta")
    @ManyToOne
    private ContabilidadCuenta idCuentaPadre;

    public ContabilidadCuenta() {
    }

    public ContabilidadCuenta(Integer idCuenta) {
        this.idCuenta = idCuenta;
    }

    public Integer getIdCuenta() {
        return idCuenta;
    }

    public void setIdCuenta(Integer idCuenta) {
        this.idCuenta = idCuenta;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipoCuenta() {
        return tipoCuenta;
    }

    public void setTipoCuenta(String tipoCuenta) {
        this.tipoCuenta = tipoCuenta;
    }

    public String getNumeroCuenta() {
        return numeroCuenta;
    }

    public void setNumeroCuenta(String numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }

    @XmlTransient
    public List<Factura> getFacturaList() {
        return facturaList;
    }

    public void setFacturaList(List<Factura> facturaList) {
        this.facturaList = facturaList;
    }

    @XmlTransient
    public List<Factura> getFacturaList1() {
        return facturaList1;
    }

    public void setFacturaList1(List<Factura> facturaList1) {
        this.facturaList1 = facturaList1;
    }

    @XmlTransient
    public List<ContabilidadCuenta> getContabilidadCuentaList() {
        return contabilidadCuentaList;
    }

    public void setContabilidadCuentaList(List<ContabilidadCuenta> contabilidadCuentaList) {
        this.contabilidadCuentaList = contabilidadCuentaList;
    }

    public ContabilidadCuenta getCuentaEnlazada() {
        return cuentaEnlazada;
    }

    public void setCuentaEnlazada(ContabilidadCuenta cuentaEnlazada) {
        this.cuentaEnlazada = cuentaEnlazada;
    }

    @XmlTransient
    public List<ContabilidadCuenta> getContabilidadCuentaList1() {
        return contabilidadCuentaList1;
    }

    public void setContabilidadCuentaList1(List<ContabilidadCuenta> contabilidadCuentaList1) {
        this.contabilidadCuentaList1 = contabilidadCuentaList1;
    }

    public ContabilidadCuenta getIdCuentaPadre() {
        return idCuentaPadre;
    }

    public void setIdCuentaPadre(ContabilidadCuenta idCuentaPadre) {
        this.idCuentaPadre = idCuentaPadre;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idCuenta != null ? idCuenta.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ContabilidadCuenta)) {
            return false;
        }
        ContabilidadCuenta other = (ContabilidadCuenta) object;
        if ((this.idCuenta == null && other.idCuenta != null) || (this.idCuenta != null && !this.idCuenta.equals(other.idCuenta))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.restmanager.ContabilidadCuenta[ idCuenta=" + idCuenta + " ]";
    }

}
