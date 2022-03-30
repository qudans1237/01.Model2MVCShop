package com.model2.mvc.view.product;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.model2.mvc.framework.Action;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.product.impl.ProductServiceImpl;
import com.model2.mvc.service.product.vo.ProductVO;

public class GetProductAction extends Action {


	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		int prodNo=Integer.parseInt(request.getParameter("prodNo"));
		
		ProductService service=new ProductServiceImpl();
		ProductVO vo=service.getProduct(prodNo);
		System.out.println("getprodNo>>>>>:"+prodNo);
		System.out.println("getprodNo¢¯¢® vo>>>>>:"+vo);
		request.setAttribute("vo", vo);
		
		
		HashMap<String,Object> map = new HashMap ();
		map.put("prodNo", request.getParameter("prodNo"));
		map.put("prodName", request.getParameter("prodName"));
		map.put("manuDate", request.getParameter("manuDate"));
		map.put("price", request.getParameter("price"));
		map.put("prodDetail", request.getParameter("prodDetail"));
		map.put("regDate", request.getParameter("regDate"));
		
		HttpSession session=request.getSession();
		map.get("prodName");
		System.out.println("GetProduct  session >>"+map);
		session.setAttribute("productVO", map);
		
		
		

		return "forward:/product/getProduct.jsp";
	}
}
