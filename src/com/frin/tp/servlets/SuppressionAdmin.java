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
import com.frin.tp.dao.DAOException;
import com.frin.tp.dao.DAOFactory;

public class SuppressionAdmin extends HttpServlet {
    /**
     * 
     */
    private static final long  serialVersionUID = 1L;
    public static final String CONF_DAO_FACTORY = "daofactory";
    public static final String PARAM_ID_ADMIN   = "idAdmin";
    public static final String SESSION_ADMINS   = "admins";

    public static final String VUE              = "/deconnexion";

    private AdminDao           adminDao;

    public void init() throws ServletException {
        /* Récupération d'une instance de notre DAO Utilisateur */
        this.adminDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getAdminDao();
    }

    public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
        /* Récupération du paramètre */
        String idAdmin = getValeurParametre( request, PARAM_ID_ADMIN );
        Long id = Long.parseLong( idAdmin );

        /* Récupération de la Map des clients enregistrés en session */
        HttpSession session = request.getSession();
        Map<Long, Admin> admins = (HashMap<Long, Admin>) session.getAttribute( SESSION_ADMINS );

        /* Si l'id du client et la Map des clients ne sont pas vides */
        if ( id != null && admins != null ) {
            try {
                /* Alors suppression du client de la BDD */
                adminDao.supprimer( admins.get( id ) );
                /* Puis suppression du client de la Map */
                admins.remove( id );
            } catch ( DAOException e ) {
                e.printStackTrace();
            }
            /* Et remplacement de l'ancienne Map en session par la nouvelle */
            session.setAttribute( SESSION_ADMINS, admins );
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