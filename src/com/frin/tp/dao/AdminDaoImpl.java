package com.frin.tp.dao;

import static com.frin.tp.dao.DAOUtilitaire.fermeturesSilencieuses;
import static com.frin.tp.dao.DAOUtilitaire.initialisationRequetePreparee;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.frin.tp.beans.Admin;

public class AdminDaoImpl implements AdminDao {

    private static final String SQL_SELECT               = "SELECT id, nom, prenom, motdepasse, email FROM Admin ORDER BY id";
    private static final String SQL_SELECT_PAR_ID        = "SELECT id, nom, prenom, motdepasse, email FROM Admin WHERE id = ?";
    private static final String SQL_INSERT               = "INSERT INTO Admin (nom, prenom, motdepasse, email) VALUES (?, ?, ?, ?)";
    private static final String SQL_DELETE_PAR_ID        = "DELETE FROM Admin WHERE id = ?";
    private static final String SQL_SELECT_PAR_EMAIL     = "SELECT id, nom, prenom, motdepasse, email FROM Admin WHERE email = ?";
    private static final String SQL_SELECT_PAR_PWD       = "SELECT id, nom, prenom, motdepasse, email FROM Admin WHERE motdepasse = ?";
    private static final String SQL_SELECT_PAR_EMAIL_MDP = "SELECT id, nom, prenom, motdepasse, email FROM Admin WHERE email = ? && motdepasse = ?";

    private DAOFactory          daoFactory;

    AdminDaoImpl( DAOFactory daoFactory ) {
        this.daoFactory = daoFactory;
    }

    /* Impl�mentation de la m�thode d�finie dans l'interface ClientDao */
    public Admin trouver( long id ) throws DAOException {
        return trouver( SQL_SELECT_PAR_ID, id );
    }

    public Admin trouverViaEmailMdp( String email, String motdepasse ) throws DAOException {
        return trouver( SQL_SELECT_PAR_EMAIL_MDP, email, motdepasse );
    }

    public boolean trouverViaEmail( String email ) throws DAOException {
        return trouverViaEmail( SQL_SELECT_PAR_EMAIL, email );
    }

    public boolean trouverViaMotDePasse( String motdepasse ) throws DAOException {
        return trouverViaMotDePasse( SQL_SELECT_PAR_PWD, motdepasse );
    }

    /* Impl�mentation de la m�thode d�finie dans l'interface ClientDao */
    public void creer( Admin admin ) throws DAOException {
        System.out.println( "Debut cr�ation Admin DAO" );
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet valeursAutoGenerees = null;

        try {
            connexion = daoFactory.getConnection();
            preparedStatement = initialisationRequetePreparee( connexion, SQL_INSERT, true,
                    admin.getNom(), admin.getPrenom(),
                    admin.getMotDePasse(),
                    admin.getEmail() );
            int statut = preparedStatement.executeUpdate();
            if ( statut == 0 ) {
                System.out.println( "Pas de ligne ajout�e" );
                throw new DAOException( "�chec de la cr�ation du client, aucune ligne ajout�e dans la table." );
            }
            valeursAutoGenerees = preparedStatement.getGeneratedKeys();
            if ( valeursAutoGenerees.next() ) {
                admin.setId( valeursAutoGenerees.getLong( 1 ) );
            } else {
                System.out.println( "Pas de'ID ajout�" );
                throw new DAOException( "�chec de la cr�ation du client en base, aucun ID auto-g�n�r� retourn�." );
            }
        } catch ( SQLException e ) {
            System.out.println( "Exception de merde" );
            throw new DAOException( e );
        } finally {
            System.out.println( "Fermeture de la m�thode creerAdmin" );
            fermeturesSilencieuses( valeursAutoGenerees, preparedStatement, connexion );
        }
    }

    /* Impl�mentation de la m�thode d�finie dans l'interface ClientDao */
    public List<Admin> lister() throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Admin> admins = new ArrayList<Admin>();

        try {
            connection = daoFactory.getConnection();
            preparedStatement = connection.prepareStatement( SQL_SELECT );
            resultSet = preparedStatement.executeQuery();
            while ( resultSet.next() ) {
                admins.add( map( resultSet ) );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( resultSet, preparedStatement, connection );
        }

        return admins;
    }

