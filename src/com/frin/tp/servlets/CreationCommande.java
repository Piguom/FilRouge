package com.frin.tp.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.frin.tp.beans.Commande;
import com.frin.tp.dao.ClientDao;
import com.frin.tp.dao.CommandeDao;
import com.frin.tp.dao.DAOFactory;
import com.frin.tp.forms.CreationCommandeForm;

public class CreationCommande extends HttpServlet {
    /**
     * 
     */
    private static final long  serialVersionUID      = 1L;
    public static final String CONF_DAO_FACTORY      = "daofactory";
    public static final String CHEMIN                = "chemin";
    public static final String ATT_COMMANDE          = "commande";
    public static final String ATT_FORM              = "form";
    public static final String SESSION_CLIENTS       = "clients";
    public static final String APPLICATION_CLIENTS   = "initClients";
    public static final String SESSION_COMMANDES     = "commandes";
    public static final String APPLICATION_COMMANDES = "initCommandes";

    public static final String VUE_SUCCES            = "/afficherCommande.jsp";
    public static final String VUE_FORM              = "/creerCommande.jsp";

    private ClientDao          clientDao;
    private CommandeDao        commandeDao;

    public void init() throws ServletException {
        /* R�cup�ration d'une instance de nos DAO Client et Commande */
        this.clientDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getClientDao();
        this.commandeDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getCommandeDao();
    }

    public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
        this.getServletContext().getRequestDispatcher( VUE_FORM ).forward( request, response );
    }

    public void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
        /*
         * Lecture du param�tre 'chemin' pass� � la servlet via la d�claration
         * dans le web.xml
         */
        String chemin = this.getServletConfig().getInitParameter( CHEMIN );

        /* Pr�paration de l'objet formulaire */
        CreationCommandeForm form = new CreationCommandeForm( clientDao, commandeDao );

        /* Traitement de la requ�te et r�cup�ration du bean en r�sultant */
        Commande commande = form.creerCommande( request, chemin );

        /* Ajout du bean et de l'objet m�tier � l'objet requ�te */
        request.setAttribute( ATT_COMMANDE, commande );
        request.setAttribute( ATT_FORM, form );

        /* Si aucune erreur */
        if ( form.getErreurs().isEmpty() ) {
            /* Alors r�cup�ration de la map des clients dans la session */
            HttpSession session = request.getSession();
            session.setAttribute( ATT_COMMANDE, commande );
            /* Affichage de la fiche r�capitulative */
            this.getServletContext().getRequestDispatcher( VUE_SUCCES ).forward( request, response );
        } else {
            /* Sinon, r�-affichage du formulaire de cr�ation avec les erreurs */
            this.getServletContext().getRequestDispatcher( VUE_FORM ).forward( request, response );
        }
    }
}