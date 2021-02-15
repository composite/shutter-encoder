/*******************************************************************************************
* Copyright (C) 2021 PACIFICO PAUL
*
* This program is free software; you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation; either version 2 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License along
* with this program; if not, write to the Free Software Foundation, Inc.,
* 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
* 
********************************************************************************************/

package application;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Area;
import java.awt.geom.RoundRectangle2D;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JPopupMenu;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import library.FFMPEG;
import library.YOUTUBEDL;

import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JRadioButton;
import javax.swing.JComboBox;

/** ATTENTION pour windows les commandes sont sous forme de tableau ["a","b"], sous mac en forme de String... **/

public class VideoWeb {
	public static JDialog frame;
	ImageIcon header = new ImageIcon(getClass().getClassLoader().getResource("contents/header.png"));
	private static int complete;
		
	/*
	 * Composants
	 */
	private JLabel quit;
	private JLabel help;
	private JPanel topPanel;
	private JLabel topImage;	
	private JPanel grpURL;
	private JLabel lblURL;
	private JTextField textURL;
	private JTextField textVideoPass;
	private JTextField textUser;
	private JPasswordField textPass;
	private JLabel lblQualit;
	private JButton btnOK;
	private JRadioButton caseMP3;
	private JRadioButton caseWAV;
	private JRadioButton caseAuto;
	private JRadioButton caseUser;
	private JRadioButton casePass;
	private JRadioButton caseVideoPass;
	private JComboBox<String> comboFormats;

	public VideoWeb() {	
		frame = new JDialog();
		frame.getContentPane().setBackground(new Color(50,50,50));
		frame.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		frame.setTitle(Shutter.language.getProperty("frameVideoWeb"));
		frame.setForeground(Color.WHITE);
		frame.getContentPane().setLayout(null); 
		frame.setSize(420, 247);
		frame.setResizable(false);
		frame.setModal(true);
		frame.setAlwaysOnTop(true);
		
		
		if (frame.isUndecorated() == false) //Evite un bug lors de la seconde ouverture
		{
			frame.setUndecorated(true);
			Area shape1 = new Area(new RoundRectangle2D.Double(0, 0, frame.getWidth(), frame.getHeight(), 15, 15));
	        Area shape2 = new Area(new Rectangle(0, frame.getHeight()-15, frame.getWidth(), 15));
	        shape1.add(shape2);
			frame.setShape(shape1);
			frame.getRootPane().setBorder(BorderFactory.createMatteBorder(0, 1, 1, 1, new Color(100,100,100)));
			frame.setIconImage(new ImageIcon((getClass().getClassLoader().getResource("contents/icon.png"))).getImage());
			Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
			frame.setLocation(dim.width/2-frame.getSize().width/2, dim.height/2-frame.getSize().height/2);
			
		}
						
		topPanel();
		grpURL();
					
		Utils.changeDialogVisibility(frame, false);	
		
	}
	
	private static class MousePosition {
		static int mouseX;
		static int mouseY;
	}
			
