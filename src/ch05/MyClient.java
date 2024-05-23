package ch05;

import java.io.IOException;
import java.net.Socket;

public class MyClient extends AbstractClient{


	@Override
	protected void connection() throws IOException {
		super.setSocket(new Socket("192.168.0.132", 5000));
		System.out.println("### 서버와 연결됨 ###");
	}

	public static void main(String[] args) {
		MyClient myClient = new MyClient();
		myClient.run();
	}
}
