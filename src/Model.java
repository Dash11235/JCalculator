import jdk.swing.interop.SwingInterOpUtils;

import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

public class Model {
    String deleteResult = "";

    /**
     *
     * @param input
     * @param num
     * @return
     */

    public String addNum(String input, String num){
        if(input.length() < 45){
            input += num;
        }
        return input;
    }

    /**
     *
     * @param input
     * @return
     */

    //Löcht dan zuletzt eingegebenen Char.
    public String delete(String input){

        //deleteResult wird auf "" gesetzt, damit der result davor nicht dazuaddiert wird.
        deleteResult = "";

        //Der Input wird zu einem CharArray ungewandelt, sodass man den letzen Char entfernen kann.
        char[] inputArray = input.toCharArray();

        //Sodass es zu keinem error kommt, der durch dan Versuch etwas zu löschen, obwohl nichts eingegeben wurde verursacht wird, wird geschaut, ob überhaupt etwas eingegeben wird.
        if (input != "") {
            //Das letzte Element im charArray wird durch einen Leeren char ersetzt
            inputArray[inputArray.length - 1] = ' ';
        }

        //Damit keine leeren Chars im label vorkommen, werden alle leeren Chars nicht zum deleteResult hinzugefügt.
        for(int i = 0; i < inputArray.length; i++){
            if(inputArray[i] != ' '){
                deleteResult += inputArray[i];
            }
        }
        return deleteResult;
    }

    /**
     *
     * @param input
     * @return
     */

    //Setzt alles eingegebene zurück.
    public String clear(String input){

        //Der Input wird auf 0 gestzt und daher werden alle eingegeben chars entfernt.
        input = "";
        return input;
    }

    /**
     *
     * @param input
     * @return
     */

