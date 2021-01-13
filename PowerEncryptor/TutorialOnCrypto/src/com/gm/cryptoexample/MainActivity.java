package com.gm.cryptoexample;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint({ "TrulyRandom", "SdCardPath" })
public class MainActivity extends Activity {

	private RadioGroup fileSizeGroup, algoGroup;
	private RadioButton hundredKBBtn, fiveHundredKBBtn, onethousandKBBtn,
			fifteenKBBtn, aesBtn, blowfishBtn,Blowfish_AES_Btn, AES_Blowfish_Btn;
	private Button uploadBtn,encryptBtn;

	public static String textToEncrypt = null, encryptedText = null,
			decryptedText = null;

	private static String fileSize = "100 KB", algoName = "AES Encription";
	private static int inputFileNO = 1;

	//private TextView encriptionName;  //, tvorig, tvencoded, tvdecoded;

	//Upload File 
	TextView messageText,encryptionTimer;
 	int serverResponseCode = 0;
	ProgressDialog dialog = null;

	String upLoadServerUri = "https://www.facebook.com/";

	final String uploadFilePath = "/data/data/com.gm.cryptoexample/files/";
	final String uploadFileName = "encryptedText.txt";
	
	//Mix algorithems
	String encryptedTextAES,encryptedTextBlowfish;
	String decryptedTextBlowfish,decryptedTextAES;
	
	
	//Timer
	//long start_time, end_time; 
	
	
	@SuppressLint("TrulyRandom")
	@Override
	public void onCreate(Bundle savedState) {
		super.onCreate(savedState);
		setContentView(R.layout.activity_main);
		
		int serverRandomURL =  randInt(1, 4);

		//System.out.println("random number"+serverRandomURL);
		
		if(serverRandomURL == 1){
			upLoadServerUri = "https://www.facebook.com/";
		}else if(serverRandomURL == 2){
			upLoadServerUri = "https://www.facebook.com/";
		}else if(serverRandomURL == 3){
			upLoadServerUri = "https://www.facebook.com/";
		}else if(serverRandomURL == 4){
			upLoadServerUri = "https://www.facebook.com/";
		}

		//Log.i("Random No: "+serverRandomURL," upLoadServerUri "+upLoadServerUri);


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

		algoGroup = (RadioGroup) findViewById(R.id.algorithems);
		algoGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId == R.id.AesEncription) {
					algoName = "AES Encription";

				}  else if (checkedId == R.id.BlowfishEncription) {
					algoName = "Blowfish Encription";

				}else if (checkedId == R.id.Blowfish_AES) {
					algoName = "Blowfish-AES";

				} else if (checkedId == R.id.AES_Blowfish) {
					algoName = "AES-Blowfish";
				}
			}
		});

		hundredKBBtn = (RadioButton) findViewById(R.id.hundredKB);
		fiveHundredKBBtn = (RadioButton) findViewById(R.id.fiveHundredKB);
		onethousandKBBtn = (RadioButton) findViewById(R.id.onethousandKB);
		fifteenKBBtn = (RadioButton) findViewById(R.id.fifteenKB);

		aesBtn = (RadioButton) findViewById(R.id.AesEncription);
		blowfishBtn = (RadioButton) findViewById(R.id.BlowfishEncription);
		
		//
		Blowfish_AES_Btn = (RadioButton) findViewById(R.id.Blowfish_AES);
		AES_Blowfish_Btn = (RadioButton) findViewById(R.id.AES_Blowfish);

		messageText = (TextView) findViewById(R.id.encription_name);
		encryptionTimer = (TextView) findViewById(R.id.encrypt_timer);
		//tvorig = (TextView) findViewById(R.id.tvorig);
		//tvencoded = (TextView) findViewById(R.id.tvencoded);
		//tvdecoded = (TextView) findViewById(R.id.tvdecoded);

	
		
		encryptBtn = (Button) findViewById(R.id.encryptBtn);
		encryptBtn.setOnClickListener(new OnClickListener() {

			@SuppressLint("TrulyRandom")
			@Override
			public void onClick(View v) {

				Log.e("File Encryption","starting file encryption");
				 
				//Timer Start
				//start_time = System.nanoTime();
				
				
				messageText.setText("starting file encryption");
				
				int selectedId = fileSizeGroup.getCheckedRadioButtonId();
				int selectedAlgoId = algoGroup.getCheckedRadioButtonId();

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

				AesAlgo aes = new AesAlgo();
				BlowFishAlgo blowfishAlgorithm = new BlowFishAlgo();
				
				// find which radioButton is checked by id
				if (selectedAlgoId == aesBtn.getId()) {
			//		AesAlgo aes = new AesAlgo();
					try {
						encryptedText = AesAlgo.encrypt(textToEncrypt,aes.secretKey);
						decryptedText = AesAlgo.decrypt(encryptedText,aes.secretKey);

						//tvencoded.setText("[ENCODED]:\n" + encryptedText + "\n");

					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				} else if (selectedAlgoId == Blowfish_AES_Btn.getId()) {
					
					
					try {
						encryptedText = blowfishAlgorithm.encrypt(textToEncrypt);
						
						encryptedTextAES = AesAlgo.encrypt(encryptedText,aes.secretKey);
						decryptedTextAES = AesAlgo.decrypt(encryptedTextAES,aes.secretKey);
						
						decryptedText = blowfishAlgorithm.decrypt(encryptedText);
						

						

					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				
					
					Log.d("textToEncrypt *****",textToEncrypt);
					Log.i("encryptedText Blowfish",encryptedText);
					Log.w("encryptedText AES",encryptedTextAES);
					Log.d("decryptedText AES",decryptedTextAES);
					Log.i("decryptedText Blowfish",decryptedText);
					
					//Log.e("HelloWorld","HelloWorld");
					
					//encryptedText = CaesarShift.encrypt(textToEncrypt, 5);
					//decryptedText = CaesarShift.decrypt(encryptedText, 5);

					//tvencoded.setText("[ENCODED]:\n" + encryptedText + "\n");

				} else if (selectedAlgoId == blowfishBtn.getId()) {

					//BlowFishAlgo blowfishAlgorithm = new BlowFishAlgo();
					encryptedText = blowfishAlgorithm.encrypt(textToEncrypt);
					decryptedText = blowfishAlgorithm.decrypt(encryptedText);

					//tvencoded.setText("[ENCODED]:\n" + encryptedText + "\n");

				} else if (selectedAlgoId == AES_Blowfish_Btn.getId()) {

					/*AesAlgo aes = new AesAlgo();
					BlowFishAlgo blowfishAlgorithm = new BlowFishAlgo();*/
					try {
						encryptedText = AesAlgo.encrypt(textToEncrypt,aes.secretKey);

						//AES to Blowfish, file encrypted aes to blowfish
						encryptedTextBlowfish = blowfishAlgorithm.encrypt(encryptedText);
						decryptedTextBlowfish = blowfishAlgorithm.decrypt(encryptedTextBlowfish);
						
						//Final Decryption file
						decryptedText = AesAlgo.decrypt(decryptedTextBlowfish,aes.secretKey);

					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				
					
					Log.d("textToEncrypt *****",textToEncrypt);
					Log.i("encryptedText AES",encryptedText);
					Log.w("encryptedText Blowfish",encryptedTextBlowfish);
					Log.d("decryptedText Blowfish",decryptedTextBlowfish);
					Log.i("decryptedText AES",decryptedText);
					
					
					/*	try {
						byte[] encryptedText = RC4Algo.encrypt(textToEncrypt,"password");
						decryptedText = RC4Algo.decrypt(encryptedText,"password");

						String encriptedMsg = "";
						for (int i = 0; i < encryptedText.length; i++) {
							encriptedMsg = encriptedMsg + encryptedText[i];
						}

						//tvencoded.setText("[ENCODED]:\n" + encriptedMsg + "\n");
						//tvdecoded.setText("[DECODED]:\n" + decryptedText + "\n");

					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}*/

				}

				// read & write encrypted file
				try {
					writeFile(encryptedText);
					Log.e("Write encrypt file","done writing");
					readFile();
					
					Log.e("Read encrypted file","done reading");
					//Log.e("readFile", readFile());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				//tvorig.setText("\n[ORIGINAL]:\n" + textToEncrypt + "\n");
				//tvdecoded.setText("[DECODED]:\n" + decryptedText + "\n");
				
				//Log.e("textToEncrypt",textToEncrypt);
				//Log.e("encryptedText",encryptedText);
				//Log.e("decryptedText",decryptedText);

				
				
				
				messageText.setText("Encryption done.\n"+algoName+", File Size: "+fileSize);

				Toast.makeText(getApplicationContext(),"File Size :" + fileSize + "\n Algorithem :" + algoName,
						Toast.LENGTH_SHORT).show();
								
				Log.e("File Encryption","end file encryption");
				
				//Timer end
				/*end_time = System.nanoTime();
				double difference = (end_time - start_time)/1e6;
				double seconds =  (difference / 1000) % 60 ;
				System.out.println(difference+" mili,"+seconds+" sec");
				
				encryptionTimer.setText("Time: "+difference+" mili, "+seconds+" sec");*/
			}
		});
		
		//messageText = (TextView) findViewById(R.id.encription_name);
		//messageText.setText("Uploading file path :- 'path" + uploadFileName	+ "'");

		uploadBtn = (Button) findViewById(R.id.uploadBtn);
		uploadBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.e("Upload File","Start uploading....");
				
				messageText.setText("Uploading file path :- 'path" + uploadFileName	+ "'");
				
				dialog = ProgressDialog.show(MainActivity.this, "","Uploading file...", true);
				

				new Thread(new Runnable() {
					@Override
					public void run() {
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								messageText.setText("uploading started.....");
							}
						});

						uploadFile(uploadFilePath + "" + uploadFileName);

					}
				}).start();
			}
		});
	}
	
	

	//Uploading file into server
	public int uploadFile(String sourceFileUri) {

		String fileName = sourceFileUri;

		HttpURLConnection conn = null;
		DataOutputStream dos = null;
		String lineEnd = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";
		int bytesRead, bytesAvailable, bufferSize;
		byte[] buffer;
		int maxBufferSize = 1 * 1024 * 1024;
		File sourceFile = new File(sourceFileUri);

		if (!sourceFile.isFile()) {

			dialog.dismiss();

			Log.e("uploadFile", "Source File not exist :" + uploadFilePath + " "
					+ uploadFileName);

			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					messageText.setText("Source File not exist :"
							+ uploadFilePath + " " + uploadFileName);
				}
			});

			return 0;

		} else {
			try {

				// open a URL connection to the Servlet
				FileInputStream fileInputStream = new FileInputStream(
						sourceFile);
				URL url = new URL(upLoadServerUri);

				// Open a HTTP connection to the URL
				conn = (HttpURLConnection) url.openConnection();
				conn.setDoInput(true); // Allow Inputs
				conn.setDoOutput(true); // Allow Outputs
				conn.setUseCaches(false); // Don't use a Cached Copy
				conn.setRequestMethod("POST");
				conn.setRequestProperty("Connection", "Keep-Alive");
				conn.setRequestProperty("ENCTYPE", "multipart/form-data");
				conn.setRequestProperty("Content-Type",
						"multipart/form-data;boundary=" + boundary);
				conn.setRequestProperty("uploaded_file", fileName);

				dos = new DataOutputStream(conn.getOutputStream());

				dos.writeBytes(twoHyphens + boundary + lineEnd);
				dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\" filename= \""
						+ fileName + "\"" + lineEnd);

				dos.writeBytes(lineEnd);

				// create a buffer of maximum size
				bytesAvailable = fileInputStream.available();

				bufferSize = Math.min(bytesAvailable, maxBufferSize);
				buffer = new byte[bufferSize];

				// read file and write it into form...
				bytesRead = fileInputStream.read(buffer, 0, bufferSize);

				while (bytesRead > 0) {

					dos.write(buffer, 0, bufferSize);
					bytesAvailable = fileInputStream.available();
					bufferSize = Math.min(bytesAvailable, maxBufferSize);
					bytesRead = fileInputStream.read(buffer, 0, bufferSize);

				}

				// send multipart form data necesssary after file data...
				dos.writeBytes(lineEnd);
				dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

				// Responses from the server (code and message)
				serverResponseCode = conn.getResponseCode();
				String serverResponseMessage = conn.getResponseMessage();

				Log.i("uploadFile", "HTTP Response is : "
						+ serverResponseMessage + ": " + serverResponseCode);

				if (serverResponseCode == 200) {

					runOnUiThread(new Runnable() {
						@Override
						public void run() {

						/*	String msg = "File Upload Completed.\n\n See uploaded file here : \n\n"
									+ upLoadServerUri + uploadFileName;*/

							String msg = "File Upload Completed.\n\n"+ upLoadServerUri;
							
							messageText.setText(msg);
							Toast.makeText(MainActivity.this,"File Upload Complete.", Toast.LENGTH_LONG)
									.show();
							
							Log.e("Upload File","end uploading....");
						}
					});
				}

				// close the streams //
				fileInputStream.close();
				dos.flush();
				dos.close();

			} catch (MalformedURLException ex) {

				dialog.dismiss();
				ex.printStackTrace();

				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						messageText
								.setText("MalformedURLException Exception : check script url.");
						Toast.makeText(MainActivity.this,	"MalformedURLException", Toast.LENGTH_SHORT)
								.show();
					}
				});

				Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
			} catch (Exception e) {

				dialog.dismiss();
				e.printStackTrace();

				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						messageText.setText("Got Exception : see logcat ");
						Toast.makeText(MainActivity.this,
								"Got Exception : see logcat ",
								Toast.LENGTH_SHORT).show();
					}
				});
				Log.e("Upload file to server Exception","Exception : " + e.getMessage(), e);
				Log.e("No internet access","unable to upload file" );

			}
			dialog.dismiss();
			return serverResponseCode;

		}
	}
	
	
	public void writeFile(String text) throws IOException{
		File root = android.os.Environment.getExternalStorageDirectory();
		File dir = new File(root.getAbsolutePath() + "/download");
		dir.mkdirs();
		File  log = new File(dir, uploadFileName);
 		 
 		    try{
		    if(!log.exists()){
		            System.out.println("We had to make a new file.");
		            log.createNewFile();
		    }
		    		    
		    FileWriter fileWriter = new FileWriter(log, true);

		    BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
		    bufferedWriter.write(text);
		    //bufferedWriter.write("\n*****************************"+"\n");
		    bufferedWriter.close();

		    }catch(IOException e){
		        System.out.println("COULD NOT LOG!!");
		    }
	}
	
	public String readFile() throws IOException{
		File root = android.os.Environment.getExternalStorageDirectory();
		File dir = new File(root.getAbsolutePath() + "/download");
		
		System.out.println("rading... file path: "+new File(dir, uploadFileName));
		BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(dir, uploadFileName)));
				
		String read;
		StringBuilder builder = new StringBuilder("");

		while((read = bufferedReader.readLine()) != null){
			builder.append(read);
			
			//System.out.println(read);
		}
		bufferedReader.close();
		
		return builder.toString();
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

	
	public static int randInt(int min, int max) {
		Random rand = new Random();
		int randomNum = rand.nextInt((max - min) + 1) + min;
	    return randomNum;
	}
	
}
