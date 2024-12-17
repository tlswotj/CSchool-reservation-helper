import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class loginTitlePanel extends JPanel{
    public loginTitlePanel(){
        JLabel titLabel = new JLabel("인하대학교 창업지원단");
        titLabel.setForeground(new Color(100, 130, 250));
        this.setBackground(new Color(180, 230, 255));

        titLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titLabel.setFont(new Font("Dialog", Font.PLAIN, 50));
        this.add(titLabel);

    }
}

class loginInputPanel extends JPanel{
    public loginInputPanel(){
        this.setLayout(new GridBagLayout());
        GridBagConstraints indexer = new GridBagConstraints();
        indexer.gridx = 0;
        indexer.gridy = 0;
        indexer.insets = new Insets(0, 10, 0, 10);
        JPanel inputPanel =  new idNPasswordPanel();
        this.add(inputPanel, indexer);

        JButton loginButton = new JButton("로그인");
        loginButton.setFont(new Font("Dialog", Font.PLAIN ,20));
        loginButton.setPreferredSize(new Dimension(150, 70));
        indexer.gridx += 1;
        indexer.insets = new Insets(0,0,0,50);
        this.add(loginButton, indexer);

    }
}

class idNPasswordPanel extends JPanel{
    public idNPasswordPanel(){
        //setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setLayout(new GridLayout(2,2,5,5));
        JTextField idField = new JTextField(10);
        idField.setFont(new Font("Dialog", Font.PLAIN ,20));
        JLabel idLabel = new JLabel(" 학번 ", SwingConstants.CENTER);
        idLabel.setFont(new Font("Dialog", Font.PLAIN ,20));
        JPanel idPanel = new JPanel();
        idPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        idPanel.add(idLabel);
        idPanel.add(idField);

        JPasswordField pwField = new JPasswordField(10);
        pwField.setFont(new Font("Dialog", Font.PLAIN ,20));
        JLabel pwLabel = new JLabel("비밀번호", SwingConstants.CENTER);
        pwLabel.setFont(new Font("Dialog", Font.PLAIN ,20));
        JPanel pwPanel = new JPanel();
        pwPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        pwPanel.add(pwLabel);
        pwPanel.add(pwField);
        
        //this.add(idPanel);
        //this.add(pwPanel);

        add(idLabel);
        add(idField);
        add(pwLabel);
        add(pwField);

    }

}

class loginPanel extends JPanel{
        public loginPanel(){
            this.setLayout(new BorderLayout());
            JPanel titlePanel = new loginTitlePanel();
            this.add(titlePanel, BorderLayout.NORTH);
            JPanel loginInputpane = new loginInputPanel();
            this.add(loginInputpane, BorderLayout.CENTER);
        }
    }

public class PriterReservatior extends JFrame{
    public PriterReservatior(){
        setTitle("시스쿨 3D 프린터 예약 프로그램");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container pane = getContentPane();
        pane.setLayout(new BorderLayout());
        setContentPane(new loginPanel());



        setSize(1280, 720);
        setVisible(true);
    }
    public static void main(String[] args){
        PriterReservatior a = new PriterReservatior();
    }
    
}