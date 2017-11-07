package com.frin.tp.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.frin.tp.beans.Admin;
import com.frin.tp.dao.AdminDao;
import com.frin.tp.dao.CommandeDao;
import com.frin.tp.dao.DAOFactory;
import com.frin.tp.forms.ConnexionAdminForm;

public class ConnexionAdmin extends HttpServlet {
    /**
     * 
     */
    private static final long  serialVersionUID = 1L;
    public static final String CONF_DAO_FACTORY = "daofactory";
    public static final String ATT_USER         = "admin";
    public static final String ATT_FORM         = "form";

    public static final String VUE              = "/connexionAdmin.jsp";
    public static final String VUE_SUCCES       = "/accesAdmin.jsp";

    private AdminDao           adminDao;
    private CommandeDao        commandeDao;

    public void init() throws ServletException {
        /* Récupération d'une instance de notre DAO Utilisateur */
        this.adminDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getAdminDao();
        this.commandeDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getCommandeDao();
    }

    public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
        /* Affichage de la page de connexion */
        this.getServletContext().getRequestDispatcher( VUE ).forward( request, response );
    }

    public void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
        /* Préparation de l'objet formulaire */
        ConnexionAdminForm form = new ConnexionAdminForm( adminDao );

        /* Traitement de la requête et récupération du bean en résultant */
        Admin admin = form.connecterAdmin( request );

        /* Stockage du formulaire et du bean dans l'objet request */
        request.setAttribute( ATT_FORM, form );
        request.setAttribute( ATT_USER, admin );

        /**
         * Si aucune erreur de validation n'a eu lieu, alors ajout du bean
         * Utilisateur à la session, sinon suppression du bean de la session.
         */
        if ( form.getErreurs().isEmpty() ) {

            /* Alors récupération de la map des clients dans la session */
            HttpSession session = request.getSession();
            session.setAttribute( ATT_USER, admin );

            /* Affichage de la fiche récapitulative */
            this.getServletContext().getRequestDispatcher( VUE_SUCCES ).forward( request, response );
        } else {
            /* Sinon, ré-affichage du formulaire de création avec les erreurs */
            this.getServletContext().getRequestDispatcher( VUE ).forward( request, response );
        }
    }
}