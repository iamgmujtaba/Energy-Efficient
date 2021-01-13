package com.gm.cryptoexample;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class CaesarShift extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shifting_encription);

		String text = MainActivity.textToEncrypt;
		String encryptedText = encrypt(text, 10);
		String decryptedText = decrypt(encryptedText, 10);
		
		// 5 value

		System.out.println("Text before Encryption: " + text);
		System.out.println("Cipher Text: " + encryptedText);
		System.out.println("Text after Decryption: " + decryptedText);

		TextView tvorig = (TextView) findViewById(R.id.tvorig);
		tvorig.setText("\n[ORIGINAL]:\n" + text + "\n");

		TextView tvencoded = (TextView) findViewById(R.id.tvencoded);
		tvencoded.setText("[ENCODED]:\n" + encryptedText + "\n");

		TextView tvdecoded = (TextView) findViewById(R.id.tvdecoded);
		tvdecoded.setText("[DECODED]:\n" + decryptedText + "\n");

	}

	public static String encrypt(String original, int shift) {
		String encrypted = "";
		for (int i = 0; i < original.length(); i++) {
			if ((original.charAt(i) >= 'A' && original.charAt(i) <= 'Z')
					|| (original.charAt(i) >= 'a' && original.charAt(i) <= 'z')) {
				if (((char) (original.charAt(i) + shift) > 'Z' && original
						.charAt(i) <= 'Z')
						|| ((char) (original.charAt(i) + shift) > 'z' && original
								.charAt(i) <= 'z'))

				{
					encrypted = encrypted
							+ (char) (original.charAt(i) + shift - 26);
				} else {

					encrypted = encrypted + (char) (original.charAt(i) + shift);
				}
			} else {

				encrypted = encrypted + original.charAt(i);
			}
		}

		return encrypted;
	}

	public static String decrypt(String original, int shift) {
		String decrypted = "";
		for (int i = 0; i < original.length(); i++) {
			if ((original.charAt(i) >= 'A' && original.charAt(i) <= 'Z')
					|| (original.charAt(i) >= 'a' && original.charAt(i) <= 'z')) {
				if (((char) (original.charAt(i) - shift) < 'A' && original
						.charAt(i) >= 'A')
						|| ((char) (original.charAt(i) - shift) < 'a' && original
								.charAt(i) >= 'a'))

				{
					decrypted = decrypted
							+ (char) (original.charAt(i) - shift + 26);
				} else {
					decrypted = decrypted + (char) (original.charAt(i) - shift);
				}
			} else {
				decrypted = decrypted + original.charAt(i);
			}
		}
		return decrypted;
	}

	// this method is a generalization of the above 2 methods to merge them into
	// one.
	public static String shiftLetters(String original, int shift) {
		String shifted = "";
		for (int i = 0; i < original.length(); i++) {
			if (isLetter(original.charAt(i))) {
				shifted = shifted + shift(original.charAt(i), shift);
			} else {
				shifted = shifted + original.charAt(i);
			}
		}

		return shifted;
	}

	// helper method to check if something is a letter
	public static boolean isLetter(char letter) {
		return (letter >= 'a' && letter <= 'z')
				|| (letter >= 'A' && letter <= 'Z');
	}

	public static char shift(char original, int shift) {
		int normalizedShift = shift % 26;
		char shifted = (char) (original + shift);
		if ((shift > 'Z' && original <= 'Z')
				|| (shift > 'z' && original <= 'z')) {
			shifted = (char) (shifted - 26);
		}

		return shifted;
	}

}