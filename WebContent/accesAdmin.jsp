<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8" />
        <title>Accès Admin</title>
        <link type="text/css" rel="stylesheet" href="<c:url value="/inc/style.css"></c:url>" />
        
         <!--Bootstrap header references-->
		<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-alpha.6/css/bootstrap.min.css" integrity="sha384-rwoIResjU2yc3z8GV/NPeZWAv56rSmLldC3R/AZzGRnGxQQKnKkoFVhFQhNUwEyJ" crossorigin="anonymous">
		<script src="https://code.jquery.com/jquery-3.1.1.slim.min.js" integrity="sha384-A7FZj7v+d/sdmMqp/nOQwliLvUsJfDHW+k9Omg/a/EheAdgtzNs3hpfag6Ed950n" crossorigin="anonymous"></script>
		<script src="https://cdnjs.cloudflare.com/ajax/libs/tether/1.4.0/js/tether.min.js" integrity="sha384-DztdAPBWPRXSA/3eYEEUWrWCy7G5KFbe8fFjk5JAIxUYHKkDx6Qin1DkWx51bBrb" crossorigin="anonymous"></script>
		<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-alpha.6/js/bootstrap.min.js" integrity="sha384-vBWWzlZJ8ea9aCX4pEW3rVHjgjt7zpkNpZk+02D9phzyeVkE+jo0ieGizqPLForn" crossorigin="anonymous"></script>
        
        <!--Meterialize header references -->
 	 	<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/materialize/0.100.2/css/materialize.min.css">
  		<script src="https://cdnjs.cloudflare.com/ajax/libs/materialize/0.100.2/js/materialize.min.js"></script>
 		<link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
        
    </head>
    <body>
        <h3 style="text-align:center;">Bienvenue ${ admin.prenom } ${ admin.nom } dans votre espace administrateur !</h3>
         <br>
         	<fieldset style="width:50%; margin-left:25%;">
                    <legend>Mes informations personnelles</legend>
                    <c:if test="${!empty sessionScope.admins}">
                	  <table>
                		<tr>
                   			<th>Nom</th>
                    		<th>Prénom</th>
                   			<th>Email</th>
                    		<th class="action">Supprimer mon compte</th>                    
                		</tr>
                		<tr>
                		<%-- Affichage des propriétés du bean Client, qui est stocké en tant que valeur de l'entrée courante de la map --%>
                    		<td><c:out value="${ admin.nom }"></c:out></td>
                    		<td><c:out value="${ admin.prenom }"></c:out></td>
                    		<td><c:out value="${ admin.email }"></c:out></td>
                    		<%-- Lien vers la servlet de suppression, avec passage du nom du client - c'est-à-dire la clé de la Map - en paramètre grâce à la balise <c:param></c:param>. --%>
                    		<td class="action">
                        		<a href="<c:url value="/suppressionAdmin"><c:param name="idAdmin" value="${ admin.id }" ></c:param></c:url>">
                            		<img src="<c:url value="/inc/supprimer.png"></c:url>" alt="Supprimer" />
                       			</a>
                    		</td>
                		</tr>
            		</table>
    			</c:if>
    			<a href="<c:url value="/deconnexion"/>">
   						<input type="button" value="Se déconnecter" class="btn waves-effect waves-light"/>
					</a>
            </fieldset >
            <br><br>
            <fieldset style="width:50%; margin-left:25%;">
                    <legend>Les commandes</legend>
                    <a href="<c:url value="listeToutesCommandes"/>">
   						<input type="button" value="Voir toutes les commandes" class="btn waves-effect waves-light"/>
					</a>
            </fieldset >
            <br><br>
            <fieldset style="width:50%; margin-left:25%;">
                    <legend>Les utilisateurs</legend>
					<a href="<c:url value="listeClients"/>">
   						<input type="button" value="Voir tous les clients" class="btn waves-effect waves-light"/>
					</a>
            	</fieldset>
            	<br><br>
            <fieldset style="width:50%; margin-left:25%;">
                    <legend>Les produits</legend>
					<a href="<c:url value="listeProduit"/>">
   						<input type="button" value="Voir tous les produits" class="btn waves-effect waves-light"/>
					</a>
            	</fieldset>
    		</body>
</html>