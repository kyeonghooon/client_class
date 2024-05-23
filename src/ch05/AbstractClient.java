package ch05;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

public abstract class AbstractClient {

	private Socket socket;
	private BufferedReader readerStream;
	private PrintWriter writerStream;
	private BufferedReader keyboardReader;

	// set 메서드
	// 메서드 의존 주입(멤버 변수에 참조 변수 할당)
	protected void setSocket(Socket socket) {
		this.socket = socket;
	}

	// 실행의 흐름
	public final void run() {
		try {
			connection();
			setupStream();
			startService();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			cleanup();
		}
	}

	// 1. 서버에 연결
	protected abstract void connection() throws IOException;

	// 2. 스트림 초기화
	private void setupStream() throws IOException {
		readerStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		writerStream = new PrintWriter(socket.getOutputStream(), true);
		keyboardReader = new BufferedReader(new InputStreamReader(System.in));
	}

	// 3. 서비스 시작
	private void startService() {
		Thread readThread = createReadThread();
		Thread writeThread = createWriteThread();
		readThread.start();
		writeThread.start();
		try {
			readThread.join();
			writeThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	// 캡슐화
	private Thread createReadThread() {
		return new Thread(() -> {
			try {
				String msg;
				while ((msg = readerStream.readLine()) != null) {
					System.out.println("server 측 msg : " + msg);
				}
			} catch (SocketException e) {
				System.out.println("연결이 끊어졌습니다.");
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	private Thread createWriteThread() {
		return new Thread(() -> {
			try {
				String msg;
				// 클라이언트측 키보드에서 데이터를 한줄라인으로 읽음
				while ((msg = keyboardReader.readLine()) != null) {
					// 서버와 연결된 소켓에다가 데이터를 보냄
					writerStream.println(msg);
					writerStream.flush();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	// 캡슐화 - 소켓 자원 종료
	private void cleanup() {
		try {
			if (socket != null) {
				socket.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
