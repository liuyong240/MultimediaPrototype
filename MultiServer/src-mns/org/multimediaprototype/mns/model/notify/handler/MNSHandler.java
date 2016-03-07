
package org.multimediaprototype.mns.model.notify.handler;

import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.protocol.HttpRequestHandler;
import org.apache.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.*;


/**
 * core class for processing /notifications request
 */
/**
 * MTS视频转码完成后的回调接口实现，参考文档https://help.aliyun.com/knowledge_detail/6675119.html
 */
public class MNSHandler {
    //public Logger logger = Logger.getLogger(HttpRequestHandler.class);
    private static org.apache.logging.log4j.Logger logger = LogManager.getLogger(HttpRequestHandler.class);
    public MNSHandler() {
        super();
    }

    public String safeGetElementContent(Element element, String tag) {
        NodeList nl = element.getElementsByTagName(tag);
        if (nl != null && nl.getLength() > 0) {
            return nl.item(0).getTextContent();
        } else {
            logger.warn("get " + tag + " from xml fail");
            return "";
        }
    }


    /**
     * check if this request comes from MNS Server
     *
     * @param method,  http method
     * @param uri,     http uri
     * @param headers, http headers
     * @param cert,    cert url
     * @return true if verify pass
     */
    public Boolean authenticate(String method, String uri, Map<String, String> headers, String cert) {
        logger.debug("authenticate method is:" + method);
        logger.debug("authenticate uri is:" + uri);
        logger.debug("authenticate headers is:" + headers);
        logger.debug("authenticate cert is:" + cert);
        String str2sign = getSignStr(method, uri, headers);
        logger.debug("authenticate str2sign is:" + str2sign);
        //System.out.println(str2sign);
        String signature = headers.get("authorization");
        logger.debug("authenticate signature is: " + signature);
        byte[] decodedSign = org.apache.commons.codec.binary.Base64.decodeBase64(signature.getBytes());
        //get cert, and verify this request with this cert
        try {
            //String cert = "http://mnstest.oss-cn-hangzhou.aliyuncs.com/x509_public_certificate.pem";
            URL url = new URL(cert);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            DataInputStream in = new DataInputStream(conn.getInputStream());
            CertificateFactory cf = CertificateFactory.getInstance("X.509");

            Certificate c = cf.generateCertificate(in);
            PublicKey pk = c.getPublicKey();
            logger.debug("authenticate pk is:" + pk);

            java.security.Signature signetcheck = java.security.Signature.getInstance("SHA1withRSA");
            signetcheck.initVerify(pk);
            signetcheck.update(str2sign.getBytes());
            Boolean res = signetcheck.verify(decodedSign);
            logger.debug("authenticate res is: " + res);
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            logger.debug("authenticate fail Exception is:" + e.getMessage());
            return false;
        }
    }

    public String safeGetHeader(Map<String, String> headers, String name) {
        if (headers.containsKey(name))
            return headers.get(name);
        else
            return "";
    }


    /**
     * build string for sign
     *
     * @param method,  http method
     * @param uri,     http uri
     * @param headers, http headers
     * @return String fro sign
     */
    public String getSignStr(String method, String uri, Map<String, String> headers) {
        StringBuilder sb = new StringBuilder();
        sb.append(method);
        sb.append("\n");
        sb.append(safeGetHeader(headers, "content-md5"));
        sb.append("\n");
        sb.append(safeGetHeader(headers, "content-type"));
        sb.append("\n");
        sb.append(safeGetHeader(headers, "date"));
        sb.append("\n");

        List<String> tmp = new ArrayList<String>();
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            if (entry.getKey().startsWith("x-mns-"))
                tmp.add(entry.getKey() + ":" + entry.getValue());
        }
        Collections.sort(tmp);

        for (String kv : tmp) {
            sb.append(kv);
            sb.append("\n");
        }

        sb.append(uri);
        return sb.toString();
    }

    public Map<String, String> getHeadersInfo(HttpServletRequest request) {
        Map<String, String> map = new HashMap<String, String>();
        Enumeration headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            String value = request.getHeader(key);
            map.put(key, value);
        }
        return map;
    }

    public Element parserBodyContent(HttpServletRequest request) throws IOException {
        InputStream content = request.getInputStream();
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        Element notify = null;
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document document = db.parse(content);
            NodeList nl = document.getElementsByTagName("Notification");
            if (nl == null || nl.getLength() == 0) {
                System.out.println("xml tag error");
                logger.warn("xml tag error");
            }
            notify = (Element) nl.item(0);
            return notify;
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            logger.warn("xml parser fail! " + e.getMessage());
            return null;
        } catch (SAXException e) {
            e.printStackTrace();
            logger.warn("xml parser fail! " + e.getMessage());
            return null;
        }
    }
}


