<%@ taglib prefix="f" uri="http://www.springframework.org/tags/form"%>
<div class="collapse navbar-collapse" id="navbarNav">
	<ul class="navbar-nav">
	<li class="nav-item"><a class="nav-link active"
			aria-current="page"
			href="${pageContext.request.contextPath}/prof/showHome">Home</a></li>

		

		<li class="nav-item dropdown"><a class="nav-link dropdown-toggle"
			href="#" id="navbarScrollingDropdown" role="button"
			data-bs-toggle="dropdown" aria-expanded="false" onclick="showOrnotMenu()"> Scores with excel 
			 </a>
			<ul class="dropdown-menu" aria-labelledby="navbarScrollingDropdown">
			
			 <!-- TODO : for loop 3la hsab les modules li kay9rihom l prof so khsni ntelechargihom f model -->
			
				<li class="dropdown-item"><a class="nav-link"   
					 href="${pageContext.request.contextPath}/prof/showNotes/module=1">Module 1</a></li> <!-- passer le nom du module or id mn hna -->
				<li class="dropdown-item"><a class="nav-link"  href="${pageContext.request.contextPath}/prof/showNotes/module=2"
					>Module 2</a></li>
				<li class="dropdown-item"><a class="nav-link"
					href="${pageContext.request.contextPath}/prof/NotesImport/">import new Scores</a></li>

				


			</ul></li>



            <li class="nav-item">

		<f:form action="${pageContext.request.contextPath}/logout" method="POST">
			
			<button type="submit" class="btn btn-link">logout</button>
			
		 </f:form></li>

		

	</ul>



</div>


