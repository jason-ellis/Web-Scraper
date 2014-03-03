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

    private FileWriter writer;

    public Printer() throws IOException {
        writer = new FileWriter("../output.csv"); // create new FileWriter and file to write to
    }

    public void startFile() {
        printToFile("Parsing started at: " + getDateTime());
        System.out.println("Parsing started at: " + getDateTime());
    }

    public void endFile() {
        printToFile("Parsing ended at: " + getDateTime());
        System.out.println("Parsing ended at: " + getDateTime());
    }

    private void printToFile(String text) {
        try {
            writer.append(text + "\n");
            writer.flush();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void printFailed(String failed) {
        System.out.println("FAIL: " + failed);
        printToFile("FAIL: " + failed);
    }

    public void printPassed(String passed) {
        System.out.println("PASS: " + passed);
        printToFile("PASS: " + passed);
    }

    private String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }
}
