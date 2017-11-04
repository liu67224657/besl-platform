/**
 * (c) 2008 Fivewh.com
 */

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

public class FetchProperty extends Task {
    private String key;
    private String name;
    private String defaultValue = "";

    public FetchProperty() {
    }

    public void execute() throws BuildException {
        String value = getProject().getProperty(key);
        if (value == null) {
            value = defaultValue;
        }

        getProject().setProperty(name, value);
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
