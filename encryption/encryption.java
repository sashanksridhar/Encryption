
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

import javax.crypto.IllegalBlockSizeException;

import javax.crypto.NoSuchPaddingException;

import javax.crypto.spec.SecretKeySpec;

import java.util.Scanner;

public class encryption {


	static void splitFile(File f,String p) throws IOException {

		int partCounter = 1;



		int sizeOfFiles = 128;

		byte[] buffer = new byte[sizeOfFiles];



		String fileName = f.getName();



		

		try (FileInputStream fis = new FileInputStream(f);

				BufferedInputStream bis = new BufferedInputStream(fis)) {



			int bytesAmount = 0;

			while ((bytesAmount = bis.read(buffer)) > 0) {

				

				String filePartName = p+"/"+Integer.toString(partCounter)+".txt";
				partCounter++;
				File newFile = new File(filePartName);

				try (FileOutputStream out = new FileOutputStream(newFile)) {

					out.write(buffer, 0, bytesAmount);

				}

			}

		}

	}
	static void fileProcessor(int cipherMode,String key,File inputFile,File outputFile){

		try {

			Key secretKey = new SecretKeySpec(key.getBytes(), "AES");

			Cipher cipher = Cipher.getInstance("AES");

			cipher.init(cipherMode, secretKey);



			FileInputStream inputStream = new FileInputStream(inputFile);

			byte[] inputBytes = new byte[(int) inputFile.length()];

			inputStream.read(inputBytes);



			byte[] outputBytes = cipher.doFinal(inputBytes);



			FileOutputStream outputStream = new FileOutputStream(outputFile);

			outputStream.write(outputBytes);



			inputStream.close();

			outputStream.close();



		} catch (NoSuchPaddingException | NoSuchAlgorithmException 

				| InvalidKeyException | BadPaddingException

				| IllegalBlockSizeException | IOException e) {

			e.printStackTrace();

		}

	}
	static void encrypt(String key,String src,String des) throws IOException
	{
		int counter = 1;
		while(true)
		{
			String p = src+"/"+Integer.toString(counter)+".txt";
			File f1 = new File(p);
			if(!f1.exists())
			{
				break;
			}
			File encryptedFile = new File(des+"/"+Integer.toString(counter)+".encrypted");
			encryption.fileProcessor(Cipher.ENCRYPT_MODE,key,f1,encryptedFile);
			counter++;
		}
	}
	static void decrypt(String key,String src,String des) throws IOException
	{
		int counter = 1;
		while(true)
		{
			String p = src+"/"+Integer.toString(counter)+".encrypted";
			File f1 = new File(p);
			if(!f1.exists())
			{
				break;
			}
			File decryptedFile = new File(des+"/"+Integer.toString(counter)+".txt");
			encryption.fileProcessor(Cipher.DECRYPT_MODE,key,f1,decryptedFile);
			counter++;
		}
	}



	static void merge(String src,String des)
	{
		
		int counter = 1;
		while(true)
		{
			String p = src+"/"+Integer.toString(counter)+".txt";
			File f1 = new File(p);
			if(!f1.exists())
			{
				break;
			}
			counter++;
			File file = new File(des+"/merge.txt");
			BufferedWriter bw = null;
			FileWriter fw = null;

			try {

				FileInputStream fis = new FileInputStream(f1);
				BufferedReader in = new BufferedReader(new InputStreamReader(fis));

				FileWriter fstream = new FileWriter(file, true);
				BufferedWriter out = new BufferedWriter(fstream);
				PrintWriter pw = new PrintWriter(out);
				String aLine =null;
				while ((aLine = in.readLine()) != null) {
					
					pw.print(aLine);
					
					
				}

				pw.close();
				System.out.println("Done");

			} catch (IOException e) {

				e.printStackTrace();

			} finally {

				try {

					if (bw != null)
						bw.close();

					if (fw != null)
						fw.close();

				} catch (IOException ex) {

					ex.printStackTrace();

				}
			}

		}

	}



	public static void main(String[] args) {

		String key = "This is a secret";

		Scanner reader = new Scanner(System.in);  
		System.out.println("Enter a file name: ");
		String n = reader.nextLine(); 
		reader.close();
		String[] parsed = n.split("\\.");
		String name = parsed[0];
		File newDir = new File(name);
		newDir.mkdir();
		String path1 = "/home/sashank/encryption/"+name;
		String path2 = "/home/sashank/encryption/"+name+"/split";
		String path3 = "/home/sashank/encryption/"+name+"/encrypted";
		String path4 = "/home/sashank/encryption/"+name+"/decrypted";
		File d2 = new File(path2);
		d2.mkdir();
		File d3 = new File(path3);
		d3.mkdir();
		File d4 = new File(path4);
		d4.mkdir();
		File inputFile = new File(n);


		File encryptedFile = new File(path1+"/"+"test.encrypted");

		File decryptedFile = new File(path1+"/"+"decrypted-text.txt");

		try{

			splitFile(new File(n),path2);	

			encrypt(key,path2,path3);
			decrypt(key,path3,path4);
			merge(path4,path1);
			

			System.out.println("Sucess");

		} catch (Exception ex) {

			System.out.println(ex.getMessage());

			ex.printStackTrace();

		}

	}



}
