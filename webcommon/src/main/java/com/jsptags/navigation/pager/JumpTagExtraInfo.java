/*
 *  Pager Tag Library
 *
 *  Copyright (C) 2002  James Klicman <james@jsptags.com>
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2.1 of the License, or (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this library; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package com.jsptags.navigation.pager;

import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;

public final class JumpTagExtraInfo extends PageTagExtraInfo {

	public boolean isValid(TagData tagData) {
		if (!super.isValid(tagData))
			return false;

		Object val = tagData.getAttribute("unless");
		if (val != null && val != TagData.REQUEST_TIME_VALUE &&
			val instanceof String)
		{
			String unless = (String) val;

			return (JumpTagSupport.INDEXED.equals(unless) ||
					JumpTagSupport.CURRENT.equals(unless));
		}
		return true;
	}
}

/* vim:set ts=4 sw=4: */
