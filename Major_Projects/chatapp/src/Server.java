import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.*;
import java.io.*;
public class Server {

    //constructor ..
    Socket socket;
    ServerSocket server;
    //br for reading
    BufferedReader br;
    //out for wrritting
    PrintWriter out;
    public Server(){
        //we have to specify port here
    try {
        server=new ServerSocket(7777);
        System.out.println("server is ready to accept connection");
        System.out.println("waiting for client request....");
        //accepting the object throw by client and accept it here...
        socket=server.accept();

        br=new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out =new PrintWriter(socket.getOutputStream());
        startReading();
        startWriting();
    }catch (Exception e){
        e.printStackTrace();
    }


    }

    public void startReading(){
        //one thread is for reading the data
        Runnable r1=()->{
            System.out.println("reader started");
            try {
                while (true) {

                    String message = br.readLine();
                    if (message.equals("exit")) {
                        System.out.println("client terminated the chat");
                        socket.close();
                        break;
                    }
                    System.out.println("Client:" + message);


                }
            }catch (Exception e){
               // e.printStackTrace();
                System.out.println("connection closed");
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

                    out.println(content);
                    out.flush();
                    if(content.equals("exit")){
                        socket.close();
                        break;
                    }


                }
            }catch (Exception e){
              //  e.printStackTrace();
                System.out.println("connection closed");
            }

        };
        new Thread(r2).start();
    }




    public static void main(String[] args){
        System.out.println("this is server ..going to start");
        //initailaisatin of server
        new Server();
    }
}
