import java.awt.*;
import java.awt.event.*;
import java.security.cert.LDAPCertStoreParameters;

import javax.swing.*;


class loginPanel extends JPanel{
    public loginPanel(){
        this.setLayout(new BorderLayout());
        JPanel titlePanel = new loginTitlePanel();
        this.add(titlePanel, BorderLayout.NORTH);
        JPanel loginInputpane = new loginInputPanel();
        this.add(loginInputpane, BorderLayout.CENTER);
    }

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

            this.setLayout(new BorderLayout());
            JPanel inputcontent = new JPanel(new GridBagLayout());
            GridBagConstraints indexer = new GridBagConstraints();
            
            JLabel descriveLabel = new JLabel("C-school 3D 프린터 예약 프로그램", SwingConstants.CENTER);
            descriveLabel.setFont(new Font("Dialog", Font.PLAIN, 30));
            this.add(descriveLabel, BorderLayout.NORTH);

            indexer.gridx = 0;
            indexer.gridy = 0;
            indexer.insets = new Insets(0, 10, 150, 10);
            JPanel inputPanel =  new idNPasswordPanel();
            inputcontent.add(inputPanel, indexer);
    
            JButton loginButton = new JButton("로그인");
            loginButton.setFont(new Font("Dialog", Font.PLAIN ,20));
            loginButton.setPreferredSize(new Dimension(150, 70));
            indexer.gridx += 1;
            indexer.insets = new Insets(0,0,150,50);
            inputcontent.add(loginButton, indexer);

            this.add(inputcontent, BorderLayout.CENTER);
            
    
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
}

class reservationPanel extends JPanel{
    public reservationPanel(){
        this.setLayout(new BorderLayout());

        JPanel upperBar = new upperBar();
        this.add(upperBar, BorderLayout.NORTH);

        JPanel mainGrid = new reservationGrid();
        this.add(mainGrid, BorderLayout.CENTER);

    }

    class upperBar extends JPanel{
        public upperBar(){
            this.setLayout(new BorderLayout());

            JPanel titlePanel = new JPanel();
            JLabel titleLabel = new JLabel("예약 현황");
            titleLabel.setFont(new Font("Dialog", Font.PLAIN, 28));
            titlePanel.add(titleLabel);
            titlePanel.setBackground(new Color(180, 230, 255));
            this.add(titlePanel, BorderLayout.NORTH);

            // 프린터 모델, 날자 선택, 이미지 추가.

        }
    }

    class 

    class reservationGrid extends JPanel{
        int []times = {9, 10, 11, 13, 14, 15, 16, 17, 19, 20};
        JPanel []printers;
        int printerCount = 10;

        public reservationGrid(){
            printers =  new printerStatusPanel[printerCount];
            this.setLayout(new GridLayout(printers.length+1,1 ));
            
            printerStatusPanel topChartPanel = new printerStatusPanel();
            topChartPanel.setPrinterName("프린터명/시작시간");
            for(int i=0; i<times.length; i++){
                topChartPanel.setStatus(Integer.toString(times[i]), i);
            }
            this.add(topChartPanel, 0);

            

            printers = new printerStatusPanel[printerCount];
            for(int i=0; i<printerCount; i++){
                printers[i] = new printerStatusPanel();
                this.add(printers[i], i+1);
            }
            

        }
    }

    class printerStatusPanel extends JPanel{
        JLabel printerNameLabel;
        JLabel[] reservationStatus = new JLabel[10];
        
        public printerStatusPanel(){
            this.setLayout(new BorderLayout());
            printerNameLabel = new JLabel("",SwingConstants.CENTER);
            printerNameLabel.setFont(new Font("Dialog", Font.PLAIN, 20));
            printerNameLabel.setPreferredSize(new Dimension(180, 30));
            this.add(printerNameLabel, BorderLayout.WEST);

            JPanel statusGrid = new JPanel(new GridLayout(1, 10));
            for(int i=0; i<10; i++){
                reservationStatus[i] = new JLabel("", SwingConstants.CENTER);
                reservationStatus[i].setFont(new Font("Dialog", Font.PLAIN, 20));
                reservationStatus[i].setPreferredSize(new Dimension(50, 30));
                statusGrid.add(reservationStatus[i]);
            }
            this.add(statusGrid, BorderLayout.CENTER);
        }

        public void setPrinterName(String name){
            printerNameLabel.setText(name);
        }

        public void setStatus(String status, int idx){
            reservationStatus[idx].setText(status);
        }

        public void setStatusColor(Color color, int idx){
            reservationStatus[idx].setForeground(color);
        }
    }
}




public class PriterReservatior extends JFrame{
    public PriterReservatior(){
        setTitle("시스쿨 3D 프린터 예약 프로그램");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setContentPane(new reservationPanel());



        setSize(1280, 720);
        setVisible(true);
    }
    public static void main(String[] args){
        PriterReservatior a = new PriterReservatior();
    }
    
}