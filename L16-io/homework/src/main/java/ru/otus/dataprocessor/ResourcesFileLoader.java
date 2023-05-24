package ru.otus.dataprocessor;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import ru.otus.model.Measurement;

public class ResourcesFileLoader implements Loader {

    private final String fileName;

    public ResourcesFileLoader(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public List<Measurement> load() {
        //читает файл, парсит и возвращает результат
        var gson = new Gson();
        List<Measurement> measurements;
        try (var input = new BufferedInputStream(ResourcesFileLoader.class.getClassLoader().getResourceAsStream(fileName))) {
            var json = new String(input.readAllBytes());
            Type listMeasurements = new TypeToken<List<Measurement>>() {
            }.getType();
            measurements = gson.fromJson(json, listMeasurements);
        } catch (IOException e) {
            throw new FileProcessException("Ошибка чтения json");
        }
        return measurements;
    }
}
