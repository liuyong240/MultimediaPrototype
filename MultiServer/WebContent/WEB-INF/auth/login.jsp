<%--
  Created by IntelliJ IDEA.
  User: yangdongxu
  Date: 15/11/24
  Time: 11:55
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Admin</title>
</head>
<body>
<script src="/auth/jquery-2.1.4.min.js"></script>
<script>
  $(function() {

    $('form').on('submit', function(e) {

      $('.loading').show();

      e.preventDefault();

      var data = $(this).serializeArray();

      $.ajax({
        url: '/auth/api/login',
        method: 'POST',
        data: data
      }).success(function(data) {
        location.href = "/";
      }).error(function() {
        alert('登录失败!');
      }).always(function() {
        $('.loading').hide();
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
  <p>用户登录</p>

  <p class="loading" style="display: none">loading...</p>

  <label for="username">用户名</label>
  <input type="text" name="username" id="username">

  <label for="password">密码</label>
  <input type="password" name="password" id="password">

  <input type="hidden"
         name="${_csrf.parameterName}"
         value="${_csrf.token}"/>

  <button type="submit">提交</button>

  <p>
    <a href="/auth/signup">新用户注册</a>
  </p>
</form>
</body>
</html>


