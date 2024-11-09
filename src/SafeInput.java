import java.util.Scanner;

public class SafeInput {
    public static String getNonZeroLenString (Scanner pipe, String prompt){
        /*
         *
         * @param pipe a Scanner opened to read from System.in
         * @param prompt for the user
         * @return a String response that is not zero length
         */
        String retString = ""; // Set this to zero length. Loop runs until it isn't
        do {
            System.out.print("\n" + prompt + ": "); // show prompt add space
            retString = pipe.nextLine();
        } while (retString.length() == 0);

        return retString;

    }
    public static int getInt (Scanner pipe, String prompt){
        int retInt = 0;
        boolean done = false;
        do{
            System.out.print(prompt + ": ");
            if (pipe.hasNextInt()){ // if integer is given
                done=true; // stops loop
                retInt = pipe.nextInt();
            }
            else{
                pipe.nextLine(); // clear input, loop again
            }

        } while (!done);

        return retInt;
    }

    public static double getDouble(Scanner pipe, String prompt){
        double retDouble= 0.0;
        boolean done = false;
        do{
            System.out.print(prompt + ": ");
            if (pipe.hasNextDouble()){
                done = true;
                retDouble= pipe.nextDouble();
            }
            else{
                pipe.nextLine();
            }

        } while(!done);

        return retDouble;
    }

    public static int getRangedInt(Scanner pipe, String prompt, int low, int high){
        int rangeInt=0;
        boolean done = false;
        do {
            System.out.print(prompt + ": ");
            if (pipe.hasNextInt()) {
                rangeInt = pipe.nextInt();
                if(rangeInt<low || rangeInt>high){
                    System.out.println("Out of range.");
                    pipe.nextLine();
                }
                else{
                    done=true;
                }
            } else {
                pipe.nextLine();
            }
        } while(!done);

        return rangeInt;
    }

    public static double getRangedDouble(Scanner pipe, String prompt, double low, double high){
        double rangeDouble =0;
        boolean done = false;
        do {
            System.out.print(prompt + ": ");
            if (pipe.hasNextDouble()) {
                rangeDouble = pipe.nextDouble();
                if(rangeDouble <low || rangeDouble >high){
                    System.out.println("Out of range.");
                    pipe.nextLine();
                }
                else{
                    done=true;
                }
            } else {
                pipe.nextLine();
            }
        } while(!done);

        return rangeDouble;
    }

    public static boolean getYNConfirm(Scanner pipe, String prompt){
        boolean retConfirm = false;
        boolean done = false;
        String entry = "";

        do{
            System.out.print(prompt + ": ");
            if (pipe.hasNextLine()) {
                entry = pipe.nextLine();
                if (entry.equalsIgnoreCase("Y")) {
                    done = true;
                    retConfirm = true;
                } else if (entry.equalsIgnoreCase("N")) {
                    done = true; // don't need to reassign boolean because already initialized as false
                }
            }

        }while(!done);


        return retConfirm;
    }

    public static String getRegExString(Scanner pipe, String prompt, String regEx){
        String retRegEx = "";
        boolean done = false;
        do{
            System.out.print(prompt + ": ");
            retRegEx = pipe.nextLine();
            if (retRegEx.matches(regEx)) {
                done = true;
            }
            else{ // runs if given empty body or String that doesn't match regEx
                System.out.println("No matches.");
            }

        }while(!done);



        return retRegEx;
    }

    public static void prettyHeader(String msg){
        for(int row=1; row<=3; row++){ // creates three rows
            if(row==2){ // if second row, then:
                for (int column = 1; column <= 3; column++){ // first three
                    System.out.print("*");
                }
                int space = (54 - msg.length())/2; // calculate necessary space -- may be very slightly uneven since not all divided values will provide whole number
                System.out.printf("%" + space + "s%s%" + space + "s", "", msg, ""); // center the text
                for (int column = 1; column <=3; column++) { // last three stars
                    System.out.print("*");
                }
                System.out.println(); // move to next line

            }
            else { // for first and third row
                for (int column = 1; column <= 60; column++) {
                    System.out.print("*");
                }
                System.out.println(); // move to next line
            }
        }
    }
}
