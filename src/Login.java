
import java.io.*;
import java.util.*;
import org.json.*;
import java.security.*;
import java.security.spec.*;
import javax.crypto.*;
import java.net.*;

public class Login
{
	private String account,password;
	private HttpTools http;
	
	public Login(String account,String password){
		this.account=account;
		this.password=password;
		http=new HttpTools();
	}
	//获得盐和RSA公钥
	public HashMap getRSAKey() throws IOException, JSONException{
		ParamMap param=new ParamMap();
		//传入参数
		param.add("appkey",BiliParams.appkey);
		param.add("sign",BiliParams.sign);
		System.out.println(param.toString());
		//请求盐和rsa公钥
		String reault=http.POST("https://passport.bilibili.com/api/oauth2/getKey",param.toString());
		JSONObject obj=new JSONObject(reault);
		//解析json对象
		obj=(JSONObject)obj.get("data");
		HashMap map=new HashMap();
		//盐
		map.put("hash",obj.get("hash").toString());
		//公钥
		map.put("key",obj.get("key").toString());
		return map;
	}
	//密码加密处理
	public String handlePassword(String password) throws IOException, JSONException, Exception{
		HashMap map=getRSAKey();
		//拿到公钥并剔除无用数据
		String key=map.get("key").toString().substring(26).replaceAll("\n","");
		//剔除结尾
		key=key.substring(0,key.length()-24);
		//解码公钥
		byte[] key1=Base64.getDecoder().decode(key.getBytes());
		//加盐后的密码
		String saltPasswd=map.get("hash").toString()+password;
		//初始化公钥
		PublicKey pubkey=KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(key1));
		//通过获得的公钥将密码加密
		Cipher cipher=Cipher.getInstance("RSA/ECB/PKCS1Padding");
		//初始化模式
		cipher.init(cipher.ENCRYPT_MODE,pubkey);
		//加密后再进行base64编码
		return Base64.getEncoder().encodeToString(cipher.doFinal(saltPasswd.getBytes()));
	}
	//登录账户
	public String login() throws Exception{
		ParamMap params=new ParamMap();
		String passwd=handlePassword(password);
		params.add("appkey",BiliParams.appkey);
		params.add("build","5442100");
		params.add("mobi_app","android");
		//对加密后的密码进行URL编码
		params.add("password",URLEncoder.encode(passwd));
		params.add("platform","android");
		params.add("ts",String.valueOf(System.currentTimeMillis()/1000));
		params.add("username",account);
		String tmp=http.getSign(params.toString());
		params.add("sign",tmp);
		JSONObject obj=new JSONObject(http.POST("https://passport.bilibili.com/api/v3/oauth2/login",params.toString()));
		obj=(JSONObject) obj.get("data");
		System.out.println(obj);
		obj=(JSONObject) obj.get("token_info");
		return obj.get("access_token").toString();
	}
}
