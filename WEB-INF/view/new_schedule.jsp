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
    <a class="btn btn-primary" role="button" href="user_profile">My profile</a>
    <a class="btn btn-primary" role="button" href="user_schedules">My schedules</a>
    <a class="btn btn-primary" role="button" href="user_recommendation">My recommendations</a>
    <a class="btn btn-primary" role="button" href="user_company">Company information</a>
    <a class="btn btn-primary" role="button" href="index">Sign out</a>
</header>
<form>
    <label>Name of new schedule: <input required type="text" placeholder="Schedule's name" formmethod="post" name="name"></label>   <br>
    <label>How many times usually do you having a meal during the day(from 2 to 5 times): <input required type="text" placeholder="3" formmethod="post" name="meals"></label>  <br>
    <label>When do you usually start your work: <input required type="text" placeholder="08-00" formmethod="post" name="work_start"></label>    <br>
    <label>When do you usually end your work: <input required type="text" placeholder="18-00" formmethod="post" name="work_end"></label>   <br>
    <label>When do you usually waking up: <input required type="text" placeholder="06-00" formmethod="post" name="sleep_over_time"></label> <br>
    <label>When do you usually going to sleep: <input required type="text" placeholder="23-00" formmethod="post" name="sleep_time"></label> <br>
    <label class="checkbox-group required">Select your working days: <br>
        <label>Monday <input checked type="checkbox" name="work_days" formmethod="post" value="Monday" class="checkbox"></label>        <br>
        <label>Tuesday <input checked type="checkbox" name="work_days" formmethod="post" value="Tuesday" class="checkbox"></label>      <br>
        <label>Wednesday <input checked type="checkbox" name="work_days" formmethod="post" value="Wednesday" class="checkbox"></label>  <br>
        <label>Thursday <input checked type="checkbox" name="work_days" formmethod="post" value="Thursday" class="checkbox"></label>    <br>
        <label>Friday <input checked type="checkbox" name="work_days" formmethod="post" value="Friday" class="checkbox"></label>        <br>
        <label>Saturday <input type="checkbox" name="work_days" formmethod="post" value="Saturday" class="checkbox"></label>    <br>
        <label>Sunday <input type="checkbox" name="work_days" formmethod="post" value="Sunday" class="checkbox"></label>
    </label>
    <button class="btn btn-primary" type="submit" formmethod="post">Create new schedule</button>
</form>
<a class="btn btn-primary" role="button" href="user_schedules">Go back</a>
</body>
</html>