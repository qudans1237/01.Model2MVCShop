package com.model2.mvc.service.purchase.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

import com.model2.mvc.common.SearchVO;
import com.model2.mvc.common.util.DBUtil;
import com.model2.mvc.service.product.vo.ProductVO;
import com.model2.mvc.service.purchase.vo.PurchaseVO;
import com.model2.mvc.service.user.vo.UserVO;

public class PurchaseDAO {
	
	public PurchaseDAO() {
		
	}
	public PurchaseVO findPurchase(int tranNo)throws Exception{
		
		Connection con = DBUtil.getConnection();

		String sql = "select * "
				+ "				from transaction t,product p,users u "
				+ "								where t.prod_no=p.prod_no "
				+ "									and t.buyer_id=u.user_id "
				+ "									and t.tran_no = ?";
				
		
		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.setInt(1, tranNo);

		ResultSet rs = stmt.executeQuery();

		System.out.println("DAO tranNo >>"+tranNo);
		PurchaseVO purchaseVO = new PurchaseVO();
		while (rs.next()) {
			ProductVO productVO = new ProductVO();
			UserVO userVO = new UserVO();
			userVO.setUserId(rs.getString("buyer_id")); // join된 userId 정보로 가져온다
			productVO.setProdNo(rs.getInt("prod_no"));  // join된 상품번호 가져온다.
			purchaseVO.setTranNo(rs.getInt("tran_no")); // 구매 번호 
			purchaseVO.setPurchaseProd(productVO);
			purchaseVO.setBuyer(userVO);
			purchaseVO.setPaymentOption(rs.getString("payment_option"));// 지불방법
			purchaseVO.setReceiverName(rs.getString("receiver_name")); //받는사람 일므
			purchaseVO.setReceiverPhone(rs.getString("receiver_phone")); //받는사람 전화번호
			purchaseVO.setDivyAddr(rs.getString("demailaddr")); // 배송지 주소
			purchaseVO.setDivyRequest(rs.getString("dlvy_request")); //배송시 요구사항
			purchaseVO.setTranCode(rs.getString("tran_status_code")); //구매상태코드
			purchaseVO.setOrderDate(rs.getDate("order_data")); //구매 일자
			purchaseVO.setDivyDate(rs.getString("dlvy_date")); //배송 희망 일자
		}
		System.out.println("DAO purchaseVO >>"+purchaseVO);
		//System.out.println("DAO purchaseVO >>"+rs.getString("buyer_id"));
		
		
		
		con.close();

		return purchaseVO;


	}
	public PurchaseVO findPurchase2 (int prodNo)throws Exception{
		Connection con = DBUtil.getConnection();
		
		String sql = "select t.tran_no "
				+ "from transaction t, product p "
				+ "where t.prod_no = p.prod_no "
				+ "and p.prod_no = ? ";
		PreparedStatement stmt =con.prepareStatement(sql);
		stmt.setInt(1,prodNo);
		
		ResultSet rs = stmt.executeQuery();
		
		PurchaseVO purchaseVO = new PurchaseVO();
		while (rs.next()) {
		purchaseVO.setTranNo(rs.getInt("tran_no"));
		}
		con.close();
		
		return purchaseVO;
	}
public HashMap<String, Object> getPurchaseList(SearchVO searchVO,String buyerId) throws Exception {
		
		Connection con = DBUtil.getConnection();
		
		String sql = "select "
				+ "t.tran_no, p.prod_no,u.user_id,t.receiver_name,t.receiver_phone,t.tran_status_code "
				+ "from transaction t,product p,users u "
				+ "where t.prod_no=p.prod_no "
				+ "	and t.buyer_id=u.user_id "
				+ "	and u.user_id=? ";
				System.out.println("sql print"+sql);
		PreparedStatement stmt = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

		
		stmt.setString(1, buyerId);
		ResultSet rs = stmt.executeQuery();
		System.out.println(buyerId);
		System.out.println("stmt print "+sql);
		rs.last();
		int total = rs.getRow();
		System.out.println("로우의 수:" + total);

		HashMap<String,Object> map = new HashMap<String,Object>();
		map.put("count", new Integer(total));

		rs.absolute(searchVO.getPage() * searchVO.getPageUnit() - searchVO.getPageUnit()+1);
		
		System.out.println(searchVO.getPage() * searchVO.getPageUnit() - searchVO.getPageUnit()+1);
		System.out.println("searchVO.getPage():" + searchVO.getPage());
		System.out.println("searchVO.getPageUnit():" + searchVO.getPageUnit());

		ArrayList<PurchaseVO> list = new ArrayList<PurchaseVO>();
		if (total > 0) {
			for (int i = 0; i < searchVO.getPageUnit(); i++) {
				PurchaseVO vo = new PurchaseVO();
				UserVO userVO = new UserVO();
				userVO.setUserId(rs.getString("user_id"));
				System.out.println("rs>>>>>>>>>>>"+rs.getString("user_id"));
				System.out.println("rs>>>>>>>>>>>"+rs.getInt("prod_no"));
				vo.setReceiverName(rs.getString("receiver_name"));
				vo.setTranNo(rs.getInt("tran_no"));
				vo.setBuyer(userVO);
				//vo.setPaymentOption(rs.getString("payment_option"));
				vo.setReceiverPhone(rs.getString("receiver_phone"));
				//vo.setDivyAddr(rs.getString("demailaddr"));
				//vo.setDivyRequest(rs.getString("dlvy_request"));
				vo.setTranCode(rs.getString("tran_status_code"));
				System.out.println("tran_status_code: "+rs.getString("tran_status_code")+":  :");
				//vo.setOrderDate(rs.getDate("order_data"));
				//vo.setDivyDate(rs.getString("dlvy_date"));
				
				

				list.add(vo);
				if (!rs.next())
					break;
			}
		}
		System.out.println("list.size() : "+ list.size());
		map.put("list", list);
		System.out.println("map().size() : "+ map.size());

		con.close();
			
		return map;
	}
public HashMap<String,Object> getSaleList(SearchVO searchVO) throws Exception {
	
	Connection con = DBUtil.getConnection();
	
	String sql = "select * from USERS ";
	if (searchVO.getSearchCondition() != null) {
		if (searchVO.getSearchCondition().equals("0")) {
			sql += " where USER_ID='" + searchVO.getSearchKeyword()
					+ "'";
		} else if (searchVO.getSearchCondition().equals("1")) {
			sql += " where USER_NAME='" + searchVO.getSearchKeyword()
					+ "'";
		}
	}
	sql += " order by USER_ID";

	PreparedStatement stmt = 
		con.prepareStatement(	sql,
													ResultSet.TYPE_SCROLL_INSENSITIVE,
													ResultSet.CONCUR_UPDATABLE);
	ResultSet rs = stmt.executeQuery();

	rs.last();
	int total = rs.getRow();
	System.out.println("로우의 수:" + total);

	HashMap<String,Object> map = new HashMap<String,Object>();
	map.put("count", new Integer(total));

	rs.absolute(searchVO.getPage() * searchVO.getPageUnit() - searchVO.getPageUnit()+1);
	
	System.out.println(searchVO.getPage() * searchVO.getPageUnit() - searchVO.getPageUnit()+1);
	System.out.println("searchVO.getPage():" + searchVO.getPage());
	System.out.println("searchVO.getPageUnit():" + searchVO.getPageUnit());

	ArrayList<PurchaseVO> list = new ArrayList<PurchaseVO>();
	if (total > 0) {
		for (int i = 0; i < searchVO.getPageUnit(); i++) {
			PurchaseVO vo = new PurchaseVO();
			UserVO uvo = null;
			ProductVO pvo = null;
			uvo.setUserId(rs.getString("buyer_id"));
			pvo.setProdNo(rs.getInt("prod_no"));
			vo.setTranNo(rs.getInt("tran_no"));
			vo.setPurchaseProd(pvo);
			vo.setBuyer(uvo);
			vo.setPaymentOption(rs.getString("payment_option"));
			vo.setReceiverName(rs.getString("receiver_name"));
			vo.setReceiverPhone(rs.getString("receiver_phone"));
			vo.setDivyAddr(rs.getString("demailaddr"));
			vo.setDivyRequest(rs.getString("dlvy_request"));
			vo.setTranCode(rs.getString("tran_status_code"));
			vo.setOrderDate(rs.getDate("order_data"));
			vo.setDivyDate(rs.getString("dlvy_date"));
			
			list.add(vo);
			if (!rs.next())
				break;
		}
	}
	System.out.println("list.size() : "+ list.size());
	map.put("list", list);
	System.out.println("map().size() : "+ map.size());

	con.close();
		
	return map;
}
	
public void insertPurchase(PurchaseVO purchaseVO) throws Exception {
	
	Connection con = DBUtil.getConnection();

String sql = "insert into transaction values  "
		+ "(seq_transaction_tran_no.nextval,?,?,?,?,?,?,?,'001',sysdate,?)";
	
	PreparedStatement stmt = con.prepareStatement(sql);
	stmt.setInt(1, purchaseVO.getTranNo());
	stmt.setString(3, purchaseVO.getPaymentOption());
	stmt.setString(2, purchaseVO.getBuyer().getUserId());
	stmt.setString(4, purchaseVO.getReceiverName());
	stmt.setString(5, purchaseVO.getReceiverPhone());
	stmt.setString(6, purchaseVO.getDivyAddr());
	stmt.setString(7, purchaseVO.getDivyRequest());
	//stmt.setDate(10, purchaseVO.getOrderDate());// sysdate 로 구매일자 대체
	stmt.setString(8, purchaseVO.getDivyDate());
	System.out.println("sql insert>>>>>>>>>>"+stmt);
	stmt.executeUpdate();
	
	
	
	con.close();
}
public void updatePurchase(PurchaseVO purchaseVO) throws Exception {
	
	Connection con = DBUtil.getConnection();

	String sql = "update TRANSACTION set PAYMENT_OPTION=?, RECEIVER_NAME=?, RECEIVER_PHONE=?, demailaddr=?, DLVY_REQUEST=?, DLVY_DATE=?   where Tran_No=?";
	
	PreparedStatement stmt = con.prepareStatement(sql);
	stmt.setString(1, purchaseVO.getPaymentOption());
	stmt.setString(2, purchaseVO.getReceiverName());
	stmt.setString(3, purchaseVO.getReceiverPhone());
	stmt.setString(4, purchaseVO.getDivyAddr());
	stmt.setString(5, purchaseVO.getDivyRequest());
	stmt.setString(6, purchaseVO.getDivyDate());
	stmt.setInt(7, purchaseVO.getTranNo());
	System.out.println("sql>>>>>>>>>>>>"+stmt);
	stmt.executeUpdate();
	
	con.close();
}
public void updateTranCode(PurchaseVO purchaseVO) throws Exception {
	
	Connection con = DBUtil.getConnection();

	String sql = "update TRANSACTION set TRAN_STATUS_CODE=? where Tran_No=?";
	System.out.println("update>"+sql);
	System.out.println("purchaseVO"+purchaseVO);
	PreparedStatement stmt = con.prepareStatement(sql);
	stmt.setString(1, purchaseVO.getTranCode());
	stmt.setInt(2, purchaseVO.getTranNo());
	//stmt.executeUpdate();
	if(stmt.executeUpdate()==1) {
		System.out.println("성공");
	}
	con.close();
}
	
}