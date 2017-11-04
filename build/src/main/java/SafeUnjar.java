import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.Expand;

/**
 * used to unjar a file only if it exists.  if a FileNotFoundException is thrown
 * because the specified jar doesn't exist, it is caught and ignored.
 */
public class SafeUnjar extends Expand {
    public void execute() throws BuildException {
        try {
            super.execute();
        }
        catch (Exception e) {
            return;
        }
    }
}
