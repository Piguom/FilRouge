package com.frin.tp.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.frin.tp.beans.Produits;
import com.frin.tp.dao.DAOFactory;
import com.frin.tp.dao.ProduitDao;

public class AfficherProduits extends HttpServlet {
    /**
     * 
     */
    private static final long  serialVersionUID = 1L;
    public static final String CONF_DAO_FACTORY = "daofactory";

    public static final String VUE              = "/afficherProduits.jsp";
    private ProduitDao         produitDao;

    public void init() throws ServletException {
        /* R�cup�ration d'une instance de nos DAO Client et Commande */
        this.produitDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getProduitDao();
    }

    public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
        /*
         * � la r�ception d'une requ�te GET, affichage de la liste des clients
         */
        List<Produits> produits = produitDao.lister();
        System.out.println( produits );
        this.getServletContext().getRequestDispatcher( VUE ).forward( request, response );
    }
}
