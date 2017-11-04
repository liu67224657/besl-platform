import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

/**
 * this class will scan two directory trees and produce a CSV (comma separated
 * values) file specifying which files appear in just one of those directories
 * and which files appear in both.  if told to do so, it will also scan the
 * contents of jar files and compare them (setCheckJars), and compare the
 * bytecode versions of classes it finds (setCheckClassVersions).  if class
 * versions are checked, the output will contain the bytecode version as well
 * as the oldest JVM on which the class can run.
 */
public class DirComp extends Task {
    private String dir1name = null;
    private String dir2name = null;

    private File root1 = null;
    private File root2 = null;

    private Map<String, ComparisonElement> comparisonMap; // maps a relative file name to a ComparisonElement

    private boolean checkJars = false;
    private boolean checkClassVersions = false;
    private boolean writeOverlap = false;

    private String outputFileName = null;
    private FileOutputStream outputFile = null;

    private static byte[] NEWLINE = {(byte) '\r', (byte) '\n'};
    private static char SEPARATOR = ',';
    private static final String YES_STRING = "yes";
    private static final String NO_STRING = "";
    /* from the sun website:
     "The Java virtual machine implementation of Sun's JDK release 1.0.2
     supports class file format versions 45.0 through 45.3 inclusive. Sun's
     JDK releases 1.1.X can support class file formats of versions in the
     range 45.0 through 45.65535 inclusive. Implementations of version 1.2 of
     the Java 2 platform can support class file formats of versions in the
     range 45.0 through 46.0 inclusive."
     "The J2SE 1.4.0 platform accepts class file versions in the range 45.3 to 48.0"
     */
    private final String JVM10 = "JVM 1.0";
    private final ClassVersion JVM10MAXVER = new ClassVersion(45, 3);
    private final String JVM11 = "JVM 1.1";
    private final ClassVersion JVM11MAXVER = new ClassVersion(45, 65535);
    private final String JVM12 = "JVM 1.2";
    private final ClassVersion JVM12MAXVER = new ClassVersion(46, 0);
    private final String JVM13 = "JVM 1.3";
    private final ClassVersion JVM13MAXVER = new ClassVersion(46, 0); // not sure of this
    private final String JVM14 = "JVM 1.4";
    private final ClassVersion JVM14MAXVER = new ClassVersion(48, 0);

    class ClassVersion {
        int major;
        int minor;

        public ClassVersion(int major, int minor) {
            this.major = major;
            this.minor = minor;
        }

        public int getMajor() {
            return major;
        }

        public int getMinor() {
            return minor;
        }

        public String toString() {
            return major + "." + minor;
        }

        public boolean equals(ClassVersion cv) {
            return (cv.major == major && cv.minor == minor);
        }

        /**
         * this>cv returns 1,
         * cv>this returns -1,
         * cv=this returns 0
         */
        public int compareTo(ClassVersion cv) {
            if (this.major > cv.major) {
                return 1;
            } else if (this.major < cv.major) {
                return -1;
            } else { // (this.major==cv.major)
                if (this.minor > cv.minor) {
                    return 1;
                } else if (this.minor < cv.minor) {
                    return -1;
                } else {
                    return 0;
                }
            }
        }

        public String getOldestJVMSupported() {
            if (this.compareTo(JVM10MAXVER) != 1) {
                return JVM10 + "(" + this.toString() + ")";
            } else if (this.compareTo(JVM11MAXVER) != 1) {
                return JVM11 + "(" + this.toString() + ")";
            } else if (this.compareTo(JVM12MAXVER) != 1) {
                return JVM12 + "(" + this.toString() + ")";
            } else if (this.compareTo(JVM13MAXVER) != 1) {
                return JVM13 + "(" + this.toString() + ")";
            } else if (this.compareTo(JVM14MAXVER) != 1) {
                return JVM14 + "(" + this.toString() + ")";
            } else {
                throw new BuildException("Will not run on any known JVMs (you gotta upgrade this code)");
            }
        }
    }

    // inner class to describe an element in the file comparison table
    class ComparisonElement {

