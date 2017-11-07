<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8" />
        <title>Produits</title>
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
    	<h3 style="text-align:center;">Liste des produits</h3>
    	<c:import url="/HTML/header.html" />
      	<br>
      	<c:choose>
            <%-- Si aucun client n'existe en session, affichage d'un message par défaut. --%>
            <c:when test="${ empty produits }">
                <p class="erreur">Aucun produits enregistrés.</p>
            </c:when>
            <%-- Sinon, affichage du tableau. --%>
            <c:otherwise>
            	<c:forEach items="${ produits }" var="mapProduits" varStatus="boucle">
            		<div class="col">
			      			<div class="card little" style="margin-left:10px;width:10%;height:100%;float:left;">
						        <div class="card-image" style="width:100%; height:105%;">
	                    			<img src="images/${ mapProduits.value.image }" >
						         <%-- <span class="card-title" style="color:black;" ><c:out value="${mapProduits.value.nom}"/></span>--%> 
						          <a class="btn-floating halfway-fab waves-effect waves-light red" href="creationPanier?id=${mapProduits.value.id}">
						          	<i class="material-icons">add</i>
						          </a>
						        </div>
						        <div class="card-content">
						          <p>Fabriquant : <b><c:out value="${mapProduits.value.constructeur}"/></b></p>
						          <p>Quantité restante : <b><c:out value="${mapProduits.value.quantite}"/></b></p>
						        </div>
						    </div>
					</div>
				</c:forEach>
            </c:otherwise>            
       	 </c:choose>
		<br>
    </body>
</html>