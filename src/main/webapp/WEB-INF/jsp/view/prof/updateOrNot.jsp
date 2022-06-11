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





    <h1 style="color:red;">there is some values in the database are you really ready to lose them !!!! </h1>

	
	<a href="${pageContext.request.contextPath}/prof/updateYes">VALIDER</a>
	------
	<a href="${pageContext.request.contextPath}/prof/dontupdate">ANNULER</a>


<jsp:include page="../fragments/userfooter.jsp" />

