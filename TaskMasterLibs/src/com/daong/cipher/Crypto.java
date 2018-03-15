package com.daong.cipher;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Scanner;
import java.util.Set;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public final class Crypto {
	private final String ENCRYPT_KEY;
	private final String ENCRYPT_IV;
	private final String ALGORITHM = "AES/CBC/PKCS5PADDING";

	/**
	 * 
	 * @param encrypt_key
	 * @param encrypt_iv
	 */
	public Crypto(String encrypt_key, String encrypt_iv) {
		ENCRYPT_KEY = encrypt_key;
		ENCRYPT_IV = encrypt_iv;
	}

	/**
	 * 
	 * @param encrypt_iv
	 */
	public Crypto(String encrypt_iv) {
		ENCRYPT_KEY = "danielisawesome!";
		ENCRYPT_IV = encrypt_iv;
	}



	/**
	 *テキストを暗号化し、バイトの配列として返します。
	 * @param text
	 * @return byte[]
	 */
	public byte[] encrypt(String text) {
		byte[] encryptedBytes = null;
		try {
			byte[] input = text.getBytes("UTF-8");
			byte[] keyBytes = ENCRYPT_KEY.getBytes("UTF-8");
			byte[] ivBytes = ENCRYPT_IV.getBytes("UTF-8");
			SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
			IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
			Cipher cipher = Cipher.getInstance(ALGORITHM);
			cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);

			encryptedBytes = new byte[cipher.getOutputSize(input.length)];
			int encryptLength = cipher.update(input, 0, input.length, encryptedBytes, 0);
			encryptLength += cipher.doFinal(encryptedBytes, encryptLength);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			e.printStackTrace();
		} catch (ShortBufferException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return encryptedBytes;
	}

//	/**
//	 * バイトの配列を解読Stringとして返す。テスト用なのでprivate
//	 *
//	 * @param input
//	 * @return 
//	 */
//	private String decrypt(byte[] input) {
//		String decryptedText = null;
//		try {
//			byte[] keyBytes = ENCRYPT_KEY.getBytes("UTF-8");
//			byte[] ivBytes = ENCRYPT_IV.getBytes("UTF-8");
//			SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
//			IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
//			Cipher cipher = Cipher.getInstance(ALGORITHM);
//			cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);
//
//			byte[] decryptedBytes = new byte[cipher.getOutputSize(input.length)];
//			int decryptLength = cipher.update(input, 0, input.length, decryptedBytes, 0);
//			cipher.doFinal(decryptedBytes, decryptLength);
//			decryptedText = new String(decryptedBytes, "UTF-8");
//		} catch (NoSuchAlgorithmException e) {
//			e.printStackTrace();
//		} catch (NoSuchPaddingException e) {
//			e.printStackTrace();
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		} catch (InvalidKeyException e) {
//			e.printStackTrace();
//		} catch (InvalidAlgorithmParameterException e) {
//			e.printStackTrace();
//		} catch (IllegalBlockSizeException e) {
//			e.printStackTrace();
//		} catch (BadPaddingException e) {
//			e.printStackTrace();
//		} catch (ShortBufferException e) {
//			e.printStackTrace();
//		}
//		return decryptedText.trim();
//	}

	/**
	 * 指定バイト数のランダムStringを作成
	 *
	 * @param bytes
	 * @return String
	 */
	public static String randomString(int bytes) {
		char[] chrs = new char[bytes];

		for (int i = 0; i < chrs.length; i++) {
			int rand = (int)(Math.random() * 3);
			if (rand == 0) {
				chrs[i] = (char)(Math.random() * 26 + 65);
			}
			else if (rand == 1) {
				chrs[i] = (char)(Math.random() * 26 + 97);
			} else {
				chrs[i] = (char)(Math.random() * 10 + 48);
			}
		}
		return new String(chrs);
	}

}
