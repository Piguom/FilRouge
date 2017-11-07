package com.frin.tp.servlets;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Download extends HttpServlet {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
        String chemin = this.getServletConfig().getInitParameter( "chemin" );
        String fichierRequis = request.getPathInfo();
        if ( fichierRequis == null || "/".equals( fichierRequis ) ) {
            response.sendError( HttpServletResponse.SC_NOT_FOUND );
            return;
        }

        fichierRequis = URLDecoder.decode( fichierRequis, "UTF-8" );
        File fichier = new File( chemin, fichierRequis );
        if ( !fichier.exists() ) {
            response.sendError( HttpServletResponse.SC_NOT_FOUND );
            return;
        }

        String type = getServletContext().getMimeType( fichier.getName() );
        if ( type == null ) {
            type = "application/octet-stream";
        }

    }
}
