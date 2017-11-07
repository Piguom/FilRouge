package com.frin.tp.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.frin.tp.beans.Produits;
import com.frin.tp.dao.DAOFactory;
import com.frin.tp.dao.ProduitDao;
import com.frin.tp.forms.CreationProduitForm;

public class CreationProduit extends HttpServlet {

    /**
     * 
     */
    private static final long  serialVersionUID = 1L;
    public static final String CONF_DAO_FACTORY = "daofactory";
    public static final String CHEMIN           = "chemin";
    public static final String ATT_PRODUIT      = "produit";
    public static final String ATT_FORM         = "form";
    public static final String SESSION_PRODUITS = "produits";

    public static final String VUE_SUCCES       = "/listeProduit";
    public static final String VUE_FORM         = "/creerProduit.jsp";

    private ProduitDao         produitDao;

    public void init() throws ServletException {
        /* Récupération d'une instance de notre DAO Utilisateur */
        this.produitDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getProduitDao();
    }

    public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
        /* À la réception d'une requête GET, simple affichage du formulaire */
        this.getServletContext().getRequestDispatcher( VUE_FORM ).forward( request, response );
    }

    public void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
        /*
         * Lecture du paramètre 'chemin' passé à la servlet via la déclaration
         * dans le web.xml
         */
        String chemin = this.getServletConfig().getInitParameter( CHEMIN );

        /* Préparation de l'objet formulaire */
        CreationProduitForm form = new CreationProduitForm( produitDao );

        /* Traitement de la requête et récupération du bean en résultant */
        Produits produit = form.creerProduit( request, chemin );

        /* Ajout du bean et de l'objet métier à l'objet requête */
        request.setAttribute( ATT_PRODUIT, produit );
        request.setAttribute( ATT_FORM, form );

        /* Si aucune erreur */
        if ( form.getErreurs().isEmpty() ) {
            /* Affichage de la fiche récapitulative */
            this.getServletContext().getRequestDispatcher( VUE_SUCCES ).forward( request, response );
        } else {
            /* Sinon, ré-affichage du formulaire de création avec les erreurs */
            this.getServletContext().getRequestDispatcher( VUE_FORM ).forward( request, response );
        }
    }
}
