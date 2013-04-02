/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.ixr.website.tools;

import java.util.UUID;

/**
 * 数据格式化
 * @author IXR
 */
public class DataFormat {
    /**
     * 生成唯一ID
     *
     * @return
     * @author IXR
     */
    public static String newPrimaryKey() {
        return UUID.randomUUID().toString();
    }
    
    /**
     * 数字自动补0填充
     *
     * @param num 数字
     * @param zero 补0填充的位数
     * @return
     */
    public static String autoZero(int num, int zero) {
        return String.format("%1$0" + zero + "d", num);
    }
}
