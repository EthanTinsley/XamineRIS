package XamineRIS;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Panel;
import javax.swing.JTextField;
import java.awt.Label;
import java.awt.Font;
import javax.swing.JList;
import java.awt.List;
import java.awt.TextField;
import javax.swing.JTextArea;
import javax.swing.JRadioButton;
import java.awt.Choice;
import java.awt.Color;

import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;

import java.awt.Font;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;




import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

//Nick's changes, added static frame for added logout functionality, added User parameter for displaying username, *******
public class UIRadiologist extends JFrame {

	private JPanel contentPane;
	private JFrame frame;
	
	private JPanel viewPanel;
	private JPanel actionPanel;
	private JPanel subActionPanel;
	
	private ActionListener actionPanelListener;
	private ActionListener subActionPanelListener;
	private ActionListener radioListener;
	ActionListener imageListener;
	
	private JButton homeButton;
	private JButton viewImageButton;
	private JButton submitAnalysis;
	
	ButtonGroup radioButtonGroup;
	
	private User currentUser;
	
	private Order orderTransfer;
	
	protected ArrayList<Order> orderList = new ArrayList<Order>();
	
	private static final long serialVersionUID = 1L;
	
	protected Order selectedOrder; //For use in details view
	
	protected ArrayList<JRadioButton> buttonTracker = new ArrayList<>();
	protected ArrayList<Order> orderTracker = new ArrayList<>();
	
	private static UIRadiologist window;
	
	private int currentIndex = 0;

	/**
	 * Launch the application.
	 */
	public static void main(User user) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					window = new UIRadiologist(user);
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	
	public UIRadiologist(User user) throws SQLException {
		
		currentUser = user;
		initialize();
		initializeListeners();
		GenerateOrders();
	}
	
