package server.cli.commands;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import server.database.Database;
import server.exceptions.NoSuchKeyException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class GetCommand implements Command{

    private final JsonElement key;
    private JsonElement result;

    public GetCommand(JsonElement key) {
        this.key = key;
    }

    public final JsonElement getResult() {
        return result;
    }

    @Override
    public void execute() {
        result = Database.INSTANCE.get(key);
    }
}
