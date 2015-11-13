package org.ligson.ssodemo.auth;

import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ligson on 2015/11/13.
 * RSA软加密key生成还原工具,硬key不可以使用
 */
public class KeyUtils {
    private static final String ALG_NAME = "RSA";

    /***
     * 生成RSA密钥对
     *
     * @return [pubKey:公钥base64,priKey:私钥base64]或者null
     */
    public static Map<String, String> generateKey() {
        Map<String, String> map = new HashMap<String, String>();
        KeyPairGenerator generator;
        try {
            generator = KeyPairGenerator.getInstance(ALG_NAME);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        KeyPair keyPair = generator.generateKeyPair();
        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();
        String priKeyBase64 = Base64.getEncoder().encodeToString(privateKey.getEncoded());
        String pubKeyBase64 = Base64.getEncoder().encodeToString(publicKey.getEncoded());
        map.put("pubKey", pubKeyBase64);
        map.put("priKey", priKeyBase64);
        System.out.println("私钥:" + priKeyBase64);
        System.out.println("公钥:" + pubKeyBase64);
        return map;
    }

    /***
     * pkcs8形式的私钥还原
     *
     * @param priKey 私钥的base64字符串
     * @return 私钥对象或者null
     */
    public static PrivateKey decodePrivateKey(String priKey) {
        byte[] buf = Base64.getDecoder().decode(priKey);
        try {
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buf);
            KeyFactory factory = KeyFactory.getInstance(ALG_NAME);
            return factory.generatePrivate(keySpec);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /***
     * x509证书公钥还原
     *
     * @param pubKey 公钥的base64字符串
     * @return 公钥对象或者null
     */
    public static PublicKey decodePublicKey(String pubKey) {
        byte[] buf = Base64.getDecoder().decode(pubKey);
        X509EncodedKeySpec encodedKeySpec = new X509EncodedKeySpec(buf);
        try {
            KeyFactory factory = KeyFactory.getInstance(ALG_NAME);
            return factory.generatePublic(encodedKeySpec);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static void main(String[] args) throws Exception {
        Map<String, String> map = generateKey();

        assert map != null;
        String pubKey = map.get("pubKey");
        String priKey = map.get("priKey");
        System.out.println(pubKey);
        System.out.println(priKey);

        PublicKey publicKey = decodePublicKey(pubKey);
        PrivateKey privateKey = decodePrivateKey(priKey);

        assert publicKey != null;
        assert privateKey != null;
        String pubKey2 = Base64.getEncoder().encodeToString(publicKey.getEncoded());
        String priKey2 = Base64.getEncoder().encodeToString(privateKey.getEncoded());

        System.out.println(pubKey.equals(pubKey2));
        System.out.println(priKey.equals(priKey2));
    }
}
