package com.vas.aps;

public class MainApplication {

	public static void main(String[] args) {

		if (args != null && args.length > 0 && args[0].equalsIgnoreCase("monthfee")) {
			
		} else {
			MainAps.main(args);
			MainMonfee.main(args);
		}
	}
}
