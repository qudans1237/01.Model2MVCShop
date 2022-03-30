package com.model2.mvc.view.purchase;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.model2.mvc.common.SearchVO;
import com.model2.mvc.framework.Action;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.product.impl.ProductServiceImpl;
import com.model2.mvc.service.purchase.PurchaseService;
import com.model2.mvc.service.purchase.impl.PurchaseServiceImpl;
import com.model2.mvc.service.purchase.vo.PurchaseVO;

public class UpdateTranCodeByProdAction extends Action {


	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
//		PurchaseVO purchaseVO = new PurchaseVO();
//		ProductVO productVO = new ProductVO();
//		int prodNo=Integer.parseInt(request.getParameter("prodNo"));
//		//purchaseVO.getTranCode(request.getParameter(""));
//		//purchaseVO.getTranNo(request.getParameter(""));
//		
//		ProductService service=new ProductServiceImpl();
//		ProductVO vo=service.getProduct(prodNo);
//		vo.setProTranCode(request.getParameter("tranCode"));	// tranCode 바꿈
//		service.updateProduct(vo);								// VO 업데이트
//		
//		System.out.println("purchase 에getprodNo>>>>>:"+prodNo);
//		SearchVO searchVO = new SearchVO();
//		System.out.println("purchase 에getprodNo에 vo>>>>>:"+vo);
//		
//		int page = 1;
//		searchVO.setPage(page);
//		searchVO.setSearchCondition(null);
//		searchVO.setSearchKeyword(null);
//		searchVO.setPageUnit(3);
//		
//		HashMap<String,Object> map=service.getProductList(searchVO);
//		request.setAttribute("map", map);
//		request.setAttribute("menu", "manage");
//		request.setAttribute("searchVO", searchVO);
//		
		
		SearchVO searchVO=new SearchVO();
		String encoding = request.getParameter("searchKeyword");

		
		//keyword1 = new String(encoding.getBytes("8859_1"),"euc-kr");
				
		//System.out.println(keyword1);
		int page=1;
		PurchaseVO purchaseVO = new PurchaseVO();
		//purchaseVO.get     request.getParameter("prodNo");
		
		int ProdNo = Integer.parseInt(request.getParameter("prodNo"));
		System.out.println("tranCode :"+request.getParameter("tranCode"));
		System.out.println("prodNo :"+request.getParameter("prodNo"));
		PurchaseService service1 = new PurchaseServiceImpl();
		//service1.updateTranCode(purchaseVO);
		purchaseVO = service1.getPurchase2(ProdNo);
		purchaseVO.setTranCode(request.getParameter("tranCode"));
		System.out.println("DB 들어가기전"+purchaseVO);
		//purchaseVO.setTranNo(purchaseVO.(service1.getPurchase2(ProdNo)));
		service1.updateTranCode(purchaseVO);
		System.out.println("DB 들어간이후"+purchaseVO);
		if(request.getParameter("page") != null)
			//받아온 page가 null이 아니면
			page=Integer.parseInt(request.getParameter("page"));
		//page에 담는다
		System.out.println("ListProductAction >>> "+ page+"    "+request.getParameter("searchCondition")+"    "+
				request.getParameter("searchKeyword"));
		//page에 담겨져 있는거 확인  
		searchVO.setPage(page);
		searchVO.setSearchCondition(request.getParameter("searchCondition"));
		String keyword = (request.getParameter("searchKeyword")==null)? "": request.getParameter("searchKeyword");
		searchVO.setSearchKeyword(keyword);
		
		String pageUnit=getServletContext().getInitParameter("pageSize");
		searchVO.setPageUnit(Integer.parseInt(pageUnit));
		
		ProductService service=new ProductServiceImpl();
		System.out.println("ListProductAction >>> searchCondition: "+searchVO.getSearchCondition()+
				" searchKeyword: "+searchVO.getSearchKeyword()+" page unit: "+searchVO.getPageUnit());
		/**
		 * 
		 */
		HashMap<String,Object> map=service.getProductList(searchVO);
		request.setAttribute("map", map);
		request.setAttribute("searchVO", searchVO);
		
		return "forward:/product/listProduct.jsp?menu=manage";
	}

}