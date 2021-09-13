/**
 * @Title: AesUtil.java
 * @Package com.xiangtong.common.utils
 * @Description: TODO(用一句话描述该文件做什么)
 * @author cys
 * @date 2020年2月24日
 * @version V1.0
 */
package com.gov.wiki.common.utils;

import java.io.UnsupportedEncodingException;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.security.spec.InvalidParameterSpecException;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import com.alibaba.fastjson.JSONObject;

/**
 * @ClassName: AesUtil
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author cys
 * @date 2020年2月24日
 */
public class AesUtil {
	static {
		// BouncyCastle是一个开源的加解密解决方案，主页在http://www.bouncycastle.org/
		Security.addProvider(new BouncyCastleProvider());
	}

	/**
	 * @Title: decrypt
	 * @Description: AES解密
	 * @param data
	 * @param key
	 * @param iv
	 * @param encodingFormat
	 * @throws Exception
	 * @return String 返回类型
	 * @throws
	 */
	public static String decrypt(String data, String key, String iv, String encodingFormat){

		// 被加密的数据
		byte[] dataByte = Base64.decodeBase64(data);
		// 加密秘钥
		byte[] keyByte = Base64.decodeBase64(key);
		// 偏移量
		byte[] ivByte = Base64.decodeBase64(iv);
		try {
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");
			SecretKeySpec spec = new SecretKeySpec(keyByte, "AES");
			AlgorithmParameters parameters = AlgorithmParameters.getInstance("AES");
			parameters.init(new IvParameterSpec(ivByte));
			cipher.init(Cipher.DECRYPT_MODE, spec, parameters);// 初始化
			byte[] resultByte = cipher.doFinal(dataByte);
			if (null != resultByte && resultByte.length > 0) {
				String result = new String(resultByte, encodingFormat);
				return result;
			}
			return null;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidParameterSpecException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static JSONObject getSessionKeyOrOpenId(String code) {
		JSONObject json = null;
		try {
			String url = "https://api.weixin.qq.com/sns/jscode2session";
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("grant_type", "authorization_code");
			params.put("appid", "wx444a7fde12c68a4d");
			params.put("secret", "ba538785708bd83245c3ab8424fa183d");
			params.put("js_code", code);
			Object obj = HttpUtils.sentRequestData(url, "GET", params, null, String.class, "");
			if(obj != null && StringUtils.isNotBlank(obj.toString())) {
				json = JSONObject.parseObject(obj.toString());
			}
		}catch(Exception e) {
		}
		return json;
	}
	
	public static void main(String[] args) {
		/*
		 * String code = "043zoLHu0Y7EJg1jtwFu0gtKHu0zoLHh"; JSONObject json =
		 * getSessionKeyOrOpenId(code); if(json == null) { return; }
		 * System.out.println(json); String sessionKey = json.getString("session_key");
		 */
		String data = "Pg1VgSgrXOv+hubGWjigEes23FnhspiBy4TlX98mWJ/Lr2LW2MZIou6uAi1HFecbr/q3K8PUCGeaUyLxaOl0ta6esYLyKGrsRRnRYFHhR1sDnAtmAplTFTVCz8EHX2986+IQNXJc7BenkHuZc+qbQiUT+a+K9oRmVhVid57b2G2QLG6WzYHcylz6I6N/DIDcjd2sSsB/Qe12x/piEgMyB+e7wIn3dDptN0nuuPJB/NY9SR90V2vUpH5IzQ2u94pM0ZVlnG34vGE+ujLU1xrk1kSCANiOZNbM8CPZBBsOK/vlzRS1clL3JdnL9RDD3/STO6VeVEmN+IYweJyJ1jq46gulW67bcH5iv+2oHJ5RUjZYagkvS2Ef1Ypafu+h1Up8fgl8KDFFyKM6LqqbS+EJgL0FDmefRj59wi1f+PxZmlw7ZuLwN6cY+HjA7snLFU+l02aJhFjR/lp3FllStEUWwWz3inqmCC7EnAPT8k/wEtY=";
		String iv = "l/9UITFNYQfxXH3D6K93+g==";
		String jsonData = AesUtil.decrypt(data, "RCwdo3nfCCL2taihCldY7g==", iv, "UTF-8");
		System.out.println(jsonData);
	}
}