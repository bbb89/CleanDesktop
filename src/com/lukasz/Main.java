package com.lukasz;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        boolean quit = false;
        Path path = FileSystems.getDefault().getPath(Data.getPathOfDesktop());

        if(path == null) {
            path = checkFolder();
        }


        while(!quit) {
            char choice;
            System.out.println("Your current folder is " + Data.getPathOfDesktop().toString());
            System.out.println("Choose an option:");
            System.out.println("1. Show extensions to move");
            System.out.println("2. Add extensions");
            System.out.println("3. Change extension folder");
            System.out.println("4. Remove extensions");
            System.out.println("5. Change folder to clean");
            System.out.println();
            System.out.println("C. Clean folder");
            System.out.println("Q. Quit");
            System.out.println("0. Reset settings");
            choice = scanner.nextLine().toUpperCase().charAt(0);

            switch (choice) {
                case '1':
                    showExtensions();
                    break;
                case '2':
                    addExtension();
                    break;
                case '3':
                    changeExtensionFolder();
                    break;
                case '4':
                    removeExtension();
                    break;
                case '5':
                    checkFolder();
                    break;
                case 'C':
                    Cleaner cleaner = new Cleaner(path, Data.getExtensions());
                    cleaner.doCleaning();
                    break;
                case 'Q':
                    quit = true;
                    break;
                case '0':
                    quit = resetSettings();
                    break;
                default:
            }

            if(choice != '0') Data.saveData();
        }

    }

    private static String getPathFromClipboard() {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable t = clipboard.getContents(null);
        if (t == null) return null;
        try {
            return (String) t.getTransferData(DataFlavor.stringFlavor);
        }catch (Exception e) {
            System.out.println("Clipboard error!");
        }
        return null;
    }

    private static Path checkFolder() {
        Path path;
        System.out.println("Copy path of desktop or other directory you want to clean. Then press enter.");
        while(true) {
            scanner.nextLine();
            path = Paths.get(getPathFromClipboard());
            if (Files.exists(path) && Files.isDirectory(path)) {
                Data.setPathOfDesktop(path.toString());
                return path;
            } else
                System.out.println("Please, copy path to a directory");
        }
    }

    private static void showExtensions() {
        HashSet<FileExtension> extensions = Data.getExtensions();
        System.out.println("\tFile type\t\tFolder");
        Iterator<FileExtension> iterator = extensions.iterator();
        int i = 1;
        while(iterator.hasNext()) {
            System.out.println((i++ + ".\t" + iterator.next().toString()));
        }
    }

    private static void addExtension() {
        String[] exts;
        String folder;
        System.out.println("Enter file type/types (split by \",\", no spaces):");
        exts = scanner.nextLine().split(",");
        System.out.println("Enter folder where your files will be moved");
        folder = scanner.nextLine();

        for(String s : exts) {
            HashSet<FileExtension> extensions = Data.getExtensions();
            FileExtension extension = new FileExtension(s, folder);
            boolean added = extensions.add(extension);
            if(!added) System.out.println("Error, extension exists on your list");
        }
    }

    private static void changeExtensionFolder() {
        HashSet<FileExtension> extensions = Data.getExtensions();
        System.out.print("Your extensions: ");
        Iterator<FileExtension> iterator = extensions.iterator();
        while(iterator.hasNext()) {
            System.out.print(iterator.next().getExtension());
            if(iterator.hasNext()) System.out.print(", ");
        }
        System.out.println(".");
        System.out.println("Choose extensions you want to move to another folder (split by \",\", no spaces).");
        String[] exts = scanner.nextLine().split(",");
        System.out.println("Choose new folder.");
        String folder = scanner.nextLine();
        for(String s : exts) {
            FileExtension extension = new FileExtension(s, folder);
            if(extensions.contains(extension)) extensions.remove(extension);
            extensions.add(extension);
        }
    }

    private static void removeExtension() {
        HashSet<FileExtension> extensions = Data.getExtensions();
        System.out.print("Your extensions: ");
        Iterator<FileExtension> iterator = extensions.iterator();
        while(iterator.hasNext()) {
            System.out.print(iterator.next().getExtension());
            if(iterator.hasNext()) System.out.print(", ");
        }
        System.out.println(".");
        System.out.println("Choose extensions you want to remove (split by \",\", no spaces).");
        String[] exts = scanner.nextLine().split(",");
        for(String s : exts) {
            FileExtension extension = new FileExtension(s, null);
            if(extensions.contains(extension)) extensions.remove(extension);
        }
    }

    private static boolean resetSettings() {
        System.out.println("Are you sure? All your settings will be lost and program will be terminated!");
        System.out.println("Y/N");
        char choice = scanner.nextLine().toUpperCase().charAt(0);
        if(choice == 'Y') {
            Data.resetSettings();
            return true;
        }else
            return false;
    }
}
