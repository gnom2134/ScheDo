<html>
<head>
    <title>Log in window</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css" integrity="sha384-MCw98/SFnGE8fJT3GXwEOngsV7Zt27NXFoaoApmYm81iuXoPkFOJwJ8ERdknLPMO" crossorigin="anonymous">
    <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js" integrity="sha384-ZMP7rVo3mIykV+2+9J3UJ46jBk0WLaUAdn689aCwoqbBJiSnjAK/l8WvCWPIPm49" crossorigin="anonymous"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.min.js" integrity="sha384-ChfqqxuZUCnJSK3+MXmPNIyE6ZbWh2IMqE241rYiqJxyMiZ6OW/JmZQ5stwEULTy" crossorigin="anonymous"></script>
</head>

<body>
<header>
    <a class="btn btn-primary" role="button" href="index">ScheDo</a>
    <a class="btn btn-primary" role="button" href="log_in">Sign in</a>
    <a class="btn btn-primary" role="button" href="registration">Registration</a> <br>
</header>
<h1>Log in window</h1>
<div>
    <form>
        <div>
            <label>Login</label>
            <input type="text" placeholder="login example" formmethod="post" name="login">
        </div>
        <div>
            <label>Password</label>
            <input type="password" placeholder="Password" formmethod="post" name="password">
        </div>
        <button class="btn btn-primary" type="submit" formmethod="post">Sign in</button>
    </form>
    <div >
        <a class="btn btn-primary" role="button" href="registration">New around here? Sign up</a>
    </div>
</div>
</body>
</html>