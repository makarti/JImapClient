package code.repository;

import java.util.List;

import code.core.Setting;
import code.core.StaticData;
import code.dto.Letter;

public class LetterRepository {
	
	private StaticData data = Setting.Instance();

	public List<Letter> GetInbox(String to)
	{
		return null;
	}
	
	public List<Letter> GetTrash(String to)
	{
		return null;
	}

	public Letter GetLetter(String id)
	{
		return null;
	}
	
	public void DeleteLetters(String[] listId)
	{
		
	}
	
	public void SetRead(String[] listId)
	{
	
	}
	
	public void SetUnRead(String[] listId)
	{
		
	}
}
