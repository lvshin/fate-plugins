package cn.reinforce.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.reinforce.utils.entity.juhe.IP;
import cn.reinforce.utils.entity.juhe.JuheResponse;
import cn.reinforce.utils.entity.juhe.Sms;
import cn.reinforce.utils.entity.juhe.Weather;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.json.JSONException;

import com.google.gson.Gson;

/**
 * 聚合数据工具类
 *
 * @author hhFate
 * @create 2016-09-06
 * @since 1.0.0
 */
public class JHUtils {

    private static Logger LOG = Logger.getLogger(JHUtils.class);

    private JHUtils() {
    }

    /**
     * 短信发送
     *
     * @param mobile     手机号
     * @param code       验证码
     * @param timeout    超时时间，分钟
     * @param templeteId 短信模版id
     * @return 错误码
     */
    public static JuheResponse sendSms(String mobile, String code, int timeout, String key, int templeteId) {
        List<NameValuePair> pair = new ArrayList<>();
        pair.add(new BasicNameValuePair("mobile", mobile));
        pair.add(new BasicNameValuePair("tpl_id", Integer.toString(templeteId)));
        pair.add(new BasicNameValuePair("tpl_value", "#code#=" + code + "&#hour#=" + timeout));
        pair.add(new BasicNameValuePair("key", key));

        String result = HttpClientUtil.post("http://v.juhe.cn/sms/send", pair).getResult();
        Gson gson = new Gson();

        JuheResponse response = gson.fromJson(result, JuheResponse.class);

        Sms sms = gson.fromJson(gson.toJson(response.getResult()), Sms.class);

        response.setSms(sms);

        return response;
    }

    /**
     * 评论提醒通知
     *
     * @param mobile     手机号
     * @param title
     * @param content
     * @param key
     * @param templeteId 短信模版id
     * @return 错误码
     */
    public static Sms sendCommentSms(String mobile, String title, String content, String key, int templeteId) {
        List<NameValuePair> pair = new ArrayList<>();
        pair.add(new BasicNameValuePair("mobile", mobile));
        pair.add(new BasicNameValuePair("tpl_id", Integer.toString(templeteId)));
        pair.add(new BasicNameValuePair("tpl_value", "#title#=" + title + "&#content#=" + content));
        pair.add(new BasicNameValuePair("key", key));

        String result = HttpClientUtil.post("http://v.juhe.cn/sms/send", pair).getResult();
        Gson gson = new Gson();
        JuheResponse response = gson.fromJson(result, JuheResponse.class);

        Sms sms = gson.fromJson(gson.toJson(response.getResult()), Sms.class);

        response.setSms(sms);
        return sms;
    }

    /**
     * 友链申请提醒
     *
     * @param mobile
     * @param site
     * @param key
     * @param templeteId
     * @return
     */
    public static Sms sendFriendLinkSms(String mobile, String site, String key, int templeteId) {
        List<NameValuePair> pair = new ArrayList<>();
        pair.add(new BasicNameValuePair("mobile", mobile));
        pair.add(new BasicNameValuePair("tpl_id", Integer.toString(templeteId)));
        pair.add(new BasicNameValuePair("tpl_value", "#site#=" + site));
        pair.add(new BasicNameValuePair("key", key));

        String result = HttpClientUtil.post("http://v.juhe.cn/sms/send", pair).getResult();
        Gson gson = new Gson();
        JuheResponse response = gson.fromJson(result, JuheResponse.class);

        Sms sms = gson.fromJson(gson.toJson(response.getResult()), Sms.class);

        response.setSms(sms);
        return sms;
    }


    /**
     * 全国天气查询
     *
     * @param ip
     * @return
     * @throws JSONException
     */
    public static Weather weather(String ip) {
        List<NameValuePair> pair = new ArrayList<>();
        pair.add(new BasicNameValuePair("ip", ip));
        pair.add(new BasicNameValuePair("key", "f2f2be1d2581c633dbff0ed0099d4f6a"));
        String result = HttpClientUtil.post("http://v.juhe.cn/weather/ip", pair).getResult();
        Gson gson = new Gson();
        JuheResponse response = gson.fromJson(result, JuheResponse.class);

        Weather weather = gson.fromJson(gson.toJson(response.getResult()), Weather.class);

        response.setWeather(weather);
        return weather;
    }

    /**
     * 天气查询，weather.com
     *
     * @param ip
     * @return
     * @throws JSONException
     */
    public static Map<String, Object> weather2(String ip) {
        Map<String, Object> map = new HashMap<>();

        String city = ip(ip).getIp().getArea();
        city = "苏州市";
        try {
            city = URLEncoder.encode(city, "utf-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        String result = HttpClientUtil.get("http://op.juhe.cn/onebox/weather/query?cityname=" + city + "&key=e6189b8525a1355dbacd219618359a46").getResult();
        System.out.println(result);
        Gson gson = GsonUtil.getGson();
        JuheResponse response = gson.fromJson(result, JuheResponse.class);

        System.out.println(response.getResult());

        return map;
    }

    public static JuheResponse ip(String ip) {
        String result = HttpClientUtil.get("http://apis.juhe.cn/ip/ip2addr?ip=" + ip + "&key=acd6918ff5a3b87ed1119c0df7d61408").getResult();
        Gson gson = GsonUtil.getGson();
        JuheResponse response = gson.fromJson(result, JuheResponse.class);

        IP ip1 = gson.fromJson(gson.toJson(response.getResult()), IP.class);
        System.out.println(ip1);
        response.setIp(ip1);
        return response;
    }

    public static void main(String[] args) {
        weather2("221.6.89.54");
    }
}