	private void topPanel() {	
		topPanel = new JPanel();		
		topPanel.setLayout(null);
		topPanel.setBounds(0, 0, 420, 52);
			
		quit = new JLabel(new ImageIcon(getClass().getClassLoader().getResource("contents/quit2.png")));
		quit.setHorizontalAlignment(SwingConstants.CENTER);
		quit.setBounds(frame.getSize().width - 24,0,21, 21);
		topPanel.add(quit);
		
		quit.addMouseListener(new MouseListener(){

			private boolean accept = false;

			@Override
			public void mouseClicked(MouseEvent e) {			
			}

			@Override
			public void mousePressed(MouseEvent e) {		
				quit.setIcon(new ImageIcon((getClass().getClassLoader().getResource("contents/quit3.png"))));
				accept = true;
			}

			@Override
			public void mouseReleased(MouseEvent e) {	
				if (accept)		
				{
					Shutter.lblEncodageEnCours.setForeground(Color.RED);
					Shutter.lblEncodageEnCours.setText(Shutter.language.getProperty("processCancelled"));
					Shutter.progressBar1.setValue(0);		            
					Utils.changeDialogVisibility(frame, true);
				}
			}

			@Override
			public void mouseEntered(MouseEvent e) {			
				quit.setIcon(new ImageIcon((getClass().getClassLoader().getResource("contents/quit.png"))));
			}

			@Override
			public void mouseExited(MouseEvent e) {		
				quit.setIcon(new ImageIcon((getClass().getClassLoader().getResource("contents/quit2.png"))));
				accept = false;
			}

						
		});
	
		help = new JLabel(new ImageIcon(getClass().getClassLoader().getResource("contents/help2.png")));
		help.setHorizontalAlignment(SwingConstants.CENTER);
		help.setBounds(quit.getLocation().x - 21,0,21, 21);
		topPanel.add(help);
		
		help.addMouseListener(new MouseListener(){

			private boolean accept = false;

			@Override
			public void mouseClicked(MouseEvent e) {			
			}

			@Override
			public void mousePressed(MouseEvent e) {		
				help.setIcon(new ImageIcon((getClass().getClassLoader().getResource("contents/help3.png"))));
				accept = true;
			}

			@Override
			public void mouseReleased(MouseEvent e) {			
				if (accept)
				{
					try {
					    Desktop.getDesktop().browse(new URL( "https://rg3.github.io/youtube-dl/supportedsites.html").toURI());
					} catch (Exception error) {}	
				}
			}

			@Override
			public void mouseEntered(MouseEvent e) {			
				help.setIcon(new ImageIcon((getClass().getClassLoader().getResource("contents/help.png"))));
			}

			@Override
			public void mouseExited(MouseEvent e) {		
				help.setIcon(new ImageIcon((getClass().getClassLoader().getResource("contents/help2.png"))));
				accept = false;
			}
		
	});
		
		JLabel title = new JLabel(Shutter.language.getProperty("panelWebVideo"));
		title.setHorizontalAlignment(JLabel.CENTER);
		title.setBounds(0, 0, frame.getWidth(), 52);
		title.setFont(new Font("Magneto", Font.PLAIN, 26));
		topPanel.add(title);
		
		topImage = new JLabel();
		ImageIcon header = new ImageIcon(getClass().getClassLoader().getResource("contents/header.png"));
		ImageIcon imageIcon = new ImageIcon(header.getImage().getScaledInstance(topPanel.getSize().width, topPanel.getSize().height, Image.SCALE_DEFAULT));
		topImage.setIcon(imageIcon);		
		topImage.setBounds(title.getBounds());
		
		topPanel.add(topImage);
		
		frame.getContentPane().add(topPanel);
		
		topImage.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent down) {
			}

			@Override
			public void mousePressed(MouseEvent down) {
				MousePosition.mouseX = down.getPoint().x;
				MousePosition.mouseY = down.getPoint().y;					
			}

			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {					
			}

