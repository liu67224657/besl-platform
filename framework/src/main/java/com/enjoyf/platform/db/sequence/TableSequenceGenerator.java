/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.db.sequence;

import java.io.Serializable;
import java.util.Properties;

import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.id.Configurable;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.type.Type;

/**
 * @Auther: <a mailto:yinpengyi@platform.com>Yin Pengyi</a>
 */

/**
 * the id generator is use by hibernate. the configure file like: <id name="id" type="long" column="ID"> <generator class="com.enjoyf.platform.db.sequence.TableSequenceGenerator"> <param name="sequence">SEQ_TABLE_NAME_ID</param> </generator> </id>
 */
public class TableSequenceGenerator implements IdentifierGenerator, Configurable {
    private static final String KEY_PARAM_SEQUENCE = "sequence";
    private static final String DEFAULT_SEQUENCE_NAME = "DEFAULT";

    private String sequenceName = null;

    public Serializable generate(SessionImplementor sessionImplementor, Object object) throws HibernateException {
        long returnValue = -1;

        try {
            returnValue = TableSequenceFetcher.get().generate(sequenceName, sessionImplementor.connection());
        }
        catch (TableSequenceException e) {
            throw new HibernateException(e.getMessage(), e);
        }

        return returnValue;
    }

    public void configure(Type type, Properties properties, Dialect dialect) throws MappingException {
        if (properties == null) {
            sequenceName = DEFAULT_SEQUENCE_NAME;
        } else {
            sequenceName = properties.getProperty(KEY_PARAM_SEQUENCE, DEFAULT_SEQUENCE_NAME);
        }
    }
}
