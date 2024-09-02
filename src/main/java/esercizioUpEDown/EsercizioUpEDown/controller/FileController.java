package esercizioUpEDown.EsercizioUpEDown.controller;


import esercizioUpEDown.EsercizioUpEDown.service.FileStorageService;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/file")
public class FileController {

    @Autowired
    private FileStorageService fileStorageService;

    // Endpoint per il caricamento del file
    @PostMapping("/upload")
    public String upload(@RequestParam MultipartFile file) throws IOException {
        return fileStorageService.upload(file);
    }

    // Endpoint per il download del file
    @GetMapping("/download")
    public byte[] download(@RequestParam String fileName, HttpServletResponse response) throws IOException {
        // Determina il tipo di contenuto basato sull'estensione del file
        String extension = FilenameUtils.getExtension(fileName);
        switch (extension) {
            case "gif":
                response.setContentType(MediaType.IMAGE_GIF_VALUE);
                break;
            case "jpg":
            case "jpeg":
                response.setContentType(MediaType.IMAGE_JPEG_VALUE);
                break;
            case "png":
                response.setContentType(MediaType.IMAGE_PNG_VALUE);
                break;
            default:
                response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE); // Default content type for files
        }

        // Imposta l'header per il download
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

        // Chiama il servizio per ottenere il file in byte array
        return fileStorageService.download(fileName);
    }
}

