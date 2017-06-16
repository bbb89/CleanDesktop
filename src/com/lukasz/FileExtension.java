package com.lukasz;

import java.io.Serializable;
import java.nio.file.Path;

/**
 * Created by Łukasz on 13.06.2017.
 */
public class FileExtension implements Serializable {
    String extension;
    String folder;
    static final long serialVersionUID = 42L;

    public FileExtension(String extension, String folder) {
        this.extension = extension;
        this.folder = folder;
    }

    public String getExtension() {
        return extension;
    }

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FileExtension that = (FileExtension) o;

        return extension.equals(that.extension);
    }

    @Override
    public String toString() {
        return "\"" + extension + "\"" + "\t-\t" + folder;
    }

    @Override
    public int hashCode() {
        return extension.hashCode();
    }
}
