package com.frin.tp.beans;

import java.io.Serializable;

public class Admin implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Long              id;
    private String            nom;
    private String            prenom;
    private String            motdepasse;
    private String            email;

    public void setId( Long id ) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setNom( String nom ) {
        this.nom = nom;
    }

    public String getNom() {
        return nom;
    }

    public void setPrenom( String prenom ) {
        this.prenom = prenom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setMotDePasse( String motdepasse ) {
        this.motdepasse = motdepasse;
    }

    public String getMotDePasse() {
        return motdepasse;
    }

    public void setEmail( String email ) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
