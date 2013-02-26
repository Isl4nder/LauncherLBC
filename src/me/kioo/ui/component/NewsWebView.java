package me.kioo.ui.component;

import static javafx.concurrent.Worker.State.FAILED;

import java.awt.BorderLayout;
import java.net.MalformedURLException;
import java.net.URL;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.sun.javafx.application.PlatformImpl;

public class NewsWebView extends JFXPanel {
	private static final long serialVersionUID = 1L;
	private WebView view; 
	private WebEngine engine;
    private JPanel panel = new JPanel(new BorderLayout());
    
    public static NewsWebView getInstance() {
    	return NewsWebViewHolder.instance;
    }
    
    public static void resetInstance() {
    	if (NewsWebViewHolder.instance != null) {
    		
    		Platform.runLater(new Runnable() {
  	    	   @Override public void run(){
  	    		   System.err.println( "exit/runLater/run");
  	    		   NewsWebViewHolder.instance.engine.getLoadWorker().cancel();
  	    		   Platform.exit();
  	    	   }
  	    	});
    		Platform.exit();
    		NewsWebViewHolder.instance = null;
    	}
    }
    
    private static class NewsWebViewHolder {
    	private static NewsWebView instance = new NewsWebView();
    }
    
    private NewsWebView() {
    	Platform.runLater(new Runnable() {
            @Override
            public void run() {
                view = new WebView();
                engine = view.getEngine();
                engine.getLoadWorker().exceptionProperty().addListener(new ChangeListener<Throwable>() {
                    public void changed(ObservableValue<? extends Throwable> o, Throwable old, final Throwable value) {
                        if (engine.getLoadWorker().getState() == FAILED) {
                            SwingUtilities.invokeLater(new Runnable() {
                                @Override
                                public void run() {
                                    JOptionPane.showMessageDialog(
                                        panel,
                                        (value != null) ?
                                        engine.getLocation() + "\n" + value.getMessage() :
                                        engine.getLocation() + "\nUnexpected error.",
                                        "Loading error...",
                                        JOptionPane.ERROR_MESSAGE);
                                }
                            });
                        }
                    }
                });

                //setScene(new Scene(view));
            }
        });
    	
    	createScene();
    	loadURL("http://www.team-lbc.fr/minecraft/LBC/");
    }
    
    public void createScene() {
    	 PlatformImpl.startup(new Runnable() {  
             public void run() { 
            	 setScene(new Scene(view));
             }
    	 });
    }
					
    public void loadURL(final String url) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                String tmp = toURL(url);
                if (tmp == null) {
                    tmp = toURL("http://" + url);
                }
                engine.load(tmp);
            }
        });
    }

    private static String toURL(String str) {
        try {
            return new URL(str).toExternalForm();
        } catch (MalformedURLException exception) {
        	return null;
        }
    }

}