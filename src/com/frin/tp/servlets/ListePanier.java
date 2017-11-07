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
import com.frin.tp.dao.DAOFactory;
import com.frin.tp.dao.PanierDao;

public class ListePanier extends HttpServlet {
    /**
     * 
     */
    private static final long  serialVersionUID = 1L;
    public static final String CONF_DAO_FACTORY = "daofactory";
    public static final String SESSION_CLIENT   = "client";
    public static final String SESSION_PANIERS  = "panier";

    public static final String VUE              = "/afficherPanier.jsp";
    private PanierDao          panierDao;

    public void init() throws ServletException {
        /* Récupération d'une instance de nos DAO Client et Commande */
        this.panierDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getPanierDao();
    }

    public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
        /*
         * À la réception d'une requête GET, affichage de la liste des commandes
         */
        Client client;

        HttpSession session = request.getSession();
        client = (Client) session.getAttribute( SESSION_CLIENT );

        System.out.println( "Client panier === > " + client.getId() );

        List<Panier> paniers = panierDao.listerViaClient( client.getId() );
        session.setAttribute( SESSION_PANIERS, paniers );
        System.out.println( "Panier === > " + paniers );

        this.getServletContext().getRequestDispatcher( VUE ).forward( request, response );
    }
}