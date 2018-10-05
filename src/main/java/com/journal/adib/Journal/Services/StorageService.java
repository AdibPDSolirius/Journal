package com.journal.adib.Journal.Services;

import com.journal.adib.Journal.ErrorHandling.JournalException;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Path;
import java.util.UUID;
import java.util.stream.Stream;

@Service
public class StorageService {

    String absoluteStoragePath = "/home/adib/Documents/JournalImageUploads";

    public String store(MultipartFile file){
        String newFileName = "";
        try{
            newFileName = file.getOriginalFilename() + UUID.randomUUID();
            file.transferTo(new File(absoluteStoragePath + "/" + newFileName));
        }catch(IOException ioe){

        }
        return newFileName;
    }

    public byte[] getFile(String filename) {
        InputStream in = null;
        try {
            in = new FileInputStream(new File(absoluteStoragePath + "/" + filename));
            byte[] b = IOUtils.toByteArray(in);
            return b;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean deleteFile(String filename){
        File file = new File(absoluteStoragePath + "/" + filename);
        return file.delete();
    }

}