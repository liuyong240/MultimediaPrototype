package org.multimediaprototype.oss.controller;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.multimediaprototype.oss.controller.OSSCallbackController.ResponseInfo;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

/**
 * Created by haihong.xiahh on 2015/12/24.
 */

/**
 * OSS 文件上传完成之后的回调接口，参考文档 https://help.aliyun.com/document_detail/oss/api-reference/object/Callback.html?spm=5176.docoss/practice/app_server/callback_server.2.5.DV9Zrn
 */
public class OSSCallbackHandler {
    public Logger log = LogManager.getLogger(OSSCallbackHandler.class);


    private String bucket;
    private String body;
    private String object;
    private Long userId;
    private boolean transcode = false;


    public String getBucket() {
        return bucket;
    }

    public String getBody() {
        return body;
    }

    public String getObject() {
        return object;
    }

    public Long getUserId() {
        return userId;
    }

    public boolean isTranscode() {
        return transcode;
    }

    public ResponseInfo doCheck(HttpServletRequest request) {
        // get public key url
        String pubKeyUrlBase64 = request.getHeader("x-oss-pub-key-url");
        String pubKeyUrl = new String(Base64.decodeBase64(pubKeyUrlBase64));
        log.debug(String.format("pub key url: %s", pubKeyUrl));
        if (!pubKeyUrl.startsWith("http://gosspublic.alicdn.com/") && !pubKeyUrl.startsWith("https://gosspublic.alicdn.com/"))
        {
            log.error("pub key addr must be oss addrss");
            return new ResponseInfo(400, "pub key addr must be oss addrss");
        }

        // get public key
        String publicKey = getPublicKey(pubKeyUrl);
        if (StringUtils.isEmpty(publicKey)) {
            return new ResponseInfo(400, "get public key failed");
        }
        log.debug("get publish key success");

        // get authorization
        String signatureBase64 = request.getHeader("Authorization");
        byte[] signature = Base64.decodeBase64(signatureBase64);

        // compose authorization thing
        String signToStr = getStrToSign(request);
        if (StringUtils.isEmpty(signToStr)) {
            log.error("get str to sign failed");
            return new ResponseInfo(400, "get str to sign failed");
        }
        log.debug(String.format("sign str: %s", signToStr));

        // parse body
        if (!parseBody()) {
            log.error("body invalid");
            return new ResponseInfo(400, "body invalid");
        }

        // check signature
        if (checkSignature(publicKey, signToStr, signature)) {
            log.debug("check signature success");
        } else {
            log.error("signature check failed");
            return new ResponseInfo(403, "signature check failed");
        }
        return new ResponseInfo(200, "");
    }


    private String getPublicKey(String pubKeyUrl) {
        URL url;
        InputStream in = null;
        try {
            url = new URL(pubKeyUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            log.debug("url request in success");
            in = conn.getInputStream();
            BufferedReader br= new BufferedReader(new InputStreamReader(in));
            String readLine;
            String separator = System.getProperty("line.separator");
            StringBuilder sb= new StringBuilder();
            while((readLine= br.readLine())!=null){
                sb.append(readLine).append(separator);
            }
            String result = sb.toString();
            result = result.replace("-----BEGIN PUBLIC KEY-----", "");
            result = result.replace("-----END PUBLIC KEY-----", "");
            return result;
        } catch (IOException e) {
            log.error(e.getMessage());
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }
        return null;
    }

    private boolean checkSignature(String publicKey, String strToSign, byte[] sign) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            byte[] encodedKey = Base64.decodeBase64(publicKey);
            PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));
            java.security.Signature signature = java.security.Signature.getInstance("MD5withRSA");
            signature.initVerify(pubKey);
            signature.update(strToSign.getBytes());
            return signature.verify(sign);

        } catch (InvalidKeySpecException | NoSuchAlgorithmException | SignatureException | InvalidKeyException e) {
            log.error(e.getMessage());
        }
        return false;
    }


    private String getStrToSign(HttpServletRequest request) {
        try {
            this.body = GetPostBody(request.getInputStream(), Integer.parseInt(request.getHeader("content-length")));
        } catch (IOException e) {
            log.error(e.getMessage());
            return null;
        }
        String queryString = request.getQueryString();
        String uri = request.getRequestURI();
        String decodeUri;
        try {
            decodeUri = java.net.URLDecoder.decode(uri, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage());
            return null;
        }
        String authStr = decodeUri;
        if (queryString != null && !queryString.equals("")) {
            authStr += "?" + queryString;
        }
        authStr += "\n" + this.body;
        return authStr;
    }

    public String GetPostBody(InputStream is, int contentLen) {
        if (contentLen > 0) {
            int readLen = 0;
            int readLengthThisTime = 0;
            byte[] message = new byte[contentLen];
            try {
                while (readLen != contentLen) {
                    readLengthThisTime = is.read(message, readLen, contentLen - readLen);
                    if (readLengthThisTime == -1) {// Should not happen.
                        break;
                    }
                    readLen += readLengthThisTime;
                }
                return new String(message);
            } catch (IOException e) {
            }
        }
        return "";
    }

    private boolean parseBody() {
        String[] parts = this.body.split("&");
        for (String part : parts) {
            String[] subParts = part.split("=");
            String key = subParts[0];
            String value = subParts[1];
            if ("bucket".equals(key)) {
                this.bucket = value;
            } else if ("object".equals(key)) {
                this.object = URLDecoder.decode(value);
            } else if ("userId".equals(key)) {
                this.userId = Long.valueOf(value);
            } else if ("transcode".equals(key)) {
                this.transcode = Long.valueOf(value) == 1;
            }

        }
        return (!StringUtils.isEmpty(this.bucket) && !StringUtils.isEmpty(this.object) && this.userId != 0);
    }

}
