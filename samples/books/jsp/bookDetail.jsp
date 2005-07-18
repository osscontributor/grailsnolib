<%@ page import="org.springframework.web.servlet.support.BindStatus"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>

<html>
   <head>
      <title>Book detail</title>
   </head>

   <body>
      <h1>Book detail</h1>
      <spring:hasBindErrors name="bookDetail">
         <c:forEach items="${errors.allErrors}" var="error">
            <span><c:out value="${error}"/></span>
         </c:forEach>
      </spring:hasBindErrors>
      <form method="POST">
         <fieldset>
            <input type="hidden" name="_flowExecutionId" value="<c:out value="${flowExecutionId}"/>">
            <input type="hidden" name="_eventId" value="save">
            <spring:bind path="bookDetail.id">
               <input type="hidden" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" id="bookId"/>
            </spring:bind>
            <label>title</label>
            <spring:bind path="bookDetail.title">
               <input type="text" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" id="bookAuthor">
            </spring:bind>
            <br>
            <label>author</label>
            <spring:bind path="bookDetail.author">
               <input type="text" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" id="bookTitle">
            </spring:bind>
            <br>
            <input type="submit" value="Save" id="save">
         </fieldset>
      </form>
      <form method="POST">
         <input type="hidden" name="_flowExecutionId" value="<c:out value="${flowExecutionId}"/>">
         <input type="hidden" name="_eventId" value="close">
         <input type="submit" value="Close" id="close">
      </form>
   </body>
</html>
