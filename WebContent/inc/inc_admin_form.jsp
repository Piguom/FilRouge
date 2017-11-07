<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<label for="nomAdmin">Nom <span class="requis">*</span></label>
<input type="text" id="nomAdmin" name="nomAdmin" value="<c:out value="${admin.nom}"/>" size="30" maxlength="30" />
<span class="erreur">${form.erreurs['nomAdmin']}</span>
<br />

<label for="prenomAdmin">Prénom <span class="requis">*</span></label>
<input type="text" id="prenomAdmin" name="prenomAdmin" value="<c:out value="${admin.prenom}"/>" size="30" maxlength="30" />
<span class="erreur">${form.erreurs['prenomAdmin']}</span>
<br />

<label for="motdepasseAdmin">Mot de Passe <span class="requis">*</span></label>
<input type="password" id="motdepasseAdmin" name="motdepasseAdmin" value="<c:out value="${admin.motdepasse}"/>" size="30" maxlength="30" />
<span class="erreur">${form.erreurs['motdepasseAdmin']}</span>
<br />

<label for="emailAdmin">Adresse email <span class="requis">*</span></label>
<input type="email" id="emailAdmin" name="emailAdmin" value="<c:out value="${admin.email}"/>" size="30" maxlength="60" />
<span class="erreur">${form.erreurs['emailAdmin']}</span>
<br />