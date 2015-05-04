package application;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;

public class SystemTrayListener {
	
	private boolean isSupported;
	private String dummyString = "                                                                                ";
	
	private PopupMenu popUp;
	private TrayIcon icon;
	
	public SystemTrayListener() {
		isSupported = SystemTray.isSupported();
		
		if (isSupported) {
			System.out.println("System tray is supported!");
			SystemTray tray = SystemTray.getSystemTray();
			
			
		    Image image = Toolkit.getDefaultToolkit().getImage( SystemTrayListener.class.getResource( "project5logo.png" ) );
			
			popUp = new PopupMenu();
			
			icon = new TrayIcon(image, "News Reader", popUp);
			try {
				tray.add(icon);
			} catch (AWTException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void displayRefresh( String feed_title ) {
		if (isSupported) {
			icon.displayMessage("New data received!", feed_title + " has been refreshed!" + dummyString, TrayIcon.MessageType.INFO);
		}
	}
}
