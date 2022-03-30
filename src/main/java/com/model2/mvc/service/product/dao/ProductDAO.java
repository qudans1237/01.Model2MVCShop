package com.model2.mvc.service.product.dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

import com.model2.mvc.common.SearchVO;
import com.model2.mvc.common.util.DBUtil;
import com.model2.mvc.service.product.vo.ProductVO;

public class ProductDAO {

	public ProductDAO() {
		// TODO Auto-generated constructor stub
	}
public void insertProduct(ProductVO productVO ) throws Exception {
		
		Connection con = DBUtil.getConnection();
		System.out.println(productVO);
		String sql = "insert into PRODUCT(prod_no ,prod_name,prod_detail,manufacture_day,price,image_file,reg_date) values (seq_product_prod_no.nextval,?,?,?,?,?,sysdate)";
		//String sql = "insert into PRODUCT VALUES (SEQ_PRODUCT_PROD_NO.NEXTVAL,?";
		//seq_product_prod_no.nextval로 넣을때 마다 번호를 매겨서 입력
		//sysdate는 sql문으로 현재날짜를 입력할때 사용
		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.setString(1, productVO.getProdName());
		stmt.setString(2, productVO.getProdDetail());
		stmt.setString(3, productVO.getManuDate());
		stmt.setInt(4, productVO.getPrice());
		stmt.setString(5, productVO.getFileName());
//		stmt.setDate(6, productVO.getRegDate());
		stmt.executeUpdate();
		
		con.close();
	}

	public ProductVO findProduct(int prodNo) throws Exception {
		
		Connection con = DBUtil.getConnection();

		String sql = "select * from Product where prod_no=?";
		
		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.setInt(1, prodNo);

		ResultSet rs = stmt.executeQuery();

		ProductVO productVO = null;
		while (rs.next()) {
			productVO = new ProductVO();
			productVO.setProdNo(rs.getInt("PROD_NO"));
			productVO.setProdName(rs.getString("PROD_NAME"));
			productVO.setManuDate(rs.getString("MANUFACTURE_DAY"));
			productVO.setPrice(rs.getInt("PRICE"));
			productVO.setFileName(rs.getString("IMAGE_FILE"));
			productVO.setProdDetail(rs.getString("PROD_DETAIL"));
			productVO.setRegDate(rs.getDate("REG_DATE"));
		}
		
		con.close();

		return productVO;
	}

