import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.DocumentFilter;
import java.util.*;

//메인 함수가 있는 클래스
public class PriterReservatior extends JFrame{
    InternetConnect internetConnect;
    
    //로그인 화면을 맨 처음에 띄운다.
    public PriterReservatior(){
        setTitle("시스쿨 3D 프린터 예약 프로그램");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        internetConnect = new InternetConnect(); //인터넷 연결 시작
        if(internetConnect.testInternetConnection())
        setContentPane(new LoginPanel(internetConnect, this));
        
        setMinimumSize(new Dimension(1138, 640));
        setMaximumSize(new Dimension(2560, 1440));
        setSize(1280, 720);
        setVisible(true);
    }
    
    //로그인이 성공하면 이 함수를 호출하여 예약 화면으로 넘어간다.
    public void loginSuccess(){
        setContentPane(new ReservationPanel(internetConnect));
        revalidate();
        repaint();
    }

    //메인 함수, 자기자신만 인스턴스 한다.
    public static void main(String[] args){
        PriterReservatior a = new PriterReservatior();
    }
    
}


//로그인 화면을 구현하는 클래스
class LoginPanel extends JPanel{
    InternetConnect internetInstance;
    JPasswordField pwField;
    JTextField idField;
    PriterReservatior mainObject;

    // 로그인 패널 인스턴스 
    public LoginPanel(InternetConnect internetInstance, PriterReservatior mainObject){
        this.internetInstance = internetInstance;
        this.setLayout(new BorderLayout());

        // 위쪽 상단바 패널널
        JPanel titlePanel = new loginTitlePanel();
        this.add(titlePanel, BorderLayout.NORTH);

        //로그인 버튼과 입력 창이 있는 패널널
        JPanel loginInputpane = new loginInputPanel();
        this.add(loginInputpane, BorderLayout.CENTER);

        this.mainObject = mainObject;
    }

    // 위쪽 상단 부분 패널
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
    
