package com.softpuzzle.angkor.utility;

import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.util.Pair;
import org.springframework.util.Base64Utils;

import javax.crypto.Cipher;
import java.util.Base64;

@Slf4j
public class RSAEncryptionUtil {

	public static void main(String[] args) {
		try {
			// Encrypt a sample string
			String pKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEArH4zZoUJbjipgElrWDupjRWGhgZSPgG5zmF1g8jx2H6CkpYOQFBiittqOXGSsJjW6aKoeLnLKLC6gIF/2ob3d1pzWcvCjYrQiydekspIqwEVRoK/WkzntlNDHG82gXI4I5o9OH14+/Ep228UvxH2EzregDoP6NxKVS9HkbsarBEhNbmcR9bpBvHW9kyyx5vgLuL31Z3ekiChxR6eQlqLodyr/DBzN7De1EAuw/C+RCnuNmlwt1VKPXcDqWkvKzl1yXc7MkHbdHF82guTh9XpyvT4NtLz2WJOioNNBZFKFiY765YD899EiR/KSyug9iP869IshPl3ttWpmGK/scdiuQIDAQAB";
			String publicKey = pKey.replaceAll("\\s", "");
			System.out.println("publicKey text: " + publicKey);

			// Decrypt the encrypted string
			String uPKey = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCsfjNmhQluOKmASWtYO6mNFYaGBlI+AbnOYXWDyPHYfoKSlg5AUGKK22o5cZKwmNbpoqh4ucsosLqAgX/ahvd3WnNZy8KNitCLJ16SykirARVGgr9aTOe2U0McbzaBcjgjmj04fXj78SnbbxS/EfYTOt6AOg/o3EpVL0eRuxqsESE1uZxH1ukG8db2TLLHm+Au4vfVnd6SIKHFHp5CWouh3Kv8MHM3sN7UQC7D8L5EKe42aXC3VUo9dwOpaS8rOXXJdzsyQdt0cXzaC5OH1enK9Pg20vPZYk6Kg00FkUoWJjvrlgPz30SJH8pLK6D2I/zr0iyE+Xe21amYYr+xx2K5AgMBAAECggEADaRBbmcxEr16ckHcMnMtepHsPKc9U9gRGMhOYei/b665ptwlLmLtGCw4H2jjAAJmZL3PcxTGg2ZOqaEUOnUYFyFZJA3gSg9jZM3tRdCnpJaGVzg55+tTG/FDynjlJ6Gf3PjyxIfvzIULGWaUjnPeMTEjraJp5bGeU1RU5aqVdmwXCpZncEDqlZZai90vPxcHdhQ9eI0G7m/d6FQSeVMjFzNUYf6KgclLYZtHJQtn7zNJ2Cy4xpDEqkSmjA5/t3OATtJqaa7X6GSTfvkAy8vNxEP4a0UuX718qq4V3Dg9FZUWbCYqoZCCg+42IT2qWhUqV7IziGw6M4Th5ptBr5dRDQKBgQDjzN02zy7/VDIuqdnGafptMQ+oHCFdq+flXAZ/CvKKe8y+sd6uJpThJmyExwP3iMTPBLu2LD+4V58/VCNadZB1Nus4hBZboxhBPnT6TIlF7VDSBDAFiDBQt4L3O/Prqo2EiMH/XIDvNXhiqRzFcOsLhU73f2gkjPYGPYMLGlZOzwKBgQDB2J0TANATqCHkWU3fqIuKi9fHaYuLWPhDF/GbEpQwpbhsqCsey98IWkWfzBkhrr1VjVKaOgE1e67tvP6FCtnc8daUxxmwW9INQDAe/umUsu4OOw2Jdku0a0ozABboev+Zo+XopgfxPIChASXxnHXr4Bbg7OImLgS2AEfb9vdX9wKBgFFYt+o5pBPDkkP9nWnwMGBLs8aZfQTBPYc3DI7Tbvcq7Ftwkw9ncaTRHtFvbCg56+XtT8bnGD4Q9CoeNp23+8UPvoGpEX7Zx/CMiCNVr3OMIl9M5YUkM/SvA5KcEyel+PFKENTKyLQjSQEaWM7c2uJ7UU6qbZsXx0dFMgWF+yobAoGAP5iOZ9ajbV+CxwTZfKAyL6ETiNPCuVPWF0T6uhKNVax45gUCKJdgM8BSCUwPjrJiYXf8+qY/EUnjqRGROxrFQSyCZssvTWHCdyBw6Spefa96VGbhWRDbY3SzoinMWjdINiLEYeBeyLE9zhSvWyYmtd0OBHsS8s20M/XBnZPXsB8CgYEAtLsc/FdWsKERHvNHCao7OyM/sx6QnH3LzmjwR8K3omAPW0bW+EAwxuzhZi3OhmTT8ziU2TjEGqzTtcfwnBlsVu+LyCe3R3I2l5qLmIZ4KicM2Bq2xH66k8Ur5iityuKusfNG4erJ9zKZRv11FirVt2H5N+pWR6RjnU32CDl+sUQ=";
			String uPrivateKey = uPKey.replaceAll("\\s", "");
			System.out.println("uPrivateKey text: " + uPrivateKey);

			// Generate RSA key pair
			Pair<String, String> keyPair = generateRSAKeyPair();
			String privateKey = keyPair.getFirst();
			System.out.println("privateKey text: " + privateKey);

			// Encrypt a sample string
			String encryptedText = encryptStringRSA(publicKey, privateKey);
			System.out.println("Encrypted text: " + encryptedText);

			// Decrypt the encrypted string
			String decryptedText = decryptStringRSA(uPrivateKey, encryptedText);
			System.out.println("Decrypted text: " + decryptedText);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Pair<String, String> generateRSAKeyPair() throws NoSuchAlgorithmException {
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
		keyPairGenerator.initialize(2048); // 키 크기 설정

		KeyPair keyPair = keyPairGenerator.generateKeyPair();

		String publicKey = Base64Utils.encodeToString(keyPair.getPublic().getEncoded());
		String privateKey = Base64Utils.encodeToString(keyPair.getPrivate().getEncoded());

		return Pair.of(privateKey, publicKey);
	}

	public static String encryptStringRSA(String publicKeyString, String inputData) throws Exception {
		byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyString);
		X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);

		byte[] inputBytes = inputData.getBytes();
		List<byte[]> encryptedBlocks = new ArrayList<>();

		int blockSize = 245;
		int offset = 0;
		while (offset < inputBytes.length) {
			byte[] block;
			if (offset + blockSize <= inputBytes.length) {
				block = cipher.doFinal(inputBytes, offset, blockSize);
			} else {
				block = cipher.doFinal(inputBytes, offset, inputBytes.length - offset);
			}
			encryptedBlocks.add(block);
			offset += blockSize;
		}

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		for (byte[] encryptedBlock : encryptedBlocks) {
			outputStream.write(encryptedBlock);
		}
		byte[] encryptedData = outputStream.toByteArray();
		return Base64Utils.encodeToString(encryptedData);
	}

	public static String decryptStringRSA(String privateKeyString, String encryptedData) throws Exception {
		byte[] privateKeyBytes = Base64.getDecoder().decode(privateKeyString);
		PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);

		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		cipher.init(Cipher.DECRYPT_MODE, privateKey);

		byte[] encryptedBytes = Base64.getDecoder().decode(encryptedData);
		List<byte[]> decryptedBlocks = new ArrayList<>();

		int blockSize = 256;
		int offset = 0;
		while (offset < encryptedBytes.length) {
			byte[] block;
			if (offset + blockSize <= encryptedBytes.length) {
				block = cipher.doFinal(encryptedBytes, offset, blockSize);
			} else {
				block = cipher.doFinal(encryptedBytes, offset, encryptedBytes.length - offset);
			}
			decryptedBlocks.add(block);
			offset += blockSize;
		}

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		for (byte[] decryptedBlock : decryptedBlocks) {
			outputStream.write(decryptedBlock);
		}
		byte[] decryptedData = outputStream.toByteArray();

		return new String(decryptedData, StandardCharsets.UTF_8);
	}
}