package hello.utils;

import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

public class WxSignUtils {
    /**
     * 微信支付签名算法sign
     *
     * @param characterEncoding
     * @param parameters
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static String createSign(String characterEncoding, SortedMap<Object, Object> parameters) {
        StringBuilder sb = new StringBuilder();
        Set es = parameters.entrySet();//所有参与传参的参数按照accsii排序（升序）
        for (Object e : es) {
            Map.Entry entry = (Map.Entry) e;
            String k = (String) entry.getKey();
            Object v = entry.getValue();
            if (null != v && !"".equals(v)
                    && !"sign".equals(k) && !"key".equals(k)) {
                sb.append(k).append("=").append(v).append("&");
            }
        }
        sb.append("key=" + "swLke2rKeQl98eoYcx1p9MbVzsTrd63s");  //密钥
        return MD5Util.MD5Encode(sb.toString(), characterEncoding).toUpperCase();

    }
}