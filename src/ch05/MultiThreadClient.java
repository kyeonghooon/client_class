package ch05;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

// 1단계 - 함수로 분리 해서 리팩트로이 진행
public class MultiThreadClient {

	public static void main(String[] args) {
		System.out.println("### 클라이언트 실행 ###");
		try (Socket socket = new Socket("localhost", 5000)) {
			System.out.println("connected to the server !!");

			// 서버와 통신을 위한 초기화
			Thread readThread = startReadThread(new BufferedReader(new InputStreamReader(socket.getInputStream())));
			Thread writeThread = startWrtieThread(new PrintWriter(socket.getOutputStream()),
					new BufferedReader(new InputStreamReader(System.in)));
			readThread.join();
			writeThread.join();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 1. 서버로부터 데이터를 읽는 스레드 시작 메서드 생성
	private static Thread startReadThread(BufferedReader reader) {
		Thread readThread = new Thread(() -> {
			try {
				String msg;
				while ((msg = reader.readLine()) != null) {
					System.out.println("서버에서 온 msg : " + msg);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		readThread.start();
		return readThread;
	}

	// 2. 키보드에서 입력을 받아 서버 측으로 데이터를 전송하는 스레드
	private static Thread startWrtieThread(PrintWriter writer, BufferedReader keyboardReader) {
		Thread writeThread = new Thread(() -> {
			try {
				String msg;
				while ((msg = keyboardReader.readLine()) != null) {
					// 전송
					writer.println(msg);
					writer.flush();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		writeThread.start();
		return writeThread;
	}
}