	public HashMap<String,Object> getProductList(SearchVO searchVO) throws Exception {
		
		Connection con = DBUtil.getConnection();

		System.out.println("ProductDAO: searchCondition: "+ searchVO.getSearchCondition()+"  searchKeyword: "+searchVO.getSearchKeyword());
		String sqlcount = "	select count(*) "
				+ " from (select vr.* ,TRAN_STATUS_CODE d "
				+ "		from product vr,transaction t "
				+ "		where vr.prod_no = t.prod_no(+) "
				+ "		order by vr.prod_no) v";
		PreparedStatement stmtcount = con.prepareStatement(sqlcount);
		ResultSet rscount = stmtcount.executeQuery();
		rscount.next();
		int total =rscount.getInt("count(*)");
		
		System.out.println(total);
//		String sql = "select * from PRODUCT ";
		
		
		
		String sql = "select v.* FROM (select rownum rnum, vr.*,TRAN_STATUS_CODE from PRODUCT vr,transaction t where ";
		if (searchVO.getSearchCondition() != null) {
			System.out.println("ProductDAO: searchCondition Not Null"+searchVO.getSearchCondition());
			//검색조건이 들어오면
			if (searchVO.getSearchCondition().equals("0")) {
				//검색조건이 0번째의 검색조건에 해당하는 상품번호 이면
				sql += " PROD_NO like'%" + searchVO.getSearchKeyword()
						+ "%' and ";
			} else if (searchVO.getSearchCondition().equals("1")) {
				//검색조건이 1번째의 검색조건에 해당하는 상품이름 이면
				sql += " PROD_NAME like '%" + searchVO.getSearchKeyword()
						+ "%' and ";
			} else if (searchVO.getSearchCondition().equals("2")) {
				//검색조건이 2번째의 검색조건에 해당하는 상품가격 이면
				sql += " PRICE like '%" + searchVO.getSearchKeyword()
						+ "%' and ";
			}
		}else
			System.out.println("ProductDAO: searchCondition Null");
		
		sql += " vr.prod_no = t.prod_no(+)";
		sql += " order by vr.PROD_NO) v";
		sql += " where rnum BETWEEN "+(searchVO.getPage()*searchVO.getPageUnit()-2)+" and "+(searchVO.getPage()*searchVO.getPageUnit());
		System.out.println("ProductDAO: "+ sql);
		PreparedStatement stmt = 
			con.prepareStatement(	sql,
														ResultSet.TYPE_SCROLL_INSENSITIVE,  //스크롤에 둔감하다?
														// scroll에 따라서 이전 값도 나오고 다음값도 나오게
														ResultSet.CONCUR_UPDATABLE);//동의한다. 업데이트 가능?
														//데이터 변경이 가능하도록
		ResultSet rs = stmt.executeQuery();

		rs.last();
		//마지막 데이터를 가게 한다
		//int total = rs.getRow();
		
		System.out.println("로우의 수:" + total);	// 40 
		//sql의 로우의 수

		HashMap<String,Object> map = new HashMap<String,Object>();
		map.put("count", new Integer(total));

//		rs.absolute(searchVO.getPage() * searchVO.getPageUnit() - searchVO.getPageUnit()+1);
		rs.absolute(1);
		//구체적으로 몇번째 컬럼으로 커서를 옮기겠다
		//각페이지의 순서를 알려준다 
		//absolute 뒤에 있는 값만큼 이동하겠다. 돌려서 확인
		System.out.println("searchVO.getPage():" + searchVO.getPage());		// 1
		System.out.println("searchVO.getPageUnit():" + searchVO.getPageUnit());	// 3
		System.out.println("start page:"+(searchVO.getPage()*searchVO.getPageUnit()-2));
		System.out.println("end page:"+(searchVO.getPage()*searchVO.getPageUnit()));
		
		//vo 한 속성의 값
		//list 한 테이블 
		ArrayList<ProductVO> list = new ArrayList<ProductVO>();
		if (total > 0) {
			System.out.println(rs.getRow());
			for (int i = 0; i < rs.getRow(); i++) {
				System.out.println(i);
				ProductVO vo = new ProductVO();
				vo.setProdNo(rs.getInt("PROD_NO"));
				vo.setProdName(rs.getString("PROD_NAME"));
				vo.setManuDate(rs.getString("MANUFACTURE_DAY"));
				vo.setPrice(rs.getInt("PRICE"));
				vo.setFileName(rs.getString("IMAGE_FILE"));
				vo.setProdDetail(rs.getString("PROD_DETAIL"));
				vo.setRegDate(rs.getDate("REG_DATE"));
				vo.setProTranCode(rs.getString("TRAN_STATUS_CODE"));

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

	public void updateProduct(ProductVO ProductVO) throws Exception {
		
		Connection con = DBUtil.getConnection();

		String sql = "update PRODUCT set PROD_NAME=?,MANUFACTURE_DAY=?,PRICE=?,IMAGE_FILE=?,PROD_DETAIL=? where PROD_NO=?";
		
		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.setString(1, ProductVO.getProdName());
		stmt.setString(2, ProductVO.getManuDate());
		stmt.setInt(3, ProductVO.getPrice());
		stmt.setString(4, ProductVO.getFileName());
		stmt.setString(5, ProductVO.getProdDetail());
		stmt.setInt(6, ProductVO.getProdNo());
		
		stmt.executeUpdate();
		
		con.close();
	}

}