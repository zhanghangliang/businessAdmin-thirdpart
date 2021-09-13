package com.gov.wiki.common.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alibaba.fastjson.JSONObject;

/**
 * @ClassName: MapUtil
 * @Description: 地图通用类
 * @author cys
 * @date 2018年9月14日 上午10:25:46
 */
public class MapUtil {
	private static final Logger log = LoggerFactory.getLogger(MapUtil.class);
	// QEPBZ-LQHW4-476UC-X7WAY-MHRT5-K7BZM
	// MKGBZ-UMK3F-PG4JG-JNPBP-FIVQ3-ETFYJ
	// M6DBZ-RKG3V-YEUPT-UEYWP-7MOT6-GSFP4
	private static final String USER_KEY = "M6DBZ-RKG3V-YEUPT-UEYWP-7MOT6-GSFP4";
	private static final DecimalFormat format = new DecimalFormat("00.######");
	private static double EARTH_RADIUS = 6378.137;
	
	public static void main(String[] args) throws IOException {
		Map<String, Object> map = getLngAndLat("双流县西南航港区临港路四段9号");
		System.out.println(map.toString());
		Map dataMap = findRangeByPoint(103.98995, 30.56773, 500);
		System.out.println(dataMap.toString());
	}

	/**
	 * @Title: getLngAndLat 
	 * @Description: 获取位置的经纬度信息
	 * @param 设定文件 
	 * @return Map<String,Object>    返回类型 
	 * @throws
	 */
	public static Map<String, Object> getLngAndLat(String address) {
		Map<String, Object> map = new HashMap<String, Object>();
		if(StringUtils.isBlank(address)) {
			return map;
		}
		log.info("解析地址：" + address);
		String url = "https://apis.map.qq.com/ws/geocoder/v1/?address=" + address
				+ "&output=json&key=" + USER_KEY;
		String json = loadJSON(url);
		if(StringUtils.isBlank(json)) {
			return map;
		}
		JSONObject obj = JSONObject.parseObject(json);
		if (obj.get("status").toString().equals("0")) {
			JSONObject result = obj.getJSONObject("result");
			if(result != null) {
				JSONObject location = result.getJSONObject("location");
				if(location != null) {
					map.put("lng", location.getDouble("lng"));
					map.put("lat", location.getDouble("lat"));
				}
				JSONObject addressComponents = result.getJSONObject("address_components");
				if(addressComponents != null) {
					map.put("province", addressComponents.getString("province"));
					map.put("city", addressComponents.getString("city"));
					map.put("district", addressComponents.getString("district"));
					map.put("street", addressComponents.getString("street"));
					map.put("street_number", addressComponents.getString("street_number"));
				}
				JSONObject adInfo = result.getJSONObject("ad_info");
				if(adInfo != null) {
					map.put("adcode", adInfo.getString("adcode"));
				}
			}
		} else {
			System.out.println("未找到相匹配的经纬度！错误信息：" + obj.get("message"));
			log.info("地址：" + address + "，未找到相匹配的经纬度！错误信息：" + obj.get("message"));
		}
		return map;
	}
	
	public static Map<String, Object> getLngAndLat(String address, String key) {
		Map<String, Object> map = new HashMap<String, Object>();
		if(StringUtils.isBlank(address)) {
			return map;
		}
		String url = "https://apis.map.qq.com/ws/geocoder/v1/?address=" + address
				+ "&output=json&key=" + key;
		String json = loadJSON(url);
		if(StringUtils.isBlank(json)) {
			return map;
		}
		JSONObject obj = JSONObject.parseObject(json);
		if (obj.get("status").toString().equals("0")) {
			JSONObject result = obj.getJSONObject("result");
			if(result != null) {
				JSONObject location = result.getJSONObject("location");
				if(location != null) {
					map.put("lng", location.getDouble("lng"));
					map.put("lat", location.getDouble("lat"));
				}
				JSONObject addressComponents = result.getJSONObject("address_components");
				if(addressComponents != null) {
					map.put("province", addressComponents.getString("province"));
					map.put("city", addressComponents.getString("city"));
					map.put("district", addressComponents.getString("district"));
					map.put("street", addressComponents.getString("street"));
					map.put("street_number", addressComponents.getString("street_number"));
				}
			}
		} else {
			System.out.println("未找到相匹配的经纬度！错误信息：" + obj.get("message"));
			log.info("地址：" + address + "，未找到相匹配的经纬度！错误信息：" + obj.get("message"));
		}
		return map;
	}

	public static String loadJSON(String url) {
		StringBuilder json = new StringBuilder();
		BufferedReader in = null;
		try {
			URL uri = new URL(url);
			URLConnection yc = uri.openConnection();
			in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
			String inputLine = null;
			while ((inputLine = in.readLine()) != null) {
				json.append(inputLine);
			}
			in.close();
		} catch (MalformedURLException e) {
			log.error("MalformedURLException:", e);
		} catch (IOException e) {
			log.error("IOException:", e);
		} finally {
			try {
				if (in != null) {
					in.close();
					in = null;
				}
			} catch (Exception ex) {
				log.error("Exception:", ex);
			}
		}
		return json.toString();
	}
	
	/**
	 * @Title: findRangeByPoint 
	 * @Description: 获取到某点经纬度特定距离的经纬度范围,距离单位为米
	 * @param 设定文件 
	 * @return Map    返回类型 
	 * @throws
	 */
	public static Map<String, Object> findRangeByPoint(double longitude, double latitude, double dis) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		double r = 6371;//地球半径千米
        dis = dis/1000; // 距离范围
        double dlng =  2*Math.asin(Math.sin(dis/(2*r))/Math.cos(latitude*Math.PI/180));
        dlng = dlng*180/Math.PI;//角度转为弧度
        double dlat = dis/r;
        dlat = dlat*180/Math.PI;        
        double minlat = latitude - dlat;
        double maxlat = latitude + dlat;
        double minlng = longitude - dlng;
        double maxlng = longitude + dlng;
        dataMap.put("minlat", format.format(minlat));
        dataMap.put("maxlat", format.format(maxlat));
        dataMap.put("minlng", format.format(minlng));
        dataMap.put("maxlng", format.format(maxlng));
        return dataMap;
	}
	
	/**
	 * @Title: getDistance 
	 * @Description: 计算两点间距离
	 * @param 设定文件 
	 * @return double    返回类型 
	 * @throws
	 */
	public static double getDistance(double lat1, double lng1, double lat2, double lng2) {
		double radLat1 = rad(lat1);
		double radLat2 = rad(lat2);
		double a = radLat1 - radLat2;
		double b = rad(lng1) - rad(lng2);
		double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
		s = s * EARTH_RADIUS;
		s = Math.round(s * 10000d) / 10000d;
		s = s * 1000;
		return s;
	}
	
	private static double rad(double d) {
		return d * Math.PI / 180.0;
	}
}