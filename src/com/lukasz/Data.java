package com.lukasz;

import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashSet;

/**
 * Created by Łukasz on 13.06.2017.
 */
public class Data {
    private static String pathOfDesktop;
    private static HashSet<FileExtension> extensions;

    static {
        try(ObjectInputStream input = new ObjectInputStream(new BufferedInputStream(new FileInputStream("data.dat")))) {
            pathOfDesktop = (String) input.readObject();
            extensions = (HashSet<FileExtension>) input.readObject();
        }catch (FileNotFoundException e) {
            System.out.println("Settings not found.");
            extensions = new HashSet<>();
        }catch (ClassNotFoundException e) {
            extensions = new HashSet<>();
        }catch(IOException e) {
            extensions = new HashSet<>();
        }
    }

    public static HashSet<FileExtension> getExtensions() {
        return extensions;
    }

    public static String getPathOfDesktop() {
        return pathOfDesktop;
    }

    public static void setPathOfDesktop(String path) {
        pathOfDesktop = path;
    }

    public boolean addExtension(FileExtension extension) {
        return extensions.add(extension);
    }

    public boolean removeExtension(FileExtension extension) {
        return extensions.remove(extension);
    }

    public boolean changeFolder(FileExtension extension) {
        boolean extensionExists = extensions.contains(extension);
        if(extensionExists) {
            extensions.remove(extension);
            extensions.add(extension);
        }

        return extensionExists;
    }

    public static void saveData() {
        try(ObjectOutputStream output = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream("data.dat")))) {
            output.writeObject(pathOfDesktop);
            if(extensions != null) output.writeObject(extensions);
        }catch (IOException e) {
            System.out.println("File error");
        }
    }

    public static void resetSettings() {
        pathOfDesktop = null;
        extensions = new HashSet<>(Collections.emptySet());
        Path path = FileSystems.getDefault().getPath("data.dat");
        try {
            Files.delete(path);
        }catch (IOException e) {
            System.out.println("File error!");
        }
    }
}
