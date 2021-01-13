package com.gm.xts.aes.android;

import java.io.IOException;
import java.io.InputStream;

import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.engines.AESFastEngine;
import org.bouncycastle.crypto.params.KeyParameter;

import android.app.Activity;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

public class MainActivity extends Activity {

	Button encryptBtn;

	TextView encryptTextView, encryptedText, decriptedText;
	static String textToEncrypt = "";

	private RadioGroup fileSizeGroup;
	private static String fileSize = "100 KB";
	private RadioButton hundredKBBtn, fiveHundredKBBtn, onethousandKBBtn,fifteenKBBtn;
	private static int inputFileNO = 1;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		
		fileSizeGroup = (RadioGroup) findViewById(R.id.fileSizeGroup);
		fileSizeGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId == R.id.hundredKB) {
					fileSize = "100 KB";

				} else if (checkedId == R.id.fiveHundredKB) {
					fileSize = "500 KB";

				} else if (checkedId == R.id.onethousandKB) {
					fileSize = "1000KB";

				} else if (checkedId == R.id.fifteenKB) {
 					fileSize = "1500 KB";
				}
			}

		});
		
		hundredKBBtn = (RadioButton) findViewById(R.id.hundredKB);
		fiveHundredKBBtn = (RadioButton) findViewById(R.id.fiveHundredKB);
		onethousandKBBtn = (RadioButton) findViewById(R.id.onethousandKB);
		fifteenKBBtn = (RadioButton) findViewById(R.id.fifteenKB);
		
		
		encryptTextView = (TextView) findViewById(R.id.encryptBtn);
		encryptTextView.setText("Encrypt File");
		
		//Read File From Assests
		//textToEncrypt = readFileAssest("input_3");

		//textToEncrypt.setText(text);
		
		encryptBtn = (Button) findViewById(R.id.encryptBtn);
		encryptBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				
				
				int selectedId = fileSizeGroup.getCheckedRadioButtonId();
				if (selectedId == hundredKBBtn.getId()) {
					inputFileNO = 1;

				} else if (selectedId == fiveHundredKBBtn.getId()) {
					inputFileNO = 2;

				} else if (selectedId == onethousandKBBtn.getId()) {
					inputFileNO = 3;

				} else if (selectedId == fifteenKBBtn.getId()) {
					inputFileNO = 4;

				}

				// read text file from assest folder
				textToEncrypt = readFileAssest("input_" + inputFileNO);
				
				
				startEncryprtion();
				Toast.makeText(getApplicationContext(),"File Size :" + fileSize,
						Toast.LENGTH_SHORT).show();
			}
		});

		// Log.e("Hello",text);

	}
	
	public String readFileAssest(String fileName){
		AssetManager assetManager = getAssets();
		InputStream input;
		String text = "";
		try {
			input = assetManager.open(fileName + ".txt");
			int size = input.available();
			byte[] buffer = new byte[size];
			input.read(buffer);
			input.close();
			text = new String(buffer);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return text;
	}
	
	
	public static void startEncryprtion(){
        // IEEE 1619 test vector 10
        final String key = "2718281828459045235360287471352662497757247093699959574966967627";
        final String tweakKey = "3141592653589793238462643383279502884197169399375105820974944592";
        final String dataUnit = "00000000000000ff";
        String plainText = textToEncrypt;
        
        final String cipherText = "1c3b3a102f770386e4836c99e370cf9bea00803f5e482357a4ae12d414a3e63b"
                                + "5d31e276f8fe4a8d66b317f9ac683f44680a86ac35adfc3345befecb4bb188fd"
                                + "5776926c49a3095eb108fd1098baec70aaa66999a72a82f27d848b21d4a741b0"
                                + "c5cd4d5fff9dac89aeba122961d03a757123e9870f8acf1000020887891429ca"
                                + "2a3e7a7d7df7b10355165c8b9a6d0a7de8b062c4500dc4cd120c0f7418dae3d0"
                                + "b5781c34803fa75421c790dfe1de1834f280d7667b327f6c8cd7557e12ac3a0f"
                                + "93ec05c52e0493ef31a12d3d9260f79a289d6a379bc70c50841473d1a8cc81ec"
                                + "583e9645e07b8d9670655ba5bbcfecc6dc3966380ad8fecb17b6ba02469a020a"
                                + "84e18e8f84252070c13e9f1f289be54fbc481457778f616015e1327a02b140f1"
                                + "505eb309326d68378f8374595c849d84f4c333ec4423885143cb47bd71c5edae"
                                + "9be69a2ffeceb1bec9de244fbe15992b11b77c040f12bd8f6a975a44a0f90c29"
                                + "a9abc3d4d893927284c58754cce294529f8614dcd2aba991925fedc4ae74ffac"
                                + "6e333b93eb4aff0479da9a410e4450e0dd7ae4c6e2910900575da401fc07059f"
                                + "645e8b7e9bfdef33943054ff84011493c27b3429eaedb4ed5376441a77ed4385"
                                + "1ad77f16f541dfd269d50d6a5f14fb0aab1cbb4c1550be97f7ab4066193c4caa"
                                + "773dad38014bd2092fa755c824bb5e54c4f36ffda9fcea70b9c6e693e148c151";

        /*
         * Run test with IEEE 1619 test vector 10
         */

        // Setup ciphers
        CipherParameters params = new KeyParameter(ByteUtil.hexToBytes(key));
        CipherParameters tweakParams = new KeyParameter(ByteUtil.hexToBytes(tweakKey));
        BlockCipher cipher = new AESFastEngine();
        BlockCipher tweakCipher = new AESFastEngine();
        cipher.init(true, params);
        tweakCipher.init(true, tweakParams);

        // Setup XTS mode test
        XTS xts = new XTS(cipher, tweakCipher);
        byte[] plaintext = ByteUtil.hexToBytes(plainText);
        byte[] ciphertext = ByteUtil.hexToBytes(cipherText);
        long dataUnitNumber = ByteUtil.loadInt64BE(ByteUtil.hexToBytes(dataUnit), 0);
        byte[] createdCipherText = new byte[xts.getDataUnitSize()];
        byte[] decryptedPlainText = new byte[xts.getDataUnitSize()];

        System.out.println("====================================================");
        System.out.println("IEEE 1619 test vector 10");
        System.out.println("Key               " + key);
        System.out.println("Tweak key:        " + tweakKey);
        System.out.println("Data unit number: " + dataUnitNumber);
        System.out.println("Plaintext:        " + plainText);
        System.out.println("Ciphertext:       " + cipherText);
        System.out.println("createdCipherText:       " + createdCipherText);
        System.out.println("====================================================");
        System.out.println("Result");
        System.out.println("====================================================");

        // Encrypt
        xts.processDataUnit(plaintext, 0, createdCipherText, 0, dataUnitNumber);

        System.out.println("Ciphertext:       " + ByteUtil.bytesToHex(createdCipherText));
       
        if (ByteUtil.bytesToHex(createdCipherText).equals(cipherText))
            System.out.println("Ciphertext matches IEEE 1619 test vector 10");
        else
            System.out.println("Ciphertext does not match IEEE 1619 test vector 10");

        // Decrypt
        cipher.init(false, params);
        xts.resetCipher(cipher);
        xts.processDataUnit(ciphertext, 0, decryptedPlainText, 0, dataUnitNumber);

        System.out.println("Plaintext:        " + ByteUtil.bytesToHex(decryptedPlainText));
        if (ByteUtil.bytesToHex(decryptedPlainText).equals(plainText))
            System.out.println("Plaintext matches IEEE 1619 test vector 10");
        else
            System.out.println("Plaintext does not match IEEE 1619 test vector 10");
    
	
	}

}