package hello.WxInterface;

import net.sf.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class GetAccessToken implements CommandLineRunner {

    private String accessToken;

    private long period = 7200 * 1000;

    private void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    private long getPeriod() {
        return period;
    }

    private void setPeriod(long period) {
        this.period = period * 1000;
    }

    @Override
    public void run(String... args) throws Exception {
        autoGet();
    }

    public String autoGet(){
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                JSONObject json = startGettingAccessToken();
                setPeriod(Objects.requireNonNull(json).getLong("expires_in"));
                setAccessToken(json.getString("access_token"));
                System.out.println(getAccessToken());
            }
        }, 0, getPeriod());
        return getAccessToken();
    }

    public JSONObject startGettingAccessToken(){

        //小程序唯一标识   (在微信小程序管理后台获取)
        String wxspAppid = "wxd27778623447727b";
        //小程序的 app secret (在微信小程序管理后台获取)
        String wxspSecret = "81885240e51157415a3bcf0f70081c25";
        //授权（必填）
        String grant_type = "client_credential";
        //////////////// 1、向微信服务器 使用登录凭证 code 获取 session_key 和 openid ////////////////
        //请求参数
        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type="+grant_type+"&appid="+wxspAppid+"&secret="+wxspSecret;
        //发送请求
        CloseableHttpClient httpClient = HttpClients.createDefault();//Creates CloseableHttpClient instance with default configuration.
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse response;
        String temp;
        try {
            response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            temp = EntityUtils.toString(entity,"UTF-8");
            //解析相应内容（转换成json对象）
            JSONObject json = JSONObject.fromObject(temp);
            System.out.println(json);
            return json;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                httpClient.close();//释放资源
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
