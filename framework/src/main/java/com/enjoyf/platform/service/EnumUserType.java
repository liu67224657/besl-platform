/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.service;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.usertype.ParameterizedType;
import org.hibernate.usertype.UserType;

/**
 * This is a helper class to instruct Hibernate on how to set/get enum classes
 * <p/>
 * This class is generic enough to be reused in most classes.
 * <p/>
 * Here is an example of how to use it in mapping files:
 * <p/>
 * <property column="TYPE" name="type" not-null="true">
 * <type name="com.enjoyf.platform.db.customtype.StringEnumUserType">
 * <param name="className">com.enjoyf.platform.service.ladder.LadderType</param>
 * </type>
 * </property>
 * <p/>
 * 1) you need to know the type of value the enum is stored in database
 * if it is string, you should use StringEnumUserType
 * if it is a integer, you should use IntegerEnumUserType
 * 2) you need specify the enum class in "classNam" parameter
 * 3) optionally, you specify "getterName" parameter to instruct hibernate on how to get the db value out of enum object.
 * This class is smart enough to figure out the right method to use if there is only one method meeting the method signature.
 * 4) likewise, you can optionally specify "resolverName" parameter to instruct to resolve db values to enum objects
 * <p/>
 * Here is another example with all 3 parameters set:
 * <p/>
 * <property name="type" column="OWNER_TYPE">
 * <type name="com.enjoyf.platform.db.customtype.IntegerEnumUserType">
 * <param name="className">com.enjoyf.platform.service.OwnerType</param>
 * <param name="getterName">getValue</param>
 * <param name="resolverName">valueOf</param>
 * </type>
 * </property>
 * <p/>
 * NOTE:  all 3 parameters are needed in OwnerType because the OwnerType have alias methods for both getter and resolver methods.
 */
public abstract class EnumUserType implements UserType, ParameterizedType {
    public static final String CLASS_NAME = "className";
    public static final String GETTER_METHOD = "getterName";
    public static final String RESOLVER_METHOD = "resolverName";

    private Class clazz;
    private Method getterMethod;
    private Method resolverMethod;

    protected abstract Class primitiveType();

    public Class returnedClass() {
        return clazz;
    }

    public boolean equals(Object x, Object y) throws HibernateException {
        if (x == y) {
            return true;
        }
        if (null == x || null == y) {
            return false;
        }
        return x.equals(y);
    }

    public int hashCode(Object x) throws HibernateException {
        return x.hashCode();
    }

    public Object nullSafeGet(ResultSet rs, String[] names, Object owner) throws HibernateException, SQLException {
        Object id;

        switch (sqlTypes()[0]) {
            case Types.NUMERIC:
            case Types.INTEGER: {
                id = new Integer(rs.getInt(names[0]));
                break;
            }
            case Types.VARCHAR: {
                id = rs.getString(names[0]);
                break;
            }
            default: {
                throw new MappingException("unable mapping type: " + sqlTypes()[0]);
            }
        }

        try {
            Object returnObj = resolverMethod.invoke(null, new Object[]{id});

            return returnObj;
        } catch (Exception e) {
            throw new MappingException("unable to invoke resolver method: " + resolverMethod + ", e=" + e);
        }
    }

    public void nullSafeSet(PreparedStatement st, Object value, int index) throws HibernateException, SQLException {

        if (value == null) {
            st.setNull(index, sqlTypes()[0]);
        } else {
            try {
                Object finalValue = getterMethod.invoke(value, new Object[]{});

                st.setObject(index, finalValue);
            } catch (Exception e) {
                throw new MappingException("unable to invoke getter method: " + getterMethod + ", e=" + e);
            }
        }
    }

    public Object deepCopy(Object value) throws HibernateException {
        return value;
    }

    public boolean isMutable() {
        return false;
    }

    public Serializable disassemble(Object value) throws HibernateException {
        return (Serializable) value;
    }

    public Object assemble(Serializable cached, Object owner) throws HibernateException {
        return cached;
    }

    public Object replace(Object original, Object target, Object owner) throws HibernateException {
        return original;
    }


    public void setParameterValues(Properties parameters) {
        String className = parameters.getProperty(CLASS_NAME);
        String getterName = parameters.getProperty(GETTER_METHOD);
        String setterName = parameters.getProperty(RESOLVER_METHOD);

        if (className == null) {
            throw new MappingException(CLASS_NAME + " parameter not specified");
        }

        try {
            clazz = initClass(className);
            getterMethod = initGetterMethod(clazz, getterName);
            resolverMethod = initResolverMethod(clazz, setterName);
        } catch (SecurityException e) {
            throw new MappingException("unable to extract enum info: " + e);
        } catch (NoSuchMethodException e) {
            throw new MappingException("unable to extract enum info: " + e);
        }
    }

    private Class initClass(String className) {
        try {
            return Class.forName(className);
        } catch (java.lang.ClassNotFoundException e) {
            throw new MappingException("enumClass " + className + " not found", e);
        }
    }

    private Method initGetterMethod(Class clazz, String getterName) throws SecurityException, NoSuchMethodException {
        if (getterName != null && !getterName.trim().equals("")) {
            return clazz.getMethod(getterName, new Class[0]);
        } else {
            // do the best guess
            List getterList = new ArrayList();

            Method[] methods = clazz.getMethods();
            for (int i = 0; i < methods.length; i++) {
                Method method = methods[i];

                Class[] parameters = method.getParameterTypes();

                // skip if the method has any parameters
                if (parameters != null && parameters.length != 0) {
                    continue;
                }

                Class returnedType = method.getReturnType();

                // skip if the method does not return the right class
                if (!returnedType.equals(primitiveType())) {
                    continue;
                }

                // skipp if the method does not start with "get"
                if (!method.getName().startsWith("get")) {
                    continue;
                }

                getterList.add(method);
            }

            if (getterList.size() != 1) {
                throw new MappingException("unable to locate a SINGLE getter method: class: " + clazz.getName() + ", method(s) located: " + getterList);
            }

            return (Method) getterList.get(0);
        }
    }

    private Method initResolverMethod(Class clazz, String setterName) throws SecurityException, NoSuchMethodException {
        if (setterName != null && !setterName.trim().equals("")) {
            return clazz.getMethod(setterName, new Class[]{primitiveType()});
        } else {
            // do the best guess
            List setterList = new ArrayList();

            Method[] methods = clazz.getMethods();
            for (int i = 0; i < methods.length; i++) {
                Method method = methods[i];

                Class[] parameters = method.getParameterTypes();

                // skip if the method has any parameters
                if (parameters == null || parameters.length != 1) {
                    continue;
                }

                // skip if paramerter is not matched
                if (!primitiveType().equals(parameters[0])) {
                    continue;
                }

                // skip if method is not a static one
                if (!Modifier.isStatic(method.getModifiers())) {
                    continue;
                }

                Class returnedType = method.getReturnType();

                // skip if the method does not return the right class
                if (!returnedType.equals(returnedClass())) {
                    continue;
                }

                setterList.add(method);
            }

            if (setterList.size() != 1) {
                throw new MappingException("unable to locate a SINGLE resolver method: class: " + clazz.getName() + ", method(s) located: " + setterList);
            }

            return (Method) setterList.get(0);
        }
    }
}