package com.frin.tp.forms;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

import com.frin.tp.beans.Produits;
import com.frin.tp.dao.DAOException;
import com.frin.tp.dao.ProduitDao;

import eu.medsea.mimeutil.MimeUtil;

public class CreationProduitForm {
    private static final String CHAMP_NOM          = "nomProduit";
    private static final String CHAMP_CONSTRUCTEUR = "constructeurProduit";
    private static final String CHAMP_QUANTITE     = "quantiteProduit";
    private static final String CHAMP_IMAGE        = "imageProduit";

    private static final int    TAILLE_TAMPON      = 10240;

    private String              resultat;
    private Map<String, String> erreurs            = new HashMap<String, String>();
    private ProduitDao          produitDao;

    public CreationProduitForm( ProduitDao produitDao ) {
        this.produitDao = produitDao;
    }

    public Map<String, String> getErreurs() {
        return erreurs;
    }

    public String getResultat() {
        return resultat;
    }

    public Produits creerProduit( HttpServletRequest request, String chemin ) {
        String nom = getValeurChamp( request, CHAMP_NOM );
        String constructeur = getValeurChamp( request, CHAMP_CONSTRUCTEUR );
        String quantite = getValeurChamp( request, CHAMP_QUANTITE );

        Produits produit = new Produits();

        traiterNom( nom, produit );
        traiterConstructeur( constructeur, produit );
        traiterQuantite( quantite, produit );
        traiterImage( produit, request, chemin );

        try {
            if ( erreurs.isEmpty() ) {
                produitDao.creer( produit );
                resultat = "Succès de la création du produit.";
            } else {
                resultat = "Échec de la création du produit.";
            }
        } catch ( DAOException e ) {
            setErreur( "imprévu", "Erreur imprévue lors de la création." );
            resultat = "Échec de la création du produit : une erreur imprévue est survenue, merci de réessayer dans quelques instants.";
            e.printStackTrace();
        }

        return null;
    }

    private void traiterNom( String nom, Produits produit ) {
        try {
            validationNom( nom );
        } catch ( FormValidationException e ) {
            setErreur( CHAMP_NOM, e.getMessage() );
        }
        produit.setNom( nom );
    }

    private void traiterConstructeur( String constructeur, Produits produit ) {
        try {
            validationConstructeur( constructeur );
        } catch ( FormValidationException e ) {
            setErreur( CHAMP_CONSTRUCTEUR, e.getMessage() );
        }
        produit.setConstructeur( constructeur );
    }

    private void traiterQuantite( String quantite, Produits produit ) {
        try {
            validationQuantite( quantite );
        } catch ( FormValidationException e ) {
            setErreur( CHAMP_QUANTITE, e.getMessage() );
        }
        produit.setQuantite( Integer.parseInt( quantite ) );
    }

    private void traiterImage( Produits produit, HttpServletRequest request, String chemin ) {
        String image = null;
        try {
            image = validationImage( request, chemin );
        } catch ( FormValidationException e ) {
            setErreur( CHAMP_IMAGE, e.getMessage() );
        }
        produit.setImage( image );
    }

    private void validationNom( String nom ) throws FormValidationException {
        if ( nom != null ) {
            if ( nom.length() < 1 ) {
                throw new FormValidationException( "Le nom du produit doit contenir au moins 1 caractère." );
            }
        } else {
            throw new FormValidationException( "Merci d'entrer un nom de produit." );
        }
    }

    private void validationConstructeur( String constructeur ) throws FormValidationException {
        if ( constructeur != null ) {
            if ( constructeur.length() < 1 ) {
                throw new FormValidationException( "Le nom du constructeur doit contenir au moins 1 caractère." );
            }
        } else {
            throw new FormValidationException( "Merci d'entrer un nom de constructeur." );
        }
    }

    private void validationQuantite( String quantite ) throws FormValidationException {
        if ( quantite != null ) {
            if ( quantite.length() < 1 ) {
                throw new FormValidationException( "La quantite ne peut pas être nulle." );
            }
        } else {
            throw new FormValidationException( "Merci d'entrer un nombre d'article." );
        }
    }

