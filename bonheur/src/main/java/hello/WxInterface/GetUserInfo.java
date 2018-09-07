package hello.WxInterface;

import net.sf.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class GetUserInfo {

    private String openId;

    public String getOpenId() {
        return openId;
    }

    private void setOpenId(String openId) {
        this.openId = openId;
    }

    public JSONObject decodeUserInfo(String encryptedData, String iv, String code){
        Map<String, Object> map = new HashMap<>();
        try {
            String result = AesUtil.decrypt(encryptedData, getOpenIdByCode(code).getString("session_key"), iv, "UTF-8");
            System.out.println(result);
            if (null != result && result.length() > 0) {
                map.put("status", 1);
                map.put("msg", "解密成功");
                JSONObject userInfoJSON = JSONObject.fromObject(result);
                Map<String, Object> userInfo = new HashMap<>();
                userInfo.put("openId", userInfoJSON.get("openId"));
                //保存openId
                setOpenId(userInfoJSON.getString("openId"));
                userInfo.put("nickName", userInfoJSON.get("nickName"));
                userInfo.put("gender", userInfoJSON.get("gender"));
                userInfo.put("city", userInfoJSON.get("city"));
                userInfo.put("province", userInfoJSON.get("province"));
                userInfo.put("country", userInfoJSON.get("country"));
                userInfo.put("avatarUrl", userInfoJSON.get("avatarUrl"));
//                userInfo.put("unionId", userInfoJSON.get("unionId"));
                map.put("userInfo", userInfo);
                return JSONObject.fromObject(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        map.put("status", 0);
        map.put("msg", "解密失败");
        return JSONObject.fromObject(map);
    }

    public JSONObject getOpenIdByCode(String code){
        System.out.println(code);
        Map<String, Object> map = new HashMap<>();
        if (code == null || code.length() == 0) {
            map.put("status", 0);
            map.put("msg", "code 不能为空");
            return JSONObject.fromObject(map);
        }
        //小程序唯一标识   (在微信小程序管理后台获取)
        String wxspAppid = "wxd27778623447727b";
        //小程序的 app secret (在微信小程序管理后台获取)
        String wxspSecret = "81885240e51157415a3bcf0f70081c25";
        //授权（必填）
        String grant_type = "authorization_code";
        //////////////// 1、向微信服务器 使用登录凭证 code 获取 session_key 和 openid ////////////////
        //请求参数
        String params = "appid=" + wxspAppid + "&secret=" + wxspSecret + "&js_code=" + code + "&grant_type=" + grant_type;
        //发送请求
        CloseableHttpClient httpClient = HttpClients.createDefault();//Creates CloseableHttpClient instance with default configuration.
        HttpGet httpGet = new HttpGet("https://api.weixin.qq.com/sns/jscode2session?"+params+"");
        CloseableHttpResponse response;
        String temp;
        try{
            response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            temp = EntityUtils.toString(entity,"UTF-8");
            //解析相应内容（转换成json对象）
            JSONObject json = JSONObject.fromObject(temp);
            //获取会话密钥（session_key）
            String session_key = json.getString("session_key");
            //用户的唯一标识（openid）
            String openid = json.getString("openid");
            map.put("openId", openid);
            map.put("session_key", session_key);
        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                httpClient.close();//释放资源
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        map.put("status", 0);
        map.put("msg", "解密失败");
        return JSONObject.fromObject(map);
    }
}
