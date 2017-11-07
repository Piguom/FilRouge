package com.frin.tp.forms;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.frin.tp.beans.Client;
import com.frin.tp.dao.ClientDao;
import com.frin.tp.dao.DAOException;

public final class ConnexionForm {
    private static final String CHAMP_EMAIL = "email";
    private static final String CHAMP_PASS  = "motdepasse";

    private String              resultat;
    private Map<String, String> erreurs     = new HashMap<String, String>();
    private ClientDao           clientDao;

    public ConnexionForm( ClientDao clientDao ) {
        this.clientDao = clientDao;
    }

    public String getResultat() {
        return resultat;
    }

    public Map<String, String> getErreurs() {
        return erreurs;
    }

    public Client connecterUtilisateur( HttpServletRequest request ) {
        String email = getValeurChamp( request, CHAMP_EMAIL );
        String password = getValeurChamp( request, CHAMP_PASS );

        Client user = new Client();

        traiterEmail( email, user );
        traiterPassword( password, user );

        try {
            if ( clientDao.trouverViaEmail( user.getEmail() )
                    && clientDao.trouverViaMotDePasse( user.getMotDePasse() ) ) {
                resultat = "Vous êtes bien connecté.";
                user = clientDao.trouverViaEmailMdp( user.getEmail(), user.getMotDePasse() );
            } else if ( !clientDao.trouverViaEmail( user.getEmail() )
                    && clientDao.trouverViaMotDePasse( user.getMotDePasse() ) ) {
                setErreur( CHAMP_EMAIL, "Adresse email incorrecte !" );
            } else if ( clientDao.trouverViaEmail( user.getEmail() )
                    && !clientDao.trouverViaMotDePasse( user.getMotDePasse() ) ) {
                setErreur( CHAMP_PASS, "Mot de passe incorrect !" );
            } else {
                setErreur( CHAMP_EMAIL, "Échec de la connexion. Veuillez vous inscrire vial le bouton ci-dessous." );
            }
        } catch ( DAOException e ) {
            setErreur( "imprévu", "Erreur imprévue lors de la connexion." );
            resultat = "Échec de la connexion du client : une erreur imprévue est survenue, merci de réessayer dans quelques instants.";
            e.printStackTrace();
        }

        return user;

    }

    private void traiterEmail( String email, Client user ) {
        try {
            validationEmail( email );
        } catch ( FormValidationException e ) {
            setErreur( CHAMP_EMAIL, e.getMessage() );
        }
        user.setEmail( email );
    }

    private void traiterPassword( String password, Client user ) {
        try {
            validationMotDePasse( password );
        } catch ( FormValidationException e ) {
            setErreur( CHAMP_PASS, e.getMessage() );
        }
        user.setMotDePasse( password );
    }

    private void validationEmail( String email ) throws FormValidationException {
        if ( email != null && !email.matches( "([^.@]+)(\\.[^.@]+)*@([^.@]+\\.)+([^.@]+)" ) ) {
            throw new FormValidationException( "Merci de saisir une adresse mail valide." );
        }
    }

    private void validationMotDePasse( String motdepasse ) throws FormValidationException {
        if ( motdepasse != null && motdepasse.length() < 8 ) {
            throw new FormValidationException( "Le mot de passe doit contenir au moins 8 caractères." );
        }
    }

    private void setErreur( String champ, String message ) {
        erreurs.put( champ, message );
    }

    private static String getValeurChamp( HttpServletRequest request, String nomChamp ) {
        String valeur = request.getParameter( nomChamp );
        if ( valeur == null || valeur.trim().length() == 0 ) {
            return null;
        } else {
            return valeur;
        }
    }
}