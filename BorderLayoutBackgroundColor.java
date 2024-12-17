import javax.swing.*;
import java.awt.*;

public class BorderLayoutBackgroundColor {
    public static void main(String[] args) {
        JFrame frame = new JFrame("BorderLayout Background Color Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        // BorderLayout을 사용한 패널 생성
        JPanel panel = new JPanel(new BorderLayout());

        // 패널의 배경색을 설정
        panel.setBackground(Color.CYAN);  // 배경색을 Cyan으로 설정

        // BorderLayout의 각 영역에 컴포넌트 추가
        panel.add(new JLabel("북쪽"), BorderLayout.NORTH);
        panel.add(new JLabel("센터"), BorderLayout.CENTER);
        panel.add(new JLabel("남쪽"), BorderLayout.SOUTH);

        // 패널을 프레임에 추가
        frame.add(panel);
        frame.setVisible(true);
    }
}