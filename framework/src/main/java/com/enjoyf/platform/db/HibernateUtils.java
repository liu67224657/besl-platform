package com.enjoyf.platform.db;

import org.hibernate.Session;
import org.hibernate.Transaction;

public class HibernateUtils {
    public static final String DOT = ".";
    public static final String PLUS = " + ";
    public static final String EQUAL = " = ";
    public static final String COLON = ":";
    public static final String SPACE = " ";
    public static final String COMMA = ", ";

    public static void closeSession(Session session) {
        if (session != null) {
            session.close();
        }
    }

    public static void rollBackTrans(Transaction trans) {
        if (trans != null) {
            trans.rollback();
        }
    }
}
