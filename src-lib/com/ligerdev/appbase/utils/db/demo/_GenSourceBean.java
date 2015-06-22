package com.ligerdev.appbase.utils.db.demo;

import com.ligerdev.appbase.utils.db.GenDbSource;

public class _GenSourceBean {

	
	public static void main(String[] args) {
		
		// gen ra class bean map tương ứng với 1 bảng trong DB
//		GenDbSource.genDTO("main", "cp_account", null, "id", "com.elcom.demo.db");
		GenDbSource.genDTO("main","sub_active_duplicate",null,"msisdn","com.vas.aps.db.orm");
		
		// gen ra class bean map tương ứng với kết quả của 1 câu SQL
//		String sql = "select a.id, b.name from STUDENT a inner join TEACHER b on a.tc_id = b.id where name is not null";
//		GenDbSource.genDTO("main", "special_bean", sql, "", "com.elcom.demo.db");
//		
//		// cũng có thể gen ra lớp DAO để tùy chỉnh và sử dụng thay cho BaseDao (nếu cần thiết)
//		GenDbSource.genDAO("main", "cp_account", null, "id", "com.elcom.demo.db");
	}
}
