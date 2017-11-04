package com.enjoyf.platform.db.conn;

import java.util.Properties;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.log.GAlerter;

public class DbHibernateSessionFactory {
    private SessionFactory sessionFactory;
    private Configuration hibernateCfg;

    public DbHibernateSessionFactory(String name, FiveProps fiveProps) {
        // Hibernate initialization
        Configuration cfg = new Configuration();

        try {
            Properties props = new Properties();
            props.putAll(fiveProps.getProps());
            props.setProperty(FivewhConnectionProvider.PREFIX_NAME, name);
            cfg.addProperties(props);

            hibernateCfg = cfg;
        } catch (HibernateException e) {
            GAlerter.lab("The database configure failed.", e);
        }
    }


    public SessionFactory getSessionFactory() {
        init();

        return sessionFactory;
    }

    public Configuration getHibernateCfg() {
        return hibernateCfg;
    }

    private synchronized void init() {
        if (sessionFactory == null) {
            try {
                sessionFactory = hibernateCfg.buildSessionFactory();
            } catch (HibernateException e) {
                GAlerter.lab("DbHibernateSessionFactory init failed.", e);
            }
        }
    }
}
