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

			<form action="${pageContext.request.contextPath}/prof/importExcel" method="post" enctype="multipart/form-data">  
				Select File: <input type="file" name="file"/>  <br>
				<input type="submit" value="Upload File"/>  
			</form>  
			 


<jsp:include page="../fragments/userfooter.jsp" />

