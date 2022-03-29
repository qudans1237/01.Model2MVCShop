package com.model2.mvc.view.purchase;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.model2.mvc.framework.Action;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.product.impl.ProductServiceImpl;
import com.model2.mvc.service.product.vo.ProductVO;
import com.model2.mvc.service.purchase.PurchaseService;
import com.model2.mvc.service.purchase.impl.PurchaseServiceImpl;
import com.model2.mvc.service.purchase.vo.PurchaseVO;
import com.model2.mvc.service.user.UserService;
import com.model2.mvc.service.user.impl.UserServiceImpl;
import com.model2.mvc.service.user.vo.UserVO;

public class AddPurchaseAction extends Action {//구매 요청

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		System.out.println("<<<<< AddPurchaseAction : execute() 시작 >>>>>");
		
		//addPurchaseView.jsp에서 가져온 값을 DB에 쉽게 전달하기 위해 PurchaseVO 인스턴스를 생성하여, 가져온 값을 저장
		PurchaseVO purchaseVO = new PurchaseVO();
		purchaseVO.setPaymentOption(request.getParameter("paymentOption")); //구매방법
		purchaseVO.setReceiverName(request.getParameter("receiverName"));   //구매자이름
		purchaseVO.setReceiverPhone(request.getParameter("receiverPhone")); //구매자연락처
		purchaseVO.setDivyAddr(request.getParameter("receiverAddr"));       //구매자주소
		purchaseVO.setDivyRequest(request.getParameter("receiverRequest")); //구매요청사항
		purchaseVO.setDivyDate(request.getParameter("receiverDate"));       //배송희망일자
		purchaseVO.setTranCode("1");
		
		//2. ProductVO의 값을 PurchaseVO에 저장
		//방법1)
		ProductService productService = new ProductServiceImpl();
		ProductVO productVO = productService.getProduct((Integer.parseInt(request.getParameter("prodNo"))));
		purchaseVO.setPurchaseProd(productVO);                              //상품정보
		//방법2)
//		ProductVO productVO = new ProductVO();
//		productVO.setProdNo(Integer.parseInt(request.getParameter("prodNo"))); 
//		purchaseVO.setPurchaseProd(productVO);
		
		//3. UserVO의 값을 PurchaseVO에 저장
		//방법1)
		HttpSession session = request.getSession();
		purchaseVO.setBuyer((UserVO)session.getAttribute("user"));		    //구매자아이디
		//방법2)
//		UserVO userVO = new UserVO();
//		userVO.setUserId(request.getParameter("userId"));
//		purchaseVO.setBuyer(userVO);	
		
		System.out.println("purchaseVO 셋팅완료 : " + purchaseVO);
		
		//purchaseVO를 DB에 저장하기 위해 PurchaseServiceImpl 인스턴스 생성
		PurchaseService purchaseService = new PurchaseServiceImpl();
		purchaseService.addPurchase(purchaseVO);
		
		request.setAttribute("purchaseVO", purchaseVO);
		
		System.out.println("<<<<< AddPurchaseAction : execute() 종료 >>>>>");
		return "forward:/purchase/addPurchase.jsp";		
	}
}