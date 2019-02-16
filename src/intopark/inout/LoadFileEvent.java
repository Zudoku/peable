/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package intopark.inout;

import com.google.inject.Singleton;
import java.io.File;

/**
 *
 * @author arska
 */
@Singleton
public class LoadFileEvent {
    private File file;

    public LoadFileEvent(File file) {
        this.file = file;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
    
    
}
