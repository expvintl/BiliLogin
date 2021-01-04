
import org.json.*;
import java.io.*;
public class Comment
{
	private String token;
	private HttpTools http;

	public Comment(String token){
		this.token=token;
		http=new HttpTools();
	}
	public boolean sendComment(int vid,String msg) throws IOException, Exception{
		ParamMap params=new ParamMap();
		params.add("access_key",token);
		params.add("type","1");
		params.add("oid",String.valueOf(vid));
		params.add("message",msg);
		params.add("plat","2");
		JSONObject obj=new JSONObject(http.POST("http://api.bilibili.com/x/v2/reply/add",params.toString()));
		if(obj.get("message").toString().equals("0"))
			return true;
			else return false;
	}
}
