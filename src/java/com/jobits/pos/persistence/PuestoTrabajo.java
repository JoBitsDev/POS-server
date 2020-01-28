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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
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
@Table(name = "puesto_trabajo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PuestoTrabajo.findAll", query = "SELECT p FROM PuestoTrabajo p")
    , @NamedQuery(name = "PuestoTrabajo.findByNombrePuesto", query = "SELECT p FROM PuestoTrabajo p WHERE p.nombrePuesto = :nombrePuesto")
    , @NamedQuery(name = "PuestoTrabajo.findByIdPuesto", query = "SELECT p FROM PuestoTrabajo p WHERE p.idPuesto = :idPuesto")
    , @NamedQuery(name = "PuestoTrabajo.findBySalarioFijo", query = "SELECT p FROM PuestoTrabajo p WHERE p.salarioFijo = :salarioFijo")
    , @NamedQuery(name = "PuestoTrabajo.findBySalarioPorcientoVentaTotal", query = "SELECT p FROM PuestoTrabajo p WHERE p.salarioPorcientoVentaTotal = :salarioPorcientoVentaTotal")
    , @NamedQuery(name = "PuestoTrabajo.findBySalarioPorcientoDeArea", query = "SELECT p FROM PuestoTrabajo p WHERE p.salarioPorcientoDeArea = :salarioPorcientoDeArea")
    , @NamedQuery(name = "PuestoTrabajo.findByAreaPago", query = "SELECT p FROM PuestoTrabajo p WHERE p.areaPago = :areaPago")
    , @NamedQuery(name = "PuestoTrabajo.findByAPartirDe", query = "SELECT p FROM PuestoTrabajo p WHERE p.aPartirDe = :aPartirDe")
    , @NamedQuery(name = "PuestoTrabajo.findByNivelAcceso", query = "SELECT p FROM PuestoTrabajo p WHERE p.nivelAcceso = :nivelAcceso")
    , @NamedQuery(name = "PuestoTrabajo.findByPuestosDisponibles", query = "SELECT p FROM PuestoTrabajo p WHERE p.puestosDisponibles = :puestosDisponibles")
    , @NamedQuery(name = "PuestoTrabajo.findByRequiereAutenticar", query = "SELECT p FROM PuestoTrabajo p WHERE p.requiereAutenticar = :requiereAutenticar")
    , @NamedQuery(name = "PuestoTrabajo.findByPagoPorVentas", query = "SELECT p FROM PuestoTrabajo p WHERE p.pagoPorVentas = :pagoPorVentas")
    , @NamedQuery(name = "PuestoTrabajo.findByPropina", query = "SELECT p FROM PuestoTrabajo p WHERE p.propina = :propina")})
