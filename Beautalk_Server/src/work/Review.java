package work;

import items.ReviewItem;

import java.util.ArrayList;

import com.google.gson.Gson;

public class Review {
	private DBAdapter db;
	private Gson gson;

	public Review() {
		db = new DBAdapter();
		gson = new Gson();
	}

	public String work(String division, String raw) {

		ReviewItem result, rev;
		rev = gson.fromJson(raw, ReviewItem.class);

		switch (division) {
		case Tags.REVIEW_READ_SET:
			result = getNormalSet(rev);
			break;
		case Tags.REVIEW_READ_SEARCH:
			result = getSearchResult(rev);
			break;
		default:
			result = new ReviewItem(2);
		}

		return gson.toJson(result);
	}
	public ReviewItem getSearchResult(ReviewItem rev){
		System.out.println("getSearchResult " + rev.getTag() );
		ArrayList<ReviewItem> list = db.getReveiwListByTag(rev.getId(), rev.getTag());
		
		return new ReviewItem(list, list.get(list.size() - 1).getId());
		
		
	}

	public ReviewItem getNormalSet(ReviewItem rev) {
		System.out.println("getNormalSet");
		ArrayList<ReviewItem> list = db.getReveiwList(rev.getId());

		// if(db!=null)

		return new ReviewItem(list, list.get(list.size() - 1).getId());
	}

	public ReviewItem getRanks(ReviewItem rev) {
		System.out.println("getRanks");

		return null;
	}
}
