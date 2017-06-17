package com.lukasz;

import java.io.IOException;
import java.nio.file.*;
import java.util.HashSet;
import java.util.Iterator;

/**
 * Created by Łukasz on 13.06.2017.
 */
public class Cleaner {
    Path path;
    HashSet<FileExtension> extensions;

    public Cleaner(Path path, HashSet<FileExtension> extensions) {
        this.path = path;
        this.extensions = extensions;
    }

    public boolean doCleaning() {
        Iterator<FileExtension> iterator = extensions.iterator();
        if(extensions.size() == 0) {
            return false;
        }
        while(iterator.hasNext()) {
            FileExtension currentExt = iterator.next();
            String extension = currentExt.getExtension();
            String folder = currentExt.getFolder();
            Path destination = Paths.get(path.toString()
                    + FileSystems.getDefault().getSeparator()
                    + folder);
            if (!Files.exists(destination))
                try {
                    Files.createDirectory(destination);
                }catch (IOException e) {
                    System.out.println("Error creating directory");
                }
            try(DirectoryStream<Path> files = Files.newDirectoryStream(path, "*." + extension)) {
                for(Path file : files) {
                    destination = Paths.get(path.toString()
                            + FileSystems.getDefault().getSeparator()
                            + folder
                            + FileSystems.getDefault().getSeparator()
                            + file.getFileName());
                    Files.move(file, destination, StandardCopyOption.REPLACE_EXISTING);
                }
            }catch (IOException e) {
                System.out.println("File error!");
            }
        }

        return true;
    }
}
