package org.example.CommandInterface;

import org.example.AVL;
import org.example.RedBlack;
import org.example.Tree;

import java.util.Scanner;

public class IntitalizeMenu implements State{
    Scanner scanner;
    State nextState;

    public IntitalizeMenu(Scanner scanner) {
        this.scanner = scanner;
        this.nextState = null;
    }

    @Override
    public void excute() {
        String errorMsg = "";
        while (true) {
            System.out.print("\033[H\033[2J");
            if (!errorMsg.isEmpty())
                System.out.println(ConsoleColors.RED + errorMsg + ConsoleColors.RESET);

            System.out.println(ConsoleColors.BLUE + "Initialize Tree Type" + ConsoleColors.RESET);
            System.out.println("Choose Tree type:");
            printMenu();

            String input = scanner.nextLine().trim().replaceAll("\\s+", "");
            if (input.equalsIgnoreCase("exit")){
                System.out.println("Exiting");
                System.exit(0);
            }

            Tree<String> tree = null;
            switch (input) {
                case "1":
                    tree = new AVL<String>();

                case "2":
                    if (tree == null) tree = new RedBlack<String>();
                    nextState = new OperationMenu(scanner, tree);
                    return;

                default:
                    errorMsg = "Invalid Input";
            }

        }
    }

    @Override
    public State nextState() {
        return nextState;
    }

    private void printMenu() {
        System.out.println("1- AVL Tree");
        System.out.println("2- RedBlack Tree");
    }
}
