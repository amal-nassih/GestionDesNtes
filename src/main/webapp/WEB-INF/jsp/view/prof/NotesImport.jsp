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

         

        
           
           <f:form action="${pageContext.request.contextPath}/prof/records/export/excel" modelAttribute="module" method="GET">
			     <f:label path="session"> Module : </f:label>
			     <f:input path="name" placeHolder="enter the module name ... "/> </br>
			     
			     <f:label path="niveau"> Niveau : </f:label>
			     <f:select path="niveau">
			     <f:option value="1">1</f:option>
			     <f:option value="2">2</f:option>
			     <f:option value="3">3</f:option>
			     		  </f:select> </br>
			     
			     <f:label path="session"> Session : </f:label>
			     <f:select path="session">
			     <f:option value="SN">normale</f:option>
			     <f:option value="SR">rattrapage</f:option>
		  </f:select>

			     
			     <f:hidden path="done"  value="1"/>
			     <br>
			     <button type="submit">Submit</button>
			</f:form>
			
			<c:if test="${1==1}">
			     <a href="${pageContext.request.contextPath}/prof/ScoresImport/">Export Done   </a>
			</c:if>
           
		
		


<jsp:include page="../fragments/userfooter.jsp" />

