import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.*;
import java.io.*;
import java.nio.file.*;
import static java.nio.file.StandardOpenOption.CREATE;

public class ListMaker {
    static ArrayList<String> list = new ArrayList<>();
    static String item = "";
    static Scanner in = new Scanner(System.in);
    static boolean needsSaved = false;

    public static void main(String[] args) {
        String menuChoice = "";
        boolean confirm = false;
        boolean save = false;
        displayList();
        do {
            // PICK CHOICE
            menuChoice = SafeInput.getRegExString(in, "What would you like to do?", "[AaDdIiVvQqMmOoSsCc]");
            if (menuChoice.equalsIgnoreCase("A")) {
                addItem(in);
            } else if (menuChoice.equalsIgnoreCase("D")) {
                deleteItem();
            } else if (menuChoice.equalsIgnoreCase("I")) {
                updItem(in);
            } else if (menuChoice.equalsIgnoreCase("V")) {
                viewList();
            } else if (menuChoice.equalsIgnoreCase("M")) {
                moveItem(in);
            } else if (menuChoice.equalsIgnoreCase("O")) {
                // Forces save if an unsaved list is in existence
                if (needsSaved) {
                    System.out.println("Your file has data that needs saved. ");
                    saveList();
                    }
                openList();
            } else if (menuChoice.equalsIgnoreCase("S")) {
                saveList();
            } else if (menuChoice.equalsIgnoreCase("C")) {
                clearList();
            }
            // IF CHOICE IS QUIT, ENSURE USER WANTS TO DO SO
            else if (menuChoice.equalsIgnoreCase("Q")) {
                confirm = SafeInput.getYNConfirm(in, "Are you sure? [Y/N] ");
                // Check to make sure data has been saved first - if not, prompt save
                if (needsSaved) {
                    System.out.println("Your file has data that needs saved. If this data is not saved, it will be lost. ");
                    save = SafeInput.getYNConfirm(in, "Save your data? [Y/N]");
                    if (save) {
                        saveList();
                    } else {
                        needsSaved = false;
                        System.out.println("Quitting without saving..");
                    }
                }
                if (!confirm) {
                    menuChoice = ""; // resets menu choice if user says no
                } else {
                    System.out.println("Quitting...");
                }
            }
        }
        // KEEP GOING THROUGH MENU UNTIL USER QUITS
        while (!menuChoice.equalsIgnoreCase("Q"));
    }

    // -------------- UTILITY METHODS -------------
    private static void displayList() {
        // display current list
        for (String item : list) {
            System.out.println("Index: " + list + "Item:" + item);
        }
        System.out.println(); // extra space
        // menu options
        System.out.println("Your options are:");
        System.out.println("""
                A – Add an item to the list
                D – Delete an item from the list
                I – Insert an item into the list
                V – View (i.e. print) the list
                Q – Quit the program
                M – Move an item
                O – Open a list file from disk
                S – Save the current list file to disk
                C – Clear all elements from the current list
                """
        );
    }

    // ------------- METHODS FOR EACH OPTION -----------
    private static void addItem(Scanner pipe) {
        System.out.print("What would you like to add? ");
        if (pipe.hasNextLine()) {
            item = pipe.nextLine();
            list.add(item);
        }
        needsSaved = true; // since a change has been made, the list needs saved

    }

    private static void deleteItem() {
        int index = 0;
        for (String item : list) {
            System.out.println("Index: " + (index++) + " Item: " + item);
        }
        index = SafeInput.getRangedInt(in, "Which index would you like to delete? ", 0, list.size());
        list.remove(index);
        in.nextLine();
        needsSaved = true; // since a change has been made, the list needs saved
    }

    private static void updItem(Scanner pipe) {
        int index = 0;
        for (String item : list) {
            System.out.println("Index: " + (index++) + " Item: " + item);
        }
        index = SafeInput.getRangedInt(in, "Which index would you like to update? ", 0, list.size());
        in.nextLine();
        System.out.print("What would you like to insert? ");
        if (pipe.hasNextLine()) {
            item = pipe.nextLine();
        }
        list.add(index, item);
        needsSaved = true; // since a change has been made, the list needs saved
    }

    private static void viewList() {
        int index = 0;
        for (String item : list) {
            System.out.println("Index: " + (index++) + " Item: " + item);
        }
    }

    private static void moveItem(Scanner pipe) {
        int index = 0;
        for (String item : list) {
            System.out.println("Index: " + (index++) + " Item: " + item);
        }
        System.out.print("What item would you like to move? ");
        if (pipe.hasNextLine()) {
            item = pipe.nextLine();
            list.remove(item); // removes the item from its current location
        }
        index = SafeInput.getRangedInt(in, "Which index would you like to move this item to? ", 0, list.size());
        list.add(index, item); // adds item back at requested location
        needsSaved = true; // since a change has been made, the list needs saved
        in.nextLine();


    }

    private static void openList() {
        JFileChooser chooser = new JFileChooser(); // create JFileChooser
        Path readFile = new File(System.getProperty("user.dir")).toPath(); // look through user directory
        readFile = readFile.resolve("src"); // look through java source, not looking at a specific file
        chooser.setCurrentDirectory(readFile.toFile());
        try {
            if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) { // if the user has chosen a file, then do the following
                readFile = chooser.getSelectedFile().toPath();
                // (CREATE) means that if file does not already exist, create it
                InputStream in = new BufferedInputStream(Files.newInputStream(readFile, CREATE));
                // what we use to actually read the file, but it needs an input stream before reading (grabs file location)
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                // read line by line
                while (reader.ready()) {
                    String line = reader.readLine(); // each line is a string
                    System.out.println(line); // output each line to the user
                    list.add(line);
                }
                reader.close(); // Closes the reader once finished
            }
            else { // user didn't pick a file, close the chooser
                System.out.println("You must select a file.  Terminating...");
                System.exit(0);
            }
        } catch (IOException e) { // catch if something goes wrong
            e.printStackTrace();
        }
    }

    private static void saveList() {
        try{
            Path target = new File(System.getProperty("user.dir")).toPath(); // look through user directory
            String path = SafeInput.getNonZeroLenString(in,"What is the name of the file you want to save to? [Give the name of the file you want to create if not already made] ");
            target = target.resolve("src\\" + path + ".txt");
            OutputStream out = new BufferedOutputStream(Files.newOutputStream(target,CREATE));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out));
            for(String string : list) {
                writer.write(string, 0, string.length()); // write the record with its proper length
                writer.newLine(); // add new line after each record
            }
            writer.close();
            System.out.println("File saved! ");
        }
        catch(IOException e){
            e.printStackTrace();
        }
        needsSaved = false; // list has been saved - needsSaved is now false
    }

    private static void clearList() {
        list.clear(); // empties the entire list
        System.out.println("List has been cleared. ");
        needsSaved = true; // since a change has been made, the list needs saved
    }
}




