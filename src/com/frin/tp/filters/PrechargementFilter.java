package com.frin.tp.filters;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.frin.tp.beans.Admin;
import com.frin.tp.beans.Client;
import com.frin.tp.beans.Commande;
import com.frin.tp.beans.Panier;
import com.frin.tp.beans.Produits;
import com.frin.tp.dao.AdminDao;
import com.frin.tp.dao.ClientDao;
import com.frin.tp.dao.CommandeDao;
import com.frin.tp.dao.DAOFactory;
import com.frin.tp.dao.PanierDao;
import com.frin.tp.dao.ProduitDao;

public class PrechargementFilter implements Filter {
    public static final String CONF_DAO_FACTORY      = "daofactory";
    public static final String ATT_SESSION_CLIENTS   = "clients";
    public static final String ATT_SESSION_COMMANDES = "commandes";
    public static final String ATT_SESSION_ADMINS    = "admins";
    public static final String ATT_SESSION_PRODUITS  = "produits";
    public static final String ATT_SESSION_PANIERS   = "paniers";

    private ClientDao          clientDao;
    private CommandeDao        commandeDao;
    private AdminDao           adminDao;
    private ProduitDao         produitDao;
    private PanierDao          panierDao;

    public void init( FilterConfig config ) throws ServletException {
        /* R�cup�ration d'une instance de nos DAO Client et Commande */
        this.clientDao = ( (DAOFactory) config.getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getClientDao();
        this.commandeDao = ( (DAOFactory) config.getServletContext().getAttribute( CONF_DAO_FACTORY ) )
                .getCommandeDao();
        this.adminDao = ( (DAOFactory) config.getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getAdminDao();
        this.produitDao = ( (DAOFactory) config.getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getProduitDao();
        this.panierDao = ( (DAOFactory) config.getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getPanierDao();
    }

    public void doFilter( ServletRequest req, ServletResponse res, FilterChain chain ) throws IOException,
            ServletException {
        /* Cast de l'objet request */
        HttpServletRequest request = (HttpServletRequest) req;

        /* R�cup�ration de la session depuis la requ�te */
        HttpSession session = request.getSession();

        /*
         * Si la map des clients n'existe pas en session, alors l'utilisateur se
         * connecte pour la premi�re fois et nous devons pr�charger en session
         * les infos contenues dans la BDD.
         */
        if ( session.getAttribute( ATT_SESSION_CLIENTS ) == null ) {
            /*
             * R�cup�ration de la liste des clients existants, et enregistrement
             * en session
             */
            List<Client> listeClients = clientDao.lister();
            Map<Long, Client> mapClients = new HashMap<Long, Client>();
            for ( Client client : listeClients ) {
                mapClients.put( client.getId(), client );
            }
            session.setAttribute( ATT_SESSION_CLIENTS, mapClients );
        }

        /*
         * De m�me pour la map des commandes
         */
        if ( session.getAttribute( ATT_SESSION_COMMANDES ) == null ) {
            /*
             * R�cup�ration de la liste des commandes existantes, et
             * enregistrement en session
             */
            List<Commande> listeCommandes = commandeDao.lister();
            Map<Long, Commande> mapCommandes = new HashMap<Long, Commande>();
            for ( Commande commande : listeCommandes ) {
                mapCommandes.put( commande.getId(), commande );
            }
            session.setAttribute( ATT_SESSION_COMMANDES, mapCommandes );
        }
        /*
         * De m�me pour la map des admins
         */
        if ( session.getAttribute( ATT_SESSION_ADMINS ) == null ) {
            /*
             * R�cup�ration de la liste des commandes existantes, et
             * enregistrement en session
             */
            List<Admin> listeAdmins = adminDao.lister();
            Map<Long, Admin> mapAdmins = new HashMap<Long, Admin>();
            for ( Admin admin : listeAdmins ) {
                mapAdmins.put( admin.getId(), admin );
            }
            session.setAttribute( ATT_SESSION_ADMINS, mapAdmins );
        }
        /*
         * De m�me pour la map des produits
         */
        if ( session.getAttribute( ATT_SESSION_PRODUITS ) == null ) {
            List<Produits> listeProduits = produitDao.lister();
            Map<Long, Produits> mapProduits = new HashMap<Long, Produits>();
            for ( Produits produit : listeProduits ) {
                mapProduits.put( produit.getId(), produit );
            }
            session.setAttribute( ATT_SESSION_PRODUITS, mapProduits );
        }
        /*
         * De m�me pour la map des paniers
         */
        if ( session.getAttribute( ATT_SESSION_PANIERS ) == null ) {
            /*
             * R�cup�ration de la liste des commandes existantes, et
             * enregistrement en session
             */
            List<Panier> listePaniers = panierDao.lister();
            Map<Long, Panier> mapPaniers = new HashMap<Long, Panier>();
            for ( Panier panier : listePaniers ) {
                mapPaniers.put( panier.getId(), panier );
            }
            session.setAttribute( ATT_SESSION_PANIERS, mapPaniers );
        }

        /* Pour terminer, poursuite de la requ�te en cours */
        chain.doFilter( request, res );
    }

    public void destroy() {
    }
}