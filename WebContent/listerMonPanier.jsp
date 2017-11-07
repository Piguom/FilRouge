<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix = "fmt" %>

	        <c:choose>
	            <%-- Si aucune commande n'existe en session, affichage d'un message par défaut. --%>
	            <c:when test="${ empty paniers }">
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
	                <c:forEach items="${ paniers }" var="mapPaniers" varStatus="boucle">
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