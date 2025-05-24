package org.example.CommandInterface;

import org.example.Tree;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class OperationMenu implements State{
    Scanner scanner;
    State nextState;
    Tree<String> tree;

    OperationMenu(Scanner scanner, Tree<String> tree) {
        this.scanner = scanner;
        this.nextState = null;
        this.tree = tree;
    }

    @Override
    public void excute() {
        System.out.println(ConsoleColors.BLUE + "Operation Command Line" + ConsoleColors.RESET);
        while (true) {
            System.out.print(">");
            String input = scanner.nextLine().trim();

            switch (checkBackOrExit(input)) {
                case 1:
                    nextState = new IntitalizeMenu(scanner);
                    return;
                case 2:
                    System.exit(0);
            }

            String[] tokens = splitInput(input);
            if (tokens == null) {
                System.out.println(ConsoleColors.RED + "Invalid Input" + ConsoleColors.RESET);
                continue;
            }

            boolean flag;
            int[] rets;
            long startTime;
            long endTime;
            long duration;

            switch (tokens[0].toLowerCase()) {
                case "insert":
                case "i":
                    flag = tree.insert(tokens[1]);
                    if (flag)
                        System.out.println(ConsoleColors.CYAN + "String inserted successfully" + ConsoleColors.RESET);
                    else
                        System.out.println(ConsoleColors.RED + "Already Existed" + ConsoleColors.RESET);
                    break;

                case "search":
                case "se":
                case "contains":
                case "c":
                    flag = tree.search(tokens[1]);
                    if (flag)
                        System.out.println(ConsoleColors.CYAN + "String exists" + ConsoleColors.RESET);
                    else
                        System.out.println(ConsoleColors.RED + "String doesn't exist" + ConsoleColors.RESET);
                    break;

                case "delete":
                case "d":
                    flag = tree.delete(tokens[1]);
                    if (flag)
                        System.out.println(ConsoleColors.CYAN + "String deleted successfully" + ConsoleColors.RESET);
                    else
                        System.out.println(ConsoleColors.RED + "String doesn't exist" + ConsoleColors.RESET);
                    break;

                case "size":
                case "si":
                    int size = tree.getSize();
                    System.out.println(ConsoleColors.CYAN + "Tree size = " + size + ConsoleColors.RESET);
                    break;

                case "height":
                case "h":
                case "depth":
                    int height = tree.getHeight();
                    System.out.println(ConsoleColors.CYAN + "Tree height = " + height + ConsoleColors.RESET);
                    break;

                case "batch_insert":
                case "bi":
                    startTime = System.nanoTime();
                    rets = insertStringsFromFile(tokens[1]);
                    endTime = System.nanoTime();
                    duration = (endTime - startTime) / 1_000_000;
                    if (rets == null) {
                        System.out.println(ConsoleColors.RED + "Invalid Path" + ConsoleColors.RESET);
                        break;
                    }
                    System.out.println(ConsoleColors.CYAN + "Added strings: " + rets[0] + ConsoleColors.RESET);
                    System.out.println(ConsoleColors.CYAN + "Existing/Duplicates strings: " + rets[1] + ConsoleColors.RESET);
                    System.out.println(ConsoleColors.CYAN + "Time taken: " + duration + " ms" + ConsoleColors.RESET);
                    break;

                case "batch_delete":
                case "bd":
                    startTime = System.nanoTime();
                    rets = deleteStringsFromFile(tokens[1]);
                    endTime = System.nanoTime();
                    duration = (endTime - startTime) / 1_000_000;
                    if (rets == null) {
                        System.out.println(ConsoleColors.RED + "Invalid Path" + ConsoleColors.RESET);
                        break;
                    }
                    System.out.println(ConsoleColors.CYAN + "Deleted strings: " + rets[0] + ConsoleColors.RESET);
                    System.out.println(ConsoleColors.CYAN + "nonExisting/Duplicates strings: " + rets[1] + ConsoleColors.RESET);
                    System.out.println(ConsoleColors.CYAN + "Time taken: " + duration + " ms" + ConsoleColors.RESET);
                    break;

                default:
                    System.out.println(ConsoleColors.RED + "Invalid Input" + ConsoleColors.RESET);
            }
        }
    }

    @Override
    public State nextState() {
        return nextState;
    }

    private int checkBackOrExit(String s) {
        if (s.equalsIgnoreCase("back"))
            return 1;
        else if (s.equalsIgnoreCase("exit"))
            return 2;
        return 0;
    }

    private String[] splitInput(String input) {
        int firstSpaceIndex = input.indexOf(" ");
        String[] result = new String[2];
        if (firstSpaceIndex != -1) {
            result[0] = input.substring(0, firstSpaceIndex); // "Insert"
            result[1] = input.substring(firstSpaceIndex + 1); // "hello i am here"
        }
        else if (validInput(input)) {
            result[0] = input;
        }
        else {
            return null;
        }
        return result;
    }

    private boolean validInput(String input) {
        return input.equalsIgnoreCase("size") ||
                input.equalsIgnoreCase("si") ||
                input.equalsIgnoreCase("height") ||
                input.equalsIgnoreCase("h") ||
                input.equalsIgnoreCase("depth");

    }

    private int[] insertStringsFromFile(String filePath) {
        File file = loadFile(filePath);
        if (file == null) return null;
        try (Scanner fileScan = new Scanner(file)) {
            int[] result = {0, 0};
            while (fileScan.hasNextLine()) {
                String input = fileScan.nextLine().trim();
                boolean flag = tree.insert(input);
                if (flag)
                    result[0]++;
                else
                    result[1]++;
            }
            fileScan.close();
            return result;
        }
        catch (FileNotFoundException e) {
            return null;
        }
    }

    private int[] deleteStringsFromFile(String filePath) {
        File file = loadFile(filePath);
        if (file == null) return null;
        try (Scanner fileScan = new Scanner(file)) {
            int[] result = {0, 0};
            while (fileScan.hasNextLine()) {
                String input = fileScan.nextLine().trim();
                boolean flag = tree.delete(input);
                if (flag)
                    result[0]++;
                else
                    result[1]++;
            }
            return result;
        }
        catch (FileNotFoundException e) {
            return null;
        }
    }

    private File loadFile(String path) {
        if (
            path.equalsIgnoreCase("default") ||
            path.equalsIgnoreCase("d")
        )
        {
            path = "./src/stringFiles/default.txt";
        }
        File file = new File(path);
        if (!file.exists() || !file.isFile() || !path.endsWith(".txt"))
            return null;
        return file;
    }
}
