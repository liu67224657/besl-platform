import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

import java.util.Date;

/**
 * A Task to measure elapsed time for the build. I tried using the ant contrib stopwatch task
 * (http://ant-contrib.sourceforge.net/tasks/tasks/stopwatch_task.html) but had a very hard time
 * getting it to do what I wanted. I just want to print two things:
 * 1. Total time elapsed since the start of the build
 * 2. Time elapsed since the last call to the task.
 */
public class PerformanceTimer extends Task {
    private static Date startTime = null;
    private static Date lastCallTime = null;
    private static final double MIN_TIME_TO_BOTHER_PRINTING = 0.10;

    public PerformanceTimer() {
    }

    public void execute() throws BuildException {
        printElapsedTime();
    }

    public static void printElapsedTime() {
        Date now = new Date();

        if (startTime == null) {
            startTime = now;
            System.out.println("Initialized timers");
        } else {
            double totalTime = (now.getTime() - startTime.getTime()) / 1000.0;
            double timeSinceLastCall = (now.getTime() - lastCallTime.getTime()) / 1000.0;

            if (timeSinceLastCall < MIN_TIME_TO_BOTHER_PRINTING) {
                return;
            }

            System.out.println("Total time spent: " + totalTime);
            System.out.println("Time since last call: " + timeSinceLastCall);
        }

        lastCallTime = now;
    }
}
