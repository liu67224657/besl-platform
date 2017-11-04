/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.io.smime;

import java.util.HashMap;
import java.util.Map;

import com.enjoyf.platform.props.SMIMEProperties;

/**
 * Factory class to obtain a message generator that will
 * sign a email message.
 */
public class SMIMEFactory {
    private static SMIMEFactory sInstance = new SMIMEFactory();
    private static Map sProviders = new HashMap();

    private SMIMEProperties mProps = SMIMEProperties.instance();
    private Map mGenerators = new HashMap();

    static {
        sProviders.put("BC", SMIMEMessageGeneratorBC.class);
    }

    /**
     * Singleton
     */
    private SMIMEFactory() {

    }

    /**
     * Default instance.
     *
     * @return instance    SMIMEFactory
     */
    public static SMIMEFactory instance() {
        return sInstance;
    }

    /**
     * Obtain a message generator by provider and email address.
     *
     * @param emailAddr String, email address of the user sending the message.
     * @throws SMIMEException
     */
    public SMIMEMessageGenerator getMessageGenerator(String emailAddr)
            throws SMIMEException {
        if (emailAddr == null) {
            throw new IllegalArgumentException("SMIMEFactory.getMessageGenerator(): emailAddr cannot be null.");
        }

        String providerName = mProps.getProperties().get(emailAddr + ".provider");

        if (providerName == null) {
            throw new SMIMEException(SMIMEException.PROVIDER_NOT_FOUND);
        }

        Class providerClass = (Class) sProviders.get(providerName);

        if (providerClass == null) {
            throw new SMIMEException(SMIMEException.PROVIDER_NOT_FOUND);
        }

        SMIMEMessageGenerator gen = (SMIMEMessageGenerator) mGenerators.get(emailAddr);

        if (gen == null) {
            synchronized (this) {
                gen = (SMIMEMessageGenerator) mGenerators.get(emailAddr);

                if (gen == null) {
                    try {
                        gen = (SMIMEMessageGenerator) providerClass.newInstance();
                    }
                    catch (Exception e) {
                        throw new SMIMEException(SMIMEException.PROVIDER_NOT_FOUND, e.toString());
                    }

                    String publicKey = mProps.getProperties().get(emailAddr + ".cert.public");
                    String privateKey = mProps.getProperties().get(emailAddr + ".cert.private");

                    if (publicKey == null || privateKey == null) {
                        throw new SMIMEException(SMIMEException.CERT_NOT_FOUND);
                    }

                    gen.init(publicKey, privateKey);
                    mGenerators.put(emailAddr, gen);
                }
            }
        }

        return gen;
    }
}
