public class ParamMap
{
	private StringBuilder builder;
	public ParamMap(){
		builder=new StringBuilder();
	}
	public void add(String K,String V){
		//如果是空的
		if(builder.toString().isEmpty()){
			builder.append(K+"="+V);
		}else{
			builder.append("&"+K+"="+V);
			}
	}
	public String toString(){
		return builder.toString();
	}
}
