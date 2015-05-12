package work;

import items.BeauTalkItem;
import items.CommentItem;

import java.util.ArrayList;

import com.google.gson.Gson;

public class BeauTalk {

	private DBAdapter db;
	private Gson gson;

	public BeauTalk() {
		db = new DBAdapter();
		gson = new Gson();

	}

	public String work(String div, String raw) {
		
		if(div.equalsIgnoreCase(Tags.BEAUTY_COMMENT)){
			CommentItem item = gson.fromJson(raw, CommentItem.class);
			db.insertComment(item);
			return null;
		}
		
		BeauTalkItem beauty = gson.fromJson(raw, BeauTalkItem.class);
		
		switch (div) {
		case Tags.BEAUTY_WRITE:
			db.insertBeautalk(beauty);
			return null;
		case Tags.BEAUTY_CUTE:
			cute(beauty);
			return null;
		case Tags.BEAUTY_READALL:
			return readAll(beauty.getId());
			
		case Tags.BEAUTY_READ :
			return read(beauty.getId());
			
		default:
			return null;
		}

	}
	
	public void cute(BeauTalkItem item){
		if(item.isCute())
			db.insertCute(item.getIdx(), item.getId());
		else
			db.deleteCute(item.getIdx(), item.getId());
	}
	
	public String read(int _idx){
		
		BeauTalkItem item = db.getBeautyById(_idx);
		return gson.toJson(item);
		
		
	}

	public String readAll(int _idx) {
		ArrayList<BeauTalkItem> list = db.getBeauty(_idx);
		int idx = 0;
		if (!list.isEmpty())
			idx = list.get(list.size() - 1).getId();

		BeauTalkItem beauty = new BeauTalkItem(idx, list);

		return gson.toJson(beauty);

	}

	 

}
