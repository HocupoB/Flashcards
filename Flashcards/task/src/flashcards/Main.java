package flashcards;

import java.io.*;
import java.util.*;

public class Main {
    public static StringBuilder sb = new StringBuilder();
    public static Scanner scanner = new Scanner(System.in);
    public static LinkedHashMap<String, String[]> flashCards = new LinkedHashMap<>();

    public static String importFile = "";
    public static String exportFile = "";


    public static void add(){
        print("The card:", sb);

        String term = scanner.nextLine();
        sb.append(term + "\n");
        if (flashCards.containsKey(term)) {
            print("The card \"" + term + "\" already exists.", sb);
            return;
        } else {
            print("The definition of the card:", sb);
            String definition = scanner.nextLine();
            sb.append(definition + "\n");
            ArrayList<String> answers = new ArrayList<>();
            for (Map.Entry<String, String[]> str : flashCards.entrySet()) {
                String[] values = str.getValue();
                answers.add(values[0]);
            }
            if (answers.contains(definition)) {
                print("The definition \"" + definition + "\" already exists.", sb);
                return;
            } else {
                String[] defArray = {definition, "0"};
                flashCards.put(term, defArray);
                print("The pair (\"" + term + "\":\"" + definition +
                        "\") has been added.", sb);
                return;
            }
        }
    }
    public static void remove(){
        print("Which card?", sb);
        String remove = scanner.nextLine();
        sb.append(remove + "\n");
        if (flashCards.containsKey(remove)) {
            flashCards.keySet().removeIf(key -> key.equals(remove));
            print("The card has been removed.", sb);
        } else {
            print("Can't remove \"" + remove + "\": there is no such card.", sb);
        }
        return;
    }
    public static void importFile(){
        print("File name:", sb);
        String path1 = scanner.nextLine();
        sb.append(path1 + "\n");
        File file = new File(path1);
        int count = 0;

        try (Scanner forFile = new Scanner(file)) {
            while (forFile.hasNext()) {
                String addingWords1 = forFile.nextLine();
                String[] addingWords2 = addingWords1.split(" ");
                flashCards.put(addingWords2[0], new String[]{addingWords2[1], addingWords2[2]});
                count++;
            }
            print(count + " cards have been loaded.\n", sb);
        } catch (FileNotFoundException e) {
            print("File not found.", sb);
        }

    }
    public static void log(){
        print("File name: ", sb);
        String pathToFile3 = scanner.nextLine();
        sb.append(pathToFile3 + "\n");
        File file3 = new File(pathToFile3);
        print("The log has been saved.", sb);
        try (FileWriter writer = new FileWriter(file3, true)) {
            writer.write(String.valueOf(sb));
        } catch (IOException e) {
            System.out.printf("An exception occurred %s", e.getMessage());
        }
    }

    public static void showHardest(){
        int maxValue = 0;
        ArrayList<String> hardest = new ArrayList<>();
        int[] maxValues = new int[flashCards.size()];
        int i = 0;
        for (Map.Entry<String, String[]> entry : flashCards.entrySet()) {
            String[] values = entry.getValue();
            maxValues[i] = Integer.valueOf(values[1]);
            i++;
        }
        Arrays.sort(maxValues);
        try {
            maxValue = maxValues[flashCards.size() - 1];
        } catch (Exception e) {
        }
        if (maxValue == 0) {
            print("There are no cards with errors.", sb);
        } else {
            for (Map.Entry<String, String[]> entry : flashCards.entrySet()) {
                String[] values = entry.getValue();
                if (Integer.valueOf(values[1]) == maxValue) {
                    hardest.add(entry.getKey());
                }
            }
            if (hardest.size() == 1) {
                print("The hardest card is \"" + hardest.get(0) + "\". " +
                        "You have " + maxValue + " errors answering it.", sb);
            } else {
                String answers = "";
                for (int j = 0; j < hardest.size(); j++) {
                    answers += "\"" + hardest.get(j) + "\", ";
                }
                answers = answers.substring(0, answers.length() - 2);
                print("The hardest cards are " + answers + ". " +
                        "You have " + maxValue + " errors answering them.", sb);
            }
        }
    }
    public static void exportFile(){
        print("File name:", sb);
        String path = scanner.nextLine();
        sb.append(path + "\n");
        File file2 = new File(path);
        int count2 = 0;
        try (PrintWriter printWriter = new PrintWriter(file2)) {
            for (var str : flashCards.entrySet()) {
                String[] values = str.getValue();
                printWriter.println(str.getKey() + " " + values[0] + " " + values[1]);
                count2++;
            }
        } catch (IOException e) {
            print("An exception occurred", sb);
        }
        print(count2 + " cards have been saved.\n", sb);
    }

