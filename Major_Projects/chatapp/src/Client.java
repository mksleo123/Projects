import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.net.Socket;
import java.io.*;

public class Client extends JFrame {

    Socket socket;
    //br for reading
    BufferedReader br;
    //out for wrritting
    PrintWriter out;

    private JLabel haeding=new JLabel("Client Area");
    private JTextArea messageArea=new JTextArea();
    private JTextField messageinput=new JTextField();
    private Font font=new Font("Roboto",Font.PLAIN,20);

    public Client(){
        try{
            System.out.println("sending request to server");
            socket=new Socket("127.0.0.1",7777);
            System.out.println("connection done.");

            br=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out =new PrintWriter(socket.getOutputStream());
            createGui();
            handleEvents();
            startReading();
            startWriting();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void handleEvents(){
        messageinput.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {
                //System.out.println("key relaesed"+e.getKeyCode());
                if(e.getKeyCode()==10){
                  //  System.out.println("you have pressed enter button");
                    String contentTosend=messageinput.getText();
                    messageArea.append("Me :"+contentTosend+"\n");
                    out.println(contentTosend);
                    out.flush();
                    messageinput.setText("");
                    messageinput.requestFocus();
                }

            }
        });
    }

    private void createGui(){
        //this represent window
        this.setTitle("Client Messenger");
        this.setSize(600,600);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //code the componetns
        haeding.setFont(font);
        messageArea.setFont(font);
        messageinput.setFont(font);
        haeding.setIcon(new ImageIcon("chat.png"));

        haeding.setHorizontalAlignment(SwingConstants.CENTER);
        //some sort of padding
        haeding.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        messageArea.setEnabled(false);
        messageinput.setHorizontalAlignment(SwingConstants.CENTER);
        //set the frame layout
        this.setLayout(new BorderLayout());

        //add components to frame
        this.add(haeding,BorderLayout.NORTH);
        JScrollPane jscrollpane=new JScrollPane(messageArea);
        this.add(jscrollpane,BorderLayout.CENTER);
        this.add(messageinput,BorderLayout.SOUTH);

        this.setVisible(true);


    }

    public void startReading(){
        //one thread is for reading the data
        Runnable r1=()-> {
            System.out.println("reader started");
            try{
            while (true) {
                String message = br.readLine();
                if (message.equals("exit")) {
                    System.out.println("Server terminated the chat");
                    JOptionPane.showMessageDialog(this,"server terminated the chat");
                    messageinput.setEnabled(false);
                    socket.close();
                    break;
                }
                //System.out.println("Server:" + message);
                messageArea.append("server : " + message+"\n" );


            }
        }catch (Exception e){
           // e.printStackTrace();
                System.out.println("connection is closed");
        }
        };
        new Thread(r1).start();
    }

    public void startWriting(){
        // other thread is for handling the sending of output
        Runnable r2=()->{
            System.out.println("Writer started........");
            try {
                while (!socket.isClosed()) {

                    BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
                    String content = br1.readLine();
                    //reading from console
                    out.println(content);
                    out.flush();
                    if(content.equals("exit")){
                        socket.close();
                        break;
                    }


                }
            }catch (Exception e){
                e.printStackTrace();
            }
        };
        new Thread(r2).start();
    }



    public static void main(String[] args) {
        System.out.println("This is client.......");
        new Client();
    }
}

