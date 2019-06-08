/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.restmanager;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
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
@Table(name = "personal")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Personal.findAll", query = "SELECT p FROM Personal p")
    , @NamedQuery(name = "Personal.findByUsuario", query = "SELECT p FROM Personal p WHERE p.usuario = :usuario")
    , @NamedQuery(name = "Personal.findByContrasenna", query = "SELECT p FROM Personal p WHERE p.contrasenna = :contrasenna")
    , @NamedQuery(name = "Personal.findByOnline", query = "SELECT p FROM Personal p WHERE p.online = :online")
    , @NamedQuery(name = "Personal.findByFrecuencia", query = "SELECT p FROM Personal p WHERE p.frecuencia = :frecuencia")
    , @NamedQuery(name = "Personal.findByUltimodiaTrabajo", query = "SELECT p FROM Personal p WHERE p.ultimodiaTrabajo = :ultimodiaTrabajo")})
public class Personal implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "usuario")
    private String usuario;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "contrasenna")
    private String contrasenna;
    @Column(name = "online")
    private Boolean online;
    @Column(name = "frecuencia")
    private Short frecuencia;
    @Column(name = "ultimodia_pago")
    @Temporal(TemporalType.DATE)
    private Date ultimodiaTrabajo;
    @Lob
    @Column(name = "foto")
    private byte[] foto;
    @ManyToMany(mappedBy = "personalList")
    private List<PuestoTrabajo> puestoTrabajoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "personalusuario")
    private List<Orden> ordenList;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "personal")
    private DatosPersonales datosPersonales;
    @JoinColumn(name = "puesto_trabajonombre_puesto", referencedColumnName = "nombre_puesto")
    @ManyToOne(optional = false)
    private PuestoTrabajo puestoTrabajonombrePuesto;

    public Personal() {
    }

    public Personal(String usuario) {
        this.usuario = usuario;
    }

    public Personal(String usuario, String contrasenna) {
        this.usuario = usuario;
        this.contrasenna = contrasenna;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getContrasenna() {
        return contrasenna;
    }

    public void setContrasenna(String contrasenna) {
        this.contrasenna = contrasenna;
    }

    public Boolean getOnline() {
        return online;
    }

    public void setOnline(Boolean online) {
        this.online = online;
    }

    public Short getFrecuencia() {
        return frecuencia;
    }

    public void setFrecuencia(Short frecuencia) {
        this.frecuencia = frecuencia;
    }

    public Date getUltimodiaTrabajo() {
        return ultimodiaTrabajo;
    }

    public void setUltimodiaTrabajo(Date ultimodiaTrabajo) {
        this.ultimodiaTrabajo = ultimodiaTrabajo;
    }

    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }

    @XmlTransient
    public List<PuestoTrabajo> getPuestoTrabajoList() {
        return puestoTrabajoList;
    }

    public void setPuestoTrabajoList(List<PuestoTrabajo> puestoTrabajoList) {
        this.puestoTrabajoList = puestoTrabajoList;
    }

    @XmlTransient
    public List<Orden> getOrdenList() {
        return ordenList;
    }

    public void setOrdenList(List<Orden> ordenList) {
        this.ordenList = ordenList;
    }

    public DatosPersonales getDatosPersonales() {
        return datosPersonales;
    }

    public void setDatosPersonales(DatosPersonales datosPersonales) {
        this.datosPersonales = datosPersonales;
    }

    public PuestoTrabajo getPuestoTrabajonombrePuesto() {
        return puestoTrabajonombrePuesto;
    }

    public void setPuestoTrabajonombrePuesto(PuestoTrabajo puestoTrabajonombrePuesto) {
        this.puestoTrabajonombrePuesto = puestoTrabajonombrePuesto;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (usuario != null ? usuario.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Personal)) {
            return false;
        }
        Personal other = (Personal) object;
        if ((this.usuario == null && other.usuario != null) || (this.usuario != null && !this.usuario.equals(other.usuario))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.restmanager.Personal[ usuario=" + usuario + " ]";
    }

}
