package org.multimediaprototype.test.multimedia.sts;

import org.junit.Test;
import org.multimediaprototype.test.SprintJunitTest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by haihong.xiahh on 2015/12/23.
 */
public class OSSCallbackTest extends SprintJunitTest {

    @Test
    public void postOSSCallback() {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        String url = "http://127.0.0.1:8080/api/oss/callback";
        try {
            URL realUrl = new URL(url);
            URLConnection conn = realUrl.openConnection();
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("user-agent", "ehttp-client/0.0.1");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Authorization", "kcF2v+B+3JJ3jUn3a7uDTbOEKkTmG1tYALJQ9h2vj2H/EKBGI74t0lMvFTxfkzQQ+ewhAT2obqAfSsQy8SZ/Tg==");
            conn.setRequestProperty("x-oss-pub-key-url", "aHR0cDovL2dvc3NwdWJsaWMuYWxpY2RuLmNvbS9jYWxsYmFja19wdWJfa2V5X3YxLnBlbQ==");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            out.print("bucket=multimedia-input&object=test.mp3&userId=30");
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += "/n" + line;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("发送POST请求出现异常！" + e);
            e.printStackTrace();
        } finally { // 使用finally块来关闭输出流、输入流
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