			@Override
			public void mouseExited(MouseEvent e) {				
			}		

		 });
		
		topImage.addMouseMotionListener(new MouseMotionListener(){

			@Override
			public void mouseDragged(MouseEvent e) {
					frame.setLocation(MouseInfo.getPointerInfo().getLocation().x - MousePosition.mouseX, MouseInfo.getPointerInfo().getLocation().y - MousePosition.mouseY);	
			}

			@Override
			public void mouseMoved(MouseEvent e) {
			}
			
		});
		
	}

	private void grpURL() {
		grpURL = new JPanel();
		grpURL.setLayout(null);
		grpURL.setLocation(6, 56);
		grpURL.setSize(408, 185);
		grpURL.setBackground(new Color(50,50,50));
		grpURL.setBorder(BorderFactory.createTitledBorder(new LineBorder(new Color (80,80,80), 1), Shutter.language.getProperty("videoUrl") + " ", 0, 0, new Font("Montserrat", Font.PLAIN, 12), Color.WHITE));
		
		lblURL = new JLabel(Shutter.language.getProperty("lblURL"));
		lblURL.setHorizontalAlignment(SwingConstants.RIGHT);
		lblURL.setFont(new Font("FreeSans", Font.PLAIN, 12));
		lblURL.setBounds(26, 25, 35, 16);			
		grpURL.add(lblURL);
		
		textURL = new JTextField();
		textURL.setForeground(Color.LIGHT_GRAY);
		textURL.setFont(new Font("SansSerif", Font.ITALIC, 12));
		textURL.setText(Shutter.language.getProperty("textURL"));
		textURL.setBounds(66, 22, 270, 21);
		textURL.setColumns(10);	
		
        final JPopupMenu menu = new JPopupMenu();
        JMenuItem coller = new JMenuItem(Shutter.language.getProperty("MenuItemPaste"));
        coller.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				PasteFromClipBoard(true);
			}
        	
        });
        menu.add(coller);
        
        textURL.setComponentPopupMenu(menu);		
      
		textURL.addKeyListener(new KeyListener(){

			@Override
			public void keyTyped(KeyEvent e) {
				if (textURL.getText().equals("") || textURL.getText().equals(Shutter.language.getProperty("textURL")))
				{
					btnOK.setEnabled(false);
					if (caseAuto.isSelected() == false)
						caseAuto.doClick();
					caseAuto.setEnabled(false);
				}
				else
				{
					if (Settings.comboTheme.getSelectedItem().equals(Shutter.language.getProperty("darkTheme")))
						textURL.setForeground(Color.WHITE);
					else
						textURL.setForeground(Color.BLACK);
					textURL.setFont(new Font("SansSerif", Font.PLAIN, 12));
		       		btnOK.setEnabled(true);
					if (caseAuto.isSelected() == false)
						caseAuto.doClick();
					caseAuto.setEnabled(true);
				}
			}

			@Override
			public void keyPressed(KeyEvent e) {				
					//CTRL + V coller
			        if ((e.getKeyCode() == KeyEvent.VK_V) && ((e.getModifiersEx() & KeyEvent.META_DOWN_MASK) != 0) || (e.getKeyCode() == KeyEvent.VK_V) && ((e.getModifiersEx() & KeyEvent.CTRL_DOWN_MASK) != 0)) 
			        	PasteFromClipBoard(false);
			           
			        //CTRL + A
			        if ((e.getKeyCode() == KeyEvent.VK_A) && ((e.getModifiersEx() & KeyEvent.META_DOWN_MASK) != 0) || (e.getKeyCode() == KeyEvent.VK_A) && ((e.getModifiersEx() & KeyEvent.CTRL_DOWN_MASK) != 0)) 
			        	textURL.selectAll();
					
				}

			@Override
			public void keyReleased(KeyEvent e) {
			}
			
		});
        
		grpURL.add(textURL);
								
		caseAuto = new JRadioButton("Auto");
		caseAuto.setSelected(true);
		caseAuto.setFont(new Font("FreeSans", Font.PLAIN, 12));
		caseAuto.setBounds(66, 52, caseAuto.getPreferredSize().width, 16);	
		caseAuto.setEnabled(false);
		grpURL.add(caseAuto);
				
		caseAuto.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				comboFormats.removeAllItems();
				
				if (caseAuto.isSelected())
				{		
					comboFormats.addItem("bestvideo+bestaudio");
					comboFormats.addItem("bestvideo");
					comboFormats.addItem("bestaudio");
				}
				else
				{
					frame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
					
					//Récupération des formats disponibles
					
					YOUTUBEDL.getAvailableFormats(textURL.getText(), options());
					
					do {
						try {
							Thread.sleep(100);
						} catch (Exception er){}
					}while (YOUTUBEDL.runProcess.isAlive() && Shutter.cancelled == false);
					
					if (YOUTUBEDL.error == false)
					{
						String allFormats = YOUTUBEDL.formatsOutput.substring(YOUTUBEDL.formatsOutput.lastIndexOf(":") + 2).replace("null", "").replace("DASH audio", "").replace("DASH video", "");
						
						for (String format : allFormats.split(System.lineSeparator()))
						{
							if (format.contains("format") == false)
								comboFormats.addItem(format);
						}
					}
					else
					{
						if (caseAuto.isSelected() == false)
							caseAuto.doClick();
						JOptionPane.showMessageDialog(frame, Shutter.language.getProperty("invalidUrl"), Shutter.language.getProperty("downloadError"), JOptionPane.ERROR_MESSAGE);
					}
										
					frame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				}
			}
			
		});
			
		comboFormats = new JComboBox<String>();
		comboFormats.addItem("bestvideo+bestaudio");
		comboFormats.addItem("bestvideo");
		comboFormats.addItem("bestaudio");
		comboFormats.setLocation(16, 153);	
		comboFormats.setSize(380, 22);
		comboFormats.setMaximumRowCount(10);
		grpURL.add(comboFormats);
		
		comboFormats.addPopupMenuListener(new PopupMenuListener(){

			@Override
			public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
				if (comboFormats.getPreferredSize().getWidth() > 380)
					comboFormats.setSize(comboFormats.getPreferredSize());				
			}

			@Override
			public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
				comboFormats.setSize(380,22);	
			}

			@Override
			public void popupMenuCanceled(PopupMenuEvent e) {
			}
			
		});
		
		lblQualit = new JLabel(Shutter.language.getProperty("lblQualit"));
		lblQualit.setHorizontalAlignment(SwingConstants.RIGHT);
		lblQualit.setFont(new Font("FreeSans", Font.PLAIN, 12));
		lblQualit.setBounds(16, 51, lblQualit.getPreferredSize().width, 16);		
		grpURL.add(lblQualit);
		
		btnOK = new JButton("OK");
		btnOK.setFont(new Font("Montserrat", Font.PLAIN, 12));
		btnOK.setBounds(343, 22, 53, 21);		
		btnOK.setEnabled(false);
		grpURL.add(btnOK);
		
		btnOK.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				startDownload();
			}
		});
		
		caseMP3 = new JRadioButton(Shutter.language.getProperty("caseMP3"));
		caseMP3.setFont(new Font("FreeSans", Font.PLAIN, 12));
		caseMP3.setBounds(121, 52, 127, 16);		
		grpURL.add(caseMP3);
		
		caseMP3.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				if (caseMP3.isSelected())
				{
					caseWAV.setSelected(false);
					
					if (caseAuto.isSelected())
						comboFormats.setSelectedIndex(2);
				}
				else
				{
					if (caseAuto.isSelected())
						comboFormats.setSelectedIndex(0);
				}
			}
			
		});
		
		caseWAV = new JRadioButton(Shutter.language.getProperty("caseWAV"));
		caseWAV.setFont(new Font("FreeSans", Font.PLAIN, 12));
		caseWAV.setBounds(248, 52, 127, 16);		
		grpURL.add(caseWAV);
		
		caseWAV.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				if (caseWAV.isSelected())
				{
					caseMP3.setSelected(false);
					
					if (caseAuto.isSelected())
						comboFormats.setSelectedIndex(2);
				}
				else
				{
					if (caseAuto.isSelected())
						comboFormats.setSelectedIndex(0);
				}
			}
			
		});
		
		frame.getContentPane().add(grpURL);			
		
		caseVideoPass = new JRadioButton(Shutter.language.getProperty("caseVideoPass"));
		caseVideoPass.setFont(new Font("FreeSans", Font.PLAIN, 12));
		caseVideoPass.setBounds(66, 123, caseVideoPass.getPreferredSize().width, 16);			
		grpURL.add(caseVideoPass);
			
		caseVideoPass.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (caseVideoPass.isSelected())
					textVideoPass.setEnabled(true);
				else
				{
					textVideoPass.setText("");
					textVideoPass.setEnabled(false);
				}
			}
		
		});
		
		textVideoPass = new JTextField();
		textVideoPass.setFont(new Font("SansSerif", Font.PLAIN, 12));
		textVideoPass.setBounds(caseVideoPass.getLocation().x + caseVideoPass.getWidth() + 4, 120, grpURL.getSize().width - (caseVideoPass.getLocation().x + caseVideoPass.getSize().width) - 17, 21);
		textVideoPass.setEnabled(false);
		textVideoPass.setColumns(10);	
		grpURL.add(textVideoPass);
		
		caseUser = new JRadioButton(Shutter.language.getProperty("caseUser"));
		caseUser.setFont(new Font("FreeSans", Font.PLAIN, 12));
		caseUser.setBounds(66, 79, caseUser.getPreferredSize().width, 16);
		grpURL.add(caseUser);
		
		caseUser.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (caseUser.isSelected())
					textUser.setEnabled(true);
				else
				{
					textUser.setText("");
					textUser.setEnabled(false);
				}
			}
		
		});
		
		textUser = new JTextField();
		textUser.setEnabled(false);
		textUser.setText((String) null);
		textUser.setFont(new Font("SansSerif", Font.PLAIN, 12));
		textUser.setColumns(10);
		textUser.setBounds(caseVideoPass.getLocation().x + caseVideoPass.getWidth() + 4, 76, grpURL.getSize().width - (caseVideoPass.getLocation().x + caseVideoPass.getSize().width) - 17, 21);
		grpURL.add(textUser);
				
		casePass = new JRadioButton(Shutter.language.getProperty("casePass"));
		casePass.setFont(new Font("FreeSans", Font.PLAIN, 12));
		casePass.setBounds(66, 101, casePass.getPreferredSize().width, 16);
		grpURL.add(casePass);
		
		casePass.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (casePass.isSelected())
					textPass.setEnabled(true);
				else
				{
					textPass.setText("");
					textPass.setEnabled(false);
				}
			}
		
		});
		
		textPass = new JPasswordField();
		textPass.setEnabled(false);
		textPass.setText((String) null);
		textPass.setFont(new Font("SansSerif", Font.PLAIN, 12));
		textPass.setColumns(10);
		textPass.setEchoChar('•');
		textPass.setBounds(caseVideoPass.getLocation().x + caseVideoPass.getWidth() + 4, 98, grpURL.getSize().width - (caseVideoPass.getLocation().x + caseVideoPass.getSize().width) - 17, 21);
		grpURL.add(textPass);
		
	}

	private String options() {
		String options = "";
		if (caseUser.isSelected())
			options += " --username " + '"' + textUser.getText() + '"';
		if (casePass.isSelected())
			options += " --password " + '"' + new String(textPass.getPassword()) + '"';
		if (caseVideoPass.isSelected())
			options += " --video-password " + '"' + textVideoPass.getText() + '"';
		
		return options;		
	}
	
	private void startDownload() {
			Thread downloadProcess = new Thread(new Runnable() {
				public void run(){ 
						
				complete = 0;
				Shutter.lblTermine.setText(Utils.completedFiles(complete));
						
		        try {
		        	
	        		if (textURL.getText().toLowerCase().equals("update"))
	        		{
	        			FFMPEG.disableAll();
	        			Shutter.btnStart.setEnabled(false);
	        			Shutter.lblEncodageEnCours.setText(Shutter.language.getProperty("update"));
	        			YOUTUBEDL.update();
	        			Shutter.progressBar1.setIndeterminate(true);
	        			        			
	        			do 
	        			{
	        				Thread.sleep(100);
	        			}while(YOUTUBEDL.isRunning);
	        			
	        			Shutter.progressBar1.setIndeterminate(false);
	        			FFMPEG.enableAll();	 
	        			FFMPEG.enfOfFunction();	     
	        		}
	        		else 
	        		{		        	
		        		//Choix du format
						if (caseAuto.isSelected()) 
						{				
							YOUTUBEDL.format = "-f " + comboFormats.getSelectedItem().toString();
						}
						else
						{
							String[] f = comboFormats.getSelectedItem().toString().split(" ");
							
							if (comboFormats.getSelectedItem().toString().contains("audio only"))
								YOUTUBEDL.format = "-f " + f[0];
							else
								YOUTUBEDL.format = "-f " + f[0]+"+bestaudio";
						}

						FFMPEG.disableAll();
						Shutter.btnStart.setEnabled(false);
					
					    //Téléchargement	    
						YOUTUBEDL.run(textURL.getText(), options());
						
					       do { 
					    	   Thread.sleep(100);		
					       }while (YOUTUBEDL.runProcess.isAlive() && FFMPEG.cancelled == false);
					       
					       if (Shutter.cancelled)
					       {
						    	 if (YOUTUBEDL.fichierDeSortie.exists()) 
						    		 YOUTUBEDL.fichierDeSortie.delete();
					       }
					       else //Conversions Audio
					       {
					    	   Shutter.tempsRestant.setVisible(false);
					    	   String ext = YOUTUBEDL.fichierDeSortie.toString().substring(YOUTUBEDL.fichierDeSortie.toString().lastIndexOf("."));
					    	   if (caseWAV.isSelected())
					    	   {		
					    		   	Shutter.lblEncodageEnCours.setText(Shutter.language.getProperty("convertToWAV")); 
									String cmd = " -vn -y ";
									FFMPEG.run(" -i " + '"' + YOUTUBEDL.fichierDeSortie.toString() + '"' + cmd + '"'  + YOUTUBEDL.fichierDeSortie.toString().replace(ext, ".wav") + '"');	
								
								       do { 
											Thread.sleep(100);		
							       }while (FFMPEG.isRunning && FFMPEG.cancelled == false);			
									
							       //Suppression du fichier audio si processus annulé
							       if (Shutter.cancelled)
							       {
							    	   File audioFile = new File (YOUTUBEDL.fichierDeSortie.toString().replace(ext, ".wav"));
							    	   audioFile.delete();
							       }
					    	   }
					    	   else if (caseMP3.isSelected())
					    	   {
					    		   Shutter.lblEncodageEnCours.setText(Shutter.language.getProperty("convertToMP3"));  
					    		   String cmd = " -vn -acodec mp3 -b:a 256k -y ";
					    		   FFMPEG.run(" -i " + '"' + YOUTUBEDL.fichierDeSortie.toString() + '"' + cmd + '"'  + YOUTUBEDL.fichierDeSortie.toString().replace(ext, ".mp3") + '"');	
								
							       do { 
											Thread.sleep(100);		
							       }while (FFMPEG.isRunning && FFMPEG.cancelled == false);			
							       
							       //Suppression du fichier audio si processus annulé
							       if (Shutter.cancelled)
							       {
							    	   File audioFile = new File (YOUTUBEDL.fichierDeSortie.toString().replace(ext, ".mp3"));
							    	   audioFile.delete();
							       }
					    	   }		    	   			       
					       }
					       					       
					       if (Shutter.cancelled == false)
					       {
								complete++;
								Shutter.lblTermine.setText(Utils.completedFiles(complete));
								
								//Ouverture du dossier
								if (Shutter.caseOpenFolderAtEnd1.isSelected())
								{
									try {
										Desktop.getDesktop().open(new File(Shutter.lblDestination1.getText()));
									} catch (IOException e) {
										e.printStackTrace();
									}
								}
					       }
							FFMPEG.enableAll();
							FFMPEG.enfOfFunction();							
		        			Utils.sendMail(YOUTUBEDL.fichierDeSortie.toString());		        			
	        			}
					       				        
					} catch (InterruptedException e1) {}
		        }
			});
			downloadProcess.start();
					
			Utils.changeDialogVisibility(frame, true);				
	}

	private void PasteFromClipBoard(boolean mouse){
		if (textURL.getText().equals(Shutter.language.getProperty("textURL")))
			textURL.setText("");
		
			textURL.setFont(new Font("SansSerif", Font.PLAIN, 13));
       		btnOK.setEnabled(true);
			if (caseAuto.isSelected() == false)
				caseAuto.doClick();
			caseAuto.setEnabled(true);
		
		if (mouse)
		{
    	   Clipboard sysClip = Toolkit.getDefaultToolkit().getSystemClipboard();
           Transferable clipTf = sysClip.getContents(null);
           if (clipTf != null) {
               if (clipTf.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                   try {
                	   	textURL.setText((String) clipTf.getTransferData(DataFlavor.stringFlavor));
                   } catch (Exception er) {}
               }
           }
		}       
        
		if (textURL.getText().equals(Shutter.language.getProperty("textURL")))
			textURL.setText("");
		

		if (Settings.comboTheme.getSelectedItem().equals(Shutter.language.getProperty("darkTheme")))
			textURL.setForeground(Color.WHITE);
		else
			textURL.setForeground(Color.BLACK);
		textURL.setFont(new Font("SansSerif", Font.PLAIN, 13));
		caseAuto.setEnabled(true);
		btnOK.setEnabled(true);	
	}
}