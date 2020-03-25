<form action="/signIn" method="post">
    <input name="email" placeholder="email" type="text">
    <input name="password" type="password" placeholder="password">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    <input type="submit">
</form>
