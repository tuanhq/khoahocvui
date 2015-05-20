package com.vas.aps.mpsclient;


public class UnreqResult {

	public static int CODE_SYSTEM_ERR = 3099;
	
	private int code;
	private int fee;

	public UnreqResult() {
		// TODO Auto-generated constructor stub
	}

	public UnreqResult(int code, int fee) {
		super();
		this.code = code;
		this.fee = fee;
	}
	
	public static UnreqResult parse(String s){
		try {
			String tmp[] = s.replace(" ", "").split("\\|");
			return new UnreqResult(Integer.parseInt(tmp[0]), Integer.parseInt(tmp[1]));
		} catch (Exception e) {
			return new UnreqResult(3099, 0);
		}
	}

	public boolean isSuccess() {
		// 0 = hủy thành công
		// 411 = đã hủy trước đó (hoặc ko tồn tại) ...
		// 414 = hủy trong chu kỳ cước
		if (code == 0 || code == 411 || code == 414) {
			return true;
		}
		return false;
	}

	public int getFee() {
		return fee;
	}

	public void setFee(int fee) {
		this.fee = fee;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
}
