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
        /* R�cup�ration d'une instance de notre DAO Utilisateur */
        this.produitDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getProduitDao();
    }

    public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
        /* � la r�ception d'une requ�te GET, simple affichage du formulaire */
        this.getServletContext().getRequestDispatcher( VUE_FORM ).forward( request, response );
    }

    public void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
        /*
         * Lecture du param�tre 'chemin' pass� � la servlet via la d�claration
         * dans le web.xml
         */
        String chemin = this.getServletConfig().getInitParameter( CHEMIN );

        /* Pr�paration de l'objet formulaire */
        CreationProduitForm form = new CreationProduitForm( produitDao );

        /* Traitement de la requ�te et r�cup�ration du bean en r�sultant */
        Produits produit = form.creerProduit( request, chemin );

        /* Ajout du bean et de l'objet m�tier � l'objet requ�te */
        request.setAttribute( ATT_PRODUIT, produit );
        request.setAttribute( ATT_FORM, form );

        /* Si aucune erreur */
        if ( form.getErreurs().isEmpty() ) {
            /* Affichage de la fiche r�capitulative */
            this.getServletContext().getRequestDispatcher( VUE_SUCCES ).forward( request, response );
        } else {
            /* Sinon, r�-affichage du formulaire de cr�ation avec les erreurs */
            this.getServletContext().getRequestDispatcher( VUE_FORM ).forward( request, response );
        }
    }
}
