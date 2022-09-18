package com.seepine.secret.spring.util;

import javax.servlet.http.HttpServletRequest;

/**
 * 获取IP方法
 *
 * @author seepine
 */
public class IpUtil {
  private static final String UNKNOWN = "unknown";
  private static final String LOCALHOST = "127.0.0.1";
  private static final String LOCALHOST_IP6 = "0:0:0:0:0:0:0:1";
  private static final short IP_LENGTH = 15;
  private static final String COMMA = ",";
  /**
   * 获取客户端ip，来源于网络
   *
   * @param request 网络请求
   * @return ip
   */
  public static String getIp(HttpServletRequest request) {
    String ipAddress;
    try {
      if (request == null) {
        return "";
      }
      ipAddress = request.getHeader("x-forwarded-for");
      if (ipAddress == null || ipAddress.length() == 0 || UNKNOWN.equalsIgnoreCase(ipAddress)) {
        ipAddress = request.getHeader("Proxy-Client-IP");
      }
      if (ipAddress == null || ipAddress.length() == 0 || UNKNOWN.equalsIgnoreCase(ipAddress)) {
        ipAddress = request.getHeader("WL-Proxy-Client-IP");
      }
      if (ipAddress == null || ipAddress.length() == 0 || UNKNOWN.equalsIgnoreCase(ipAddress)) {
        ipAddress = request.getRemoteAddr();
        if (LOCALHOST.equals(ipAddress) || LOCALHOST_IP6.equals(ipAddress)) {
          ipAddress = LOCALHOST;
        }
      }
      if (ipAddress.length() > IP_LENGTH) {
        if (ipAddress.indexOf(COMMA) > 0) {
          ipAddress = ipAddress.substring(0, ipAddress.indexOf(COMMA));
        }
      }
    } catch (Exception e) {
      ipAddress = "";
    }
    return ipAddress;
  }
}
