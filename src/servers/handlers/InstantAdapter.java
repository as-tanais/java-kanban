package servers.handlers;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class InstantAdapter extends TypeAdapter<Instant> {
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy;HH:mm").withZone(ZoneId.systemDefault());
    @Override
    public void write(JsonWriter jsonWriter, Instant instant) throws IOException {
        jsonWriter.value(instant.toEpochMilli());
    }

    @Override
    public Instant read(JsonReader jsonReader) throws IOException {
        return Instant.parse(jsonReader.nextString());
    }

}

