package com.enjoyf.platform.db.conn;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.SessionFactory;

import com.enjoyf.platform.db.DataSourceException;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.util.FiveProps;

public class DbSessionFactoryMgr {
	
    private static DbSessionFactoryMgr instance = new DbSessionFactoryMgr();

    private static Map<String, DbHibernateSessionFactory> factoryMap =
            new HashMap<String, DbHibernateSessionFactory>();

    DbSessionFactoryMgr() {
    }

    public static DbSessionFactoryMgr instance() {
        return instance;
    }

    public void init(List<String> names, FiveProps fiveProps) {
        for (String name : names) {
            DbHibernateSessionFactory factory;
            factory = new DbHibernateSessionFactory(name, fiveProps);

            if (factory != null) {
                factoryMap.put(name, factory);
            }
        }
    }

    public SessionFactory getFactory() throws DbException {
        if (factoryMap.size() > 0) {
            return getSessionFactory(factoryMap.values().iterator().next());
        }

        return getSessionFactory(null);
    }

    public Iterator<DbHibernateSessionFactory> iterator() {
        return factoryMap.values().iterator();
    }

    public SessionFactory getFactory(String dsName) throws DbException {
        if (factoryMap.size() > 0) {
            return getSessionFactory(factoryMap.get(dsName));
        }

        return getSessionFactory(null);
    }

    private SessionFactory getSessionFactory(DbHibernateSessionFactory hSessionFactory) throws DbException {
        if (hSessionFactory == null) {
            throw new DataSourceException(DataSourceException.DS_MISS_CONFIGURE);
        } else {
            return hSessionFactory.getSessionFactory();
        }
    }
}
