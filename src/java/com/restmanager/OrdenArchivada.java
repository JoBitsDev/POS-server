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
@Table(name = "orden_archivada")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "OrdenArchivada.findAll", query = "SELECT o FROM OrdenArchivada o")
    , @NamedQuery(name = "OrdenArchivada.findByCodOrden", query = "SELECT o FROM OrdenArchivada o WHERE o.codOrden = :codOrden")
    , @NamedQuery(name = "OrdenArchivada.findByHoraComenzada", query = "SELECT o FROM OrdenArchivada o WHERE o.horaComenzada = :horaComenzada")
    , @NamedQuery(name = "OrdenArchivada.findByHoraTerminada", query = "SELECT o FROM OrdenArchivada o WHERE o.horaTerminada = :horaTerminada")
    , @NamedQuery(name = "OrdenArchivada.findByDeLaCasa", query = "SELECT o FROM OrdenArchivada o WHERE o.deLaCasa = :deLaCasa")
    , @NamedQuery(name = "OrdenArchivada.findByPorciento", query = "SELECT o FROM OrdenArchivada o WHERE o.porciento = :porciento")
    , @NamedQuery(name = "OrdenArchivada.findByGananciaXporciento", query = "SELECT o FROM OrdenArchivada o WHERE o.gananciaXporciento = :gananciaXporciento")
    , @NamedQuery(name = "OrdenArchivada.findByOrdenvalorMonetario", query = "SELECT o FROM OrdenArchivada o WHERE o.ordenvalorMonetario = :ordenvalorMonetario")
    , @NamedQuery(name = "OrdenArchivada.findByOrdengastoEninsumos", query = "SELECT o FROM OrdenArchivada o WHERE o.ordengastoEninsumos = :ordengastoEninsumos")})
public class OrdenArchivada implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 11)
    @Column(name = "cod_orden")
    private String codOrden;
    @Column(name = "hora_comenzada")
    @Temporal(TemporalType.TIME)
    private Date horaComenzada;
    @Column(name = "hora_terminada")
    @Temporal(TemporalType.TIME)
    private Date horaTerminada;
    @Basic(optional = false)
    @NotNull
    @Column(name = "de_la_casa")
    private boolean deLaCasa;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "porciento")
    private Float porciento;
    @Column(name = "ganancia_xporciento")
    private Float gananciaXporciento;
    @Column(name = "ordenvalor_monetario")
    private Float ordenvalorMonetario;
    @Column(name = "ordengasto_eninsumos")
    private Float ordengastoEninsumos;
    @JoinColumn(name = "clientecod_cliente", referencedColumnName = "cod_cliente")
    @ManyToOne(optional = false)
    private Cliente clientecodCliente;
    @JoinColumn(name = "mesacod_mesa", referencedColumnName = "cod_mesa")
    @ManyToOne(optional = false)
    private Mesa mesacodMesa;
    @JoinColumn(name = "personalusuario", referencedColumnName = "usuario")
    @ManyToOne(optional = false)
    private Personal personalusuario;
    @JoinColumn(name = "ventafecha", referencedColumnName = "fecha")
    @ManyToOne(optional = false)
    private VentaArchivada ventafecha;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ordenArchivada")
    private List<ProductovOrdenArchivado> productovOrdenArchivadoList;

    public OrdenArchivada() {
    }

    public OrdenArchivada(String codOrden) {
        this.codOrden = codOrden;
    }

    public OrdenArchivada(String codOrden, boolean deLaCasa) {
        this.codOrden = codOrden;
        this.deLaCasa = deLaCasa;
    }

    public String getCodOrden() {
        return codOrden;
    }

    public void setCodOrden(String codOrden) {
        this.codOrden = codOrden;
    }

    public Date getHoraComenzada() {
        return horaComenzada;
    }

    public void setHoraComenzada(Date horaComenzada) {
        this.horaComenzada = horaComenzada;
    }

    public Date getHoraTerminada() {
        return horaTerminada;
    }

    public void setHoraTerminada(Date horaTerminada) {
        this.horaTerminada = horaTerminada;
    }

    public boolean getDeLaCasa() {
        return deLaCasa;
    }

    public void setDeLaCasa(boolean deLaCasa) {
        this.deLaCasa = deLaCasa;
    }

    public Float getPorciento() {
        return porciento;
    }

    public void setPorciento(Float porciento) {
        this.porciento = porciento;
    }

    public Float getGananciaXporciento() {
        return gananciaXporciento;
    }

    public void setGananciaXporciento(Float gananciaXporciento) {
        this.gananciaXporciento = gananciaXporciento;
    }

    public Float getOrdenvalorMonetario() {
        return ordenvalorMonetario;
    }

    public void setOrdenvalorMonetario(Float ordenvalorMonetario) {
        this.ordenvalorMonetario = ordenvalorMonetario;
    }

    public Float getOrdengastoEninsumos() {
        return ordengastoEninsumos;
    }

    public void setOrdengastoEninsumos(Float ordengastoEninsumos) {
        this.ordengastoEninsumos = ordengastoEninsumos;
    }

    public Cliente getClientecodCliente() {
        return clientecodCliente;
    }

    public void setClientecodCliente(Cliente clientecodCliente) {
        this.clientecodCliente = clientecodCliente;
    }

    public Mesa getMesacodMesa() {
        return mesacodMesa;
    }

    public void setMesacodMesa(Mesa mesacodMesa) {
        this.mesacodMesa = mesacodMesa;
    }

    public Personal getPersonalusuario() {
        return personalusuario;
    }

    public void setPersonalusuario(Personal personalusuario) {
        this.personalusuario = personalusuario;
    }

    public VentaArchivada getVentafecha() {
        return ventafecha;
    }

    public void setVentafecha(VentaArchivada ventafecha) {
        this.ventafecha = ventafecha;
    }

    @XmlTransient
    public List<ProductovOrdenArchivado> getProductovOrdenArchivadoList() {
        return productovOrdenArchivadoList;
    }

    public void setProductovOrdenArchivadoList(List<ProductovOrdenArchivado> productovOrdenArchivadoList) {
        this.productovOrdenArchivadoList = productovOrdenArchivadoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codOrden != null ? codOrden.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OrdenArchivada)) {
            return false;
        }
        OrdenArchivada other = (OrdenArchivada) object;
        if ((this.codOrden == null && other.codOrden != null) || (this.codOrden != null && !this.codOrden.equals(other.codOrden))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.restmanager.OrdenArchivada[ codOrden=" + codOrden + " ]";
    }

}
