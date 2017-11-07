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
                resultat = "Succ�s de la cr�ation du produit.";
            } else {
                resultat = "�chec de la cr�ation du produit.";
            }
        } catch ( DAOException e ) {
            setErreur( "impr�vu", "Erreur impr�vue lors de la cr�ation." );
            resultat = "�chec de la cr�ation du produit : une erreur impr�vue est survenue, merci de r�essayer dans quelques instants.";
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
                throw new FormValidationException( "Le nom du produit doit contenir au moins 1 caract�re." );
            }
        } else {
            throw new FormValidationException( "Merci d'entrer un nom de produit." );
        }
    }

    private void validationConstructeur( String constructeur ) throws FormValidationException {
        if ( constructeur != null ) {
            if ( constructeur.length() < 1 ) {
                throw new FormValidationException( "Le nom du constructeur doit contenir au moins 1 caract�re." );
            }
        } else {
            throw new FormValidationException( "Merci d'entrer un nom de constructeur." );
        }
    }

    private void validationQuantite( String quantite ) throws FormValidationException {
        if ( quantite != null ) {
            if ( quantite.length() < 1 ) {
                throw new FormValidationException( "La quantite ne peut pas �tre nulle." );
            }
        } else {
            throw new FormValidationException( "Merci d'entrer un nombre d'article." );
        }
    }

    private String validationImage( HttpServletRequest request, String chemin ) throws FormValidationException {
        /*
         * R�cup�ration du contenu du champ image du formulaire. Il faut ici
         * utiliser la m�thode getPart().
         */
        String nomFichier = null;
        InputStream contenuFichier = null;
        try {
            Part part = request.getPart( CHAMP_IMAGE );
            nomFichier = getNomFichier( part );

            /*
             * Si la m�thode getNomFichier() a renvoy� quelque chose, il s'agit
             * donc d'un champ de type fichier (input type="file").
             */
            if ( nomFichier != null && !nomFichier.isEmpty() ) {
                /*
                 * Antibug pour Internet Explorer, qui transmet pour une raison
                 * mystique le chemin du fichier local � la machine du client...
                 * 
                 * Ex : C:/dossier/sous-dossier/fichier.ext
                 * 
                 * On doit donc faire en sorte de ne s�lectionner que le nom et
                 * l'extension du fichier, et de se d�barrasser du superflu.
                 */
                nomFichier = nomFichier.substring( nomFichier.lastIndexOf( '/' ) + 1 )
                        .substring( nomFichier.lastIndexOf( '\\' ) + 1 );

                /* R�cup�ration du contenu du fichier */
                contenuFichier = part.getInputStream();

                /* Extraction du type MIME du fichier depuis l'InputStream */
                MimeUtil.registerMimeDetector( "eu.medsea.mimeutil.detector.MagicMimeMimeDetector" );
                Collection<?> mimeTypes = MimeUtil.getMimeTypes( contenuFichier );

                /*
                 * Si le fichier est bien une image, alors son en-t�te MIME
                 * commence par la cha�ne "image"
                 */
                if ( mimeTypes.toString().startsWith( "image" ) ) {
                    /* �criture du fichier sur le disque */
                    ecrireFichier( contenuFichier, nomFichier, chemin );
                } else {
                    throw new FormValidationException( "Le fichier envoy� doit �tre une image." );
                }
            }
        } catch ( IllegalStateException e ) {
            /*
             * Exception retourn�e si la taille des donn�es d�passe les limites
             * d�finies dans la section <multipart-config> de la d�claration de
             * notre servlet d'upload dans le fichier web.xml
             */
            e.printStackTrace();
            throw new FormValidationException( "Le fichier envoy� ne doit pas d�passer 1Mo." );
        } catch ( IOException e ) {
            /*
             * Exception retourn�e si une erreur au niveau des r�pertoires de
             * stockage survient (r�pertoire inexistant, droits d'acc�s
             * insuffisants, etc.)
             */
            e.printStackTrace();
            throw new FormValidationException( "Erreur de configuration du serveur." );
        } catch ( ServletException e ) {
            /*
             * Exception retourn�e si la requ�te n'est pas de type
             * multipart/form-data.
             */
            e.printStackTrace();
            throw new FormValidationException(
                    "Ce type de requ�te n'est pas support�, merci d'utiliser le formulaire pr�vu pour envoyer votre fichier." );
        }

        return nomFichier;
    }

    /*
     * Ajoute un message correspondant au champ sp�cifi� � la map des erreurs.
     */
    private void setErreur( String champ, String message ) {
        erreurs.put( champ, message );
    }

    /*
     * M�thode utilitaire qui retourne null si un champ est vide, et son contenu
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
     * M�thode utilitaire qui a pour unique but d'analyser l'en-t�te
     * "content-disposition", et de v�rifier si le param�tre "filename" y est
     * pr�sent. Si oui, alors le champ trait� est de type File et la m�thode
     * retourne son nom, sinon il s'agit d'un champ de formulaire classique et
     * la m�thode retourne null.
     */
    private static String getNomFichier( Part part ) {
        /* Boucle sur chacun des param�tres de l'en-t�te "content-disposition". */
        for ( String contentDisposition : part.getHeader( "content-disposition" ).split( ";" ) ) {
            /* Recherche de l'�ventuelle pr�sence du param�tre "filename". */
            if ( contentDisposition.trim().startsWith( "filename" ) ) {
                /*
                 * 
                 * Si "filename" est pr�sent, alors renvoi de sa valeur,
                 * 
                 * c'est-�-dire du nom de fichier sans guillemets.
                 */
                return contentDisposition.substring( contentDisposition.indexOf( '=' ) + 1 ).trim().replace( "\"", "" );
            }
        }
        /* Et pour terminer, si rien n'a �t� trouv�... */
        return null;
    }

    /*
     * M�thode utilitaire qui a pour but d'�crire le fichier pass� en param�tre
     * sur le disque, dans le r�pertoire donn� et avec le nom donn�.
     */
    private void ecrireFichier( InputStream contenuFichier, String nomFichier, String chemin )
            throws FormValidationException {
        /* Pr�pare les flux. */
        BufferedInputStream entree = null;
        BufferedOutputStream sortie = null;
        try {
            /* Ouvre les flux. */
            entree = new BufferedInputStream( contenuFichier, TAILLE_TAMPON );
            sortie = new BufferedOutputStream( new FileOutputStream( new File( chemin + nomFichier ) ),
                    TAILLE_TAMPON );

            /*
             * Lit le fichier re�u et �crit son contenu dans un fichier sur le
             * disque.
             */
            byte[] tampon = new byte[TAILLE_TAMPON];
            int longueur = 0;
            while ( ( longueur = entree.read( tampon ) ) > 0 ) {
                sortie.write( tampon, 0, longueur );
            }
        } catch ( Exception e ) {
            throw new FormValidationException( "Erreur lors de l'�criture du fichier sur le disque." );
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
