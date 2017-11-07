<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page import="Oauth.FBConnection"%>
<%
	FBConnection fbConnection = new FBConnection();
%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8" />
        <title>Affichage d'un client</title>
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
    	<h1 style="text-align:center">Bienvenue sur le site marchand !</h1>
         <br>  
        <form method="post" action="connexion">
            <fieldset style="margin-left:25%;width:50%;">
                <legend>Connexion</legend>
                <p>Vous pouvez vous connecter via ce formulaire.</p>

                <label for="nom">Adresse email <span class="requis">*</span></label>
                <input type="email" id="email" name="email" value="<c:out value="${client.email}"/>" size="20" maxlength="60" />
                <span class="erreur">${form.erreurs['email']}</span>
                <br>
                

                <label for="motdepasse">Mot de passe <span class="requis">*</span></label>
                <input type="password" id="motdepasse" name="motdepasse" value="<c:out value="${client.motdepasse}"/>" size="20" maxlength="20" />
                <span class="erreur">${form.erreurs['motdepasse']}</span>
                <br>
				  
                <input type="submit" value="Connexion" class="btn waves-effect waves-light" />
                <br>
                <a href="creationClient">
   					<input type="button" value="S'enregistrer" class="btn waves-effect waves-light"/>
				</a>
               <br><br>
                <a style="margin-left:30%;" href="<%=fbConnection.getFBAuthUrl()%>"> 
                	<img  src="HTML/images/facebookloginbutton.png" />
				</a>
				<script>
					  // This is called with the results from from FB.getLoginStatus().
					  function statusChangeCallback(response) {
					    console.log('statusChangeCallback');
					    console.log(response);
					    // The response object is returned with a status field that lets the
					    // app know the current login status of the person.
					    // Full docs on the response object can be found in the documentation
					    // for FB.getLoginStatus().
					    if (response.status === 'connected') {
					      // Logged into your app and Facebook.
					      testAPI();
					    } else {
					      // The person is not logged into your app or we are unable to tell.
					      document.getElementById('status').innerHTML = 'Please log ' +
					        'into this app.';
					    }
					  }
					
					  // This function is called when someone finishes with the Login
					  // Button.  See the onlogin handler attached to it in the sample
					  // code below.
					  function checkLoginState() {
					    FB.getLoginStatus(function(response) {
					      statusChangeCallback(response);
					    });
					  }
					
					  window.fbAsyncInit = function() {
					  FB.init({
					    appId      : '127919564584728',
					    cookie     : true,  // enable cookies to allow the server to access 
					                        // the session
					    xfbml      : true,  // parse social plugins on this page
					    version    : 'v2.8' // use graph api version 2.8
					  });
					
					  // Now that we've initialized the JavaScript SDK, we call 
					  // FB.getLoginStatus().  This function gets the state of the
					  // person visiting this page and can return one of three states to
					  // the callback you provide.  They can be:
					  //
					  // 1. Logged into your app ('connected')
					  // 2. Logged into Facebook, but not your app ('not_authorized')
					  // 3. Not logged into Facebook and can't tell if they are logged into
					  //    your app or not.
					  //
					  // These three cases are handled in the callback function.
					
					  FB.getLoginStatus(function(response) {
					    statusChangeCallback(response);
					  });
					
					  };
					
					  // Load the SDK asynchronously
					  (function(d, s, id) {
					    var js, fjs = d.getElementsByTagName(s)[0];
					    if (d.getElementById(id)) return;
					    js = d.createElement(s); js.id = id;
					    js.src = "//connect.facebook.net/en_US/sdk.js";
					    fjs.parentNode.insertBefore(js, fjs);
					  }(document, 'script', 'facebook-jssdk'));
					
					  // Here we run a very simple test of the Graph API after login is
					  // successful.  See statusChangeCallback() for when this call is made.
					  function testAPI() {
					    console.log('Welcome!  Fetching your information.... ');
					    FB.api('/me', function(response) {
					      console.log('Successful login for: ' + response.name);
					      document.getElementById('status').innerHTML =
					        'Thanks for logging in, ' + response.name + '!';
					    });
					  }
					</script>
                
                
                <p class="${empty form.erreurs ? 'succes' : 'erreur'}">${form.resultat}</p>
                
                <%-- Vérification de la présence d'un objet utilisateur en session --%>
                <c:if test="${!empty sessionScope.sessionUtilisateur}">
                    <%-- Si l'utilisateur existe en session, alors on affiche son adresse email. --%>
                    <p class="succes">Vous êtes connecté(e) avec l'adresse : ${sessionScope.sessionUtilisateur.email}</p>
                </c:if>
            </fieldset>
        </form>
        <a href="connexionAdmin">
   					<input type="button" value="Admin" class="btn waves-effect waves-light"/>
				</a>
    </body>
</html>