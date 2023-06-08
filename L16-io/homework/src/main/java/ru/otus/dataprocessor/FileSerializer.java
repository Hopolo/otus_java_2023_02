package ru.otus.dataprocessor;

import com.google.common.reflect.TypeToken;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Paths;
import java.util.Map;

public class FileSerializer implements Serializer {

    private final String fileName;

    public FileSerializer(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void serialize(Map<String, Double> data) {
        //формирует результирующий json и сохраняет его в файл
        Type mapMeasurements = new TypeToken<Map<String, Double>>() {
        }.getType();
        var gson = new GsonBuilder()
            .registerTypeAdapter(mapMeasurements, new MeasurementSerializer())
            .create();

        String outputJson = gson.toJson(data, mapMeasurements);
        try (FileWriter fileWriter = new FileWriter(new File(Paths.get(fileName).toUri()))) {
            fileWriter.write(outputJson);
        } catch (IOException e) {
            throw new FileProcessException("Ошибка записи json");
        }
    }
}
