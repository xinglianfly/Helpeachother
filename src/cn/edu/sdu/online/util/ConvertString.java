package cn.edu.sdu.online.util;

public  class ConvertString {
	
	public static String convert(String email){
		String emailc = null;
		if(email.contains("@")){
			emailc=email.replace("@", "at");
		}
		return emailc;
	}

}
