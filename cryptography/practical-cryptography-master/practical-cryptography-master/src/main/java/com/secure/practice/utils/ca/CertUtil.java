package com.secure.practice.utils.ca;


import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

public class CertUtil {

	/**
	 * 通过给定证书别名，用证书库中的证书公钥给输入内容加密
	 * @param ks 证书库
	 * @param alias 证书别名
	 * @param input 输入内容
	 * @return 加密结果
	 */
	public static byte[] encodeByKeyStorePublicKey(KeyStore ks, String alias,
			byte[] input) {

		try {
			PublicKey pk = ks.getCertificate(alias).getPublicKey();
			return crypt(Cipher.ENCRYPT_MODE, pk, input);
		} catch (KeyStoreException e) {
			e.printStackTrace();
		}
		return null;

	}

	/**
	 * 通过给定证书别名，用证书库中的证书公钥给输入内容解密
	 * @param ks 证书库
	 * @param alias 证书别名
	 * @param input 输入内容
	 * @return 解密结果
	 */
	public static byte[] decodeByKeyStorePublicKey(KeyStore ks, String alias,
			byte[] input) {

		try {
			PublicKey pk = ks.getCertificate(alias).getPublicKey();
			return crypt(Cipher.DECRYPT_MODE, pk, input);
		} catch (KeyStoreException e) {
			e.printStackTrace();
		}
		return null;

	}

