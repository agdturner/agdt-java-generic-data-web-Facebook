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
package uk.ac.leeds.ccg.andyt.generic.data.web.facebook;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import uk.ac.leeds.ccg.andyt.data.core.Data_Environment;
import uk.ac.leeds.ccg.andyt.web.io.Web_Scraper;
import uk.ac.leeds.ccg.andyt.web.core.Web_Environment;

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
                    new Data_Environment()));
            p.run(args);
        } catch (IOException ex) {
            ex.printStackTrace(System.err);
        }
    }

    public void run(String[] args) throws IOException {
        url = "http://www.facebook.com/search.php?init=dir&q=BNP&type=groups#!/group.php?gid=280968101201";
        getFacebookContacts();
    }

    public void getFacebookContacts() throws IOException {
        dir = new File(env.de.files.getDataDir(), "web");
        dir = new File(dir, "web");
        dir = new File(dir, "Facebook");
        dir.mkdirs();
        File outputFile = new File(dir, "Test.html");
        outputFile.getParentFile().mkdirs();
        outputFile.createNewFile();
        PrintWriter logPrintWriter;
        try (PrintWriter outputPW = env.env.io.getPrintWriter(outputFile, false)) {
            File logFile = new File(dir, "Test.log");
            logFile.createNewFile();
            logPrintWriter = env.env.io.getPrintWriter(logFile, false);
            getHTML(outputPW);
        }
        logPrintWriter.close();
    }

    /**
     * @param pw
     */
    public void getHTML(PrintWriter pw) {
        HashSet r = new HashSet();
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
