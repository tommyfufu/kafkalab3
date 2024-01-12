package org.example;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.io.IOException;
import java.net.MalformedURLException;

public class Main {
    public static void main(String[] args) {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        System.out.printf("Hello and welcome!");

        for (int i = 1; i <= 5; i++) {
            //TIP Press <shortcut actionId="Debug"/> to start debugging your code. We have set one <icon src="AllIcons.Debugger.Db_set_breakpoint"/> breakpoint
            // for you, but you can always add more by pressing <shortcut actionId="ToggleLineBreakpoint"/>.
            System.out.println("i = " + i);
        }
        try {
            URL myURL = new URL("http://127.0.0.1:8080");
            URLConnection myURLConnection = myURL.openConnection();
//            myURLConnection.connect();
            myURLConnection.setDoOutput(true);
            OutputStreamWriter out = new OutputStreamWriter(
                    myURLConnection.getOutputStream());
            out.write("string=");
            System.out.println("conneted");

        }
        catch (MalformedURLException e) {
            System.out.println("MalformedURLException");

        }
        catch (IOException e) {
            System.out.println(e);

        }
    }
}