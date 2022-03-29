package com.model2.mvc.service.purchase.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import com.model2.mvc.common.SearchVO;
import com.model2.mvc.common.util.DBUtil;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.product.impl.ProductServiceImpl;
import com.model2.mvc.service.product.vo.ProductVO;
import com.model2.mvc.service.purchase.PurchaseService;
import com.model2.mvc.service.purchase.impl.PurchaseServiceImpl;
import com.model2.mvc.service.purchase.vo.PurchaseVO;
import com.model2.mvc.service.user.UserService;
import com.model2.mvc.service.user.impl.UserServiceImpl;
import com.model2.mvc.service.user.vo.UserVO;

public class PurchaseDAO {
	//Constructor
	public PurchaseDAO() {
	}
	
	//Method
	//구매를 위한 DBMS를 수행
	public void insertPurchase(PurchaseVO purchaseVO) throws SQLException {	
		System.out.println("<<<<< PurchaseDAO : insertPurchase() 시작 >>>>>");
		System.out.println("받은 purchaseVO : " + purchaseVO);
		
		Connection con = DBUtil.getConnection();
		
		String sql = "INSERT INTO transaction VALUES (seq_product_prod_no.nextval,?,?,?,?,?,?,?,?,sysdate,?)";
		
		PreparedStatement pStmt = con.prepareStatement(sql);
		pStmt.setInt(1, purchaseVO.getPurchaseProd().getProdNo());
		pStmt.setString(2, purchaseVO.getBuyer().getUserId());
		pStmt.setString(3, purchaseVO.getPaymentOption());
		pStmt.setString(4, purchaseVO.getReceiverName());
		pStmt.setString(5, purchaseVO.getReceiverPhone());
		pStmt.setString(6, purchaseVO.getDivyAddr());
		pStmt.setString(7, purchaseVO.getDivyRequest());
		pStmt.setString(8, purchaseVO.getTranCode());
		pStmt.setString(9, purchaseVO.getDivyDate());
		pStmt.executeUpdate();
		System.out.println("insert 완료 : " + sql);
		
		pStmt.close();
		con.close();	
		System.out.println("<<<<< PurchaseDAO : insertProduct() 종료 >>>>>");
	}
	
	
	//구매정보 상세 조회를 위한 DBMS를 수행 ==> tranNo
	public PurchaseVO findPurchase(int tranNo) throws Exception {
		System.out.println("<<<<< PurchaseDAO : findPurchase() 시작 >>>>>");
		System.out.println("받은 tranNo : " + tranNo);
		
		Connection con = DBUtil.getConnection();
		
		String sql = "SELECT * FROM transaction WHERE tran_no=? ";
		
		PreparedStatement pStmt = con.prepareStatement(sql);
		pStmt.setInt(1, tranNo);
		ResultSet rs = pStmt.executeQuery();
		System.out.println("sql 전송완료 : " + sql);
		
		PurchaseVO purchaseVO = new PurchaseVO();
		ProductVO productVO = new ProductVO();
		UserVO userVO = new UserVO();
		while (rs.next()) {	
			purchaseVO.setPaymentOption(rs.getString("payment_option"));
			purchaseVO.setReceiverName(rs.getString("receiver_name"));
			purchaseVO.setReceiverPhone(rs.getString("receiver_phone"));
			purchaseVO.setDivyAddr(rs.getString("demailaddr"));
			purchaseVO.setDivyRequest(rs.getString("dlvy_request"));
			purchaseVO.setDivyDate(rs.getString("dlvy_date"));
			purchaseVO.setOrderDate(rs.getDate("order_data"));
			purchaseVO.setTranNo(rs.getInt("tran_no"));
			purchaseVO.setTranCode(rs.getString("tran_status_code"));
			
			productVO.setProdNo(rs.getInt("prod_no"));
			purchaseVO.setPurchaseProd(productVO);
			
			userVO.setUserId(rs.getString("buyer_id"));
			purchaseVO.setBuyer(userVO);
		}
		System.out.println("purchaseVO 셋팅완료 : " + purchaseVO);
		
		rs.close();
		pStmt.close();
		con.close();
		System.out.println("<<<<< PurchaseDAO : findPurchase() 종료 >>>>>");
		return purchaseVO; 
	}
	
	
	//구매정보 상세 조회를 위한 DBMS를 수행 ==> prodNo
	public PurchaseVO findPurchase2(int prodNo) throws Exception {
		System.out.println("<<<<< PurchaseDAO : findPurchase2() 시작 >>>>>");
		System.out.println("받은 prodNo : " + prodNo);
			
		Connection con = DBUtil.getConnection();
			
		String sql = "SELECT * FROM transaction WHERE prod_no=? ";
			
		PreparedStatement pStmt = con.prepareStatement(sql);
		pStmt.setInt(1, prodNo);
		ResultSet rs = pStmt.executeQuery();
		System.out.println("sql 전송완료 : " + sql);	
		
		PurchaseVO purchaseVO = null;
		while (rs.next()) {
			purchaseVO = new PurchaseVO();
			purchaseVO.setPaymentOption(rs.getString("payment_option"));
			purchaseVO.setReceiverName(rs.getString("receiver_name"));
			purchaseVO.setReceiverPhone(rs.getString("receiver_phone"));
			purchaseVO.setDivyAddr(rs.getString("demailaddr"));
			purchaseVO.setDivyRequest(rs.getString("dlvy_request"));
			purchaseVO.setDivyDate(rs.getString("dlvy_date"));
			purchaseVO.setOrderDate(rs.getDate("order_data"));
			purchaseVO.setTranNo(rs.getInt("tran_no"));
			purchaseVO.setTranCode(rs.getString("tran_status_code"));	
		
			System.out.println("purchaseVO 셋팅완료 : " + purchaseVO);
			System.out.println("findPurchase2 tran_no : " + purchaseVO.getTranNo());
			System.out.println("findPurchase2 tran_status_code : " + purchaseVO.getTranCode());		
		}		
			
		rs.close();
		pStmt.close();
		con.close();
			
		System.out.println("<<<<< PurchaseDAO : findPurchase2() 종료 >>>>>");
		return purchaseVO; 
	}
		
	
	//구매목록 보기를 위한 DBMS를 수행
	public HashMap<String,Object> getPurchaseList(SearchVO searchVO, String buyerId) throws Exception {
		System.out.println("<<<<< PurchaseDAO : getPurchaseList() 시작 >>>>>");
		System.out.println("받은 searchVO : " + searchVO);
		System.out.println("받은 buyerId : " + buyerId);
		
		Connection con = DBUtil.getConnection();
		
		String sql = "SELECT * FROM transaction WHERE buyer_id=? ";
		
		PreparedStatement pStmt = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, 
															ResultSet.CONCUR_UPDATABLE);
		pStmt.setString(1, buyerId);
		ResultSet rs = pStmt.executeQuery();
		System.out.println("sql 전송완료 : " + sql);
		