	public void initialize() {
		
		frame = new JFrame();
		frame.setBounds(100, 100, 1204, 512);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JPanel statusBar = new JPanel();
		statusBar.setBackground(UIManager.getColor("CheckBoxMenuItem.selectionBackground"));
		statusBar.setBounds(0, 0, 1178, 25);
		frame.getContentPane().add(statusBar);
		statusBar.setLayout(null);
		
		JLabel versionText = new JLabel("Radiologist Portal");
		versionText.setBounds(0, 0, 125, 23);
		statusBar.add(versionText);
		versionText.setHorizontalAlignment(SwingConstants.CENTER);
		
		JButton logoutButton = new JButton("Log out");
		logoutButton.setBounds(1043, 3, 125, 20);
		statusBar.add(logoutButton);
		
		ActionListener logoutListener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent click) {
				
				UILogin.main(null);
				frame.dispose();
				
			}//end Action
		};//end logoutListener
			
		logoutButton.addActionListener(logoutListener);
		
		JLabel nameSpace = new JLabel(currentUser.getFirstName() + " " + currentUser.getLastName());
		nameSpace.setHorizontalAlignment(SwingConstants.CENTER);
		nameSpace.setBounds(384, 4, 265, 14);
		statusBar.add(nameSpace);

		actionPanel = new JPanel();
		actionPanel.setBounds(10, 37, 239, 212);
		frame.getContentPane().add(actionPanel);
		actionPanel.setLayout(null);
		
		homeButton = new JButton("Home");
		homeButton.setBounds(10, 25, 203, 23);
		actionPanel.add(homeButton);
		
	actionPanelListener = new ActionListener() {
			
			public void actionPerformed(ActionEvent click) {
				
				if(click.getSource() == homeButton) {
					try {
						GenerateOrders();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}//end if
				
				
				
			}//end Action
		};//end actionPanelListener
		
		homeButton.addActionListener(actionPanelListener);
		
		JButton searchOrder = new JButton("Search Orders");
		searchOrder.setBounds(10, 75, 203, 23);
		actionPanel.add(searchOrder);
		
		searchOrder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				SearchOrder();
			}


		});
		
		subActionPanel = new JPanel();
		subActionPanel.setLayout(null);
		subActionPanel.setBounds(10, 250, 239, 212);
		frame.getContentPane().add(subActionPanel);
		
		viewPanel = new JPanel();
		viewPanel.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		viewPanel.setBounds(259, 36, 919, 426);
		frame.getContentPane().add(viewPanel);
		viewPanel.setLayout(null);	
		
		//Button group for all generated radio buttons
				radioButtonGroup = new ButtonGroup(); 
	
	
		Panel viewOrdersButtonPanel = new Panel();
		viewOrdersButtonPanel.setBounds(255, 33, 913, 420);
		viewOrdersButtonPanel.setLayout(null);
		
		JLabel lblviewOrderInfo = new JLabel("Order Information");
		lblviewOrderInfo.setBounds(93, 35, 148, 14);
		viewOrdersButtonPanel.add(lblviewOrderInfo);
		
		JLabel lblviewOrderOrderIdNumber = new JLabel("Order ID Number");
		lblviewOrderOrderIdNumber.setBounds(15, 70, 130, 14);
		viewOrdersButtonPanel.add(lblviewOrderOrderIdNumber);
		
		JTextField txtviewOrderOrderId = new JTextField();
		txtviewOrderOrderId.setBounds(138, 67, 129, 20);
		viewOrdersButtonPanel.add(txtviewOrderOrderId);
		txtviewOrderOrderId.setColumns(10);
		
		JLabel lblviewOrderPatient = new JLabel("Patient Information");
		lblviewOrderPatient.setBounds(93, 108, 148, 14);
		viewOrdersButtonPanel.add(lblviewOrderPatient);
		
		JLabel lblviewOrderFirstName = new JLabel("First Name");
		lblviewOrderFirstName.setBounds(15, 146, 75, 14);
		viewOrdersButtonPanel.add(lblviewOrderFirstName);
			
		JTextField txtviewOrderFirstName = new JTextField();
		txtviewOrderFirstName.setBounds(138, 143, 129, 20);
		viewOrdersButtonPanel.add(txtviewOrderFirstName);
		txtviewOrderFirstName.setColumns(10);
	
		JLabel lblviewOrderLastName = new JLabel("Last Name");
		lblviewOrderLastName.setBounds(15, 190, 75, 24);
		viewOrdersButtonPanel.add(lblviewOrderLastName);
		
		TextField txtviewOrderLastName = new TextField();
		txtviewOrderLastName.setBounds(138, 192, 129, 20);
		viewOrdersButtonPanel.add(txtviewOrderLastName);
		
		JLabel lblviewOrderDateOfBirth = new JLabel("Date Of Birth");
		lblviewOrderDateOfBirth.setBounds(15, 247, 87, 14);
		viewOrdersButtonPanel.add(lblviewOrderDateOfBirth);
		
		JTextField txtviewOrderDateOfBirth = new JTextField();
		txtviewOrderDateOfBirth.setText("YYYY-MM-DD");
		txtviewOrderDateOfBirth.setBounds(138, 244, 129, 20);
		viewOrdersButtonPanel.add(txtviewOrderDateOfBirth);
		txtviewOrderDateOfBirth.setColumns(10);
			
		JButton Searchbutton = new JButton("Search");
		Searchbutton.setBounds(85, 300, 257, 23);
		viewOrdersButtonPanel.add(Searchbutton);
	
}
	
	public void initializeListeners() {
		
		//First Listener for radio button selection, selecting a radio button for either orders or appointments will generate a "Details" button along with details listener
		 radioListener = new ActionListener() {
			
			public void actionPerformed(ActionEvent selection) {
				
				Object selected = selection.getSource();
				int selectedIndex = buttonTracker.indexOf(selected);
				orderTransfer = orderTracker.get(selectedIndex);
				
				JButton detailsButton = new JButton("Details");
				detailsButton.setBounds(10, 30, 219, 37);
				subActionPanel.add(detailsButton);
				subActionPanel.repaint();
				subActionPanel.revalidate();
				
				ActionListener detailListener = new ActionListener() {
					
					public void actionPerformed(ActionEvent click) {
						
						detailView(orderTransfer);
					}//end Action
				};//end detailListener
				

				
				detailsButton.addActionListener(detailListener);
			}//end Action
		};//end radioListener
		
		
	}
	
	private void GenerateOrders() throws SQLException {
		//Setup Users radio list on portal generation and Home Button click
						
						subActionPanel.removeAll();
						viewPanel.removeAll();
						
						buttonTracker.clear();					
						orderTracker.clear();
						
						//CALL SQL HERE, SIMLILAR TO TEAMS MODALITIES ETC.
						orderList = ViewOrders();
						
						int yValue = 10;
						int xValue = 20;					
						
						//fills out view panel with radio buttons for each order that has not been completed.
						for(int i = 0; i < orderList.size(); i++) {
							
							JRadioButton orderRdButton = new JRadioButton("Order: " + orderList.get(i).getOrderID() + " - " + orderList.get(i).getPatient().getFirstName() + " " + orderList.get(i).getPatient().getLastName());
							orderRdButton.setBounds(xValue, yValue, 250, 15);
							viewPanel.add(orderRdButton);
							orderRdButton.addActionListener(radioListener);
							radioButtonGroup.add(orderRdButton);
							
							buttonTracker.add(orderRdButton);
							orderTracker.add(orderList.get(i));
							
							yValue += 25;
							
						}//end for
						
						subActionPanel.repaint();
						subActionPanel.revalidate();
						viewPanel.repaint();
						viewPanel.revalidate();

			
		}//end GenerateOrders
	
	private void SearchOrder() {
		
		subActionPanel.removeAll();
		viewPanel.removeAll();

		Label lblsearchOrderID = new Label("Order ID*");
		lblsearchOrderID.setBounds(15, 33, 95, 22);
		viewPanel.add(lblsearchOrderID);
		
		JTextField txtsearchOrderID = new JTextField();
		txtsearchOrderID.setBounds(125, 38, 164, 20);
		viewPanel.add(txtsearchOrderID);
		txtsearchOrderID.setColumns(10);
		
		JButton submitOrder = new JButton("Submit ");
		submitOrder.setBounds(15, 72, 74, 22);
		viewPanel.add(submitOrder);	
	
		
		submitOrder.addActionListener(new ActionListener() {

	        @Override
	        public void actionPerformed(ActionEvent click) {
	        	
	        	//SQL to grab one order
	        	try {
					orderTransfer = ReturnOrder(Integer.valueOf(txtsearchOrderID.getText()));
					if(orderTransfer != null) {
						detailView(orderTransfer);
						
					}//end if
					
				} catch (NumberFormatException | SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        }
	    });
		
		subActionPanel.repaint();
		subActionPanel.revalidate();
		viewPanel.repaint();
		viewPanel.revalidate();
		
	}
	
	public void detailView(Order order) {
		
		//Setup Details view
		subActionPanel.removeAll();
		viewPanel.removeAll();
		
		//Patient info (Allergies, name, gender, age), checked-in, Appt ID, Date, Time, Order ID, Order Status, Modality, Imaging, Images on order
		//Generate Details View
		JLabel orderIDLabel = new JLabel("Order ID:");
		orderIDLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		orderIDLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		orderIDLabel.setBounds(95, 50, 100, 14);
		viewPanel.add(orderIDLabel);
		
		JLabel orderIDOut = new JLabel(String.valueOf(order.getOrderID()));
		orderIDOut.setFont(new Font("Tahoma", Font.PLAIN, 18));
		orderIDOut.setBounds(205, 50, 100, 14);
		viewPanel.add(orderIDOut);
		
		JLabel patientNameLabel = new JLabel("Patient:");
		patientNameLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		patientNameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		patientNameLabel.setBounds(95, 100, 100, 24);
		viewPanel.add(patientNameLabel);
		
		JLabel patientNameOut = new JLabel(order.getPatient().getFirstName() + " " + order.getPatient().getLastName());
		patientNameOut.setFont(new Font("Tahoma", Font.PLAIN, 14));
		patientNameOut.setBounds(205, 100, 140, 24);
		viewPanel.add(patientNameOut);
		
		JLabel orderStatusLabel = new JLabel("Order Status:");
		orderStatusLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		orderStatusLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		orderStatusLabel.setBounds(95, 150, 100, 24);
		viewPanel.add(orderStatusLabel);
		
		JLabel orderStatusOut = new JLabel(order.getOrderStatus());
		orderStatusOut.setFont(new Font("Tahoma", Font.PLAIN, 14));
		orderStatusOut.setBounds(205, 150, 140, 24);
		viewPanel.add(orderStatusOut);
		
		JTextArea analysisTextArea = new JTextArea();
		analysisTextArea.setBounds(446, 62, 292, 290);
		
		if (order.getRadioAnalysis() != null) analysisTextArea.setText(order.getRadioAnalysis());
		
		viewPanel.add(analysisTextArea);
		analysisTextArea.setColumns(10);
		
		JLabel analysis = new JLabel("Analysis");
		analysis.setBounds(502, 23, 174, 31);
		viewPanel.add(analysis);
		analysis.setHorizontalAlignment(SwingConstants.CENTER);
		analysis.setFont(new Font("Lucida Grande", Font.BOLD, 17));
		
		JLabel filesLabel = new JLabel("Files on Order:");
		filesLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		filesLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		filesLabel.setBounds(40, 200, 100, 24);
		viewPanel.add(filesLabel);
		
		JLabel filesOut = new JLabel("");
		filesOut.setFont(new Font("Tahoma", Font.PLAIN, 14));
		filesOut.setBounds(150, 200, 508, 24);
		viewPanel.add(filesOut);
		
		if (orderTransfer.getImages().isEmpty() != true) {
			
			for(int i = 0; i < orderTransfer.getImages().size(); i++) {
				filesOut.setText(filesOut.getText() + order.getImages().get(i).getLabel() + "   ");
			}//end for
		}//end if
		
		viewImageButton = new JButton("View Image");
		viewImageButton.setBounds(125, 250, 150, 25);
		viewPanel.add(viewImageButton);	
		
		
		 imageListener = new ActionListener() {
			
			public void actionPerformed(ActionEvent click) {
				
				
				 if (click.getSource() == viewImageButton ) {
					
					if (orderTransfer.getImages().isEmpty() != true) {
						
		
						ImageIcon icon = new ImageIcon(orderTransfer.getImages().get(0).getPath());
						
						JLabel imageLabel = new JLabel(icon);
						imageLabel.setBounds(0, 0, 410, 420);
						
						ImageViewer imageFrame = new ImageViewer();
						imageFrame.setVisible(true);
						
						JButton nextButton = new JButton("Next Image");
						nextButton.setBounds(514, 431, 130, 23);
						imageFrame.contentPane.add(nextButton);
						
						JButton prevButton = new JButton("Previous Image");
						prevButton.setBounds(234, 431, 130, 23);
						imageFrame.contentPane.add(prevButton);
						
						imageFrame.viewImagePanel.add(imageLabel, BorderLayout.CENTER);
						imageFrame.viewImagePanel.repaint();
						imageFrame.viewImagePanel.revalidate();
						
						ActionListener imageFrameListener = new ActionListener() {
						
							public void actionPerformed(ActionEvent click) {
								
								System.out.println("View image pressed");
								ImageIcon currentIcon = new ImageIcon();
								
								if (click.getSource() == nextButton) {
									
									if (currentIndex + 1 < orderTransfer.getImages().size()) {
										
										currentIndex = currentIndex+1;
										currentIcon = new ImageIcon(orderTransfer.getImages().get(currentIndex).getPath()); 
										imageLabel.setIcon(currentIcon);
										imageFrame.viewImagePanel.repaint();
										imageFrame.viewImagePanel.revalidate();
										
									
									}//end nested if
									
									else {
										
										currentIndex = 0;
										currentIcon = new ImageIcon(orderTransfer.getImages().get(currentIndex).getPath());  
										imageLabel.setIcon(currentIcon);
										imageFrame.viewImagePanel.repaint();
										imageFrame.viewImagePanel.revalidate();
										
									}//end nested else
									
									System.out.println("Next pressed");
								}//end if
								
								else if (click.getSource() == prevButton) {
									
									if (currentIndex -1 >= 0) {
										
										currentIndex = currentIndex-1;
										currentIcon = new ImageIcon(orderTransfer.getImages().get(currentIndex).getPath()); 
										imageLabel.setIcon(currentIcon);
										imageFrame.viewImagePanel.repaint();
										imageFrame.viewImagePanel.revalidate();
										
									
									}//end nested if
									
									else {
										
										currentIndex = orderTransfer.getImages().size()-1;										
										currentIcon = new ImageIcon(orderTransfer.getImages().get(currentIndex).getPath()); 
										imageLabel.setIcon(currentIcon);
										imageFrame.viewImagePanel.repaint();
										imageFrame.viewImagePanel.revalidate();
										
									}//end nested else
									
								}//end else if
								
							}//end Action
						};//end imageFrameListener
						
						
						nextButton.addActionListener(imageFrameListener);
						prevButton.addActionListener(imageFrameListener);
						
						
						
					}//end if
						
				}//end else if
			}//end Action
				
			};//end imageListener
			
		viewImageButton.addActionListener(imageListener);
		
		submitAnalysis = new JButton("Submit Analysis");
		submitAnalysis.setBounds(650, 375, 150, 25);
		viewPanel.add(submitAnalysis);	
		
		ActionListener analysisListener = new ActionListener() {
			
			public void actionPerformed (ActionEvent click){
				order.setRadioAnalysis(analysisTextArea.getText());
				
				//SQL to update database
				try {
					UpdateAnalysis(order.getOrderID(), order.getRadioAnalysis());
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				try {
					GenerateOrders() ;
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}//end Action

		};//end analysisListener
		
		submitAnalysis.addActionListener(analysisListener);
		
		JButton saveAnalysis = new JButton("Save Analysis");
		saveAnalysis.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				order.setRadioAnalysis(analysisTextArea.getText());
				
					try {
						saveAnalysis(order.getOrderID(), order.getRadioAnalysis());
					} catch (SQLException error) {
						error.printStackTrace();
					}
				
					try {
						GenerateOrders() ;
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					};
			}
		});
		saveAnalysis.setBounds(400, 375, 150, 25);
		viewPanel.add(saveAnalysis);
		
		
		subActionPanel.repaint();
		subActionPanel.revalidate();
		viewPanel.repaint();
		viewPanel.revalidate();
	}
	
	public static Connection getConnection(){
		 try{
			   String driver = "com.mysql.cj.jdbc.Driver";
			   String url = "jdbc:mysql://localhost:3306/xaminedatabase";
			   String username = "root";
			   String password = "Restoration2021!";
			   Class.forName(driver);
			   
			   Connection conn = DriverManager.getConnection(url,username,password);
			   System.out.println("Connected");
			   System.out.println("Restoration2021!");
			   return conn;
			  } 
		 catch(Exception e){
			 System.out.println(e);
		}	  
		return null ;
	}
	
	public static ArrayList<Order> ViewOrders() throws SQLException {
		/*Searches through orders for current day's appointments
		 * Puts Orders for selected date in arraylist for UIManager to access and display
		 * UIManager displays orderID, appt time, room number, and imaging ordered
		 * User can select appt and press select button to display patient information
		 */		
		
		//Adding parameters for testing
		ArrayList<Order> orders = new ArrayList<>();
		Patient currPatient ;
		int index = 0;
		Connection conn = getConnection();
		
		PreparedStatement statement = conn.prepareStatement("Select * from imagingorder Where orderStatus = ? ; " ) ;
		statement.setString(1, "Imaging Complete");
		
		ResultSet result = statement.executeQuery() ;
		
		while(result.next()) {
			currPatient = ReturnPatient(result.getInt("patientID") ) ;
			orders.add(index, new Order(result.getInt("orderID")));
			orders.get(index).setImagingOrder(result.getString("imagingNeeded"));
			orders.get(index).setVisitReason(result.getString("visitReason"));
			orders.get(index).setOrderStatus(result.getString("orderStatus"));
			orders.get(index).setApptTime(result.getString("appointment"));
			orders.get(index).setPatient(currPatient);
			orders.get(index).setImages(getOrderImages(orders.get(index).getOrderID()));
			orders.get(index).setRadioAnalysis(result.getString("technicalReport"));
			
			index++ ;
		}
		
		System.out.println(" Orders found successfully");
		return orders ;
	}
	
	public static Order ReturnOrder(int ID) throws SQLException {
		
		Order foundOrder = null;
		Patient foundPatient;
		ArrayList<Modality> modalities = ReturnMod();
		
		 Connection conn = getConnection() ;
		 PreparedStatement statement = conn.prepareStatement("Select * from imagingorder Where orderID  = ?  ;") ;
		 
		statement.setInt(1, ID );
		
		ResultSet result = statement.executeQuery() ;
		
		while(result.next()) {
			 foundOrder = new Order(result.getInt("orderID")) ;
			 foundPatient = ReturnPatient(result.getInt("patientID") ) ;
			 foundOrder.setPatient(foundPatient);
			 foundOrder.setOrderStatus(result.getString("orderStatus"));
			 foundOrder.setVisitReason(result.getString("visitReason"));
			 foundOrder.setImagingOrder(result.getString("imagingNeeded"));
			 foundOrder.setTeamID(result.getInt("teamID"));
			 
			//Find modality matching this user
				for(int i = 0; i < modalities.size(); i++) {
					if(modalities.get(i).getModalityID().equals(result.getString("modalityID"))) {
						foundOrder.setModality(modalities.get(i));
						break;
					}//end if
				}//end for
			
	}
		 
		result.close();
		
		foundOrder.setImages(getOrderImages(foundOrder.getOrderID()));
		
		return foundOrder;
	}
	
	public static ArrayList<ImageFile> getOrderImages(int OrderID) throws SQLException {
		
		ArrayList<ImageFile> images = new ArrayList<ImageFile>() ;
		
		Connection conn = getConnection() ;
		PreparedStatement statement = conn.prepareStatement("Select * from image Where orderID  = ?  ;") ;
		 
		statement.setInt(1, OrderID );
		
		ResultSet result = statement.executeQuery() ;
		
		while(result.next()) {
			ImageFile image = new ImageFile(result.getInt("imageID"), null , result.getString("pathName"), result.getString("imagelabel"), null);
			images.add(image) ;
			System.out.println(image.getLabel());
		}
		
		return images ;
	}
	
	public static ArrayList<Modality> ReturnMod() throws SQLException {
		
		 ArrayList<Modality> currMod = new ArrayList<>();
		 int index = 0 ;
		 
		 Connection conn = getConnection() ;
		 PreparedStatement statement = conn.prepareStatement("Select * from modality ;") ;
		
		ResultSet result = statement.executeQuery() ;
		
		while(result.next()) {
			currMod.add(index, new Modality(result.getString("modalityName")));
			currMod.get(index).setModalityID(result.getString("modalityID"));
			index++;		
	}
		 
		result.close();
		
		return currMod ;
	}
	
	public static Patient ReturnPatient(int ID) throws SQLException {
		
		 Patient currPatient = new Patient("", "");
		 
		 Connection conn = getConnection() ;
		 PreparedStatement statement = conn.prepareStatement("Select * from patient Where patientID  = ?  ;") ;
		 
		statement.setInt(1, ID );
		
		ResultSet result = statement.executeQuery() ;
		
		while(result.next()) {
			 currPatient = new Patient(result.getString("firstName"), result.getString("lastName")) ;
			 currPatient.setPatientId(result.getInt("patientID"));
			 currPatient.setEmail(result.getString("email"));
			 currPatient.setPhoneNumber(result.getString("phoneNumber"));
			 currPatient.setAllergyLatex(result.getBoolean("allergyLatex"));
			 currPatient.setAllergyMridye(result.getBoolean("allergyMridye"));
			 currPatient.setAllergyXraydye(result.getBoolean("allergyXrayDye"));
			 currPatient.setNotes(result.getString("notes"));
			 currPatient.setDateOfBirth(result.getString("dateOfBirth"));
	}
		 
		
		result.close();
		
		return currPatient;
	}
	
	private void UpdateAnalysis(int orderID, String report) throws SQLException {
		
		 Connection conn = getConnection() ;
		 PreparedStatement statement = conn.prepareStatement("Update imagingorder "
		 		+ "SET orderStatus = ?, technicalReport = ?"
		 		+ "WHERE orderID = ?;") ;
		 
		statement.setString(1, "Complete");
		statement.setString(2, report);
		statement.setInt(3, orderID );
		
		statement.executeUpdate();
		
	}
	
	private void saveAnalysis(int orderID, String report) throws SQLException {
		 Connection conn = getConnection() ;
		 PreparedStatement statement = conn.prepareStatement("Update imagingorder "
		 		+ "SET technicalReport = ?"
		 		+ "WHERE orderID = ?;") ;
		 
		
		statement.setString(1, report);
		statement.setInt(2, orderID );
		
		statement.executeUpdate();
		
		System.out.println("Analysis Saved Correctly");
	}
}
