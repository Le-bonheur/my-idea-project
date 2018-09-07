package hello.utils;

import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

public class SignUtils {
    /**
     * @param characterEncoding 编码格式 utf-8
     * */
    public static String creatSign(String characterEncoding, SortedMap<Object, Object> parameters) {
        StringBuilder sb = new StringBuilder();
        Set es = parameters.entrySet();
        for (Object e : es) {
            Map.Entry entry = (Map.Entry) e;
            String k = (String) entry.getKey();
            Object v = entry.getValue();
            if (null != v && !"".equals(v)
                    && !"sign".equals(k) && !"key".equals(k)) {
                sb.append(k + "=" + v + "&");
            }
        }
        //wxconfig.apikey。这句代码是获取商务号设置的api秘钥。这里不方便贴出来，
        //复制签名代码的人，需要把该常量改成自己商务号的key值。原因是Api规定了签名必须加上自己的key值哦
        sb.append("key=" + "swLke2rKeQl98eoYcx1p9MbVzsTrd63s");
        String sign = MD5Util.MD5Encode(sb.toString(), characterEncoding).toUpperCase();
        System.out.println(sign);
        return sign;
    }
}
