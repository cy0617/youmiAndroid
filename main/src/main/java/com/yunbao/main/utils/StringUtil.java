package com.yunbao.main.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

    public static String subZeroAndDot(BigDecimal decimal) {
        String s = decimal.toPlainString();
        if (s.indexOf(".") > 0) {
            s = s.replaceAll("0+?$", "");//去掉多余的0
            s = s.replaceAll("[.]$", "");//如最后一位是.则去掉
        }
        return s;
    }

    /**
     * 判断是否包含中文
     *
     * @param str
     * @return
     */
    public static boolean isContainChinese(String str) {
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
    }


    /**
     * 判断给定字符串是否空白串 空白串是指由空格、制表符、回车符、换行符组成的字符串 若输入字符串为null或空字符串，返回true
     */
    public static boolean isEmpty(CharSequence input) {
        if (input == null || "".equals(input) || "null".equals(input))
            return true;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return false;
            }
        }
        return true;
    }

    /**
     * BigDecimal   保留小数位
     *
     * @param num1      需要处理的数据
     * @param retainNum 小数位
     * @return
     */
    public static String bigDecimalReservedDecimalPlaces(BigDecimal num1, int retainNum) {
        BigDecimal one = new BigDecimal("1");
        NumberFormat format = getNumberFormat(retainNum);
        String s = subZeroAndDot(num1.toPlainString());
        double v = new BigDecimal(s).divide(one, retainNum, BigDecimal.ROUND_HALF_UP).doubleValue();
        return format.format(v);
    }

    /**
     * 处理BigDecimal接收的数据展示出多余的8个0
     *
     * @param s
     * @return
     */
    public static String subZeroAndDot(String s) {
        if (s != null && !s.equals("")) {
            if (s.indexOf(".") > 0) {
                s = s.replaceAll("0+?$", "");//去掉多余的0
                s = s.replaceAll("[.]$", "");//如最后一位是.则去掉
            }
            return s;
        } else {
            return " ";
        }
    }

    /**
     * 此方法只是关闭软键盘
     */
    public static void Closekeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive() && activity.getCurrentFocus() != null) {
            if (activity.getCurrentFocus().getWindowToken() != null) {
                imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    /**
     * 实现文本复制功能
     *
     * @param content 要复制的内容
     */
    @SuppressLint("NewApi")
    public static void copy(String content, Context context) {
        // 得到剪贴板管理器
        ClipboardManager cmb = (ClipboardManager) context
                .getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setText(content.trim());
    }


    /**
     * 提供NumberFormat对象
     * @param retainNum
     * @return
     */
    public static NumberFormat getNumberFormat(int retainNum){
        NumberFormat format = NumberFormat.getInstance();
        format.setMaximumFractionDigits(retainNum);
        format.setGroupingUsed(false);
        return format;
    }


    /**
     * BigDecimal   精确除法计算
     *
     * @param num1      被除数
     * @param num2      除数  (不能为0)
     * @param retainNum 保留小数位
     * @param upOrDown  true向上取整 false向下取整
     * @return
     */
    public static String bigDecimalDivide(BigDecimal num1, BigDecimal num2, int retainNum, boolean upOrDown) {
        NumberFormat format = getNumberFormat(retainNum);
        if (!checkNotZero(num1)) {
            if (!checkNotZero(num2)) {
                double v = 0.0;
                if (upOrDown) {
                    v = num1.divide(num2, retainNum, BigDecimal.ROUND_HALF_UP).doubleValue();
                } else {
                    v = num1.divide(num2, retainNum, BigDecimal.ROUND_DOWN).doubleValue();
                }
                return format.format(v);
            } else {
                return "0.0";
            }
        } else {
            return "0.0";
        }
    }

    /**
     * BigDecimal   精确乘法计算
     *
     * @param num1      被乘数
     * @param num2      乘数
     * @param retainNum 保留位数
     * @param upOrDown  true向上取整 false向下取整
     * @return
     */
    public static String bigDecimalMultiply(BigDecimal num1, BigDecimal num2, int retainNum, boolean upOrDown) {
        BigDecimal zero = new BigDecimal("0");
        BigDecimal one = new BigDecimal("1");
        NumberFormat format = getNumberFormat(retainNum);

        BigDecimal multiply = num1.multiply(num2);

        int i = multiply.compareTo(zero);
        if (i == 0) {
            return "0.0";
        } else {
            double v = 0.0;
            if (upOrDown) {
                v = multiply.divide(one, retainNum, BigDecimal.ROUND_HALF_UP).doubleValue();
            } else {
                v = multiply.divide(one, retainNum, BigDecimal.ROUND_DOWN).doubleValue();
            }
            return format.format(v);
        }
    }


    /**
     * 判断当前数字是否等于0
     *
     * @param number
     * @return
     */
    public static boolean checkNotZero(BigDecimal number) {
        BigDecimal zero = new BigDecimal("0");
        int i = number.compareTo(zero);
        if (i == 0) {
            return true;
        } else {
            return false;
        }
    }



}
