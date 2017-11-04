/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.db.social;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.social.RelationType;

import java.sql.Connection;
import java.util.Set;

/**
 * @author <a href=mailto:yinpengyi@fivewh.com>Yin Pengyi</a>
 */
interface RelationCategoryAccessor {
    //insert
    public boolean insert(String srcUno, String destUno, RelationType type, Set<Long> categoryIds, Connection conn) throws DbException;

    //query by type, scr uno and statuses
    public Set<Long> queryRelationCategories(String srcUno, String destUno, RelationType type, Connection conn) throws DbException;

    //remove the relation category.
    public boolean remove(String srcUno, String destUno, RelationType type, Connection conn) throws DbException;

    //remove the destuno from one category
    public boolean remove(String srcUno, String destUno, RelationType type, Long cateId, Connection conn) throws DbException;

    //remove all the uno in a cateId
    public boolean remove(String srcUno, RelationType type, Long cateId, Connection conn) throws DbException;
}
