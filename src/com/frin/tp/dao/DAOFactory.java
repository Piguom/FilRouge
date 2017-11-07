package com.frin.tp.dao;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;

public class DAOFactory {

    private static final String FICHIER_PROPERTIES       = "/com/frin/tp/dao/dao.properties";
    private static final String PROPERTY_URL             = "url";
    private static final String PROPERTY_DRIVER          = "driver";
    private static final String PROPERTY_NOM_UTILISATEUR = "nomutilisateur";
    private static final String PROPERTY_MOT_DE_PASSE    = "motdepasse";

    BoneCP                      connectionPool           = null;

    /* package */DAOFactory( BoneCP connectionPool ) {
        this.connectionPool = connectionPool;
    }

    /*
     * M�thode charg�e de r�cup�rer les informations de connexion � la base de
     * donn�es, charger le driver JDBC et retourner une instance de la Factory
     */
    public static DAOFactory getInstance() throws DAOConfigurationException {
        Properties properties = new Properties();
        String url;
        String driver;
        String nomUtilisateur;
        String motDePasse;
        BoneCP connectionPool = null;

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream fichierProperties = classLoader.getResourceAsStream( FICHIER_PROPERTIES );

        if ( fichierProperties == null ) {
            throw new DAOConfigurationException( "Le fichier properties " + FICHIER_PROPERTIES + " est introuvable." );
        }

        try {
            properties.load( fichierProperties );
            url = properties.getProperty( PROPERTY_URL );
            driver = properties.getProperty( PROPERTY_DRIVER );
            nomUtilisateur = properties.getProperty( PROPERTY_NOM_UTILISATEUR );
            motDePasse = properties.getProperty( PROPERTY_MOT_DE_PASSE );
        } catch ( FileNotFoundException e ) {
            throw new DAOConfigurationException( "Le fichier properties " + FICHIER_PROPERTIES + " est introuvable.", e );
        } catch ( IOException e ) {
            throw new DAOConfigurationException( "Impossible de charger le fichier properties " + FICHIER_PROPERTIES, e );
        }

        try {
            Class.forName( driver );
        } catch ( ClassNotFoundException e ) {
            throw new DAOConfigurationException( "Le driver est introuvable dans le classpath.", e );
        }

        try {
            BoneCPConfig config = new BoneCPConfig();
            config.setJdbcUrl( url );
            config.setUsername( nomUtilisateur );
            config.setPassword( motDePasse );
            config.setMinConnectionsPerPartition( 5 );
            config.setMaxConnectionsPerPartition( 10 );
            config.setPartitionCount( 2 );
            connectionPool = new BoneCP( config );
        } catch ( SQLException e ) {
            e.printStackTrace();
            throw new DAOConfigurationException( "Erreur de configuration du pool de connexions.", e );
        }
        DAOFactory instance = new DAOFactory( connectionPool );
        return instance;
    }

    /* M�thode charg�e de fournir une connexion � la base de donn�es */
    /* package */Connection getConnection() throws SQLException {
        return connectionPool.getConnection();
    }

    /*
     * M�thodes de r�cup�ration de l'impl�mentation des diff�rents DAO
     * (uniquement deux dans le cadre de ce TP)
     */
    public ClientDao getClientDao() {
        return new ClientDaoImpl( this );
    }

    public CommandeDao getCommandeDao() {
        return new CommandeDaoImpl( this );
    }

    public AdminDao getAdminDao() {
        return new AdminDaoImpl( this );
    }

    public ProduitDao getProduitDao() {
        return new ProduitDaoImpl( this );
    }

    public PanierDao getPanierDao() {
        return new PanierDaoImpl( this );
    }
}