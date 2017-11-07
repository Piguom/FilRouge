package com.frin.tp.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.frin.tp.beans.Commande;
import com.frin.tp.dao.CommandeDao;
import com.frin.tp.dao.DAOFactory;

public class ListeToutesCommandes extends HttpServlet {
    /**
     * 
     */
    private static final long  serialVersionUID = 1L;
    public static final String ATT_COMMANDE     = "commande";
    public static final String ATT_FORM         = "form";
    public static final String CONF_DAO_FACTORY = "daofactory";

    public static final String VUE              = "/listerToutesCommandes.jsp";
    private CommandeDao        commandeDao;

    public void init() throws ServletException {
        /* Récupération d'une instance de nos DAO Client et Commande */
        this.commandeDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getCommandeDao();
    }

    public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {

        List<Commande> commandes = commandeDao.lister();
        this.getServletContext().getRequestDispatcher( VUE ).forward( request, response );
    }
}