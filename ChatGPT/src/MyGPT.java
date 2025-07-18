import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONObject;

public class MyGPT extends JFrame {
    private JTextArea chatArea;
    private JTextField inputField;
    private JButton sendButton;
    private JSONArray messages = new JSONArray();

    // OpenAI API 정보
    private static final String API_KEY = "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";
    private static final String GPT_URL = "https://api.openai.com/v1/chat/completions";
    
    // 화면 구성하기 ( 생성자 함수 ) : 초기 화면을 만드는 부분
    public MyGPT() {
        setTitle("💬 MyGPT 와 대화하기");
        setSize(500, 600); // 창 크기
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 창 닫으면 프로그램 종료
        setLayout(new BorderLayout()); // 창을 위아래로 나누기 (구역 분할)

        // 채팅창 영역 ( 사용자가 쓴 말과 챗봇이 응답한 내용을 chatArea에 출력
        chatArea = new JTextArea(); // 채팅 내역 보여 줄 텍스트 박스
        chatArea.setEditable(false); // 사용자가 수정 하지 못하도록 설정
        chatArea.setLineWrap(true); // 줄바꿈 허용
        JScrollPane scrollPane = new JScrollPane(chatArea); // 스크롤 붙이기
        add(scrollPane, BorderLayout.CENTER); // 창 중앙에 배치

        // 입력창과 버튼
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputField = new JTextField(); // 입력창
        sendButton = new JButton("전송");
        
        // 입력창과 버튼을 BorderLayout.SOUTH (아래쪽) 에 넣기
        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);
        add(inputPanel, BorderLayout.SOUTH);

        // 이벤트 등록 (버튼을 누르거나 엔터를 치면 sendMessage() 함수 실행)
        sendButton.addActionListener(e -> sendMessage());
        inputField.addActionListener(e -> sendMessage());

        setVisible(true); // 이거 뭐임? 
    }

    private void sendMessage() { // 사용자가 입력하면 챗봇에게 요청을 보내는 함수
    	
    	/*
    	 * 역할 요약 :
    	 * 1. 입력창에서 사용자 말 꺼냄
    	 * 2. chatArea에 사용자 메시지 출력
    	 * 3. API에 요청 -> 응답 받아서 출력
    	 */
    	
        String userText = inputField.getText().trim(); // 사용자 입력 근데 trim() 은 왜 하지? 
        if (userText.isEmpty()) return; // 입력 없으면 종료

        chatArea.append("👤 나: " + userText + "\n"); // 내가 한 말 출력
        inputField.setText(""); // 입력창 비우기
        
        // messages 배열에 사용자 메시지 추가
        JSONObject userMsg = new JSONObject();
        userMsg.put("role", "user");
        userMsg.put("content", userText);
        messages.put(userMsg);
        
        // GPT API 호출 - 새로운 스레드에서 실행
        new Thread(() -> {
            try {
                String response = sendToGPT(messages); // 응답 받아오기
                
                // 응답도 저장
                JSONObject assistantMsg = new JSONObject();
                assistantMsg.put("role", "assistant");
                assistantMsg.put("content", response);
                messages.put(assistantMsg);

                SwingUtilities.invokeLater(() -> { // 지금 말고 화면 업데이트가 가능할 때 실행
                    chatArea.append("🤖 챗봇: " + response + "\n\n"); // 응답 출력
                });
            } catch (IOException e) {
                SwingUtilities.invokeLater(() -> {
                    chatArea.append("❌ 오류: " + e.getMessage() + "\n");
                });
            }
        }).start();
    }

    private String sendToGPT(JSONArray messages) throws IOException {
    	// 실제로 GPT에게 메시지 보내고 응답 받는 함수
    	
    	// URL, 연결 설정
        URL url = new URL(GPT_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("POST");
        conn.setRequestProperty("Authorization", "Bearer " + API_KEY);
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);
        
        // 요청 JSON 만들기
        JSONObject request = new JSONObject();
        request.put("model", "gpt-4o-mini");
        request.put("temperature", 0.7);
        request.put("messages", messages);
        
        // 서버에 요청 보내기
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()))) {
            bw.write(request.toString());
        }

     // 서버에서 응답 받기
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        }
        
        // 응답 JSON 파싱해서 content(내용)만 꺼냄
        JSONObject root = new JSONObject(sb.toString());
        return root.getJSONArray("choices")
                   .getJSONObject(0)
                   .getJSONObject("message")
                   .getString("content")
                   .trim();
    }
    
    // GUI 시작
    public static void main(String[] args) {
        SwingUtilities.invokeLater(MyGPT::new);
    }
}
