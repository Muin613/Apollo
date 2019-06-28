package com.munin.library.utils;

import java.security.MessageDigest;

/**
 * @author mingyao
 */
public class StringUtils {
    private static final char[] DIGITS_LOWER = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    public StringUtils() {
    }

    public static String concatString(String var0, String var1) {
        return (new StringBuilder(var0.length() + var1.length())).append(var0).append(var1).toString();
    }

    public static String concatString(String var0, String var1, String var2) {
        return (new StringBuilder(var0.length() + var1.length() + var2.length())).append(var0).append(var1).append(var2).toString();
    }

    public static String buildKey(String var0, String var1) {
        return concatString(var0, "://", var1);
    }

    public static String simplifyString(String var0, int var1) {
        return var0.length() <= var1 ? var0 : concatString(var0.substring(0, var1), "......");
    }

    public static String stringNull2Empty(String var0) {
        return var0 == null ? "" : var0;
    }

    public static String md5ToHex(String var0) {
        if (var0 == null) {
            return null;
        } else {
            try {
                MessageDigest var1 = MessageDigest.getInstance("MD5");
                return bytesToHexString(var1.digest(var0.getBytes("utf-8")));
            } catch (Exception var2) {
                return null;
            }
        }
    }

    private static String bytesToHexString(byte[] var0, char[] var1) {
        int var2 = var0.length;
        char[] var3 = new char[var2 << 1];
        int var4 = 0;

        for (int var5 = 0; var4 < var2; ++var4) {
            var3[var5++] = var1[(240 & var0[var4]) >>> 4];
            var3[var5++] = var1[15 & var0[var4]];
        }

        return new String(var3);
    }

    public static String bytesToHexString(byte[] var0) {
        return var0 == null ? "" : bytesToHexString(var0, DIGITS_LOWER);
    }

    public static boolean isNotBlank(String var0) {
        return !isBlank(var0);
    }

    public static boolean isBlank(String var0) {
        int var1;
        if (var0 != null && (var1 = var0.length()) != 0) {
            for (int var2 = 0; var2 < var1; ++var2) {
                if (!Character.isWhitespace(var0.charAt(var2))) {
                    return false;
                }
            }
            return true;
        } else {
            return true;
        }
    }

    public static boolean isStringEqual(String var0, String var1) {
        return var0 == null && var1 == null || var0 != null && var0.equals(var1);
    }
}
