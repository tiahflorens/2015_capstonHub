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
		case Tags.REVIEW_READ_RANK:
			result = getRanks(rev);
			break;
		case Tags.REVIEW_WRITE:
			result = saveReview(rev);
			break;
		case Tags.REVIEW_READ_SINGLE:
			result = getDetails(rev);
			break;
		default:
			result = new ReviewItem(2);
		}

		return gson.toJson(result);
	}

	public ReviewItem getDetails(ReviewItem rev) {
		return db.getReviewItemByRid(rev.getId());
	}

	public ReviewItem saveReview(ReviewItem rev) {

		if (db.saveReview(rev))
			return new ReviewItem(true);
		else

			return new ReviewItem(false);

	}

	public ReviewItem getSearchResult(ReviewItem rev) {
		System.out.println("getSearchResult " + rev.getTag());
		ArrayList<ReviewItem> list = db.getReveiwListByTag(rev.getId(),
				rev.getTag());
		int index;
		if (list.isEmpty())
			index = 0;
		else
			index = list.get(list.size() - 1).getId();
		
		return new ReviewItem(list, index);

	}

	public ReviewItem getNormalSet(ReviewItem rev) {
		System.out.println("getNormalSet");
		ArrayList<ReviewItem> list = db
				.getReveiwList(rev.getId(), rev.getIdx());
		int index;
		if (list.isEmpty())
			index = 0;
		else
			index = list.get(list.size() - 1).getId();
		return new ReviewItem(list, index);
	}

	public ReviewItem getRanks(ReviewItem rev) {

		System.out.println("getRanks");

		return new ReviewItem(db.getRankList(rev.getId(), Tags.SKINTONE),
				db.getRankList(rev.getId(), Tags.SKINTYPE));

	}
}
