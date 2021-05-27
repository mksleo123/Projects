import java.io.BufferedReader;
import java.net.Socket;
import java.io.*;

public class Client {

    Socket socket;
    //br for reading
    BufferedReader br;
    //out for wrritting
    PrintWriter out;

    public Client(){
        try{
            System.out.println("sending request to server");
            socket=new Socket("127.0.0.1",7777);
            System.out.println("connection done.");

            br=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out =new PrintWriter(socket.getOutputStream());
            startReading();
            startWriting();
        }catch(Exception e){
            e.printStackTrace();
        }
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
                    socket.close();
                    break;
                }
                System.out.println("Server:" + message);


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

