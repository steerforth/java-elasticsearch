package com.steer.javaelasticsearch.util;

import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ResourceFileUtil {

    public String getPath(){
        String path = this.getClass().getClassLoader().getResource("").getPath();
        return path;
    }

    public static String getJson(String filename){
        ClassPathResource resource = new ClassPathResource("es/"+filename);
        StringBuilder builder = new StringBuilder();
        try (InputStream inputStream = resource.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return builder.toString();
    }
}
