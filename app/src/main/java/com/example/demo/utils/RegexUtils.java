package com.example.demo.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <pre>
 *     author: Blankj
 *     blog  : http://blankj.com
 *     time  : 2016/8/2
 *     desc  : 正则相关工具类
 * </pre>
 */
public class RegexUtils {
    //英文特殊符号的转义：$()*+.[]?\^{}|
    public static final String SPECIAL_SIGN = "\\\\$\\(\\)\\*\\+\\.\\[\\]\\?\\^\\{\\}\\|";
    //所有英文符号
    public static final String ALL_ENGLISH_SIGN = SPECIAL_SIGN + "!\"#%&',-/:;<=>@_~`";

    private RegexUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * If u want more please visit http://toutiao.com/i6231678548520731137/
     */

    /**
     * 验证手机号（简单）
     *
     * @param string 待验证文本
     * @return {@code true}: 匹配<br>{@code false}: 不匹配
     */
    public static boolean isMobileSimple(String string) {
        return isMatch(RegexConstants.REGEX_MOBILE_SIMPLE, string);
    }

    /**
     * 验证手机号（简单）
     *
     * @param input 待验证文本
     * @return {@code true}: 匹配<br>{@code false}: 不匹配
     */
    public static boolean isMobileSimple(final CharSequence input) {
        return isMatch(RegexConstants.REGEX_MOBILE_SIMPLE, input);
    }

    /**
     * 判断是否是合法密码（以字母开头，允许6~20字节，允许字母数字下划线）
     *
     * @param input 待验证文本
     * @return {@code true}: 匹配<br>{@code false}: 不匹配
     */
    public static boolean isPassword(final CharSequence input) {
        return isMatch(RegexConstants.REGEX_PASSWORD, input);
    }

    /**
     * 判断是否是合法登录密码（允许6~20字节，必须字母数字相结合）
     *
     * @param input 待验证文本
     * @return {@code true}: 匹配<br>{@code false}: 不匹配
     */
    public static boolean isLoginPassword(final CharSequence input) {
        return isMatch(RegexConstants.REGEX_PASSWORD_LOGIN, input);
    }


    public static boolean isUS_NUM_ZH(final CharSequence input) {
        String regex="^[a-zA-Z0-9\u4E00-\u9FA5]+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher match=pattern.matcher(input);
        boolean b=match.matches();
        return b;
    }
    public static boolean isNUM_ZH(final CharSequence input) {
        String regex="^[0-9\u4E00-\u9FA5]+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher match=pattern.matcher(input);
        boolean b=match.matches();
        return b;
    }

    /**
     * 验证手机号（精确）
     *
     * @param string 待验证文本
     * @return {@code true}: 匹配<br>{@code false}: 不匹配
     */
    public static boolean isMobileExact(String string) {
        return isMatch(RegexConstants.REGEX_MOBILE_EXACT, string);
    }

    /**
     * 验证手机号（精确）
     *
     * @param input 待验证文本
     * @return {@code true}: 匹配<br>{@code false}: 不匹配
     */
    public static boolean isMobileExact(final CharSequence input) {
        return isMatch(RegexConstants.REGEX_MOBILE_EXACT, input);
    }

    /**
     * 判断是否是合法验证码（4位数字）
     *
     * @param input 待验证文本
     * @return {@code true}: 匹配<br>{@code false}: 不匹配
     */
    public static boolean isCaptcha(final CharSequence input) {
        return isMatch(RegexConstants.REGEX_CAPTCHA, input);
    }

    /**
     * 判断是否是合法验证码（4位数字）
     *
     * @param input 待验证文本
     * @return {@code true}: 匹配<br>{@code false}: 不匹配
     */
    public static boolean isCaptcha_6(final CharSequence input) {
        return isMatch(RegexConstants.REGEX_CAPTCHA_6, input);
    }

    /**
     * 判断是否匹配正则
     *
     * @param regex 正则表达式
     * @param input 要匹配的字符串
     * @return {@code true}: 匹配<br>{@code false}: 不匹配
     */
    private static boolean isMatch(final String regex, final CharSequence input) {
        return input != null && input.length() > 0 && Pattern.matches(regex, input);
    }

    /**
     * 验证电话号码
     *
     * @param string 待验证文本
     * @return {@code true}: 匹配<br>{@code false}: 不匹配
     */
    public static boolean isTel(String string) {
        return isMatch(RegexConstants.REGEX_TEL, string);
    }

