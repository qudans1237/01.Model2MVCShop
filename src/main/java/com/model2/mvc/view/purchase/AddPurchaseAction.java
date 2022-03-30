package com.model2.mvc.view.purchase;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.model2.mvc.framework.Action;
import com.model2.mvc.service.product.vo.ProductVO;
import com.model2.mvc.service.purchase.PurchaseService;
import com.model2.mvc.service.purchase.impl.PurchaseServiceImpl;
import com.model2.mvc.service.purchase.vo.PurchaseVO;
import com.model2.mvc.service.user.vo.UserVO;

public class AddPurchaseAction extends Action {

	public AddPurchaseAction() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		PurchaseVO purchaseVO = new PurchaseVO();
		UserVO userVO = new UserVO();
		//ProductVO productVO = new ProductVO();
		//productVO.setProdName(request.getParameter("prodName"));
		userVO.setUserId(request.getParameter("buyerId"));
		//purchaseVO.setPurchaseProd(productVO);
		purchaseVO.setTranNo(Integer.parseInt(request.getParameter("prodNo")));
		purchaseVO.setPaymentOption(request.getParameter("paymentOption"));
		purchaseVO.setReceiverName(request.getParameter("receiverName"));
		purchaseVO.setReceiverPhone(request.getParameter("receiverPhone"));
		purchaseVO.setDivyAddr(request.getParameter("receiverAddr"));
		purchaseVO.setDivyRequest(request.getParameter("receiverRequest"));
		purchaseVO.setDivyDate(request.getParameter("receiverDate"));
		purchaseVO.setBuyer(userVO);
		System.out.println("TranNo >>>>>>>>>"+purchaseVO.getTranNo());
		System.out.println("PaymentOption>>>>>>>>>>>>>"+purchaseVO.getPaymentOption());
		System.out.println("Add purchase:"+purchaseVO);
		PurchaseService service = new PurchaseServiceImpl();
		service.addPurchase(purchaseVO);
		System.out.println("Add purchase:"+purchaseVO);
		HttpSession session = request.getSession();
		session.setAttribute("purchasevo", purchaseVO);
		request.setAttribute("purch", purchaseVO);
		
		
		
		
		return "forward:/purchase/getPurchase.jsp";
	}

}