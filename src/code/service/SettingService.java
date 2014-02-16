package code.service;

import code.common.IRepository;
import code.core.Setting;
import code.core.StaticData;

public class SettingService {
	private StaticData data = Setting.Instance();
	private IRepository repo = data.getRepository();
	
	public int getInterval()
	{
		return data.getInterval();
	}
	
	public void savePeriod(String period)
	{
		int per = Integer.parseInt(period);
		repo.setInterval(per);
		data.refresh();
	}
	
}
