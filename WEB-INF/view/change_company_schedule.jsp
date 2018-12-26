<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css" integrity="sha384-MCw98/SFnGE8fJT3GXwEOngsV7Zt27NXFoaoApmYm81iuXoPkFOJwJ8ERdknLPMO" crossorigin="anonymous">
    <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js" integrity="sha384-ZMP7rVo3mIykV+2+9J3UJ46jBk0WLaUAdn689aCwoqbBJiSnjAK/l8WvCWPIPm49" crossorigin="anonymous"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.min.js" integrity="sha384-ChfqqxuZUCnJSK3+MXmPNIyE6ZbWh2IMqE241rYiqJxyMiZ6OW/JmZQ5stwEULTy" crossorigin="anonymous"></script>
    <script>

        function addActivity(dayId) {
            let list = document.getElementById("list_" + dayId).innerHTML;
            document.getElementById("list_" + dayId).innerHTML = list + "<li>\n" +
                "                    <label>Delete? <input type=\"checkbox\" formmethod=\"post\" name=\"is_delete\" value=\"0\"></label>\n" +
                "                    <label>Activity: <input type=\"text\" formmethod=\"post\" name=\"activity\" required></label> -\n" +
                "                    <label>Time: <input type=\"text\" pattern=\"[0-2][0-9]-[0-5][0-9]\" formmethod=\"post\" name=\"time\" required></label>\n" +
                "                    <label><input type=\"hidden\" name=\"item_id\" formmethod=\"post\" value=\"0\" required></label>\n" +
                "                </li>"
        }

    </script>
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
        <button onclick="addActivity(${day.id})" class="btn btn-primary">+</button>
        <form>
            <h2>${day.day_of_the_week}</h2><label><input type="hidden" formmethod="post" name="day_id" value="${day.id}"></label>
            <ul id="list_${day.id}">
                <c:forEach items="${day.items}" var="item">
                    <li>
                        <label>Delete? <input type="checkbox" formmethod="post" name="is_delete" value="${item.id}"></label>
                        <label>Activity: <input type="text" formmethod="post" value="${item.activity}" name="activity"></label> -
                        <label>Time: <input pattern="[0-2][0-9]-[0-5][0-9]" type="text" formmethod="post" value="${item.end_time}" name="time"></label>
                        <label><input type="hidden" name="item_id" formmethod="post" value="${item.id}"></label>
                    </li>
                </c:forEach>
            </ul>
            <input class="btn btn-primary" role="button" type="submit" formmethod="post">
        </form>
            <%--<form>--%>
            <%--<label>New activity: <input required type="text" placeholder="Enter new activity" formmethod="post" name="new_activity"></label>--%>
            <%--<label>Time: <input required type="text" placeholder="Enter time" formmethod="post" name="new_time"></label>--%>
            <%--<input class="btn btn-primary" role="button" type="submit" formmethod="post">--%>
            <%--</form>--%>
    </div>
    <%--<div style="border: 2px solid aquamarine; background: coral; float: left; margin: 0 15px 15px 0; padding: 10px;">--%>
        <%--<form>--%>
            <%--<h2>${day.day_of_the_week}</h2><label><input type="hidden" formmethod="post" name="day_id" value="${day.id}"></label>--%>
            <%--<ul>--%>
                <%--<c:forEach items="${day.items}" var="item">--%>
                    <%--<li>--%>
                        <%--<label>Activity: <input type="text" formmethod="post" value="${item.activity}" name="activity"></label> ---%>
                        <%--<label>Time: <input type="text" formmethod="post" value="${item.end_time}" name="time"></label>--%>
                        <%--<label><input type="hidden" name="item_id" formmethod="post" value="${item.id}"></label>--%>
                    <%--</li>--%>
                <%--</c:forEach>--%>
                <%--<input class="btn btn-primary" role="button" type="submit" formmethod="post">--%>
            <%--</ul>--%>
        <%--</form>--%>
        <%--<form>--%>
            <%--<label>New activity: <input required type="text" placeholder="Enter new activity" formmethod="post" name="new_activity"></label>--%>
            <%--<label>Time: <input required type="text" placeholder="Enter time" formmethod="post" name="new_time"></label>--%>
            <%--<input class="btn btn-primary" role="button" type="submit" formmethod="post">--%>
        <%--</form>--%>
    <%--</div>--%>
</c:forEach>

</body>
</html>