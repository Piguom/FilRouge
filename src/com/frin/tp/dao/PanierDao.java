package com.frin.tp.dao;

import java.util.List;

import com.frin.tp.beans.Panier;

public interface PanierDao {
    void creer( Panier panier ) throws DAOException;

    Panier trouver( long id ) throws DAOException;

    List<Panier> lister() throws DAOException;

    List<Panier> listerViaClient( long id ) throws DAOException;

    void supprimer( Panier panier ) throws DAOException;
}
