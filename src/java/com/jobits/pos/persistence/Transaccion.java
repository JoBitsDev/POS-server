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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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
@Table(name = "transaccion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Transaccion.findAll", query = "SELECT t FROM Transaccion t")
    , @NamedQuery(name = "Transaccion.findByNoTransaccion", query = "SELECT t FROM Transaccion t WHERE t.noTransaccion = :noTransaccion")
    , @NamedQuery(name = "Transaccion.findByFecha", query = "SELECT t FROM Transaccion t WHERE t.fecha = :fecha")
    , @NamedQuery(name = "Transaccion.findByHora", query = "SELECT t FROM Transaccion t WHERE t.hora = :hora")
    , @NamedQuery(name = "Transaccion.findByCantidad", query = "SELECT t FROM Transaccion t WHERE t.cantidad = :cantidad")
    , @NamedQuery(name = "Transaccion.findByDescripcion", query = "SELECT t FROM Transaccion t WHERE t.descripcion = :descripcion")})
public class Transaccion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "no_transaccion")
    private Integer noTransaccion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha")
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @Basic(optional = false)
    @NotNull
    @Column(name = "hora")
    @Temporal(TemporalType.TIME)
    private Date hora;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "cantidad")
    private Float cantidad;
    @Size(max = 255)
    @Column(name = "descripcion")
    private String descripcion;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "transaccion")
    private TransaccionSalida transaccionSalida;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "transaccion")
    private TransaccionTraspaso transaccionTraspaso;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "transaccion")
    private TransaccionEntrada transaccionEntrada;
    @JoinColumn(name = "almacencod_almacen", referencedColumnName = "cod_almacen")
    @ManyToOne(optional = false)
    private Almacen almacencodAlmacen;
    @JoinColumn(name = "insumocod_insumo", referencedColumnName = "cod_insumo")
    @ManyToOne(optional = false)
    private Insumo insumocodInsumo;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "transaccion")
    private TransaccionMerma transaccionMerma;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "transaccion")
    private List<TransaccionTransformacion> transaccionTransformacionList;

    public Transaccion() {
    }

    public Transaccion(Integer noTransaccion) {
        this.noTransaccion = noTransaccion;
    }

    public Transaccion(Integer noTransaccion, Date fecha, Date hora) {
        this.noTransaccion = noTransaccion;
        this.fecha = fecha;
        this.hora = hora;
    }

    public Integer getNoTransaccion() {
        return noTransaccion;
    }

    public void setNoTransaccion(Integer noTransaccion) {
        this.noTransaccion = noTransaccion;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Date getHora() {
        return hora;
    }

    public void setHora(Date hora) {
        this.hora = hora;
    }

    public Float getCantidad() {
        return cantidad;
    }

    public void setCantidad(Float cantidad) {
        this.cantidad = cantidad;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public TransaccionSalida getTransaccionSalida() {
        return transaccionSalida;
    }

    public void setTransaccionSalida(TransaccionSalida transaccionSalida) {
        this.transaccionSalida = transaccionSalida;
    }

    public TransaccionTraspaso getTransaccionTraspaso() {
        return transaccionTraspaso;
    }

    public void setTransaccionTraspaso(TransaccionTraspaso transaccionTraspaso) {
        this.transaccionTraspaso = transaccionTraspaso;
    }

    public TransaccionEntrada getTransaccionEntrada() {
        return transaccionEntrada;
    }

    public void setTransaccionEntrada(TransaccionEntrada transaccionEntrada) {
        this.transaccionEntrada = transaccionEntrada;
    }

    public Almacen getAlmacencodAlmacen() {
        return almacencodAlmacen;
    }

    public void setAlmacencodAlmacen(Almacen almacencodAlmacen) {
        this.almacencodAlmacen = almacencodAlmacen;
    }

    public Insumo getInsumocodInsumo() {
        return insumocodInsumo;
    }

    public void setInsumocodInsumo(Insumo insumocodInsumo) {
        this.insumocodInsumo = insumocodInsumo;
    }

    public TransaccionMerma getTransaccionMerma() {
        return transaccionMerma;
    }

    public void setTransaccionMerma(TransaccionMerma transaccionMerma) {
        this.transaccionMerma = transaccionMerma;
    }

    @XmlTransient
    public List<TransaccionTransformacion> getTransaccionTransformacionList() {
        return transaccionTransformacionList;
    }

    public void setTransaccionTransformacionList(List<TransaccionTransformacion> transaccionTransformacionList) {
        this.transaccionTransformacionList = transaccionTransformacionList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (noTransaccion != null ? noTransaccion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Transaccion)) {
            return false;
        }
        Transaccion other = (Transaccion) object;
        if ((this.noTransaccion == null && other.noTransaccion != null) || (this.noTransaccion != null && !this.noTransaccion.equals(other.noTransaccion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.restmanager.Transaccion[ noTransaccion=" + noTransaccion + " ]";
    }

}
