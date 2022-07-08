package com.secure.practice.utils.ca;


import sun.security.tools.keytool.CertAndKeyGen;
import sun.security.x509.*;

import java.io.*;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.Random;

public class DigitalCertificateGenerator {

	//证书库类型
	public static final String KEY_STORE_TYPE_JKS = "jks";
	public static final String KEY_STORE_TYPE_PKCS12 = "pkcs12";
	//安全随机算法
	public static final String SECURE_RANDOM_ALGORITHM = "SHA1PRNG";
	//安全随机提供方
	public static final String SECURE_RANDOM_PROVIDER = "SUN";
	//签名算法
	public static final String SIGN_ALGORITHM_SHA256 = "sha256WithRSA";
	public static final String SIGN_ALGORITHM_MD5 = "MD5WithRSA";
	//密钥对算法
	public static final String KEY_PAIR_ALGORITHM_RSA = "RSA";

	public static void signCertPFXForSubject(SignedCertInfo signedCertInfo) {
		try {
			//子证书信息
			X500Name subject = new X500Name("CN=" + signedCertInfo.getCN()
					+ ",OU=" + signedCertInfo.getOU() + ",O="
					+ signedCertInfo.getO() + ",L=" + signedCertInfo.getL()
					+ ",ST=" + signedCertInfo.getST() + ",C="
					+ signedCertInfo.getC());

			issueSignedCert(signedCertInfo.getKeyStorePath(),
					signedCertInfo.getKeyStorePass(), KEY_STORE_TYPE_PKCS12,
					signedCertInfo.getIssuerAlias(),
					signedCertInfo.getIssuerAliasPass(),
					signedCertInfo.getSubjectAlias(),
					signedCertInfo.getSubjectAliasPass(), subject,
					signedCertInfo.getStart(),
					signedCertInfo.getValidity(),
					signedCertInfo.getSubjectPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}



	public static void signCertJKSForSubject(SignedCertInfo signedCertInfo) {
		try {
			//子证书信息
			X500Name subject = new X500Name("CN=" + signedCertInfo.getCN()
					+ ",OU=" + signedCertInfo.getOU() + ",O="
					+ signedCertInfo.getO() + ",L=" + signedCertInfo.getL()
					+ ",ST=" + signedCertInfo.getST() + ",C="
					+ signedCertInfo.getC());

			issueSignedCert(signedCertInfo.getKeyStorePath(),
					signedCertInfo.getKeyStorePass(), KEY_STORE_TYPE_JKS,
					signedCertInfo.getIssuerAlias(),
					signedCertInfo.getIssuerAliasPass(),
					signedCertInfo.getSubjectAlias(),
					signedCertInfo.getSubjectAliasPass(), subject,
					signedCertInfo.getStart(),
					signedCertInfo.getValidity(),
					signedCertInfo.getSubjectPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 利用颁发者证书，签发一个具有指定信息的子证书，并存储到证书库中
	 * @param keyStorePath 证书库路径
	 * @param keyStorePass 证书库密码
	 * @param keyStoreType 证书库类型
	 * @param issuerAlias 颁发者证书别名
	 * @param issuerAliasPass 颁发者密码
	 * @param subjectAlias 子证书别名
	 * @param subjectAliasPass 子证书密码
	 * @param subject 子证书详细信息
	 * @param validity 有效时间，单位：天
	 * @param subjectPath 子证书路径
	 */
	public static void issueSignedCert(String keyStorePath,
			String keyStorePass, String keyStoreType, String issuerAlias,
			String issuerAliasPass, String subjectAlias,
			String subjectAliasPass, X500Name subject, Date start, int validity,
			String subjectPath) {

		FileOutputStream fos = null;
		FileOutputStream keyStoreFos = null;

		FileInputStream fis = null;

		try {
			fis = new FileInputStream(keyStorePath);
			KeyStore ks = KeyStore.getInstance(keyStoreType);
			ks.load(fis, keyStorePass.toCharArray());
			//颁发者证书
			X509Certificate issuerCert = (X509Certificate) ks.getCertificate(issuerAlias);
			//构造颁发者证书对象
			X509CertImpl issuerCertImpl = new X509CertImpl(issuerCert.getEncoded());
			//获取颁发者信息
			X509CertInfo issuerCertInfo = (X509CertInfo) issuerCertImpl.get(X509CertImpl.NAME + "." + X509CertImpl.INFO);
			//颁发者信息
			X500Name issuer = (X500Name) issuerCertInfo
					.get(X509CertInfo.SUBJECT + "."
							+ CertificateIssuerName.DN_NAME);
			//获取颁发者的私钥
			PrivateKey pk = (PrivateKey) ks.getKey(issuerAlias, issuerAliasPass.toCharArray());

			//生成密钥和签发的证书
			CertAndKeyGen cakg = new CertAndKeyGen(KEY_PAIR_ALGORITHM_RSA,
					SIGN_ALGORITHM_SHA256);
			SecureRandom sr = SecureRandom.getInstance(SECURE_RANDOM_ALGORITHM,
					SECURE_RANDOM_PROVIDER);
			cakg.setRandom(sr);
			cakg.generate(2048);

			X509CertInfo info = new X509CertInfo();
			//设置证书版本
			info.set(X509CertInfo.VERSION, new CertificateVersion(
					CertificateVersion.V3));
			//设置证书序列号
			info.set(X509CertInfo.SERIAL_NUMBER, new CertificateSerialNumber(
					new Random().nextInt() & 0x7fffffff));
			//设置证书算法
			AlgorithmId aid = AlgorithmId.get(SIGN_ALGORITHM_SHA256);
			info.set(X509CertInfo.ALGORITHM_ID, new CertificateAlgorithmId(aid));
			//设置证书SUBJECT
			info.set(X509CertInfo.SUBJECT, subject);
			//设置证书颁发者
			info.set(X509CertInfo.ISSUER, issuer);
			//设置证书公钥
			info.set(X509CertInfo.KEY, new CertificateX509Key(cakg.getPublicKey()));
			//设置有效时间
			Date fistDate;
			if (start!=null)
			{
				fistDate = start;
			}
			else
			 	fistDate = new Date();
			Date lastDate = new Date();
			lastDate.setTime(fistDate.getTime() + (validity * 24L * 60L * 60L * 1000));
			CertificateValidity interval = new CertificateValidity(fistDate, lastDate);
			info.set(X509CertInfo.VALIDITY, interval);
			//获取子证书
			X509CertImpl cert = new X509CertImpl(info);
			//利用颁发者私钥给自己（子证书）签名
			cert.sign(pk, SIGN_ALGORITHM_SHA256);
			//获取到最终的子证书
			X509Certificate subjectCert = cert;
			//证书链 { 子证书、父证书 }
			X509Certificate[] chain = new X509Certificate[] { subjectCert, issuerCert };
			//存放到证书库中
			ks.setKeyEntry(subjectAlias, cakg.getPrivateKey(), subjectAliasPass.toCharArray(), chain);
			//证书库文件输出流
			keyStoreFos = new FileOutputStream(keyStorePath);
			//利用证书库密码加密证书库之后保存到流中（文件中）
			ks.store(keyStoreFos, keyStorePass.toCharArray());
			//子证书输出流
			fos = new FileOutputStream(subjectPath);
			//子证书输出
			fos.write(cert.getEncoded());
			fos.flush();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (CertificateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SignatureException e) {
			e.printStackTrace();
		} catch (KeyStoreException e) {
			e.printStackTrace();
		} catch (UnrecoverableKeyException e) {
			e.printStackTrace();
		}
		finally {
			try {
				if (fos != null) {
					fos.close();
				}
				if (fis != null) {
					fis.close();
				}
				if (keyStoreFos != null) {
					keyStoreFos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 通过证书别名从证书库导出证书
	 * @param keyStorePathAndFileName 证书库文件名（路径）
	 * @param keyStorePass 证书库密码
	 * @param alias 证书别名
	 * @param exportPathAndFileName 导出路径
	 */
	public static void exportPFXPublicKeyCertificate(
			String keyStorePathAndFileName, String keyStorePass, String alias,
			String exportPathAndFileName) {

		exportPublicKeyCertificate(keyStorePathAndFileName, keyStorePass,
				KEY_STORE_TYPE_PKCS12, alias, exportPathAndFileName);

	}
	/**
	 * 通过证书别名从证书库导出证书
	 * @param keyStorePathAndFileName 证书库文件名（路径）
	 * @param keyStorePass 证书库密码
	 * @param alias 证书别名
	 * @param exportPathAndFileName 导出路径
	 */
	public static void exportJKSPublicKeyCertificate(
			String keyStorePathAndFileName, String keyStorePass, String alias,
			String exportPathAndFileName) {

		exportPublicKeyCertificate(keyStorePathAndFileName, keyStorePass,
				KEY_STORE_TYPE_JKS, alias, exportPathAndFileName);

	}

	/**
	 * 通过证书别名从证书库导出证书
	 * @param keyStorePathAndFileName 证书库文件名（路径）
	 * @param keyStorePass 证书库密码
	 * @param keyStoreType 证书库种类
	 * @param alias 证书别名
	 * @param exportPathAndFileName 导出路径
	 */
	public static void exportPublicKeyCertificate(
			String keyStorePathAndFileName, String keyStorePass,
			String keyStoreType, String alias, String exportPathAndFileName) {
		FileOutputStream fos = null;
		FileInputStream fis = null;
		try {
			KeyStore ks = KeyStore.getInstance(keyStoreType);
			fis = new FileInputStream(keyStorePathAndFileName);
			ks.load(fis, keyStorePass.toCharArray());
			Certificate cert = ks.getCertificate(alias);
			fos = new FileOutputStream(exportPathAndFileName);
			fos.write(cert.getEncoded());
			fos.flush();

		} catch (KeyStoreException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (CertificateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fos != null) {
					fos.close();
				}
				if (fis != null) {
					fis.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void generatePFX(String alias, String keyStorePass,
			String certPass, String CN, String OU, String O, String L,
			String ST, String C, Date start, long validityDays,
			String pathAndFileName) {
		generateDigitalCert(KEY_STORE_TYPE_PKCS12, SIGN_ALGORITHM_SHA256,
				KEY_PAIR_ALGORITHM_RSA, SECURE_RANDOM_ALGORITHM,
				SECURE_RANDOM_PROVIDER, alias, keyStorePass, certPass, CN, OU,
				O, L, ST, C, start, validityDays, pathAndFileName, true);

	}

	public static void addNewCert2PFX(String alias, String keyStorePass,
			String certPass, String CN, String OU, String O, String L,
			String ST, String C, Date start, long validityDays,
			String pathAndFileName) {
		generateDigitalCert(KEY_STORE_TYPE_PKCS12, SIGN_ALGORITHM_SHA256,
				KEY_PAIR_ALGORITHM_RSA, SECURE_RANDOM_ALGORITHM,
				SECURE_RANDOM_PROVIDER, alias, keyStorePass, certPass, CN, OU,
				O, L, ST, C, start, validityDays, pathAndFileName, false);

	}

	public static void addNewCert2PFX(KeyStoreInfo certInfo) {
		generateDigitalCert(KEY_STORE_TYPE_PKCS12, SIGN_ALGORITHM_SHA256,
				KEY_PAIR_ALGORITHM_RSA, SECURE_RANDOM_ALGORITHM,
				SECURE_RANDOM_PROVIDER, certInfo.getAlias(),
				certInfo.getKeyStorePass(), certInfo.getCertPass(),
				certInfo.getCN(), certInfo.getOU(), certInfo.getO(),
				certInfo.getL(), certInfo.getST(), certInfo.getC(),
				certInfo.getStart(), certInfo.getValidityDays(),
				certInfo.getPathAndFileName(), false);
	}

	public static void generatePFX(KeyStoreInfo certInfo) {
		generateDigitalCert(KEY_STORE_TYPE_PKCS12, SIGN_ALGORITHM_SHA256,
				KEY_PAIR_ALGORITHM_RSA, SECURE_RANDOM_ALGORITHM,
				SECURE_RANDOM_PROVIDER, certInfo.getAlias(),
				certInfo.getKeyStorePass(), certInfo.getCertPass(),
				certInfo.getCN(), certInfo.getOU(), certInfo.getO(),
				certInfo.getL(), certInfo.getST(), certInfo.getC(),
				certInfo.getStart(), certInfo.getValidityDays(),
				certInfo.getPathAndFileName(), true);
	}

	public static void generateJKS(String alias, String keyStorePass,
			String certPass, String CN, String OU, String O, String L,
			String ST, String C, Date start, long validityDays,
			String pathAndFileName) {
		generateDigitalCert(KEY_STORE_TYPE_JKS, SIGN_ALGORITHM_SHA256,
				KEY_PAIR_ALGORITHM_RSA, SECURE_RANDOM_ALGORITHM,
				SECURE_RANDOM_PROVIDER, alias, keyStorePass, certPass, CN, OU,
				O, L, ST, C, start, validityDays, pathAndFileName, true);
	}

	public static void addNewCert2JKS(String alias, String keyStorePass,
			String certPass, String CN, String OU, String O, String L,
			String ST, String C, Date start, long validityDays,
			String pathAndFileName) {
		generateDigitalCert(KEY_STORE_TYPE_JKS, SIGN_ALGORITHM_SHA256,
				KEY_PAIR_ALGORITHM_RSA, SECURE_RANDOM_ALGORITHM,
				SECURE_RANDOM_PROVIDER, alias, keyStorePass, certPass, CN, OU,
				O, L, ST, C, start, validityDays, pathAndFileName, false);
	}

	/**
	 * 生成一个证书库，证书库中只有这一个证书
	 * @param certInfo 证书库信息
	 */
	public static void generateJKS(KeyStoreInfo certInfo) {
		generateDigitalCert(KEY_STORE_TYPE_JKS, SIGN_ALGORITHM_SHA256,
				KEY_PAIR_ALGORITHM_RSA, SECURE_RANDOM_ALGORITHM,
				SECURE_RANDOM_PROVIDER, certInfo.getAlias(),
				certInfo.getKeyStorePass(), certInfo.getCertPass(),
				certInfo.getCN(), certInfo.getOU(), certInfo.getO(),
				certInfo.getL(), certInfo.getST(), certInfo.getC(),
				certInfo.getStart(), certInfo.getValidityDays(),
				certInfo.getPathAndFileName(), true);
	}

	public static void addNewCert2JKS(KeyStoreInfo certInfo) {
		generateDigitalCert(KEY_STORE_TYPE_JKS, SIGN_ALGORITHM_SHA256,
				KEY_PAIR_ALGORITHM_RSA, SECURE_RANDOM_ALGORITHM,
				SECURE_RANDOM_PROVIDER, certInfo.getAlias(),
				certInfo.getKeyStorePass(), certInfo.getCertPass(),
				certInfo.getCN(), certInfo.getOU(), certInfo.getO(),
				certInfo.getL(), certInfo.getST(), certInfo.getC(),
				certInfo.getStart(), certInfo.getValidityDays(),
				certInfo.getPathAndFileName(), false);
	}

	/**
	 * 利用证书库根证书签名一个证书并加入到证书库中
	 * @param keyStoreType 证书库类型
	 * @param signAlgorithm 签名算法
	 * @param keyPairAlgorithm 密钥生成算法
	 * @param secureRandomAlgorithm 安全随机算法
	 * @param secureRandomProvider 安全随机提供者
	 * @param alias 别名
	 * @param keyStorePass 证书库密码
	 * @param certPass 证书密码（不是证书密钥，密钥是现生成的）
	 * @param CN 用户名称
	 * @param OU 机构名称
	 * @param O 组织名称
	 * @param L 地市
	 * @param ST 省份
	 * @param C 国家
	 * @param start 开始时间
 	 * @param validityDays 有效时间
	 * @param pathAndFileName 路径和文件名
	 * @param createNew 是否创建一个新的证书库，如果原本存在证书库文件会发生覆盖
	 */
	public static void generateDigitalCert(String keyStoreType,
			String signAlgorithm, String keyPairAlgorithm,
			String secureRandomAlgorithm, String secureRandomProvider,
			String alias, String keyStorePass, String certPass, String CN,
			String OU, String O, String L, String ST, String C, Date start,
			long validityDays, String pathAndFileName, boolean createNew) {
		FileOutputStream out = null;
		try {
			SecureRandom sr = SecureRandom.getInstance(secureRandomAlgorithm,
					secureRandomProvider);
			CertAndKeyGen cakg = new CertAndKeyGen(keyPairAlgorithm,
					signAlgorithm);
			cakg.setRandom(sr);
			cakg.generate(2048);
			X500Name subject = new X500Name("CN=" + CN + ",OU=" + OU + ",O="
					+ O + ",L=" + L + ",ST=" + ST + ",C=" + C);

			X509Certificate certificate = cakg.getSelfCertificate(subject,
					start, validityDays * 24L * 60L * 60L);
			KeyStore outStore = KeyStore.getInstance(keyStoreType);
			if (createNew) {
				outStore.load(null, keyStorePass.toCharArray());
				outStore.setKeyEntry(alias, cakg.getPrivateKey(),
						certPass.toCharArray(),
						new Certificate[] { certificate });
			} else {
				File f = new File(pathAndFileName);
				if (!f.exists()) {
					throw new FileNotFoundException("证书库文件不存在,不能把新的证书加入到证书库.");
				}

				FileInputStream fis = new FileInputStream(f);
				outStore.load(fis, keyStorePass.toCharArray());
				fis.close();
				outStore.setKeyEntry(alias, cakg.getPrivateKey(),
						certPass.toCharArray(),
						new Certificate[] { certificate });

			}
			out = new FileOutputStream(pathAndFileName);
			outStore.store(out, keyStorePass.toCharArray());

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (CertificateException e) {
			e.printStackTrace();
		} catch (SignatureException e) {
			e.printStackTrace();
		} catch (KeyStoreException e) {
			e.printStackTrace();
		} finally {

			try {
				if (out != null) {
					out.close();
					out = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

}
