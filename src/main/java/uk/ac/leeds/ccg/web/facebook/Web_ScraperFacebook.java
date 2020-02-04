/*
 * Copyright 2017 Andy Turner, University of Leeds.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.ac.leeds.ccg.web.facebook;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import uk.ac.leeds.ccg.data.core.Data_Environment;
import uk.ac.leeds.ccg.generic.core.Generic_Environment;
import uk.ac.leeds.ccg.generic.io.Generic_Defaults;
import uk.ac.leeds.ccg.generic.io.Generic_IO;
import uk.ac.leeds.ccg.web.io.Web_Scraper;
import uk.ac.leeds.ccg.web.core.Web_Environment;

public class Web_ScraperFacebook extends Web_Scraper {

    public Web_ScraperFacebook(Web_Environment e) {
        super(e);
    }

    /**
     * Main method
     *
     * @param args
     */
    public static void main(String[] args) {
        try {
            Web_ScraperFacebook p = new Web_ScraperFacebook(new Web_Environment(
                    new Data_Environment(new Generic_Environment(
                            new Generic_Defaults()))));
            p.run(args);
        } catch (Exception ex) {
            ex.printStackTrace(System.err);
        }
    }

    public void run(String[] args) throws IOException {
        url = "http://www.facebook.com/search.php?init=dir&q=BNP&type=groups#!/group.php?gid=280968101201";
        getFacebookContacts();
    }

    public void getFacebookContacts() throws IOException {
        dir = Paths.get(env.de.files.getDataDir().toString(), "web", "Facebook");
        Files.createDirectories(dir);
        Path outputFile = Paths.get(dir.toString(), "Test.html");
        Files.createFile(outputFile);
        PrintWriter logPrintWriter;
        try (PrintWriter outputPW = Generic_IO.getPrintWriter(outputFile, false)) {
            Path logFile = Paths.get(dir.toString(), "Test.log");
            Files.createFile(logFile);
            logPrintWriter = Generic_IO.getPrintWriter(logFile, false);
            getHTML(outputPW);
        }
        logPrintWriter.close();
    }

    /**
     * @param pw
     */
    public void getHTML(PrintWriter pw) {
        try {
            HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
            con.setRequestMethod("GET");
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String line;
            while ((line = br.readLine()) != null) {
                pw.write(line);
            }
        } catch (MalformedURLException ex) {
            ex.printStackTrace(System.err);
            env.env.log(ex.getMessage());
        } catch (IOException ex) {
            ex.printStackTrace(System.err);
            env.env.log(ex.getMessage());
        }
    }
}
