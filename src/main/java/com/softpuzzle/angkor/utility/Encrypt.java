package com.softpuzzle.angkor.utility;


import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.AlgorithmParameters;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

@Component
public class Encrypt {
	/**
	 * password용 암호화
	 * 
	 * @param value
	 * @return
	 * @throws Exception
	 */
	
	private static final String password = "i";
	private static String salt;
	private static int pswdIterations = 64;
	private static int keySize = 256;
	private static int saltlength = keySize / 8;
	private static byte[] ivBytes;
	
	public static String encriptSHA256(String value) throws Exception {
		String encryptStr = "";

		try{
			MessageDigest sha = MessageDigest.getInstance("SHA-256");
			sha.update(value.getBytes());

			byte[] digest = sha.digest();
			encryptStr = Hex.encodeHexString(digest);
		}catch (NoSuchAlgorithmException e){
			e.printStackTrace();
			encryptStr = null;
		}

		return encryptStr;
	}

	public static String encryptAES256(String plainText) {

		// get salt

		try {
			salt = generateSalt();
			byte[] saltBytes = salt.getBytes("UTF-8");

			// Derive the key
			SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
			PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), saltBytes, pswdIterations, keySize);

			SecretKey secretKey = factory.generateSecret(spec);
			SecretKeySpec secret = new SecretKeySpec(secretKey.getEncoded(), "AES");

			// encrypt the message
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, secret);
			AlgorithmParameters params = cipher.getParameters();
			ivBytes = params.getParameterSpec(IvParameterSpec.class).getIV();
			byte[] encryptedTextBytes = cipher.doFinal(plainText.getBytes("UTF-8"));

			// Base64 for Android
			// String encodedText = Base64.encodeToString(encryptedTextBytes,
			// Base64.DEFAULT);

			// Base64 for Java
			String encodedText = Base64.encodeBase64String(encryptedTextBytes);
			String encodedIV = Base64.encodeBase64String(ivBytes);
			String encodedSalt = Base64.encodeBase64String(saltBytes);
			String encodedPackage = encodedSalt + "]" + encodedIV + "]" + encodedText;
			return encodedPackage;

		}catch (Exception e) {
			// TODO: handle exception
			return null;
		}


	}

	public static String decryptAES256(String encryptedText) {

		try {
			String[] fields = encryptedText.split("]");
			byte[] saltBytes = Base64.decodeBase64(fields[0]);
			ivBytes = Base64.decodeBase64(fields[1]);
			byte[] encryptedTextBytes = Base64.decodeBase64(fields[2]);

			// Derive the key
			SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
			PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), saltBytes, pswdIterations, keySize);

			SecretKey secretKey = factory.generateSecret(spec);
			SecretKeySpec secret = new SecretKeySpec(secretKey.getEncoded(), "AES");

			// Decrypt the message
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(ivBytes));

			byte[] decryptedTextBytes =decryptedTextBytes = cipher.doFinal(encryptedTextBytes);
			return new String(decryptedTextBytes);
		} catch (Exception e) {

			return null;
		}

	}
	
	//랜덤하게 생성하여 적용
	//크기는 최소 48 비트 이상이며 난수 발생기에 의해서 사용 하도록 한다.
	public static String generateSalt() {
		SecureRandom random = new SecureRandom();
		byte bytes[] = new byte[saltlength];
		random.nextBytes(bytes);
		return new String(bytes);
	}

}