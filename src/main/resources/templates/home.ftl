<html>
<head>
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
            console.log(email);
            console.log(password);
            // добавляю файл в formData
            formData.append("email", email);
            formData.append("password", password);
            formData.append("file", file);

            $.ajax({
                type: "POST",
                url: "http://localhost:8080/signUp",
                data: formData,
                processData: false,
                contentType: false
            })
                .done(function () {
                    alert("link was sent to your email :)")
                })
                .fail(function () {
                    alert('Error')
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