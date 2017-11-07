package com.frin.tp.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.frin.tp.beans.Client;
import com.frin.tp.beans.Panier;
import com.frin.tp.beans.Produits;
import com.frin.tp.dao.DAOFactory;
import com.frin.tp.dao.PanierDao;
import com.frin.tp.dao.ProduitDao;

public class CreationPanier extends HttpServlet {
    /**
     * 
     */
    private static final long  serialVersionUID = 1L;
    public static final String CONF_DAO_FACTORY = "daofactory";
    public static final String CHEMIN           = "chemin";
    public static final String ATT_PANIER       = "panier";
    public static final String ATT_PRODUITS     = "produits";
    public static final String SESSION_PANIER   = "panier";
    public static final String SESSION_CLIENT   = "client";
    public static final String SESSION_PRODUITS = "produit";

    public static final String VUE              = "/afficherPanier.jsp";

    private ProduitDao         produitDao;
    private PanierDao          panierDao;

    public void init() throws ServletException {
        /* Récupération d'une instance de nos DAO Client et Commande */
        this.produitDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getProduitDao();
        this.panierDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getPanierDao();
    }

    public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
        /*
         * À la réception d'une requête GET, affichage de la liste des commandes
         */
        Client client;
        Produits produit;
        Panier panier = new Panier();

        HttpSession session = request.getSession();
        client = (Client) session.getAttribute( SESSION_CLIENT );
        System.out.println( "Client panier ===> " + client.getId() );

        produit = produitDao.trouver( Long.parseLong( request.getParameter( "id" ) ) );
        System.out.println( "Produit : " + produit.getNom() + " Quantite = " + produit.getQuantite() );
        produitDao.updateQuantite( produit, produit.getQuantite() - 1 );
        System.out.println( "Produit : " + produit.getNom() + " Quantite = " + produit.getQuantite() );
        produit.setQuantite( produit.getQuantite() );

        panier.setClient( client );
        panier.setNom( produit.getNom() );
        panier.setConstructeur( produit.getConstructeur() );
        panier.setQuantite( produit.getQuantite() - produit.getQuantite() + 1 );
        panier.setImage( produit.getImage() );

        panierDao.creer( panier );

        List<Panier> paniers = panierDao.listerViaClient( client.getId() );
        session.setAttribute( SESSION_PRODUITS, paniers );

        this.getServletContext().getRequestDispatcher( VUE ).forward( request, response );
    }
}