package com.lab3.server;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@RestController
public class Controller {


    @PostMapping("/save/")
    public ResponseEntity<String> saveFile(@RequestParam MultipartFile file){

        try{
            Path fileStorageLocation = Paths.get("./uploads").toAbsolutePath().normalize();
            Files.createDirectories(fileStorageLocation);
            Path targetLocation = fileStorageLocation.resolve(file.getOriginalFilename());
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
        }
        catch (IOException e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Could not save file");
        }
        return ResponseEntity.ok().body("Success");
    }

    @GetMapping("/retrieve/{fileName}")
    public ResponseEntity<File> retrieveFile(@PathVariable String fileName){
        File file = new File(Paths.get("./uploads").toAbsolutePath().normalize() + fileName);
        return ResponseEntity.ok().body(file);
    }
}
