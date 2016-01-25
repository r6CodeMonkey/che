package util.security;

import model.security.User;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.Certificate;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v1CertificateBuilder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509ExtensionUtils;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.util.SubjectPublicKeyInfoFactory;
import org.bouncycastle.jce.PrincipalUtil;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.DefaultDigestAlgorithmIdentifierFinder;
import org.bouncycastle.operator.DefaultSignatureAlgorithmIdentifierFinder;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.bc.BcRSAContentSignerBuilder;

import java.io.IOException;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.Date;

/**
 * Created by timmytime on 28/12/15.
 */
public class CertificateBuilder {

    private static final BouncyCastleProvider bouncy = new BouncyCastleProvider();

    public static Certificate createRootCertificate(AsymmetricCipherKeyPair keyPair) throws OperatorCreationException, IOException {

        Security.addProvider(bouncy);


        AlgorithmIdentifier sigAlgId = new DefaultSignatureAlgorithmIdentifierFinder().find("SHA1withRSA");
        AlgorithmIdentifier digAlgId = new DefaultDigestAlgorithmIdentifierFinder().find(sigAlgId);

        Date firstDate = new Date();
        Date lastDate = new Date();

        lastDate.setTime(firstDate.getTime() + 3650L * 1000L * 60L * 60L * 24L);

        //we are self signing ourselves as root.  so they are the same
        String issuer = "C=UK, O=OddyMobstar, OU=OddyMobstar Root Certificate";
        String subject = "C=UK, O=OddyMobstar, OU=OddyMobstar Root Certificate";

        AsymmetricKeyParameter publicKey = keyPair.getPublic();


        X509v1CertificateBuilder certBuilder = new X509v1CertificateBuilder(
                new X500Name(issuer),
                BigInteger.valueOf(1),
                firstDate,
                lastDate,
                new X500Name(subject),
                SubjectPublicKeyInfoFactory.createSubjectPublicKeyInfo(publicKey)

        );


        AsymmetricKeyParameter privateKey = keyPair.getPrivate();

        ContentSigner sigGen = new BcRSAContentSignerBuilder(sigAlgId, digAlgId).build(privateKey);
        X509CertificateHolder certHolder = certBuilder.build(sigGen);


        return certHolder.toASN1Structure();
    }

    public static Certificate createClientCertificate(AsymmetricKeyParameter publicKey,
                                                      AsymmetricKeyParameter caPrivateKey,
                                                      X509Certificate caCert,
                                                      User client) throws CertificateEncodingException, NoSuchAlgorithmException, OperatorCreationException, IOException {

        Security.addProvider(bouncy);

        AlgorithmIdentifier sigAlgId = new DefaultSignatureAlgorithmIdentifierFinder().find("SHA1withRSA");
        AlgorithmIdentifier digAlgId = new DefaultDigestAlgorithmIdentifierFinder().find(sigAlgId);

        Date firstDate = new Date();
        Date lastDate = new Date();

        lastDate.setTime(firstDate.getTime() + 3650L * 1000L * 60L * 60L * 24L);


        X500NameBuilder nameBuilder = new X500NameBuilder();
        nameBuilder.addRDN(BCStyle.C, client.getCountry());
        nameBuilder.addRDN(BCStyle.O, client.getCompany());
        nameBuilder.addRDN(BCStyle.OU, client.getCompany());


        X509v3CertificateBuilder certBuilder = new X509v3CertificateBuilder(

                new X500Name(PrincipalUtil.getSubjectX509Principal(caCert).getName()),
                BigInteger.valueOf(2),
                firstDate,
                lastDate,
                nameBuilder.build(),
                SubjectPublicKeyInfoFactory.createSubjectPublicKeyInfo(publicKey));


        certBuilder.addExtension(
                Extension.subjectKeyIdentifier,
                false,
                new JcaX509ExtensionUtils().createSubjectKeyIdentifier(SubjectPublicKeyInfoFactory.createSubjectPublicKeyInfo(publicKey)));


        certBuilder.addExtension(
                Extension.authorityKeyIdentifier,
                false,
                new JcaX509ExtensionUtils().createAuthorityKeyIdentifier(caCert));


        certBuilder.addExtension(
                Extension.basicConstraints,
                true,
                new BasicConstraints(0));


        ContentSigner sigGen = new BcRSAContentSignerBuilder(sigAlgId, digAlgId).build(caPrivateKey);
        X509CertificateHolder certHolder = certBuilder.build(sigGen);


        return certHolder.toASN1Structure();
    }


}
