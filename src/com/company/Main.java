// David Crawford

package com.company;
import java.io.*;
import java.net.URL;
import java.net.HttpURLConnection;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        String url = "http://www.cis.gvsu.edu/~dulimarh/demo.html";

        try {

            // Initial connection

            URL obj = new URL(url);
            HttpURLConnection httpClient = (HttpURLConnection) obj.openConnection();
            httpClient.setRequestMethod("GET");
            int responseCode = httpClient.getResponseCode();

            // Builds the response header section

            StringBuilder builder = new StringBuilder();
            builder.append(responseCode)
                    .append(" ")
                    .append(httpClient.getResponseMessage())
                    .append("\n");

            Map<String, List<String>> map = httpClient.getHeaderFields();
            for (Map.Entry<String, List<String>> entry : map.entrySet()) {
                if (entry.getKey() == null)
                    continue;
                builder.append(entry.getKey())
                        .append(": ");

                List<String> headerValues = entry.getValue();
                Iterator<String> it = headerValues.iterator();
                if (it.hasNext()) {
                    builder.append(it.next());

                    while (it.hasNext()) {
                        builder.append(", ")
                                .append(it.next());
                    }
                }
                builder.append("\n");
            }

            // Builds the content section

            String result = "";
            if(responseCode == 200) {
                InputStream in = new BufferedInputStream(httpClient.getInputStream());
                if (in != null) {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
                    String line = "";
                    while ((line = bufferedReader.readLine()) != null)
                        result += (line + "\n");
                }
                in.close();
            }
            else if(String.valueOf(responseCode).startsWith("4")) {
                result = "Resource not found";
            }

            // Output

            System.out.println("Headers:\n");
            System.out.println(builder);
            //System.out.println("Result:\n");
            //System.out.println(result);

            /*try (PrintWriter payload = new PrintWriter(new FileOutputStream("payload.txt", true))) {
                payload.println(builder);
                payload.close();
            }
            catch (Exception e) {
                System.out.println(e);
            }
            */
            try (PrintWriter payload = new PrintWriter(new FileOutputStream("response.txt", true))) {
                payload.println(result);
                payload.close();
            }
            catch (Exception e) {
                System.out.println(e);
            }
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }
}