    /**
     * 验证身份证号码15位
     *
     * @param string 待验证文本
     * @return {@code true}: 匹配<br>{@code false}: 不匹配
     */
    public static boolean isIDCard15(String string) {
        return isMatch(RegexConstants.REGEX_IDCARD15, string);
    }

    /**
     * 验证身份证号码18位
     *
     * @param string 待验证文本
     * @return {@code true}: 匹配<br>{@code false}: 不匹配
     */
    public static boolean isIDCard18(String string) {
        return isMatch(RegexConstants.REGEX_IDCARD18, string);
    }


    /**
     * 验证邮箱
     *
     * @param string 待验证文本
     * @return {@code true}: 匹配<br>{@code false}: 不匹配
     */
    public static boolean isEmail(String string) {
        return isMatch(RegexConstants.REGEX_EMAIL, string);
    }

    /**
     * 验证URL
     *
     * @param string 待验证文本
     * @return {@code true}: 匹配<br>{@code false}: 不匹配
     */
    public static boolean isURL(String string) {
        return isMatch(RegexConstants.REGEX_URL, string);
    }

    /**
     * 验证汉字
     *
     * @param string 待验证文本
     * @return {@code true}: 匹配<br>{@code false}: 不匹配
     */
    public static boolean isChz(String string) {
        return isMatch(RegexConstants.REGEX_CHZ, string);
    }

    /**
     * 验证用户名
     * <p>取值范围为a-z,A-Z,0-9,"_",汉字，不能以"_"结尾,用户名必须是6-20位</p>
     *
     * @param string 待验证文本
     * @return {@code true}: 匹配<br>{@code false}: 不匹配
     */
    public static boolean isUsername(String string) {
        return isMatch(RegexConstants.REGEX_USERNAME, string);
    }

    /**
     * 验证yyyy-MM-dd格式的日期校验，已考虑平闰年
     *
     * @param string 待验证文本
     * @return {@code true}: 匹配<br>{@code false}: 不匹配
     */
    public static boolean isDate(String string) {
        return isMatch(RegexConstants.REGEX_DATE, string);
    }

    /**
     * 验证IP地址
     *
     * @param string 待验证文本
     * @return {@code true}: 匹配<br>{@code false}: 不匹配
     */
    public static boolean isIP(String string) {
        return isMatch(RegexConstants.REGEX_IP, string);
    }

    /**
     * string是否匹配regex
     *
     * @param regex  正则表达式字符串
     * @param string 要匹配的字符串
     * @return {@code true}: 匹配<br>{@code false}: 不匹配
     */
    public static boolean isMatch(String regex, String string) {
        return Pattern.matches(regex, string);
    }

    //判断是否全为特殊字符与emoji的组合
    public static boolean isErrorValue(String str) {
        for (int i = 0; i < str.length(); i++) {
            if (!RegexConstants.REGEX_UNCHAR.contains(String.valueOf(str.charAt(i))) && !isEmojiCharacter(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isErrorValue2(String str) {
        for (int i = 0; i < str.length(); i++) {
            if (RegexConstants.REGEX_UNCHAR.contains(String.valueOf(str.charAt(i))) || isEmojiCharacter(str.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    private static boolean isEmojiCharacter(char codePoint) {
        return !((codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA) ||
                (codePoint == 0xD) || ((codePoint >= 0x20) && (codePoint <= 0xD7FF)) ||
                ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) || ((codePoint >= 0x10000)
                && (codePoint <= 0x10FFFF)));
    }

    /**
     * 替换正则匹配的第一部分
     *
     * @param input       要替换的字符串
     * @param regex       正则表达式
     * @param replacement 代替者
     * @return 替换正则匹配的第一部分
     */
    public static String getReplaceFirst(final String input,
                                         final String regex,
                                         final String replacement) {
        if (input == null) return null;
        return Pattern.compile(regex).matcher(input).replaceFirst(replacement);
    }

    /**
     * 替换所有正则匹配的部分
     *
     * @param input       要替换的字符串
     * @param regex       正则表达式
     * @param replacement 代替者
     * @return 替换所有正则匹配的部分
     */
    public static String getReplaceAll(final String input,
                                       final String regex,
                                       final String replacement) {
        if (input == null) return null;
        return Pattern.compile(regex).matcher(input).replaceAll(replacement);
    }
}