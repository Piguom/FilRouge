package com.frin.tp.servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.frin.tp.beans.Panier;
import com.frin.tp.dao.DAOException;
import com.frin.tp.dao.DAOFactory;
import com.frin.tp.dao.PanierDao;

public class SuppressionPanier extends HttpServlet {
    /**
     * 
     */
    private static final long  serialVersionUID = 1L;
    public static final String CONF_DAO_FACTORY = "daofactory";
    public static final String PARAM_ID_PRODUIT = "idProduit";
    public static final String SESSION_PANIERS  = "paniers";

    public static final String VUE              = "/listePanier";

    private PanierDao          panierDao;

    public void init() throws ServletException {
        /* Récupération d'une instance de notre DAO Utilisateur */
        this.panierDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getPanierDao();
    }

    public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
        /* Récupération du paramètre */
        String idPanier = getValeurParametre( request, PARAM_ID_PRODUIT );
        System.out.println( idPanier );
        Long id = Long.parseLong( idPanier );

        /* Récupération de la Map des commandes enregistrées en session */
        HttpSession session = request.getSession();
        @SuppressWarnings( "unchecked" )
        Map<Long, Panier> paniers = (HashMap<Long, Panier>) session.getAttribute( SESSION_PANIERS );

        /* Si l'id de la commande et la Map des commandes ne sont pas vides */
        if ( id != null && paniers != null ) {
            try {
                /* Alors suppression de la commande de la BDD */
                panierDao.supprimer( paniers.get( id ) );
                /* Puis suppression de la commande de la Map */
                paniers.remove( id );
            } catch ( DAOException e ) {
                e.printStackTrace();
            }
            /* Et remplacement de l'ancienne Map en session par la nouvelle */
            session.setAttribute( SESSION_PANIERS, paniers );
        }

        /* Redirection vers la fiche récapitulative */
        response.sendRedirect( request.getContextPath() + VUE );
    }

    /*
     * Méthode utilitaire qui retourne null si un paramètre est vide, et son
     * contenu sinon.
     */
    private static String getValeurParametre( HttpServletRequest request, String nomChamp ) {
        String valeur = request.getParameter( nomChamp );
        if ( valeur == null || valeur.trim().length() == 0 ) {
            return null;
        } else {
            return valeur;
        }
    }
}