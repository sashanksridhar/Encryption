import java.io.File;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import java.io.IOException;

import java.security.InvalidKeyException;

import java.security.Key;

import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.IllegalBlockSizeException;

import javax.crypto.NoSuchPaddingException;

import javax.crypto.spec.SecretKeySpec;

import java.util.Scanner;

public class videoencryption
{
	static void fileProcessor(int cipherMode,String key,File inputFile,File outputFile){

		try {

			Key secretKey = new SecretKeySpec(key.getBytes(), "AES");

			Cipher cipher = Cipher.getInstance("AES");

			cipher.init(cipherMode, secretKey);



			FileInputStream inputStream = new FileInputStream(inputFile);

			//byte[] inputBytes = new byte[(int) inputFile.length()];

			//inputStream.read(inputBytes);



			//byte[] outputBytes = cipher.doFinal(inputBytes);



			FileOutputStream outputStream = new FileOutputStream(outputFile);
if(cipherMode == Cipher.ENCRYPT_MODE)
{
CipherInputStream cis = new CipherInputStream(inputStream, cipher);
int read;
    while((read=cis.read())!=-1)
    {
      outputStream.write(read);
      outputStream.flush();
    }
}
else
{

CipherOutputStream cos = new CipherOutputStream(outputStream,cipher);
int read;
    while((read=inputStream.read())!=-1)
    {
      outputStream.write(read);
      outputStream.flush();
    }
}
    

		

//	outputStream.write(outputBytes);



			inputStream.close();

			outputStream.close();



		} catch (NoSuchPaddingException | NoSuchAlgorithmException 

				| InvalidKeyException 

				| IOException e) {

			e.printStackTrace();

		}

	}
public static void main(String args[])
{
String key = "This is a secret";

		Scanner reader = new Scanner(System.in);  
		System.out.println("Enter a file name: ");
		String n = reader.nextLine(); 
		reader.close();
String path1 = "/home/sashank/encryption/";
File inputFile = new File(n);
File encryptedFile = new File(path1+"encrypted.mp4");
 try
{if(encryptedFile.exists())
    encryptedFile.createNewFile();
}
catch(IOException e)
{
e.printStackTrace();
}
videoencryption.fileProcessor(Cipher.ENCRYPT_MODE,key,inputFile,encryptedFile);
File outputFile = new File(path1 + "decrypted.mp4");
videoencryption.fileProcessor(Cipher.DECRYPT_MODE,key,inputFile,outputFile);

}




}
