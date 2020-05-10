package server;

import keyGenerator.PublicKey;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread {
    private ObjectInputStream[] in;
    private ObjectOutputStream[] out;

    private String clientNames[];
    private PublicKey publicKey[];

    private static final int MAX_COUNT = 2;

    public static final int READ_OPTION = 0;
    public static final int WRITE_OPTION = 1;

    public Server() {
        in = new ObjectInputStream[MAX_COUNT];
        out = new ObjectOutputStream[MAX_COUNT];

        clientNames = new String[MAX_COUNT];
        publicKey = new PublicKey[MAX_COUNT];
    }

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(8080)) {
            initializeClients(serverSocket);
            sendDataToClients();
            startDialogue();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void startDialogue() {
        try {
            int index = 0;

            System.out.println("Начало пользовательского диалога");

            out[0].writeObject("Вам предоставлено право первым начать диалог");
            out[0].flush();
            out[1].writeObject("Вы начнёте диалог вторым");

            ObjectInputStream readStream = in[index];
            ObjectOutputStream writer = out[index];
            ObjectOutputStream reader = out[index + 1];

            while (true) {
                reader.writeInt(READ_OPTION);
                reader.flush();
                writer.writeInt(WRITE_OPTION);
                writer.flush();

                Object[] object = (Object[]) readStream.readObject();
                reader.writeObject(object);
                reader.flush();

                index = (index + 1) % MAX_COUNT;
                readStream = in[index];
                writer = out[index];
                reader = out[(index + 1) % MAX_COUNT];
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    private void sendDataToClients() throws IOException {
        System.out.println("Отправка данных пользователям для установки безопасного соединения");
        out[0].writeObject(clientNames[1]);
        out[0].writeObject(publicKey[1]);

        out[1].writeObject(clientNames[0]);
        out[1].writeObject(publicKey[0]);
        System.out.println("Отправка данных завершена");
    }

    private void initializeClients(ServerSocket serverSocket) throws IOException, ClassNotFoundException {
        int index = 0;
        while (index < MAX_COUNT) {
            Socket socket = serverSocket.accept();
            System.out.println("К серверу подключился " + socket.getRemoteSocketAddress());

            out[index] = new ObjectOutputStream(socket.getOutputStream());
            in[index] = new ObjectInputStream(socket.getInputStream());

            System.out.println("Получение данных от сокета " + socket.getRemoteSocketAddress());

            clientNames[index] = (String) in[index].readObject();
            publicKey[index] = (PublicKey) in[index].readObject();

            System.out.println("Получены данные от сокета " + socket.getRemoteSocketAddress());
            index++;
        }
    }

    public static void main(String[] args) throws IOException {
        new Server().start();
    }
}
