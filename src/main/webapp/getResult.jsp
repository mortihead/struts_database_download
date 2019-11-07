<%@ page language="java"
contentType="application/json; charset=UTF-8" pageEncoding="UTF-8"%>

<%@page import="org.json.simple.JSONObject"%>

<%
	JSONObject obj = (JSONObject)request.getSession().getAttribute("json");
%>

<%=obj%>
