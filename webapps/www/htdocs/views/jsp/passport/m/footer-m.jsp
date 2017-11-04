<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
document.writeln('<footer class="footer">');
document.writeln('<p>');
document.writeln('<span>');
document.writeln('<a href="${pcUrl}" class="joyme-pc">访问着迷网电脑版</a>');
document.writeln('</span>');
document.writeln('<span>');
document.writeln('<a href="${mUrl}" class="joyme-phone">访问着迷网手机版</a>');
document.writeln('</span>');
document.writeln('</p>');
document.writeln('<p>2011－2017 joyme.com, all rights reserved</p>');
document.writeln('</footer>');
