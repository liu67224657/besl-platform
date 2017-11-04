import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Properties;
import java.util.StringTokenizer;

/**
 * A megatask to handle the creation of installable signed cabs for
 * Internet Explorer.
 */
public class CreateInstallable extends Task {
    private static final String NEWLINE = System.getProperty("line.separator");
    private static final String MANIFEST = "/META-INF/MANIFEST.MF";
    private static final String MANIFEST_DIR = "/META-INF";
    private static final String VERSION_JAVA = "/com/platform/build/Version.java";
    private static final String VERSION_CLASS = "/com/platform/build/Version.class";
    private static final String TEMPLATE = "/com/platform/build/Version-java.template";
    private static final long TIMEOUT = 5L * 60L * 1000L; // 5 minutes

    private String mViewroot;
    private String mConfigprops;
    private String mBuildprops;
    private String mTempdir;
    private String mDocs;
    private String mHost;
    private int mPort;
    private String mBuildversion;
    private String mBuildlabel;
    private String mBuilddate;
    private String mSrcfile;
    private String mLocale;
    private String mLocaleSuffix;

    public CreateInstallable() {
        mLocale = "en_US";
    }

    public void execute() throws BuildException {
        // Figure out which build properties file will need to be updated.
        // Convert:
        //    /path/to/file.properties"
        // into
        //    /path/to/file_<locale>.properties
        mLocaleSuffix = "-" + mLocale;
        mBuildprops = mBuildprops.replaceAll(
                "^(.*)\\.properties$", "$1_" + mLocale + ".properties");

        try {
            boolean success = false;
            for (int retries = 0; !success && retries < 3; retries++) {
                try {
                    createInstallable(mConfigprops, mBuildprops);
                    success = true;
                }
                catch (Throwable e) {
                    if (retries < 2) {
                        System.out.println("caught " + e + ", retrying");
                    } else {
                        throw e;
                    }
                }
            }
        }
        catch (IOException e) {
            if (e.getMessage().indexOf("Too many open files") >= 0) {
                try {
                    String[] lsofcmd = {
                            "/usr/sbin/lsof"
                    };
                    invoke(lsofcmd);
                }
                catch (Throwable t) {
                    System.out.println("could not execute lsof: " + t);
                }
            }

            throw new BuildException(e);
        }
        catch (BuildException e) {
            throw e;
        }
        catch (Throwable e) {
            // ant doesn't normally dump a trace
            e.printStackTrace();

            throw new BuildException(e);
        }
    }

