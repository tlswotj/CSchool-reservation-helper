import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

public class PriterReservatior extends JFrame{
    InternetConnect internetConnect;
    public PriterReservatior(){
        setTitle("시스쿨 3D 프린터 예약 프로그램");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        internetConnect = new InternetConnect();
        setContentPane(new LoginPanel(internetConnect, this));
        
        setSize(1280, 720);
        setVisible(true);
    }

    public void loginSuccess(){
        setContentPane(new ReservationPanel(internetConnect));
        revalidate();
        repaint();
    }

    public static void main(String[] args){
        PriterReservatior a = new PriterReservatior();
    }
    
}

class LoginPanel extends JPanel{
    InternetConnect internetInstance;
    JPasswordField pwField;
    JTextField idField;
    PriterReservatior mainObject;

    public LoginPanel(InternetConnect internetInstance, PriterReservatior mainObject){
        this.internetInstance = internetInstance;
        this.setLayout(new BorderLayout());
        JPanel titlePanel = new loginTitlePanel();
        this.add(titlePanel, BorderLayout.NORTH);
        JPanel loginInputpane = new loginInputPanel();
        this.add(loginInputpane, BorderLayout.CENTER);

        this.mainObject = mainObject;
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
            loginButton.addActionListener(new LoginEventLister());
            
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
            
            setLayout(new GridLayout(2,2,5,5));
            idField = new JTextField(10);
            idField.setFont(new Font("Dialog", Font.PLAIN ,20));
            JLabel idLabel = new JLabel(" 학번 ", SwingConstants.CENTER);
            idLabel.setFont(new Font("Dialog", Font.PLAIN ,20));
            JPanel idPanel = new JPanel();
            idPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
            idPanel.add(idLabel);
            idPanel.add(idField);
    
            pwField = new JPasswordField(10);
            pwField.setFont(new Font("Dialog", Font.PLAIN ,20));
            JLabel pwLabel = new JLabel("비밀번호", SwingConstants.CENTER);
            pwLabel.setFont(new Font("Dialog", Font.PLAIN ,20));
            JPanel pwPanel = new JPanel();
            pwPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
            pwPanel.add(pwLabel);
            pwPanel.add(pwField);
            
           
    
            add(idLabel);
            add(idField);
            add(pwLabel);
            add(pwField);
    
        }
    
    }
    class LoginEventLister implements ActionListener{
        public void actionPerformed(ActionEvent e){
            System.out.println("로그인 시도");
            char[] passwd = pwField.getPassword();
            String id = idField.getText();
            if(id.isEmpty() || passwd.equals("")){
                //학번과 비밀번호를 입력해 주세요 창 띄우기
                return;
            }
            if(!(internetInstance.login(id, passwd))){
                //학번과 비밀번호가 틀렸습니다. 
                return;
            }
            mainObject.loginSuccess();
        }
    }
}


class ReservationPanel extends JPanel{
    JPanel upperBar;
    JPanel reservationGrid;
    JComboBox<Integer> dateSelectBox;
    JComboBox<String> printerModelSelectBox;
    ImageIcon []printerImages = new ImageIcon[2];
    JLabel printerImageLabel;
    int []times = {9, 10, 11, 13, 14, 15, 16, 17, 19, 20};
    PrinterStatusPanel []printers;
    InternetConnect internetInstance;

    public void changeImage(String model){
        if(model.equals("x1c")){
            printerImageLabel = new JLabel(printerImages[1]);
        }
        else if(model.equals("p1s")){
            printerImageLabel = new JLabel(printerImages[0]);
        }
        printerImageLabel.repaint();
    }

    public ReservationPanel(InternetConnect instance){
        internetInstance = instance;
        this.setLayout(new BorderLayout());

        upperBar = new UpperBar();
        this.add(upperBar, BorderLayout.NORTH);

        reservationGrid = new ReservationGrid();
        this.add(reservationGrid, BorderLayout.CENTER);
    }

