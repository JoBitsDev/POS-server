/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jobits.authentication;

import java.io.Serializable;

/**
 * Objeto credenciales para parsear, este objeto debe ser pasado por un post al
 * servicio de autenticacion del sistema.
 * 
 *
 * @author Jorge
 *
 */
public class Credentials implements Serializable {

    private String username;
    private String password;
    private int accessLevel;

    public Credentials() {
    }

    public Credentials(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAccessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(int accessLevel) {
        this.accessLevel = accessLevel;
    }

}
