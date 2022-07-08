package server.cli.commands;

import com.google.gson.JsonElement;
import server.database.Database;
import server.exceptions.NoSuchKeyException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class DeleteCommand implements Command{

    private final JsonElement key;

    public DeleteCommand(JsonElement key) {
        this.key = key;
    }

    @Override
    public void execute() {
        Database.INSTANCE.delete(key);
    }
}
