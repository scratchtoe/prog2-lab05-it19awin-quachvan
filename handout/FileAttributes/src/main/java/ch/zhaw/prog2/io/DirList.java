package ch.zhaw.prog2.io;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class DirList {
    private static final DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static void main(String[] args) throws IOException {
        String pathName = (args.length == 1)? args[0] : ".";
        File file = new File(pathName);
        // Write metadata of given file, resp. all of its files if it is a directory
        // Whith each file on one line in the following format.
        // - type of file ('d'=directory, 'f'=file)
        // - readable   'r', '-' otherwise
        // - writable   'w', '-' otherwise
        // - executable 'x', '-' otherwise
        // - hidden     'h', '-' otherwise
        // - modified date in format 'yyyy-MM-dd HH:mm:ss'
        // - length in bytes
        // - name of the file

    }

}
