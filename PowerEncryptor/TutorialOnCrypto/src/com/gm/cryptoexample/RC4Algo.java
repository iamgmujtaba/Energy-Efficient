package com.gm.cryptoexample;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class RC4Algo extends Activity {

	private static String algorithm = "RC4";

	@Override
	public void onCreate(Bundle savedState) {
		super.onCreate(savedState);
		setContentView(R.layout.rc4_encription);

		try {

			String text = MainActivity.textToEncrypt;
			byte[] encryptedText = encrypt(text, "password");
			String decryptedText = decrypt(encryptedText, "password");
			
			
			TextView tvorig = (TextView) findViewById(R.id.tvorig);
			tvorig.setText("\n[ORIGINAL]:\n" + text + "\n");

			String encriptedMsg = null;
			for (int i = 0; i < encryptedText.length; i++) {
				encriptedMsg = encriptedMsg+encryptedText[i];
			}
			
			TextView tvencoded = (TextView) findViewById(R.id.tvencoded);
			tvencoded.setText("[ENCODED]:\n" + encriptedMsg + "\n");
			
			TextView tvdecoded = (TextView) findViewById(R.id.tvdecoded);
			tvdecoded.setText("[DECODED]:\n" + decryptedText + "\n");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static byte[] encrypt(String toEncrypt, String key) throws Exception {
		// create a binary key from the argument key (seed)
		SecureRandom sr = new SecureRandom(key.getBytes());
		KeyGenerator kg = KeyGenerator.getInstance(algorithm);
		kg.init(sr);
		SecretKey sk = kg.generateKey();

		// create an instance of cipher
		Cipher cipher = Cipher.getInstance(algorithm);

		// initialize the cipher with the key
		cipher.init(Cipher.ENCRYPT_MODE, sk);

		// enctypt!
		byte[] encrypted = cipher.doFinal(toEncrypt.getBytes());

		return encrypted;
	}

	public static String decrypt(byte[] toDecrypt, String key) throws Exception {
		// create a binary key from the argument key (seed)
		SecureRandom sr = new SecureRandom(key.getBytes());
		KeyGenerator kg = KeyGenerator.getInstance(algorithm);
		kg.init(sr);
		SecretKey sk = kg.generateKey();

		// do the decryption with that key
		Cipher cipher = Cipher.getInstance(algorithm);
		cipher.init(Cipher.DECRYPT_MODE, sk);
		byte[] decrypted = cipher.doFinal(toDecrypt);

		return new String(decrypted);
	}

}
