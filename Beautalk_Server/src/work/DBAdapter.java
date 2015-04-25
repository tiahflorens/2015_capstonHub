package work;

import items.UserItem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
			int c = p.executeUpdate();
			System.out.println("insert user : " + c);
			if (c > 0)
				return true;

			else
				return false;

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
	public boolean updateUserInfo(String passwd , String email){
		String q = "update users set passwd= ? where email=?";
		try {
			PreparedStatement p = conn.prepareStatement(q);
			p.setString(1, passwd);
			p.setString(2,email);
			System.out.println(p.toString());
			return p.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	public boolean updateUserInfo(int uid, int type, int shape){
		String q = "update users set type=?, shape=? where uid=?";
		try {
			PreparedStatement p = conn.prepareStatement(q);
			p.setInt(1, type);
			p.setInt(2,shape);
			p.setInt(3,uid);
			System.out.println(p.toString());
			return p.execute();
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

			 UserItem user = new UserItem(rs.getInt(1),
			 rs.getString(2),rs.getString(3), rs.getString(4), rs.getLong(5),
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

}
