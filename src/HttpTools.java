
import java.io.*;
import java.net.*;
import java.security.*;
import java.util.*;

public class HttpTools
{
	private HttpURLConnection httpclient;
	public static String UserAgent="Mozilla/5.0 BiliBili/5.51.1 (test@gmail.com)";
	public String GET(String url,String params) throws IOException{
		httpclient=(HttpURLConnection)new URL(url+"?"+params).openConnection();
		httpclient.addRequestProperty("User-Agent",UserAgent);
		InputStreamReader reader=new InputStreamReader(httpclient.getInputStream());
		int c;
		StringBuilder builder=new StringBuilder();
		while((c=reader.read())!=-1){
			builder.append((char)c);
		}
		reader.close();
		httpclient.disconnect();
		return builder.toString();
	}
		
	public String POST(String url,String params) throws IOException{
		httpclient=(HttpURLConnection)new URL(url).openConnection();
		httpclient.setDoInput(true);
		httpclient.setDoOutput(true);
		httpclient.addRequestProperty("User-Agent",UserAgent);
		OutputStream out=httpclient.getOutputStream();
		out.write(params.getBytes());
		out.flush();
		InputStreamReader reader=new InputStreamReader(httpclient.getInputStream());
		int c;
		StringBuilder builder=new StringBuilder();
		while((c=reader.read())!=-1){
			builder.append((char)c);
		}
		reader.close();
		out.close();
		httpclient.disconnect();
		return builder.toString();
	}
public String getSign(String params) throws NoSuchAlgorithmException{
	StringBuilder tmp=new StringBuilder();
	//值
	tmp.append(params+BiliParams.appSecret);
	//使用md5参数
	MessageDigest md=MessageDigest.getInstance("MD5");
	byte[] sign=md.digest(tmp.toString().getBytes());
	tmp=new StringBuilder();
	//拿到md5值
	for(int i=0;i<sign.length;i++){
		String hex=Integer.toHexString(sign[i]&0xff);
		if(hex.length()<2)
			tmp.append("0"+hex);
		else
			tmp.append(hex);
	}
	return tmp.toString();
}
}
