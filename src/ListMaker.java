import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ListMaker {
    private static final Scanner in = new Scanner(System.in);

    public static void main(String[] args) {
        ArrayList<String> list = new ArrayList<>();
        boolean needsToBeSaved = false;
        String currentFileName = "";
        boolean done = false;

        while (!done) {
            try {
                System.out.println("\nA – Add");
                System.out.println("D – Delete");
                System.out.println("I – Insert");
                System.out.println("M – Move");
                System.out.println("V – View");
                System.out.println("C – Clear");
                System.out.println("O – Open");
                System.out.println("S – Save");
                System.out.println("Q – Quit");
                System.out.print("Choice: ");
                String choice = in.nextLine().trim().toUpperCase();

                switch (choice) {
                    case "A":
                        System.out.print("Enter item: ");
                        list.add(in.nextLine());
                        needsToBeSaved = true;
                        break;

                    case "D":
                        if (list.isEmpty()) break;
                        int d = getIndex(list, "Delete index");
                        list.remove(d);
                        needsToBeSaved = true;
                        break;

                    case "I":
                        if (list.isEmpty()) break;
                        int i = getIndex(list, "Insert at index");
                        System.out.print("Enter item: ");
                        list.add(i, in.nextLine());
                        needsToBeSaved = true;
                        break;

                    case "M":
                        if (list.isEmpty()) break;
                        int from = getIndex(list, "Move FROM index");
                        int to = getIndex(list, "Move TO index");
                        String item = list.remove(from);
                        list.add(to, item);
                        needsToBeSaved = true;
                        break;

                    case "V":
                        printList(list);
                        break;

                    case "C":
                        list.clear();
                        needsToBeSaved = true;
                        break;

                    case "O":
                        if (needsToBeSaved) {
                            if (getYN("Unsaved changes. Save first?")) {
                                if (currentFileName.isEmpty()) {
                                    currentFileName = getFileName();
                                }
                                saveFile(currentFileName, list);
                                needsToBeSaved = false;
                            }
                        }
                        currentFileName = getFileName();
                        list = loadFile(currentFileName);
                        needsToBeSaved = false;
                        break;

                    case "S":
                        if (currentFileName.isEmpty()) {
                            currentFileName = getFileName();
                        }
                        saveFile(currentFileName, list);
                        needsToBeSaved = false;
                        break;

                    case "Q":
                        if (needsToBeSaved) {
                            if (getYN("Unsaved changes. Save before quitting?")) {
                                if (currentFileName.isEmpty()) {
                                    currentFileName = getFileName();
                                }
                                saveFile(currentFileName, list);
                            }
                        }
                        done = true;
                        break;
                }
            } catch (IOException e) {
                System.out.println("File error.");
            }
        }
    }

    private static void printList(ArrayList<String> list) {
        if (list.isEmpty()) {
            System.out.println("(empty)");
            return;
        }
        for (int i = 0; i < list.size(); i++) {
            System.out.println(i + ": " + list.get(i));
        }
    }

    private static int getIndex(ArrayList<String> list, String prompt) {
        int index = -1;
        boolean valid = false;
        while (!valid) {
            System.out.print(prompt + " (0-" + (list.size() - 1) + "): ");
            if (in.hasNextInt()) {
                index = in.nextInt();
                if (index >= 0 && index < list.size()) valid = true;
            }
            in.nextLine();
        }
        return index;
    }

    private static boolean getYN(String prompt) {
        String response;
        do {
            System.out.print(prompt + " (Y/N): ");
            response = in.nextLine().trim().toUpperCase();
        } while (!response.equals("Y") && !response.equals("N"));
        return response.equals("Y");
    }

    private static String getFileName() {
        System.out.print("Enter filename (no extension): ");
        return in.nextLine().trim() + ".txt";
    }

    private static ArrayList<String> loadFile(String filename) throws IOException {
        List<String> lines = Files.readAllLines(Path.of(filename));
        return new ArrayList<>(lines);
    }

    private static void saveFile(String filename, ArrayList<String> list) throws IOException {
        Files.write(Path.of(filename), list);
        System.out.println("Saved to " + filename);
    }
}