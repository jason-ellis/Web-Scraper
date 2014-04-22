import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Printer class for Web Scraper. Various methods for printing to CSV file,
 * printing to console, or both.
 */

public class Printer {

    private FileWriter writer;  // creates FileWriter to write to CSV file

    /**
     * Constructor for Printer
     *
     * @throws IOException
     */
    public Printer() throws IOException {
        writer = new FileWriter("../output.csv"); // create new FileWriter and file to write to
    }

    /**
     * Starts the output file by printing the starting date/time to CSV file and console
     */
    public void startFile() {
        printToFile("Parsing started at: " + getDateTime());
        System.out.println("Parsing started at: " + getDateTime());
    }

    /**
     * Ends the output file by printing the finish date/time to the CSV file and console
     */
    public void endFile() {
        printToFile("Parsing ended at: " + getDateTime());
        System.out.println("Parsing ended at: " + getDateTime());
    }

    /**
     * Prints the text to the output file
     *
     * @param text text that is to be printed on line of output file
     */
    private void printToFile(String text) {
        try {
            writer.append(text).append("\n");
            writer.flush();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends failed URL path/identifier to be printed in output file and console.
     * Failed URL is one that contains strings from checklist file
     *
     * @param failed String to be printed in output file and console
     */
    public void printFailed(String failed) {
        System.out.println("FAIL: " + failed);
        printToFile("FAIL: " + failed);
    }

    /**
     * Sends passed URL path/identifier to be printed in output file and console.
     * Passed URL is one that does not contain strings from checklist file
     *
     * @param passed String to be printed in output file and console
     */
    public void printPassed(String passed) {
        System.out.println("PASS: " + passed);
        printToFile("PASS: " + passed);
    }

    /**
     * Provides date and time in readable String for printing
     *
     * @return current system date and time in readable format
     */
    private String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }
}
