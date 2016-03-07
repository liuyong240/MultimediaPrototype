<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!doctype html>
<html ng-app="admin">
<head>
    <meta charset="utf-8">
    <title>Multimedia</title>
    <meta name="description" content="流媒体">
    <meta name="viewport" content="width=device-width">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <!-- Place favicon.ico and apple-touch-icon.png in the root directory -->
    <!--[if lt IE 9]>
    <script>
        window.location.href = '/oldie';
    </script>
    <![endif]-->

    <link rel="stylesheet" href="/admin/styles/vendor-0.0.1.css">

    <link rel="stylesheet" href="/admin/styles/app-0.0.1.css">
</head>
<body>
<script>
    var user = '${user}';
</script>
<div kai-navbar></div>
<!--div ng-view></div-->
<div ui-view></div>

<script src="/admin/scripts/vendor-0.0.1.js"></script>

<script src="/admin/scripts/app-0.0.1.js"></script>

</body>
</html>

