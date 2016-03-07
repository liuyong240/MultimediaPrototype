<%--
  Created by IntelliJ IDEA.
  User: yangdongxu
  Date: 15/11/24
  Time: 11:56
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title></title>
</head>
<body>
<script src="/auth/jquery-2.1.4.min.js"></script>
<script>
    $(function() {

        $('form').on('submit', function(e) {

            e.preventDefault();

            if ($('#password').val() !== $('#repassword').val()) {
                alert('两次密码不一样');
                return;
            }

            var data = $(this).serializeArray();

            $.ajax({
                url: '/auth/api/signup',
                method: 'POST',
                data: data
            }).success(function(data) {
                if (data.code === 0) {
                    location.href = "/auth/login";
                } else {
                    alert(data.error);
                }
            });

            return false;
        });

    });
</script>

<style>
    label, input, button {
        display: block;
        margin: 5px;
    }
    button {
        padding: 10px 20px;
    }
</style>
<form>
    <p>用户注册</p>

    <label for="username">用户名</label>
    <input type="text" name="username" id="username">
    <p>大小写字母和数字, 长度6-20</p>

    <label for="password">密码</label>
    <input type="password" name="password" id="password">
    <p>必须包含大小写字母和数字, 长度6-20</p>

    <label for="repassword">重复密码</label>
    <input type="password" name="repassword" id="repassword">

    <input type="hidden"
           name="${_csrf.parameterName}"
           value="${_csrf.token}"/>

    <button type="submit">提交</button>
</form>

</body>
</html>
