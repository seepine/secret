package com.seepine.secret.util;


import javax.annotation.Nullable;

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
   * 获取客户端ip
   *
   * @param xForwardedFor   x-forwarded-for请求头
   * @param proxyClientIp   Proxy-Client-IP请求头
   * @param wlProxyClientIp WL-Proxy-Client-IP请求头
   * @param remoteAddr      request的remoteAddr
   * @return ip地址
   */
  @Nullable
  public static String getIp(@Nullable String xForwardedFor, @Nullable String proxyClientIp, @Nullable String wlProxyClientIp, @Nullable String remoteAddr) {
    String ipAddress = null;
    try {
      ipAddress = xForwardedFor;
      if (invalidIp(ipAddress)) {
        ipAddress = proxyClientIp;
      }
      if (invalidIp(ipAddress)) {
        ipAddress = wlProxyClientIp;
      }
      if (invalidIp(ipAddress)) {
        ipAddress = remoteAddr;
        if (LOCALHOST.equals(ipAddress) || LOCALHOST_IP6.equals(ipAddress)) {
          ipAddress = LOCALHOST;
        }
      }
      if (ipAddress != null && ipAddress.length() > IP_LENGTH) {
        if (ipAddress.indexOf(COMMA) > 0) {
          ipAddress = ipAddress.substring(0, ipAddress.indexOf(COMMA));
        }
      }
    } catch (Exception ignore) {
    }
    return ipAddress;
  }

  private static boolean invalidIp(@Nullable String ipAddress) {
    return ipAddress == null || ipAddress.length() == 0 || UNKNOWN.equalsIgnoreCase(ipAddress);
  }
}
