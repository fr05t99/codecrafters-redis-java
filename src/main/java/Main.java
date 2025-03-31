import java.io.*;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
  public static void main(String[] args){
    // You can use print statements as follows for debugging, they'll be visible when running tests.
    System.out.println("Logs from your program will appear here!");

    //  Uncomment this block to pass the first stage
        ExecutorService executor = Executors.newFixedThreadPool(5);


      ServerSocket serverSocket = null;
      Socket clientSocket = null;
      int port = 6379;
      try {
          serverSocket = new ServerSocket(port);
          // Since the tester restarts your program quite often, setting SO_REUSEADDR
          // ensures that we don't run into 'Address already in use' errors
          serverSocket.setReuseAddress(true);
          // Wait for connection from client.
          clientSocket = serverSocket.accept();
          BufferedReader in =
                  new BufferedReader(
                          new InputStreamReader(clientSocket.getInputStream()));
          String line = null;
          while ((line = in.readLine()) != null) {
              if (line.toLowerCase(Locale.ROOT).contains("ping")) {
                  final OutputStream out = clientSocket.getOutputStream();
                  executor.execute(() -> {
                      try {
                          respond(out);
                      } catch (IOException e) {
                          throw new RuntimeException(e);
                      }
                  });
              }
          }
      } catch (IOException | RuntimeException e) {
          System.out.println("IOException: " + e.getMessage());
      } finally {
          try {
              if (clientSocket != null) {
                  clientSocket.close();
              }
          } catch (IOException e) {
              System.out.println("IOException: " + e.getMessage());
          }
      }


        executor.shutdown();
  }

  public static void respond(final OutputStream out) throws IOException {
      out.write("+PONG\r\n".getBytes());
  }

}
