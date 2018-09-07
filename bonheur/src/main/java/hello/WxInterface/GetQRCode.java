package hello.WxInterface;

import hello.repository.UserRepository;
import net.sf.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class GetQRCode {

    private byte[] qrCode;

    public byte[] getQrCode() {
        return qrCode;
    }

    public void setQrCode(byte[] qrCode) {
        this.qrCode = qrCode;
    }

    private final GetAccessToken getAccessToken;

    private final UserRepository userRepository;

    @Autowired
    public GetQRCode(GetAccessToken getAccessToken, UserRepository userRepository) {
        this.getAccessToken = getAccessToken;
        this.userRepository = userRepository;
    }

    //获取头像图片字节流
    private byte[] getAvatar(String avatarUrl){
        CloseableHttpClient httpClient = HttpClients.createDefault();//Creates CloseableHttpClient instance with default configuration.
        HttpGet httpGet = new HttpGet(avatarUrl);
        CloseableHttpResponse response;
        byte[] temp;
        try {
            response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            temp = EntityUtils.toByteArray(entity);
            return temp;
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

    //获取二维码
    public byte[] getQRCOde(String scene) {
        String accessToken = getAccessToken.getAccessToken();
        String url = "https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=" + accessToken;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("scene", scene);//你二维码带的scene
        map.put("width", "430");
        JSONObject json = JSONObject.fromObject(map);
        System.out.println(scene);
        System.out.println(json);
        CloseableHttpClient httpClient = HttpClients.createDefault();//Creates CloseableHttpClient instance with default configuration.
        HttpPost httpPost = new HttpPost(url);
        CloseableHttpResponse response;
        byte[] temp;
        try {
            httpPost.addHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded");
            StringEntity stringEntity = new StringEntity(json.toString());
            httpPost.setEntity(stringEntity);
            response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            temp = EntityUtils.toByteArray(entity);
            return temp;
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

    //加上头像
    private byte[] addAvatar(String scene) {
        byte[] avatarImage = getAvatar(userRepository.getAvatarUrl(scene));
        InputStream is = new ByteArrayInputStream(Objects.requireNonNull(getQRCOde(scene)));
        InputStream avatar = new ByteArrayInputStream(Objects.requireNonNull(avatarImage));
        try {
            BufferedImage appletImg = ImageIO.read(is);
            BufferedImage poster = ImageIO.read(avatar);
            BufferedImage output = new BufferedImage(195, 195, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = output.createGraphics();
            output = g2.getDeviceConfiguration().createCompatibleImage(195, 195, Transparency.TRANSLUCENT);
            g2.dispose();
            g2 = output.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.fillRoundRect(0, 0, 195, 195, 720, 720);
            g2.setComposite(AlphaComposite.SrcIn);
            g2.drawImage(poster, 0, 0, 195, 195, null);
            g2.dispose();

            Graphics2D g2d = appletImg.createGraphics();
            // 设置抗锯齿的属性
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g2d.drawImage(output.getScaledInstance(output.getWidth(), output.getHeight(), Image.SCALE_SMOOTH), (appletImg.getWidth() - output.getWidth()) / 2, (appletImg.getHeight() - output.getHeight()) / 2, null);

            // 关闭资源
            g2d.dispose();
            OutputStream out = new ByteArrayOutputStream(1024);
            //生成新的二维码，覆盖原来的，此处为原小程序码路径，如需另为保存，请自定义路径
            ImageIO.write(appletImg, "png", out);
            return ((ByteArrayOutputStream) out).toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //加上背景
    public void change(String scene){
        BufferedImage buffImg;
        try {
            buffImg = ImageIO.read(new File("/var/games/poster.jpg"));
//            buffImg = ImageIO.read(new ByteArrayInputStream(Objects.requireNonNull(getPoster())));
            BufferedImage coverImg = ImageIO.read(new ByteArrayInputStream(Objects.requireNonNull(addAvatar(scene))));    //覆盖层

            BufferedImage output = new BufferedImage(430, 430, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = output.createGraphics();
            output = g2.getDeviceConfiguration().createCompatibleImage(430, 430, Transparency.TRANSLUCENT);
            g2.dispose();
            g2 = output.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.fillRoundRect(0, 0,430, 430, 720, 720);
            g2.setComposite(AlphaComposite.SrcIn);
            g2.drawImage(coverImg, 0, 0, 430, 430, null);
            g2.dispose();

            buffImg = coverImage(buffImg, output);
//            OutputStream out = new ByteArrayOutputStream(1024);
//            ImageIO.write(buffImg,"jpg",out);
            ImageIO.write(buffImg,"jpg", new File("/var/ftp/secret-posters/"+scene+".jpg"));
//            return ((ByteArrayOutputStream) out).toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        return null;
    }

    //绘图
    public static BufferedImage coverImage(BufferedImage baseBufferedImage, BufferedImage coverBufferedImage){

        // 创建Graphics2D对象，用在底图对象上绘图
        Graphics2D g2d = baseBufferedImage.createGraphics();

        // 绘制
        g2d.drawImage(coverBufferedImage, 228, 335, 170, 170, null);
        g2d.dispose();// 释放图形上下文使用的系统资源

        return baseBufferedImage;
    }

    //获取海报字节流
    public byte[] getPoster(){
        String url = "https://questiongame-1255384257.cos.ap-chengdu.myqcloud.com/secret-photos/poster.jpg";
        CloseableHttpClient httpClient = HttpClients.createDefault();//Creates CloseableHttpClient instance with default configuration.
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse response;
        byte[] temp;
        try {
            httpGet.addHeader(HTTP.CONTENT_TYPE, "image/jpeg");
            response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            temp = EntityUtils.toByteArray(entity);
            return temp;
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
