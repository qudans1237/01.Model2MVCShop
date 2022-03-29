package com.model2.mvc.view.product;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.model2.mvc.common.SearchVO;
import com.model2.mvc.framework.Action;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.product.impl.ProductServiceImpl;
import com.model2.mvc.service.user.UserService;
import com.model2.mvc.service.user.impl.UserServiceImpl;

public class ListProductAction extends Action {// 상품목록조회 요청

	// 1. 판매상품관리 or 상품검색 메뉴 클릭
	// 2. 판매상품관리 ==> http://192.168.0.96:8080/listProduct.do?menu=manage 이동
	// 3. 상품검색 ==> http://192.168.0.96:8080/listProduct.do?menu=search 이동
	// 4. ListProductAction.java의 execute() 실행
	// 5. listProduct.jsp 이동

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		System.out.println("<<<<< ListProductAction : execute() 시작 >>>>>");

		SearchVO searchVO = new SearchVO();

		int page = 1;

		if (request.getParameter("page") != null) {
			page = Integer.parseInt(request.getParameter("page"));
			System.out.println("현재 page : " + page);
		}

		searchVO.setPage(page);
		searchVO.setSearchCondition(request.getParameter("searchCondition"));
		searchVO.setSearchKeyword(request.getParameter("searchKeyword"));

		System.out.println("searchCondition : " + request.getParameter("searchCondition"));
		System.out.println("searchKeyword : " + request.getParameter("searchKeyword"));

		// pageUnit에 web.xml의 "pageSize" 값 3을 저장하고, SearchVO의 pageUnit에 3 저장
		String pageUnit = getServletContext().getInitParameter("pageSize");
		searchVO.setPageUnit(Integer.parseInt(pageUnit));
		System.out.println("searchVO 셋팅완료 : " + searchVO);

		ProductService service = new ProductServiceImpl();
		HashMap<String, Object> map = service.getProductList(searchVO);
		System.out.println("map 셋팅완료 : " + map);

		String menu = request.getParameter("menu");
		System.out.println("menu 셋팅완료 : " + menu);

		request.setAttribute("map", map);
		request.setAttribute("searchVO", searchVO);
		request.setAttribute("menu", menu);

		System.out.println("<<<<< ListProductAction : execute() 종료 >>>>>");

		return "forward:/product/listProduct.jsp";
	}
}