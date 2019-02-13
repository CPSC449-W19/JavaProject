package IO;

import java.io.*;

public class Output {

    private String fileName;
    private PrintWriter printWriter;

    public Output(String fileName) {
        this.fileName = fileName;
        try  {
            File file = new File(this.fileName);
            if (file.createNewFile())
                this.printWriter = new PrintWriter(new File(this.fileName));
            else this.printWriter = new PrintWriter(new File(this.fileName));
        } catch (IOException iOException) {
            System.out.println("From Constructor");
            iOException.printStackTrace();
        }
    }

    public void write(String line) {
        printWriter.write(line);
        printWriter.flush();
    }

    public void close() {
        printWriter.close();
    }
}
