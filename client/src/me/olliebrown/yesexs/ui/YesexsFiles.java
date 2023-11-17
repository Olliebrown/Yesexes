package me.olliebrown.yesexs.ui;

import java.io.File;
import java.io.IOException;

public class YesexsFiles {

    private YesexsFiles(){

    }

    private static final File tmp = new File("./tmp");

    static{
        if(!tmp.exists()) {
            if(!tmp.mkdirs()) {
                System.err.println("Failed to create temporary directory");
            }
        }
    }

    public static File createTempFile(String ext) throws IOException {
        File res = new File(tmp, System.currentTimeMillis() + "." + ext);
        if (!res.createNewFile()) {
            System.out.println("Temporary file already exists");
        }
        return res;
    }

    public static File createTempDir(){
        File res = new File(tmp, "" + System.currentTimeMillis());
        res.mkdirs();
        return res;
    }

    public static File getTempDir() {
        return tmp;
    }
}
