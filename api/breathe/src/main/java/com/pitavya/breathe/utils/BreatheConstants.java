package com.pitavya.breathe.utils;

public class BreatheConstants {

	public static enum RESPONSE_CODE {

		ERROR_OCCURED(500), SUCCESS(200);

		private int code;

		RESPONSE_CODE(int code) {
			this.setCode(code);
		}

		public int getCode() {
			return code;
		}

		public void setCode(int code) {
			this.code = code;
		}
	}

}
