package org.drincruz.cli;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Parser;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import au.com.bytecode.opencsv.CSVReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * CSVRead
 */
public class CSVRead {
    // Private
    private static String csvfile;
    private static String[] csvColumns = null;

    /**
     * Set up cli options
     */
    private static Options CLIOptions() {
        final Options options = new Options();
        options.addOption("f", "file", true, "csv file");
        options.addOption("c", "columns", true, "Comma separated columns to display");
        options.addOption("h", "help", false, "Show help");
        return options;
    }

    /**
     * Prints help usage
     * @param Options options
     * @return void
     */
    private static void printHelpUsage(final Options options) {
        final HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("org.drincruz.cli.CSVRead", options);
    }

    /**
     * Processes any cli arguments
     * @param CommandLine cli
     * @throws IllegalArgumentException
     * @return void
     */
    private static void processArgs(final CommandLine cli) throws IllegalArgumentException {
        if (cli.hasOption("file")) {
            csvfile = cli.getOptionValue("file");
            System.out.println("Filename: " + csvfile);
        }
        if (cli.hasOption("columns")) {
            String cliColumns = cli.getOptionValue("columns");
            csvColumns = cliColumns.split(",");
        }
    }

    /**
     * Prints csv file
     * @param String file
     * @param String[] columns
     * @return void
     */
    private static void printCSVFile(String file, String[] columns) {
        CSVReader reader = null;
        try {
            reader = new CSVReader(new FileReader(file));
        }
        catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        }
        try {
            String[] nextLine;
            // if specific columns
            if (null != columns) {
                while ((nextLine = reader.readNext()) != null) {
                    for (String col: columns) {
                        System.out.printf("%s ", nextLine[Integer.parseInt(col)]);
                    }
                    System.out.println();
                }
            }
            // else, print all columns
            else {
                while ((nextLine = reader.readNext()) != null) {
                    for (int i=0; i<nextLine.length; i++) {
                        System.out.printf("%s ", nextLine[i]);
                    }
                    System.out.println();
                }
            }
        }
        catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Main
     */
    public static void main(String[] args) {
        final Parser cliParser = new PosixParser();
        final Options options = CLIOptions();
        CommandLine cli = null;
        try {
            cli = cliParser.parse(options, args, true);
        }
        catch (final ParseException e) {
            System.err.println(e.getMessage());
        }
        try {
            if (null != cli) {
                processArgs(cli);
            }
        }
        catch (final IllegalArgumentException e) {
            printHelpUsage(options);
            System.err.println(e.getMessage());
            return;
        }
        if ( ((null != cli) && (cli.hasOption("help"))) || (1 > args.length) ) {
            printHelpUsage(options);
            return;
        }
        // process
        printCSVFile(csvfile, csvColumns);
    }
}
