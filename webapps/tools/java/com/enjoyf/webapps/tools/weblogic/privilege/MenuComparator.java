/**
 * 
 */
package com.enjoyf.webapps.tools.weblogic.privilege;

import com.enjoyf.platform.service.tools.PrivilegeResource;

import java.util.Comparator;

/**
 * @author zx
 *
 */
public class MenuComparator implements Comparator {

	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(Object o1, Object o2) {
		PrivilegeResource pres1 = (PrivilegeResource)o1;
		PrivilegeResource pres2 = (PrivilegeResource)o2;
		
		if(pres1.getOrderfield() > pres2.getOrderfield())
			return 1;
		else{
			if(pres1.getOrderfield() == pres2.getOrderfield())
				return 0;
			return 0;
		}
	}

}
