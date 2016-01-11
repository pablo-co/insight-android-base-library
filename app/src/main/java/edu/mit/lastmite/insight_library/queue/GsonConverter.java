package edu.mit.lastmite.insight_library.queue;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.squareup.tape.FileObjectQueue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;

import edu.mit.lastmite.insight_library.error.ParseError;

/**
 * Use GSON to serialize classes to a bytes.
 *
 * This variant of {@link GsonConverter} works with anything you throw at it.
 * It is however important for Gson to be able to understand your inner complex objects/entities
 * Use an InterfaceAdapter for these purposes.
 *
 */
public class GsonConverter<T>  implements FileObjectQueue.Converter<T> {
    public static final String CONCRETE_CLASS_NAME = "concrete_class_name";
    public static final String CONCRETE_CLASS_OBJECT = "concrete_class_object";
    private final Gson _gson;

    public GsonConverter(Gson gson) {
        _gson = gson;
    }

    @Override
    public T from(byte[] bytes) {
        Reader reader = new InputStreamReader(new ByteArrayInputStream(bytes));
        JsonObject completeAbstractClassInfoAsJson = _gson.fromJson(reader, JsonObject.class);

        Class<T> klass;
        try {
            String className = completeAbstractClassInfoAsJson.get(CONCRETE_CLASS_NAME).getAsString();
            klass = (Class<T>) Class.forName(className);
        } catch (Exception e) {
            throw new ParseError(e.getMessage());
        }

        String objectDataAsString = completeAbstractClassInfoAsJson.get(CONCRETE_CLASS_OBJECT)
                .getAsString();

        return _gson.fromJson(objectDataAsString, klass);
    }

    @Override
    public void toStream(T object, OutputStream bytes) throws IOException {
        Writer writer = new OutputStreamWriter(bytes);

        JsonObject completeAbstractClassInfoAsJson = new JsonObject();
        completeAbstractClassInfoAsJson.addProperty(CONCRETE_CLASS_NAME, object.getClass().getName());
        completeAbstractClassInfoAsJson.addProperty(CONCRETE_CLASS_OBJECT, _gson.toJson(object));

        _gson.toJson(completeAbstractClassInfoAsJson, writer);
        writer.close();
    }
}