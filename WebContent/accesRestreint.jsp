<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page import="Oauth.FBConnection"%>
<%FBConnection fbConnection = new FBConnection(); %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8" />
        <title>Accès restreint</title>
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
        <h3 style="text-align:center;">Bienvenue ${ client.prenom } ${ client.nom } dans votre espace personnel !</h3>
    	<c:import url="/HTML/header.html" />
         <br>         
         	<fieldset style="margin-left:20%; width:60%;">
                    <legend>
                    	Mes informations personnelles
                    	<c:if test="${!empty sessionScope.clients}">
                    		<a href="<c:url value="modificationClient"/>">
                    			<i class="little material-icons">border_color</i>
                    		</a>
                    	</c:if>
                    </legend>
                    <c:if test="${!empty sessionScope.clients}">
                	  <table>
                		<tr>
                   			<th>Nom</th>
                    		<th>Prénom</th>
                    		<th>Adresse</th>
                    		<th>Téléphone</th>
                   			<th>Email</th>
                   			<c:if test="${ !empty client.image }">
                    			<th>Image</th>
                    		</c:if>
                  <%--    		<th class="action">Action</th>         --%>                   
                		</tr>
                		<tr>
                		<%-- Affichage des propriétés du bean Client, qui est stocké en tant que valeur de l'entrée courante de la map --%>
                    		<td><c:out value="${ client.nom }"></c:out></td>
                    		<td><c:out value="${ client.prenom }"></c:out></td>
                    		<td><c:out value="${ client.adresse }"></c:out></td>
                    		<td><c:out value="${ client.telephone }"></c:out></td>
                    		<td><c:out value="${ client.email }"></c:out></td>
                    		<td>
                        		<%-- On ne construit et affiche un lien vers l'image que si elle existe.--%>
                        		<c:if test="${ !empty client.image }">
                        		
                    				<img src="images/${ client.image }" width="100" height="70">
                            	<%--	<c:set var="image"><c:out value="${ client.image }"></c:out></c:set>
                            		<a href="<c:url value="/images/${ image }"/>">Voir</a>--%>
                        		</c:if> 
                    		</td>
                    		<%-- Lien vers la servlet de suppression, avec passage du nom du client - c'est-à-dire la clé de la Map - en paramètre grâce à la balise <c:param></c:param>. --%>
                    <%--  		<td class="action">
                        		<a href="<c:url value="/suppressionClient"><c:param name="idClient" value="${ client.id }" ></c:param></c:url>">
                            		<img src="<c:url value="/inc/supprimer.png"></c:url>" alt="Supprimer" />
                       			</a>
                    		</td>  --%>      
                		</tr>
            		</table>
    			</c:if>
            </fieldset>
            <br><br>
            <fieldset style="margin-left:20%; width:60%;">
                    <legend>Mes commandes</legend>
                    <c:choose>
            			<%-- Si aucune commande n'existe en session, affichage d'un message par défaut. --%>
            			<c:when test="${ empty commandes }">
                			<p class="erreur">Aucune commande enregistrée pour votre compte.</p>
            			</c:when>
            			<%-- Sinon, affichage du tableau. --%>
            			<c:otherwise>
            			<c:import url="listeCommandes"/>
            			<%--	<td>
            					<a href="<c:url value="listeCommandes"/>">
   									<input type="button" value="Voir mes commandes" class="sansLabel"/>
								</a>
            				</td>--%>
            			</c:otherwise>
					</c:choose>
            </fieldset>
             <br><br>
            <fieldset style="margin-left:20%; width:60%;">
                    <legend>Mon panier</legend>
                    <c:choose>
            			<%-- Si aucune commande n'existe en session, affichage d'un message par défaut. --%>
            			<c:when test="${ empty paniers }">
                			<p class="erreur">Aucun produit enregistré dans votre panier.</p>
            			</c:when>
            			<%-- Sinon, affichage du tableau. --%>
            			<c:otherwise>
            				<c:import url="/listerMonPanier.jsp"/>
            				<br>
            				<a  data-toggle="modal" href="#infos" style="float:right;">
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
												<a href="accesRestreint.jsp">
			   										<input type="button" value="Annuler" class="btn waves-effect waves-light"/>
												</a>
						                    </div>
						                </div>
			        			</div>
            			</c:otherwise>
					</c:choose>
            </fieldset>
            <br><br>
    		</body>
</html>