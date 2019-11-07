<%@ page import="java.util.List"%><%@ page import="myApp.rest.RestRequests"%><%@ page import="com.google.gson.Gson"%><%@ page language="java"
contentType="application/json; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	List data = RestRequests.getTableData("T_DICTIONARY");
	String jsonStr = new Gson().toJson(data);
%>
<%=jsonStr%>