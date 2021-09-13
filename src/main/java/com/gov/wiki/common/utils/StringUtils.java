package com.gov.wiki.common.utils;

import java.util.Collection;
import java.util.Iterator;
import java.util.regex.Pattern;

public abstract class StringUtils {

	public static final String ID_CARD_REGEX_15 = "^[1-9]\\d{5}\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{2}$";
	public static final String ID_CARD_REGEX_18 = "^[1-9]\\d{5}(18|19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$";
	
	/**
	 * 检测string 是否为空
	 * <pre>
     * StringUtils.isBlank(null)      = true
     * StringUtils.isBlank("")        = true
     * StringUtils.isBlank(" ")       = true
     * StringUtils.isBlank("bob")     = false
     * StringUtils.isBlank("  bob  ") = false
     * </pre>
	 * @param string
	 * @return
	 */
	public static boolean isBlank(String string){
		return !isNotBlank(string);
	}
	
	/**
	 * 检测string 是否不为空
	 * @param string
	 * @return
	 * 		!isBlank(string)
	 */
	public static boolean isNotBlank(String string){
		return org.springframework.util.StringUtils.hasText(string);
	}
	
	/**
	 * 校验是否是网址
	 * @param url
	 * @return
	 * 		 true 是  false 否
	 */
	public static boolean isUrl(String url){
		if(url == null){
			return false;
		}
		String regex = "^([hH][tT]{2}[pP]:/*|[hH][tT]{2}[pP][sS]:/*|[fF][tT][pP]:/*)(([A-Za-z0-9-~]+).)+([A-Za-z0-9-~\\/])+(\\?{0,1}(([A-Za-z0-9-~]+\\={0,1})([A-Za-z0-9-~]*)\\&{0,1})*)$";
		Pattern pattern = Pattern.compile(regex);
		return pattern.matcher(url).matches() || url.startsWith("http://") || url.startsWith("https://");
	}

	public static String listToString(Collection<?> collection, String split) {
		StringBuffer result = new StringBuffer("");
		if(collection == null || collection.isEmpty()) {
			return result.toString();
		}
		for (Object o : collection) {
			result.append(o).append(split);
		}
		return result.toString();
	}

	public static String join(Collection<?> collection,String split) {
		if(collection == null) return null;
		Iterator iterator = collection.iterator();
		StringBuffer sb = new StringBuffer();
		sb.append(iterator.next());
		while (iterator.hasNext()) {
			sb.append(split);
			sb.append(iterator.next());
		}
		return sb.toString();
	}

	public static String randomStr(int i) {
		if(i <= 0) return null; 
		String str = "1234567890QWERTYUIOPASDFGHJKLZXCVBNMqwertyuioplkjhgfdsazxcvbnm";
		char[] chars = str.toCharArray();
		char[] result = new char[i];
		for (int j = 0; j < i; j++) {
			result[j] = chars[(int) (Math.random()*chars.length)];
		}
		return String.valueOf(result);
	}

	/**
	 * 校验是否是身份证号
	 * @param str
	 * @return
	 */
	public static boolean isIdCard(String str) {
		if(isBlank(str)) return false;
		if(str.length() != 15 && str.length() != 18) return false;
		return pattern(ID_CARD_REGEX_18, str) || pattern(ID_CARD_REGEX_15, str);
	}
	
	public static boolean pattern(String regex,String str) {
		if(isBlank(str)) return false;
		Pattern pattern = Pattern.compile(regex);
		return pattern.matcher(str).matches();
	}
}