        private File file = null;
        private boolean inDir1 = false;
        private boolean inDir2 = false;
        private ClassVersion dir1version = null;
        private ClassVersion dir2version = null;

        public ComparisonElement(File f) {
            this.file = f;
        }

        public void setInDir1(boolean inDir1) {
            this.inDir1 = inDir1 | this.inDir1;
        } // once set can not be unset

        public void setInDir2(boolean inDir2) {
            this.inDir2 = inDir2 | this.inDir2;
        } // once set can not be unset

        public void setDir1version(ClassVersion dir1version) {
            this.dir1version = dir1version;
        }

        public void setDir2version(ClassVersion dir2version) {
            this.dir2version = dir2version;
        }

        public File getFile() {
            return file;
        }

        public String getInDir1() {
            return (sameOldestJVMSupported() ? YES_STRING : getInDir(inDir1, dir1version));
        }

        public String getInDir2() {
            return (sameOldestJVMSupported() ? YES_STRING : getInDir(inDir2, dir2version));
        }

        public String getInDir(boolean inDir, ClassVersion version) {
            if (inDir) {
                if (isClass(file)) {
                    if (version != null) {
                        return version.getOldestJVMSupported();
                    }
                }
                return YES_STRING;
            }
            return NO_STRING;
        }

        public boolean sameOldestJVMSupported() {
            if (dir1version != null && dir2version != null) {
                return dir1version.getOldestJVMSupported().equals(dir2version.getOldestJVMSupported());
            }
            return false;
        }
    }

    public DirComp() {
        comparisonMap = new HashMap<String, ComparisonElement>();
    }

    public void execute() throws BuildException {
        try {
            root1 = new File(dir1name);
            root2 = new File(dir2name);
            dir1name = root1.getCanonicalPath();
            dir2name = root2.getCanonicalPath();
            if (!(root1.isDirectory() && root2.isDirectory())) {
                throw new BuildException("dir1 and dir2 must both be valid directory paths");
            }
        }
        catch (IOException e) {
            throw new BuildException("could not access one of the directories: " + dir1name + " or " + dir2name);
        }

        try {
            // try to open the output file for writing:
            outputFile = new FileOutputStream(outputFileName);
            add(root1);
            add(root2);
            writeOutput();
        }
        catch (IOException e) {
            throw new BuildException("could not create output file '" + outputFileName + "'");
        }
    }

    public void setOutputFile(String outputFileName) {
        this.outputFileName = outputFileName;
    }

    public void setDir1(String root1) {
        this.dir1name = root1;
    }

    public void setDir2(String root2) {
        this.dir2name = root2;
    }

    public void setCheckJars(boolean checkJars) {
        this.checkJars = checkJars;
    }

    public void setCheckClassVersions(boolean checkClassVersions) {
        this.checkClassVersions = checkClassVersions;
    }

    // ##########################################
    // # methods for building a map of files    #
    // ##########################################

    private void add(File f) {
        if (f.isFile()) {
            if (isJar(f)) {
                addJar(f);
            } else if (isClass(f)) {
                addClass(f);
            } else {
                addFile(f);
            }
        } else if (f.isDirectory()) {
            addDir(f);
        } else {
            throw new BuildException("Unknown file type: " + f.getAbsolutePath());
        }
    }

    private void addDir(File dir) {
        System.out.println("Checking directory " + dir.getAbsolutePath());
        // get all files in dir:
        File dirFiles[] = dir.listFiles();

        // add each file in directory:
        for (int x = 0; x < dirFiles.length; x++) {
            add(dirFiles[x]);
        }
    }

    private void addClass(File c) {
        ComparisonElement e = addFile(c);
        if (checkClassVersions) {
            ClassVersion ver = getVersion(c);
            if (isInDir1(c)) {
                e.setDir1version(ver);
            } else if (isInDir2(c)) {
                e.setDir2version(ver);
            }
        }
    }

