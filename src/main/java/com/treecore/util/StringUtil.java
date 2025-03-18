package com.treecore.util;

import org.springframework.util.StringUtils;

public class StringUtil {

    /**
     * 문자열에서 XSS 공격에 사용될 수 있는 특수 문자를 제거합니다.
     * 
     * @param input 입력 문자열
     * @return 특수 문자가 제거된 문자열
     */
    public static String sanitizeString(String input) {
        if (!StringUtils.hasText(input)) {
            return input;
        }
        
        return input.replaceAll("<", "&lt;")
                   .replaceAll(">", "&gt;")
                   .replaceAll("\"", "&quot;")
                   .replaceAll("'", "&#39;")
                   .replaceAll("&", "&amp;")
                   .replaceAll("\\(", "&#40;")
                   .replaceAll("\\)", "&#41;");
    }
    
    /**
     * 문자열이 null이거나 빈 문자열인지 확인합니다.
     * 
     * @param str 확인할 문자열
     * @return null이거나 빈 문자열이면 true, 그렇지 않으면 false
     */
    public static boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }
    
    /**
     * 문자열이 null이 아니고 빈 문자열이 아닌지 확인합니다.
     * 
     * @param str 확인할 문자열
     * @return null이 아니고 빈 문자열이 아니면 true, 그렇지 않으면 false
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }
} 