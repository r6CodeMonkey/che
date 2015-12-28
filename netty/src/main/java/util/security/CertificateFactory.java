package util.security;

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
import org.bouncycastle.crypto.generators.RSAKeyPairGenerator;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.params.RSAKeyGenerationParameters;
import org.bouncycastle.crypto.util.PrivateKeyFactory;
import org.bouncycastle.crypto.util.PrivateKeyInfoFactory;
import org.bouncycastle.crypto.util.SubjectPublicKeyInfoFactory;
import org.bouncycastle.jce.PrincipalUtil;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.DefaultDigestAlgorithmIdentifierFinder;
import org.bouncycastle.operator.DefaultSignatureAlgorithmIdentifierFinder;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.bc.BcRSAContentSignerBuilder;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Date;

/**
 * Created by timmytime on 28/12/15.
 */
public class CertificateFactory {

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


    public static KeyStore create(User root, User client) throws NoSuchAlgorithmException, KeyStoreException, IOException, CertificateException, OperatorCreationException, InvalidKeySpecException, UnrecoverableKeyException {

        Security.addProvider(bouncy);

        RSAKeyPairGenerator gen = new RSAKeyPairGenerator();

        gen.init(new RSAKeyGenerationParameters(
                new BigInteger("10001", 16),
                SecureRandom.getInstance("SHA1PRNG"),
                2048,
                80));


        AsymmetricCipherKeyPair keyPair = gen.generateKeyPair();

        Certificate[] certChain = new Certificate[1];
        certChain[0] = createRootCertificate(keyPair);

        AsymmetricKeyParameter privateKey = keyPair.getPrivate();
        KeyStore keyStore = createKeyStore(certChain, root, privateKey);


        //need key and certificate
        PrivateKey rootPrivateKey = (PrivateKey) keyStore.getKey(root.getCompany(), root.getPassword().toCharArray());
        java.security.cert.Certificate[] rootCertificate = keyStore.getCertificateChain(root.getCompany());


        AsymmetricKeyParameter privKeyParams = PrivateKeyFactory.createKey(rootPrivateKey.getEncoded());
        keyPair = gen.generateKeyPair();

        certChain = new Certificate[2];

        java.security.cert.CertificateFactory cf = java.security.cert.CertificateFactory.getInstance("X.509");

        InputStream is = new ByteArrayInputStream(rootCertificate[0].getEncoded());
        certChain[1] = org.bouncycastle.asn1.x509.Certificate.getInstance(rootCertificate[0].getEncoded());


        privateKey = keyPair.getPrivate();
        AsymmetricKeyParameter publicKey = keyPair.getPublic();

        certChain[0] = createClientCertificate(publicKey, privKeyParams, (X509Certificate) cf.generateCertificate(is), client);

        return createKeyStore(certChain, client, privateKey);

    }


    private static KeyStore createKeyStore(Certificate[] certChain, User user, AsymmetricKeyParameter privateKey) throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException, InvalidKeySpecException, CertificateException, InvalidKeySpecException {

        Security.addProvider(bouncy);

        KeyStore store = KeyStore.getInstance("PKCS12", bouncy);
        store.load(null, null);

        java.security.cert.CertificateFactory cf = java.security.cert.CertificateFactory.getInstance("X.509");
        java.security.cert.Certificate certificates[] = new java.security.cert.Certificate[certChain.length];

        int counter = 0;

        for (Certificate cert : certChain) {
            InputStream is = new ByteArrayInputStream(cert.getEncoded());
            certificates[counter] = cf.generateCertificate(is);
            counter++;
        }


        byte[] pkB = PrivateKeyInfoFactory.createPrivateKeyInfo(privateKey).getEncoded();

        KeyFactory keyFactory = KeyFactory.getInstance("RSA", bouncy);
        EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(pkB);
        PrivateKey pk = keyFactory.generatePrivate(privateKeySpec);


        store.setKeyEntry(user.getCompany(), pk, null, certificates);

        return store;
    }

    public static void main(String[] args) {
        //need latest certificates
        User root = new User("UK", "OddyMobstar", args[0]);
        //could be other games in future.
        User client = new User("UK", "ProjectChe", args[1]);

        try {
            KeyStore keyStore = create(root, client);
            FileOutputStream fOut = new FileOutputStream("/home/timmytime/keystore/" + client.getCompany() + ".p12");

            keyStore.store(fOut, client.getPassword().toCharArray());

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }
}
