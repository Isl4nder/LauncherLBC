/*
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package me.kioo.core.updater;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.swing.SwingWorker;

import me.kioo.ui.ProgressDialog;
import me.kioo.ui.WindowFrame;
import me.kioo.util.ConfigInside;
import me.kioo.util.Util;

/**
 *
 * @author skyghis
 */
public class Updater extends SwingWorker<Integer, String> {

    private static final int READ_BUFFER_SIZE = 32000;
    private static final MessageDigest SHA1 = Updater.initializeMessageDigestSha1("SHA1");
	private Properties filesList;
	private String updateUrl;
	public ProgressDialog progressDialog;
	private WindowFrame windowFrame;
	
	
	public Updater(WindowFrame windowFrame, ProgressDialog progressDialog) throws IOException {
		this.windowFrame = windowFrame;
    	this.progressDialog =progressDialog;
    	
    	/* On ajoute un écouteur de barre de progression. */
        addPropertyChangeListener(new PropertyChangeListener() {
        	@Override
            public void propertyChange(PropertyChangeEvent evt) {
                if("progress".equals(evt.getPropertyName())) {
                	Updater.this.progressDialog.setValue((Integer) evt.getNewValue());
                }
            }
        });
    	
    	this.updateUrl = ConfigInside.getProperty("update_url");
         String updatePropertiesFile = ConfigInside.getProperty("update_properties_file");
         
         URL updateFile = new URL(this.updateUrl + '/' + updatePropertiesFile);
         InputStream input = updateFile.openStream();
         this.filesList = new Properties();
         
         try {
        	 this.filesList.load(input);
         } finally {
        	 input.close();
         }
    }

	public void update(float progressStart, float progressEnd) {
        try {
            final File workingDirectory = Util.getWorkingDirectory();
            
            final int size = this.filesList.size();
            float step = (progressEnd - progressStart) / size;

            for (Map.Entry<Object, Object> entry : this.filesList.entrySet()) {
            	progressStart += step;
                setProgress((int) progressStart);
                this.progressDialog.refresh();
            	
                String strFile = String.valueOf(entry.getKey());
                String strHash = String.valueOf(entry.getValue());
                
                File file = new File(workingDirectory, strFile);
                if ((!file.exists())
                || !hashFile(file).equals(strHash)) {
                    if (!file.exists()) {
                        System.out.println("create " + file.toString());
                    } else {
                        System.out.println("update " + file.toString());
                    }
                    URL url = new URL(this.updateUrl + URLEncoder.encode(strFile, "UTF-8").replace("%2F", "/").replace("+", "%20"));
                    Updater.copyToFile(url, file);
                } else {
                    System.out.println("skip   " + file.toString());
                }
            }

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
    }
    
    public void remove(float progressStart, float progressEnd) {
    	File workDirectory = Util.getWorkingDirectory();
    	String strDirectory = ConfigInside.getProperty("update_directory_checkup");
    	StringTokenizer tokenizer = new StringTokenizer(strDirectory, ",");
    	
    	double step = (progressEnd - progressStart) / tokenizer.countTokens()+1;
    	
    	while (tokenizer.hasMoreTokens()) {
        	progressStart += step;
            setProgress((int) progressStart);
    		
    		final File folder = new File(workDirectory, tokenizer.nextToken());
    		if (folder.exists()) {
    			this.listFileToRemove(folder, true);
    		}
    	}
    	
    	// pour le répertoire bin
    	String strDirectoryBin = ConfigInside.getProperty("update_directory_checkup_no_recursive");
    	final File folder = new File(workDirectory, strDirectoryBin);
		if (folder.exists()) {
			setProgress((int) progressEnd);
			this.listFileToRemove(folder, false);	
		}
    }
    
    private void listFileToRemove(final File folder, Boolean recursive) {

		for (final File fileEntry : folder.listFiles()) {
			System.out.println("Lecture du fichier/dossier " + fileEntry.getAbsolutePath());
			if ((fileEntry.isDirectory())
			&& (recursive)) {
				this.listFileToRemove(fileEntry, true);
			} else {
				if ((fileEntry.isFile())
				&& (fileEntry.canWrite())
				&& (!this.checkExistence(fileEntry))) {
					fileEntry.delete();
					System.out.println("delete " + fileEntry.toString());
				}
			}
		}
    }
    
    private boolean checkExistence(File file) {
    	
    	final File workingDirectory = Util.getWorkingDirectory();
    	String pathWorkingDirectory = workingDirectory.getAbsolutePath();
    	String pathFile = file.getAbsolutePath();
    	
    	if (pathFile.startsWith(pathWorkingDirectory)) {
            pathFile = pathFile.substring(pathWorkingDirectory.length());
        }
    	pathFile = pathFile.replace(File.separator, "/");
    	
    	if (this.filesList.containsKey(pathFile)) {
    		return true;
    	}
    	System.out.println("n'existe pas" + pathFile);
    	return false;
    }

    /**
     * 
     * @param source
     * @param dest
     * @throws IOException
     */
    private static void copyToFile(URL source, File dest) throws IOException {
        InputStream input = null;
        OutputStream output = null;
        byte[] dataBytes = new byte[Updater.READ_BUFFER_SIZE];
        try {
            input = source.openStream();
            dest.getParentFile().mkdirs();
            output = new FileOutputStream(dest);
            int nread = input.read(dataBytes);
            while (nread != -1) {
                output.write(dataBytes, 0, nread);
                nread = input.read(dataBytes);
            }
        } finally {
            close(input);
            close(output);
        }
    }

    /**
     * Close file
     * @param closeable
     */
    private static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException ex) {
                System.out.println("Failed to close '" + closeable + "'");
            }
        }
    }

    /**
     * Retourne le hash du fichier
     * @param sourceFile
     * @return
     * @throws IOException
     */
    private static String hashFile(File sourceFile) throws IOException {
        FileInputStream fis = new FileInputStream(sourceFile);
        try {
            byte[] dataBytes = new byte[READ_BUFFER_SIZE];
            int nread = fis.read(dataBytes);
            while (nread != -1) {
                SHA1.update(dataBytes, 0, nread);
                nread = fis.read(dataBytes);
            }
        } finally {
            fis.close();
        }
        byte[] digest = SHA1.digest();
        // Convert the byte to hex format
        StringBuilder digestString = new StringBuilder();
        for (int i = 0; i < digest.length; i++) {
            digestString.append(Integer.toString((digest[i] & 0xff) + 0x100, 16).substring(1));
        }
        return digestString.toString();
    }

    private static MessageDigest initializeMessageDigestSha1(String algorithm) {
        try {
            return MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException(ex);
        }
    }

	@Override
	public Integer doInBackground() throws Exception {
		update(0, 100);
		remove(0, 100);
		return 100;
	}
	
    @Override
    protected void done() {
        try {
            setProgress(100);
            this.progressDialog.setVisible(false);
            this.windowFrame.READY_TO_LAUNCH = true;
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