    public void setRowData(int idx, String printerName, String[] status){
        PrinterStatusPanel printer = printers[idx];
        printer.setPrinterName(printerName);
        if (printerName.equals("")){
            printer.setBackground(new Color(242, 242, 242));
        }
        else{
            if(idx%2 == 1){
                printer.setBackground(new Color(200, 200, 200));
            }
            else{
                printer.setBackground(new Color(220, 220, 220));
            }
        }
        for(int i=0; i<10; i++){
            printer.setStatus(status[i], i);
        }
    }

    public String getSelectedModel(){
        return (String)printerModelSelectBox.getSelectedItem();
    }
    
    public int getSelectedDate(){
        return (int)dateSelectBox.getSelectedItem();
    }

    

    class UpperBar extends JPanel{
        JPanel printerDataSelectPanel;
        public UpperBar(){
            this.setLayout(new BorderLayout());

            JPanel titlePanel = new JPanel();
            JLabel titleLabel = new JLabel("예약 현황");
            titleLabel.setFont(new Font("Dialog", Font.PLAIN, 28));
            titlePanel.add(titleLabel);
            titlePanel.setBackground(new Color(180, 230, 255));
            this.add(titlePanel, BorderLayout.NORTH);

            // 프린터 모델, 날자 선택, 이미지 CENTER에 추가.
            printerDataSelectPanel = new PrinterdateSelectPanel();
            this.add(printerDataSelectPanel);

        }
    }

    class PrinterdateSelectPanel extends JPanel{
        String []printerOption = {"p1s", "x1c"};
        DateArrayMaker dateArrayMaker = new DateArrayMaker();
        public PrinterdateSelectPanel(){

            this.setLayout(new FlowLayout());
            for(int i=0; i<2; i++){
                printerImages[i] = new ImageIcon("images/"+printerOption[i]+".png");
                Image image = printerImages[i].getImage();
                image = image.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                printerImages[i] = new ImageIcon(image);
            }
            

            
            printerImageLabel = new JLabel(printerImages[0]);
            printerImageLabel.setFont(new Font("Dialog", Font.PLAIN ,20));
            this.add(printerImageLabel);
            
            JLabel modelSelectCommentLabel = new JLabel("프린터 모델 : ");
            modelSelectCommentLabel.setFont(new Font("Dialog", Font.PLAIN ,20));
            this.add(modelSelectCommentLabel);

            printerModelSelectBox = new JComboBox<>(printerOption);
            printerModelSelectBox.setFont(new Font("Dialog", Font.PLAIN ,20));
            this.add(printerModelSelectBox);           


            JLabel dateSelectCommantLabel = new JLabel("날자 선택 : "+dateArrayMaker.getMonth()+"월  ");
            dateSelectCommantLabel.setFont(new Font("Dialog", Font.PLAIN ,20));
            this.add(dateSelectCommantLabel);


            dateSelectBox = new JComboBox<>(dateArrayMaker.getBusinessDays());
            dateSelectBox.setFont(new Font("Dialog", Font.PLAIN ,20));
            this.add(dateSelectBox);

            JButton checkButton = new JButton("조회");
            checkButton.setFont(new Font("Dialog", Font.PLAIN ,20));
            checkButton.addActionListener(new ChackEventLister());
            this.add(checkButton);
        }
    }

    class ReservationGrid extends JPanel{
        int printerCount = 9;

        public ReservationGrid(){
            printers =  new PrinterStatusPanel[printerCount];
            this.setLayout(new GridLayout(printers.length+1,1 ));
            
            PrinterStatusPanel topChartPanel = new PrinterStatusPanel();
            topChartPanel.setPrinterName("프린터명/시작시간");
            for(int i=0; i<times.length; i++){
                topChartPanel.setStatus(Integer.toString(times[i]), i);
            }
            this.add(topChartPanel, 0);
            topChartPanel.setBackground(new Color(200, 230, 255));

            

            printers = new PrinterStatusPanel[printerCount];
            for(int i=0; i<printerCount; i++){
                printers[i] = new PrinterStatusPanel();
                printers[i].setBackground(new Color(242, 242, 242));
                this.add(printers[i], i+1);
            }
        }
    }

