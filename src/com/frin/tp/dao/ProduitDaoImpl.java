package com.frin.tp.dao;

import static com.frin.tp.dao.DAOUtilitaire.fermeturesSilencieuses;
import static com.frin.tp.dao.DAOUtilitaire.initialisationRequetePreparee;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.frin.tp.beans.Produits;

public class ProduitDaoImpl implements ProduitDao {

    private static final String SQL_SELECT                 = "SELECT id, nom, constructeur, quantite, image FROM Produit";
    private static final String SQL_SELECT_PAR_ID          = "SELECT id, nom, constructeur, quantite, image FROM Produit WHERE id = ?";
    private static final String SQL_INSERT                 = "INSERT INTO Produit (nom, constructeur, quantite, image) VALUES (?, ?, ?, ?)";
    private static final String SQL_UPDATE_QUANTITE_PAR_ID = "UPDATE Produit SET quantite=? WHERE id=?";

    private DAOFactory          daoFactory;

    ProduitDaoImpl( DAOFactory daoFactory ) {
        this.daoFactory = daoFactory;
    }

    public Produits trouver( long id ) throws DAOException {
        return trouver( SQL_SELECT_PAR_ID, id );
    }

    public void creer( Produits produit ) throws DAOException {
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet valeursAutoGenerees = null;

        try {
            connexion = daoFactory.getConnection();
            preparedStatement = initialisationRequetePreparee( connexion, SQL_INSERT, true,
                    produit.getNom(), produit.getConstructeur(),
                    produit.getQuantite(), produit.getImage() );
            int statut = preparedStatement.executeUpdate();
            if ( statut == 0 ) {
                throw new DAOException( "Echec de la creation du produit, aucune ligne ajoutée dans la table." );
            }
            valeursAutoGenerees = preparedStatement.getGeneratedKeys();
            if ( valeursAutoGenerees.next() ) {
                produit.setId( valeursAutoGenerees.getLong( 1 ) );
            } else {
                throw new DAOException( "Échec de la création du client en base, aucun ID auto-généré retourné." );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( valeursAutoGenerees, preparedStatement, connexion );
        }
    }

    public void updateQuantite( Produits produit, int quantite ) throws DAOException {
        Connection connexion = null;
        PreparedStatement preparedStatement = null;

        try {
            connexion = daoFactory.getConnection();
            preparedStatement = initialisationRequetePreparee( connexion, SQL_UPDATE_QUANTITE_PAR_ID, false,
                    produit.getQuantite(), produit.getId() );

            int statut = preparedStatement.executeUpdate();
            if ( statut == 0 ) {
                throw new DAOException( "Échec de la modification du produit, aucune ligne modifiée de la table." );
            }
            produit.setQuantite( quantite );

        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( preparedStatement, connexion );
        }
    }

    public List<Produits> lister() throws DAOException {

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Produits> produits = new ArrayList<Produits>();

        try {
            connection = daoFactory.getConnection();
            preparedStatement = connection.prepareStatement( SQL_SELECT );
            resultSet = preparedStatement.executeQuery();
            while ( resultSet.next() ) {
                produits.add( map( resultSet ) );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( resultSet, preparedStatement, connection );
        }
        return produits;
    }

    private Produits trouver( String sql, Object... objets ) throws DAOException {
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Produits produit = null;

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
                produit = map( resultSet );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( resultSet, preparedStatement, connexion );
        }

        return produit;
    }

    private static Produits map( ResultSet resultSet ) throws SQLException {
        Produits produit = new Produits();
        produit.setId( resultSet.getLong( "id" ) );
        produit.setNom( resultSet.getString( "nom" ) );
        produit.setConstructeur( resultSet.getString( "constructeur" ) );
        produit.setQuantite( resultSet.getInt( "quantite" ) );
        produit.setImage( resultSet.getString( "image" ) );
        return produit;
    }

}
