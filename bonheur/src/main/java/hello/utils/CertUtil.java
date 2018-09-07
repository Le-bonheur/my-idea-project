package hello.utils;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.ssl.SSLContexts;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.FileInputStream;
import java.security.KeyStore;

@SuppressWarnings("deprecation")
public class CertUtil {

    /**
     * 加载证书
     */
    public static SSLConnectionSocketFactory initCert(String mch_id) throws Exception {
        FileInputStream instream;
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        instream = new FileInputStream(new File("/Users/Lebonheur/Documents/Photos/work/问卷有奖/cert/apiclient_cert.p12"));
        keyStore.load(instream, mch_id.toCharArray());

        instream.close();

        SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, mch_id.toCharArray()).build();

        return new SSLConnectionSocketFactory(sslcontext, new String[]{"TLSv1"}, null, SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
    }
}
