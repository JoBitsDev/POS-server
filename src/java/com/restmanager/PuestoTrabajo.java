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
 *
 * @author Jorge
 */
@Entity
@Table(name = "puesto_trabajo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PuestoTrabajo.findAll", query = "SELECT p FROM PuestoTrabajo p")
    , @NamedQuery(name = "PuestoTrabajo.findByNombrePuesto", query = "SELECT p FROM PuestoTrabajo p WHERE p.nombrePuesto = :nombrePuesto")
    , @NamedQuery(name = "PuestoTrabajo.findBySalarioFijo", query = "SELECT p FROM PuestoTrabajo p WHERE p.salarioFijo = :salarioFijo")
    , @NamedQuery(name = "PuestoTrabajo.findBySalarioPorciento", query = "SELECT p FROM PuestoTrabajo p WHERE p.salarioPorciento = :salarioPorciento")
    , @NamedQuery(name = "PuestoTrabajo.findByAreaPago", query = "SELECT p FROM PuestoTrabajo p WHERE p.areaPago = :areaPago")
    , @NamedQuery(name = "PuestoTrabajo.findByNivelAcceso", query = "SELECT p FROM PuestoTrabajo p WHERE p.nivelAcceso = :nivelAcceso")
    , @NamedQuery(name = "PuestoTrabajo.findByRequiereAutenticar", query = "SELECT p FROM PuestoTrabajo p WHERE p.requiereAutenticar = :requiereAutenticar")
    , @NamedQuery(name = "PuestoTrabajo.findByPresupuestoEstimadoDiario", query = "SELECT p FROM PuestoTrabajo p WHERE p.presupuestoEstimadoDiario = :presupuestoEstimadoDiario")
    , @NamedQuery(name = "PuestoTrabajo.findByPuestosDisponibles", query = "SELECT p FROM PuestoTrabajo p WHERE p.puestosDisponibles = :puestosDisponibles")})
public class PuestoTrabajo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "nombre_puesto")
    private String nombrePuesto;
    @Column(name = "salario_fijo")
    private Integer salarioFijo;
    @Column(name = "salario_porciento")
    private Integer salarioPorciento;
    @Size(max = 30)
    @Column(name = "area_pago")
    private String areaPago;
    @Column(name = "nivel_acceso")
    private Integer nivelAcceso;
    @Column(name = "requiere_autenticar")
    private Boolean requiereAutenticar;
    @Column(name = "presupuesto_estimado_diario")
    private Integer presupuestoEstimadoDiario;
    @Column(name = "puestos_disponibles")
    private Integer puestosDisponibles;
    @JoinTable(name = "puesto_trabajo_personal", joinColumns = {
        @JoinColumn(name = "puesto_trabajonombre_puesto", referencedColumnName = "nombre_puesto")}, inverseJoinColumns = {
        @JoinColumn(name = "personalusuario", referencedColumnName = "usuario")})
    @ManyToMany
    private List<Personal> personalList;

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

    public Integer getSalarioFijo() {
        return salarioFijo;
    }

    public void setSalarioFijo(Integer salarioFijo) {
        this.salarioFijo = salarioFijo;
    }

    public Integer getSalarioPorciento() {
        return salarioPorciento;
    }

    public void setSalarioPorciento(Integer salarioPorciento) {
        this.salarioPorciento = salarioPorciento;
    }

    public String getAreaPago() {
        return areaPago;
    }

    public void setAreaPago(String areaPago) {
        this.areaPago = areaPago;
    }

    public Integer getNivelAcceso() {
        return nivelAcceso;
    }

    public void setNivelAcceso(Integer nivelAcceso) {
        this.nivelAcceso = nivelAcceso;
    }

    public Boolean getRequiereAutenticar() {
        return requiereAutenticar;
    }

    public void setRequiereAutenticar(Boolean requiereAutenticar) {
        this.requiereAutenticar = requiereAutenticar;
    }

    public Integer getPresupuestoEstimadoDiario() {
        return presupuestoEstimadoDiario;
    }

    public void setPresupuestoEstimadoDiario(Integer presupuestoEstimadoDiario) {
        this.presupuestoEstimadoDiario = presupuestoEstimadoDiario;
    }

    public Integer getPuestosDisponibles() {
        return puestosDisponibles;
    }

    public void setPuestosDisponibles(Integer puestosDisponibles) {
        this.puestosDisponibles = puestosDisponibles;
    }

    @XmlTransient
    public List<Personal> getPersonalList() {
        return personalList;
    }

    public void setPersonalList(List<Personal> personalList) {
        this.personalList = personalList;
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
