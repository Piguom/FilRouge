package com.frin.tp.forms;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.frin.tp.beans.Admin;
import com.frin.tp.dao.AdminDao;
import com.frin.tp.dao.DAOException;

public class CreationAdminForm {
    private static final String CHAMP_NOM      = "nomAdmin";
    private static final String CHAMP_PRENOM   = "prenomAdmin";
    private static final String CHAMP_PASSWORD = "motdepasseAdmin";
    private static final String CHAMP_EMAIL    = "emailAdmin";

    private String              resultat;
    private Map<String, String> erreurs        = new HashMap<String, String>();
    private AdminDao            adminDao;

    public CreationAdminForm( AdminDao adminDao ) {
        this.adminDao = adminDao;
    }

    public Map<String, String> getErreurs() {
        return erreurs;
    }

    public String getResultat() {
        return resultat;
    }

    public Admin creerAdmin( HttpServletRequest request ) {
        String nom = getValeurChamp( request, CHAMP_NOM );
        String prenom = getValeurChamp( request, CHAMP_PRENOM );
        String motdepasse = getValeurChamp( request, CHAMP_PASSWORD );
        String email = getValeurChamp( request, CHAMP_EMAIL );

        Admin admin = new Admin();
        System.out.println( nom );
        System.out.println( prenom );
        System.out.println( motdepasse );
        System.out.println( email );

        System.out.println( "X" );
        System.out.println( "X" );
        System.out.println( "X" );

        traiterNom( nom, admin );
        traiterPrenom( prenom, admin );
        traiterMotDePasse( motdepasse, admin );
        traiterEmail( email, admin );

        System.out.println( "0" );
        try {
            if ( erreurs.isEmpty() ) {
                adminDao.creer( admin );
                resultat = "Succès de la création de l'admin.";
            } else {
                resultat = "Échec de la création de l'admin.";
            }
        } catch ( DAOException e ) {
            setErreur( "imprévu", "Erreur imprévue lors de la création." );
            resultat = "Échec de la création du client : une erreur imprévue est survenue, merci de réessayer dans quelques instants.";
            e.printStackTrace();
        }

        System.out.println( resultat );

        return admin;
    }

    private void traiterNom( String nom, Admin admin ) {
        try {
            validationNom( nom );
        } catch ( FormValidationException e ) {
            setErreur( CHAMP_NOM, e.getMessage() );
        }
        admin.setNom( nom );
        System.out.println( nom );
    }

    private void traiterPrenom( String prenom, Admin admin ) {
        try {
            validationPrenom( prenom );
        } catch ( FormValidationException e ) {
            setErreur( CHAMP_PRENOM, e.getMessage() );
        }
        admin.setPrenom( prenom );
        System.out.println( prenom );
    }

    private void traiterMotDePasse( String motdepasse, Admin admin ) {
        try {
            validationMotDePasse( motdepasse );
        } catch ( FormValidationException e ) {
            setErreur( CHAMP_PASSWORD, e.getMessage() );
        }
        admin.setMotDePasse( motdepasse );
        System.out.println( motdepasse );
    }

    private void traiterEmail( String email, Admin admin ) {
        try {
            validationEmail( email );
        } catch ( FormValidationException e ) {
            setErreur( CHAMP_EMAIL, e.getMessage() );
        }
        admin.setEmail( email );
        System.out.println( email );
    }

    private void validationNom( String nom ) throws FormValidationException {
        if ( nom != null ) {
            if ( nom.length() < 2 ) {
                throw new FormValidationException( "Le nom d'utilisateur doit contenir au moins 2 caractères." );
            }
        } else {
            throw new FormValidationException( "Merci d'entrer votre nom." );
        }
    }

    private void validationPrenom( String prenom ) throws FormValidationException {
        if ( prenom != null && prenom.length() < 2 ) {
            throw new FormValidationException( "Le prénom d'utilisateur doit contenir au moins 2 caractères." );
        }
    }

    private void validationMotDePasse( String motdepasse ) throws FormValidationException {
        if ( motdepasse != null && motdepasse.length() < 8 ) {
            throw new FormValidationException( "Le mot de passe doit contenir au moins 8 caractères." );
        }
    }

    private void validationEmail( String email ) throws FormValidationException {
        if ( email != null && !email.matches( "([^.@]+)(\\.[^.@]+)*@([^.@]+\\.)+([^.@]+)" ) ) {
            throw new FormValidationException( "Merci de saisir une adresse mail valide." );
        }
    }

    /*
     * Ajoute un message correspondant au champ spécifié à la map des erreurs.
     */
    private void setErreur( String champ, String message ) {
        erreurs.put( champ, message );
    }

    /*
     * Méthode utilitaire qui retourne null si un champ est vide, et son contenu
     * sinon.
     */
    private static String getValeurChamp( HttpServletRequest request, String nomChamp ) {
        String valeur = request.getParameter( nomChamp );
        if ( valeur == null || valeur.trim().length() == 0 ) {
            return null;
        } else {
            return valeur;
        }
    }
}
