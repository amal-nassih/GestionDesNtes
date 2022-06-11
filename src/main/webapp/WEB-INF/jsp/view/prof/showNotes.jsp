<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ taglib prefix="f" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/security/tags"%>


<jsp:include page="../fragments/userheader.jsp" />
<div class="container">

	<nav class="navbar navbar-expand-lg navbar-light bg-light">
		<div class="container-fluid">



			<jsp:include page="../fragments/profMenu.jsp" /> 

		</div>
	</nav>






	<div>
		<h3>Les Notes des Etudiants dans le module : </h3>  ${module}   <!-- 9bl mat acceder l had l page f le controlleur enregistrer le module dans model -->
		
		<!-- for loop de thymeleaf -->
		
	 	<table class="table">
		
			<thead>
			
					<tr>
					   <th>Nom : </th>
					   
					   <th>Note</th> <!-- for loop 3la les elements (notes de elements 1 ...) -->
					    <th>Moyenne :</th>
					   <th>Validation : </th>
					</tr>
			
			</thead>
		
			<tbody>
			 <!-- etudiant in etudiants list : afficher note 1 : ... moyenne : , validation : -->
		 	</tbody>
			
		</table>
		
		


<jsp:include page="../fragments/userfooter.jsp" />

