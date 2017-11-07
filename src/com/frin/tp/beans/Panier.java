package com.frin.tp.beans;

import java.io.Serializable;

import org.joda.time.DateTime;

public class Panier implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Long              id;
    private Client            client;
    private DateTime          date;
    private String            nom;
    private String            constructeur;
    private int               quantite;
    private String            image;

    public Long getId() {
        return id;
    }

    public void setId( Long id ) {
        this.id = id;
    }

    public Client getClient() {
        return client;
    }

    public void setClient( Client client ) {
        this.client = client;
    }

    public DateTime getDate() {
        return date;
    }

    public void setDate( DateTime date ) {
        this.date = date;
    }

    public void setNom( String nom ) {
        this.nom = nom;
    }

    public String getNom() {
        return nom;
    }

    public void setConstructeur( String constructeur ) {
        this.constructeur = constructeur;
    }

    public String getConstructeur() {
        return constructeur;
    }

    public void setQuantite( int quantite ) {
        this.quantite = quantite;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setImage( String image ) {
        this.image = image;
    }

    public String getImage() {
        return image;
    }

}
