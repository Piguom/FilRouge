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

    /* Implémentation de la méthode définie dans l'interface CommandeDao */
    public Panier trouver( long id ) throws DAOException {
        return trouver( SQL_SELECT_PAR_ID, id );
    }

    /* Implémentation de la méthode définie dans l'interface CommandeDao */
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
                throw new DAOException( "Échec de la création de la commande, aucune ligne ajoutée dans la table." );
            }
            valeursAutoGenerees = preparedStatement.getGeneratedKeys();
            if ( valeursAutoGenerees.next() ) {
                panier.setId( valeursAutoGenerees.getLong( 1 ) );
            } else {
                throw new DAOException( "Échec de la création de la commande en base, aucun ID auto-généré retourné." );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( valeursAutoGenerees, preparedStatement, connexion );
        }
    }

    /* Implémentation de la méthode définie dans l'interface ClientDao */
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

    /* Implémentation de la méthode définie dans l'interface ClientDao */
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

    /* Implémentation de la méthode définie dans l'interface CommandeDao */
    public void supprimer( Panier panier ) throws DAOException {
        Connection connexion = null;
        PreparedStatement preparedStatement = null;

        try {
            connexion = daoFactory.getConnection();
            preparedStatement = initialisationRequetePreparee( connexion, SQL_DELETE_PAR_ID, true, panier.getId() );
            int statut = preparedStatement.executeUpdate();
            if ( statut == 0 ) {
                throw new DAOException( "Échec de la suppression du panier, aucune ligne supprimée de la table." );
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
     * Méthode générique utilisée pour retourner une commande depuis la base de
     * données, correspondant à la requête SQL donnée prenant en paramètres les
     * objets passés en argument.
     */
    private Panier trouver( String sql, Object... objets ) throws DAOException {
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Panier panier = null;

        try {
            /* Récupération d'une connexion depuis la Factory */
            connexion = daoFactory.getConnection();
            /*
             * Préparation de la requête avec les objets passés en arguments
             * (ici, uniquement un id) et exécution.
             */
            preparedStatement = initialisationRequetePreparee( connexion, sql, false, objets );
            resultSet = preparedStatement.executeQuery();
            /* Parcours de la ligne de données retournée dans le ResultSet */
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
     * Simple méthode utilitaire permettant de faire la correspondance (le
     * mapping) entre une ligne issue de la table des commandes (un ResultSet)
     * et un bean Commande.
     */
    private Panier map( ResultSet resultSet ) throws SQLException {
        Panier panier = new Panier();
        panier.setId( resultSet.getLong( "id" ) );

        /*
         * Petit changement ici : pour récupérer un client, il nous faut faire
         * appel à la méthode trouver() du DAO Client, afin de récupérer un bean
         * Client à partir de l'id présent dans la table Commande.
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