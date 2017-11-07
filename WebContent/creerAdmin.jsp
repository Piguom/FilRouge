<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8" />
        <title>Création d'un admin</title>
        <link type="text/css" rel="stylesheet" href="<c:url value="/inc/style.css"/>" />
        
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
    <!--   <c:import url="/inc/menu.jsp" />  --> 
    <h3 style="text-align:center">Création de votre compte administrateur</h3>
         <br>  
        <div>
            <form method="post" action="<c:url value="/creationAdmin"/>">
                <fieldset style="width:50%; margin-left:25%;">
                    <legend>Informations admin</legend>
                    <c:import url="/inc/inc_admin_form.jsp" />
                </fieldset>  
                <fieldset style="width:50%; margin-left:25%;">
                    <p class="info">${ form.resultat }</p>
             	   <input type="submit" value="Valider"  class="btn waves-effect waves-light"/>
              	  <input type="reset" value="Remettre à zéro" class="btn waves-effect waves-light"/> 
                	<a href="<c:url value="connexion"/>">
                		<input type="button" value="Précédent" class="btn waves-effect waves-light" />
                	</a>
                	<br />
                </fieldset>
            </form>
        </div>
    </body>
</html>