package com.zz.bms.util.base.encrypt;


import com.alibaba.fastjson.JSON;
import com.zz.bms.util.base.data.StringUtil;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.HashMap;

/**
 * @author Administrator
 */
public class MacKit {
    private final static String DES = "DES";
    private static String IV_KEY = "ZZLL123OM_";
    private static String SECRETKEY= "QWERTTTQ";

    public static void main(String[] args) {
        //加密
        //String encrypt = encrypt("{userName:\"钟黎鸿\",password:\"123456\"}");


        String json = "{\"data\":[{\"id\":\"01\",\"name\":\"贷款\"},{\"id\":\"02\",\"name\":\"信用贷款\"},{\"id\":\"03\",\"name\":\"抵押担保贷款\"},{\"id\":\"04\",\"name\":\"质押担保贷款\"},{\"id\":\"05\",\"name\":\"保证贷款\"},{\"id\":\"06\",\"name\":\"住房按揭贷款\"},{\"id\":\"07\",\"name\":\"职工保证贷款\"},{\"id\":\"08\",\"name\":\"汽车消费贷款\"}],\"errcode\":0,\"success\":true}";
        json = "E8241533-4829-498E-A2A7-579F743BB7EB";
        HashMap<String,String> params = new HashMap<>();
        params.put("username","caochong");
        params.put("password","111111");
        json = JSON.toJSONString(params);
        System.out.println(json);
        String encrypt = encrypt(json);
        System.out.println(encrypt);

        //解密
        try {
        //   System.out.println(decrypt(encrypt));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 作者：zwq
     * 时间: 2017/3/17 11:15
     * 描述: 解密
     */
    public static String decrypt(String decryptString)
    {
        byte[] byteMi = new byte[0];
        try {
            byteMi = Base64.decode(decryptString);
            IvParameterSpec zeroIv = new IvParameterSpec(IV_KEY.getBytes());
            SecretKeySpec key = new SecretKeySpec(SECRETKEY.getBytes(), "DES");
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, key, zeroIv);
            byte decryptedData[] = cipher.doFinal(byteMi);

            return new String(decryptedData,"UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * EDS加密
     * @param originalStr
     * @return
     */
    public static String encrypt(String originalStr) {
        String result = null;
        byte[] tmpOriginalStr = null;
        try {
            if (!StringUtil.isEmpty(originalStr)) {
                tmpOriginalStr = originalStr.getBytes("UTF-8");
                SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
                DESKeySpec dks = new DESKeySpec(SECRETKEY.getBytes());
                SecretKey secretKey = keyFactory.generateSecret(dks);
                IvParameterSpec param = new IvParameterSpec(IV_KEY.getBytes());
                Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
                cipher.init(Cipher.ENCRYPT_MODE, secretKey,param);
                byte[] tmpEncypt = cipher.doFinal(tmpOriginalStr);
                if (tmpEncypt != null) {
                    result = Base64.encodeBytes(tmpEncypt);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }






}
