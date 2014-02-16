package code.core;

import java.util.List;

public class Setting {

	private static StaticData setting;
	
	private Setting()
	{
	}
		
	public static StaticData Instance()
	{
		if(setting == null)
		{
			setting = new StaticData();
			setting.refresh();
		}
		return setting;
			
	}
	
	public static void Update()
	{
		Instance().refresh();
	}
	
	public static String Joiner(List<String> list)
	{
		StringBuilder out = new StringBuilder();
		int count = list.size();
		if(count>0)
		{
			for (int i = 0; i<count; i++)
			{
				out.append('(');
				out.append(list.get(i));
				out.append(')');
				if(i!=count-1)
					out.append('|');
			}	
			return out.toString();
		}
		else 
			return null;
	}
}
