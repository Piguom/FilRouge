package com.frin.tp.servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.frin.tp.beans.Client;
import com.frin.tp.dao.ClientDao;
import com.frin.tp.dao.DAOFactory;
import com.frin.tp.forms.ModificationClientForm;

public class ModificationClient extends HttpServlet {
    /**
     * 
     */
    private static final long  serialVersionUID = 1L;
    public static final String CONF_DAO_FACTORY = "daofactory";
    public static final String CHEMIN           = "chemin";
    public static final String ATT_CLIENT       = "client";
    public static final String ATT_FORM         = "form";
    public static final String SESSION_CLIENTS  = "clients";

    public static final String VUE_SUCCES       = "/accesRestreint.jsp";
    public static final String VUE_FORM         = "/modificationClient.jsp";

    private ClientDao          clientDao;

    public void init() throws ServletException {
        /* R�cup�ration d'une instance de notre DAO Utilisateur */
        this.clientDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getClientDao();
    }

    public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
        /* � la r�ception d'une requ�te GET, simple affichage du formulaire */

        /* Alors r�cup�ration de la map des clients dans la session */
        HttpSession session = request.getSession();

        System.out.println( "Session" );
        Client user2 = (Client) session.getAttribute( "client" );
        System.out.println( user2.getNom() );
        System.out.println( user2.getEmail() );
        System.out.println( user2.getMotDePasse() );

        this.getServletContext().getRequestDispatcher( VUE_FORM ).forward( request, response );
    }

    public void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
        /*
         * Lecture du param�tre 'chemin' pass� � la servlet via la d�claration
         * dans le web.xml
         */
        HttpSession session = request.getSession();
        session.getAttribute( SESSION_CLIENTS );

        String chemin = this.getServletConfig().getInitParameter( CHEMIN );

        /* Pr�paration de l'objet formulaire */
        ModificationClientForm form = new ModificationClientForm( clientDao );

        /* Traitement de la requ�te et r�cup�ration du bean en r�sultant */
        Client client = form.modifierClient( request, chemin );

        /* Ajout du bean et de l'objet m�tier � l'objet requ�te */
        request.setAttribute( ATT_CLIENT, client );
        request.setAttribute( ATT_FORM, form );

        /* Si aucune erreur */
        if ( form.getErreurs().isEmpty() ) {
            /* Alors r�cup�ration de la map des clients dans la session */

            session = request.getSession();
            Map<Long, Client> clients = (HashMap<Long, Client>) session.getAttribute( SESSION_CLIENTS );

            /* Si aucune map n'existe, alors initialisation d'une nouvelle map */

            if ( clients == null ) {
                clients = new HashMap<Long, Client>();
            }

            /* Puis ajout du client courant dans la map */

            clients.put( client.getId(), client );
            /* Et enfin (r�)enregistrement de la map en session */

            session.setAttribute( SESSION_CLIENTS, clients );

            /* Affichage de la fiche r�capitulative */
            this.getServletContext().getRequestDispatcher( VUE_SUCCES ).forward( request, response );
        } else {
            /* Sinon, r�-affichage du formulaire de cr�ation avec les erreurs */
            this.getServletContext().getRequestDispatcher( VUE_FORM ).forward( request, response );
        }
    }
}