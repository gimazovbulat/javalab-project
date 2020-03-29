<html>
<body>
<form method="post" action="/signUp">
    <input type="text" placeholder="email" name="email" id="email">
    <input type="password" placeholder="password" name="password" id="password">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    <input type="submit">
    <#if status??>
        ${status}
    </#if>
</body>
</html>