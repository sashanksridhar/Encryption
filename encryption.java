
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
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;
import java.io.IOException;
import java.io.*;
import java.util.*;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
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
try
{
		int counter = 1;
Vector v = new Vector();
String pi = des+"/merge.txt";
   FileOutputStream fout=new FileOutputStream(pi);    
		while(true)
		{
			String p = src+"/"+Integer.toString(counter)+".txt";
			File f1 = new File(p);
			if(!f1.exists())
			{
				break;
			}
			counter++;
FileInputStream fin=new FileInputStream(p);
v.add(fin);
}
Enumeration enumeration = v.elements();
 
        //passing the enumeration object in the constructor
        SequenceInputStream sin = new SequenceInputStream(enumeration);
int data = sin.read();
while(data != -1){
     fout.write(data);         
    data = sin.read();
}
sin.close();         
	int i = verify(pi,des+"/signature",des+"/publickey");
File filemer = new File(des+"/merge.txt");
chksum(filemer);	
	if(i==1)				
		System.out.println("Done");
	else
	{
File file = new File(des+"/merge.txt");		
file.delete();
		File directory = new File(des+"/decrypted");
		delete(directory);
		System.out.println("Not Vefified");
	}
}
catch(Exception e)
{
e.printStackTrace();
}
}
	public static void delete(File file)
		throws IOException{

			if(file.isDirectory()){

				//directory is empty, then delete it
				if(file.list().length==0){

					file.delete();
					System.out.println("Directory is deleted : " 
							+ file.getAbsolutePath());

				}else{

					//list all the directory contents
					String files[] = file.list();

					for (String temp : files) {
						//construct the file structure
						File fileDelete = new File(file, temp);

						//recursive delete
						delete(fileDelete);
					}

					//check the directory again, if empty then delete it
					if(file.list().length==0){
						file.delete();
						System.out.println("Directory is deleted : " 
								+ file.getAbsolutePath());
					}
				}

			}else{
				//if file, then delete it
				file.delete();
				System.out.println("File is deleted : " + file.getAbsolutePath());
			}
		}
	public static void sign(File i,String sign,String key)
	{
		try {
			// Get instance and initialize a KeyPairGenerator object.
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DSA", "SUN");
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
			keyGen.initialize(1024, random);

			// Get a PrivateKey from the generated key pair.
			KeyPair keyPair = keyGen.generateKeyPair();
			PrivateKey privateKey = keyPair.getPrivate();

			// Get an instance of Signature object and initialize it.
			Signature signature = Signature.getInstance("SHA1withDSA", "SUN");
			signature.initSign(privateKey);

			// Supply the data to be signed to the Signature object
			// using the update() method and generate the digital
			// signature.
			byte[] bytes = Files.readAllBytes(Paths.get(i.getAbsolutePath()));
			signature.update(bytes);
			byte[] digitalSignature = signature.sign();

			// Save digital signature and the public key to a file.
			Files.write(Paths.get(sign), digitalSignature);
			Files.write(Paths.get(key), keyPair.getPublic().getEncoded());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static int verify(String src,String sign,String key)
	{
		try {
			byte[] publicKeyEncoded = Files.readAllBytes(Paths.get(key));
			byte[] digitalSignature = Files.readAllBytes(Paths.get(sign));

			X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyEncoded);
			KeyFactory keyFactory = KeyFactory.getInstance("DSA", "SUN");

			PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);
			Signature signature = Signature.getInstance("SHA1withDSA", "SUN");
			signature.initVerify(publicKey);

			byte[] bytes = Files.readAllBytes(Paths.get(src));
			signature.update(bytes);

			boolean verified = signature.verify(digitalSignature);
			if (verified) {
				System.out.println("Data verified.");
				return 1;
			} else {
				System.out.println("Cannot verify data.");
				return 0;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 1;
	}

public static void chksum(File src)
{
try
{
String s = src.getAbsolutePath();
  MessageDigest md = MessageDigest.getInstance("SHA1");
    FileInputStream fis = new FileInputStream(s);
long l = src.length();
int le = (int)l; 
byte[] dataBytes = new byte[le];
    
    int nread = 0; 
    
    while ((nread = fis.read(dataBytes)) != -1) {
      md.update(dataBytes, 0, nread);
    };

    byte[] mdbytes = md.digest();
   
    //convert the byte to hex format
    StringBuffer sb = new StringBuffer("");
    for (int i = 0; i < mdbytes.length; i++) {
    	sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
    }
    
    System.out.println("Digest(in hex format):: " + sb.toString());
}
catch(Exception e)
{
e.printStackTrace();
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
        chksum(inputFile);

		File encryptedFile = new File(path1+"/"+"test.encrypted");

		File decryptedFile = new File(path1+"/"+"decrypted-text.txt");
		sign(inputFile,path1+"/signature",path1+"/publickey");
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
