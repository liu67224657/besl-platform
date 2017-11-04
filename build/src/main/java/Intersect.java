//
// (C) 2009 Fivewh platform platform.com
//

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Ant task which computes the intersection of two lists of tokens.
 */
@SuppressWarnings("rawtypes")
public class Intersect extends Task {
    private String mProp1;
    private String mProp2;
    private String mResult;

    public Intersect() {
    }

    public void setProp1(String prop1) {
        mProp1 = prop1;
    }

    public void setProp2(String prop2) {
        mProp2 = prop2;
    }

    public void setResult(String result) {
        mResult = result;
    }

	public void execute() throws BuildException {
        if (mProp1 == null) {
            throw new BuildException("prop1 is unset");
        }

        if (mProp2 == null) {
            throw new BuildException("prop2 is unset");
        }

        if (mResult == null) {
            throw new BuildException("no result property specified");
        }

        // Tokenize each property
        String props1[] = mProp1.split("\\s+");
        String props2[] = mProp2.split("\\s+");

        // Convert the arrays to sets
        Set set1 = new HashSet(Arrays.asList(props1));
        Set set2 = new HashSet(Arrays.asList(props2));

        // Compute the intersection of set1 and set2
        set1.retainAll(set2);

        // Convert to a string
        StringBuffer result = new StringBuffer();
        for (Iterator i = set1.iterator(); i.hasNext();) {
            result.append(i.next());
            if (i.hasNext()) {
                result.append(' ');
            }
        }

        // Return the result back to ant
        getProject().setProperty(mResult, result.toString());
    }
}
