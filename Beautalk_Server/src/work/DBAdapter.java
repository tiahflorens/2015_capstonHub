package work;

import items.BeauTalkItem;
import items.CommentItem;
import items.ReviewItem;
import items.UserItem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DBAdapter {
	Statement stm, stm2;
	String DB_URL = "jdbc:mysql://127.0.0.1:3306/test?useUnicode=true& useUnicode=true&characterEncoding=euc_kr";
	String DB_USER = "root";
	String DB_PASSWORD = "zmfkdns";
	Connection conn;

	// 테이블은 id + json

	public DBAdapter() {

		try {
			Class.forName("org.gjt.mm.mysql.Driver");
			conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

			stm = conn.createStatement();

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void close() {
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public boolean isEmailExist(String email) {

		String q = "select exists(select 1 from users where email='" + email
				+ "');";
		System.out.println(q);
		try {
			ResultSet rs = stm.executeQuery(q);
			rs.next();

			return rs.getInt(1) == 0 ? false : true;

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	public boolean isNickExist(String nick) {
		String q = "select exists(select 1 from users where nick='" + nick
				+ "');";
		System.out.println(q);
		try {
			ResultSet rs = stm.executeQuery(q);
			rs.next();
			return rs.getInt(1) == 0 ? false : true;

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	public boolean isMatch(String email, String passwd) {
		ResultSet rs;

		String q = "select passwd from users where email='" + email + "';";
		System.out.println(q);

		String passwd2 = null;
		try {
			rs = stm.executeQuery(q);

			if (rs.next())
				passwd2 = rs.getString(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		System.out.println(passwd + " == " + passwd2);
		if (passwd2 == null)
			return false;
		if (passwd.equalsIgnoreCase(passwd2))
			return true;
		else
			return false;
	}

	public void updateDeviceId(String email, String devId) {
		String q = "update users set did='" + devId + "' where email='" + email
				+ "';";
		try {
			int res = stm.executeUpdate(q);
			System.out.println("update dev id result : " + res);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public boolean insertUser(UserItem user) {
		// String q2 = "insert into users values(?,?,?,?,?)";
		String q2 = "insert into users (email,passwd,nick,birth,pic) values(?,?,?,?,?)";

		try {
			PreparedStatement p = conn.prepareStatement(q2);
			p.setString(1, user.getEmail());
			p.setString(2, user.getPasswd());
			p.setString(3, user.getNickname());
			p.setLong(4, user.getBirth());
			p.setBytes(5, user.getPics());
			p.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean updateUserInfo(UserItem user) {
		// String q = "update users set passwd='" +user.getPasswd() +
		// "' birth='"+user.getBirth()
		// +"' nick='"+user.getNickname()+"' pic='"+user.getPics()+"' where email='"+
		// user.getEmail()+ "';";

		String qq = "update users set passwd=?, nick=? ,pic=? where email=?";

		try {
			PreparedStatement p = conn.prepareStatement(qq);
			p.setString(1, user.getPasswd());
			p.setString(2, user.getNickname());
			p.setBytes(3, user.getPics());
			return p.execute();

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}

	}

	public boolean updateUserInfo(String passwd, String email) {
		String q = "update users set passwd= ? where email=?";
		try {
			PreparedStatement p = conn.prepareStatement(q);
			p.setString(1, passwd);
			p.setString(2, email);
			System.out.println(p.toString());
			return p.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean updateUserInfo(int uid, int type, int shape) {
		String q = "update users set type=?, shape=? where uid=?";
		try {
			PreparedStatement p = conn.prepareStatement(q);
			p.setInt(1, type);
			p.setInt(2, shape);
			p.setInt(3, uid);
			System.out.println(p.toString());
			p.execute();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public UserItem getUserInfo(String email) {
		String q = "select * from users where email='" + email + "'";

		System.out.println(q);
		try {
			PreparedStatement p = conn.prepareStatement(q);
			ResultSet rs = p.executeQuery();
			rs.next();

			UserItem user = new UserItem(rs.getInt(1), rs.getString(2),
					rs.getString(3), rs.getString(4), rs.getLong(5),
					rs.getBytes(6), rs.getInt(7), rs.getInt(8));

			return user;
		} catch (SQLException e) {
			e.printStackTrace();
			return new UserItem("get user info failed ", false);

		}
	}

	public void removeUser(String email) {
		String q = "delete from users where email='" + email + "';";
		System.out.println(q);
		try {
			stm.executeUpdate(q);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public boolean saveReview(ReviewItem rev) {

		System.out.println("db.saveReview");

		int pid = isProductExist(rev.getProductName());

		System.out.println("1  isProductExist ? : " + pid);

		if (pid < 0) {
			insertProduct(rev.getProductName(), rev.getBrandName(),
					rev.getCategory());
			pid = isProductExist(rev.getProductName());
			System.out.println("2  isProductExist ? : " + pid);
		}

		int feature = getFeature(rev.getId());
		float rating = getRating(pid, feature);
		updateRating(pid, feature, rating + rev.getRating());

		return insertReview(rev, pid);

	}

	public int getFeature(int uid) {
		System.out.println("db.getFeature");
		String q = "select type from users where uid=?";
		try {
			PreparedStatement p = conn.prepareStatement(q);
			p.setInt(1, uid);
			System.out.println(p.toString());
			ResultSet rs = p.executeQuery();
			if (rs.next())
				return rs.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return -1;

	}

	public void updateRating(int pid, int feature, float rating) {
		String q = "update products set " + getKeyword(feature)
				+ "=? where pid=?";

		System.out.println("db.updateRating");
		try {
			PreparedStatement p = conn.prepareStatement(q);
			p.setFloat(1, rating);
			p.setInt(2, pid);
			System.out.println(p.toString());

			p.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public float getRating(int pid, int feature) {
		System.out.println("db.getRating");

		String q = "select " + getKeyword(feature)
				+ " from products where pid=?";
		try {
			PreparedStatement p = conn.prepareStatement(q);
			p.setInt(1, pid);
			System.out.println(p.toString());
			ResultSet rs = p.executeQuery();
			if (rs.next())
				return rs.getFloat(1);
			else
				return -1;
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
	}

	public boolean insertReview(ReviewItem rev, int pid) {
		System.out.println("db.insertReview");
		String q = "insert into reviews (pid,uid,rating,title,memo,price, nick,pic) values(?,?,?,?,?,?,?,?)";
		try {
			PreparedStatement p = conn.prepareStatement(q);
			p.setInt(1, pid);
			p.setInt(2, rev.getId());
			p.setFloat(3, rev.getRating());
			p.setString(4, rev.getTitle());
			p.setString(5, rev.getMemo());
			p.setInt(6, rev.getPrice());
			p.setString(7, rev.getNickName());
			p.setBytes(8, rev.getPic());

			p.execute();

			return true;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}

	}

	public int isProductExist(String pname) {
		String q = "select pid from products where productname=?";

		try {
			PreparedStatement p = conn.prepareStatement(q);
			p.setString(1, pname);

			ResultSet rs = p.executeQuery();

			if (rs.next())
				return rs.getInt(1);
			else
				return -1;

		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
	}

	public void insertProduct(String pname, String bname, int category) {
		String q = "insert into products (brandname,productname,category) values(?,?,?)";
		try {
			PreparedStatement p = conn.prepareStatement(q);
			p.setString(1, bname);
			p.setString(2, pname);
			p.setInt(3, category);

			p.execute();
			// return true;
		} catch (SQLException e) {

			e.printStackTrace();
			// return false;
		}

	}

	public ArrayList<ReviewItem> getReveiwList(int uid, int idx) {
		String q = " select r.rid, p.brandname , p.productname , r.pic "
				+ "from reviews r ,products p"
				+ " where (select type from users where uid=r.uid)=(select type from users where uid=?)"
				+ "and r.rid>= ?;";
		System.out.println("db.getReviewList");

		try {
			PreparedStatement p = conn.prepareStatement(q);
			p.setInt(1, uid);
			p.setInt(2, idx);
			ArrayList<ReviewItem> list = new ArrayList<ReviewItem>();
			System.out.println(p.toString());
			ResultSet r = p.executeQuery();

			while (r.next()) {
				if (list.size() > 20)
					break;

				list.add(new ReviewItem(r.getInt(1), r.getString(2), r
						.getString(3), r.getBytes(4)));
			}

			return list;
		} catch (SQLException e) {

			e.printStackTrace();
			return null;
		}

	}

	public ArrayList<ReviewItem> getReveiwListByTag(int uid, String tag) {

		String q = " select r.rid, p.brandname , p.productname , r.pic "
				+ "from reviews r ,products p"
				+ " where (select type from users where uid=r.uid)=(select type from users where uid=?) and"
				+ "p.productname=? or p.brandname=?;";
		try {
			PreparedStatement p = conn.prepareStatement(q);
			p.setInt(1, uid);
			p.setString(2, tag);
			p.setString(3, tag);
			ArrayList<ReviewItem> list = new ArrayList<ReviewItem>();
			System.out.println("db.getReviewListByTag");
			System.out.println(p.toString());
			ResultSet r = p.executeQuery();
			while (r.next())
				list.add(new ReviewItem(r.getInt(1), r.getString(2), r
						.getString(3), r.getBytes(4)));
			return list;
		} catch (SQLException e) {

			e.printStackTrace();
			return null;
		}
	}

	public ReviewItem getReviewItemByRid(int rid) {
		String q = " select r.price , p.brandname , p.productname , r.nick , r.memo , r.title, r.rating , r.pic "
				+ "from reviews r ,products p " + "where r.rid=?";

		try {
			PreparedStatement p = conn.prepareStatement(q);
			p.setInt(1, rid);
			ResultSet rs = p.executeQuery();
			if (rs.next()) {
				ReviewItem rev = new ReviewItem(rs.getInt(1), rs.getString(2),
						rs.getString(3), rs.getString(4), rs.getString(5),
						rs.getString(6), rs.getFloat(7), rs.getBytes(8));

				return rev;
			}
			return null;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

	}

	public String getKeyword(int feature) {
		switch (feature) {
		case Tags.AC:
			return "ratingA";
		case Tags.AD:
			return "ratingB";
		case Tags.BC:
			return "ratingC";
		case Tags.BD:
			return "ratingD";

		default:
			return null;
		}
	}

	public ArrayList<ReviewItem> getRankList(int uid, int category) {

		int feature = getFeature(uid);
		String keyword = getKeyword(feature);
		String q = "select m.pid,m.brandname , m.productname "
				+ "from (select pid,brandname,productname, " + keyword
				+ " from products where " + keyword
				+ ">-1 and category = ?) as m order by " + keyword + " desc;";

		try {
			PreparedStatement p = conn.prepareStatement(q);
			p.setInt(1, category);
			System.out.println(q);
			ResultSet rs = p.executeQuery();
			ArrayList<ReviewItem> list = new ArrayList<>();
			while (rs.next()) {
				if (list.size() > 3)
					break;

				list.add(new ReviewItem(rs.getInt(1), rs.getString(2), rs
						.getString(3), null));

			}

			return list;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	public void insertBeautalk(BeauTalkItem item) {
		System.out.println("db.insertBeautalk");

		String q = "insert into beautalk (nickname,title,memo,pic) values(?,?,?,?)";

		try {
			PreparedStatement p = conn.prepareStatement(q);
			p.setString(1, item.getNickname());
			p.setString(2, item.getTitle());
			p.setString(3, item.getMemo());
			System.out.println(p.toString());
			p.setBytes(4, item.getPic());

			p.execute();

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public ArrayList<BeauTalkItem> getBeauty(int idx) {
		System.out.println("db.getBeauty");
		String q = "select * from beautalk where bid>=?";
		ArrayList<BeauTalkItem> list = new ArrayList<>();
		try {
			PreparedStatement p = conn.prepareStatement(q);
			p.setInt(1, idx);
			ResultSet rs = p.executeQuery();

			while (rs.next()) {
				list.add(new BeauTalkItem(rs.getInt(1), rs.getBytes(5), rs
						.getString(3), rs.getString(2), rs.getString(4), 추천인가져오기(rs.getInt(1))));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	public BeauTalkItem getBeautyById(int bid){
		System.out.println("db.getBeautyById");
		String q ="select * from beautalk where bid =?";
		try {
			PreparedStatement p = conn.prepareStatement(q);
			p.setInt(1, bid);
			ResultSet rs = p.executeQuery();

			if(rs.next()) {
				return new BeauTalkItem(rs.getInt(1), rs.getBytes(5), rs
						.getString(3), rs.getString(2), rs.getString(4), 추천인가져오기(rs.getInt(1)),getCommentsById(bid) );
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	public ArrayList<Integer> 추천인가져오기(int bid){
		System.out.println("db.추천인가져오기");
		String q = "select uid from cutes where bid=?";
		ArrayList<Integer> list= new ArrayList<>();
		try {
			PreparedStatement p = conn.prepareStatement(q);
			p.setInt(1, bid);
			ResultSet rs = p.executeQuery();
			while(rs.next()){
				list.add(rs.getInt(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	public void insertComment(CommentItem item){
		
		String q = "insert into comments (bid,uid,comment) values(?,?,?)";
		System.out.println("db.insertcomment");
 		try {
			PreparedStatement p = conn.prepareStatement(q);
			p.setInt(1, item.getBid());
			p.setInt(2, item.getUid());
			p.setString(3, item.getComment());
			System.out.println(p.toString());
			p.execute();
			
		} catch (SQLException e) {
 			e.printStackTrace();
		}
	}
	
	public ArrayList<CommentItem> getCommentsById(int bid){
		System.out.println("db.getCommentById");
		String q ="select u.nick , u.pic , c.comment from users u, comments c where c.bid=? and c.uid=u.uid";
		ArrayList<CommentItem> list = new ArrayList<CommentItem>();
		try {
			PreparedStatement p = conn.prepareStatement(q);
			p.setInt(1, bid);
			
			ResultSet rs = p.executeQuery();
			while(rs.next()){
				list.add(new CommentItem(rs.getString(1), rs.getString(3), rs.getBytes(2)));
			}
			
		} catch (SQLException e) {
 			e.printStackTrace();
		}
		return list;
		
	}
	
	public void insertCute(int bid, int uid){
		System.out.println("db.insertCute");
		String q = "insert into cutes values(?,?)";
		try {
			PreparedStatement p = conn.prepareStatement(q);
			p.setInt(1, bid);
			p.setInt(2, uid);
			System.out.println(p.toString());
			p.execute();
		} catch (SQLException e) {
 			e.printStackTrace();
		}
	}
	public void deleteCute(int bid, int uid){
		System.out.println("db.deletecute");
		String q = "delete from cutes where bid=? and uid=?";
		try {
			PreparedStatement p = conn.prepareStatement(q);
			p.setInt(1, bid);
			p.setInt(2, uid);
			System.out.println(p.toString());
			p.execute();
		} catch (SQLException e) {
 			e.printStackTrace();
		}
	}
	
	public ArrayList<String> getDid(){
		ArrayList<String> list = new ArrayList<String>();
		String q= "select did from users";
		try {
			PreparedStatement p = conn.prepareStatement(q);
			ResultSet rs = p.executeQuery();
			while(rs.next())
				list.add(rs.getString(1));
		} catch (SQLException e) {
 			e.printStackTrace();
		}
		return list;
	}
	
	
}
