<%@ page
        import="org.springframework.web.servlet.support.BindStatus" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/tags"
           prefix="spring" %>

<html>
<head>
    <title>Book list</title>
</head>

<body>
<h1>Book list</h1>

<form method="POST">
    <input type="hidden" name="_eventId" value="addBook">
    <input type="submit" value="Add book ..." id="addbook">
</form>
<table>
    <thead>
        <tr>
            <td>Title</td>
            <td>Author</td>
            <td>&nbsp;</td>
        </tr>
    </thead>
    <tbody>
        <c:forEach items="${books}" var="book">
            <tr>
                <td><c:out value="${book.title}"/></td>
                <td><c:out value="${book.author}"/></td>
                <td>
                    <form method="POST">
                        <input type="hidden" name="_eventId"
                               value="detail">
                        <input type="hidden" name="id"
                               value="<c:out value="${book.id}"/>">
                        <input type="submit"
                               value="Details"
                               id="detail<c:out value="${book.id}"/>">
                    </form>
                </td>
            </tr>
        </c:forEach>
    </tbody>
</table>
</body>
</html>
