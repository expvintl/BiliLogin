
import org.json.*;
import java.security.*;
import java.io.*;
import java.util.*;
import java.text.*;
public class User
{
	private String token;
	private HttpTools http;
	//缓存用户json对象
	JSONObject dataTmp=null;
	
	public User(String token){
		this.token=token;
		http=new HttpTools();
		
	}
	
	public JSONObject getInfo() throws NoSuchAlgorithmException, IOException,Exception{
		ParamMap params=new ParamMap();
		params.add("access_key",token);
		params.add("appkey",BiliParams.appkey);
		params.add("ts",String.valueOf(System.currentTimeMillis()/1000));
		String tmp=http.getSign(params.toString());
		params.add("sign",tmp);
		JSONObject obj=new JSONObject(http.GET("http://app.bilibili.com/x/v2/account/myinfo",params.toString()));
		if(obj.get("message").equals("0")){
		dataTmp=(JSONObject) obj.get("data");
		}else{
			//如果消息不是0，那么一定是出问题了
			throw new Error(obj.get("message").toString());
		}
		return dataTmp;
		}
		
	public int getUID() throws Exception{
			//如果不是空的就读取缓存
			if(dataTmp!=null){
				return dataTmp.get("mid");
			}
			return getInfo().get("mid");
		}
	public String getName() throws Exception{
		if(dataTmp!=null){
			return dataTmp.get("name").toString();
		}
		return getInfo().get("name").toString();
	}
	public String getSign() throws Exception{
		if(dataTmp!=null){
			return dataTmp.get("sign").toString();
		}
		return getInfo().get("sign").toString();
	}
	public float getCoins() throws Exception{
		if(dataTmp!=null){
			return Float.parseFloat(dataTmp.get("coins").toString());
		}
		return Float.parseFloat(getInfo().get("coins").toString());
	}
	public String getBirthday() throws Exception{
		if(dataTmp!=null){
			return dataTmp.get("birthday").toString();
		}
		return getInfo().get("birthday").toString();
	}
	public String getFaceUrl() throws Exception{
		if(dataTmp!=null){
			return dataTmp.get("face").toString();
		}
		return getInfo().get("face").toString();
	}
	public String getSex() throws Exception{
		if(dataTmp!=null){
			switch(dataTmp.get("sex").toString()){
			case "0":
				return "保密";
			case "1":
				return "男";
			case "2":
				return "女";
			}
			
		}
		switch(getInfo().get("sex").toString()){
		case "0":
			return "保密";
		case "1":
			return "男";
		case "2":
			return "女";
	}
		return "未知性别";
	}
	public int getLevel() throws Exception{
		if(dataTmp!=null){
			return dataTmp.get("level");
		}
		return getInfo().get("level");
	}
	public int getRank() throws Exception{
		if(dataTmp!=null){
			return dataTmp.get("rank");
		}
		return getInfo().get("rank");
	}
	public boolean isBanned() throws Exception{
		if(dataTmp!=null){
		return (dataTmp.get("silence").toString().equals("1"))?true:false;
		}
		return (getInfo().get("silence").toString().equals("1"))?true:false;
	}
	public boolean isEmailVerified() throws Exception{
		if(dataTmp!=null){
			return (dataTmp.get("email_status").toString().equals("1"))?true:false;
			}
		return (getInfo().get("email_status").toString().equals("1"))?true:false;
	}
	public boolean isPhoneVerified() throws Exception{
		if(dataTmp!=null){
			return (dataTmp.get("tel_status").toString().equals("1"))?true:false;
		}
		return (getInfo().get("tel_status").toString().equals("1"))?true:false;
	}
	public Vip getVip() throws Exception{
		if(dataTmp!=null){
			JSONObject obj= (JSONObject) dataTmp.get("vip");
			Vip vip=new Vip();
			vip.type=obj.get("type");
			vip.status=obj.get("status");
			vip.vip_pay_type=obj.get("vip_pay_type");
			vip.due_date=obj.get("due_date");
			vip.theme_type=obj.get("theme_type");
			vip.avatar_subscript=obj.get("avatar_subscript");
			vip.nickname_color=obj.get("nickname_color").toString();
			return vip;
		}
		JSONObject obj= (JSONObject) getInfo().get("vip");
		Vip vip=new Vip();
		vip.type=obj.get("type");
		vip.status=obj.get("status");
		vip.vip_pay_type=obj.get("vip_pay_type");
		vip.due_date=obj.get("due_date");
		vip.theme_type=obj.get("theme_type");
		vip.avatar_subscript=obj.get("avatar_subscript");
		vip.nickname_color=obj.get("nickname_color").toString();
		return vip;
	}
	//Vip类
	class Vip{
		public int type;
		public int status;
		public long due_date;
		public int vip_pay_type,theme_type;
		public int avatar_subscript;
		public String nickname_color;
		
		public String getType(){
			switch (type){
				case 0:
					return "无";
				case 1:
					return "月度";
				case 2:
					return "年度";
			}
			return "未知";
		}
	
	public String getStatus(){
		switch (status){
			case 0:
				return "未开通";
			case 1:
				return "已开通";
		}
		return "未知";
	}
	public String getDueDate(){
		return new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss",Locale.CHINA).format(new Date(due_date));
	}
	public String getNicknameColor(){
		return nickname_color;
	}
	}
}