	/**
	 * 通过证书别名，和证书库密码获取证书私钥，并加密内容
	 * @param ks 证书库
	 * @param alias 证书别名
	 * @param certPass 证书密码
	 * @param input 输入内容
	 * @return 加密后内容
	 */
	public static byte[] decodeByKeyStorePrivateKey(KeyStore ks, String alias,
			String certPass, byte[] input) {
		PrivateKey pk;
		try {
			pk = (PrivateKey) ks.getKey(alias, certPass.toCharArray());
			return crypt(Cipher.DECRYPT_MODE, pk, input);
		} catch (UnrecoverableKeyException e) {
			e.printStackTrace();
		} catch (KeyStoreException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;

	}

	/**
	 * 通过证书别名获取证书私钥，并解密内容
	 * @param ks 证书库
	 * @param alias 别名
	 * @param certPass 证书密码
	 * @param input 输入内容
	 * @return 解密结果
	 */
	public static byte[] encodeByKeyStorePrivateKey(KeyStore ks, String alias,
			String certPass, byte[] input) {
		PrivateKey pk;
		try {
			pk = (PrivateKey) ks.getKey(alias, certPass.toCharArray());
			return crypt(Cipher.ENCRYPT_MODE, pk, input);
		} catch (UnrecoverableKeyException e) {
			e.printStackTrace();
		} catch (KeyStoreException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;

	}

	public static byte[] encodeByJKSPublicKey(String storePath,
			String storePass, String alias, byte[] msg) {
		return encodeByKeyStorePublicKey(storePath, storePass,
				DigitalCertificateGenerator.KEY_STORE_TYPE_JKS, alias, msg);
	}

	public static byte[] decodeByJKSPublicKey(String storePath,
			String storePass, String alias, byte[] msg) {
		return decodeByKeyStorePublicKey(storePath, storePass,
				DigitalCertificateGenerator.KEY_STORE_TYPE_JKS, alias, msg);
	}

	public static byte[] decodeByPFXPublicKey(String storePath,
			String storePass, String alias, byte[] msg) {
		return decodeByKeyStorePublicKey(storePath, storePass,
				DigitalCertificateGenerator.KEY_STORE_TYPE_PKCS12, alias, msg);
	}

	public static byte[] encodeByPFXPublicKey(String storePath,
			String storePass, String alias, byte[] msg) {
		return encodeByKeyStorePublicKey(storePath, storePass,
				DigitalCertificateGenerator.KEY_STORE_TYPE_PKCS12, alias, msg);
	}

	public static byte[] encodeByJKSPrivateKey(String storePath,
			String storePass, String alias, String certPass, byte[] msg) {
		return encodeByKeyStorePrivateKey(storePath, storePass,
				DigitalCertificateGenerator.KEY_STORE_TYPE_JKS, alias,
				certPass, msg);
	}

	public static byte[] decodeByJKSPrivateKey(String storePath,
			String storePass, String alias, String certPass, byte[] msg) {
		return decodeByKeyStorePrivateKey(storePath, storePass,
				DigitalCertificateGenerator.KEY_STORE_TYPE_JKS, alias,
				certPass, msg);
	}

	public static byte[] decodeByPFXPrivateKey(String storePath,
			String storePass, String alias, String certPass, byte[] msg) {
		return decodeByKeyStorePrivateKey(storePath, storePass,
				DigitalCertificateGenerator.KEY_STORE_TYPE_PKCS12, alias,
				certPass, msg);
	}

	public static byte[] encodeByPFXPrivateKey(String storePath,
			String storePass, String alias, String certPass, byte[] msg) {
		return encodeByKeyStorePrivateKey(storePath, storePass,
				DigitalCertificateGenerator.KEY_STORE_TYPE_PKCS12, alias,
				certPass, msg);
	}

	public static byte[] encodeByKeyStorePublicKey(String storePath,
			String storePass, String storeType, String alias, byte[] msg) {
		PublicKey pk = publicKeyInKeyStore(storePath, storePass, storeType,
				alias);
		return crypt(Cipher.ENCRYPT_MODE, pk, msg);
	}

	public static byte[] decodeByKeyStorePublicKey(String storePath,
			String storePass, String storeType, String alias, byte[] msg) {
		PublicKey pk = publicKeyInKeyStore(storePath, storePass, storeType,
				alias);
		return crypt(Cipher.DECRYPT_MODE, pk, msg);
	}

	public static byte[] encodeByKeyStorePrivateKey(String storePath,
			String storePass, String storeType, String alias, String certPass,
			byte[] msg) {

		PrivateKey pk = privateKeyInKeyStore(storePath, storePass, storeType,
				alias, certPass);

		return crypt(Cipher.ENCRYPT_MODE, pk, msg);

	}

	public static byte[] decodeByKeyStorePrivateKey(String storePath,
			String storePass, String storeType, String alias, String certPass,
			byte[] msg) {

		PrivateKey pk = privateKeyInKeyStore(storePath, storePass, storeType,
				alias, certPass);

		return crypt(Cipher.DECRYPT_MODE, pk, msg);

	}

	/**
	 *
	 * @param opmode 操作模式 1是加密，2是解密
	 * @param key 密钥
	 * @param input 输入内容
	 * @return 操作后的结果，如果产生异常返回null
	 */
	private static byte[] crypt(int opmode, Key key, byte[] input) {
		Cipher cipher;
		try {
			cipher = Cipher.getInstance(DigitalCertificateGenerator.KEY_PAIR_ALGORITHM_RSA);
			cipher.init(opmode, key);
			return cipher.doFinal(input);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return null;

	}

	/**
	 * 通过给定证书路径，获取证书公钥，解密内容
	 * @param certPath 证书
	 * @param msgData 内容
	 * @return 解密后的数据，如果产生异常，返回null
	 */
	public static byte[] decodeByCert(String certPath, byte[] msgData) {
		try {

			PublicKey pk = publicKeyInCert(certPath);

			Cipher cipher = Cipher.getInstance(DigitalCertificateGenerator.KEY_PAIR_ALGORITHM_RSA);

			cipher.init(Cipher.DECRYPT_MODE, pk);

			byte[] data = cipher.doFinal(msgData);

			return data;

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return null;

	}

	/**
	 * 利用证书公钥加密
	 * @param certPath 证书路径
	 * @param msgData 加密内容
	 * @return 加密结果
	 */
	public static byte[] encodeByCert(String certPath, byte[] msgData) {
		try {

			PublicKey pk = publicKeyInCert(certPath);

			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.ENCRYPT_MODE, pk);

			byte[] data = cipher.doFinal(msgData);

			return data;

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * PKCS12类型证书库获取证书公钥
	 * @param storePath 证书库路径
	 * @param storePass 证书库密码
	 * @param alias 证书别名
	 * @return 证书公钥
	 */
	public static PublicKey publicKeyInPFX(String storePath, String storePass,
			String alias) {
		return publicKeyInKeyStore(storePath, storePass,
				DigitalCertificateGenerator.KEY_STORE_TYPE_PKCS12, alias);
	}

	/**
	 * JKS类型证书库获取证书公钥
	 * @param storePath 证书库路径
	 * @param storePass 证书库密码
	 * @param alias 证书别名
	 * @return 证书公钥
	 */
	public static PublicKey publicKeyInJKS(String storePath, String storePass,
			String alias) {
		return publicKeyInKeyStore(storePath, storePass,
				DigitalCertificateGenerator.KEY_STORE_TYPE_JKS, alias);
	}

	/**
	 * 通过证书别名获取库中证书公钥
	 * @param storePath 证书库路径
	 * @param storePass 证书库密码（加载证书库需要证书库密码）
	 * @param storeType 证书类型
	 * @param alias 证书别名
	 * @return
	 */
	public static PublicKey publicKeyInKeyStore(String storePath,
			String storePass, String storeType, String alias) {
		//载入
		KeyStore ks = keyStoreLoad(storePath, storePass, storeType);
		try {
			return ks.getCertificate(alias).getPublicKey();
		} catch (KeyStoreException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * JKS证书库，通过证书别名获取证书私钥
	 * @param storePath 证书库路径
	 * @param storePass 证书库密码
	 * @param alias 证书别名
	 * @param certPass 证书密码
	 * @return 证书私钥
	 */
	public static PrivateKey privateKeyInJKS(String storePath,
			String storePass, String alias, String certPass) {
		return privateKeyInKeyStore(storePath, storePass,
				DigitalCertificateGenerator.KEY_STORE_TYPE_JKS, alias, certPass);
	}
	/**
	 * PKCS12证书库，通过证书别名获取证书私钥
	 * @param storePath 证书库路径
	 * @param storePass 证书库密码
	 * @param alias 证书别名
	 * @param certPass 证书密码
	 * @return 证书私钥
	 */
	public static PrivateKey privateKeyInPFX(String storePath,
			String storePass, String alias, String certPass) {
		return privateKeyInKeyStore(storePath, storePass,
				DigitalCertificateGenerator.KEY_STORE_TYPE_PKCS12, alias,
				certPass);
	}

	/**
	 * 通过证书别名从证书库中获取证书私钥
	 * @param storePath 证书库路径
	 * @param storePass 证书库密码
	 * @param storeType 证书库类型
	 * @param alias 证书别名
	 * @param certPass 证书密码
	 * @return 证书私钥
	 */
	public static PrivateKey privateKeyInKeyStore(String storePath,
			String storePass, String storeType, String alias, String certPass) {
		KeyStore ks = keyStoreLoad(storePath, storePass, storeType);
		PrivateKey pk = null;
		try {
			pk = (PrivateKey) ks.getKey(alias, certPass.toCharArray());
		} catch (UnrecoverableKeyException e) {
			e.printStackTrace();
		} catch (KeyStoreException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return pk;
	}

	/**
	 * 从文件中加载证书库
	 * @param storePath 证书库路径
	 * @param storePass 证书库密码
	 * @param storeType 证书库类型
	 * @return
	 */
	public static KeyStore keyStoreLoad(String storePath, String storePass,
			String storeType) {

		FileInputStream fis = null;

		try {
			fis = new FileInputStream(storePath);
			KeyStore ks = KeyStore.getInstance(storeType);
			ks.load(fis, storePass.toCharArray());
			return ks;
		} catch (KeyStoreException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (CertificateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fis != null) {
					fis.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return null;
	}

	/**
	 * 通过证书路径获取证书公钥
	 * @param certPath 证书路径
	 * @return 证书公钥
	 */
	public static PublicKey publicKeyInCert(String certPath) {

		return cert(certPath).getPublicKey();

	}
	/**
	 * 获取证书库路径获取JKS类型证书库中所有的证书别名列表
	 * @param storePath 证书库路径
	 * @param storePass 证书库密码
	 * @return 所有证书别名列表
	 */
	public static List<String> allAliasesInJKS(String storePath,
			String storePass) {
		return allAliasesInKeyStore(storePath,
				DigitalCertificateGenerator.KEY_STORE_TYPE_JKS, storePass);
	}
	/**
	 * 获取证书库路径获取PKCS12类型证书库中所有的证书别名列表
	 * @param storePath 证书库路径
	 * @param storePass 证书库密码
	 * @return 所有证书别名列表
	 */
	public static List<String> allAliasesInPFX(String storePath,
			String storePass) {
		return allAliasesInKeyStore(storePath,
				DigitalCertificateGenerator.KEY_STORE_TYPE_PKCS12, storePass);
	}

	/**
	 * 获取证书库路径获取证书库中所有的证书别名列表
	 * @param storePath 证书库路径
	 * @param keyStoreType 证书库类型
	 * @param storePass 证书库密码
	 * @return 所有证书别名列表
	 */
	public static List<String> allAliasesInKeyStore(String storePath,
			String keyStoreType, String storePass) {

		List<String> aliases = new ArrayList<String>();
		File f = new File(storePath);
		FileInputStream fis = null;
		KeyStore outStore;
		try {
			outStore = KeyStore.getInstance(keyStoreType);
			fis = new FileInputStream(f);
			outStore.load(fis, storePass.toCharArray());
			Enumeration<String> e = outStore.aliases();
			while (e.hasMoreElements()) {
				String alias = e.nextElement();
				aliases.add(alias);
			}
		} catch (KeyStoreException e1) {
			e1.printStackTrace();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		} catch (CertificateException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			try {
				if (fis != null) {
					fis.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return aliases;

	}

	/**
	 * 验证路径的证书是否有效
	 * @param certPath 证书路径
	 */
	public static void verifyValidityDays(String certPath) {

		X509Certificate cert = (X509Certificate) cert(certPath);
		try {
			cert.checkValidity(new Date());
		} catch (CertificateExpiredException e) {
			//如果证书过期，抛出此异常
			e.printStackTrace();
		} catch (CertificateNotYetValidException e) {
			//如果证书未生效，抛出此异常
			e.printStackTrace();
		}
	}

	public static String getSerialNumber(String certPath)
	{
		X509Certificate cert = (X509Certificate) cert(certPath);
		return cert.getSerialNumber().toString();
	}

	/**
	 * 用父证书验证子证书签名
	 * @param fatherCertPath 父证书路径
	 * @param sonCertPath 子证书路径
	 */
	public static void verifySign(String fatherCertPath, String sonCertPath) {

		Certificate son = cert(sonCertPath);
		Certificate father = cert(fatherCertPath);
		try {
			son.verify(father.getPublicKey());
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (CertificateException e) {
			//证书解码异常
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			//不支持的签名算法
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			//没有默认的提供者
			e.printStackTrace();
		} catch (SignatureException e) {
			//证书签名异常
			e.printStackTrace();
		}
	}
	/**
	 * 通过证书路径，获取证书，构造证书对象
	 * @param certPath 证书路径
	 * @return 证书对象
	 */
	public static Certificate cert(String certPath) {
		FileInputStream fis = null;
		try {
			CertificateFactory cf = CertificateFactory.getInstance("X509");
			fis = new FileInputStream(certPath);
			//通过文件输入流生成证书
			Certificate certificate = cf.generateCertificate(fis);
			try {
				fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return certificate;

		} catch (CertificateException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fis != null) {
					fis.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

}
