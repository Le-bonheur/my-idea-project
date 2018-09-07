package hello.WxInterface;

import hello.entity.Transfers;
import hello.repository.UserRepository;
import hello.utils.*;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class WxPayInterface {

    private final UserRepository userRepository;

    private Transfers transfers = new Transfers();
    // 构造签名的map
    private SortedMap<Object, Object> parameters = new TreeMap<>();

    @Autowired
    public WxPayInterface(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * 微信提现（企业付款）
     */
    @Transactional
    public Map<String, String> wxWithdraw(String openId, String money) {
        Double old = userRepository.getMoneyByOpenId(openId);
        userRepository.withdraw(openId, Math.round(old * 100 - Double.valueOf(money))/100.0);
        String ip = "121.35.183.171";
//        String doctorId = request.getParameter("doctorId");   && StringUtils.isNotBlank(doctorId)
        if (StringUtils.isNotBlank(money) && StringUtils.isNotBlank(ip) && StringUtils.isNotBlank(openId)) {
            // 参数组
            String appid = "wxd27778623447727b";
            String mch_id = "1505380041";
            String nonce_str = RandCharsUtils.getRandomString(16);
            //是否校验用户姓名 NO_CHECK：不校验真实姓名  FORCE_CHECK：强校验真实姓名
            String checkName = "NO_CHECK";
            //等待确认转账金额,ip,openid的来源
            Integer amount = Integer.valueOf(money);
            String partner_trade_no = new SimpleDateFormat("GGyyyyMMddHHmmssSSzz").format(Calendar.getInstance().getTime());    //UUID.randomUUID().toString().substring(0, 30);

            //描述
            String desc = "提现" + Double.valueOf(amount.toString()) / 100 + "元";
            // 参数：开始生成第一次签名
            parameters.put("mch_appid", appid);
            parameters.put("mchid", mch_id);
            parameters.put("partner_trade_no", partner_trade_no);
            parameters.put("nonce_str", nonce_str);
            parameters.put("openid", openId);
            parameters.put("check_name", checkName);
            parameters.put("amount", amount);
            parameters.put("spbill_create_ip", ip);
            parameters.put("desc", desc);
            System.out.println(parameters);
            String sign = WxSignUtils.createSign("UTF-8", parameters);
            System.out.println(sign);
            transfers.setAmount(amount);
            transfers.setCheck_name(checkName);
            transfers.setDesc(desc);
            transfers.setMch_appid(appid);
            transfers.setMchid(mch_id);
            transfers.setNonce_str(nonce_str);
            transfers.setOpenid(openId);
            transfers.setPartner_trade_no(partner_trade_no);
            transfers.setSign(sign);
            transfers.setSpbill_create_ip(ip);
            String xmlInfo = HttpXmlUtils.transferXml(transfers);
            try {
                CloseableHttpResponse response = HttpUtil.Post(xmlInfo, mch_id, true);
                String transfersXml = EntityUtils.toString(response.getEntity(), "utf-8");
                Map<String, String> transferMap = HttpXmlUtils.xmlToMap(transfersXml);
                if (transferMap.size() > 0) {
                    if (transferMap.get("result_code").equals("SUCCESS") && transferMap.get("return_code").equals("SUCCESS")) {
                        //成功需要进行的逻辑操作，
                        return transferMap;
                    }
                }
                System.out.println("成功");
                return transferMap;
            } catch (Exception ignored) {

            }
        } else {
            System.out.println("失败");
        }
        return null;
    }


}
