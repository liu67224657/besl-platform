import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This is a custom ant task designed to "discover" junit test suites from a package of test files, and
 * add them to a variation of AllTests.java.  These files will not be checked in to source control, but
 * will be deleted and re-generated at compile time.
 * It uses a template file that has the basic java structure with a few tags in the file for replacement.
 * Any file with a name that starts with "Test" will be put into the test suite, using the junit method
 * addTestSuite(TestName.class).  Currently there are two template files, the basic AllTests.java.template, which
 * does nothing except add Test classes by class to the junit.framework.TestSuite.
 * Then there is QuarantineAllTests.java.template, which adds Test classes to com.enjoyf.platform.rtest.QuarantineTestSuite,
 * which adds all tests that start with the word "quarantine" instead of "test".  This separates out our tests
 * from the ones that should be running nightly and passing from the ones that are failing and need investigation.
 */
@SuppressWarnings("rawtypes")
public class MakeAllTestsFiles extends Task {
    private String filePath = null;
    private String templatePath = null;

    private static final String PACKAGE_TOKEN = "PACKAGE_TOKEN_REPLACE_ME";
    private static final String CLASS_TOKEN = "CLASS_TOKEN_REPLACE_ME";
    private static final String TEMPLATE_TOKEN = ".template";

    public MakeAllTestsFiles() {
    }

    public void execute() throws BuildException {
        try {
            p_buildAllFiles(filePath, templatePath);
        } catch (IOException e) {
            throw new BuildException(e.getMessage());
        }
    }

    /**
     * @param filePath the path to a directory that contains test code
     */
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    /**
     * @param templatePath the path to a file that is the template that we'll use to build our AllTests class
     */

    public void setTemplatePath(String templatePath) {
        this.templatePath = templatePath;
    }

    /**
     * This is the recursive function that will go through the start directory and try to build AllTests files
     * if Test* files are present in the directory.
     *
     * @param startPath    the path to a directory that contains test code
     * @param templatePath the path to a file that is the template that we'll use to build our AllTests class
     */
    private void p_buildAllFiles(String startPath, String templatePath) throws IOException {
        File startDir = p_verifyDirPath(startPath);

        //try and build the AllTests file, will just do nothing if there are no Test* files present

        if (!p_isLoadTestDir(startDir)) {
            p_buildAllTestsFile(startPath, templatePath);
        }
        //check and see if we have any children
        String[] files = startDir.list();
        for (int i = 0; i < files.length; i++) {
            String filename = files[i];

            File file = new File(startPath + File.separator + filename);
            if (file.isDirectory()) {
                //now make the recursive call
                p_buildAllFiles(file.getPath(), templatePath);
            }
        }

    }

    /**
     * Returns true if the directory follows our load test convention, i.e. named "load".
     *
     * @param file
     * @return
     */
    private boolean p_isLoadTestDir(File file) {
        return (file.getName().startsWith("load") || file.getName().startsWith("Load"));
    }

    /**
     * Opens a buffered reader for the template file path passed in to the task.
     *
     * @param templatePath
     * @return
     * @throws IOException
     */
    private BufferedReader p_openTemplateFile(String templatePath) throws IOException {
        File file = new File(templatePath);
        if (!file.exists()) {
            throw new BuildException("template file " + templatePath + " doesn't exist!");
        }
        BufferedReader reader = new BufferedReader(new FileReader(file));
        return reader;
    }

    /**
     * Parses out the package name from the file path.
     * If the file path is /vobs/ben/rtest/com/platform/service/item, this method will return
     * the string com.enjoyf.platform.service.item.
     *
     * @param filePath
     * @return
     */
    private String p_getPackageName(String filePath) {

        String rtest = "rtest" + File.separator;
        int rtestIndex = filePath.indexOf(rtest);
        String packageName = filePath.substring(rtestIndex + rtest.length());
        packageName = packageName.replace(File.separatorChar, '.');
        return packageName;
    }

    private File p_getAllTestsFile(String filePath, String templatePath) {
        //make sure it contains the template string, otherwise it's not a template file
        if (!templatePath.endsWith(TEMPLATE_TOKEN)) {
            throw new BuildException("Template file doesn't end with " + TEMPLATE_TOKEN);
        }

        //take AllTests.java.template, and turn it into AllTests.java
        File template = new File(templatePath);
        String templateName = template.getName();
        int index = templateName.indexOf(TEMPLATE_TOKEN);

        String filename = templateName.substring(0, index);

        filename = filePath + File.separator + filename;

        return new File(filename);

    }

    private void p_buildAllTestsFile(String filePath, String templatePath) throws IOException {

        List testFiles = p_getTestFiles(filePath);
        if (testFiles.isEmpty()) {
            //System.out.println("No test files found in " + filePath);
            return;
        }
        BufferedReader reader = p_openTemplateFile(templatePath);

        File allTestsFile = p_getAllTestsFile(filePath, templatePath);
        BufferedWriter writer = new BufferedWriter(new FileWriter(allTestsFile));

        String line = reader.readLine();

        String packageName = p_getPackageName(filePath);
        while (line != null) {

            if (line.indexOf(PACKAGE_TOKEN) > 0) {

                //replace the package token with the actual package
                line = line.replaceFirst(PACKAGE_TOKEN, packageName);

            }

            if (line.indexOf(CLASS_TOKEN) > 0) {
                //do the actual class expansion
                for (Iterator it = testFiles.iterator(); it.hasNext();) {
                    File testFile = (File) it.next();
                    String name = testFile.getName().replaceAll(".java", ".class");
                    line = "\t\tsuite.addTestSuite(" + name + ");";

                    writer.write(line);
                    writer.newLine();
                    line = "";
                }

            }

            writer.write(line);
            writer.newLine();
            line = reader.readLine();
        }

        writer.close();
        //System.out.println("Created file: " + allTestsFile.getAbsolutePath());

    }

    private File p_verifyDirPath(String dirPath) {
        File dir = new File(dirPath);

        if (!dir.exists()) {
            throw new BuildException("Directory path " + dirPath + " does not exist");
        }
        if (!dir.isDirectory()) {
            throw new BuildException("Directory path " + dirPath + " is not a directory");
        }

        return dir;
    }

    private List p_getTestFiles(String dirPath) {
        //make sure that dirPath actually points to a directory
        File srcDir = p_verifyDirPath(dirPath);

        //create an arraylist of files that we'll return... since we don't know the size before hand,
        //it's hard to use a straight array
        ArrayList testFiles = new ArrayList();
        File[] files = srcDir.listFiles();

        //iterate through and look for file that start with "Test*"
        for (int i = 0; i < files.length; i++) {
            String name = files[i].getName();
            if (p_isNameTestFile(name)) {
                testFiles.add(files[i]);
            }
        }
        return testFiles;
    }

    private boolean p_isNameTestFile(String name) {
        return name.startsWith("Test") && name.endsWith(".java");
    }
}
