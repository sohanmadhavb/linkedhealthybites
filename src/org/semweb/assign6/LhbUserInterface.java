package org.semweb.assign6;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
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
import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.JList;
import javax.swing.JComboBox;
import javax.swing.JCheckBox;
import java.awt.Color;

public class LhbUserInterface {

	private JFrame frame;

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
	
	@SuppressWarnings("unchecked")
	public void display(List<String> input) throws IOException
	{
		String[] data = new String[input.size()];
		JTextArea txt = new JTextArea();
		frame.getContentPane().add(txt);
		input.toArray(data);
		StringBuffer tmp = new StringBuffer();
		for( int i = 0 ; i < input.size(); i++ )
		{
		    tmp.append( input.get(i) + "\n");
		}
		txt.setText(tmp.toString());

	}
	
	private void showImage(String path) throws IOException {
		URL url = new URL(path);
		BufferedImage image = ImageIO.read(url);
		JLabel label = new JLabel(new ImageIcon(image));
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.getContentPane().add(label);
		f.pack();
		f.setLocation(200, 200);
		f.setVisible(true);

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

		JButton btnOk = new JButton("Ok");
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnOk.setBounds(238, 433, 117, 29);
		frame.getContentPane().add(btnOk);
		
		final JTextArea textArea = new JTextArea();
		textArea.setRows(4);
		textArea.setColumns(4);
		textArea.setTabSize(20);
		textArea.setBounds(367, 131, 348, 357);
		frame.getContentPane().add(textArea);
		textArea.setVisible(false);

		JLabel lblNewLabel = new JLabel("Linked Healthy Bites");
		lblNewLabel.setFont(new Font("Lucida Grande", Font.BOLD, 19));
		lblNewLabel.setForeground(Color.RED);
		lblNewLabel.setBounds(238, 26, 225, 35);
		frame.getContentPane().add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("Pick your favourite Restaurant");
		lblNewLabel_1.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		lblNewLabel_1.setForeground(Color.WHITE);
		lblNewLabel_1.setBounds(23, 105, 211, 16);
		frame.getContentPane().add(lblNewLabel_1);

		final JCheckBox chckbxViolations = new JCheckBox("Violations");
		chckbxViolations.setForeground(Color.WHITE);
		chckbxViolations.setBounds(18, 255, 128, 23);
		frame.getContentPane().add(chckbxViolations);

		final JCheckBox chckbxDeals = new JCheckBox("Deals");
		chckbxDeals.setForeground(Color.WHITE);
		chckbxDeals.setBounds(92, 220, 128, 23);
		frame.getContentPane().add(chckbxDeals);

		final JCheckBox chckbxInspections = new JCheckBox("Inspections");
		chckbxInspections.setForeground(Color.WHITE);
		chckbxInspections.setBounds(151, 255, 128, 23);
		frame.getContentPane().add(chckbxInspections);	

		LinkedHealthyBites restauratName = new LinkedHealthyBites();	// restaurant names

		restauratName.restaurantRdftomodel();

		List<String> nameOfAllRestaurant = restauratName.retrieveRestaurantName(restauratName.restaurantModel );				// Storing names of all restaurant for later use

		Object[] elements = new Object[nameOfAllRestaurant.size()] ;

		int i=0;
		for(String temp : nameOfAllRestaurant)
		{
			elements[i] = temp;
			i++;
		}

		final JComboBox<String> restaurantListDropDown = new JComboBox<String>();
		restaurantListDropDown.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		restaurantListDropDown.setForeground(Color.BLUE);

		AutoCompleteSupport.install(restaurantListDropDown, GlazedLists.eventListOf(elements));		// Display all restaurant names in a drop-down
		restaurantListDropDown.setBounds(259, 91, 204, 47);
		frame.getContentPane().add(restaurantListDropDown);

		JButton btnStartProcessing = new JButton("Start Processing");	
		btnStartProcessing.setBounds(30, 424, 150, 47);
		frame.getContentPane().add(btnStartProcessing);

		btnStartProcessing.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {


				String selectedRestaurant = (String) restaurantListDropDown.getSelectedItem();		// picking the restaurant selected by user

				String restaurantID = restauratName.retrieveIDagainstRName(restauratName.restaurantModel ,selectedRestaurant);	// picking the ID of restaurant selected

				if(chckbxDeals.isSelected())														// If Deals check-box is checked
				{
					LinkedHealthyBites restaurantDeals = new LinkedHealthyBites();
					LinkedHealthyBites restaurantRatings = new LinkedHealthyBites();
					try
					{
						restaurantDeals.dealsRdftoModel(); 
						restaurantRatings.ratingsRdftoModel();
					} 
					catch (IOException e1)
					{
						e1.printStackTrace();
					}
					try {
						restaurantDeals.retrieveDeals(restaurantDeals.dealsModel, selectedRestaurant);
						List<String> output = restaurantRatings.retrieveRatings(restaurantRatings.ratingsModel, selectedRestaurant);
						textArea.setText(output.toString()); 
						textArea.setVisible(true); 
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
				if(chckbxInspections.isSelected())									// If Inspections check-box is checked
				{
					LinkedHealthyBites restaurantInspections = new LinkedHealthyBites();
					try 
					{
						restaurantInspections.inspectionsRdftoModel();
					} 
					catch (IOException e1)
					{
						e1.printStackTrace();
					}
					List<String> output = restaurantInspections.retrieveInspections(restaurantInspections.inspectionsModel, restaurantID);
					textArea.setText(output.toString()); 
					textArea.setVisible(true); 
				}
				if (chckbxViolations.isSelected())									// If Violations check-box is checked
				{	
					LinkedHealthyBites restaurantViolations = new LinkedHealthyBites();
					try 
					{
						restaurantViolations.violationsRdftoModel();
					} 
					catch (IOException e1) 
					{
						e1.printStackTrace();
					}
					try 
					{
						List<String> output = restaurantViolations.retrieveViolations(restaurantViolations.violationsModel, restaurantID);
						textArea.setText(output.toString()); 
						textArea.setVisible(true); 
					} 
					catch (IOException e2) 
					{
						e2.printStackTrace();
					}
				}
			}
		});

		String path = "http://www.8coupons.com/image/deal/13516077";	// TODO change this
		URL url;

		url = new URL(path);
		BufferedImage imge = ImageIO.read(url);

		String path1 = "http://www.8coupons.com/image/deal/13516073";
		URL url1;

		url1 = new URL(path1);
		BufferedImage imge1 = ImageIO.read(url1);

		JLabel lblFunctions = new JLabel("What are you interested in?");
		lblFunctions.setForeground(Color.WHITE);
		lblFunctions.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		lblFunctions.setBounds(30, 187, 204, 16);
		frame.getContentPane().add(lblFunctions);

		JList list = new JList();
		list.setBackground(UIManager.getColor("PopupMenu.translucentBackground"));
		list.setBounds(604, 173, -134, 29);
		frame.getContentPane().add(list);

		JLabel lblRatings = new JLabel("Ratings");
		lblRatings.setForeground(Color.WHITE);
		lblRatings.setIcon(new ImageIcon("/Users/agmip/Desktop/bk2.png"));
		lblRatings.setBounds(6, 29, 709, 500);
		frame.getContentPane().add(lblRatings);
		
		JCheckBox chckbxRatings = new JCheckBox("Ratings");
		chckbxRatings.setForeground(Color.WHITE);
		chckbxRatings.setBounds(92, 304, 128, 23);
		frame.getContentPane().add(chckbxRatings);
		
		

	}
}
