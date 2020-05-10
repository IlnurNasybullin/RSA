package clients;

import keyGenerator.PrivateKey;
import keyGenerator.PublicKey;
import keyGenerator.RSAKeyGenerator;
import server.Server;

import java.io.*;
import java.math.BigInteger;
import java.net.Socket;
import java.util.Scanner;

public class Client extends Thread {

    private Scanner scanner;
    private PrintStream writeStream;

    private PublicKey publicKey;
    private PrivateKey privateKey;

    private String name;
    private String userName;

    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    private static final int MIN_LENGTH = 128;

    public Client(InputStream readStream, PrintStream writeStream) {
        this.scanner = new Scanner(readStream);
        this.writeStream = writeStream;
    }

    @Override
    public void run() {
        initializeClientName();
        generateKeys();
        connectWithServer();
        sendDataToServer();
        getDataFromServer();

        startDialogue();
    }

    private void startDialogue() {
        try {
            String message = (String) in.readObject();
            writeStream.println(message);

            int option;
            BigInteger[] encryptedMessage;
            scanner.nextLine();
            while (true) {
                option = in.readInt();

                if (option == Server.READ_OPTION) {
                    encryptedMessage = (BigInteger[]) in.readObject();
                    message = privateKey.decrypt(encryptedMessage);
                    writeStream.println(message);
                }

                if (option == Server.WRITE_OPTION) {
                    writeStream.print("[Ввод сообщения]: ");
                    writeStream.flush();

                    message = scanner.nextLine();

                    message += "(от " + name + ")";
                    encryptedMessage = publicKey.encrypt(message);
                    out.writeObject(encryptedMessage);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void getDataFromServer() {
        writeStream.println("Ожидание данных от сервера");
        try {
            userName = (String) in.readObject();
            publicKey = (PublicKey) in.readObject();

            writeStream.println("Получены данные от сервера");
            writeStream.println("Установлена безопасная связь с пользователем " + userName);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void sendDataToServer() {
        writeStream.println("Отправление данных серверу");
        try {
            out.writeObject(name);
            out.writeObject(publicKey);
            writeStream.println("Данные успешно отправлены");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void connectWithServer() {
        writeStream.println("Подключение к серверу");
        try {
            socket = new Socket("localhost", 8080);
            in = new ObjectInputStream(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());

            writeStream.println("Подключение к серверу успешно завершено");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initializeClientName() {
        writeStream.println("Введите ваше имя: ");
        name = scanner.next();
    }

    private void generateKeys() {
        writeStream.println("Для безопасного обмена данных необходимо сгенерировать ключи шифрования и расшифрования.");
        writeStream.print("Выберите длину простых чисел, на основе которых будут построены ключи RSA (минимальная длина - "+ MIN_LENGTH + "): ");

        int length;
        do {
            length = scanner.nextInt();
            if (length <= 0) {
                System.err.print("Ошибка! Нельзя сгенерировать простое число данной длины! Введите длину чисул ещё раз: ");
            }
        } while (length < MIN_LENGTH);

        RSAKeyGenerator keyGenerator = new RSAKeyGenerator();
        keyGenerator.generateKeys(length, length);

        privateKey = keyGenerator.getPrivateKey();
        publicKey = keyGenerator.getPublicKey();

        writeStream.println("Ключи успешно сгенерированы!");
    }
}