		rs.last(); //boolean last() : 마지막 행으로 커서 이동
		int total = rs.getRow(); //int getRow() : 현재 행번호 검색 (마지막 행번호 = 전체 행의 수)
		System.out.println("전체 로우 수(total) : " + total);
		
		HashMap<String,Object> map = new HashMap<String,Object>();
		map.put("count", total);
		System.out.println("map에 count 추가 : " + map);
		
		//boolean absolute(int row) : 지정된 행번호로 커서 이동
		rs.absolute(searchVO.getPage() * searchVO.getPageUnit() - searchVO.getPageUnit()+1);
		
		PurchaseVO purchaseVO = null;
		UserService service = new UserServiceImpl();
		ArrayList<PurchaseVO> list = new ArrayList<PurchaseVO>();
		
		if (total > 0) {
			for (int i=0; i<searchVO.getPageUnit(); i++) {
				purchaseVO = new PurchaseVO();
				purchaseVO.setReceiverName(rs.getString("receiver_name"));
				purchaseVO.setReceiverPhone(rs.getString("receiver_phone"));
				purchaseVO.setTranNo(rs.getInt("tran_no"));
				purchaseVO.setTranCode(rs.getString("tran_status_code"));
				purchaseVO.setBuyer(service.getUser(rs.getString("buyer_id")));
				
				list.add(purchaseVO);
				if (!rs.next()) {
					break;
				}
			}
			System.out.println("purchaseVO 셋팅완료 : " + purchaseVO);
		}
		map.put("list", list);
		System.out.println("map에 list 추가 : " + map);
		System.out.println("list.size() : " + list.size()); 
		System.out.println("map.size() : " + map.size());
		
