package work;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.MulticastResult;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;

public class Send {
	private final String key = "AIzaSyCbKnN2mZfRTYm0JmnfSGWJajpqTIlvmrI";
	private final boolean DELAY_WHILE_IDLE = true;
	private final int TIME_TO_LIVE = 3;
	private final int RETRY = 3;

	private Sender gcmSender; // GCM Sender
	private Result gcmResult; // GCM Result(단일 전송)
	MulticastResult gcmResults;
	private DBAdapter db;

	private String brand,memo, due;
 
	public Send(String due,String brand,String memo) {
		System.out.println(brand +" , " + memo +" , " + due);

		gcmSender = new Sender(key);
		db = new DBAdapter();
		this.brand =brand;
		this.memo = memo;
		this.due = due(due);
	}
	public String due(String due){
		if(due == null)
			return System.currentTimeMillis()+"";
		
		Calendar c = Calendar.getInstance();
		int y = Integer.parseInt(due.substring(0,4));
		int m = Integer.parseInt(due.substring(5, 7));
		int d = Integer.parseInt(due.substring(8,10));
		c.set(y, m, d);
		return c.getTimeInMillis()+"";
	}

	public void sendMessage() {
		ArrayList<String> list = db.getDid();

 		String COLLAPSE_KEY = String.valueOf(Math.random() % 100 + 1);
		Message gcmMessage = new Message.Builder().collapseKey(COLLAPSE_KEY)
				.delayWhileIdle(DELAY_WHILE_IDLE).timeToLive(TIME_TO_LIVE)
				.addData("brand", brand).addData("due", due).addData("memo", memo).build();
		try {
			 
			//gcmResult = gcmSender.send(gcmMessage, did, RETRY);
			gcmResults =  gcmSender.send(gcmMessage, list, RETRY);
		} catch (IOException e) {
			System.out.println("IOException " + e.getMessage());
		}
		if (gcmResult != null)
			System.out.println("getCanonicalIds : "
					+ gcmResult.getCanonicalRegistrationId() + "\n"
					+ "getMessageId : " + gcmResult.getMessageId());
	}

}