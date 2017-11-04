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
import com.jsptags.navigation.pager.parser.*;

public class PageTagExtraInfo extends TagExtraInfo {

	public VariableInfo[] getVariableInfo(TagData tagData) {
		String export = tagData.getAttributeString("export");
		if (export != null) {
			try {
				PageTagExport pageTagExport =
					TagExportParser.parsePageTagExport(export);
				int len = 0;
				if (pageTagExport.getPageUrl() != null)
					len++;
				if (pageTagExport.getPageNumber() != null)
					len++;
				if (pageTagExport.getFirstItem() != null)
					len++;
				if (pageTagExport.getLastItem() != null)
					len++;

				VariableInfo[] varinfo = new VariableInfo[len];
				int i = 0;

				String name;
				if ((name = pageTagExport.getPageUrl()) != null)
					varinfo[i++] = new VariableInfo(name,
							java.lang.String.class.getName(),
							true, VariableInfo.NESTED);
				if ((name = pageTagExport.getPageNumber()) != null)
					varinfo[i++] = new VariableInfo(name,
							java.lang.Integer.class.getName(),
							true, VariableInfo.NESTED);
				if ((name = pageTagExport.getFirstItem()) != null)
					varinfo[i++] = new VariableInfo(name,
							java.lang.Integer.class.getName(),
							true, VariableInfo.NESTED);
				if ((name = pageTagExport.getLastItem()) != null)
					varinfo[i++] = new VariableInfo(name,
							java.lang.Integer.class.getName(),
							true, VariableInfo.NESTED);

				return varinfo;
			} catch (ParseException ex)  {
				return new VariableInfo[0];
			}
		} else {
			return new VariableInfo[] {
					new VariableInfo(PageTagExport.PAGE_URL,
						java.lang.String.class.getName(),
						true, VariableInfo.NESTED),
					new VariableInfo(PageTagExport.PAGE_NUMBER,
						java.lang.Integer.class.getName(),
						true, VariableInfo.NESTED)
				};
		}
	}

	public boolean isValid(TagData tagData) {
		String export = tagData.getAttributeString("export");
		if (export != null) {
			try {
				TagExportParser.parsePageTagExport(export);
			} catch (ParseException ex)  {
				return false;
			}
		}
		return true;
	}
}

/* vim:set ts=4 sw=4: */