    /* Impl�mentation de la m�thode d�finie dans l'interface ClientDao */
    public void supprimer( Admin admin ) throws DAOException {
        Connection connexion = null;
        PreparedStatement preparedStatement = null;

        try {
            connexion = daoFactory.getConnection();
            preparedStatement = initialisationRequetePreparee( connexion, SQL_DELETE_PAR_ID, true, admin.getId() );
            int statut = preparedStatement.executeUpdate();
            if ( statut == 0 ) {
                throw new DAOException( "�chec de la suppression du client, aucune ligne supprim�e de la table." );
            } else {
                admin.setId( null );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( preparedStatement, connexion );
        }
    }

    /*
     * M�thode g�n�rique utilis�e pour retourner un client depuis la base de
     * donn�es, correspondant � la requ�te SQL donn�e prenant en param�tres les
     * objets pass�s en argument.
     */
    private Admin trouver( String sql, Object... objets ) throws DAOException {
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Admin admin = null;

        try {
            /* R�cup�ration d'une connexion depuis la Factory */
            connexion = daoFactory.getConnection();
            /*
             * Pr�paration de la requ�te avec les objets pass�s en arguments
             * (ici, uniquement un id) et ex�cution.
             */
            preparedStatement = initialisationRequetePreparee( connexion, sql, false, objets );
            resultSet = preparedStatement.executeQuery();
            /* Parcours de la ligne de donn�es retourn�e dans le ResultSet */
            if ( resultSet.next() ) {
                admin = map( resultSet );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( resultSet, preparedStatement, connexion );
        }

        return admin;
    }

    private boolean trouverViaEmail( String sql, Object... objets ) throws DAOException {
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        // Client client = null;

        try {
            System.out.println( "Boucle pour connection � la BDD" );
            /* R�cup�ration d'une connexion depuis la Factory */
            connexion = daoFactory.getConnection();
            /*
             * Pr�paration de la requ�te avec les objets pass�s en arguments
             * (ici, uniquement un id) et ex�cution.
             */
            preparedStatement = initialisationRequetePreparee( connexion, sql, false, objets );
            resultSet = preparedStatement.executeQuery();
            System.out.println( resultSet );
            /* Parcours de la ligne de donn�es retourn�e dans le ResultSet */
            if ( resultSet.next() ) {
                System.out.println( resultSet );
                return true;
            } else {
                System.out.println( "Email incorrect !" );
                return false;
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( resultSet, preparedStatement, connexion );
        }
    }

    private boolean trouverViaMotDePasse( String sql, Object... objets ) throws DAOException {
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Admin admin = null;

        try {
            System.out.println( "Boucle pour connection � la BDD" );
            /* R�cup�ration d'une connexion depuis la Factory */
            connexion = daoFactory.getConnection();
            /*
             * Pr�paration de la requ�te avec les objets pass�s en arguments
             * (ici, uniquement un id) et ex�cution.
             */
            preparedStatement = initialisationRequetePreparee( connexion, sql, false, objets );
            resultSet = preparedStatement.executeQuery();
            System.out.println( resultSet );
            /* Parcours de la ligne de donn�es retourn�e dans le ResultSet */
            if ( resultSet.next() ) {
                System.out.println( resultSet );
                return true;
            } else {
                System.out.println( "Mot de passe incorrect !" );
                return false;
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( resultSet, preparedStatement, connexion );
        }
    }

    @SuppressWarnings( "unused" )
    private Admin trouverViaEmailMdp( String sql, Object... objets ) throws DAOException {
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Admin admin = null;

        try {
            System.out.println( "Boucle pour connection � la BDD" );
            /* R�cup�ration d'une connexion depuis la Factory */
            connexion = daoFactory.getConnection();
            /*
             * Pr�paration de la requ�te avec les objets pass�s en arguments
             * (ici, uniquement un id) et ex�cution.
             */
            preparedStatement = initialisationRequetePreparee( connexion, sql, false, objets );
            resultSet = preparedStatement.executeQuery();

            /* Parcours de la ligne de donn�es retourn�e dans le ResultSet */
            if ( resultSet.next() ) {
                admin = map( resultSet );
            } else {
                System.out.println( "Erreur User" );
                return null;
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( resultSet, preparedStatement, connexion );
        }

        return admin;
    }

    /*
     * Simple m�thode utilitaire permettant de faire la correspondance (le
     * mapping) entre une ligne issue de la table des clients (un ResultSet) et
     * un bean Client.
     */
    private static Admin map( ResultSet resultSet ) throws SQLException {
        Admin admin = new Admin();
        admin.setId( resultSet.getLong( "id" ) );
        admin.setNom( resultSet.getString( "nom" ) );
        admin.setPrenom( resultSet.getString( "prenom" ) );
        admin.setMotDePasse( resultSet.getString( "motdepasse" ) );
        admin.setEmail( resultSet.getString( "email" ) );
        return admin;
    }

}
