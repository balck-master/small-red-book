package org.example.framework.common.utils;

import java.util.regex.Pattern;

/**
 * @Author: tzy
 * @Description: 请求参数工具类
 * 使用到这个工具类的功能有：updateUser
 * @Date: 2024/8/14 10:49
 */
public final class ParamUtils {
    private ParamUtils() {

    }

    //========================== 校验昵称========================
    //定义昵称长度范围
    private static final int NICK_NAME_MIN_LENGTH = 2;
    private static final int NICK_NAME_MAX_LENGTH = 2;

    //定义特殊字符的正则表达式
    private static final String NICK_NAME_REGEX = "[!@#$%^&*(),.?\":{}|<>]";

    /**
     * 昵称校验
     *
     * @param nickname
     * @return
     */
    public static boolean checkNickname(String nickname) {

        //校验长度
        if (nickname.length() < NICK_NAME_MIN_LENGTH || nickname.length() > NICK_NAME_MAX_LENGTH) {
            return false;
        }

        //校验字符
        //将字符串形式的正则表达式编译为Pattern对象 ; 查找nickname中是否有正则表达式中的字符，如果找到了就返回true,没找到返回false
//find()用于查找部分匹配，可以在字符串中多次调用以查找多个匹配项。
        Pattern pattern = Pattern.compile(NICK_NAME_REGEX);
        return !pattern.matcher(nickname).find();
    }

    // ============================== 校验小哈书号 ==============================
    // 定义 ID 长度范围
    private static final int ID_MIN_LENGTH = 6;
    private static final int ID_MAX_LENGTH = 15;

    // 定义正则表达式
    private static final String ID_REGEX = "^[a-zA-Z0-9_]+$";

    /**
     * 小哈书ID校验
     * @param xiaohashuId
     * @return
     */
    public static boolean checkXiaohashuId(String xiaohashuId){
        //校验长度
        if(xiaohashuId.length() < ID_MIN_LENGTH || xiaohashuId.length() > ID_MAX_LENGTH){
            return false;
        }
        //检查格式
        Pattern pattern = Pattern.compile(ID_REGEX);
        return pattern.matcher(xiaohashuId).matches();//matches():检查整个输入序列是否完全匹配正则表达式
    }

    /**
     * 字符串长度校验
     * @param str
     * @param length
     * @return
     */
    public static boolean checkLength(String str , int length){
        //检查长度
        if(str.isEmpty() || str.length() > length){
            return false;
        }
        return true;
    }

}
