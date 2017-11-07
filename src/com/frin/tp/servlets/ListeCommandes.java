package com.frin.tp.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.frin.tp.beans.Client;
import com.frin.tp.beans.Commande;
import com.frin.tp.dao.CommandeDao;
import com.frin.tp.dao.DAOFactory;

public class ListeCommandes extends HttpServlet {
    public static final String ATT_COMMANDE      = "commande";
    public static final String ATT_FORM          = "form";
    public static final String CONF_DAO_FACTORY  = "daofactory";
    public static final String SESSION_COMMANDES = "commandes";
    public static final String SESSION_CLIENT    = "client";

    public static final String VUE               = "/listerCommande.jsp";
    private CommandeDao        commandeDao;

    public void init() throws ServletException {
        /* Récupération d'une instance de nos DAO Client et Commande */
        this.commandeDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getCommandeDao();
    }

    public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
        /*
         * À la réception d'une requête GET, affichage de la liste des commandes
         */
        Client client;

        HttpSession session = request.getSession();
        client = (Client) session.getAttribute( SESSION_CLIENT );

        List<Commande> commandes = commandeDao.listerViaClient( client.getId() );
        session.setAttribute( SESSION_COMMANDES, commandes );

        this.getServletContext().getRequestDispatcher( VUE ).forward( request, response );
    }
}