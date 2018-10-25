package html;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class ClientThread extends Thread{

	private BufferedReader reader;
	private PrintWriter writer;
	private Socket socket;
	private DataInputStream  dataReader ;
	private DataOutputStream dataWriter ;
	private String addr = "D:\\×¨Òµ¿Î\\web\\web";
	public ClientThread(Socket s) {
		// TODO Auto-generated constructor stub
		this.socket = s;
	
	}
	
	public void run(){
		
		try {
			
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			writer = new PrintWriter(socket.getOutputStream());
			String first = reader.readLine();
			while(first==null)
				first = reader.readLine();
			System.out.println("ip :"+socket.getInetAddress()+" port"+socket.getPort());
			System.out.println("request :"+first);
			String[] f = first.split(" ");
			if(f.length == 3&&f[2].equals("HTTP/1.1")){
				switch(f[0]){
				case "GET":
					get(f[1]);
					break;
				default:
					error(400);
				}
				
			}else{
				error(400);//Bad Request
			}
			writer.close();
			reader.close();
			socket.close();
			this.interrupt();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}

	private void error(int i) {
		// TODO Auto-generated method stub
		switch(i){
		case 404:
			System.out.println("404"+socket.isConnected());
			writer.println("HTTP/1.1 404 Not Found");
			writer.println("Content-Type:text/plain");
			writer.println("Content-Length:8");
			writer.println();
			writer.print("not find");
			writer.flush();
			break;
		case 400:
			writer.println("HTTP/1.1 400 Bad Request");
			writer.println("Content-Type:text/plain");
			writer.println("Content-Length:11");
			writer.println();
			writer.print("Bad Request");
			writer.flush();
			break;
			
		}
	}

	
	private void get(String first) {
		// TODO Auto-generated method stub
		String url ;
		String[] s ;
		if(first.contains("?")){
		     s = first.split("?");
		    url = s[0];
		       if(s.length>1){
		String[] parameter = s[1].split("&");
		       }
		}else{
			url = first;
		}
		File file = new File(addr+url);
		if(file.exists()&&!file.isDirectory()){
			writer.println("HTTP/1.1 200 ok");
			writer.flush();
			System.out.println("HTTP/1.1 200 ok"+url);
		ArrayList headerlist = new ArrayList();
		HashMap headermap = new HashMap();
		
		try {
			String header = reader.readLine();
			while(header.length()>1){
				System.out.println(header);
				s=header.split(":");
				headerlist.add(s[0]);
				headermap.put(s[0], s[1]);
				header = reader.readLine();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(url.endsWith(".html")){
			writer.println("Content-Type:text/html");
			writer.flush();
		}else if(url.endsWith(".jpg")){
			writer.println("Content-Type:image/jpeg");
			writer.flush();
		}else{
			writer.println("Content-Type:application/octet-stream");
			writer.flush();
		}
		writer.println("Content-Length:"+file.length());
		writer.println();
		writer.flush();
		
		try {
			dataWriter = new DataOutputStream(socket.getOutputStream());
			 DataInputStream in = new DataInputStream(new FileInputStream(addr+url));
				for(int i=0;i<file.length();i++){
					 dataWriter.writeByte(in.readByte());
				     dataWriter.flush();
			   }
			    in.close();
			    System.out.println("finish");
			    writer =new PrintWriter(socket.getOutputStream());
				dataWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}else{
			try {
				String header;
				header = reader.readLine();
				while(header.length()>1){
					System.out.println(header);
					header = reader.readLine();
				}
				error(404);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
