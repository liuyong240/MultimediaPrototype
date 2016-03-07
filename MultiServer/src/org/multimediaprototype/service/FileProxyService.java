package org.multimediaprototype.service;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.ObjectMetadata;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.multimediaprototype.base.AliyunClientManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.activation.MimetypesFileTypeMap;
import javax.annotation.PostConstruct;
import javax.print.attribute.standard.Media;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Ref;
import java.util.*;

/**
 * Created by dx.yang on 15/12/1.
 */

/**
 * OSS文件对外服务代理
 *
 * <p>
 *     OSS如果设为私有, 读取文件需要先获取文件的URL, 该URL会在一定时间内过期.
 *     如果对外服务, 暴露给用户的URL不应该过期, 所以在这里设置一个文件代理服务器,
 *     用户获取到的是一个不会过期的URL, 该URL的访问都交给文件代理服务来响应,
 *     文件代理服务会解析该URL, 然后获取相应的OSS文件URL, 然后建立两个流, 将文件返回给用户
 * </p>
 *
 */
@Service
public class FileProxyService {

    private Logger logger = LogManager.getLogger(this.getClass());

    @Autowired
    private AliyunClientManager aliyunClientManager;

    private OSSClient client;

    /**
     * 初始化OSS客户端
     */
    @PostConstruct
    public void setupClient() {
        client = aliyunClientManager.getOssClient();
    }

    /**
     * 为OSS文件创建对外暴露的URL
     *
     * @param ossLocation 文件所在节点
     * @param ossBucket 文件所在bucket
     * @param ossFile 文件key
     * @return url
     */
    public String createProxyURL(String ossLocation, String ossBucket, String ossFile) {
        return ossLocation + "/" + ossBucket + "/" + ossFile;
    }


    /**
     * 代理读取oss文件
     *
     * <p>把请求头直接传给oss处理，并将oss响应头中的字段返回。可以满足range请求。</p>
     *
     * @param location
     * @param bucket
     * @param key
     * @param request
     * @return ResponseEntity
     */
    public ResponseEntity get(String location, String bucket, String key, HttpServletRequest request) {

        GetObjectRequest req = new GetObjectRequest(bucket, key);
        Map<String, String> headers = new HashMap<>();
        headers.put("Range", request.getHeader("Range"));
        //headers.put("If-Modified-Since", request.getHeader("If-Modified-Since"));
        //headers.put("If-Unmodified-Since", request.getHeader("If-Unmodified-Since"));
        //headers.put("If-Match", request.getHeader("If-Match"));
        //headers.put("If-None-Match", request.getHeader("If-None-Match"));
        req.getHeaders().clear();
        req.getHeaders().putAll(headers);
        OSSObject obj = client.getObject(req);

        ObjectMetadata metadata = obj.getObjectMetadata();
        Map rawMetaData = metadata.getRawMetadata();

        HttpHeaders responseHeaders = new HttpHeaders();
        try {
            String etag = metadata.getETag();
            responseHeaders.set("Etag", etag);
            responseHeaders.set("Content-Type", metadata.getContentType());
            responseHeaders.setConnection(rawMetaData.get("Connection").toString());
            responseHeaders.set("Accept-Ranges", rawMetaData.get("Accept-Ranges").toString());
            responseHeaders.set("Content-Disposition", rawMetaData.get("Content-Disposition").toString());
            //todo: encoding报错？？？
//            responseHeaders.set("Content-Encoding", rawMetaData.get("Content-Encoding").toString());
            responseHeaders.setContentLength(metadata.getContentLength());
        } catch (Exception e) {
            e.printStackTrace();
        }

        HttpStatus code = HttpStatus.OK;
        if (headers.get("Range") != null) {
            responseHeaders.set("Content-Range", rawMetaData.get("Content-Range").toString());
            code = HttpStatus.PARTIAL_CONTENT;
        } else {

        }
        InputStreamResource inputStreamResource = new InputStreamResource(obj.getObjectContent());

        // 生成http返回结果
        return new ResponseEntity(inputStreamResource, responseHeaders, code);

    }

    /**
     * 代理读取oss文件
     *
     * @param location
     * @param bucket
     * @param key
     * @return response entity
     */
    @Deprecated
    public ResponseEntity get(String location, String bucket, String key) {
        GetObjectRequest req = new GetObjectRequest(bucket, key);

        // 获取oss文件
        OSSObject obj = client.getObject(req);

        // 获取oss文件的meta data
        ObjectMetadata metadata = obj.getObjectMetadata();

        // oss文件的长度
        long contentLength = metadata.getContentLength();

        // oss文件的类型
        String[] contentType = metadata.getContentType().split("/");
        MediaType mt = new MediaType(contentType[0], contentType[1]);//, Charset.forName("UTF-8"));

        // 根据文件扩展名 获取文件mime type
        String filename = obj.getKey();

        String[] filenameWithoutPath = filename.split("/");
        filename = filenameWithoutPath[filenameWithoutPath.length - 1];

        String extname = filename;
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot >-1) && (dot < (filename.length() - 1))) {
                extname = filename.substring(dot + 1);
            }
        }

        switch (extname) {
            case "mp4":
                mt = new MediaType("video", "mp4");
                break;
            case "flv":
                mt = new MediaType("video", "x-flv");
                break;
            default:
                break;
        }


        // 生成响应头
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(mt);
        responseHeaders.setContentLength(contentLength);
        responseHeaders.set("Content-Disposition", "inline; filename=\"" + filename + "\"");

        // 生成读取oss文件流
        InputStreamResource inputStreamResource = new InputStreamResource(obj.getObjectContent());

        // 生成http返回结果
        ResponseEntity res = new ResponseEntity(inputStreamResource, responseHeaders, HttpStatus.PARTIAL_CONTENT);

        return res;
    }


    /**
     * 根据对外暴露的url, 代理获取oss文件
     * @param proxyURL
     * @param request
     * @return
     * @throws Exception
     */
    public ResponseEntity getByProxyURL(String proxyURL, HttpServletRequest request) throws Exception {
        String[] ossFields = proxyURL.split("/");

        if (ossFields.length < 3) {
            throw new Exception("url格式异常");
        }

        String location = ossFields[0];
        String bucket = ossFields[1];

        String[] file = Arrays.copyOfRange(ossFields, 2, ossFields.length);
        String filename = StringUtils.join(file, "/");

        if (request != null) {
            return get(location, bucket, filename, request);
        } else {
            return get(location, bucket, filename);
        }
    }

    /**
     * 根据对外暴露的url, 代理获取oss文件
     * @param proxyURL
     * @return response entity
     * @throws Exception 解析url失败
     */
    @Deprecated
    public ResponseEntity getByProxyURL(String proxyURL) throws Exception {
        return getByProxyURL(proxyURL, null);
    }

}
