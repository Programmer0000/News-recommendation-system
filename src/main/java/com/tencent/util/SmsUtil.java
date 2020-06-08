package com.tencent.util;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;


@Component
public class SmsUtil {
    //产品名称：云通信短信API产品，开发者无需替换
    static final String product="Dysmsapi";
    //产品域名，开发者无需替换
    static final String domain="dysmsapi.aliyuncs.com";
    //区域id,短信API的值为：cn-hangzhou
    static final String RegionId="cn-hangzhou";
    static final String AccessKeyID="LTAI4Ft7cGH2YmF7ZTWsDPEw";
    static final String AccessKeySecret="yZQqNjeWqpxpCoDBox8Zb7WMPX0whe";
    //短信签名
    static final String signName = "TC新闻网";
    //短信模板code
    static final String templateCode = "SMS_187261427";
    /*
    patam是验证码，随机生成
     */
    public void SendSms(String mobile, String code) {
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");

        DefaultProfile profile = DefaultProfile.getProfile(RegionId, AccessKeyID, AccessKeySecret);
        IAcsClient client = new DefaultAcsClient(profile);
        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain(domain);
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");
        request.putQueryParameter("RegionId", "cn-hangzhou");
        request.putQueryParameter("PhoneNumbers", mobile);
        request.putQueryParameter("SignName", "TC新闻网");
        request.putQueryParameter("TemplateCode", templateCode);
        request.putQueryParameter("TemplateParam", "{\"code\":\""+code+"\"}");

        try {
            CommonResponse response = client.getCommonResponse(request);
            System.out.println(response.getData());
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }
}
