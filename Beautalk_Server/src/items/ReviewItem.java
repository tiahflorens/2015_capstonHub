package items;
 
import java.util.ArrayList;

/**
 * Created by PeterYoon on 3/25/15.
 */
public class ReviewItem {

    private int price, idx, id;
    private String brandName, productName, nickName, memo, tag, title;
    private float rating;
    private byte[] pic;

    private ArrayList<ReviewItem> set, typeList, skinList;


    public ReviewItem(int id) {
        // ranking request ------>>
        this.id = id;
    }

    public ReviewItem(int uid, int idx) {
        // more review ------->>
        this.id = uid;
        this.idx = idx;
    }


    public ReviewItem(ArrayList<ReviewItem> typeList, ArrayList<ReviewItem> skinList) {
        // <<------ rank
        this.typeList = typeList;
        this.skinList = skinList;
    }

    public ReviewItem(ArrayList<ReviewItem> set, int idx) {
        // <<------- search result
        // <<------- initial result
        this.set = set;
        this.idx = idx;
    }

    public ReviewItem(String brandName, String productName, String memo, byte[] pic, float rating, String title, int uid, int price, String nickName) {
        // write review ------->>
        this.brandName = brandName;
        this.productName = productName;
        this.memo = memo;
        this.pic = pic;
        this.rating = rating;
        this.title = title;
        this.id = uid;
        this.price = price;
        this.nickName = nickName;
    }

    public ReviewItem(int price, String brandName, String productName, String nickName, String memo, String title, float rating, byte[] pic) {
        // <<--------- detail response
        this.price = price;
        this.brandName = brandName;
        this.productName = productName;
        this.nickName = nickName;
        this.memo = memo;
        this.title = title;
        this.rating = rating;
        this.pic = pic;
    }

    public ReviewItem(int id, int idx, String tag) {
        // search request ------->
        // more search request ------->
        this.id = id;
        this.idx = idx;
        this.tag = tag;
    }


    public ReviewItem(int id, String brandName, String productName, byte[] pic) {
        // <<------ review list response
        // <<------ search list response
        this.id = id;
        this.brandName = brandName;
        this.productName = productName;
        this.pic = pic;
    }


    public int getPrice() {
        return price;
    }

    public int getIdx() {
        return idx;
    }

    public int getId() {
        return id;
    }

    public String getBrandName() {
        return brandName;
    }

    public String getProductName() {
        return productName;
    }

    public String getNickName() {
        return nickName;
    }

    public String getMemo() {
        return memo;
    }

    public String getTag() {
        return tag;
    }

    public String getTitle() {
        return title;
    }

    public float getRating() {
        return rating;
    }

    public byte[] getPic() {
        return pic;
    }

    public ArrayList<ReviewItem> getSet() {
        return set;
    }

    public ArrayList<ReviewItem> getTypeList() {
        return typeList;
    }

    public ArrayList<ReviewItem> getSkinList() {
        return skinList;
    }
}
