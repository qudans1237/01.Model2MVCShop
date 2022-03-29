package com.model2.mvc.service.product.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import com.model2.mvc.common.SearchVO;
import com.model2.mvc.common.util.DBUtil;
import com.model2.mvc.service.product.vo.ProductVO;
import com.model2.mvc.service.purchase.PurchaseService;
import com.model2.mvc.service.purchase.impl.PurchaseServiceImpl;

public class ProductDAO {
	//Constructor
	public ProductDAO() {
	}
	
	//Method
	//상품등록을 위한 DBMS를 수행
	public void insertProduct(ProductVO productVO) throws SQLException {
		System.out.println("<<<<< ProductDAO : insertProduct() 시작 >>>>>");
		System.out.println("받은 productVO : " + productVO);
		
		Connection con = DBUtil.getConnection();
		
		String sql = "INSERT INTO product VALUES (seq_product_prod_no.nextval,?,?,?,?,?,sysdate)";
		
		PreparedStatement pStmt = con.prepareStatement(sql);
		pStmt.setString(1, productVO.getProdName());
		pStmt.setString(2, productVO.getProdDetail());
		pStmt.setString(3, productVO.getManuDate());
		pStmt.setInt(4, productVO.getPrice());
		pStmt.setString(5, productVO.getFileName());
		pStmt.executeUpdate();
		System.out.println("insert 완료 : " + sql);
		
		pStmt.close();
		con.close();	
		System.out.println("<<<<< ProductDAO : insertProduct() 종료 >>>>>");
	}
	
	
	//상품정보 조회를 위한 DBMS를 수행
	public ProductVO findProduct(int prodNo) throws Exception {
		System.out.println("<<<<< ProductDAO : findProduct() 시작 >>>>>");
		System.out.println("받은 prodNo : " + prodNo);
		
		Connection con = DBUtil.getConnection();

		String sql = "SELECT * FROM product WHERE prod_no=?";
		
		PreparedStatement pStmt = con.prepareStatement(sql);
		pStmt.setInt(1, prodNo);
		ResultSet rs = pStmt.executeQuery();
		System.out.println("sql 전송완료 : " + sql);

		ProductVO productVO = new ProductVO();
		while (rs.next()) {
			productVO.setProdNo(rs.getInt("prod_no"));
			productVO.setProdName(rs.getString("prod_name"));
			productVO.setProdDetail(rs.getString("prod_detail"));
			productVO.setManuDate(rs.getString("manufacture_day"));
			productVO.setPrice(rs.getInt("price"));
			productVO.setFileName(rs.getString("image_file"));
			productVO.setRegDate(rs.getDate("reg_date"));
		}
		System.out.println("productVO 셋팅완료 : " + productVO);
		
		rs.close();
		pStmt.close();
		con.close();
		System.out.println("<<<<< ProductDAO : findProduct() 종료 >>>>>");
		return productVO;
	}
	
	
	//상품목록 조회를 위한 DBMS를 수행
	public HashMap<String,Object> getProductList(SearchVO searchVO) throws Exception {
		System.out.println("<<<<< ProductDAO : getProductList() 시작 >>>>>");
		System.out.println("받은 searchVO : " + searchVO);
		
		Connection con = DBUtil.getConnection();
		
		String sql = "SELECT * FROM product ";
		
		//SearchCondition에 값이 있을 경우
		if (searchVO.getSearchCondition() != null) {
			if (searchVO.getSearchCondition().equals("0")) {
				sql += " WHERE prod_no LIKE '%" + searchVO.getSearchKeyword() + "%'";
			} else if (searchVO.getSearchCondition().equals("1")) {
				sql += " WHERE prod_name LIKE '%" + searchVO.getSearchKeyword() + "%'";
			} else if (searchVO.getSearchCondition().equals("2")) {
				sql += " WHERE price LIKE '%" + searchVO.getSearchKeyword() + "%'";
			}
		}
		sql += " ORDER BY prod_no";
		
		PreparedStatement pStmt = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
														    ResultSet.CONCUR_UPDATABLE);
		ResultSet rs = pStmt.executeQuery();
		System.out.println("sql 전송완료 : " + sql);
		
		rs.last(); //boolean last() : 마지막 행으로 커서 이동
		int total = rs.getRow(); //int getRow() : 현재 행번호 검색 (마지막 행번호 = 전체 행의 수)
		System.out.println("전체 로우 수(total) : " + total);

		HashMap<String,Object> map = new HashMap<String,Object>();
		map.put("count", new Integer(total));
		System.out.println("map에 count 추가 : " + map);

		//boolean absolute(int row) : 지정된 행번호로 커서 이동
		rs.absolute(searchVO.getPage() * searchVO.getPageUnit() - searchVO.getPageUnit()+1);
		
		ArrayList<ProductVO> list = new ArrayList<ProductVO>();
		
		PurchaseService service = new PurchaseServiceImpl();
		if (total > 0) {
			for (int i=0; i<searchVO.getPageUnit(); i++) {
				ProductVO productVO = new ProductVO();
				productVO.setProdNo(rs.getInt("prod_no"));
				productVO.setProdName(rs.getString("prod_name"));
				productVO.setProdDetail(rs.getString("prod_detail"));
				productVO.setManuDate(rs.getString("manufacture_day"));
				productVO.setPrice(rs.getInt("price"));
				productVO.setFileName(rs.getString("image_file"));
				productVO.setRegDate(rs.getDate("reg_date"));
				
				if(service.getPurchase2(productVO.getProdNo()) != null) {
					productVO.setProTranCode("재고없음"); 
				}else {
					productVO.setProTranCode("판매중");	
				}
				
				list.add(productVO);
				if (!rs.next()) {
					break;
				}
				System.out.println("productVO 셋팅완료 : " + productVO);	
			}
		}
		map.put("list", list);
		System.out.println("map에 list 추가 : " + map);
		System.out.println("list.size() : " + list.size()); 
		System.out.println("map.size() : " + map.size()); 
		
		rs.close();
		pStmt.close();
		con.close();
		System.out.println("<<<<< ProductDAO : getProductList() 종료 >>>>>");
		return map;
	}
	
	
	//상품정보 수정을 위한 DBMS를 수행
	public void updateProduct(ProductVO productVO) throws Exception {
		System.out.println("<<<<< ProductDAO : updateProduct() 시작 >>>>>");
		System.out.println("받은 productVO : " + productVO);
		
		Connection con = DBUtil.getConnection();

		String sql = "UPDATE product "
				    + "SET prod_name=?, prod_detail=?, manufacture_day=?, "
				    + "price=?, image_file=? WHERE prod_no=?";
		
		PreparedStatement pStmt = con.prepareStatement(sql);
		pStmt.setString(1, productVO.getProdName());
		pStmt.setString(2, productVO.getProdDetail());
		pStmt.setString(3, productVO.getManuDate());
		pStmt.setInt(4, productVO.getPrice());
		pStmt.setString(5, productVO.getFileName());
		pStmt.setInt(6, productVO.getProdNo());
		pStmt.executeUpdate();
		System.out.println("update 완료 : " + sql);
		
		pStmt.close();
		con.close();
		System.out.println("<<<<< ProductDAO : updateProduct() 종료 >>>>>");
	}

}//end of class
