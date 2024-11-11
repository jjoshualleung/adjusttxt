package edu.gatech.seclass.adjusttxt;
import java.util.ArrayList;
public class Main {
    // Empty Main class for compiling Individual Project
    // During Deliverable 1 and Deliverable 2, DO NOT ALTER THIS CLASS or implement it

    public static void main(String[] args) {
        // if argument is empty --> print error message
        if (args.length == 0) {
            usage();
        }

        // if not empty --> Create an array list to store the args
        ArrayList<String> argsList = new ArrayList<>();

        // Add each of the argument to the list
        for (int i = 0; i < args.length; i++) {
            argsList.add(args[i]);
        }

    }

    private static void usage() {
        System.err.println(
                "Usage: adjusttxt [ -s number | -w spacing | -x | -r target | -p prefix ] FILE");
    }
}
