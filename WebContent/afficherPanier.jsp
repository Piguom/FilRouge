<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix = "fmt" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8" />
        <title>Mon panier</title>
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
      <%-- <c:import url="/inc/menu.jsp" ></c:import>  --%> 
      <h3 style="text-align:center;">Mon Panier </h3> 
    	<c:import url="/HTML/header.html" />
         <br> 
         <fieldset style="width:50%; margin-left:25%;">
         	<legend>Panier</legend>
	        <c:choose>
	            <%-- Si aucune commande n'existe en session, affichage d'un message par défaut. --%>
	            <c:when test="${ empty paniers}">
	                <p class="erreur">Aucune produit enregistré dans votre panier.</p>
	            </c:when>
	            <%-- Sinon, affichage du tableau. --%>
	            <c:otherwise>
	            <table>
	                <tr>
	                    <th>Date</th>
	                    <th>Nom</th>
	                    <th>Fabriquant</th>
	                    <th>Quantite</th>
	                    <th>Image</th>
	   	                <th class="action">Supprimer</th>              
	                </tr>
	                <%-- Parcours de la Map des commandes en session, et utilisation de l'objet varStatus. --%>
	                <c:forEach items="${ paniers}" var="mapPaniers" varStatus="boucle">
	                <%-- Simple test de parité sur l'index de parcours, pour alterner la couleur de fond de chaque ligne du tableau. --%>
	                <tr class="${boucle.index % 2 == 0 ? 'pair' : 'impair'}">
	                    <%-- Affichage des propriétés du bean Commande, qui est stocké en tant que valeur de l'entrée courante de la map --%>
	            <%--      <td><c:out value="${ mapCommandes.client.prenom } ${ mapCommandes.client.nom }"></c:out></td>--%>
	                    <td><c:out value="${mapPaniers.value.date}"></c:out></td>
	                    <td><c:out value="${ mapPaniers.value.nom }"></c:out></td>
	                    <td><c:out value="${ mapPaniers.value.constructeur }"></c:out></td>
	                    <td><c:out value="${ mapPaniers.value.quantite }"></c:out></td>
	                    <td><img src="images/${ mapPaniers.value.image }" width="90" height="70"></td>
	                    <%-- Lien vers la servlet de suppression, avec passage de la date de la commande - c'est-à-dire la clé de la Map - en paramètre grâce à la balise <c:param></c:param>. --%>
	                   <td class="action">
	                        <a href="<c:url value="suppressionPanier"><c:param name="idProduit" value="${ mapPaniers.key }" ></c:param></c:url>">
	                            <img src="<c:url value="/inc/supprimer.png"></c:url>" alt="Supprimer" />
	                        </a>
	                    </td>  
	                </tr>
	                </c:forEach>
	            </table>
	            </c:otherwise>
	        </c:choose>
	       </fieldset>
	       <br><br>
            <fieldset style="width:50%; margin-left:25%;">
                    <legend>Valider le panier</legend>
					<a  data-toggle="modal" href="#infos">
   						<input type="button" value="Passer commande" class="btn waves-effect waves-light"/>
					</a>
					<div class="modal" id="infos" style="width:60%;height:90%;">
			                <div class="modal-content">
			                    <div class="modal-header">
			                        <button type="button" class="close" data-dismiss="modal" style="float:right;">x</button>
			                    </div>
			                    <div class="modal-body">
			                        <h4 class="modal-title">Vous vous appr&ecirc;tez à commander ces articles :</h4>
			                        <table>
						                <tr>
						                    <th>Nom</th>
						                    <th>Fabriquant</th>
						                    <th>Quantite</th>
						                    <th>Image</th>            
						                </tr>
						                <%-- Parcours de la Map des commandes en session, et utilisation de l'objet varStatus. --%>
						                <c:forEach items="${ paniers}" var="mapPaniers" varStatus="boucle">
							                <%-- Simple test de parité sur l'index de parcours, pour alterner la couleur de fond de chaque ligne du tableau. --%>
							                <tr class="${boucle.index % 2 == 0 ? 'pair' : 'impair'}">
							                    <%-- Affichage des propriétés du bean Commande, qui est stocké en tant que valeur de l'entrée courante de la map --%>
							                    <td><c:out value="${ mapPaniers.value.nom }"></c:out></td>
							                    <td><c:out value="${ mapPaniers.value.constructeur }"></c:out></td>
							                    <td><c:out value="${ mapPaniers.value.quantite }"></c:out></td>
							                    <td><img src="images/${ mapPaniers.value.image }" width="90" height="70"></td>
							                </tr>
						                </c:forEach>
	            					</table>
			                    </div>
			                    <div class="modal-body">
			                        <h4 class="modal-title">Ils seront livr&eacute;s &agrave; cette adresse :</h4>
			                        <table>
						                <tr>
						                    <th>Nom</th>
						                    <th>Prenom</th>
						                    <th>Adresse</th>          
						                </tr>
							            <tr class="${boucle.index % 2 == 0 ? 'pair' : 'impair'}">
							                 <%-- Affichage des propriétés du bean Commande, qui est stocké en tant que valeur de l'entrée courante de la map --%>
							                 <td><c:out value="${client.nom }"></c:out></td>
							                 <td><c:out value="${ client.prenom }"></c:out></td>
							                 <td><c:out value="${ client.adresse }"></c:out></td>
							             </tr>
	            					</table>
			                    </div>
			                    <div class="modal-footer">
			                    	<a href="#">
   										<input type="button" value="Valider" class="btn waves-effect waves-light"/>
									</a>
									<a href="listePanier">
   										<input type="button" value="Annuler" class="btn waves-effect waves-light"/>
									</a>
			                    </div>
			                </div>
        			</div>
            	</fieldset>
        <br><br>
    </body>
</html>