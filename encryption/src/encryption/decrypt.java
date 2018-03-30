/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package encryption;


import java.awt.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.SequenceInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;
import java.util.Enumeration;
import java.util.Vector;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.DefaultListModel;
import javax.swing.JComboBox;

/**
 *
 * @author sashank
 */
public class decrypt extends javax.swing.JFrame {
     /**
     * Creates new form decrypt
     */
    public decrypt() throws NullPointerException {

      //  add(list1);
        initComponents();
        
    Path currentRelativePath = Paths.get("");
String s = currentRelativePath.toAbsolutePath().toString();
//System.out.println(s);
        File folder = new File(s);
               for (final File fileEntry : folder.listFiles()) {
 
            
            if (fileEntry.isDirectory()) {
           if(!fileEntry.getName().equals("build")&&!fileEntry.getName().equals("nbproject")&&!fileEntry.getName().equals("src")&&!fileEntry.getName().equals("test"))
           {
     list.add(fileEntry.getName());
           }
        } 
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

        jLabel1 = new javax.swing.JLabel();
        decrypt = new javax.swing.JButton();
        list = new java.awt.List();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("Choose File");

        decrypt.setText("DECRYPT");
        decrypt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                decryptActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(39, 39, 39)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(list, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(79, 79, 79)
                .addComponent(decrypt)
                .addContainerGap(87, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jLabel1)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(66, 66, 66)
                        .addComponent(decrypt)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 15, Short.MAX_VALUE)
                        .addComponent(list, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(78, Short.MAX_VALUE))))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
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
    static void decrypt(String key,String src,String des) throws IOException
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
			File decryptedFile = new File(des+"/"+Integer.toString(counter)+".txt");
			fileProcessor(Cipher.DECRYPT_MODE,key,f1,decryptedFile);
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
//	int i = verify(pi,des+"/signature",des+"/publickey");
//File filemer = new File(des+"/merge.txt");
//chksum(filemer,des);	
	/*if(i==1)				
		System.out.println("Done");
	else
	{
File file = new File(des+"/merge.txt");		
file.delete();
		File directory = new File(des+"/decrypted");
		delete(directory);
		System.out.println("Not Vefified");
	}*/
}
catch(Exception e)
{
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
				System.out.println("Data sign verified.");
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
public static void chksum(File src,String d)
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
    BufferedReader br = new BufferedReader(new FileReader(d+"/shm.txt"));
    String line = br.readLine();
    if(line.equals(sb.toString()))
    {
        System.out.println("Hash verified");
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

    private void decryptActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_decryptActionPerformed
        // TODO add your handling code here:
        String name = list.getSelectedItem();
      
       String key = "This is a secret";
        try{

			 long start = System.currentTimeMillis( );			
			decrypt(key,name+"/encrypted",name+"/decrypted");
			merge(name+"/decrypted",name);
 long stop = System.currentTimeMillis( );
System.out.println("time to decrypt and merge "+(stop-start)+"ms");
File filemer = new File(name+"/merge.txt");
start = System.currentTimeMillis( );
chksum(filemer,name);
 stop = System.currentTimeMillis( );
 start = System.currentTimeMillis( );
 System.out.println("time to check hash "+(stop-start)+"ms");
int i = verify(name+"/merge.txt",name+"/signature",name+"/publickey");
if(i==1)				
		System.out.println("Done");
	else
	{
File file = new File(name+"/merge.txt");		
file.delete();
		File directory = new File(name+"/decrypted");
		delete(directory);
		System.out.println("Not Vefified");
	}
 stop = System.currentTimeMillis( );
 System.out.println("time to check sign "+(stop-start)+"ms");
 System.out.println("Sucess");

		} catch (Exception ex) {

			System.out.println(ex.getMessage());

			ex.printStackTrace();

		}
    }//GEN-LAST:event_decryptActionPerformed

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
            java.util.logging.Logger.getLogger(decrypt.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(decrypt.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(decrypt.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(decrypt.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new decrypt().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton decrypt;
    private javax.swing.JLabel jLabel1;
    private java.awt.List list;
    // End of variables declaration//GEN-END:variables
}