		rs.close();
		pStmt.close();
		con.close();
		System.out.println("<<<<< PurchaseDAO : getPurchaseList() 종료 >>>>>");
		return map;
	}
	
	
	//판매목록 보기를 위한 DBMS를 수행
	public HashMap<String,Object> getSaleList(SearchVO searchVO) throws Exception {
		System.out.println("<<<<< PurchaseDAO : getSaleList() 시작 >>>>>");
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
		
		ProductVO productVO = null;
		ArrayList<ProductVO> list = new ArrayList<ProductVO>();
		
		if (total > 0) {
			for (int i=0; i<searchVO.getPageUnit(); i++) {
				productVO = new ProductVO();
				productVO.setProdNo(rs.getInt("prod_no"));
				productVO.setProdName(rs.getString("prod_name"));
				productVO.setProdDetail(rs.getString("prod_detail"));
				productVO.setManuDate(rs.getString("manufacture_day"));
				productVO.setPrice(rs.getInt("price"));
				productVO.setFileName(rs.getString("image_file"));
				productVO.setRegDate(rs.getDate("reg_date"));
				
				if(findPurchase2(productVO.getProdNo()) != null) {
					productVO.setProTranCode(findPurchase2(productVO.getProdNo()).getTranCode());
				}
				
				list.add(productVO);
				if (!rs.next()) {
					break;
				}				
			}
			System.out.println("productVO 셋팅완료 : " + productVO);
		}
		map.put("list", list);
		System.out.println("map에 list 추가 : " + map);
		System.out.println("list.size() : " + list.size()); 
		System.out.println("map.size() : " + map.size()); 
		
		rs.close();
		pStmt.close();
		con.close();
		System.out.println("<<<<< PurchaseDAO : getSaleList() 종료 >>>>>");
		return map;
	}
	
	
	//구매정보 수정을 위한 DBMS를 수행
	public void updatePurchase(PurchaseVO purchaseVO) throws SQLException {
		System.out.println("<<<<< PurchaseDAO : updatePurchase() 시작 >>>>>");
		System.out.println("받은 purchaseVO : " + purchaseVO);
		
		Connection con = DBUtil.getConnection();
		
		String sql = "UPDATE transaction "
					+ "SET payment_option=?, receiver_name=?, receiver_phone=?,"
					+ "demailaddr=?, dlvy_request=?, dlvy_date=? "
					+ "WHERE tran_no=? ";
		
		PreparedStatement pStmt = con.prepareStatement(sql);
		pStmt.setString(1, purchaseVO.getPaymentOption());
		pStmt.setString(2, purchaseVO.getReceiverName());
		pStmt.setString(3, purchaseVO.getReceiverPhone());
		pStmt.setString(4, purchaseVO.getDivyAddr());
		pStmt.setString(5, purchaseVO.getDivyRequest());
		pStmt.setString(6, purchaseVO.getDivyDate());
		pStmt.setInt(7, purchaseVO.getTranNo());
		pStmt.executeUpdate();
		System.out.println("update 완료 : " + sql);
		
		pStmt.close();
		con.close();
		System.out.println("<<<<< PurchaseDAO : updatePurchase() 종료 >>>>>");
	}
	
	
	//구매 상태코드 수정을 위한 DBMS를 수행
	public void updateTranCode(PurchaseVO purchaseVO) throws Exception {
		System.out.println("<<<<< PurchaseDAO : updateTranCode() 시작 >>>>>");
		System.out.println("받은 purchaseVO : " + purchaseVO);
		
		Connection con = DBUtil.getConnection();
		
		String sql = "UPDATE transaction SET tran_status_code=? WHERE tran_no=?";
		
		PreparedStatement pStmt = con.prepareStatement(sql);
		pStmt.setString(1, purchaseVO.getTranCode());
		pStmt.setInt(2, purchaseVO.getTranNo());
		pStmt.executeUpdate();
		System.out.println("update 완료 : " + sql);
		System.out.println("update한 tranCode : " + purchaseVO.getTranCode());
		System.out.println("update한 tranNo : " + purchaseVO.getTranNo());
		
		pStmt.close();
		con.close();
		System.out.println("<<<<< PurchaseDAO : updateTranCode() 종료 >>>>>");
	}

}//end of class