package com.frin.tp.dao;

import static com.frin.tp.dao.DAOUtilitaire.fermeturesSilencieuses;
import static com.frin.tp.dao.DAOUtilitaire.initialisationRequetePreparee;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;

import com.frin.tp.beans.Panier;

public class PanierDaoImpl implements PanierDao {

    private static final String SQL_SELECT        = "SELECT id, id_client, date, nom, constructeur, quantite, image FROM Panier ORDER BY date";
    private static final String SQL_SELECT_PAR_ID = "SELECT id, id_client, date, nom, constructeur, quantite, image FROM Panier WHERE id_client = ?";
    private static final String SQL_INSERT        = "INSERT INTO Panier (id_client, date, nom, constructeur, quantite, image) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SQL_DELETE_PAR_ID = "DELETE FROM Panier WHERE id = ?";

    private DAOFactory          daoFactory;

    PanierDaoImpl( DAOFactory daoFactory ) {
        this.daoFactory = daoFactory;
    }

    /* Impl�mentation de la m�thode d�finie dans l'interface CommandeDao */
    public Panier trouver( long id ) throws DAOException {
        return trouver( SQL_SELECT_PAR_ID, id );
    }

    /* Impl�mentation de la m�thode d�finie dans l'interface CommandeDao */
    public void creer( Panier panier ) throws DAOException {
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet valeursAutoGenerees = null;
        System.out.println( "1" );

        try {
            connexion = daoFactory.getConnection();
            System.out.println( panier.getClient().getId() );

            Date date = new Date();
            java.sql.Date dateDB = new java.sql.Date( date.getTime() );

            preparedStatement = initialisationRequetePreparee( connexion, SQL_INSERT, true,
                    panier.getClient().getId(), dateDB,
                    panier.getNom(),
                    panier.getConstructeur(), panier.getQuantite(),
                    panier.getImage() );
            int statut = preparedStatement.executeUpdate();
            if ( statut == 0 ) {
                throw new DAOException( "�chec de la cr�ation de la commande, aucune ligne ajout�e dans la table." );
            }
            valeursAutoGenerees = preparedStatement.getGeneratedKeys();
            if ( valeursAutoGenerees.next() ) {
                panier.setId( valeursAutoGenerees.getLong( 1 ) );
            } else {
                throw new DAOException( "�chec de la cr�ation de la commande en base, aucun ID auto-g�n�r� retourn�." );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( valeursAutoGenerees, preparedStatement, connexion );
        }
    }

    /* Impl�mentation de la m�thode d�finie dans l'interface ClientDao */
    public List<Panier> lister() throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Panier> paniers = new ArrayList<Panier>();

        try {
            connection = daoFactory.getConnection();
            preparedStatement = connection.prepareStatement( SQL_SELECT );
            resultSet = preparedStatement.executeQuery();
            while ( resultSet.next() ) {
                paniers.add( map( resultSet ) );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( resultSet, preparedStatement, connection );
        }

        return paniers;
    }

    /* Impl�mentation de la m�thode d�finie dans l'interface ClientDao */
    public List<Panier> listerViaClient( long id ) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Panier> paniers = new ArrayList<Panier>();

        try {
            connection = daoFactory.getConnection();
            preparedStatement = initialisationRequetePreparee( connection, SQL_SELECT_PAR_ID, false, id );
            resultSet = preparedStatement.executeQuery();
            while ( resultSet.next() ) {
                paniers.add( map( resultSet ) );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( resultSet, preparedStatement, connection );
        }

        return paniers;
    }

    /* Impl�mentation de la m�thode d�finie dans l'interface CommandeDao */
    public void supprimer( Panier panier ) throws DAOException {
        Connection connexion = null;
        PreparedStatement preparedStatement = null;

        try {
            connexion = daoFactory.getConnection();
            preparedStatement = initialisationRequetePreparee( connexion, SQL_DELETE_PAR_ID, true, panier.getId() );
            int statut = preparedStatement.executeUpdate();
            if ( statut == 0 ) {
                throw new DAOException( "�chec de la suppression du panier, aucune ligne supprim�e de la table." );
            } else {
                panier.setId( null );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( preparedStatement, connexion );
        }
    }

    /*
     * M�thode g�n�rique utilis�e pour retourner une commande depuis la base de
     * donn�es, correspondant � la requ�te SQL donn�e prenant en param�tres les
     * objets pass�s en argument.
     */
    private Panier trouver( String sql, Object... objets ) throws DAOException {
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Panier panier = null;

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
                panier = map( resultSet );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( resultSet, preparedStatement, connexion );
        }

        return panier;
    }

    /*
     * Simple m�thode utilitaire permettant de faire la correspondance (le
     * mapping) entre une ligne issue de la table des commandes (un ResultSet)
     * et un bean Commande.
     */
    private Panier map( ResultSet resultSet ) throws SQLException {
        Panier panier = new Panier();
        panier.setId( resultSet.getLong( "id" ) );

        /*
         * Petit changement ici : pour r�cup�rer un client, il nous faut faire
         * appel � la m�thode trouver() du DAO Client, afin de r�cup�rer un bean
         * Client � partir de l'id pr�sent dans la table Commande.
         */
        ClientDao clientDao = daoFactory.getClientDao();
        panier.setClient( clientDao.trouver( resultSet.getLong( "id_client" ) ) );

        panier.setDate( new DateTime( resultSet.getTimestamp( "date" ) ) );
        panier.setNom( resultSet.getString( "nom" ) );
        panier.setConstructeur( resultSet.getString( "constructeur" ) );
        panier.setQuantite( resultSet.getInt( "quantite" ) );
        panier.setImage( resultSet.getString( "image" ) );
        return panier;
    }

}