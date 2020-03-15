<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <script
            src="https://code.jquery.com/jquery-3.4.1.js"
            integrity="sha256-WpOohJOqMqqyKL9FccASB9O0KwACQJpFTUBLTYOVvVU="
            crossorigin="anonymous"></script>
    <title>Document</title>
    <script>
        function sendFile() {
            // данные для отправки
            let formData = new FormData();
            // забрал файл из input
            let files = ($('#file'))[0]['files'];
            // добавляю файл в formData
            [].forEach.call(files, function (file, i, files) {
                formData.append("file", file);
            });

            $.ajax({
                type: "POST",
                url: "http://localhost:8080/files",
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
<div>
    <input type="file" id="file" name="file" placeholder="File name"/>
    <button onclick="sendFile()">
       Upload file
    </button>
    <input type="hidden" id="file_hidden">
    <div class="filename"></div>
</div>

<#if status??>
    ${status}
</#if>
<#if image??>
    <img src="${image}">
</#if>
</body>
</html> 