package com.frin.tp.dao;

import java.util.List;

import com.frin.tp.beans.Admin;

public interface AdminDao {
    void creer( Admin admin ) throws DAOException;

    Admin trouver( long id ) throws DAOException;

    Admin trouverViaEmailMdp( String email, String motdepasse ) throws DAOException;

    boolean trouverViaEmail( String email ) throws DAOException;

    boolean trouverViaMotDePasse( String motdepasse ) throws DAOException;

    List<Admin> lister() throws DAOException;

    void supprimer( Admin admin ) throws DAOException;
}
