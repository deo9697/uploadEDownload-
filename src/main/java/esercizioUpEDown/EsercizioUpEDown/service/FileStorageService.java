package esercizioUpEDown.EsercizioUpEDown.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;


@Service
public class FileStorageService {

    // Iniettiamo la directory di upload dal file di configurazione
    @Value("${file.upload.dir}")
    private String fileRepositoryFolder;


    public String upload(MultipartFile file) throws IOException {
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        // Nome del file originale
        String completeFileName = file.getOriginalFilename();

        File finalFolder = new File(fileRepositoryFolder);
        File finalDestination = new File(finalFolder, completeFileName);

        // Controllo se la cartella esiste, altrimenti la crea
        if (!finalFolder.exists()) {
            if (!finalFolder.mkdirs()) {
                throw new IOException("Failed to create directory " + finalFolder.getAbsolutePath());
            }
        }

        // Controlla se il file esiste già
        if (finalDestination.exists()) {
            throw new IOException("File conflict: File already exists");
        }

        // Trasferisci il file al percorso finale
        file.transferTo(finalDestination);
        return completeFileName;
    }

    public byte[] download(String fileName) throws IOException {
        Path fileFromRepository = Paths.get(fileRepositoryFolder, fileName);
        File file = fileFromRepository.toFile();
        if (!file.exists()) throw new IOException("File not found: " + fileName);
        return IOUtils.toByteArray(new FileInputStream(file));
    }
}