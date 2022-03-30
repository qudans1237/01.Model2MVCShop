<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<!DOCTYPE html>
<%@ page import="com.model2.mvc.service.purchase.vo.*" %>
<%@ page import="com.model2.mvc.service.user.vo.*" %>
<% System.out.println("<<<<< getPurchase.jsp 시작 >>>>>"); %>

<%
	PurchaseVO vo=(PurchaseVO)request.getAttribute("purch");
	UserVO userVO=(UserVO)session.getAttribute("user");
	System.out.println("받은 PurchaseVO :"+vo);
	System.out.println("받은 UserVO :"+userVO);
%>


<html>
<head>
<title>Insert title here</title>
</head>

<body>

<form name="updatePurchase" action="/updatePurchaseView.do?tranNo=0" method="post">

다음과 같이 구매가 되었습니다.

<table border=1>
	<tr>
		<td>물품번호</td>
		<td><%=vo.getTranNo()%></td>
		<td></td>
	</tr>
	<tr>
		<td>구매자아이디</td>
		<td><%=vo.getBuyer().getUserId() %></td>
		<td></td>
	</tr>
	<tr>
		<td>구매방법</td>
		<td>
		
				<%if(vo.getPaymentOption().equals("1")){ %>
				현금결제
				<%}else{ %>카드결제   <%  } %>
		
		</td>
		<td></td>
	</tr>
	<tr>
		<td>구매자이름</td>
		<td><%=vo.getReceiverName() %></td>
		<td></td>
	</tr>
	<tr>
		<td>구매자연락처</td>
		<td><%=vo.getReceiverPhone() %></td>
		<td></td>
	</tr>
	<tr>
		<td>구매자주소</td>
		<td><%=vo.getDivyAddr() %></td>
		<td></td>
	</tr>
		<tr>
		<td>구매요청사항</td>
		<td><%=vo.getDivyRequest() %></td>
		<td></td>
	</tr>
	<tr>
		<td>배송희망일자</td>
		<td><%=vo.getDivyDate() %></td>
		<td></td>
	</tr>
</table>
</form>

</body>
</html>
<% System.out.println("<<<<< getPurchase.jsp 종료 >>>>>"); %>