    class ChackEventLister implements ActionListener{
        public void actionPerformed(ActionEvent e){
            int date = getSelectedDate(); 
            String model = getSelectedModel();

            if (model.equals("x1c")){
                for(int i=0; i<4; i++){
                    String []data = internetInstance.getPrinterStatus(model, i);
                    String []status = new String[10];
                    System.arraycopy(data, 1, status, 0, 10);
                    setRowData(i, data[0], status);
                }
                for(int i=4; i<9; i++){
                    String []data = { "", "", "", "", "", "", "", "", "", "" };
                    setRowData(i, "", data);
                }

            }
            else if(model.equals("p1s")){
                for(int i=0; i<9; i++){
                    String []data = internetInstance.getPrinterStatus(model, i);
                    String []status = new String[10];
                    System.arraycopy(data, 1, status, 0, 10);
                    setRowData(i, data[0], status);
                }
            }
            changeImage(model);

            //chromium 접속 후 날자랑 시간으로 데이터 가져오기. InternetConnect 사용

        }
    }
}

class InternetConnect{
    String []x1cLink = {""};
    String []p1sLink = {""};
    public InternetConnect(){
        //크로미움 드라이버 연결
        System.out.println("연결되었습니까?");
    }

    public boolean login(String id, char[] passwd){
        //로그인 하는 함수
        System.out.println("로그인 성공?");
        if(true){//로그인 성공시)
            return true;
        }
        else {
            return false;
        }

    }

    public String[] getPrinterStatus(String printerModel, int idx){
        String []result = new String[11];
        String name = "BampuLab "+ printerModel +" 0"+Integer.toString(idx+1); //크로미움 드라이버를 통해 장비 이름 가져오기기
        result[0] = name;
        for(int i=1; i<=10; i++){
            result[i] = "예약 불가"; // 크로미움 드라이버를 통해 장비 상태 가져오기기
        }
        return result;
    }



}

class PrinterStatusPanel extends JPanel{
        JLabel printerNameLabel;
        JLabel[] reservationStatus = new JLabel[10];
        JPanel statusGrid = new JPanel(new GridLayout(1, 10));
        
        public PrinterStatusPanel() {
            this.setLayout(new BorderLayout());
            printerNameLabel = new JLabel("", SwingConstants.CENTER);
            printerNameLabel.setFont(new Font("Dialog", Font.PLAIN, 20));
            printerNameLabel.setPreferredSize(new Dimension(180, 30));
            this.add(printerNameLabel, BorderLayout.WEST);

            for (int i = 0; i < 10; i++) {
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

        @Override
        public void setBackground(Color bg){
            super.setBackground(bg);
            if (statusGrid != null) {
                statusGrid.setBackground(bg);  
            }
        }

        public void setStatus(String status, int idx){
            reservationStatus[idx].setText(status);
            if(status.equals("예약 불가")){
                reservationStatus[idx].setForeground(Color.RED);
            }
            else if (status.equals("예약됨")){
                reservationStatus[idx].setForeground(Color.CYAN);
            }
        }

        public void setStatusColor(Color color, int idx){
            reservationStatus[idx].setForeground(color);
        }
    }



class DateArrayMaker{
    Calendar calendar;
    int year;
    int month;
    int day;
    public DateArrayMaker(){
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DAY_OF_MONTH);
    }

    public Integer[] getBusinessDays(){
        ArrayList<Integer> businessDaysList = new ArrayList<>();
        calendar.set(year, month, 1);
        int lastDayOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        for(int day = 1; day <= lastDayOfMonth; day++) {
            calendar.set(year, month-1, day);
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
            if(dayOfWeek >=2 && dayOfWeek <7){
                businessDaysList.add(day);
            }
        }
        Integer[] businessDays = new Integer[businessDaysList.size()];
        for(int i=0; i<businessDaysList.size(); i++){
            businessDays[i] = businessDaysList.get(i);
        }


        return businessDays;
    }

    public String getMonth(){
        return Integer.toString(month);
    }
}
