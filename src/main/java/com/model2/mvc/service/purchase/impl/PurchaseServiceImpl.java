package com.model2.mvc.service.purchase.impl;

import java.util.HashMap;

import com.model2.mvc.common.SearchVO;
import com.model2.mvc.service.purchase.PurchaseService;
import com.model2.mvc.service.purchase.dao.PurchaseDAO;
import com.model2.mvc.service.purchase.vo.PurchaseVO;

public class PurchaseServiceImpl implements PurchaseService {
	//Field
	private PurchaseDAO purchaseDAO;
	
	//Constructor
	public PurchaseServiceImpl() {
		purchaseDAO = new PurchaseDAO();
	}

	//Method
	//구매를 위한 비즈니스를 수행
	public void addPurchase(PurchaseVO purchaseVO) throws Exception {
		System.out.println("<<<<< PurchaseServiceImpl : addPurchase() 실행 >>>>>");
		purchaseDAO.insertPurchase(purchaseVO);
	}

	//구매 정보 상세조회를 위한 비즈니스를 수행
	public PurchaseVO getPurchase(int tranNo) throws Exception {
		System.out.println("<<<<< PurchaseServiceImpl : getPurchase() 실행 >>>>>");
		return purchaseDAO.findPurchase(tranNo);
	}
	
	//물건 재고 여부를 파악하기 위한 비즈니스를 수행
	public PurchaseVO getPurchase2(int ProdNo) throws Exception {
		System.out.println("<<<<< PurchaseServiceImpl : getPurchase2() 실행 >>>>>");
		return purchaseDAO.findPurchase2(ProdNo);
	}

	//구매 목록 보기를 위한 비즈니스를 수행
	public HashMap<String, Object> getPurchaseList(SearchVO searchVO, String buyerId) throws Exception {
		System.out.println("<<<<< PurchaseServiceImpl : getPurchaseList() 실행 >>>>>");
		return purchaseDAO.getPurchaseList(searchVO, buyerId);
	}
	
	//판매 목록 보기를 위한 비즈니스를 수행
	public HashMap<String, Object> getSaleList(SearchVO searchVO) throws Exception {
		System.out.println("<<<<< PurchaseServiceImpl : getSaleList() 실행 >>>>>");
		return purchaseDAO.getSaleList(searchVO);
	}
	
	//구매 정보 수정을 위한 비즈니스 수행
	public void updatePurcahse(PurchaseVO purchaseVO) throws Exception {
		System.out.println("<<<<< PurchaseServiceImpl : updatePurcahse() 실행 >>>>>");
		purchaseDAO.updatePurchase(purchaseVO);
	}

	//구매 상태 코드 수정을 위한 비즈니스 수행
	public void updateTranCode(PurchaseVO purchaseVO) throws Exception {
		System.out.println("<<<<< PurchaseServiceImpl : updateTranCode() 실행 >>>>>");
		purchaseDAO.updateTranCode(purchaseVO);
	}
	
}//end of class