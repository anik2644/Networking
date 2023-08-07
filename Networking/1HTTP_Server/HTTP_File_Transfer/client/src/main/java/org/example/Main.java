package org.example;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;

public class Main {

    private static final String boundary =  "*****";
    private static final String crlf = "\r\n";
    private static final String twoHyphens = "--";

    public static void main(String[] args) throws IOException {
        System.out.println("Sending file to server with a POST REQUEST");
        sendPOST();
//
//        System.out.println("Retrieving file to server with a POST REQUEST");
//        sendGET();

    }


    // Send a file to the server
     private static void sendPOST() throws IOException {


        URL obj = new URL("http://localhost:8080/save/");
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary); // Indicates file sending


        con.setDoOutput(true); // Indicates POST
        DataOutputStream request =  new DataOutputStream(con.getOutputStream());

        // Adding a file
        File file = new File("C:\\Users\\Anik\\Downloads\\LAB 03\\LAB 03\\client\\src\\main\\resources\\image.jpg");

        String fileName  = file.getName();
        String fieldName = "file";

        request.writeBytes(twoHyphens + boundary + crlf);
        request.writeBytes("Content-Disposition: form-data; name=\"" +
                fieldName + "\";filename=\"" +
                fileName + "\"" + crlf);
        request.writeBytes(crlf);

        byte[] bytes = Files.readAllBytes(file.toPath());
        request.write(bytes);

        request.writeBytes(crlf);
        request.writeBytes(twoHyphens + boundary + twoHyphens + crlf);
        // File adding done

        request.flush();
        request.close();

        int responseCode = con.getResponseCode();
        System.out.println("POST Response Code :: " + responseCode);

        if (responseCode == HttpURLConnection.HTTP_OK) { //success
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // print result
            System.out.println(response.toString());


        } else {
            System.out.println("POST request did not work.");
        }
    }


    // Retrieve a file from the server
    private static void sendGET() throws IOException {
        String url = "http://localhost:8080/retrieve/image.jpeg";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        int responseCode = con.getResponseCode();
        System.out.println("GET Response Code :: " + responseCode);
        if (responseCode == HttpURLConnection.HTTP_OK) { // success
            String fileName = "";
            String disposition = con.getHeaderField("Content-Disposition");
            String contentType = con.getContentType();
            int contentLength = con.getContentLength();

            if (disposition != null) {
                // extracts file name from header field
                int index = disposition.indexOf("filename=");
                if (index > 0) {
                    fileName = disposition.substring(index + 10,
                            disposition.length() - 1);
                }
            } else {
                // extracts file name from URL
                fileName = url.substring(url.lastIndexOf("/") + 1, url.length());
            }

            System.out.println("Content-Type = " + contentType);
            System.out.println("Content-Disposition = " + disposition);
            System.out.println("Content-Length = " + contentLength);
            System.out.println("fileName = " + fileName);

            // opens input stream from the HTTP connection
            InputStream inputStream = con.getInputStream();
            String saveFilePath =  fileName;
//C:\\Users\\Anik\\Downloads\\LAB 03\\LAB 03\\client\\" + File.separator +
            // opens an output stream to save into file
            FileOutputStream outputStream = new FileOutputStream(saveFilePath);

            int bytesRead = -1;
            byte[] buffer = new byte[4096];
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            outputStream.close();
            inputStream.close();

            System.out.println("File downloaded");
        } else {
            System.out.println("GET request did not work.");
        }

    }

}