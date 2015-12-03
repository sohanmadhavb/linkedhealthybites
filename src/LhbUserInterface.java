package org.semweb.assign6;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.swing.AutoCompleteSupport;

import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.awt.event.ActionEvent;
import javax.swing.JList;
import javax.swing.JComboBox;
import javax.swing.JCheckBox;

public class LhbUserInterface {

	private JFrame frame;
	private JTextField txtText;
	private JTextField textField;
	private JTextField textField_1;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LhbUserInterface window = new LhbUserInterface();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 * @throws IOException 
	 */
	public LhbUserInterface() throws IOException {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 * @throws IOException 
	 */
	private void initialize() throws IOException {
		frame = new JFrame();
		frame.setBounds(100, 100, 721, 534);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JButton btnNewButton = new JButton("Ok");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				LinkedHealthyBites lhb = new LinkedHealthyBites();
				LinkedHealthyBites lhb1 = new LinkedHealthyBites();
				try {
					lhb1.populateFOAFFriends1();
					//txtText.setText(temp);  
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				System.out.println("\nSay Hello to Myself");
				//String outpt = lhb1.mySelf1(lhb1._friends);
			//	txtText.setText(outpt);
			}
		});
		btnNewButton.setBounds(210, 433, 117, 29);
		frame.getContentPane().add(btnNewButton);
		
		txtText = new JTextField();
		txtText.setBackground(UIManager.getColor("RadioButton.shadow"));
		txtText.setBounds(225, 114, 130, 26);
		frame.getContentPane().add(txtText);
		txtText.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("Linked Healthy Bites");
		lblNewLabel.setFont(new Font("Lucida Grande", Font.BOLD, 16));
		lblNewLabel.setForeground(UIManager.getColor("RadioButton.disabledText"));
		lblNewLabel.setBounds(301, 20, 182, 35);
		frame.getContentPane().add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Restaurants");
		lblNewLabel_1.setBounds(103, 119, 110, 16);
		frame.getContentPane().add(lblNewLabel_1);
		
		textField = new JTextField();
		textField.setColumns(10);
		textField.setBackground(UIManager.getColor("RadioButton.disabledText"));
		textField.setBounds(225, 168, 130, 26);
		frame.getContentPane().add(textField);
		
		textField_1 = new JTextField();
		textField_1.setColumns(10);
		textField_1.setBackground(UIManager.getColor("RadioButton.disabledText"));
		textField_1.setBounds(225, 225, 130, 26);
		frame.getContentPane().add(textField_1);
		
		JButton btnNewButton_1 = new JButton("Start Processing");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//JOptionPane.showMessageDialog(null, "hey guys");
				
				
                 
				
			}
		});
		btnNewButton_1.setBounds(30, 424, 150, 47);
		frame.getContentPane().add(btnNewButton_1);
		String path = "http://chart.finance.yahoo.com/z?s=GOOG&t=6m&q=l";
		URL url;
		JLabel lblImagelabel = new JLabel("img");
		url = new URL(path);
		BufferedImage imge = ImageIO.read(url);
		lblImagelabel.setIcon(new ImageIcon(imge));
	
		lblImagelabel.setBounds(367, 130, 348, 318);
		frame.getContentPane().add(lblImagelabel);
		
		JLabel lblFunctions = new JLabel("Functions");
		lblFunctions.setBounds(118, 173, 78, 16);
		frame.getContentPane().add(lblFunctions);
		
		JList list = new JList();
		list.setBackground(UIManager.getColor("PopupMenu.translucentBackground"));
		list.setBounds(604, 173, -134, 29);
		frame.getContentPane().add(list);
		
		JComboBox comboBox = new JComboBox();
		Object[] elements = new Object[] {"Cat","Calendar","Catelon", "Dog", "Lion", "Mouse"};
		AutoCompleteSupport.install(comboBox, GlazedLists.eventListOf(elements));
		comboBox.setBounds(465, 80, 204, 47);
		frame.getContentPane().add(comboBox);
		
		JCheckBox chckbxNewCheckBox = new JCheckBox("Violations");
		chckbxNewCheckBox.setBounds(52, 257, 128, 23);
		frame.getContentPane().add(chckbxNewCheckBox);
		
		JCheckBox chckbxNewCheckBox_1 = new JCheckBox("Deals");
		chckbxNewCheckBox_1.setBounds(52, 303, 128, 23);
		frame.getContentPane().add(chckbxNewCheckBox_1);
		
		JCheckBox chckbxNewCheckBox_2 = new JCheckBox("Score");
		chckbxNewCheckBox_2.setBounds(52, 352, 128, 23);
		frame.getContentPane().add(chckbxNewCheckBox_2);
	}
}
