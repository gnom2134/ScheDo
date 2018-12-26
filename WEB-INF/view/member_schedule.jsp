<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css" integrity="sha384-MCw98/SFnGE8fJT3GXwEOngsV7Zt27NXFoaoApmYm81iuXoPkFOJwJ8ERdknLPMO" crossorigin="anonymous">
    <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js" integrity="sha384-ZMP7rVo3mIykV+2+9J3UJ46jBk0WLaUAdn689aCwoqbBJiSnjAK/l8WvCWPIPm49" crossorigin="anonymous"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.min.js" integrity="sha384-ChfqqxuZUCnJSK3+MXmPNIyE6ZbWh2IMqE241rYiqJxyMiZ6OW/JmZQ5stwEULTy" crossorigin="anonymous"></script>
</head>

<body>
<header>
    <a class="btn btn-primary" role="button" href="company_profile">Company profile</a>
    <a class="btn btn-primary" role="button" href="company_schedules">Company schedules</a>
    <a class="btn btn-primary" role="button" href="company_members">Company members</a>
    <a class="btn btn-primary" role="button" href="index">Sign out</a>
</header>

<c:forEach items="${days}" var="day">
    <div style="border: 2px solid aquamarine; background: coral; float: left; margin: 0 15px 15px 0; padding: 10px;">
        <h2>${day.day_of_the_week}</h2>
        <ul>
            <c:forEach items="${day.items}" var="item">
                <li>${item.activity} - ${item.end_time}</li>
            </c:forEach>
        </ul>
    </div>
</c:forEach>
</body>
</html>