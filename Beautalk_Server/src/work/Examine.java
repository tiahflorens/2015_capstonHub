package work;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Examine {

	
	
	public native int exam(String path);

	static {
		System.loadLibrary("asdf");
	}
	
	
	public void test2(){
		
		System.out.println("run test");
		final String name ="image";
		final String path ="D:\\busdata\\" + name +".jpg";	
		
  		
		int result = -1;
		result= exam(path);
		System.out.println("result : "+ result);
	
	
	}
	public void test(){
		System.out.println("run test");
		
		DBAdapter db= new DBAdapter();
		byte[] bytes = db.test();
		final String name ="image";
		final String path ="D:\\busdata\\" + name +".jpg";
 		try {
			FileOutputStream out = new FileOutputStream(path);
			out.write(bytes);
			out.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
 		System.out.println("create jpg done");
 		
		int result = -1;
		result= exam(path);
		System.out.println("result : "+ result);
	}

}
