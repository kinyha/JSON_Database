package server;


import com.google.gson.Gson;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;


public class Main {

    private static final int PORT = 23456;
    private static final String address = "127.0.0.1";
    private static final String DB_PATH = System.getProperty("user.dir") + "/JSON Database/task/src/server/data/db.json";
    private static final Path PATH = Path.of(DB_PATH);


    public static void main(String[] args) {

        try (ServerSocket server = new ServerSocket(PORT, 50, InetAddress.getByName(address))) {
            System.out.println("Server started!");
            ExecutorService executor = Executors.newFixedThreadPool(4);
            AtomicBoolean running = new AtomicBoolean(true);


            while (!Thread.interrupted() && running.get()) {
                Socket socket = server.accept(); // accepting a new client

                executor.submit(() -> {

                    try (
                            socket; // accepting a new client
                            DataInputStream input = new DataInputStream(socket.getInputStream());
                            DataOutputStream output = new DataOutputStream(socket.getOutputStream())
                    ) {
                        Gson gson = new Gson();
                        DataBase dataBase = new DataBase();
                        ResponseHandler responseHandler = new ResponseHandler(dataBase, gson);

                        String msg = input.readUTF();
                        System.out.println("Received: " + msg);

                        Request request = responseHandler.getRequest(msg);
                        String jsonResponse = responseHandler.getJsonResponse(request);
                        output.writeUTF(jsonResponse);
                        System.out.println("Sent: " + jsonResponse);

                        if ("exit".equals(request.getType())) {
                            running.set(false);
                            server.close();
                            executor.shutdown();
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                Files.delete(PATH);
                Files.createFile(PATH);
                Files.write(PATH, "{}".getBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

}