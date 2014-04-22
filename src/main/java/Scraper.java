/**
 * A web scraper that uses input from CSV files placed in the same directory as the
 * application. Outputs a CSV file with results.
 *
 * The web scraper has a specific purpose to search a div with id HLMFfooter for strings
 * that the input CSV file specifies. This is to check for a specific issue where HTML
 * is generated with errors in production environment.
 *
 * All CSV files are separated by line breaks, one value per line, no quotes.
 * url.csv file is used to obfuscate site I'm searching for privacy reasons.
 * input.csv and idNames are required because sites are formatted as:
 * https://xxx.xxx.com/_unique_id_/yyyy
 *
 * External Dependency: jsoup 1.7.3 http://jsoup.org/download
 *
 * Required files:
 * url.csv - The URL to scan. For intended purpose, this app uses a URL on line 1, appends
 *      the path from identifier.csv, and then (optionally) appends the 2nd line from url.csv.
 * input.csv - List of identifiers/paths concatenated to URL in url.csv.
 * checklist.csv - List of strings to search for and return PASS if they are not present,
 *      FAIL if they are present.
 * output.csv - Results of scraping are printed to this file
 */

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Scraper {

    private Document doc;   // Stores HTML for parsing
    private Printer printer;    // Printer object for output
    private Scanner checkScanner;   // Scans string checklist CSV
    private Scanner inputScanner;   // Scans input CSV
    private Scanner urlScanner;     // Scans url CSV
    private ArrayList<String> idNames;  // ArrayList from inputScanner
    private ArrayList<String> checkList;    // ArrayList from checkScanner
    private ArrayList<String> failed;   // ArrayList of failed checks
    private ArrayList<String> passed;   // ArrayList of passed checks
    private ArrayList<String> urlInput; // ArrayList of URL to parse

    /**
     * Constructor for Scraper
     */
    private Scraper() throws IOException {
        printer = new Printer();
        idNames = new ArrayList();
        checkList = new ArrayList();
        failed = new ArrayList();
        passed = new ArrayList();
        urlInput = new ArrayList();
        if(checkForFiles()) {
            inputScanner = new Scanner(new File("../input.csv"));   // Sets input CSV file for URLs
            checkScanner = new Scanner(new File("../checklist.csv"));   // Sets CSV list of strings to check
            urlScanner = new Scanner(new File("../url.csv"));   // Sets URL file to scan
            populateList(inputScanner, idNames);    // Loads input.csv to idNames ArrayList
            populateList(checkScanner, checkList);  // Loads checklist.csv to checkList ArrayList
            populateList(urlScanner, urlInput);     // Loads url.csv to urlInput ArrayList
            parseSite();
            sendResults();
        }
        else {
            System.out.println("ERROR: Please check app directory for required files:\n" +
                    "input.csv\n" +
                    "checklist.csv\n" +
                    "url.csv");
        }
    }

    /**
     * Main method. Creates scraper object
     *
     * @param args
     */
    public static void main(String[] args) throws IOException {
        Scraper scraper = new Scraper();
    }

    /**
     * Check app directory for required input files.
     *
     * @return Returns true if input files are present, false if they are not
     */
    private Boolean checkForFiles() throws FileNotFoundException {
        return (new File("../input.csv").isFile() && new File("../checkList.csv").isFile() && new File("../url.csv").isFile());
    }

    /**
     * Populate idName ArrayList from input.csv
     *
     * @param listFile Scanner object (csv file) to pull values from
     * @param popList ArrayList to add values from csv file
     */
    private void populateList(Scanner listFile, ArrayList popList) {
        while (listFile.hasNext()) {
            popList.add(listFile.next());
        }
        listFile.close();
    }

    /**
     * Concatenates values from urlInput and idNames ArrayLists to form URL and get footer.
     * Passes text from footer to checkString() method to determine whether strings from
     * checkList are in footer. Based on Boolean returned from checkString, will add idName
     * to passed or failed list.
     *
     * @throws IOException
     */
    private void parseSite() throws IOException {
        for(String id : idNames) {
        doc = Jsoup.connect(urlInput.get(0) + id + urlInput.get(1)).get();
        Elements footer = doc.select("div#HLMFfooter");
        String footerText = footer.text();
        if(checkString(footerText)) {
            passed.add(id);
        }
            else {
            failed.add(id);
        }
        }
    }

    /**
     * Checks text for strings from checkList
     *
     * @param parsedText String to be searched for occurences of strings in checkList
     * @return True if string does not contain strings from checkList
     */
    private boolean checkString(String parsedText) {
        for(String checkChar : checkList) {
            if(parsedText.toLowerCase().contains(checkChar.toLowerCase())) {
                return false;
            }
        }
        return true;
    }

    /**
     * Sends results of parsing to printer object so it can output to the console
     * and output file. Starts with failed checks first.
     */
    private void sendResults() {
        printer.startFile();    // Prints start day/time to output file
        for(String id : failed) {
            printer.printFailed(id);
        }
        for(String id : passed) {
            printer.printPassed(id);
        }
        printer.endFile();      // Prints finish day/time to output file
    }
}