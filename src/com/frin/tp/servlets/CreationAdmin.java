package com.frin.tp.servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.frin.tp.beans.Admin;
import com.frin.tp.dao.AdminDao;
import com.frin.tp.dao.DAOFactory;
import com.frin.tp.forms.CreationAdminForm;

public class CreationAdmin extends HttpServlet {
    /**
     * 
     */
    private static final long  serialVersionUID = 1L;
    public static final String CONF_DAO_FACTORY = "daofactory";
    public static final String ATT_ADMIN        = "admin";
    public static final String ATT_FORM         = "form";
    public static final String SESSION_ADMINS   = "admins";

    public static final String VUE_SUCCES       = "/accesAdmin.jsp";
    public static final String VUE_FORM         = "/creerAdmin.jsp";

    private AdminDao           adminDao;

    public void init() throws ServletException {
        /* Récupération d'une instance de notre DAO Utilisateur */
        this.adminDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getAdminDao();
    }

    public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
        /* À la réception d'une requête GET, simple affichage du formulaire */
        this.getServletContext().getRequestDispatcher( VUE_FORM ).forward( request, response );
    }

    public void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {

        /* Préparation de l'objet formulaire */
        CreationAdminForm form = new CreationAdminForm( adminDao );

        /* Traitement de la requête et récupération du bean en résultant */
        Admin admin = form.creerAdmin( request );

        /* Ajout du bean et de l'objet métier à l'objet requête */
        request.setAttribute( ATT_ADMIN, admin );
        request.setAttribute( ATT_FORM, form );

        /* Si aucune erreur */
        if ( form.getErreurs().isEmpty() ) {
            /* Alors récupération de la map des clients dans la session */
            HttpSession session = request.getSession();
            Map<Long, Admin> admins = (HashMap<Long, Admin>) session.getAttribute( SESSION_ADMINS );

            /* Si aucune map n'existe, alors initialisation d'une nouvelle map */

            if ( admins == null ) {
                admins = new HashMap<Long, Admin>();
            }

            /* Puis ajout du client courant dans la map */
            admins.put( admin.getId(), admin );

            /* Et enfin (ré)enregistrement de la map en session */
            session.setAttribute( SESSION_ADMINS, admins );

            /* Affichage de la fiche récapitulative */
            this.getServletContext().getRequestDispatcher( VUE_SUCCES ).forward( request, response );
        } else {
            /* Sinon, ré-affichage du formulaire de création avec les erreurs */
            this.getServletContext().getRequestDispatcher( VUE_FORM ).forward( request, response );
        }
    }
}
