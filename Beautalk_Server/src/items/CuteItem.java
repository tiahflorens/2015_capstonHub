package items;

public class CuteItem {
    private int uid,bid;
    private boolean isCute;

    public CuteItem(int uid, int bid, boolean isCute) {
        this.uid = uid;
        this.bid = bid;
        this.isCute = isCute;
    }

    public int getUid() {
        return uid;
    }

    public int getBid() {
        return bid;
    }

    public boolean isCute() {
        return isCute;
    }
}
