<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<label for="nomProduit">Nom <span class="requis">*</span></label>
<input type="text" id="nomProduit" name="nomProduit" value="<c:out value="${produit.nom}"/>" size="30" maxlength="30" />
<span class="erreur">${form.erreurs['nomProduit']}</span>
<br />

<label for="constructeurProduit">Constructeur <span class="requis">*</span></label>
<input type="text" id="constructeurProduit" name="constructeurProduit" value="<c:out value="${produit.constructeur}"/>" size="30" maxlength="30" />
<span class="erreur">${form.erreurs['constructeurProduit']}</span>
<br />

<label for="quantiteProduit">Quantite <span class="requis">*</span></label>
<input type="text" id="quantiteProduit" name="quantiteProduit" value="<c:out value="${produit.quantite}"/>" size="30" maxlength="30" />
<span class="erreur">${form.erreurs['quantiteProduit']}</span>
<br />

<label for="imageProduit">Image</label>
<input type="file" id="imageProduit" name="imageProduit" />
<span class="erreur">${form.erreurs['imageProduit']}</span>
<br />