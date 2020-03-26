<html>
<head>
    <meta name="_csrf" content="${_csrf.token}"/>
    <!-- default header name is X-CSRF-TOKEN -->
    <meta name="_csrf_header" content="${_csrf.headerName}"/>
    <title>Sign Up</title>
    <script
            src="https://code.jquery.com/jquery-3.4.1.js"
            integrity="sha256-WpOohJOqMqqyKL9FccASB9O0KwACQJpFTUBLTYOVvVU="
            crossorigin="anonymous"></script>
    <title>Document</title>
    <script>
        function signUp() {
            // данные для отправки
            let formData = new FormData();
            // забрал файл из input
            let file = ($('#file'));
            let email = ($('#email'));
            let password = ($('#password'));
            // добавляю файл в formData-+
            formData.append("email", email);
            formData.append("password", password);
            formData.append("file", file);
            var token = $("meta[name='_csrf']").attr("content");
            var header = $("meta[name='_csrf_header']").attr("content");
            $.ajax({
                beforeSend: function(xhr){xhr.setRequestHeader(header, token);},
                type: "POST",
                url: "http://localhost:8080/signUp",
                data: formData,
                processData: false,
                contentType: false
            })
                .fail(function (mes) {
                    alert(mes)
                });
        }
    </script>
</head>
<body>
<form method="post" action="/signUp" enctype="multipart/form-data">
    <input type="text" placeholder="email" name="email" id="email">
    <input type="password" placeholder="password" name="password" id="password">
    <input type="file" id="file" name="file" placeholder="File name"/>
    <button onclick="signUp()">
        sign up
    </button></form>
<#if status??>
    ${status}
</#if>
</body>
</html>