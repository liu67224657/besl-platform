import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

import java.io.File;

/**
 * asserts that a given file is no larger than a specified size (in bytes).
 * if the file IS larger than the specified size the task will either throw
 * a BuildException or log a warning (based on whether or not 'enforce' was set to
 * true).
 */
public class CheckSize extends Task {
    private String fileName = null;
    private int maxFileSize = -1;
    private boolean enforce = false;
    private String message = null;

    public CheckSize() {
        // do nothing
    }

    public void execute() throws BuildException {
        File f = null;

        try {
            f = new File(fileName);
        } catch (NullPointerException e) {
            throw new BuildException("File name not specified");
        }

        System.out.println("checking size of " + f + " against " + maxFileSize);

        if (f.length() > maxFileSize) {
            handleErrorOutput(fileName + " (" + f.length() + " bytes) exceeded maximum allowed size (" + maxFileSize + " bytes)");
            if (enforce) {
                if (message != null) {
                    throw new BuildException(message);
                } else {
                    throw new BuildException("File size check failed.  See CheckSize error output for details");
                }
            }
        }
    }

    /**
     * the file who's size we want to check
     */
    public void setFile(String fn) {
        fileName = fn;
    }

    /**
     * max size in bytes
     */
    public void setMax(int mfs) {
        maxFileSize = mfs;
    }

    /**
     * if enforce is set to true, the build will fail if the file
     * exceeds the maximum size.  otherwise, a warning will be written
     * to the console
     */
    public void setEnforce(boolean enforce) {
        this.enforce = enforce;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
