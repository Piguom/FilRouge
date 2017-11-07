package com.frin.tp.dao;

import java.util.List;

import com.frin.tp.beans.Commande;

public interface CommandeDao {
    void creer( Commande commande ) throws DAOException;

    Commande trouver( long id ) throws DAOException;

    List<Commande> lister() throws DAOException;

    List<Commande> listerViaClient( long id ) throws DAOException;

    void supprimer( Commande commande ) throws DAOException;
}