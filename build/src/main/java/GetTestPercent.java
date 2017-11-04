import org.apache.tools.ant.BuildException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

/**
 * This is a task designed to parse through the html output of the junit ant
 * task and simply return the total percent of tests passed. Will be used in
 * darth.xml to send the percent in the notification emails. The source file
 * that contains the test percentage is "overview-summary.html" when the junit
 * report is generated with frames.
 */
public class GetTestPercent extends org.apache.tools.ant.taskdefs.Property {
    private String htmlSourcePath;
    private String percent;

    public GetTestPercent() {

    }

    public void execute() throws BuildException {

        if (htmlSourcePath == null) {
            throw new BuildException(
                    "htmlSourcePath atrribute must be specified, usually overview-summary.html");

        }

        if (percent == null) {
            throw new BuildException("Property name required.");
        }

        try {
            getPercent(htmlSourcePath);
        } catch (IOException e) {
            throw new BuildException(e);
        }

    }

    /**
     * Opens a buffered reader for the template file path passed in to the task.
     *
     * @param htmlFilePath
     * @return
     * @throws IOException
     */
    private BufferedReader openFile(String htmlFilePath) throws IOException {
        File file = new File(htmlFilePath);
        if (!file.exists()) {
            throw new BuildException("template file " + htmlFilePath
                    + " doesn't exist!");
        }
        BufferedReader reader = new BufferedReader(new FileReader(file));
        return reader;

    }

    /**
     * The little snippet of html that we're searching for looks like this:
     * <p/>
     * <th>Tests</th>
     * <th>Failures</th>
     * <th>Errors</th>
     * <th>Success rate</th>
     * <th>Time</th>
     * </tr>
     * <tr valign="top" class="Failure">
     * <td>720</td>
     * <td>4</td>
     * <td>0</td>
     * <td>99.44%</td>
     * <td>1208.280</td>
     * </tr>
     * </table>
     * <p/>
     * you can see the 99.44% in there, and that's what were looking for. We
     * should look for the line that contains the string "Success Rate" as our
     * guide, and parse from there.
     *
     * @param htmlFilePath
     * @throws IOException
     */
    private void getPercent(String htmlFilePath) throws IOException {

        BufferedReader reader = openFile(htmlFilePath);

        String line = reader.readLine();

        while (line != null) {
            String successRate = "<th>Success rate</th>";
            if (line.indexOf(successRate) > 0) {
                //we found our start point, now read two more ines and throw
                // them away, the third one is the one we want
                reader.readLine();
                reader.readLine();
                line = reader.readLine();
                //now parse the line
                StringTokenizer tokenizer = new StringTokenizer(line, "<td>");

                //should be the 5th token
                String token = tokenizer.nextToken();
                System.out.println("token is " + token);
                token = tokenizer.nextToken();
                System.out.println("token is " + token);
                token = tokenizer.nextToken();
                System.out.println("token is " + token);
                token = tokenizer.nextToken();
                System.out.println("token is " + token);
                token = tokenizer.nextToken();
                System.out.println("token is " + token);
                token = tokenizer.nextToken();
                System.out.println("token is " + token);
                token = tokenizer.nextToken();
                System.out.println("token is " + token);
                //should end with a % sign
                if (!token.endsWith("%")) {
                    throw new BuildException("String " + token
                            + " does not end with %");
                }

                getProject().setNewProperty(this.percent, token);
                return;

            }
            line = reader.readLine();
        }
        //if we made it all the way here, we didn't find what we were looking
        // for
        throw new BuildException("Percent not found in file");
    }

    public void setHtmlSourcePath(String htmlSourcePath) {
        this.htmlSourcePath = htmlSourcePath;
    }

    public void setPercent(String percent) {
        this.percent = percent;
    }

}
