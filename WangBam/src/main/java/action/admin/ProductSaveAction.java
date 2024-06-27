package action.admin;

import java.util.HashMap;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

import action.Action;
import mybatis.dao.ProductDAO;

public class ProductSaveAction implements Action {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String viewPath = null;
		String enc_type = request.getContentType();
		
		if(enc_type ==null) {
			viewPath = "/jsp/admin/productAdd.jsp";
		}else if(enc_type.startsWith("multipart")) {
			// 파일첨부가 된 상태에서 현재 객체가 불려진 경우
			// 폼에 enctype이 multipart....로 지정되었다면
			// 절대로 request.getParameter()로 값을 받지 못한다.
			try {
				ServletContext application = request.getServletContext();
				String imgPath = application.getRealPath("/img");
				
				// 첨부파일과 다른 파라미터들을 받기 위해
				// MultipartRequest 객체를 생
				MultipartRequest mr = new MultipartRequest(request, imgPath , 5*1024*1024, "utf-8", new DefaultFileRenamePolicy());
				// 이떄 이미 첨부파일은 upload라는 폴더에 저장된 상태다.
				// 나머지 파라미터 값들 받기
				String pd_name = mr.getParameter("pd_name");
				String ct_idx = mr.getParameter("ct_idx");
				String pd_cnt = mr.getParameter("pd_cnt");
				String pd_sale = mr.getParameter("pd_sale");
				String pd_price = mr.getParameter("pd_price");
				
				// 이미 업로드된 첨부파일이 파일명이 변경됐을 것 같은 느낌.
				String f = mr.getOriginalFileName("pd_thumbnail_img");
				String f2 = mr.getOriginalFileName("pd_detail_img");
				
				HashMap<String, String> map = new HashMap<>();
				map.put("pd_name", pd_name);
				map.put("ct_idx", ct_idx);
				map.put("pd_cnt", pd_cnt);
				map.put("pd_sale", pd_sale);
				map.put("pd_price", pd_price);
				map.put("pd_thumbnail_img", f);
				map.put("pd_detail_img", f2);
				
				ProductDAO.addProduct(map);
			
				viewPath = "/jsp/admin/productSave.jsp";
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
		return viewPath;
	}

}
