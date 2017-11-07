package com.frin.tp.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.frin.tp.beans.Client;
import com.frin.tp.dao.ClientDao;
import com.frin.tp.dao.DAOFactory;

public class ListeClients extends HttpServlet {
    /**
     * 
     */
    private static final long  serialVersionUID = 1L;
    public static final String CONF_DAO_FACTORY = "daofactory";

    public static final String VUE              = "/listerClient.jsp";
    private ClientDao          clientDao;

    public void init() throws ServletException {
        /* Récupération d'une instance de nos DAO Client et Commande */
        this.clientDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getClientDao();
    }

    public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
        /*
         * À la réception d'une requête GET, affichage de la liste des clients
         */
        List<Client> clients = clientDao.lister();
        this.getServletContext().getRequestDispatcher( VUE ).forward( request, response );
    }
}