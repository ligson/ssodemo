package org.ligson.ssodemo.auth;

import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ligson on 2015/11/13.
 */
public class KeyUtils {
    public static Map<String, String> generateKey() {
        Map<String, String> map = new HashMap<String, String>();
        KeyPairGenerator generator = null;
        try {
            generator = KeyPairGenerator.getInstance("RSA");
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

    public static PrivateKey decodePrivateKey(String priKey) {
        byte[] buf = Base64.getDecoder().decode(priKey);
        try {
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buf);
            KeyFactory factory = KeyFactory.getInstance("RSA");
            PrivateKey privateKey = factory.generatePrivate(keySpec);
            return privateKey;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static PublicKey decodePublicKey(String pubKey){
        byte[] buf = Base64.getDecoder().decode(pubKey);
        X509EncodedKeySpec encodedKeySpec = new X509EncodedKeySpec(buf);
        try {
            KeyFactory factory = KeyFactory.getInstance("RSA");
            PublicKey publicKey = factory.generatePublic(encodedKeySpec);
            return publicKey;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static void main(String[] args) throws Exception {
        Map<String,String> map = generateKey();
        String pubKey = map.get("pubKey");
        String priKey = map.get("priKey");
        System.out.println(pubKey);
        System.out.println(priKey);

        PublicKey publicKey = decodePublicKey(pubKey);
        PrivateKey privateKey = decodePrivateKey(priKey);

        String pubKey2 = Base64.getEncoder().encodeToString(publicKey.getEncoded());
        String priKey2 = Base64.getEncoder().encodeToString(privateKey.getEncoded());

        System.out.println(pubKey.equals(pubKey2));
        System.out.println(priKey.equals(priKey2));
    }
}
