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
import java.nio.file.Paths;
import java.util.UUID;
import java.util.stream.Stream;


@Service
public class StorageService {

    String absoluteStoragePath = "";

    public StorageService(){
        absoluteStoragePath = System.getProperty("user.dir") + "/src/main/resources/images/";
    }

    public String store(MultipartFile file) throws JournalException{
        String newFileName = "";

        try{
            newFileName = file.getOriginalFilename() + UUID.randomUUID();
            file.transferTo(new File(absoluteStoragePath + newFileName));
        }catch(IOException ioe){
            ioe.printStackTrace();
            throw new JournalException("Could not save file", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return newFileName;
    }

    public byte[] getFile(String filename) throws JournalException{
        InputStream in = null;
        try {
            in = new FileInputStream(new File(absoluteStoragePath + filename));
            byte[] b = IOUtils.toByteArray(in);
            return b;
        } catch (Exception e) {
            throw new JournalException("Could not find file", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public boolean deleteFile(String filename) {
        File file = new File(absoluteStoragePath + filename);
        return file.delete();
    }

}