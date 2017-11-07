<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix = "fmt" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8" />
        <title>Liste des produits</title>
        <link type="text/css" rel="stylesheet" href="<c:url value="/inc/style.css"></c:url>" />
        
        <!--Bootstrap header references-->
		<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-alpha.6/css/bootstrap.min.css" integrity="sha384-rwoIResjU2yc3z8GV/NPeZWAv56rSmLldC3R/AZzGRnGxQQKnKkoFVhFQhNUwEyJ" crossorigin="anonymous">
		<script src="https://code.jquery.com/jquery-3.1.1.slim.min.js" integrity="sha384-A7FZj7v+d/sdmMqp/nOQwliLvUsJfDHW+k9Omg/a/EheAdgtzNs3hpfag6Ed950n" crossorigin="anonymous"></script>
		<script src="https://cdnjs.cloudflare.com/ajax/libs/tether/1.4.0/js/tether.min.js" integrity="sha384-DztdAPBWPRXSA/3eYEEUWrWCy7G5KFbe8fFjk5JAIxUYHKkDx6Qin1DkWx51bBrb" crossorigin="anonymous"></script>
		<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-alpha.6/js/bootstrap.min.js" integrity="sha384-vBWWzlZJ8ea9aCX4pEW3rVHjgjt7zpkNpZk+02D9phzyeVkE+jo0ieGizqPLForn" crossorigin="anonymous"></script>
		
		<!--Meterialize header references -->
	  	<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/materialize/0.100.2/css/materialize.min.css">
	  	<script src="https://cdnjs.cloudflare.com/ajax/libs/materialize/0.100.2/js/materialize.min.js"></script>
        
    </head>
    <body>
      <%-- <c:import url="/inc/menu.jsp" ></c:import> 
      <h1 style="text-align:center;">Liste de vos commandes ${ client.prenom } ${ client.nom } !</h1> 
    	<c:import url="/HTML/header.html" />--%> 
    	<h3 style="text-align:center;">Liste des produits</h3> 
         <br>  
        <c:choose>
            <%-- Si aucune commande n'existe en session, affichage d'un message par défaut. --%>
            <c:when test="${ empty produits }">
                <p class="erreur">Aucun produit enregistré.</p>
            </c:when>
            <%-- Sinon, affichage du tableau. --%>
            <c:otherwise>
            <table style="margin-left:25%; width:50%;">
                <tr>
                   	<th>Nom</th>
                    <th>Constructeur</th>
                    <th>Quantite</th>
                    <th>Image</th>
                    <th class="action">Action</th>                
                </tr>
                <%-- Parcours de la Map des commandes en session, et utilisation de l'objet varStatus. --%>
                <c:forEach items="${ produits }" var="mapProduits" varStatus="boucle">
                <%-- Simple test de parité sur l'index de parcours, pour alterner la couleur de fond de chaque ligne du tableau. --%>
                <tr class="${boucle.index % 2 == 0 ? 'pair' : 'impair'}">
                    <%-- Affichage des propriétés du bean Commande, qui est stocké en tant que valeur de l'entrée courante de la map --%>
          		     <td><c:out value="${ mapProduits.value.nom}"></c:out></td>
                    <td><c:out value="${mapProduits.value.constructeur}"></c:out></td>
                    <td><c:out value="${ mapProduits.value.quantite }"></c:out></td>
                    <td><img src="images/${ mapProduits.value.image }" width="100" height="70"></td>
                    <%-- Lien vers la servlet de suppression, avec passage de la date de la commande - c'est-à-dire la clé de la Map - en paramètre grâce à la balise <c:param></c:param>. --%>
                   <td class="action">
                        <a href="<c:url value="suppressionPanier"><c:param name="idCommande" value="${ mapProduits.key }" ></c:param></c:url>">
                            <img src="<c:url value="/inc/supprimer.png"></c:url>" alt="Supprimer" />
                        </a>
                    </td>  
                </tr>
                </c:forEach>
            </table>
            </c:otherwise>
        </c:choose>
        <br><br>
            <fieldset style="width:50%; margin-left:25%;">
                    <legend>Creer un produit</legend>
					<a href="<c:url value="creationProduit"/>">
   						<input type="button" value="Creer un produit" class="btn waves-effect waves-light"/>
					</a>
            	</fieldset>
        <br><br>
            <fieldset style="width:50%; margin-left:25%;">
                    <legend>Les clients</legend>
                    <a href="<c:url value="listeClients"/>">
   						<input type="button" value="Voir toutes les clients" class="btn waves-effect waves-light"/>
					</a>
            </fieldset >
            <br><br>
            <fieldset style="width:50%; margin-left:25%;">
                    <legend>Mon compte</legend>
					<a href="<c:url value="accesAdmin.jsp"/>">
   						<input type="button" value="Mon compte" class="btn waves-effect waves-light"/>
					</a>
            	</fieldset>
    </body>
</html>