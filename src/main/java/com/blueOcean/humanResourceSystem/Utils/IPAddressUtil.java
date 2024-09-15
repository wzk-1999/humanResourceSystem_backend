package com.blueOcean.humanResourceSystem.Utils;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class IPAddressUtil {

    // Method to get client IP address from the HttpServletRequest
    public static String getClientIpAddress() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            throw new IllegalStateException("No current HttpServletRequest");
        }

        HttpServletRequest request = attributes.getRequest();

        String remoteIp = request.getHeader("x-forwarded-for");
        if (remoteIp == null || remoteIp.isEmpty() || "unknown".equalsIgnoreCase(remoteIp)) {
            remoteIp = request.getHeader("Proxy-Client-IP");
        }
        if (remoteIp == null || remoteIp.isEmpty() || "unknown".equalsIgnoreCase(remoteIp)) {
            remoteIp = request.getHeader("WL-Proxy-Client-IP");
        }
        if (remoteIp == null || remoteIp.isEmpty() || "unknown".equalsIgnoreCase(remoteIp)) {
            remoteIp = request.getHeader("HTTP_CLIENT_IP");
        }
        if (remoteIp == null || remoteIp.isEmpty() || "unknown".equalsIgnoreCase(remoteIp)) {
            remoteIp = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (remoteIp == null || remoteIp.isEmpty() || "unknown".equalsIgnoreCase(remoteIp)) {
            remoteIp = request.getRemoteAddr();
        }

        // For cases where there are multiple proxies, get the first IP address
        if (remoteIp != null && remoteIp.length() > 15) { // "***.***.***.***".length() = 15
            if (remoteIp.indexOf(",") > 0) {
                remoteIp = remoteIp.substring(0, remoteIp.indexOf(","));
            }
        }
        return remoteIp;
    }
}