public class PuestoTrabajo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "nombre_puesto")
    private String nombrePuesto;
    @Size(max = 10)
    @Column(name = "id_puesto")
    private String idPuesto;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "salario_fijo")
    private Float salarioFijo;
    @Column(name = "salario_porciento_venta_total")
    private Float salarioPorcientoVentaTotal;
    @Column(name = "salario_porciento_de_area")
    private Float salarioPorcientoDeArea;
    @Size(max = 30)
    @Column(name = "area_pago")
    private String areaPago;
    @Column(name = "a_partir_de")
    private Float aPartirDe;
    @Column(name = "nivel_acceso")
    private Integer nivelAcceso;
    @Column(name = "puestos_disponibles")
    private Integer puestosDisponibles;
    @Column(name = "requiere_autenticar")
    private Boolean requiereAutenticar;
    @Column(name = "pago_por_ventas")
    private Boolean pagoPorVentas;
    @Column(name = "propina")
    private Boolean propina;
    @JoinTable(name = "puesto_trabajo_personal", joinColumns = {
        @JoinColumn(name = "puesto_trabajonombre_puesto", referencedColumnName = "nombre_puesto")}, inverseJoinColumns = {
        @JoinColumn(name = "personalusuario", referencedColumnName = "usuario")})
    @ManyToMany
    private List<Personal> personalList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "puestoTrabajonombrePuesto")
    private List<Personal> personalList1;
    @JoinColumn(name = "areacod_area", referencedColumnName = "cod_area")
    @ManyToOne
    private Area areacodArea;

    public PuestoTrabajo() {
    }

    public PuestoTrabajo(String nombrePuesto) {
        this.nombrePuesto = nombrePuesto;
    }

    public String getNombrePuesto() {
        return nombrePuesto;
    }

    public void setNombrePuesto(String nombrePuesto) {
        this.nombrePuesto = nombrePuesto;
    }

    public String getIdPuesto() {
        return idPuesto;
    }

    public void setIdPuesto(String idPuesto) {
        this.idPuesto = idPuesto;
    }

    public Float getSalarioFijo() {
        return salarioFijo;
    }

    public void setSalarioFijo(Float salarioFijo) {
        this.salarioFijo = salarioFijo;
    }

    public Float getSalarioPorcientoVentaTotal() {
        return salarioPorcientoVentaTotal;
    }

    public void setSalarioPorcientoVentaTotal(Float salarioPorcientoVentaTotal) {
        this.salarioPorcientoVentaTotal = salarioPorcientoVentaTotal;
    }

    public Float getSalarioPorcientoDeArea() {
        return salarioPorcientoDeArea;
    }

    public void setSalarioPorcientoDeArea(Float salarioPorcientoDeArea) {
        this.salarioPorcientoDeArea = salarioPorcientoDeArea;
    }

    public String getAreaPago() {
        return areaPago;
    }

    public void setAreaPago(String areaPago) {
        this.areaPago = areaPago;
    }

    public Float getAPartirDe() {
        return aPartirDe;
    }

    public void setAPartirDe(Float aPartirDe) {
        this.aPartirDe = aPartirDe;
    }

    public Integer getNivelAcceso() {
        return nivelAcceso;
    }

    public void setNivelAcceso(Integer nivelAcceso) {
        this.nivelAcceso = nivelAcceso;
    }

    public Integer getPuestosDisponibles() {
        return puestosDisponibles;
    }

    public void setPuestosDisponibles(Integer puestosDisponibles) {
        this.puestosDisponibles = puestosDisponibles;
    }

    public Boolean getRequiereAutenticar() {
        return requiereAutenticar;
    }

    public void setRequiereAutenticar(Boolean requiereAutenticar) {
        this.requiereAutenticar = requiereAutenticar;
    }

    public Boolean getPagoPorVentas() {
        return pagoPorVentas;
    }

    public void setPagoPorVentas(Boolean pagoPorVentas) {
        this.pagoPorVentas = pagoPorVentas;
    }

    public Boolean getPropina() {
        return propina;
    }

    public void setPropina(Boolean propina) {
        this.propina = propina;
    }

    @XmlTransient
    public List<Personal> getPersonalList() {
        return personalList;
    }

    public void setPersonalList(List<Personal> personalList) {
        this.personalList = personalList;
    }

    @XmlTransient
    public List<Personal> getPersonalList1() {
        return personalList1;
    }

    public void setPersonalList1(List<Personal> personalList1) {
        this.personalList1 = personalList1;
    }

    public Area getAreacodArea() {
        return areacodArea;
    }

    public void setAreacodArea(Area areacodArea) {
        this.areacodArea = areacodArea;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (nombrePuesto != null ? nombrePuesto.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PuestoTrabajo)) {
            return false;
        }
        PuestoTrabajo other = (PuestoTrabajo) object;
        if ((this.nombrePuesto == null && other.nombrePuesto != null) || (this.nombrePuesto != null && !this.nombrePuesto.equals(other.nombrePuesto))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.restmanager.PuestoTrabajo[ nombrePuesto=" + nombrePuesto + " ]";
    }

}
