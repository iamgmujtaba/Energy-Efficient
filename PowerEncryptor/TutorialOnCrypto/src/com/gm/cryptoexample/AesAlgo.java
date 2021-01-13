package com.gm.cryptoexample;

import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.TextView;

@SuppressLint("TrulyRandom")
public class AesAlgo extends Activity {
	static Cipher cipher;
	KeyGenerator keyGenerator = null;
	SecretKey secretKey = null;
	
	public static String encoded = null,decoded = null;
	
	@Override
	@SuppressLint("TrulyRandom")
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aes_encription);
		
		AesAlgo aes = new AesAlgo();
			String text = MainActivity.textToEncrypt;
			String encryptedText;
			String decryptedText;
			try {
				encryptedText = AesAlgo.encrypt(text, secretKey);
				decryptedText = AesAlgo.decrypt(encryptedText, secretKey);
				
				
				TextView tvorig = (TextView) findViewById(R.id.tvorig);
				tvorig.setText("\n[ORIGINAL]:\n" + text + "\n");
				TextView tvencoded = (TextView) findViewById(R.id.tvencoded);
				tvencoded.setText("[ENCODED]:\n" + encryptedText + "\n");

				TextView tvdecoded = (TextView) findViewById(R.id.tvdecoded);
				tvdecoded.setText("[DECODED]:\n" + decryptedText + "\n");

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
	}
	
	@SuppressLint("TrulyRandom")
	public AesAlgo() {
		try {
			keyGenerator = KeyGenerator.getInstance("AES");
			keyGenerator.init(128); /* 128-bit AES */
			secretKey = keyGenerator.generateKey();
			cipher = Cipher.getInstance("AES");
			
			//System.out.println("secretKey:  "+secretKey.getAlgorithm());
			
		} catch (NoSuchPaddingException ex) {
			System.out.println(ex);
		} catch (NoSuchAlgorithmException ex) {
			System.out.println(ex);
		}
	}
	
	
	public static String encrypt(String plainText, SecretKey secretKey)	throws Exception {
		byte[] plainTextByte = plainText.getBytes();
		cipher.init(Cipher.ENCRYPT_MODE, secretKey);
		byte[] encryptedByte = cipher.doFinal(plainTextByte);
		String encryptedText = Base64.encodeToString(encryptedByte, 0);
		
	//System.out.println("secretKey"+secretKey.getEncoded().toString());
	//Log.w("Key Blow Fish", secretKey.toString());
		

		return encryptedText;
	}

	public static String decrypt(String encryptedText, SecretKey secretKey)
			throws Exception {
		byte[] encryptedTextByte = Base64.decode(encryptedText, 0);

		cipher.init(Cipher.DECRYPT_MODE, secretKey);
		byte[] decryptedByte = cipher.doFinal(encryptedTextByte);
		String decryptedText = new String(decryptedByte);
		return decryptedText;
	}

	
}