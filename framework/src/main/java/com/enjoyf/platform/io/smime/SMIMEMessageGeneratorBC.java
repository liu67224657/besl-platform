/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.io.smime;

import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.Security;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.activation.CommandMap;
import javax.activation.MailcapCommandMap;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.smime.SMIMECapabilitiesAttribute;
import org.bouncycastle.asn1.smime.SMIMECapability;
import org.bouncycastle.asn1.smime.SMIMECapabilityVector;
import org.bouncycastle.jce.cert.CertStore;
import org.bouncycastle.jce.cert.CertStoreException;
import org.bouncycastle.jce.cert.CollectionCertStoreParameters;
import org.bouncycastle.mail.smime.SMIMESignedGenerator;

import com.enjoyf.platform.crypto.cert.PrivateKeyFactory;
import com.enjoyf.platform.util.log.GAlerter;

/**
 * Class S/MIME encode a message using
 * the bouncy-castle libraries
 */
public class SMIMEMessageGeneratorBC implements SMIMEMessageGenerator {
    public static final String PROVIDER = "BC";

    private X509Certificate mCert;
    private PrivateKey mKey;
    private SMIMESignedGenerator mGenerator;
    private CertStore mCertsAndCrls;

    static {
        try {
            Class c = Class.forName("org.bouncycastle.jce.provider.BouncyCastleProvider");
            Provider p = (Provider) c.newInstance();
            Security.addProvider(p);
        }
        catch (Exception ex) {
        }

        MailcapCommandMap mailcap = (MailcapCommandMap) CommandMap.getDefaultCommandMap();
        mailcap.addMailcap("application/pkcs7-signature;; x-java-content-handler=org.bouncycastle.mail.smime.handlers.pkcs7_signature");
        mailcap.addMailcap("application/pkcs7-mime;; x-java-content-handler=org.bouncycastle.mail.smime.handlers.pkcs7_mime");
        mailcap.addMailcap("application/x-pkcs7-signature;; x-java-content-handler=org.bouncycastle.mail.smime.handlers.x_pkcs7_signature");
        mailcap.addMailcap("application/x-pkcs7-mime;; x-java-content-handler=org.bouncycastle.mail.smime.handlers.x_pkcs7_mime");
        mailcap.addMailcap("multipart/signed;; x-java-content-handler=org.bouncycastle.mail.smime.handlers.multipart_signed");
        CommandMap.setDefaultCommandMap(mailcap);
    }

    /**
     * Initialize a SMIMEMessageGenerator for BouncyCastle provider.
     *
     * @param cert String, resource path to cert
     * @param key  String, resource path to key
     */
    public void init(String cert, String key) {
        if (cert == null) {
            throw new IllegalArgumentException("SMIMEMessageGeneratorBC.ctor(): cert cannot be null.");
        }

        if (key == null) {
            throw new IllegalArgumentException("SMIMEMessageGeneratorBC.ctor(): key cannot be null.");
        }

        try {
            InputStream in = this.getClass().getResourceAsStream(cert);
            mCert = (X509Certificate) CertificateFactory.getInstance("X.509", PROVIDER).generateCertificate(in);
            in.close();

            mKey = (PrivateKey) PrivateKeyFactory.getKey(key, PROVIDER);

            //--
            // Validate that the keys match.
            //--
            if (!mKey.getAlgorithm().equalsIgnoreCase(mCert.getPublicKey().getAlgorithm())) {
                throw new SMIMEException(SMIMEException.ALGORITHM_MISMATCH);
            }

            List certs = new ArrayList();
            certs.add(mCert);

            //--
            // Create a certificate store
            //--
            mCertsAndCrls = CertStore.getInstance("Collection", new CollectionCertStoreParameters(certs), PROVIDER);
            ASN1EncodableVector signedAttrs = new ASN1EncodableVector();
            SMIMECapabilityVector caps = new SMIMECapabilityVector();

            caps.addCapability(SMIMECapability.dES_EDE3_CBC);
            caps.addCapability(SMIMECapability.rC2_CBC, 128);
            caps.addCapability(SMIMECapability.dES_CBC);

            signedAttrs.add(new SMIMECapabilitiesAttribute(caps));

        }
        catch (Exception ex) {
            throw new SMIMEException(SMIMEException.GENERATOR_INIT_ERROR, ex.toString());
        }
    }

    /**
     * Generate a signed mime object based on S/MIME certificates and message body.
     *
     * @param msg MimeMessage
     * @return signedMsg    MimeMessage
     * @throws SMIMEException
     */
    public MimeMessage sign(MimeMessage msg)
            throws SMIMEException {
        try {
            SMIMESignedGenerator gen = new SMIMESignedGenerator();
            gen.addSigner(mKey, mCert, SMIMESignedGenerator.DIGEST_SHA1);
            gen.addCertificatesAndCRLs(mCertsAndCrls);

            MimeMultipart mp = gen.generate(msg, PROVIDER);

            MimeMessage signed = new MimeMessage((Session) null);

            Enumeration headers = msg.getAllHeaderLines();
            while (headers.hasMoreElements()) {
                signed.addHeaderLine((String) headers.nextElement());
            }

            signed.setContent(mp);

            return signed;
        }
        catch (NoSuchAlgorithmException nsae) {
            GAlerter.lab("SMIMEMessageGeneratorBC.sign(): Failed to sign a message.", nsae);
            throw new SMIMEException(SMIMEException.ALGORITHM_NOT_FOUND);
        }
        catch (CertStoreException cse) {
            GAlerter.lab("SMIMEMessageGeneratorBC.sign(): Failed to sign a message.", cse);
            throw new SMIMEException(SMIMEException.ALGORITHM_NOT_FOUND);
        }
        catch (NoSuchProviderException nspe) {
            GAlerter.lab("SMIMEMessageGeneratorBC.sign(): Failed to find a provider.", nspe);
            throw new SMIMEException(SMIMEException.PROVIDER_NOT_FOUND);
        }
        catch (org.bouncycastle.mail.smime.SMIMEException se) {
            throw new SMIMEException(SMIMEException.SIGN_ERROR, se.toString());
        }
        catch (MessagingException me) {
            throw new SMIMEException(SMIMEException.BUILD_MESSAGE_ERROR, me.toString());
        }
    }
}