    public static void quiz(){
        print("How many times to ask?", sb);
        String enter = scanner.nextLine();
        sb.append(enter + "\n");
        int number = Integer.parseInt(enter);
        int counter = 0;
        ArrayList<String> answers = new ArrayList<>();
        for (Map.Entry<String, String[]> str : flashCards.entrySet()) {
            String[] values = str.getValue();
            answers.add(values[0]);
        }

        while (counter < number) {
            for (Map.Entry<String, String[]> entry : flashCards.entrySet()) {
                if (counter == number) {
                    break;
                }
                print("Print the definition of \"" + entry.getKey() + "\":", sb);
                String answer = scanner.nextLine();
                sb.append(answer + "\n");
                String[] rightAnswers = entry.getValue();

                if (answer.equals(rightAnswers[0])) {
                    print("Correct!", sb);
                } else if (answers.contains(answer)) {
                    String wrongKey = "";
                    int mistake = Integer.valueOf(rightAnswers[1]);
                    mistake++;
                    rightAnswers[1] = String.valueOf(mistake);
                    for (Map.Entry<String, String[]> str : flashCards.entrySet()) {
                        String[] values = str.getValue();
                        if (answer.equals(values[0])) {
                            wrongKey = str.getKey();
                        }
                    }
                    print("Wrong. The right answer is \"" + rightAnswers[0] +
                            "\", but your definition is correct for \"" +
                            wrongKey + "\".", sb);
                } else {
                    int mistake = Integer.valueOf(rightAnswers[1]);
                    mistake++;
                    rightAnswers[1] = String.valueOf(mistake);
                    print("Wrong. The right answer is \"" + rightAnswers[0] + "\".", sb);
                }
                counter++;
            }
        }
    }

    public static void reset(){
        for (var str : flashCards.entrySet()) {
            String[] values = str.getValue();
            values[1] = String.valueOf(0);
        }
        print("Card statistics have been reset.", sb);
    }

    public static void exit(){
        print("Bye bye!", sb);
        if (!exportFile.equals("")) {
            File file2 = new File(exportFile);
            int count2 = 0;
            try (PrintWriter printWriter = new PrintWriter(file2)) {
                for (var str : flashCards.entrySet()) {
                    String[] values = str.getValue();
                    printWriter.println(str.getKey() + " " + values[0] + " " + values[1]);
                    count2++;
                }
            } catch (IOException e) {
                print("An exception occurred", sb);
            }
            print(count2 + " cards have been saved.\n", sb);
        }

    }

    public static void start(){
        boolean exit = false;
        while(!exit) {
            print("Input the action (add, remove, import, export, ask, exit, log, hardest card, reset stats):", sb);
            String choice = scanner.nextLine();
            sb.append(choice + "\n");
            switch (choice) {
                case "add":
                    add();
                    break;
                case "exit":
                    exit();
                    exit = true;
                    break;
                case "remove":
                    remove();
                    break;
                case "import":
                    importFile();
                    break;
                case "export":
                    exportFile();
                    break;
                case "log":
                    log();
                    break;
                case "hardest card":
                    showHardest();
                    break;
                case "ask":
                    quiz();
                    break;
                case "reset stats":
                    reset();
                    break;
                default:
                    print("Wrong command. Try again!", sb);
                    break;
            }
        }

    }

    public static void print(String str, StringBuilder sb) {
        System.out.println(str);
        sb.append(str + "\n");
    }
    public static void getArgs(String[] args){

        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-import")) {
                importFile = args[i + 1];
            }
            if (args[i].equals("-export")) {
                exportFile = args[i + 1];
            }
        }


        if (!importFile.equals("")) {
            File file = new File(importFile);
            int count = 0;

            try (Scanner forFile = new Scanner(file)) {
                while (forFile.hasNext()) {
                    String addingWords1 = forFile.nextLine();
                    String[] addingWords2 = addingWords1.split(" ");
                    flashCards.put(addingWords2[0], new String[]{addingWords2[1], addingWords2[2]});
                    count++;
                }
                print(count + " cards have been loaded.\n", sb);
            } catch (FileNotFoundException e) {
                print("File not found.", sb);
            }
        }
    }




    public static void main(String[] args) {
        getArgs(args);
        start();
    }

}