    private String validationImage( HttpServletRequest request, String chemin ) throws FormValidationException {
        /*
         * Récupération du contenu du champ image du formulaire. Il faut ici
         * utiliser la méthode getPart().
         */
        String nomFichier = null;
        InputStream contenuFichier = null;
        try {
            Part part = request.getPart( CHAMP_IMAGE );
            nomFichier = getNomFichier( part );

            /*
             * Si la méthode getNomFichier() a renvoyé quelque chose, il s'agit
             * donc d'un champ de type fichier (input type="file").
             */
            if ( nomFichier != null && !nomFichier.isEmpty() ) {
                /*
                 * Antibug pour Internet Explorer, qui transmet pour une raison
                 * mystique le chemin du fichier local à la machine du client...
                 * 
                 * Ex : C:/dossier/sous-dossier/fichier.ext
                 * 
                 * On doit donc faire en sorte de ne sélectionner que le nom et
                 * l'extension du fichier, et de se débarrasser du superflu.
                 */
                nomFichier = nomFichier.substring( nomFichier.lastIndexOf( '/' ) + 1 )
                        .substring( nomFichier.lastIndexOf( '\\' ) + 1 );

                /* Récupération du contenu du fichier */
                contenuFichier = part.getInputStream();

                /* Extraction du type MIME du fichier depuis l'InputStream */
                MimeUtil.registerMimeDetector( "eu.medsea.mimeutil.detector.MagicMimeMimeDetector" );
                Collection<?> mimeTypes = MimeUtil.getMimeTypes( contenuFichier );

                /*
                 * Si le fichier est bien une image, alors son en-tête MIME
                 * commence par la chaîne "image"
                 */
                if ( mimeTypes.toString().startsWith( "image" ) ) {
                    /* Écriture du fichier sur le disque */
                    ecrireFichier( contenuFichier, nomFichier, chemin );
                } else {
                    throw new FormValidationException( "Le fichier envoyé doit être une image." );
                }
            }
        } catch ( IllegalStateException e ) {
            /*
             * Exception retournée si la taille des données dépasse les limites
             * définies dans la section <multipart-config> de la déclaration de
             * notre servlet d'upload dans le fichier web.xml
             */
            e.printStackTrace();
            throw new FormValidationException( "Le fichier envoyé ne doit pas dépasser 1Mo." );
        } catch ( IOException e ) {
            /*
             * Exception retournée si une erreur au niveau des répertoires de
             * stockage survient (répertoire inexistant, droits d'accès
             * insuffisants, etc.)
             */
            e.printStackTrace();
            throw new FormValidationException( "Erreur de configuration du serveur." );
        } catch ( ServletException e ) {
            /*
             * Exception retournée si la requête n'est pas de type
             * multipart/form-data.
             */
            e.printStackTrace();
            throw new FormValidationException(
                    "Ce type de requête n'est pas supporté, merci d'utiliser le formulaire prévu pour envoyer votre fichier." );
        }

        return nomFichier;
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

    /*
     * Méthode utilitaire qui a pour unique but d'analyser l'en-tête
     * "content-disposition", et de vérifier si le paramètre "filename" y est
     * présent. Si oui, alors le champ traité est de type File et la méthode
     * retourne son nom, sinon il s'agit d'un champ de formulaire classique et
     * la méthode retourne null.
     */
    private static String getNomFichier( Part part ) {
        /* Boucle sur chacun des paramètres de l'en-tête "content-disposition". */
        for ( String contentDisposition : part.getHeader( "content-disposition" ).split( ";" ) ) {
            /* Recherche de l'éventuelle présence du paramètre "filename". */
            if ( contentDisposition.trim().startsWith( "filename" ) ) {
                /*
                 * 
                 * Si "filename" est présent, alors renvoi de sa valeur,
                 * 
                 * c'est-à-dire du nom de fichier sans guillemets.
                 */
                return contentDisposition.substring( contentDisposition.indexOf( '=' ) + 1 ).trim().replace( "\"", "" );
            }
        }
        /* Et pour terminer, si rien n'a été trouvé... */
        return null;
    }

    /*
     * Méthode utilitaire qui a pour but d'écrire le fichier passé en paramètre
     * sur le disque, dans le répertoire donné et avec le nom donné.
     */
    private void ecrireFichier( InputStream contenuFichier, String nomFichier, String chemin )
            throws FormValidationException {
        /* Prépare les flux. */
        BufferedInputStream entree = null;
        BufferedOutputStream sortie = null;
        try {
            /* Ouvre les flux. */
            entree = new BufferedInputStream( contenuFichier, TAILLE_TAMPON );
            sortie = new BufferedOutputStream( new FileOutputStream( new File( chemin + nomFichier ) ),
                    TAILLE_TAMPON );

            /*
             * Lit le fichier reçu et écrit son contenu dans un fichier sur le
             * disque.
             */
            byte[] tampon = new byte[TAILLE_TAMPON];
            int longueur = 0;
            while ( ( longueur = entree.read( tampon ) ) > 0 ) {
                sortie.write( tampon, 0, longueur );
            }
        } catch ( Exception e ) {
            throw new FormValidationException( "Erreur lors de l'écriture du fichier sur le disque." );
        } finally {
            try {
                sortie.close();
            } catch ( IOException ignore ) {
            }
            try {
                entree.close();
            } catch ( IOException ignore ) {
            }
        }
    }

}
