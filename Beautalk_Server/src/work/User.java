package work;

import items.UserItem;

import com.google.gson.Gson;

public class User {

	private DBAdapter db;
	private Gson gson;

	public User() {
		db = new DBAdapter();
		gson = new Gson();
	}

	public String work(String div, String raw) {

		UserItem user = gson.fromJson(raw, UserItem.class);
		UserItem result;
		switch (div) {
		case Tags.SIGN_UP:
			result = signUp(user);
			break;
		case Tags.SIGN_IN:
			result = signIn(user);
			break;
		case Tags.USER_UPDATE:
			result = update(user);
			break;
		case Tags.USER_EXAMINE:
			result = examine(user);
			break;
		case Tags.LOSTANDFIND:
			result = findPasswd(user.getEmail());
			break;
		case "confirm" :
			System.out.println("confirm : " + raw);
			
			return null;
		default:
			result = new UserItem("switch error", false);
		}

		db.close();
		return gson.toJson(result);
	}

	public UserItem findPasswd(String email) {

		System.out.println("find pass wd");
		if (!db.isEmailExist(email))
			return new UserItem("invalid email!", false);
		else {
			new FindPasswd(db, email).sendEmail();
			return new UserItem("We've just sent you a email", false);
		}

	}

	public UserItem examine(UserItem user) {
		// JNI 사용
		int type = 4;
		int shape = 1;

		if (type < 0)// type>0 , type instanceof Integer , not null
			return new UserItem("JNI examine failed 1 ", false);

		if (!db.updateUserInfo(user.getUid(), type, shape))
			return new UserItem(type, shape, "done", true);
		else
			return new UserItem("JNI examine failed 2 ", false);

	}

	public UserItem update(UserItem user) {

		if (db.updateUserInfo(user))
			return new UserItem("done", true);
		else
			return new UserItem("update failed ", false);

	}

	public UserItem signIn(UserItem user) {

		if (db.isMatch(user.getEmail(), user.getPasswd()))
			return db.getUserInfo(user.getEmail());
		else
			return new UserItem("input email and password are incorrect", false);

	}

	public UserItem signUp(UserItem user) {

		if (db.isEmailExist(user.getEmail()))
			return new UserItem("duplicated email", false);

		if (db.insertUser(user))
			return new UserItem("done", true);

		return new UserItem("already exist email", false);

	}
}
