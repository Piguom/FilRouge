package com.frin.tp.dao;

import java.util.List;

import com.frin.tp.beans.Produits;

public interface ProduitDao {
    List<Produits> lister() throws DAOException;

    void creer( Produits produit ) throws DAOException;

    Produits trouver( long id ) throws DAOException;

    void updateQuantite( Produits produit, int quantite ) throws DAOException;
}