    private void createInstallable(String configFile, String buildFile)
            throws Exception {
        // abort if no config props file
        File f = new File(mViewroot + configFile);
        if (!f.exists()) {
            System.out.println(
                    "will not create installable archive: no assets props file");
            return;
        }

        // read in the config file
        Properties configProps = new Properties();
        InputStream in = new FileInputStream(f);
        configProps.load(in);
        in.close();

        // abort if this applet is not installable
        boolean installable = false;
        String val = getLocaleProperty(configProps, "cab.installable");
        if (val != null) {
            installable = Boolean.valueOf(val.trim()).booleanValue();
        }
        if (!installable) {
            System.out.println(
                    "will not create installable archive: cab.installable false");
            return;
        }

        // read in the build props, if available
        Properties buildProps = new Properties();
        f = new File(mViewroot + buildFile);
        if (f.createNewFile()) {
            p4add(buildFile);
        } else {
            in = new FileInputStream(f);
            buildProps.load(in);
            in.close();
        }

        PerformanceTimer.printElapsedTime();

        // derive the gamecode for a tempdir
        String filename = f.getName();
        String gamecode = filename.substring(0, filename.indexOf("."));
        String temp = mTempdir + "/" + gamecode;

        // prepare a clean temp dir
        File tempdir = new File(temp);
        if (tempdir.exists()) {
            String[] rmcmd = {
                    "/bin/rm",
                    "-Rf",
                    temp
            };
            invoke(rmcmd);
        }
        tempdir.mkdirs();

        // expand the src file into the temp dir
        String[] unjarcmd = {
                "jar",
                "xf",
                mSrcfile
        };
        invoke(unjarcmd, tempdir, true);
        PerformanceTimer.printElapsedTime();

        // remove the (irrelevant and possibly misleading) manifest file
        System.out.println("deleting manifest");
        f = new File(temp + MANIFEST);
        if (f.exists()) {
            f.delete();
        }
        f = new File(temp + MANIFEST_DIR);
        if (f.exists()) {
            f.delete();
        }

        boolean hasVersion = false;

        // remove the (incorrect, for this archive) Version.class file
        System.out.println("deleting Version.class");
        f = new File(temp + VERSION_CLASS);
        if (f.exists()) {
            hasVersion = true;
            f.delete();
        }

        // compute the archive checksum
        String prevChecksum = buildProps.getProperty("cab.checksum");
        if (prevChecksum == null) {
            prevChecksum = "0";
        }
        System.out.println("previous checksum " + prevChecksum);

        String prevVersion = buildProps.getProperty("cab.version");
        System.out.println("previous version " + prevVersion);

        if (isBackwards(buildProps.getProperty(
                "cab.build.version"), mBuildversion)) {
            System.out.println("BACKWARDS VERSION");
            String[] warncmd = {
                    mViewroot + "/com/platform/build/warn-backwards.csh",
            };
            invoke(warncmd);
        }

        String[] checksumcmd = {
                mViewroot + "/com/platform/build/checksum.csh",
                temp,
                temp + "/.."
        };
        invoke(checksumcmd);

        // we only want the first field of the checksum output
        StringTokenizer st = new StringTokenizer(readFile(temp + "/../checksum.out"));
        String checksum = st.nextToken();
        System.out.println("current checksum " + checksum);

        PerformanceTimer.printElapsedTime();

        // update the checked-in props file if necessary
        if (!checksum.equals(prevChecksum)) {
            String version = convertVersion(mBuildversion);
            System.out.println(
                    "checksum mismatch! new archive version " + version);

            buildProps.setProperty("cab.checksum", checksum);
            buildProps.setProperty("cab.build.version", mBuildversion);
            buildProps.setProperty("cab.build.date", mBuilddate);
            buildProps.setProperty("cab.version", version);

            // don't bother updating the props file in dev
            if (!mBuildversion.startsWith("0")) {
                // run a notification script
                String[] notifycmd = {
                        mViewroot + "/com/platform/build/version-notify.csh",
                        getLocaleProperty(configProps, "cab.friendly").trim()
                                + " (" + mLocale + ")",
                        mBuildversion
                };
                invoke(notifycmd);

                // update the props files
                try {
                    p4edit(buildFile);
                    OutputStream out = new FileOutputStream(
                            mViewroot + buildFile);
                    buildProps.store(out,
                            "Automatically generated by CreateInstallable - DO NOT EDIT");
                    out.close();
                    p4submit(buildFile,
                            "BUILD: new archive version " + mBuildversion);
                    relabel(buildFile, mBuildlabel);
                    PerformanceTimer.printElapsedTime();
                }
                finally {
                    try {
                        p4revert(buildFile);
                    }
                    catch (Throwable t) {
                        // throwing something already
                    }
                }
            }
        }

        // create a new Version.class file
        if (hasVersion) {
            String template = readFile(mViewroot + TEMPLATE);
            template = template.replaceAll("\\@BUILDVERSION\\@",
                    buildProps.getProperty("cab.build.version").trim());
            template = template.replaceAll("\\@BUILDDATE\\@",
                    buildProps.getProperty("cab.build.date").trim());
            File versionJava = new File(temp + VERSION_JAVA);
            BufferedWriter out = new BufferedWriter(
                    new FileWriter(versionJava));
            out.write(template);
            out.close();

            String[] javaccmd = {
                    "javac",
                    "-target",
                    "1.1",
                    "-source",
                    "1.2",
                    versionJava.toString()
            };
            invoke(javaccmd);

            versionJava.delete();
        }

        // create a jar with the ob classes and assets
        String tempjar = temp + "/../archive.jar";
        String[] jarcmd = {
                "jar",
                "cfM",
                tempjar,
                "-C",
                temp,
                "."
        };
        invoke(jarcmd);
        PerformanceTimer.printElapsedTime();

        // find out which archive to build
        String cabfile = mDocs + "/applet/" +
                getLocaleProperty(configProps, "cab.file").trim() +
                mLocaleSuffix + ".cab";

        PerformanceTimer.printElapsedTime();

    }

