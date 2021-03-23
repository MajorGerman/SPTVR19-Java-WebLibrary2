 <%-- 
    Document   : listBooks
    Created on : 03.12.2020, 13:07:53
    Author     : jvm
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <link type="text/css" rel="stylesheet" href="style.css">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title> ~ Список товаров ~ </title>
    </head>
    <body>
        <div><strong><a id="title" style="font-size: 42px; text-decoration: none;-webkit-text-stroke: 1px black;" href="index.jsp"><<< Магазин Георга Лаабе >>></a></strong></div>
        <h6 style="font-size: 32px;" ${hiddenlogout}hidden> ${upuser}</h6>
        <a href="showLoginForm" ${hiddenlogin} style="font-size: 29px;"> > Войти < </a>
        <a href="logout" ${hiddenlogout}hidden style="font-size: 29px;"> > Выйти < </a> 
        <hr>
        <h1>~ Товары ~</h1>
        <ul name="productId" multiple="true">
            <c:forEach var="product" items="${listProducts}">
                <li style="font-size: 26px;" value="${product.id}">${product.name} (${product.price}$)</li>
            </c:forEach>
                
        </ul>
    </body>
</html>
