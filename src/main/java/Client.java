import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        String host = "127.0.0.1";
        int port = 8989;
        Scanner scanner = new Scanner(System.in);

        while(true) {

            try (Socket clientSocket = new Socket(host, port);
                 PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                 BufferedReader in = new BufferedReader(new
                         InputStreamReader(clientSocket.getInputStream()))) {

                System.out.println(in.readLine());

                String wordInput = scanner.nextLine();
                String word = wordInput.toLowerCase();
                out.println(word);

                System.out.println(in.readLine());

                while (true) {

                    String message = in.readLine();
                    if (message == null) break;
                    System.out.println(message);
                }

            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}