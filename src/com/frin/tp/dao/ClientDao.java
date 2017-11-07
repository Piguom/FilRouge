package com.frin.tp.dao;

import java.util.List;

import com.frin.tp.beans.Client;

public interface ClientDao {
    void creer( Client client ) throws DAOException;

    Client trouver( long id ) throws DAOException;

    Client trouverViaEmailMdp( String email, String motdepasse ) throws DAOException;

    boolean trouverViaEmail( String email ) throws DAOException;

    boolean trouverViaMotDePasse( String motdepasse ) throws DAOException;

    List<Client> lister() throws DAOException;

    void supprimer( Client client ) throws DAOException;

    void modifier( Client client ) throws DAOException;
}