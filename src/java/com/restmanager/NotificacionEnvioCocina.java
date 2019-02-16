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
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * FirstDream
 * @author Jorge
 * 
 */
@Entity
@Table(name = "notificacion_envio_cocina")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "NotificacionEnvioCocina.findAll", query = "SELECT n FROM NotificacionEnvioCocina n")
    , @NamedQuery(name = "NotificacionEnvioCocina.findByCocinacodCocina", query = "SELECT n FROM NotificacionEnvioCocina n WHERE n.notificacionEnvioCocinaPK.cocinacodCocina = :cocinacodCocina")
    , @NamedQuery(name = "NotificacionEnvioCocina.findByProductovOrdenproductoVentapCod", query = "SELECT n FROM NotificacionEnvioCocina n WHERE n.notificacionEnvioCocinaPK.productovOrdenproductoVentapCod = :productovOrdenproductoVentapCod")
    , @NamedQuery(name = "NotificacionEnvioCocina.findByProductovOrdenordencodOrden", query = "SELECT n FROM NotificacionEnvioCocina n WHERE n.notificacionEnvioCocinaPK.productovOrdenordencodOrden = :productovOrdenordencodOrden")
    , @NamedQuery(name = "NotificacionEnvioCocina.findByCantidad", query = "SELECT n FROM NotificacionEnvioCocina n WHERE n.cantidad = :cantidad")
    , @NamedQuery(name = "NotificacionEnvioCocina.findByHoraNotificacion", query = "SELECT n FROM NotificacionEnvioCocina n WHERE n.horaNotificacion = :horaNotificacion")})
public class NotificacionEnvioCocina implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected NotificacionEnvioCocinaPK notificacionEnvioCocinaPK;
    @Column(name = "cantidad")
    private Float cantidad;
    @Column(name = "hora_notificacion")
    @Temporal(TemporalType.TIME)
    private Date horaNotificacion;
    @JoinColumn(name = "cocinacod_cocina", referencedColumnName = "cod_cocina", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Cocina cocina;
    @JoinColumns({
        @JoinColumn(name = "productov_ordenproducto_ventap_cod", referencedColumnName = "producto_ventap_cod", insertable = false, updatable = false)
        , @JoinColumn(name = "productov_ordenordencod_orden", referencedColumnName = "ordencod_orden", insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private ProductovOrden productovOrden;

    public NotificacionEnvioCocina() {
    }

    public NotificacionEnvioCocina(NotificacionEnvioCocinaPK notificacionEnvioCocinaPK) {
        this.notificacionEnvioCocinaPK = notificacionEnvioCocinaPK;
    }

    public NotificacionEnvioCocina(NotificacionEnvioCocinaPK notificacionEnvioCocinaPK, String productovOrdenordenpersonalusuario) {
        this.notificacionEnvioCocinaPK = notificacionEnvioCocinaPK;
    }

    public NotificacionEnvioCocina(String cocinacodCocina, String productovOrdenproductoVentapCod, String productovOrdenordencodOrden) {
        this.notificacionEnvioCocinaPK = new NotificacionEnvioCocinaPK(cocinacodCocina, productovOrdenproductoVentapCod, productovOrdenordencodOrden);
    }

    public NotificacionEnvioCocinaPK getNotificacionEnvioCocinaPK() {
        return notificacionEnvioCocinaPK;
    }

    public void setNotificacionEnvioCocinaPK(NotificacionEnvioCocinaPK notificacionEnvioCocinaPK) {
        this.notificacionEnvioCocinaPK = notificacionEnvioCocinaPK;
    }

    public Float getCantidad() {
        return cantidad;
    }

    public void setCantidad(Float cantidad) {
        this.cantidad = cantidad;
    }

    public Date getHoraNotificacion() {
        return horaNotificacion;
    }

    public void setHoraNotificacion(Date horaNotificacion) {
        this.horaNotificacion = horaNotificacion;
    }

    public Cocina getCocina() {
        return cocina;
    }

    public void setCocina(Cocina cocina) {
        this.cocina = cocina;
    }

    public ProductovOrden getProductovOrden() {
        return productovOrden;
    }

    public void setProductovOrden(ProductovOrden productovOrden) {
        this.productovOrden = productovOrden;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (notificacionEnvioCocinaPK != null ? notificacionEnvioCocinaPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof NotificacionEnvioCocina)) {
            return false;
        }
        NotificacionEnvioCocina other = (NotificacionEnvioCocina) object;
        if ((this.notificacionEnvioCocinaPK == null && other.notificacionEnvioCocinaPK != null) || (this.notificacionEnvioCocinaPK != null && !this.notificacionEnvioCocinaPK.equals(other.notificacionEnvioCocinaPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.restmanager.NotificacionEnvioCocina[ notificacionEnvioCocinaPK=" + notificacionEnvioCocinaPK + " ]";
    }

}
