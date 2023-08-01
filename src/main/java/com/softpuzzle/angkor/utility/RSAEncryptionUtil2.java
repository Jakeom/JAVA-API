package com.softpuzzle.angkor.utility;

import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import javax.crypto.Cipher;



@Slf4j
public class RSAEncryptionUtil2 {

	// RSA 키 쌍 생성
	public static KeyPair generateRSAKeyPair() throws Exception {
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
		keyPairGenerator.initialize(2048); // 키 크기 설정

		return keyPairGenerator.generateKeyPair();
	}

	// 공개키로 암호화
	public static byte[] encryptRSA(byte[] publicKeyBytes, byte[] plainText) throws Exception {
		X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);

		return cipher.doFinal(plainText);
	}

	// 개인키로 복호화
	public static byte[] decryptRSA(byte[] privateKeyBytes, byte[] cipherText) throws Exception {
		PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);

		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		cipher.init(Cipher.DECRYPT_MODE, privateKey);

		return cipher.doFinal(cipherText);
	}

	public static void main(String[] args) {
		try {
			// 1-1. 각 개인별 공용키
			String publicKeyString = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEArp5aB1HxwjZY/4kIVF63S+gprc/w28FYMM8fL3f7VtVVCcyiAJGIqch+ApXkHUaYeRiPJbtdwXIT6QfA2w915k09xzwnyNhiTa9Q0dtKkgMzL/lDh/kXFj5AuN/1BPNrhitKef2K8Mu8lLujhbhjsxkPSVNeWY+p2kJe3S9a1+3xhaRqogT0bbF9b3IZBap7iE+AjXDDWBF6xiKkXEJ6srVumeX2xspJznEKDnnVqK7ljBV+5m3vPtKLM1KB2pYdXsbuhidY1poR48PkzBwvaFncYD5wHGhBLbLpsgy1EeXVPEyuzLODAeziG4XPQ+D28GLahDY7Rbsx+dVPHPJZswIDAQAB";
			byte[] publicKey = Base64.getDecoder().decode(publicKeyString);

			// 1-2. 각 유저의 개인키
			String uPrivateKeyString = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCunloHUfHCNlj/iQhUXrdL6Cmtz/DbwVgwzx8vd/tW1VUJzKIAkYipyH4CleQdRph5GI8lu13BchPpB8DbD3XmTT3HPCfI2GJNr1DR20qSAzMv+UOH+RcWPkC43/UE82uGK0p5/Yrwy7yUu6OFuGOzGQ9JU15Zj6naQl7dL1rX7fGFpGqiBPRtsX1vchkFqnuIT4CNcMNYEXrGIqRcQnqytW6Z5fbGyknOcQoOedWoruWMFX7mbe8+0oszUoHalh1exu6GJ1jWmhHjw+TMHC9oWdxgPnAcaEEtsumyDLUR5dU8TK7Ms4MB7OIbhc9D4PbwYtqENjtFuzH51U8c8lmzAgMBAAECggEALiR+Gc0Xm4CNCAmH//N/cDmLOfjm5ssyqMMytHJ6IjoBXLOMY6YRfPwUAE7nlqzfAbDxssDOR54DkOKRndF6ED99AbbttG8pmumm662NCQR7dEPkCnxSE3ij1baoooRId43dAltzaaoMzSjAT0eoYN1QYsn69qO1+IztUHmBAenYy0Vc0yhOgxAYk80MVPEb1TfJcWAcUAlHsOInTY8LdvQOspFPF2L8BCJE1tB8yHIR+q2LCMggxICLkx6ofMd3bmkBu0KPjnAvx+c2fIt43kfB52WNbUByzjFHgQy1pPrOUz9q1SRu8752AMGvdNEj+LFQlW9AMgExTVJWYJK00QKBgQDaFhj58SbSs5JteneZyu/FI392ewH1dfK2bgnJQYoREzM3P89k/GMbEC4it/9m2XC8isI2KqWFF90PZRpZB8mNW1J7OZeYb/xTjtMixTYMBqRHfA4PbVY0aTj088OjML6rkQKZnqmgiCQuCjFjKt1gct1pqtcVEWlP18FVNKcXpwKBgQDM+bo1wsv0Uzo8zth+54NiuJq5fcJMn+bGjx5//ZCDyazDzdzz/ncY4OCWQXAuDJs5KnyAXoxnt/Q2EUxMfJbevvJ7dHgtLxFtNIXgRm8XFiIFmqt4+V5EQDP7J2n+m6PRRSW/I3cf4PkiS9AtNNNTpa44myuQgAs78VxiZ6RvFQKBgEMqexa/Ro2t8HL3PM6Iyb+VZzQ+PiQy50V4Lltla/hGSYfCh0U61esH2K2orWgnn6jxs06rY77R0qtjH/I7i7+VWJnK6v1vv+6cFjqnMjYR9ZZuhoYJznrlzTzjtmUUJiUAzCQz0Vo3k6z6RqucEAIcXkSk8Qj0C9MjR8/OH4gRAoGBAL5ULJn69VM7bNpF3zWHV7J1ZAZVRfPMjodVIacLFgOpYqi2ITLW2FPbi+85eCbqQcAFOXIhWRJ991+1FDn5fkX63EHSVjzudaoHmUA1ZrXzMUgu5IYCV4vcaegOIxfuAWEyaehf0B9j0TINX5J6eLg1Id8iBoO4wbW5WmVtg3w5AoGAD6On70/IhISLpoEmVgM+pIiFTsWrPC4EcS2X+qudDXqp3IfsIBesCN5LCMLjjpWxYoYy0vPqJeR9sFBjmYYn67m1sFQH0BlugEzE99dQE0sGsxE9os8VvHS9OZkfdU/29wXO2ofeGgVVYQeu40Cl9jrbQ1eRErtT8+mF1ATfg+o=";
			byte[] uPrivateKey = Base64.getDecoder().decode(uPrivateKeyString);

			// 2. 그룹 개인키 1개 생성

			KeyPair keyPair = generateRSAKeyPair();
			byte[] plainTextBytes = keyPair.getPrivate().getEncoded();

			String privateKeyString = Base64.getEncoder().encodeToString(uPrivateKey);
			String plainText = privateKeyString;
			log.info(">>>>" + privateKeyString);
			int chunkSize = 245;
			List<String> list = new ArrayList<>();

			for (int i = 0; i < plainTextBytes.length; i += chunkSize) {
				int length = Math.min(chunkSize, plainTextBytes.length - i);
				byte[] chunk = new byte[length];
				System.arraycopy(plainTextBytes, i, chunk, 0, length);
				byte[] encryptedData = encryptRSA(publicKey, chunk);
				String str = Base64.getEncoder().encodeToString(encryptedData);
				String targetString = "==";
				if (str.endsWith(targetString)) {
					str = str.substring(0, str.length() - targetString.length());
				}
				list.add(str);
			}

			int totalLength = list.stream().mapToInt(String::length).sum();
			byte[] byteArray = new byte[totalLength];
			int currentIndex = 0;

			for (String str : list) {
				byte[] strBytes = str.getBytes(StandardCharsets.UTF_8);
				System.arraycopy(strBytes, 0, byteArray, currentIndex, strBytes.length);
				currentIndex += strBytes.length;
			}

			String result = new String(byteArray, StandardCharsets.UTF_8);
			System.out.println("result1 >>> " + result);

			/*byte[] encryptedResultBytes = byteArray;

			int chunkSize2 = 245; // RSA의 키 크기에 따라 조정
			List<byte[]> encryptedChunks = new ArrayList<>();

			for (int i = 0; i < encryptedResultBytes.length; i += chunkSize2) {
				int length = Math.min(chunkSize2, encryptedResultBytes.length - i);
				byte[] chunk = new byte[length];
				System.arraycopy(encryptedResultBytes, i, chunk, 0, length);
				encryptedChunks.add(chunk);
			}

			byte[] decryptedBytes = new byte[0];

			for (byte[] chunk : encryptedChunks) {
				byte[] decryptedChunk = decryptRSA(privateKey, chunk);
				decryptedBytes = concatenateByteArrays(decryptedBytes, decryptedChunk);
			}

			String decryptedResult = new String(decryptedBytes, StandardCharsets.UTF_8);
			System.out.println("Decrypted Result: " + decryptedResult);*/


			/*// 개인키로 복호화
			// 암호화된 키 (encryptedKey)를 복호화
			byte[] decryptedKey = decryptRSA(uPrivateKey, result.getBytes());

			// 복호화된 키를 바이트 배열로 변환한 후, 다시 SecretKey 객체로 복원
			SecretKey secretKey = new SecretKeySpec(decryptedKey, "AES");

			// 복호화된 키를 사용하여 데이터 복호화
			Cipher symmetricCipher = Cipher.getInstance("AES");
			symmetricCipher.init(Cipher.DECRYPT_MODE, secretKey);
			byte[] decryptedData = symmetricCipher.doFinal(result.getBytes());

			// 복호화된 데이터 출력
			String decryptedText = new String(decryptedData);
			System.out.println("Decrypted text: " + decryptedText);*/
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}