import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.TaskContainer;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;


/**
 * Executes the contained tasks in sequence, suppressing any
 * BuildExceptions raised.
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class NonFatal extends Task implements TaskContainer {
    private Vector mTasks = new Vector();
    private String mFlag;

    
	public void addTask(Task task) throws BuildException {
        mTasks.addElement(task);
    }

    public void execute() throws BuildException {
        try {
            Enumeration tasks = mTasks.elements();
            while (tasks.hasMoreElements()) {
                Task task = (Task) tasks.nextElement();
                task.perform();
            }
        }
        catch (ThreadDeath t) {
            throw t;
        }
        catch (Throwable t) {
            // suppress
            t.printStackTrace();

            // touch flag file if configured
            try {
                if (mFlag != null && mFlag.length() > 0) {
                    new File(mFlag).createNewFile();
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void setFlag(String flag) {
        mFlag = flag;
    }
}

