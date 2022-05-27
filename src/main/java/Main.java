import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {

        String pathName = "pdfs";

        BooleanSearchEngine engine = new BooleanSearchEngine(new File(pathName));

        int port = 8989;

        try (ServerSocket serverSocket = new ServerSocket(port)) {

            while (true) {

                try (Socket socket = serverSocket.accept();
                        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

                    System.out.println("Подключение на порт: " + socket.getPort());

                    out.println("Введите слово для поиска: ");
                    out.flush();
                    String word = in.readLine();

                    List<PageEntry> list = engine.search(word);

                    GsonBuilder builder = new GsonBuilder();
                    Gson gson = builder.setPrettyPrinting().create();

                    out.println(list.isEmpty() ?
                            "Слово " + "\"" + word + "\"" + " не встретилось при поиске!" :
                            "Результаты поиска слова " + "\"" + word + "\"" + ":");

                    for (PageEntry pageEntry : list) {
                        out.println(gson.toJson(pageEntry));
                    }

                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }

            }

        } catch (IOException e) {
            System.out.println("Не могу стартовать сервер");
            e.printStackTrace();
        }
    }
}
