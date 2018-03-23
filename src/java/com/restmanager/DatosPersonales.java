/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.restmanager;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Jorge
 */
@Entity
@Table(name = "datos_personales")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DatosPersonales.findAll", query = "SELECT d FROM DatosPersonales d")
    , @NamedQuery(name = "DatosPersonales.findByPersonalusuario", query = "SELECT d FROM DatosPersonales d WHERE d.personalusuario = :personalusuario")
    , @NamedQuery(name = "DatosPersonales.findByNombre", query = "SELECT d FROM DatosPersonales d WHERE d.nombre = :nombre")
    , @NamedQuery(name = "DatosPersonales.findByApellidos", query = "SELECT d FROM DatosPersonales d WHERE d.apellidos = :apellidos")
    , @NamedQuery(name = "DatosPersonales.findByCarnet", query = "SELECT d FROM DatosPersonales d WHERE d.carnet = :carnet")
    , @NamedQuery(name = "DatosPersonales.findByTelefonoMovil", query = "SELECT d FROM DatosPersonales d WHERE d.telefonoMovil = :telefonoMovil")
    , @NamedQuery(name = "DatosPersonales.findByTelefonoFijo", query = "SELECT d FROM DatosPersonales d WHERE d.telefonoFijo = :telefonoFijo")
    , @NamedQuery(name = "DatosPersonales.findByDireccion", query = "SELECT d FROM DatosPersonales d WHERE d.direccion = :direccion")
    , @NamedQuery(name = "DatosPersonales.findByDescripcion", query = "SELECT d FROM DatosPersonales d WHERE d.descripcion = :descripcion")
    , @NamedQuery(name = "DatosPersonales.findByFechaNacimineto", query = "SELECT d FROM DatosPersonales d WHERE d.fechaNacimineto = :fechaNacimineto")
    , @NamedQuery(name = "DatosPersonales.findByEdad", query = "SELECT d FROM DatosPersonales d WHERE d.edad = :edad")
    , @NamedQuery(name = "DatosPersonales.findBySexo", query = "SELECT d FROM DatosPersonales d WHERE d.sexo = :sexo")})
public class DatosPersonales implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    private String personalusuario;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    private String nombre;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    private String apellidos;
    @Size(max = 11)
    private String carnet;
    @Column(name = "telefono_movil")
    private Integer telefonoMovil;
    @Column(name = "telefono_fijo")
    private Integer telefonoFijo;
    @Size(max = 100)
    private String direccion;
    @Size(max = 255)
    private String descripcion;
    @Column(name = "fecha_nacimineto")
    @Temporal(TemporalType.DATE)
    private Date fechaNacimineto;
    private Integer edad;
    private Character sexo;
    @JoinColumn(name = "personalusuario", referencedColumnName = "usuario", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Personal personal;

    public DatosPersonales() {
    }

    public DatosPersonales(String personalusuario) {
        this.personalusuario = personalusuario;
    }

    public DatosPersonales(String personalusuario, String nombre, String apellidos) {
        this.personalusuario = personalusuario;
        this.nombre = nombre;
        this.apellidos = apellidos;
    }

    public String getPersonalusuario() {
        return personalusuario;
    }

    public void setPersonalusuario(String personalusuario) {
        this.personalusuario = personalusuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getCarnet() {
        return carnet;
    }

    public void setCarnet(String carnet) {
        this.carnet = carnet;
    }

    public Integer getTelefonoMovil() {
        return telefonoMovil;
    }

    public void setTelefonoMovil(Integer telefonoMovil) {
        this.telefonoMovil = telefonoMovil;
    }

    public Integer getTelefonoFijo() {
        return telefonoFijo;
    }

    public void setTelefonoFijo(Integer telefonoFijo) {
        this.telefonoFijo = telefonoFijo;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getFechaNacimineto() {
        return fechaNacimineto;
    }

    public void setFechaNacimineto(Date fechaNacimineto) {
        this.fechaNacimineto = fechaNacimineto;
    }

    public Integer getEdad() {
        return edad;
    }

    public void setEdad(Integer edad) {
        this.edad = edad;
    }

    public Character getSexo() {
        return sexo;
    }

    public void setSexo(Character sexo) {
        this.sexo = sexo;
    }

    public Personal getPersonal() {
        return personal;
    }

    public void setPersonal(Personal personal) {
        this.personal = personal;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (personalusuario != null ? personalusuario.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DatosPersonales)) {
            return false;
        }
        DatosPersonales other = (DatosPersonales) object;
        if ((this.personalusuario == null && other.personalusuario != null) || (this.personalusuario != null && !this.personalusuario.equals(other.personalusuario))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.restmanager.DatosPersonales[ personalusuario=" + personalusuario + " ]";
    }
    
}