    private boolean isBackwards(String oldVer, String newVer) {
        boolean isBackwards = false;

        if (oldVer != null && newVer != null) {
            StringTokenizer oldSt = new StringTokenizer(oldVer, ".");
            StringTokenizer newSt = new StringTokenizer(newVer, ".");

            // I supposed this could be recursive, but why
            int oldMaj = Integer.parseInt(oldSt.nextToken());
            int newMaj = Integer.parseInt(newSt.nextToken());
            if (oldMaj > newMaj) {
                isBackwards = true;
            } else if (oldMaj == newMaj) {
                int oldMin = Integer.parseInt(oldSt.nextToken());
                int newMin = Integer.parseInt(newSt.nextToken());
                if (oldMin > newMin) {
                    isBackwards = true;
                } else if (oldMin == newMin) {
                    int oldBug = Integer.parseInt(oldSt.nextToken());
                    int newBug = Integer.parseInt(newSt.nextToken());
                    if (oldBug > newBug) {
                        isBackwards = true;
                    } else if (oldBug == newBug) {
                        int oldIncr = Integer.parseInt(oldSt.nextToken());
                        int newIncr = Integer.parseInt(newSt.nextToken());
                        if (oldIncr > newIncr) {
                            isBackwards = true;
                        }
                    }
                }
            }
        }

        return isBackwards;
    }

    private void p4add(String file)
            throws Exception {
        System.out.println("making new element " + file);
        String[] addcmd = {
                "p4",
                "add",
                mViewroot + file
        };
        invoke(addcmd);
        p4submit(file, "BUILD: new props");
    }

    private void p4edit(String file) throws Exception {
        System.out.println("editing " + file);
        String[] editcmd = {
                "p4",
                "edit",
                mViewroot + file
        };
        invoke(editcmd);
    }

    private void p4submit(String file, String comment) throws Exception {
        String[] submitcmd = {
                mViewroot + "/build/p4submit.sh",
                mViewroot + file,
                comment
        };
        invoke(submitcmd);
    }

    private void p4revert(String file) throws Exception {
        String[] revertcmd = {
                "p4",
                "revert",
                mViewroot + file
        };
        invoke(revertcmd);
    }

    private void relabel(String file, String label) throws Exception {
        String[] relabelcmd = {
                "p4",
                "labelsync",
                "-l",
                label,
                mViewroot + file
        };
        invoke(relabelcmd);
    }

    private void checkVersion(String buildversion) {
        // ensure a valid version string, which is "w.x.y.z" for positive ints
        StringTokenizer st = new StringTokenizer(buildversion, ".");
        for (int i = 0; i < 4; i++) {
            try {
                int val = Integer.parseInt(st.nextToken());
                if (val < 0) {
                    throw new IllegalArgumentException();
                }
            }
            catch (Exception e) {
                throw new BuildException(buildversion +
                        " is not a valid version number!");
            }
        }
    }

    private String convertVersion(String buildversion) {
        return buildversion.replace('.', ',');
    }

    private String readFile(String file) throws Exception {
        StringBuffer sb = new StringBuffer();

        BufferedReader in = new BufferedReader(new FileReader(file));
        String line = in.readLine();
        while (line != null) {
            sb.append(line);
            line = in.readLine();
            if (line != null) {
                sb.append(NEWLINE);
            }
        }
        in.close();

        return sb.toString();
    }

