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

package com.jsptags.navigation.pager.parser;

public final class IndexTagExport {

    public static final String
	ITEM_COUNT = "itemCount",
	PAGE_COUNT = "pageCount";

    private String itemCount = null;
    private String pageCount = null;

    final void setItemCount(String id) {
	itemCount = id;
    }

    final void setPageCount(String id) {
	pageCount = id;
    }

    public final String getItemCount() {
	return itemCount;
    }

    public final String getPageCount() {
	return pageCount;
    }
}
