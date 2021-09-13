package com.gov.wiki.common.utils;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator.Builder;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.Verification;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class JwtUtil {
	private static final long EXPIRE_TIME = 18 * 60 * 60 * 1000;// 过期时间120分钟
	private static final String TOKEN_SECRET = "eyJhbGciOiJIUzI1NiJ9";

	public static boolean USE_TEST = false;
	public static String DEFAULT_TOKEN = "";
	public static boolean verify(String token, String username, String openId) {
		try {
			// 根据密码生成JWT效验器
			Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
			Verification verification = JWT.require(algorithm);
			if(StringUtils.isNotBlank(username)) {
				verification = verification.withClaim("username", username);
			}
			if(StringUtils.isNotBlank(openId)) {
				verification = verification.withClaim("openId", openId);
			}
			JWTVerifier verifier = verification.build();
			// 效验TOKEN
			DecodedJWT jwt = verifier.verify(token);
			return true;
		} catch (Exception exception) {
			return false;
		}
	}

	/**
	 * @Title: getUsername 
	 * @Description: 获得token中的信息无需secret解密也能获得 
	 * @param token 
	 * @return String 返回类型
	 * @throws
	 */
	public static String getUsername(String token) {
		try {
			DecodedJWT jwt = JWT.decode(token);
			return jwt.getClaim("username").asString();
		} catch (JWTDecodeException e) {
			return null;
		}
	}
	
	public static String getOpenId(String token) {
		try {
			DecodedJWT jwt = JWT.decode(token);
			return jwt.getClaim("openId").asString();
		} catch (JWTDecodeException e) {
			return null;
		}
	}
	
	public static String getToken() {
		if(USE_TEST) return DEFAULT_TOKEN;
		ServletRequestAttributes sra = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = sra.getRequest();
		String token = request.getHeader("token");
        token = StringUtils.isBlank(token)?request.getParameter("token"):token;
		return token;
	}
	
	public static String getUserId() {
		try {
			DecodedJWT jwt = JWT.decode(getToken());
			return jwt.getClaim("userId").asString();
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * @Title: getExpiresAt
	 * @Description: 获取token过期时间
	 * @return Date 返回类型
	 * @throws
	 */
	public static Date getExpiresAt() {
		try {
			DecodedJWT jwt = JWT.decode(getToken());
			return jwt.getExpiresAt();
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 生成签名,120min后过期
	 *
	 * @param username 用户名
	 * @param secret   用户的密码
	 * @return 加密的token
	 */
	public static String sign(String userId, String username, String openId) {
		Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
		Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
		Builder builder = JWT.create();
		if(StringUtils.isNotBlank(username)) {
			builder = builder.withClaim("username", username);
		}
		if(StringUtils.isNotBlank(openId)) {
			builder = builder.withClaim("openId", openId);
		}
		return builder.withClaim("userId", userId)
				.withExpiresAt(date)
				.sign(algorithm);
	}
}
