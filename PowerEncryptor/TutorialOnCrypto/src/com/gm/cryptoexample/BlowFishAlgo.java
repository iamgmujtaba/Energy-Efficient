package com.gm.cryptoexample;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.Base64;
import android.widget.TextView;


@SuppressLint("TrulyRandom")
public class BlowFishAlgo extends Activity {

	KeyGenerator keyGenerator = null;
	SecretKey secretKey = null;
	Cipher cipher = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.blowfish_encription);
	
		BlowFishAlgo blowfishAlgorithm = new BlowFishAlgo();

		String text = MainActivity.textToEncrypt;
		String encryptedText = blowfishAlgorithm.encrypt(text);
		String decryptedText = blowfishAlgorithm.decrypt(encryptedText);
		
		TextView tvorig = (TextView) findViewById(R.id.tvorig);
		tvorig.setText("\n[ORIGINAL]:\n" + text + "\n");

		TextView tvencoded = (TextView) findViewById(R.id.tvencoded);
		tvencoded.setText("[ENCODED]:\n" + encryptedText + "\n");

		TextView tvdecoded = (TextView) findViewById(R.id.tvdecoded);
		tvdecoded.setText("[DECODED]:\n" + decryptedText + "\n");
		
	}
	

	@SuppressLint("TrulyRandom")
	public BlowFishAlgo() {
		try {
			/**
			 * Create a Blowfish key
			 */
			keyGenerator = KeyGenerator.getInstance("Blowfish");
			keyGenerator.init(128); /* 128-bit Blowfish */
			secretKey = keyGenerator.generateKey();

			//Log.w("secretKey:  ",secretKey.getAlgorithm());
			/**
			 * Create an instance of cipher mentioning the name of algorithm -
			 * Blowfish
			 */
			cipher = Cipher.getInstance("Blowfish");
		} catch (NoSuchPaddingException ex) {
			System.out.println(ex);
		} catch (NoSuchAlgorithmException ex) {
			System.out.println(ex);
		}

	}

	/**
	 * 
	 * @param plainText
	 * @return cipherBytes
	 */
	public byte[] encryptText(String plainText) {
		byte[] cipherBytes = null;
		try {
			/**
			 * Initialize the cipher for encryption
			 */
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			/**
			 * Convert the text string to byte format
			 */
			byte[] plainBytes = plainText.getBytes();
			/**
			 * Perform encryption with method doFinal()
			 */
			cipherBytes = cipher.doFinal(plainBytes);
		} catch (IllegalBlockSizeException ex) {
			System.out.println(ex);
		} catch (BadPaddingException ex) {
			System.out.println(ex);
		} catch (InvalidKeyException ex) {
			System.out.println(ex);
		}

		return cipherBytes;
	}

	/**
	 * 
	 * @param cipherBytes
	 * @return plainText
	 */
	public String decryptText(byte[] cipherBytes) {
		String plainText = null;
		try {
			/**
			 * Initialize the cipher for decryption
			 */
			cipher.init(Cipher.DECRYPT_MODE, secretKey);
			/**
			 * Perform decryption with method doFinal()
			 */
			byte[] plainBytes = cipher.doFinal(cipherBytes);
			/**
			 * Convert encrypted text to string format
			 */
			plainText = new String(plainBytes);
		} catch (IllegalBlockSizeException ex) {
			System.out.println(ex);
		} catch (BadPaddingException ex) {
			System.out.println(ex);
		} catch (InvalidKeyException ex) {
			System.out.println(ex);
		}

		return plainText;
	}

	/**
	 * 
	 * @param plainText
	 * @return cipherText
	 */
	public String encrypt(String plainText) {
		String cipherText = null;
		byte[] cipherBytes = encryptText(plainText);
		cipherText = bytesToString(cipherBytes);
		return cipherText;
	}

	/**
	 * 
	 * @param cipherText
	 * @return plainText
	 */
	public String decrypt(String cipherText) {
		String plainText = null;
		byte[] cipherBytes = stringToBytes(cipherText);
		plainText = decryptText(cipherBytes);
		return plainText;
	}

	/**
	 * 
	 * @param rawText
	 * @return plainText
	 * 
	 *         Perform Base64 encoding
	 */
	private String bytesToString(byte[] rawText) {
		String plainText = null;
		plainText = Base64.encodeToString(rawText, 0);
		return plainText;
	}

	/**
	 * 
	 * @param plainText
	 * @return rawText
	 * 
	 *         Perform Base64 decoding
	 */
	private byte[] stringToBytes(String plainText) {
		byte[] rawText = null;
		rawText = Base64.decode(plainText, 0);

		return rawText;
	}
}
