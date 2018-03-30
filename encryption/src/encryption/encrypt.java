/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package encryption;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.Signature;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.JFileChooser;

/**
 *
 * @author sashank
 */
public class encrypt extends javax.swing.JFrame {

    /**
     * Creates new form encrypt
     */
    public encrypt() {
        initComponents();
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
    
    public static void chksum(File src,String des)
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
       FileOutputStream fout=new FileOutputStream(des);
       
       fout.write(String.valueOf(sb).getBytes());
       fout.close();
}
catch(Exception e)
{
e.printStackTrace();
}
}
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
			File encryptedFile = new File(des+"/"+Integer.toString(counter)+".txt");
			fileProcessor(Cipher.ENCRYPT_MODE,key,f1,encryptedFile);
			counter++;
		}
	}  
  private void OpenActionPerformed(java.awt.event.ActionEvent evt) {
    
    if (evt.getActionCommand().equals(JFileChooser.APPROVE_SELECTION)) {
        File file = filechooser.getSelectedFile();
        // What to do with the file, e.g. display it in a TextArea
        String n = file.getName();
        String[] parsed = n.split("\\.");
		String name = parsed[0];
		File newDir = new File(name);
		newDir.mkdir();
                File d2 = new File(name+"/split");
		d2.mkdir();
		File d3 = new File(name+"/encrypted");
		d3.mkdir();
		File d4 = new File(name+"/decrypted");
		d4.mkdir();
		//File inputFile = new File(n);
         long start = System.currentTimeMillis( );
                chksum(file,name+"/shm.txt");
        sign(file,name+"/signature",name+"/publickey");
        long stop = System.currentTimeMillis( );
        System.out.println("time for hash and sign generation "+(stop-start)+" ms");
        String key = "This is a secret";
        try{
 start = System.currentTimeMillis( );
			splitFile(file,name+"/split");	

			encrypt(key,name+"/split",name+"/encrypted");
			
 stop = System.currentTimeMillis( );

			System.out.println("Sucess Encrypted in time "+(stop-start)+" ms");

		} catch (Exception ex) {

			System.out.println(ex.getMessage());

			ex.printStackTrace();

		}

    } else {
        System.out.println("File access cancelled by user.");
    }
} 
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        filechooser = new javax.swing.JFileChooser();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        filechooser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                filechooserActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(filechooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 24, Short.MAX_VALUE)
                .addComponent(filechooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void filechooserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_filechooserActionPerformed
        String result = evt.getActionCommand();
    System.out.println(result);
        if (result.equals(JFileChooser.APPROVE_SELECTION)) {
    
            OpenActionPerformed(evt);
}
        // TODO add your handling code here:
    }//GEN-LAST:event_filechooserActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(encrypt.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(encrypt.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(encrypt.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(encrypt.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new encrypt().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JFileChooser filechooser;
    // End of variables declaration//GEN-END:variables
}
