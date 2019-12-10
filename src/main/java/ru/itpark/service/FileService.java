package ru.itpark.service;

// F4 on project ->

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Part;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

public class FileService {
    private final String uploadPath;

    public FileService() throws IOException {
        uploadPath = System.getenv("UPLOAD_PATH");  //ПОЛУЧАЕТ ЗНАЧЕНИЕ ПЕРЕМЕННОЙ СРЕДЫ в виде адреса "с:/Users/..."
        Files.createDirectories(Paths.get(uploadPath));   //СОЗДАЕТ ДИРЕКТОРИЮ С НАЗВАНИЕМ UPLOAD_PATH
    }

    public void readFile(String id, ServletOutputStream os) throws IOException {
        var path = Paths.get(uploadPath).resolve(id);   //путь
        Files.copy(path, os);
    }

    public String writeFile(Part part) throws IOException {  //метод для записи файла полученного из формы
        var id = UUID.randomUUID().toString();
        part.write(Paths.get(uploadPath).resolve(id).toString());  //записать файл по указанному пути
        return id;
    }
}
