package org.multimediaprototype.common.util;

import java.util.Map;

/**
 * Created by haihong.xiahh on 2015/12/30.
 */
public class StringUtil {
    public static String template(String str, Map<String, String> data) {
        for(String key:data.keySet()){
            str = str.replace("{"+key+"}", data.get(key));
        }
        return str;
    }

}
