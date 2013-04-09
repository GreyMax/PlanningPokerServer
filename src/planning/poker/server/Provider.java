package planning.poker.server;

import planning.poker.Estimate;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Provider {

    public static final String STOP = "Stop";
    ServerSocket providerSocket;
    Socket connection = null;
    ObjectOutputStream out;
    ObjectInputStream in;
    boolean isRunning = true;

    public Provider() {

    }

    public void run()
    {
        try{
            //1. creating a server socket
            providerSocket = new ServerSocket(9999, 10);
            //2. Wait for connection
            System.out.println("Waiting for connection");
            connection = providerSocket.accept();
            System.out.println("Connection received from " + connection.getInetAddress().getHostName());
            //3. get Input and Output streams
            out = new ObjectOutputStream(connection.getOutputStream());
            out.flush();
            in = new ObjectInputStream(connection.getInputStream());
            //4. The two parts communicate via the input and output streams
            do{
                try {
                    Object obj = in.readObject();
                    Estimate est = null;
                    if (obj instanceof Estimate) {
                        est = (Estimate) in.readObject();
                        Controller.getInstance().addEstimate(est);
                    }
                    System.out.println("client>" + est);
                }
                catch(ClassNotFoundException classnot){
                    System.err.println("Data received in unknown format");
                }
            }while(isRunning);
        }
        catch(IOException ioException){
            ioException.printStackTrace();
        }
        finally{
            //4: Closing connection
            try{
                in.close();
                out.close();
                providerSocket.close();
            }
            catch(IOException ioException){
                ioException.printStackTrace();
            }
        }
    }

    public void sendItemForForecast(String itemDescription)
    {
        try{
            out.writeObject(itemDescription);
            out.flush();
            System.out.println("server>" + itemDescription);
        }
        catch(IOException ioException){
            ioException.printStackTrace();
        }
    }

    public void sendStopAction()
    {
        try{
            out.writeObject(STOP);
            out.flush();
            System.out.println("server>" + STOP);
        }
        catch(IOException ioException){
            ioException.printStackTrace();
        }
    }

    public void stopServer() {
        isRunning = false;
    }

}