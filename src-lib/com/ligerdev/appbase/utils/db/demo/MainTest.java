package com.ligerdev.appbase.utils.db.demo;

import java.util.ArrayList;

import com.ligerdev.appbase.utils.db.BaseDAO;

public class MainTest {

	private static BaseDAO baseDAO = BaseDAO.getInstance("main");
	
	public static void main(String[] args) {
		String transid = "test";
		
		// ========== demo một số hàm thường gặp ============
		
		// get theo ID của bảng
		{
			StudAccount bean = baseDAO.getBeanByKey(transid, StudAccount.class, 2);
			System.out.println("getBeanByKey: " + String.valueOf(bean)); 
		}
		
		// get cell đầu tiên trong bảng (thường dùng trong count, sum ...)
		{
			String sql = "select count(*) from cp_account";
			Integer count = baseDAO.getFirstCell(transid, sql, Integer.class);
			System.out.println("getFirstCell: " + count); 
		}
		
		// get list object bằng SQL
		{
			String sql = "select * from cp_account";  // có thể thêm điều kiện where thoải mái
			ArrayList<StudAccount> list = baseDAO.getListBySql(transid, StudAccount.class, sql, 0, 9);
			// trường hợp ko muốn phân trang mà lấy all thì truyền beginIndex và pageSize là null
			// trường hợp sql có các parameter ? thì truyền theo thứ tự các giá trị params vào cuối hàm
			
			System.out.println("getListBySql: " + list.size()); 
		}
		
		// get 1 object bằng SQL
		{
			String sql = "select * from cp_account where id = 1"; 
			StudAccount bean = baseDAO.getBeanBySql(transid, StudAccount.class, sql);
			System.out.println("getBeanBySql: " + String.valueOf(bean)); 
		}
		
		// update 1 object
		{
			StudAccount bean = baseDAO.getBeanByKey(transid, StudAccount.class, 2);
			bean.setFullName("Nguyen Van Duc");
			int updatedRow = baseDAO.updateBean(transid, bean);
			System.out.println("updateBean: " + updatedRow); 
		}
		
		
		// update 1 list các objects
		{
			StudAccount bean = baseDAO.getBeanByKey(transid, StudAccount.class, 2);
			bean.setFullName("Nguyen Van Duc");
			
			ArrayList<StudAccount> list = new ArrayList<StudAccount>();
			list.add(bean);
			
			int updatedRow = baseDAO.updateList(transid, list);
			System.out.println("updateList: " + updatedRow); 
		}
		
		// update 1 số trường trong object (ko update tất cả)
		{
			StudAccount bean = baseDAO.getBeanByKey(transid, StudAccount.class, 2);
			bean.setFullName("Nguyen Van Duc");
			
			// giả sử chỉ muốn update full_name
			int updatedRow = baseDAO.updateBeanFields(transid, bean, true, "full_name");
			System.out.println("updateBeanFields: " + updatedRow); 
			// (tương tự với 1 list các object)
		}
		
		// update tất cả ngoại trừ 1 số trường trong object
		{
			StudAccount bean = baseDAO.getBeanByKey(transid, StudAccount.class, 2);
			bean.setFullName("Nguyen Van Duc");
			
			// giả sử ko muốn update trường cpid
			int updatedRow = baseDAO.updateBeanFields(transid, bean, false, "cpid");
			System.out.println("updateBeanFields: " + updatedRow); 
			// (tương tự với 1 list các object)
		}
		
		// các hàm và các tham số tương tự đối với lệnh insert, delete
		{
			/*
			baseDAO.insert(transid, listBean);
			baseDAO.insert(transid, bean);
			baseDAO.insertFields(transid, listBean, withFields, fields);
			*/
			
			/*
			baseDAO.delete(transid, listBean);
			baseDAO.delete(transid, bean);
			*/
		}
		
		
		// thực hiện 1 lệnh insert, update tự do
		{
			String sql = "update cp_account set full_name = 'abc' where id = 1";
			// cũng có thể dùng cho delete: sql  = delete from table name where ....
			int updatedRow = baseDAO.execSql(transid, sql);
			System.out.println("execSql: " + updatedRow); 
		}
		
		
		// check tính hợp lệ của SQL, thường dùng trong test, hoặc check xem có tồn tại bảng nào đó hay ko
		{
			String sql = "select 1 from abc";
			boolean isValidSql = baseDAO.isValidSql(transid, sql);
			System.out.println("isValidSql: " + isValidSql); 
		}
	}
}
