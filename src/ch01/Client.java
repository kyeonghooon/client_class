package ch01;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {

	public static void main(String[] args) {
		// 클라이언트 측 -- 소켓 통신을 하기 위해서 준비물
		// 1. 서버 측 컴퓨터의 주소 : 포트번호
		// 2. 서버측과 연결 될 기본 소켓이 필요하다.
		
		// 생성자 매개변수에 서버측 (IP주소, 포트번호)
		// 127.0.0.1 <- 자기 자신의 주소
		try (Socket socket = new Socket("localhost", 5000)) {
			// new Socket("localhost", 5000) -> 객체 생성 시 서버측과 연결되어서
			// 스트림을 활용 할 수 있다.
			// 대상은 소켓이다. !!!
			OutputStream output = socket.getOutputStream(); // 소켓에서 기반 스트림
			PrintWriter writer = new PrintWriter(output, true); // 기능 확장 - 보조 스트림
			writer.println("Hello, server");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
