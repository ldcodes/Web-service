package html;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Serviceh {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		ServerSocket socket;
		try {
			socket = new ServerSocket(8888);
			while(true){
				Socket s=socket.accept();
				ClientThread clientThread = new ClientThread(s);
				clientThread.start();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