    private ComparisonElement addFile(File file) {
        String name = getRelativeName(file);
        ComparisonElement e = comparisonMap.get(name);
        if (e == null) {
            e = new ComparisonElement(file);
            comparisonMap.put(name, e);
        }
        e.setInDir1(isInDir1(file));
        e.setInDir2(isInDir2(file));

        return e;
    }

    @SuppressWarnings("rawtypes")
	private void addJar(File jar) {
        if (checkJars) {
            JarFile jarFile = null;

            try {
                jarFile = new JarFile(jar);
            }
            catch (IOException e) {
                throw new BuildException("could not open jar file '" + jar.getAbsolutePath() + "'");
            }

            String jarName = getRelativeName(jar);
            Enumeration jarEntries = jarFile.entries();

            // put names of jar entries in a hashset:
            while (jarEntries.hasMoreElements()) {
                ZipEntry jarEntry = (ZipEntry) (jarEntries.nextElement());
                if (!jarEntry.isDirectory()) {
                    String entryName = "[" + jarName + "]" + jarEntry.getName();
                    ComparisonElement e = comparisonMap.get(entryName);
                    if (e == null) {
                        e = new ComparisonElement(jar);
                        comparisonMap.put(entryName, e);
                    }
                    e.setInDir1(isInDir1(jar));
                    e.setInDir2(isInDir2(jar));
                }
            }
        } else {
            addFile(jar);
        }
    }

    private boolean isInDir1(File f) {
        try {
            return f.getCanonicalPath().startsWith(dir1name);
        }
        catch (IOException e) {
            throw new BuildException("could not create canonical name for " + f.getAbsolutePath());
        }
    }

    private boolean isInDir2(File f) {
        try {
            return f.getCanonicalPath().startsWith(dir2name);
        }
        catch (IOException e) {
            throw new BuildException("could not create canonical name for " + f.getAbsolutePath());
        }
    }

    private void writeOutput() {
        writeLine("file" + SEPARATOR + dir1name + SEPARATOR + dir2name);
        for (Iterator<String> i = comparisonMap.keySet().iterator(); i.hasNext();) {
            String name = i.next();
            ComparisonElement e = (ComparisonElement) comparisonMap.get(name);
            if (writeOverlap == false) {
                if (!e.getInDir1().equals(e.getInDir2())) {
                    writeLine(name + SEPARATOR + e.getInDir1() + SEPARATOR + e.getInDir2());
                }
            } else {
                writeLine(name + SEPARATOR + e.getInDir1() + SEPARATOR + e.getInDir2());
            }
        }
    }

    private ClassVersion getVersion(File classFile) {
        try {
            FileInputStream fis = new FileInputStream(classFile);
            DataInputStream dis = new DataInputStream(fis);
            dis.skipBytes(4);
            int minor = dis.readShort();
            int major = dis.readShort();
            return new ClassVersion(major, minor);
        }
        catch (FileNotFoundException fnfe) {
            throw new BuildException("file not found: " + classFile.getAbsolutePath());
        }
        catch (IOException ioe) {
            System.out.println("warning: could not read from file: " + classFile.getAbsolutePath());
        }
        return null;
    }

    private String getRelativeName(File f) {
        try {
            String fullName = f.getCanonicalPath();
            if (fullName.startsWith(dir1name)) {
                return fullName.substring(dir1name.length());
            } else if (fullName.startsWith(dir2name)) {
                return fullName.substring(dir2name.length());
            } else {
                throw new BuildException("file '" + fullName + "' is not in dir1 nor dir2");
            }
        }
        catch (IOException e) {
            throw new BuildException("could not get canonical path for " + f.getAbsolutePath());
        }
    }

    // ########################
    // # misc private methods #
    // ########################

    private boolean isJar(File f) {
        return f.getName().endsWith(".jar");
    }

    private boolean isClass(File f) {
        return f.getName().endsWith(".class");
    }

    private void writeLine(String s) {
        try {
            outputFile.write(s.getBytes());
            outputFile.write(NEWLINE);
        }
        catch (IOException e) {
            throw new BuildException("could not write to file '" + this.outputFileName + "'");
        }
    }

}
