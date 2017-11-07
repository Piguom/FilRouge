<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8" />
        <title>Affichage d'une commande</title>
        <link type="text/css" rel="stylesheet" href="<c:url value="/inc/style.css"></c:url>" />
    </head>
    <body>
    	<h1 style="text-align:center;">Votre nouvelle commande ${ client.prenom } ${ client.nom } !</h1>
         <br>
    <!--    <c:import url="/inc/menu.jsp" ></c:import> --> 
        <fieldset>
                    <legend>Ma commande</legend>
            			<table>
            				<tr>
            					<th>Date</th>
                    			<th>Montant</th>
                    			<th>Mode de paiement</th>
                   				<th>Statut du paiement</th>
                    			<th>Mode de livraison</th>
                    			<th>Statut de la livraison</th>    
            				</tr>
            				<tr>
            					<td><c:out value="${ commande.date }"></c:out></td>
            					<td><c:out value="${ commande.montant }"></c:out></td>
            					<td><c:out value="${ commande.modePaiement }"></c:out></td>
            					<td><c:out value="${ commande.statutPaiement }"></c:out></td>
            					<td><c:out value="${ commande.modeLivraison }"></c:out></td>
            					<td><c:out value="${ commande.statutLivraison }"></c:out></td>
            				</tr>
            			</table>
            </fieldset>
    </body>
</html>