    private void invoke(String[] cmd) throws Exception {
        invoke(cmd, true);
    }

    private void invoke(String[] cmd, boolean echo) throws Exception {
        invoke(cmd, null, echo);
    }

    private void invoke(String[] cmd, File dir, boolean echo) throws Exception {
        invoke(cmd, null, dir, echo);
    }

    private void invoke(String[] cmd, String[] stdin, File dir, boolean echo)
            throws Exception {
        if (echo) {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < cmd.length; i++) {
                boolean quoted = (cmd[i].indexOf(" ") > 0);
                if (quoted) {
                    sb.append("\"");
                }
                sb.append(cmd[i]);
                if (quoted) {
                    sb.append("\"");
                }
                sb.append(" ");
            }
            System.out.println(sb.toString());
        }

        class MutableBoolean {
            boolean val = false;
        }
        final MutableBoolean isComplete = new MutableBoolean();
        final Thread main = Thread.currentThread();
        final Process proc = Runtime.getRuntime().exec(cmd, null, dir);

        // clear the stdout and stderr buffers concurrently with
        // execution to avoid blocking on logging writes
        final InputStream err = proc.getErrorStream();
        new Thread(
                new Runnable() {
                    public void run() {
                        try {
                            while (!isComplete.val) {
                                System.out.write(err.read());
                            }
                            err.close();
                        }
                        catch (Exception e) {
                        }
                    }
                }
        ).start();

        final InputStream out = proc.getInputStream();
        new Thread(
                new Runnable() {
                    public void run() {
                        try {
                            while (!isComplete.val) {
                                System.out.write(out.read());
                            }
                            out.close();
                        }
                        catch (Exception e) {
                        }
                    }
                }
        ).start();

        // feed the process any input provided
        try {
            if (stdin != null && stdin.length > 0) {
                PrintWriter args = new PrintWriter(new OutputStreamWriter(
                        proc.getOutputStream()));
                for (int i = 0; i < stdin.length; i++) {
                    args.println(stdin[i]);
                    if (echo) {
                        System.out.println(stdin[i]);
                    }
                }
                args.close();
            }
        }
        catch (Exception e) {
        }

        // be prepared to kill off the subtask if it doesn't return
        Thread timeout = new Thread(
                new Runnable() {
                    public void run() {
                        try {
                            Thread.sleep(TIMEOUT);
                        } catch (Exception e) {
                        }
                        if (!isComplete.val) {
                            main.interrupt();
                        }
                    }
                }
        );
        timeout.start();

        proc.waitFor(); // allow InterruptedException to be uncaught
        isComplete.val = true;
        timeout.interrupt();
        try {
            err.close();
        } catch (Exception e) {
        }
        try {
            out.close();
        } catch (Exception e) {
        }

        /*
          if (proc.exitValue() != 0) {
              throw new BuildException(
                  "failure in " + cmd[0] + ": exit value=" + proc.exitValue());
          }
          */
    }

    private String getLocaleProperty(Properties props, String key) {
        String prop = props.getProperty(key + "." + mLocale);
        if (prop == null) {
            prop = props.getProperty(key);
        }
        return prop;
    }

    public void setViewroot(String root) {
        mViewroot = root;
    }

    public void setConfigprops(String props) {
        mConfigprops = props;
    }

    public void setBuildprops(String props) {
        mBuildprops = props;
    }

    public void setBuilddate(String date) {
        mBuilddate = date;
    }

    public void setBuildversion(String version) {
        checkVersion(version);
        mBuildversion = version;
    }

    public void setBuildlabel(String label) {
        mBuildlabel = label;
    }

    public void setTempdir(String dir) {
        mTempdir = dir;
    }

    public void setDocs(String docs) {
        mDocs = docs;
    }

    public void setHost(String host) {
        mHost = host;
    }

    public void setPort(int port) {
        mPort = port;
    }

    public void setSrcfile(String srcfile) {
        mSrcfile = srcfile;
    }

    public void setLocale(String locale) {
        mLocale = locale;
    }
}

