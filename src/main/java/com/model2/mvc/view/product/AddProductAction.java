package com.model2.mvc.view.product;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.model2.mvc.framework.Action;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.product.impl.ProductServiceImpl;
import com.model2.mvc.service.product.vo.ProductVO;

public class AddProductAction extends Action {//상품등록 요청

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		System.out.println("<<<<< AddProductAction : execute() 시작 >>>>>");
		
		ProductVO productVO = new ProductVO();
		productVO.setProdName(request.getParameter("prodName"));                    //상품명
		productVO.setProdDetail(request.getParameter("prodDetail"));                //상품상세정보
		productVO.setManuDate(request.getParameter("manuDate").replaceAll("-", ""));//제조일자
		productVO.setPrice(Integer.parseInt(request.getParameter("price")));        //가격		
		productVO.setFileName(request.getParameter("fileName"));                    //상품이미지
		System.out.println("productVO 셋팅완료 : " + productVO);
		
		ProductService service = new ProductServiceImpl();
		service.addProduct(productVO);
		
		//방법1
		request.setAttribute("productVO", productVO);
		
		//방법2
//		HttpSession session = request.getSession(true);		
//		session.setAttribute("productVO",productVO);
		
		System.out.println("<<<<< AddProductAction : execute() 종료 >>>>>");
		return "forward:/product/addProduct.jsp";
	}
}