    public String calculate(String input){
        String result = "";

        ArrayList<String> inputArrayList = new ArrayList<String>();

        for(int i = 0; i < input.length(); i++){
            inputArrayList.add(Character.toString(input.charAt(i)));
        }

        inputArrayList.removeAll(Collections.singleton(" "));

        // Detects dots and converts numbers into doubles
        for(int i = 0; i < inputArrayList.size(); i++){
            if(inputArrayList.get(i).equals(".")){
                try {
                    if(inputArrayList.get(i+1).equals("(") || inputArrayList.get(i+1).equals(")")){
                        return "error";
                    }

                    if(inputArrayList.get(i-1).equals("(") || inputArrayList.get(i-1).equals(")")){
                        return "error";
                    }
                    inputArrayList.set(i, inputArrayList.get(i-1) + inputArrayList.get(i) + inputArrayList.get(i+1));
                    inputArrayList.remove(i-1);
                    inputArrayList.remove(i);
                } catch (IndexOutOfBoundsException e) {
                    return "error";
                }
            }
        }

        if(inputArrayList.get(0).equals(")") || inputArrayList.get(inputArrayList.size()-1).equals("(")){
            return "error";
        }

        for(int i = 0; i < inputArrayList.size(); i++){
            try {
                if (i != 0) {
                    Double.parseDouble(inputArrayList.get(i));
                    Double.parseDouble(inputArrayList.get(i-1));
                    inputArrayList.set(i, inputArrayList.get(i-1)+inputArrayList.get(i));
                    inputArrayList.remove(i-1);
                    i=0;
                }else{
                    continue;
                }
            } catch (NumberFormatException e) {
                continue;
            }
        }

        // DD Kong fuckhead

        for(int i = 0; i < inputArrayList.size(); i++){
            // Try catch because there was an error when typing in "-+" eg
            try {
                if (inputArrayList.get(i).equals("-")) {
                    if (i == 0 && !(inputArrayList.get(i + 1).equals("("))) {
                        inputArrayList.set(i + 1, Double.toString(Double.parseDouble(inputArrayList.get(i + 1)) * (-1)));
                        inputArrayList.remove(i);
                        continue;
                    }
                    if ((i != 0) && inputArrayList.get(i - 1).equals("(") && !(inputArrayList.get(i + 1).equals("("))) {
                        inputArrayList.set(i + 1, Double.toString(Double.parseDouble(inputArrayList.get(i + 1)) * (-1)));
                        inputArrayList.remove(i);
                        continue;
                    }
                }
            }catch (NumberFormatException ne){
                continue;
            }
        }

        for(int i = 0; i < inputArrayList.size(); i++){
            if(inputArrayList.get(i).equals("+")){
                if(i == 0 && !(inputArrayList.get(i+1).equals("("))){
                    inputArrayList.remove(i);
                    continue;
                }
                if(i >= 2 && inputArrayList.get(i-1).equals("(") && !(inputArrayList.get(i+1).equals("("))){
                    inputArrayList.remove(i);
                    continue;
                }
            }
        }

        try {
            while (inputArrayList.contains("(")) {
                inputArrayList = brackets(inputArrayList);
            }
        }catch (NullPointerException e){
            return "error";
        }


        result = actualCalculation(inputArrayList);

        try {
            addHistory("src/Calculator/cache/cache.txt", input + " = " + result);
        }catch (IOException ioe){
            ;
        }

        try {
            cacheControl("src/Calculator/cache");
        }catch (FileNotFoundException | UnsupportedEncodingException fe){

        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     *
     * @param arrayList
     * @return
     */

    public ArrayList<String> brackets(ArrayList<String> arrayList){

        int openBracketCount = 0;
        int closedBracketCount = 0;
        int bracketPos = 0;

        if(arrayList.contains("(") && !arrayList.contains(")")){
            return null;
        }

        if (arrayList.contains(")") && !arrayList.contains("(")){
            return null;
        }

        ArrayList<String> notTo = new ArrayList<String>();
        notTo.add("+");
        notTo.add("-");
        notTo.add("*");
        notTo.add("/");
        notTo.add("%");
        notTo.add("(");
        notTo.add(")");

        for(int i = 0; i < arrayList.size(); i++){
            if(arrayList.get(i).equals("(")){
                bracketPos = i;
            }
        }

        boolean mult_flick = false;
        Double multNum = 0.0;

        if((bracketPos == 1) && !notTo.contains(arrayList.get(bracketPos-1))){
            mult_flick = true;
            multNum = Double.parseDouble(arrayList.get(bracketPos-1));
            arrayList.remove(bracketPos-1);
            bracketPos-=1;
        }

        if((bracketPos >= 2) && !notTo.contains(arrayList.get(bracketPos-1)) && arrayList.get(bracketPos-2).equals("(")){
            mult_flick = true;
            multNum = Double.parseDouble(arrayList.get(bracketPos-1));
            arrayList.remove(bracketPos-1);
            bracketPos-=1;
        }

        boolean flick = false;

        if((bracketPos == 1) && arrayList.get(bracketPos-1).equals("-")){
            flick = true;
            arrayList.remove(bracketPos-1);
            bracketPos-=1;
        }

        if((bracketPos >= 2) && arrayList.get(bracketPos-1).equals("-") && arrayList.get(bracketPos-2).equals("(")){
            flick = true;
            arrayList.remove(bracketPos-1);
            bracketPos-=1;
        }

        if((bracketPos == 1) && arrayList.get(bracketPos-1).equals("+")){
            arrayList.remove(bracketPos-1);
            bracketPos-=1;
        }

        if((bracketPos >= 2) && arrayList.get(bracketPos-1).equals("+") && arrayList.get(bracketPos-2).equals("(")){
            arrayList.remove(bracketPos-1);
            bracketPos-=1;
        }

        ArrayList<String> bracktList = new ArrayList<String>();

        for(int i = bracketPos; i < arrayList.size(); i++){
            bracktList.add(arrayList.get(i));
            if(arrayList.get(i).equals(")")){
                closedBracketCount++;
            }
            if (arrayList.get(i).equals("(")){
                openBracketCount++;
            }
            if(arrayList.get(i).equals(")") && (openBracketCount == closedBracketCount)){
                break;
            }
            if ((i == (arrayList.size()-1) && (openBracketCount != closedBracketCount))){
                return null;
            }
        }

        bracktList.remove(0);
        bracktList.remove(bracktList.size()-1);

        if(flick){
            if(bracktList.size() > 1) {
                for (int i = 0; i < bracktList.size() - 1; i++) {
                    if (!notTo.contains(bracktList.get(i))) {
                        bracktList.set(i, Double.toString(Double.parseDouble(bracktList.get(i)) * (-1)));
                    }
                }
            }else{
                for (int i = 0; i < bracktList.size(); i++) {
                    if (!notTo.contains(bracktList.get(i))) {
                        bracktList.set(i, Double.toString(Double.parseDouble(bracktList.get(i)) * (-1)));
                    }
                }
            }
        }

        if (mult_flick){
            for(int i = 0; i < bracktList.size(); i++){
                if(!notTo.contains(bracktList.get(i))){
                    bracktList.set(i, Double.toString(Double.parseDouble(bracktList.get(i)) * multNum));
                }
            }
        }

        if(bracktList.isEmpty()){
            return null;
        }

        String result = actualCalculation(bracktList);

        while(!arrayList.get(bracketPos).equals(")")){
            arrayList.remove(bracketPos);
        }

        arrayList.set(bracketPos, result);

        return arrayList;
    }

    /**
     *
     * @param arrayList
     * @return
     */

    public String actualCalculation(ArrayList<String> arrayList){
        String result = "";

        try {
            while (arrayList.contains("*")) {
                for (int i = 0; i < arrayList.size(); i++) {
                    if (arrayList.get(i).equals("*")) {
                        arrayList.set(i, Double.toString(Double.parseDouble(arrayList.get(i + 1)) * Double.parseDouble(arrayList.get(i - 1))));
                        arrayList.remove(i - 1);
                        arrayList.remove(i);
                    }
                }
            }

            while (arrayList.contains("/")) {
                for (int i = 0; i < arrayList.size(); i++) {
                    if (arrayList.get(i).equals("/")) {
                        arrayList.set(i, Double.toString(Double.parseDouble(arrayList.get(i - 1)) / Double.parseDouble(arrayList.get(i + 1))));
                        arrayList.remove(i - 1);
                        arrayList.remove(i);
                    }
                }
            }

            while (arrayList.contains("%")) {
                for (int i = 0; i < arrayList.size(); i++) {
                    if (arrayList.get(i).equals("%")) {
                        arrayList.set(i, Double.toString(Double.parseDouble(arrayList.get(i + 1)) % Double.parseDouble(arrayList.get(i - 1))));
                        arrayList.remove(i - 1);
                        arrayList.remove(i);
                    }
                }
            }

            while (arrayList.contains("+")) {
                for (int i = 0; i < arrayList.size(); i++) {
                    if (arrayList.get(i).equals("+")) {
                        if(i >=2 && arrayList.get(i-2).equals("-")){
                            arrayList.set(i, Double.toString(Double.parseDouble(arrayList.get(i - 1)) - Double.parseDouble(arrayList.get(i + 1))));
                        }else if(i >=2 && arrayList.get(i-2).equals("+")){
                            arrayList.set(i, Double.toString(Double.parseDouble(arrayList.get(i + 1)) + Double.parseDouble(arrayList.get(i - 1))));
                        }else{
                            arrayList.set(i, Double.toString(Double.parseDouble(arrayList.get(i + 1)) + Double.parseDouble(arrayList.get(i - 1))));
                        }
                        arrayList.remove(i - 1);
                        arrayList.remove(i);
                    }
                }
            }

            while (arrayList.contains("-")) {
                for (int i = 0; i < arrayList.size(); i++) {
                    if (arrayList.get(i).equals("-")) {
                        if(i >=2 && arrayList.get(i-2).equals("-")){
                            arrayList.set(i, Double.toString(Double.parseDouble(arrayList.get(i + 1)) + Double.parseDouble(arrayList.get(i - 1))));
                        }else if(i >=2 && arrayList.get(i-2).equals("+")){
                            arrayList.set(i, Double.toString(Double.parseDouble(arrayList.get(i - 1)) - Double.parseDouble(arrayList.get(i + 1))));
                        }else{
                            arrayList.set(i, Double.toString(Double.parseDouble(arrayList.get(i - 1)) - Double.parseDouble(arrayList.get(i + 1))));
                        }
                        arrayList.remove(i - 1);
                        arrayList.remove(i);
                    }
                }
            }
        }catch (NumberFormatException e){
            return "error";
        }catch (IndexOutOfBoundsException e){
            return "error";
        }

        result = arrayList.get(0);

        return result;
    }

    /**
     *
     * @param path
     * @param msg
     * @throws IOException
     */

    public void addHistory(String path, String msg) throws IOException {
        File file = new File(path);
        FileWriter fw = new FileWriter(file, true);

        fw.write(msg + "\n");
        fw.close();
    }

    /**
     *
     * @param path
     * @return
     * @throws FileNotFoundException
     */

    public ArrayList<String> readHistory(String path) throws FileNotFoundException {
        ArrayList<String> history = new ArrayList<String>();

        File file = new File(path);
        Scanner scan = new Scanner(file);

        while (scan.hasNextLine()){
            history.add(scan.nextLine());
        }

        return history;
    }

    /**
     *
     * @param pos
     * @param path
     * @return
     * @throws FileNotFoundException
     */

    public String historyItemAtIndex(int pos, String path) throws FileNotFoundException {
        ArrayList<String> resultAL = new ArrayList<String>();

        File file = new File(path);
        Scanner scan = new Scanner(file);

        for(int i = 0; i < pos+1; i++){
            resultAL.add(scan.nextLine());
        }

        String[] resultAR = resultAL.get(pos).split("=");

        return resultAR[0];
    }

    /**
     *
     * @param path
     * @throws IOException
     */

    public void cacheControl(String path) throws IOException {
        File file = new File(path + "/cache.txt");
        Scanner scan = new Scanner(file);

        ArrayList<String> strs = new ArrayList<String>();

        while (scan.hasNextLine()) {
            strs.add(scan.nextLine());
        }

        if (strs.size() > 21){

            FileWriter fw = new FileWriter(file, true);
            PrintWriter pw = new PrintWriter(file);

            strs.remove(0);

            pw.close();

            for(String s : strs){
                fw.write(s + "\n");
            }
            fw.close();
        }
    }
}