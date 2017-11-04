<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

</head>
<body>
    <input type="text" name="sign" value="${sign}"/>
    <form action="/json/share/getsign">
        <input type="text" name="uno"/>
        <input type="text" name="appkey" value="2AuLF0BeJceb2wHoTsccWbA"/>
        <input type="submit" value="提交"/>
    </form>
</body>
</html>
