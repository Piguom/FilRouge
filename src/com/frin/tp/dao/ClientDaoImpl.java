package com.frin.tp.dao;

import static com.frin.tp.dao.DAOUtilitaire.fermeturesSilencieuses;
import static com.frin.tp.dao.DAOUtilitaire.initialisationRequetePreparee;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.frin.tp.beans.Client;

public class ClientDaoImpl implements ClientDao {

    private static final String SQL_SELECT               = "SELECT id, nom, prenom, motdepasse, adresse, telephone, email, image FROM Client ORDER BY id";
    private static final String SQL_SELECT_PAR_ID        = "SELECT id, nom, prenom, motdepasse, adresse, telephone, email, image FROM Client WHERE id = ?";
    private static final String SQL_INSERT               = "INSERT INTO Client (nom, prenom, motdepasse, adresse, telephone, email, image) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_DELETE_PAR_ID        = "DELETE FROM Client WHERE id = ?";
    private static final String SQL_UPDATE_PAR_ID        = "UPDATE Client SET nom=?, prenom=?, motdepasse=?, adresse=?, telephone=?, email=?, image=? WHERE email=?";
    private static final String SQL_SELECT_PAR_EMAIL     = "SELECT id, nom, prenom, motdepasse, adresse, telephone, email, image FROM Client WHERE email = ?";
    private static final String SQL_SELECT_PAR_PWD       = "SELECT id, nom, prenom, motdepasse, adresse, telephone, email, image FROM Client WHERE motdepasse = ?";
    private static final String SQL_SELECT_PAR_EMAIL_MDP = "SELECT id, nom, prenom, motdepasse, adresse, telephone, email, image FROM Client WHERE email = ? && motdepasse = ?";

    private DAOFactory          daoFactory;

    ClientDaoImpl( DAOFactory daoFactory ) {
        this.daoFactory = daoFactory;
    }

    /* Impl�mentation de la m�thode d�finie dans l'interface ClientDao */
    public Client trouver( long id ) throws DAOException {
        return trouver( SQL_SELECT_PAR_ID, id );
    }

    public Client trouverViaEmailMdp( String email, String motdepasse ) throws DAOException {
        return trouver( SQL_SELECT_PAR_EMAIL_MDP, email, motdepasse );
    }

    public boolean trouverViaEmail( String email ) throws DAOException {
        return trouverViaEmail( SQL_SELECT_PAR_EMAIL, email );
    }

    public boolean trouverViaMotDePasse( String motdepasse ) throws DAOException {
        return trouverViaMotDePasse( SQL_SELECT_PAR_PWD, motdepasse );
    }

    /* Impl�mentation de la m�thode d�finie dans l'interface ClientDao */
    public void creer( Client client ) throws DAOException {
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet valeursAutoGenerees = null;

        try {
            connexion = daoFactory.getConnection();
            preparedStatement = initialisationRequetePreparee( connexion, SQL_INSERT, true,
                    client.getNom(), client.getPrenom(),
                    client.getMotDePasse(),
                    client.getAdresse(), client.getTelephone(),
                    client.getEmail(), client.getImage() );
            int statut = preparedStatement.executeUpdate();
            if ( statut == 0 ) {
                throw new DAOException( "�chec de la cr�ation du client, aucune ligne ajout�e dans la table." );
            }
            valeursAutoGenerees = preparedStatement.getGeneratedKeys();
            if ( valeursAutoGenerees.next() ) {
                client.setId( valeursAutoGenerees.getLong( 1 ) );
            } else {
                throw new DAOException( "�chec de la cr�ation du client en base, aucun ID auto-g�n�r� retourn�." );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( valeursAutoGenerees, preparedStatement, connexion );
        }
    }

    /* Impl�mentation de la m�thode d�finie dans l'interface ClientDao */
    public List<Client> lister() throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Client> clients = new ArrayList<Client>();

        try {
            connection = daoFactory.getConnection();
            preparedStatement = connection.prepareStatement( SQL_SELECT );
            resultSet = preparedStatement.executeQuery();
            while ( resultSet.next() ) {
                clients.add( map( resultSet ) );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( resultSet, preparedStatement, connection );
        }

        return clients;
    }

    /* Impl�mentation de la m�thode d�finie dans l'interface ClientDao */
    public void supprimer( Client client ) throws DAOException {
        Connection connexion = null;
        PreparedStatement preparedStatement = null;

        try {
            connexion = daoFactory.getConnection();
            preparedStatement = initialisationRequetePreparee( connexion, SQL_DELETE_PAR_ID, true, client.getId() );

            int statut = preparedStatement.executeUpdate();
            if ( statut == 0 ) {
                throw new DAOException( "�chec de la suppression du client, aucune ligne supprim�e de la table." );
            } else {
                client.setId( null );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( preparedStatement, connexion );
        }
    }

    public void modifier( Client client ) throws DAOException {
        Connection connexion = null;
        PreparedStatement preparedStatement = null;

        try {
            connexion = daoFactory.getConnection();
            preparedStatement = initialisationRequetePreparee( connexion, SQL_UPDATE_PAR_ID, true,
                    client.getNom(),
                    client.getPrenom(), client.getMotDePasse(), client.getAdresse(),
                    client.getTelephone(), client.getEmail(), client.getImage(), client.getEmail() );

            int statut = preparedStatement.executeUpdate();
            if ( statut == 0 ) {
                throw new DAOException( "�chec de la modification du client, aucune ligne modifi�e de la table." );
            } else {
                client.setNom( client.getNom() );
                client.setPrenom( client.getPrenom() );
                client.setMotDePasse( client.getMotDePasse() );
                client.setAdresse( client.getAdresse() );
                client.setTelephone( client.getTelephone() );
                client.setEmail( client.getEmail() );
                client.setImage( client.getImage() );
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
    private Client trouver( String sql, Object... objets ) throws DAOException {
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Client client = null;

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
                client = map( resultSet );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( resultSet, preparedStatement, connexion );
        }

        return client;
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
                System.out.println( "Mot de passe incorrect !" );
                return false;
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( resultSet, preparedStatement, connexion );
        }
    }

    private Client trouverViaEmailMdp( String sql, Object... objets ) throws DAOException {
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Client client = null;

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
                client = map( resultSet );
                return client;
            } else {
                System.out.println( "Erreur User" );
                return null;
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( resultSet, preparedStatement, connexion );
        }
    }

    /*
     * Simple m�thode utilitaire permettant de faire la correspondance (le
     * mapping) entre une ligne issue de la table des clients (un ResultSet) et
     * un bean Client.
     */
    private static Client map( ResultSet resultSet ) throws SQLException {
        Client client = new Client();
        client.setId( resultSet.getLong( "id" ) );
        client.setNom( resultSet.getString( "nom" ) );
        client.setPrenom( resultSet.getString( "prenom" ) );
        client.setMotDePasse( resultSet.getString( "motdepasse" ) );
        client.setAdresse( resultSet.getString( "adresse" ) );
        client.setTelephone( resultSet.getString( "telephone" ) );
        client.setEmail( resultSet.getString( "email" ) );
        client.setImage( resultSet.getString( "image" ) );
        return client;
    }

}