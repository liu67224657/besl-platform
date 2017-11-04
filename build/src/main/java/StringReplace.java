import org.apache.tools.ant.BuildException;

/**
 * this task takes in a string (setString), replaces all occurences
 * of a token (setToken) within that string with the specified value
 * (setValue), and stores the result in a specified property (setProperty)
 */
public class StringReplace extends org.apache.tools.ant.taskdefs.Property {
    private String string = null;
    private String property = null;
    private String token = null;
    private String value = null;

    public StringReplace() {
        // do nothing
    }

    public void execute() throws BuildException {
        addProperty(property, string.replaceAll(token, value));
    }

    public void setString(String string) {
        this.string = string;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setProperty(String property) {
        this.property = property;
    }
}
