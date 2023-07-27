<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>Клиенты</title>
</head>
<body>
<h4>Создать клиента</h4>
<form action="clients" method="post">
    <label>
        Имя <br/>
        <input name="name" type="text" value="Petya">
    </label>
    <br/>
    <label>
        Адрес<br/>
        <input name="address" type="text" value="Pushkino">
    </label>
    <br/><label>
        Телефон<br/>
        <input name="phone" type="text" value="111-111-11">
    </label>
    <br/><label>
        Пароль<br/>
        <input name="password" type="password" value="12345">
    </label>
    <br/><br/>
    <input type="submit" value="Сохранить">
</form>
<#--<pre id="clientDataContainer"></pre>-->

<h4>Список клиентов</h4>
<table style="width: 400px">
    <thead>
    <tr>
        <td style="width: 50px">Id</td>
        <td style="width: 150px">Имя</td>
        <td style="width: 150px">Адрес</td>
        <td style="width: 150px">Телефон</td>
        <td style="width: 100px">Пароль</td>
    </tr>
    </thead>
    <tbody>
    <#list clients as cl>
        <tr>
            <td>${cl.id}</td>
            <td>${cl.name}</td>
            <td>${cl.address}</td>
            <#--            <#list clients.phones as phones>-->
            <td>${cl.phones[0]}</td>
            <#--            </#list>-->
            <td>${cl.password}</td>
        </tr>
    </#list>
    </tbody>
</table>
</body>
</html>