    // 로그인 버튼과 입력창을 모은 패널
    class loginInputPanel extends JPanel{
        public loginInputPanel(){

            this.setLayout(new BorderLayout());
            JPanel inputcontent = new JPanel(new GridBagLayout());
            GridBagConstraints indexer = new GridBagConstraints();
            
            // 중앙 베너 문구
            JLabel descriveLabel = new JLabel("C-school 3D 프린터 예약 프로그램", SwingConstants.CENTER);
            descriveLabel.setFont(new Font("Dialog", Font.PLAIN, 30));
            this.add(descriveLabel, BorderLayout.NORTH);
            
            // 입력 패널 
            indexer.gridx = 0;
            indexer.gridy = 0;
            indexer.insets = new Insets(0, 10, 150, 10);
            JPanel inputPanel =  new idNPasswordPanel();
            inputcontent.add(inputPanel, indexer);
            
            // 로그인 버튼
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
    
    // 아이디와 비밀번호 TextFild를 모은 패널
    class idNPasswordPanel extends JPanel{
        public idNPasswordPanel(){
            
            setLayout(new GridLayout(2,2,5,5));

            // ID 입력 부분
            idField = new JTextField(10);
            idField.setFont(new Font("Dialog", Font.PLAIN ,20));
            ((AbstractDocument)idField.getDocument()).setDocumentFilter(new DocumentSizeFilter(40)); //최대 40 자 제한한

            JLabel idLabel = new JLabel(" 학번 ", SwingConstants.CENTER);
            idLabel.setFont(new Font("Dialog", Font.PLAIN ,20));

            JPanel idPanel = new JPanel();
            idPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
            idPanel.add(idLabel);
            idPanel.add(idField);


            // 비밀번호 입력 부분
            pwField = new JPasswordField(10);
            pwField.setFont(new Font("Dialog", Font.PLAIN ,20));
            ((AbstractDocument)pwField.getDocument()).setDocumentFilter(new DocumentSizeFilter(40)); //최대 40 자 제한한

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
    
    // 로그인 버튼에 대한 이벤트 리스너
    class LoginEventLister implements ActionListener{
        public void actionPerformed(ActionEvent e){

            char[] passwd = pwField.getPassword();
            String id = idField.getText();

            // 둘 중 하나라도 비어있다면면
            if(id.isEmpty() || passwd.length==0){
                JOptionPane.showMessageDialog(mainObject, "학번과 비밀번호를 입력해 주세요", "주의", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            // 로그인을 실패했다면면
            if(!(internetInstance.login(id, passwd))){
                JOptionPane.showMessageDialog(mainObject, "학번이나 비밀번호가 틀렸습니다", "로그인 실패", JOptionPane.ERROR_MESSAGE);
                return;
            }
            mainObject.loginSuccess();
        }
    }
}

//예약 화면을 구현하는 클래스
class ReservationPanel extends JPanel{
    boolean secondFrameIsExist = false;
    JPanel upperBar;
    JPanel reservationGrid;
    JComboBox<Integer> dateSelectBox;
    JComboBox<String> printerModelSelectBox;
    ImageIcon []printerImages = new ImageIcon[2];
    JLabel printerImageLabel;
    int []times = {9, 10, 11, 13, 14, 15, 16, 18, 19, 20};
    PrinterStatusPanel []printers;
    InternetConnect internetInstance;
    DateArrayMaker dateArrayMaker;
    PrinterReservationStatus [][]myReservationData = new PrinterReservationStatus[2][];
    
    //생성자
    public ReservationPanel(InternetConnect instance){

        //이전에 예약 파일이 있다면 파일을 로드한다.
        if(isFileExists()){
            dataLoader();
        }
        else{
            myReservationData[0] = new PrinterReservationStatus[9];
            for(int i=0; i<9; i++){
                myReservationData[0][i] = new PrinterReservationStatus("p1s", i);
            }
            myReservationData[1] = new PrinterReservationStatus[4];
            for(int i = 0; i<4; i++){
                myReservationData[1][i] = new PrinterReservationStatus("x1c", i);
            }
        }

        internetInstance = instance;
        this.setLayout(new BorderLayout());

        //내부 패널들 인스턴스 후 추가
        upperBar = new UpperBar();
        this.add(upperBar, BorderLayout.NORTH);

        reservationGrid = new ReservationGrid();
        this.add(reservationGrid, BorderLayout.CENTER);
    }   

    // 예약 목록 이외 부분을 관장하는 패널, 상단 베너 + 프린터 , 날자 선택 페널의 부모
    class UpperBar extends JPanel{
        JPanel printerDataSelectPanel;
        public UpperBar(){
            this.setLayout(new BorderLayout());

            //베너 생성
            JPanel titlePanel = new JPanel();
            JLabel titleLabel = new JLabel("예약 현황");
            titleLabel.setFont(new Font("Dialog", Font.PLAIN, 28));
            titlePanel.add(titleLabel);
            titlePanel.setBackground(new Color(180, 230, 255));
            this.add(titlePanel, BorderLayout.NORTH);

            //조회 패널 생성
            printerDataSelectPanel = new PrinterdateSelectPanel();
            this.add(printerDataSelectPanel);

        }
    }

    // 프린터 조회 패널
    class PrinterdateSelectPanel extends JPanel{
        String []printerOption = {"p1s", "x1c"}; //프린터 모델


        public PrinterdateSelectPanel(){
            dateArrayMaker = new DateArrayMaker();
            this.setLayout(new FlowLayout());

            //이미지 불러오기
            for(int i=0; i<2; i++){
                printerImages[i] = new ImageIcon("images/"+printerOption[i]+".png");
                Image image = printerImages[i].getImage();
                image = image.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                printerImages[i] = new ImageIcon(image);
            }
            

            // 요소 생성 후 패널에 추가 
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

    // 프린터 상태 출력 패널(모든 프린터 상태 페널들의 부모)
    class ReservationGrid extends JPanel{
        int printerCount = 9; //프린터 최대 대수

        public ReservationGrid(){
            printers =  new PrinterStatusPanel[printerCount];
            this.setLayout(new GridLayout(printers.length+1,1 ));//항목 표시 바 포함 10개
            
            //항목 표시 부분 생성
            PrinterStatusPanel topChartPanel = new PrinterStatusPanel();
            topChartPanel.setPrinterName("프린터명/시작시간");

            //프린팅 시작 시간, 이 시간은 C-school 예약 페이지에서 예약이 가능한 시간대
            for(int i=0; i<times.length; i++){
                topChartPanel.setStatus(Integer.toString(times[i]), i);
            }
            this.add(topChartPanel, 0);
            topChartPanel.setBackground(new Color(200, 230, 255)); //색상 밝은 파랑색

            
            //프린터들은 for문으로 생성
            printers = new PrinterStatusPanel[printerCount];
            for(int i=0; i<printerCount; i++){
                printers[i] = new PrinterStatusPanel(i);
                printers[i].setBackground(new Color(242, 242, 242));
                this.add(printers[i], i+1);
            }
        }
    }
    
    // 단일 프린터(행) 을 생성하는 함수 
    class PrinterStatusPanel extends JPanel{
        ReservationLabelClickEventListener listener[] = new ReservationLabelClickEventListener[10];
        JLabel printerNameLabel;
        JLabel[] reservationStatus = new JLabel[10];
        JPanel statusGrid = new JPanel(new GridLayout(1, 10));
        String printerModel;
        

        // 생성자, 초기 기본 빈 문자열 생성, 항목 설명 바 생성
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
        
        // 생성자 2, 빈 문자열 생성 후 프린터 인덱스 지정, 실제 프린터 항목 바 생성
        public PrinterStatusPanel(int printerIdx) {
            this();
            for (int i = 0; i < 10; i++) {
                listener[i] = new ReservationLabelClickEventListener( printerModel ,printerIdx,i);
                reservationStatus[i].addMouseListener(listener[i]);
            }
        }


        public void setPrinterModel(String printerModel){
            this.printerModel = printerModel;
            for(int i=0; i<listener.length; i++){
                listener[i].setModel(printerModel);
            }
        }

        public void setPrinterName(String name){
            printerNameLabel.setText(name);
        }

        //행의 색을 동일하게 바꾸기 위한 함수
        @Override
        public void setBackground(Color bg){
            super.setBackground(bg);
            if (statusGrid != null) {
                statusGrid.setBackground(bg);  
            }
        }

        // 프린터 상태에 따라 글자 색을 변경
        public void setStatus(String status, int idx){
            reservationStatus[idx].setText(status);
            if(status.equals("예약 불가")){
                reservationStatus[idx].setForeground(Color.RED);
            }
            else if (status.equals("예약됨")){
                reservationStatus[idx].setForeground(Color.CYAN);
            }
            else{
                reservationStatus[idx].setForeground(Color.BLACK);
            }
        }
    }

    // 조회 버튼 이벤트 리스너 프린터 상태를 업데이트 하는 함수를 호출한다.
    class ChackEventLister implements ActionListener{
        public void actionPerformed(ActionEvent e){
            updateStatusGrid();
        }
    }

    // 프린터 상태 텍스트 라벨 클릭 리스너. 예약 팝업창을 생성한다.
    class ReservationLabelClickEventListener extends MouseAdapter{
        private String printerModel;
        private int month;
        private int printerIdx;
        private int time;

        // 초기화
        public ReservationLabelClickEventListener(String printerModel, int printerIdx, int time){
            month = dateArrayMaker.getMonth();
            this.time = time;
            this.printerIdx = printerIdx;
        }

        // 예약 코드로 프린터 데이터를 전송하기 전에 미리 받아놓는다.
        public void setModel(String printerModel){
            this.printerModel = printerModel;
        }

        //마우스 클릭 리스너, 라벨의 문자열이 "예약 가능" 일 경우 위의 openPopUp 함수를 실행시킨다.
        public void mouseClicked(MouseEvent e){
            Object source  = e.getSource();

            if(source instanceof JLabel){
                JLabel clickedJLabel = (JLabel)source;
                String text = clickedJLabel.getText();
                
                if(text.equals("예약 가능")){
                   openPopUp();
                }
                else{
                    System.out.println("예약 불가능"); //예약 불가능한 경우 따로 창을 띄우지 않는것이 사용하기 편하다.
                }

            }
        }

        // 예약 팝업창을 생성한다.
        private void openPopUp(){
            if(!secondFrameIsExist){
                secondFrameIsExist = true;
                ReservationPopUp popUp = new ReservationPopUp(internetInstance, this.printerModel, printerIdx, month, getSelectedDate(), time);
                
                popUp.addWindowListener(new FrameDisposeListener());
            }
        }

        //팝업창이 사라지면 기존 프린터 상태들을 업데이트 하기 위한 이벤트 리스너
        public class FrameDisposeListener extends WindowAdapter {
            @Override
            public void windowClosed(WindowEvent e) {
                secondFrameIsExist = false;
                updateStatusGrid();
            }
        }
    }


    // 프린터 예약 상태를 파일로 저장하는 함수
    public void dataSaver(){
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("myReservationData.ser"))) {
            out.writeObject(myReservationData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    // 프린터 예약 상태를 불러오는 함수
    public void dataLoader(){
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("myReservationData.ser"))) {
            PrinterReservationStatus[][] deserializedData = (PrinterReservationStatus[][]) in.readObject();
            myReservationData = deserializedData;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    // 예약 상태 저장 파일이 있는지 확인하는 함수
    public boolean isFileExists() {
        File file = new File("myReservationData.ser");
        return file.exists();
    }

    
    // 위쪽 조회 패널의 프린터 이미지를 바꾸는 함수
    public void changeImage(String model){
        if(model.equals("x1c")){
            printerImageLabel.setIcon(printerImages[1]);
        }
        else if(model.equals("p1s")){
            printerImageLabel.setIcon(printerImages[0]);
        }
        printerImageLabel.repaint();
    }

    // 행의 라벨에 적힌 텍스트를 매개변수로 받은 데이터로 바꾸는 함수
    public void setRowData(int idx, String printerName, String printerModel, String[] status){
        PrinterStatusPanel printer = printers[idx];

        // 행에 들어갈 데이터 전달
        printer.setPrinterName(printerName);
        printer.setPrinterModel(printerModel);

        // 이름이 없다면 배경을 기본 색상으로 변경
        if (printerName.equals("")){
            printer.setBackground(new Color(242, 242, 242));
        }

        // 홀수 짝수간 행의 배경색을 구분
        else{
            if(idx%2 == 1){
                printer.setBackground(new Color(200, 200, 200));
            }
            else{
                printer.setBackground(new Color(220, 220, 220));
            }
        }

        // 예약 상태 표시 라벨의 문자열을 업데이트
        for(int i=0; i<10; i++){
            printer.setStatus(status[i], i);
            printer.setPrinterModel(printerModel);
        }
    }

    // 프린터 모델 선택 박스의 값을 반환
    public String getSelectedModel(){
        return (String)printerModelSelectBox.getSelectedItem();
    }
    
    // 날자 선택에서 가져온 값을 반환
    public int getSelectedDate(){
        return (int)dateSelectBox.getSelectedItem();
    }

    // 인터넷에서 가져온 정보와 저장된 정보로 라벨 데이터를 직접 업데이트 하는 함수
    public void updateStatusGrid(){
        // 기존 내 예약 정보 저장
        dataSaver();
        Calendar calendar = Calendar.getInstance();
        int date = getSelectedDate();
        String model = getSelectedModel();


        // x1c 모델의 경우 4대 사용 가능
        if (model.equals("x1c")){
            for(int i=0; i<4; i++){
                String []data = internetInstance.getPrinterStatus(model, i, date, calendar.get(Calendar.MONTH)+1); //인터넷에서 정보를 가져온 후 파싱
                String []status = new String[10];
                System.arraycopy(data, 1, status, 0, 10);

                // 만일 업데이트 하는 날자에 내 예약 정보가 있다면 합친다.
                if(myReservationData[1][i].containsKey(date)){
                    PrinterDayReservationStatus mydata = myReservationData[1][i].get(date);
                    mydata.addStatus(status);
                    status = mydata.getStatus();
                }
                
                // 행에 데이터 쓰기
                setRowData(i, data[0], model, status);
            }

            // 나머지 5개 행은 빈찬으로 변경
            for(int i=4; i<9; i++){
                String []data = { "", "", "", "", "", "", "", "", "", "" };
                setRowData(i, "", model,  data);
            }

        }

        // p1s 모델의 경우 9대 사용 가능 
        else if(model.equals("p1s")){
            for(int i=0; i<9; i++){
                String []data = internetInstance.getPrinterStatus(model, i, date, calendar.get(Calendar.MONTH)+1); //인터넷에서 정보를 가져온 후 파싱
                String []status = new String[10];
                System.arraycopy(data, 1, status, 0, 10);

                // 만일 업데이트 하는 날자에 내 예약 정보가 있다면 합친다.
                if(myReservationData[0][i].containsKey(date)){
                    PrinterDayReservationStatus mydata = myReservationData[0][i].get(date);
                    mydata.addStatus(status);
                    status = mydata.getStatus();
                }

                // 행에 데이터 쓰기
                setRowData(i, data[0], model, status);
            }
        }
        // 해당 모델에 맞는 이미지로 업데이트
        changeImage(model);
    }


    // 예약 팝업창
    class ReservationPopUp extends JFrame{
        private int []timeTable = {9, 10, 11, 13, 14, 15, 16, 18, 19, 20};
        private int []availableTimeTable = {1, 1, 2, 1, 1, 1, 2, 1,  1, 13};
        private int []fridayAvailableTimeTable = {1, 1, 2, 1, 1, 1, 2, 1,  1, 61};
        private String printerName;
        private JTextField printTimeTextField;
        private JTextArea printingPurposeTextArea;
        private InternetConnect internetInstance;
        private JButton cancleButton;
        private JButton reservationButton;
        private String printerModel;
        private int printerIdx;
        private int month;
        private int day;
        private int startTimeIdx;


        final int maxPrintingTime = 100; // 사용 목적 최대 글자수
        

        //메인 프레임 생성 
        public ReservationPopUp(InternetConnect internetInstance ,String printerModel,int printerIdx, int month, int day, int startTimeIdx){
            this.internetInstance = internetInstance;
            this.printerModel = printerModel;
            this.printerIdx = printerIdx;
            this.month = month;
            this.day = day;
            this.startTimeIdx = startTimeIdx;
            printerName = internetInstance.getPrinterStatus(printerModel, printerIdx, day, month)[0];

            
            setTitle(month+"월 "+day +"일 "+ timeTable[startTimeIdx]+"시 부터 사용 예약");
            setLayout(new BorderLayout());
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            this.add(informPanelMaker(), BorderLayout.CENTER);  // 정보 패널 생성
            this.add(buttonPanelMaker(), BorderLayout.SOUTH);   // 버튼 패널 생성

            this.setSize(600, 400);
            this.setResizable(false);
            this.setVisible(true);
        }

        // 버튼 패널 생성
        private JPanel buttonPanelMaker(){
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 10));

            // 취소 버튼
            cancleButton = new JButton("취소");
            cancleButton.addActionListener(new CancleButtonListener());

            // 예약 버튼
            reservationButton = new JButton("예약");
            reservationButton.addActionListener(new ReservationButtonListener());

            buttonPanel.add(reservationButton);
            buttonPanel.add(cancleButton);

            return buttonPanel;
        }

        // 정보 패널 생성
        private JPanel informPanelMaker(){
            JPanel informPanel = new JPanel(new GridBagLayout());
            GridBagConstraints indexer = new GridBagConstraints();
            
            // 선택한 프린터 정보 라벨
            indexer.gridx = 1;
            indexer.gridy = 0;
            indexer.gridwidth = 2;
            indexer.insets = new Insets(40, 10, 15, 10);
            JLabel printerInformLabel = new JLabel("선택한 프린터 : "+printerName);
            printerInformLabel.setFont(new Font("Dialog", Font.PLAIN, 18));
            informPanel.add(printerInformLabel, indexer);

            // 프린팅 예약 시작시간
            indexer.gridy = 1;
            indexer.insets = new Insets(12, 10, 15, 10);
            JLabel timeInformLabel = new JLabel("프린팅 시작 시간 : "+month+"월 "+ day +"일 " + timeTable[startTimeIdx]+"시" );
            timeInformLabel.setFont(new Font("Dialog", Font.PLAIN, 18));
            informPanel.add(timeInformLabel, indexer);

            // 프린팅 소요 시간 라벨
            indexer.gridy = 2;
            indexer.gridwidth = 1;
            indexer.insets = new Insets(0, 5, 5, 0);
            JLabel printingTimeLabel = new JLabel("프린팅 소요 시간 : ");
            printingTimeLabel.setFont(new Font("dialog", Font.PLAIN, 18));
            informPanel.add(printingTimeLabel, indexer);

            // 프린팅 소요 시간 입력 텍스트 필드
            indexer.gridx = 2;
            printTimeTextField = new JTextField(8);
            printTimeTextField.setFont(new Font("dialog", Font.PLAIN, 18));
            ((AbstractDocument)printTimeTextField.getDocument()).setDocumentFilter(new DocumentNumberFilter()); //숫자만 들어가도록 제한
            informPanel.add(printTimeTextField, indexer);

            // 사용 목적 라벨
            indexer.gridy = 3;
            indexer.gridx = 1;
            JLabel printingPurposeLabel = new JLabel("사용 목적");
            printingPurposeLabel.setFont(new Font("dialog", Font.PLAIN, 18));
            informPanel.add(printingPurposeLabel, indexer);

            //사용 목적 입력 텍스트 에러리아
            indexer.gridx = 2;
            printingPurposeTextArea = new JTextArea(5, 8);
            ((AbstractDocument)printingPurposeTextArea.getDocument()).setDocumentFilter(new DocumentSizeFilter()); //100글자 미만만 입력 가능하도록 제한
            printingPurposeTextArea.setFont(new Font("dialog", Font.PLAIN, 18));
            JScrollPane printingPurposePane = new JScrollPane(printingPurposeTextArea);
            informPanel.add(printingPurposePane, indexer);

            // 프린터 이미지 입력
            indexer.gridx = 0;  
            indexer.gridy = 0;
            indexer.gridwidth = 1;
            indexer.gridheight = 4;
            ImageIcon printerImageIcon = new ImageIcon("images/"+printerModel+".png");
            Image imageicon = printerImageIcon.getImage();
            imageicon = imageicon.getScaledInstance(230, 250, Image.SCALE_SMOOTH);
            printerImageIcon = new ImageIcon(imageicon);
            JLabel printerImageLabel = new JLabel(printerImageIcon);
            informPanel.add(printerImageLabel, indexer);


            return informPanel;
        }

        // 예약 진행 함수
        private void makeReservation(){

            // 사용 목적이 비어있는지 확인
            if(printingPurposeTextArea.getText().isEmpty()){
                JOptionPane.showMessageDialog(this, "사용 목적을 적어주세요", "주의", JOptionPane.ERROR_MESSAGE);
                return;
            }

            //사용 시간이 알맞은 범위(최대 100, 다른 예약과 겹치지는 않는지) 에 있는지 확인
            int neededPrintingTime = 0;
            if(!printTimeTextField.getText().isEmpty()){
                neededPrintingTime = Integer.parseInt(printTimeTextField.getText());
            }
            if(!checkMaxTime(neededPrintingTime)){
                return;
            }

            //예약 진행
            applyReservation(neededPrintingTime);

            JOptionPane.showMessageDialog(this, "예약되었습니다.", "예약 성공", JOptionPane.OK_CANCEL_OPTION);
            dispose();
        }
        
        // 실제 예약 실행 함수
        private void applyReservation(int neededPrintingTime){

            int findingDate = day;
            int findingTimeIdx = startTimeIdx;
            int remainNeededPrintingTime = neededPrintingTime;


            while(remainNeededPrintingTime>0){ 

                // 일별로 필요한 프린팅 시간 계산
                PrinterDayReservationStatus data = new PrinterDayReservationStatus(printerModel, printerIdx, findingDate);
                
                for(int i=findingTimeIdx; i<10; i++){
                    remainNeededPrintingTime -= availableTimeTable[i];
                    data.setStatus("예약됨", i);             

                    if(remainNeededPrintingTime<=0){ //만일 필요한 프린팅 시간을 채우면 멈추기
                        break;
                    }
                }

                // p1s일때 내 예약 정보 저장
                if(printerModel.equals("p1s")){
                    myReservationData[0][printerIdx].addStatus(data);
                    System.out.println("데이터 저장됨, "+findingDate);
                }

                // x1c일때 내 예약 정보 저장
                else if(printerModel.equals("x1c")){
                    myReservationData[1][printerIdx].addStatus(data);
                    System.out.println("데이터 저장됨, "+findingDate);
                }
                
                internetInstance.reservate(data, printingPurposeTextArea.getText());//일 단위 예약 데이터를 인터넷으로 전송

                //다음날로 이동
                findingDate++;
                findingTimeIdx = 0;
            }
        }

        // 예약을 할 수 있는지 프린팅 소요 시간을 계산하는 함수
        private boolean checkMaxTime(int neededPrintingTime){
            Calendar calendar = Calendar.getInstance();
            int thisMonthMaxDate = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

            // 최대 100시간 제한
            if(neededPrintingTime > maxPrintingTime){
                JOptionPane.showMessageDialog(this, "프린팅 가능 최대시간은 " + maxPrintingTime + "시간입니다.", "경고", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            // 소요 시간이 없거나 0보다 작을 시
            if(neededPrintingTime <= 0){
                JOptionPane.showMessageDialog(this, "프린팅에 소요되는 시간을 입력해 주세요", "경고", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            int findingDate = day;
            int findingTimeIdx = startTimeIdx;
            int remainNeededPrintingTime = neededPrintingTime;

            // 기존 예약과 겹치는지 확인 
            while(remainNeededPrintingTime>0){

                // 일별 확인
                String[] status = internetInstance.getPrinterStatus(printerModel, printerIdx, findingDate, month);
                System.arraycopy(status, 1, status, 0, 10);

                int dataIdx =0;
                if(printerModel.equals("p1s")){
                    dataIdx = 0;
                }                
                else if(printerModel.equals("x1c")){
                    dataIdx = 1;
                }
                if(myReservationData[dataIdx][printerIdx].containsKey(findingDate)){
                    PrinterDayReservationStatus mydata = myReservationData[dataIdx][printerIdx].get(findingDate);
                    mydata.addStatus(status);
                    status = mydata.getStatus();
                }
                

                System.out.println(findingTimeIdx);
                for(int i=findingTimeIdx; i<10; i++){

                    //예약 시간별 확인
                    if(status[i].equals("예약 가능")){
                        calendar.set(Calendar.DATE, findingDate); // 금요일 탐지를 위한 Calendar

                        if(5==calendar.get(Calendar.DAY_OF_WEEK)){
                            remainNeededPrintingTime -= fridayAvailableTimeTable[i]; //주말에도 프린터 출력은 유지되기에 주말 시간 48시간을 더 이용할 수 있도록 계산
                        }
                        else{
                            remainNeededPrintingTime -= availableTimeTable[i];  //예약이 잠겨있는 시간(점심시간, 저녁시간, 야간) 에도 프맅더 출력은 유지되기에 출력 필요 시간에서 감소
                        }

                        // 모든 예약시간을 충족시키면 참을 리턴
                        if(remainNeededPrintingTime<=0){
                            return true;
                        }
                    }
                    else{
                        // 다른 예약과 겹치기에 불가능, 최대 예약 시간 고지
                        JOptionPane.showMessageDialog(this, "예약 가능 최대 시간은 " + (neededPrintingTime-remainNeededPrintingTime) + "시간입니다.", "경고", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }
                }

                //C-school 예약은 월별로 열리기에 다음달로 넘어가면 불가능
                if(findingDate == thisMonthMaxDate){
                    JOptionPane.showMessageDialog(this, "예약 가능 최대 시간은 " + (neededPrintingTime-remainNeededPrintingTime) + "시간입니다.", "경고", JOptionPane.ERROR_MESSAGE);
                    return false;
                }

                //다음날로 이동
                findingDate++;
                findingTimeIdx = 0;
            }

            return true;
        }

        
        //취소 버튼 이벤트 리스너
        class CancleButtonListener implements ActionListener{
            public void actionPerformed(ActionEvent e){
                dispose(); //창 닫기
            }
        }
        
        //예약 버튼 이벤트 리스너
        class ReservationButtonListener implements ActionListener{
            public void actionPerformed(ActionEvent e){
                makeReservation(); // 예약 진행 함수 호출
            }
        }
        
    }
}

// 글자수 제한 필터
class DocumentSizeFilter extends DocumentFilter{
    private int maxChar = 100;

    public DocumentSizeFilter(){

    }

    public DocumentSizeFilter(int maxChar){
        this.maxChar = maxChar;
    }

    @Override
    public void insertString(FilterBypass filterbypass, int offset, String string, AttributeSet attr) throws BadLocationException {
        if (string == null)
            return;

        if ((filterbypass.getDocument().getLength() + string.length()) <= maxChar) {
            super.insertString(filterbypass, offset, string, attr);
        } else {
            int available = maxChar -filterbypass.getDocument().getLength();
            if (available > 0) {
                String cutString = string.substring(0, available);
                super.insertString(filterbypass, offset, cutString, attr);
            }
            JOptionPane.showMessageDialog(null, "최대 "+maxChar+"글자까지 입력 가능합니다", "경고", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void replace(FilterBypass filterbypass, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
        if (text == null)
            return;
        int currentLength = filterbypass.getDocument().getLength();
        int newLength = currentLength - length + text.length();

        if (newLength <= maxChar) {
            super.replace(filterbypass, offset, length, text, attrs);
        } else {
            int available = maxChar - (currentLength - length);
            if (available > 0) {
                String cutString = text.substring(0, available);
                super.replace(filterbypass, offset, length, cutString, attrs);
            }
            JOptionPane.showMessageDialog(null, "최대 "+maxChar+"글자까지 입력 가능합니다", "경고", JOptionPane.ERROR_MESSAGE);
        }
    }
}

// 숫자 이외 문제 제한 필터
class DocumentNumberFilter extends DocumentFilter {
    int maxChar = 5;

    public void insertString(FilterBypass fb, int offset, String text, AttributeSet attr) throws BadLocationException {
        if (text.matches("[0-9]*")) {
            if ((fb.getDocument().getLength() + text.length()) <= maxChar) {
                super.insertString(fb, offset, text, attr);
            } else {
                int available = maxChar -fb.getDocument().getLength();
                if (available > 0) {
                    String cutString = text.substring(0, available);
                    super.insertString(fb, offset, cutString, attr);
                }
            }
        } else {
            super.insertString(fb, offset, "", attr);
            JOptionPane.showMessageDialog(null, "숫자만 입력 가능합니다.", "경고", JOptionPane.ERROR_MESSAGE);
            
        }
    }

    @Override
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
        if (text.matches("[0-9]*")) {
            int currentLength = fb.getDocument().getLength();
            int newLength = currentLength - length + text.length();

            if (newLength <= maxChar) {
                super.replace(fb, offset, length, text, attrs);
            } else {
                int available = maxChar - (currentLength - length);
                if (available > 0) {
                    String cutString = text.substring(0, available);
                    super.replace(fb, offset, length, cutString, attrs);
                }
                JOptionPane.showMessageDialog(null, "최대 "+maxChar+"글자까지 입력 가능합니다", "경고", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            super.insertString(fb, offset, "", attrs);
            JOptionPane.showMessageDialog(null, "숫자만 입력 가능합니다.", "경고", JOptionPane.ERROR_MESSAGE);
        }
    }
}


//인터넷과 상호작용이 필요한 경우 이 클래스를 이용한다. 로그인, 프린터 정보 가져오기, 예약이 있으며, 추후 크롬 드라이버로 연동 가능하도록 필요한 변수를 인자로 받도록 설계하였다.
class InternetConnect{
    String []x1cLink = {""};
    String []p1sLink = {""};

    public InternetConnect(){
        //크로미움 드라이버 연결
        System.out.println("연결되었습니까?");
    }

    //인터넷 연결 확인 함수. 현재는 테스트용으로 true만 반환
    public boolean testInternetConnection(){
        System.out.println("인터넷 연결 확인");
        return true;
    }

    //로그인 하는 함수, 성공시 true반환, 실패시 false 반환, 테스트용으로 항상 true 반환
    public boolean login(String id, char[] passwd){
        String correctId = "12211641";
        String correctPasswd = "test";
        String strPassword = new String(passwd);
        System.out.println("로그인 시도, ID = " + id +" 비밀번호 : " + strPassword);
        
        if( correctId.equals(id) && correctPasswd.equals(strPassword)){
            return true;
        }
        else {
            return false;
        }

    }

    //프린터 상태를 가져오는 함수. 현재는 모든 프린터가 모든 시간에 사용 가능하다고 가정한다. 
    public String[] getPrinterStatus(String printerModel, int idx, int day, int month){
        System.out.println(month + " 월 " + day +" 일의 "+ idx+"번째 " + printerModel + " 프린터의 예약 페이지로 접근, 현황 데이터 추출");

        String []result = new String[11];
        String name = "BampuLab "+ printerModel +" 0"+Integer.toString(idx+1); //크로미움 드라이버를 통해 장비 이름 가져오기기
        result[0] = name;
        for(int i=1; i<=10; i++){
            result[i] = "예약 가능"; // 크로미움 드라이버를 통해 장비 상태 가져오기기
        }
        return result;
    }

    //예약을 진행하는 함수
    public void reservate(PrinterDayReservationStatus data, String purposeData){
        
        String printerModel = data.getPrinterModel();
        int printerIdx = data.getPrinterIdx();
        int date = data.getDate();
        String []status = data.getStatus();
        
        System.out.println((Calendar.getInstance().get(Calendar.MONTH)+1) + " 월 " + date +" 일의 "+ printerIdx+"번째 " + printerModel + " 프린터의 예약 페이지로 접근, 예약 진행");
        
        //3D프린터의 모델명, 번호를 기반으로 해당 3d프린터 예약 페이지에 접근 후 예약 버튼 누름
        for(int i=0; i<status.length; i++){
            //예약 페이지에서 status안의 문자열이 "예약됨" 에 해당하는 시간대 체크박스에 체크
            continue;
        }
        //purposeData 의 값을 예약 사유에 넣고 예약 버튼 누르기
    }

}

//현재 날자를 가져오거나, 예약일을 선택할 때 영업일 리스트를 제작하는 클래스
class DateArrayMaker{
    Calendar calendar;
    int year;
    int month;
    int day;

    public DateArrayMaker(){
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DATE);
    }

    //현재 달의 영업일을 정수 리스트로 반환, C-school은 해당 달의 예약 페이지만 오픈하며, 영업일에만 사용할 수 있기 때문이다.
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

    public int getMonth(){
        return month;
    }
    
    public int getDay(){
        return day;
    }
}


//프린터 예약을 한 정보를 일 단위로 저장할 수 있는 클래스, 예약 정보, 날자, 프린터 모델, 인덱스를 가지고 있다.
class PrinterDayReservationStatus implements Serializable{
    private int printerIdx;
    private int date;
    private String printerModel;
    private String[] status = new String[10];

    public PrinterDayReservationStatus(){
        for(int i=0; i<status.length; i++){
            status[i] = "";
        }
    }

    public PrinterDayReservationStatus(String printerModel, int printerIdx, int date){
        this();
        this.printerIdx = printerIdx;
        this.printerModel = printerModel;
        this.date = date;
    }

    public boolean setStatus(String []status){
        if(status.length==10){
            System.arraycopy(status, 0, this.status, 0, 10);
            return true;
        }
        return false;
    }

    public boolean setStatus(String status, int idx){
        if(idx<10 && idx>=0){
            this.status[idx] = status;
            return true;
        }
        return false;
    }

    public String[] getStatus(){
        return status;
    }
    
    public String getStatus(int idx){
        if(idx<10&&idx>=0){
            return status[idx];
        }
        return "";
        
    }

    public void setDate(int date){
        this.date = date;
    }
    
    public int getDate(){
        return date;
    }

    public void setPrinterIdx(int idx){
        printerIdx = idx;
    }

    public int getPrinterIdx(){
        return printerIdx;
    }

    public void setPrinterModel(String printerModel){
        this.printerModel = printerModel;
    }

    public String getPrinterModel(){
        return printerModel;
    }

    //예약 데이터의 우선순위( 빈 문자열 >> 예약 가능 >> 예약 불가 >> 예약됨) 순서로 저장
    public void addStatus(PrinterDayReservationStatus data){
        PrinterDayReservationStatus curData = this;
        for(int i=0; i<10; i++){
            if(curData.getStatus(i).isEmpty()){
                continue;
            }
            else if(data.getStatus(i).isEmpty()){
                data.setStatus(curData.getStatus(i), i);
            }
            else if(data.getStatus(i).equals("예약됨")){
                continue;
            }
            else if(curData.getStatus(i).equals("예약됨")){
                data.setStatus(curData.getStatus(i), i);
            }
            else if(data.getStatus(i).equals("예약 불가")){
                continue;
            }
            else if(curData.getStatus(i).equals("예약 불가")){
                data.setStatus(curData.getStatus(i), i);
            }
        }
        status = data.getStatus();
    }

    //예약 데이터의 우선순위( 빈 문자열 >> 예약 가능 >> 예약 불가 >> 예약됨) 순서로 저장
    public void addStatus(String[] data){
        PrinterDayReservationStatus curData = this;
        for(int i=0; i<10; i++){
            
            if(curData.getStatus(i).isEmpty()){
                continue;
            }
            else if(data[i].isEmpty()){
                data[i]=curData.getStatus(i);
            }
            else if(data[i].equals("예약됨")){
                continue;
            }
            else if(curData.getStatus(i).equals("예약됨")){
                data[i]=curData.getStatus(i);
            }
            else if(data[i].equals("예약 불가")){
                continue;
            }
            else if(curData.getStatus(i).equals("예약 불가")){
                data[i]=curData.getStatus(i);
            }
        }
        status = data;
    }


}

//프린터 예약을 한 정보를 프린터 단위로 저장할 수 있는 클래스
class PrinterReservationStatus  implements Serializable {
    HashMap<Integer, PrinterDayReservationStatus> map = new HashMap<>();

    private int printerIdx;
    private String printerModel;

    public PrinterReservationStatus(String printerModel, int printerIdx){
        this.printerModel = printerModel;
        this.printerIdx = printerIdx;
    }

    public void put(PrinterDayReservationStatus data, int idx){
        data.setPrinterIdx(printerIdx);
        data.setPrinterModel(printerModel);
        map.put(idx, data);
    }

    //특정 날자의 정보가 들어오면 그 날자의 데이터에 우선순위( 빈 문자열 >> 예약 가능 >> 예약 불가 >> 예약됨) 순서로 저장
    public void addStatus(PrinterDayReservationStatus data){
        int date = data.getDate();
        if(!containsKey(date)){
            put(data, date);
            return;
        }
        PrinterDayReservationStatus curData = map.get(date);
        for(int i=0; i<10; i++){
            if(curData.getStatus(i).isEmpty()){
                continue;
            }
            else if(data.getStatus(i).isEmpty()){
                data.setStatus(curData.getStatus(i), i);
            }
            else if(data.getStatus(i).equals("예약됨")){
                continue;
            }
            else if(curData.getStatus(i).equals("예약됨")){
                data.setStatus(curData.getStatus(i), i);
            }
            else if(data.getStatus(i).equals("예약 불가")){
                continue;
            }
            else if(curData.getStatus(i).equals("예약 불가")){
                data.setStatus(curData.getStatus(i), i);
            }
        }
        map.put(date, data);
    }

    public boolean containsKey(int key){
        return map.containsKey(key);
    }

    public PrinterDayReservationStatus get(int key){
        return map.get(key);
    }

    public void putStatus(String[] status, int key){
        PrinterDayReservationStatus data = new PrinterDayReservationStatus(printerModel, printerIdx, key);
        data.setStatus(status);
        map.put(key, data);
    }

    public String[] getStatus(int date){
        if(containsKey(date)){
            return map.get(date).getStatus();
        }
        return null;
    }

    public void setPrinterIdx(int idx){
        printerIdx = idx;
    }

    public int getPrinterIdx(){
        return printerIdx;
    }

    public void setPrinterModel(String printerModel){
        this.printerModel = printerModel;
    }

    public String getPrinterModel(){
        return printerModel;
    }
}