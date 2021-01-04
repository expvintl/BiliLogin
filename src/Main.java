import java.util.*;
import java.io.*;
import org.json.*;

public class Main
{
	public static void main(String[] args)
	{
		Login l=new Login("12345678","testpassword");
		try
		{
			//访问密钥
			String key=l.login();
			User user=new User(key);
			System.out.println("UID:"+user.getUID());
			System.out.println("名称:"+user.getName());
			System.out.println("签名:"+user.getSign());
			System.out.println("硬币数量:"+user.getCoins());
			System.out.println("头像URL:"+user.getFaceUrl());
			System.out.println("性别:"+user.getSex());
			System.out.println("等级:"+user.getLevel());
			System.out.println("生日:"+user.getBirthday());
			System.out.println("封禁:"+user.isBanned());
			System.out.println("邮箱验证:"+user.isEmailVerified());
			System.out.println("手机验证:"+user.isPhoneVerified());
			User.Vip v=user.getVip();
			System.out.println("大会员类型:"+v.getType());
			System.out.println("大会员状态:"+v.getStatus());
			System.out.println("到期时间:"+v.getDueDate());
			System.out.println("名称颜色:"+v.getNicknameColor());
			Comment cm=new Comment(key);
			//System.out.println("评论发送状态:"+cm.sendComment(92621814,"Test Comment"));
		}
		catch (Exception e)
		{
			e.printStackTrace();
			}
	}
}
