package utility;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.Instant;

public class JsonCreater {
    public static Gson createGson() {

        return new GsonBuilder()
                .registerTypeAdapter(Instant.class, new InstantAdapter())
                .create();
    }
}
