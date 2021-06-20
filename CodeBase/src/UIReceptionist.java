package XamineRIS;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.EventQueue;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import java.awt.Panel;
import javax.swing.JTextField;
import javax.swing.Popup;

import java.awt.Label;
import java.awt.Font;
import javax.swing.JList;
import javax.swing.JOptionPane;

import java.awt.List;
import java.awt.TextField;
import javax.swing.JTextArea;
import javax.swing.JRadioButton;
import java.awt.Choice;
import java.awt.Color;

//toDo
//Fix date and time display once databasse is updated

public class UIReceptionist extends JFrame {

	private JPanel contentPane;
	private Panel todayAppointmentPanel;
	
	private JTextField txtsearchPatientFirstName;
	private JTextField txtsearchPatientLastName;
	private JTextField txtsearchPatientDoB;
	private JTextField txtsearchPatientEmail;
	private JTextField txtviewOrderOrderId;
	
	private static UIReceptionist frame;
	
	private User currentUser;
	
	private ArrayList<Team> testTeams = new ArrayList<>();
	
	private Order orderTransfer;
	
	//Teams for test use
	private Team team1 = new Team(1, "Team1");
	private Team team2 = new Team(2, "Team2");
	private Team team3 = new Team(3, "Team3");
	
	private JPanel actionPanel;
	private JPanel subActionPanel;
	
	private JButton todaysAppointments;
	private JButton viewUnscheduledOrders;
	private JButton searchPatient;
	
	protected ArrayList<JRadioButton> buttonTracker = new ArrayList<>();
	protected ArrayList<Order> orderTracker = new ArrayList<>();

	/**
	 * Launch the application.
	 */
	public static void main(User user) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame = new UIReceptionist(user);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public static Connection getConnection(){
		 try{
			   String driver = "com.mysql.cj.jdbc.Driver";
			   String url = "jdbc:mysql://localhost:3306/xaminedatabase";
			   String username = "root";
			   String password = "";
			   Class.forName(driver);
			   
			   Connection conn = DriverManager.getConnection(url,username,password);
			   System.out.println("Connected");
			   System.out.println("");
			   return conn;
			  } 
		 catch(Exception e){
			 System.out.println(e);
		}	  
		return null ;
	}

	/**
	 * Create the frame.
	 */
	public UIReceptionist(User user) {
		currentUser = user;
		
		testTeams.add(team1);
		testTeams.add(team2);
		testTeams.add(team3);
		
		Initialize();
		InitializeListeners();
		
		
		JLabel lblSuccessfulCheckIn = new JLabel("Patient Checked-In Successfully! ");
		lblSuccessfulCheckIn.setForeground(Color.BLACK);
		lblSuccessfulCheckIn.setBounds(10, 395, 175, 14);
		
	}//end constructor
	
	private void Initialize() {
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1204, 512);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel statusBar = new JPanel();
		statusBar.setBackground(UIManager.getColor("CheckBoxMenuItem.selectionBackground"));
		statusBar.setBounds(0, 0, 1178, 25);
		getContentPane().add(statusBar);
		statusBar.setLayout(null);
		
		JLabel versionText = new JLabel("Receptionist Portal");
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
			}
		};
			
		logoutButton.addActionListener(logoutListener);
		
		JLabel nameSpace = new JLabel(currentUser.getFirstName() + " " + currentUser.getLastName());
		nameSpace.setHorizontalAlignment(SwingConstants.CENTER);
		nameSpace.setBounds(384, 4, 265, 14);
		statusBar.add(nameSpace);
		
		JButton notifyButton = new JButton("Notifications");
		notifyButton.setBounds(928, 3, 105, 20);
		statusBar.add(notifyButton);
		
		subActionPanel = new JPanel();
		subActionPanel.setLayout(null);
		subActionPanel.setBounds(10, 248, 239, 214);
		getContentPane().add(subActionPanel);
		
		actionPanel = new JPanel();
		subActionPanel.add(actionPanel);
		actionPanel.setBounds(10, 33, 239, 420);
		actionPanel.setLayout(null);
		
		todaysAppointments = new JButton("Today's Appointments");
		todaysAppointments.setBounds(10, 28, 203, 23);
		actionPanel.add(todaysAppointments);
		
		searchPatient = new JButton("Search Patients");
		searchPatient.setBounds(10, 130, 203, 23);
		actionPanel.add(searchPatient);
		
		viewUnscheduledOrders = new JButton("Unscheduled orders");
		viewUnscheduledOrders.setBounds(10, 62, 203, 23);
		actionPanel.add(viewUnscheduledOrders);
		
	}//end Initialize
	
	public void InitializeListeners() {
		
		todaysAppointments.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				try {
					ViewAppts();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		viewUnscheduledOrders.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				try {
					ViewUnScheduledAppts();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		searchPatient.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			
				Search();
			}
		});
	}//end InitializeListener
	
	
	public void ViewAppts() throws SQLException {
		

		// here is the panel that displays todays appointments 
		//
		//
		subActionPanel.removeAll();	
		subActionPanel.add(actionPanel);
		
		todayAppointmentPanel = new Panel();
		todayAppointmentPanel.setBounds(255, 33, 913, 420);
		todayAppointmentPanel.setLayout(null);
		
		JLabel lblCurrentOrderTodayAppointments = new JLabel("");
		lblCurrentOrderTodayAppointments.setBounds(100, 22, 122, 14);
		todayAppointmentPanel.add(lblCurrentOrderTodayAppointments);		
		
		JLabel lblAppointmentsDate = new JLabel(String.valueOf(LocalDate.now()));
		lblAppointmentsDate.setBounds(594, 22, 139, 14);//use this xy coords
		todayAppointmentPanel.add(lblAppointmentsDate);
		
		JLabel lblTodaysAppointments = new JLabel("Today's Appointments");
		lblTodaysAppointments.setBounds(566, 0, 190, 18);
		todayAppointmentPanel.add(lblTodaysAppointments);
		lblTodaysAppointments.setFont(new Font("Tahoma", Font.PLAIN, 16));
		
		JLabel lblPatientNameLabel = new JLabel("Patient: ");
		lblPatientNameLabel.setBounds(10, 47, 47, 14);
		todayAppointmentPanel.add(lblPatientNameLabel);
		
		JLabel lblNamelabel = new JLabel("Last Name , First Name");
		lblNamelabel.setBounds(60, 47, 275, 14);
		todayAppointmentPanel.add(lblNamelabel);
		
		JLabel lblAppointmentLabel = new JLabel("Appointment:");
		lblAppointmentLabel.setBounds(10, 75, 79, 14);
		todayAppointmentPanel.add(lblAppointmentLabel);
		
		JLabel lblInsertADate = new JLabel("Insert a Date and time here. Maybe just time ");
		lblInsertADate.setBounds(90, 75, 260, 14);
		todayAppointmentPanel.add(lblInsertADate);
		
		JLabel lblImagingNeededLabel = new JLabel("-- Imaging Required --");
		lblImagingNeededLabel.setBounds(100, 100, 122, 14);
		todayAppointmentPanel.add(lblImagingNeededLabel);
		
		JTextArea textAreaTodayAppointment = new JTextArea();
		textAreaTodayAppointment.setFont(new Font("Tahoma", Font.PLAIN, 13));
		textAreaTodayAppointment.setLineWrap(true);
		textAreaTodayAppointment.setText("");
		textAreaTodayAppointment.setBounds(10, 125, 315, 181);
		todayAppointmentPanel.add(textAreaTodayAppointment);
		
		JButton btnCheckIn = new JButton("Appointment Check-in");
		btnCheckIn.setBounds(60, 345, 180, 23);
		todayAppointmentPanel.add(btnCheckIn);
		
		btnCheckIn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				todayAppointmentPanel.removeAll();
				if(orderTransfer != null) {
				try {
					CheckIn(orderTransfer.getPatient().getFirstName(), orderTransfer.getPatient().getLastName());
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}//end if
			}//end Action
		});	
				
		//Special listener for radio buttons
		ActionListener radioListener = new ActionListener() {
			
			public void actionPerformed(ActionEvent selection) {
				
				Object selected = selection.getSource();
				int selectedIndex = buttonTracker.indexOf(selected);
				orderTransfer = orderTracker.get(selectedIndex);				
				
				lblCurrentOrderTodayAppointments.setText(String.valueOf(orderTransfer.getOrderID()));
				lblNamelabel.setText(orderTransfer.getPatient().getLastName() + ", " + orderTransfer.getPatient().getFirstName() );
				textAreaTodayAppointment.setText(orderTransfer.getImagingOrder());
				lblInsertADate.setText(String.valueOf(orderTransfer.getApptDay()));
				

			}//end Action
		};//end radioListener
		
		ButtonGroup radioButtonGroup = new ButtonGroup();
		
		int yValue = 40;
		int xValue = 594;
		
		ArrayList<Order> allOrders = ViewAppointments();
		
		
		//Iterates through Arraylist looking for current date, will iterate through database Order table in final version 
		for(int i = 0; i < allOrders.size(); i++) {				
				
				if(allOrders.get(i).getPatientCheckedIn() == false) {
					JRadioButton orderRdButton = new JRadioButton("Order " + ": " + allOrders.get(i).getOrderID());
					orderRdButton.setBounds(xValue, yValue, 140, 14);
					todayAppointmentPanel.add(orderRdButton);
					orderRdButton.addActionListener(radioListener);
					radioButtonGroup.add(orderRdButton);
				
					buttonTracker.add(orderRdButton);
					orderTracker.add(allOrders.get(i));
				
					yValue += 20;

			}//end if
		}//end for loop

		subActionPanel.add(todayAppointmentPanel) ;
		subActionPanel.revalidate();
		subActionPanel.repaint();
	}//end viewTodayAppts
	
	public void ViewUnScheduledAppts() throws SQLException {
		
		subActionPanel.removeAll() ;
		subActionPanel.add(actionPanel);
		
				// This panel creates the view of all the unscheduled orders 
				// To-Do have a way for the receptionist to make an appointment 
				// add GUI components for that as well 
		
				Panel unscheduledOrdersPanel = new Panel();
				unscheduledOrdersPanel.setBounds(255, 33, 913, 420);
				unscheduledOrdersPanel.setLayout(null);
				
				JLabel lblselectedOrderUnscheduled = new JLabel();
				lblselectedOrderUnscheduled.setBounds(100, 10, 122, 14);
				unscheduledOrdersPanel.add(lblselectedOrderUnscheduled);
				
				JLabel lblSelectedPatientNameUnscheduled = new JLabel("Patient: ");
				lblSelectedPatientNameUnscheduled.setBounds(20, 30, 47, 14);
				unscheduledOrdersPanel.add(lblSelectedPatientNameUnscheduled);
				lblSelectedPatientNameUnscheduled.setVisible(false);
				
				JLabel lblUnscheduledOrders = new JLabel("Unscheduled Orders");
				lblUnscheduledOrders.setFont(new Font("Tahoma", Font.PLAIN, 16));
				lblUnscheduledOrders.setBounds(566, 10, 190, 18);
				unscheduledOrdersPanel.add(lblUnscheduledOrders);
				
				JLabel lblSelectedPatientName = new JLabel();
				lblSelectedPatientName.setBounds(75, 30, 270, 14);
				unscheduledOrdersPanel.add(lblSelectedPatientName);
				
				JLabel lblEnterADate = new JLabel("Schedule Appointment Date:");
				lblEnterADate.setBounds(20, 60, 270, 25);
				unscheduledOrdersPanel.add(lblEnterADate);
				
				JTextField dateField = new JTextField();
				dateField.setBounds(20, 80, 270, 25);
				unscheduledOrdersPanel.add(dateField);
				
				JLabel lblEnterATime = new JLabel("Schedule Appointment Time:");
				lblEnterATime.setBounds(20, 105, 270, 25);
				unscheduledOrdersPanel.add(lblEnterATime);
				
				JTextField timeField = new JTextField();
				timeField.setBounds(20, 125, 270, 25);
				unscheduledOrdersPanel.add(timeField);
				
				//Special listener for radio buttons
				ActionListener radioListener = new ActionListener() {
					
					public void actionPerformed(ActionEvent selection) {
						
						Object selected = selection.getSource();
						int selectedIndex = buttonTracker.indexOf(selected);
						orderTransfer = orderTracker.get(selectedIndex);
						
						lblselectedOrderUnscheduled.setText("Order ID:  " + orderTransfer.getOrderID());
						lblSelectedPatientName.setText(orderTransfer.getPatient().getLastName() + ", " + orderTransfer.getPatient().getFirstName() );
						lblSelectedPatientNameUnscheduled.setVisible(true);
						
					}//end Action
				};//end radioListener
				
				ButtonGroup radioButtonGroup = new ButtonGroup();
				
				int yValue = 40;
				int xValue = 594;
				
				ArrayList<Order> unscheduledOrders = ViewUnscheduledAppointments();
				
				//Iterates through Arraylist looking for current date, will iterate through database Order table in final version 
				for(int i = 0; i < unscheduledOrders.size(); i++) {
					
					if(unscheduledOrders.get(i).getOrderStatus().equals("Unscheduled")) {
						JRadioButton orderRdButton = new JRadioButton("Order " + ": " + unscheduledOrders.get(i).getOrderID());
						orderRdButton.setBounds(xValue, yValue, 140, 14);
						unscheduledOrdersPanel.add(orderRdButton);
						orderRdButton.addActionListener(radioListener);
						radioButtonGroup.add(orderRdButton);
						
						buttonTracker.add(orderRdButton);
						orderTracker.add(unscheduledOrders.get(i));
						
						yValue += 20;
					}//end if
					
				}//end for loop
				
				JButton btnNewAppointment = new JButton("Schedule Appointment");
				btnNewAppointment.setBounds(60, 200, 170, 23);
				unscheduledOrdersPanel.add(btnNewAppointment);
				
				//special listener for schedule button
				ActionListener appointmentListener = new ActionListener() {
					
					public void actionPerformed(ActionEvent click){
						if (dateField.getText() != "" && timeField.getText() != "" && orderTransfer != null) {
							
							String scheduled = dateField.getText();
							
							//SQL HERE**********************************************
							
									orderTransfer.setApptDay(scheduled);
									orderTransfer.setApptTime(timeField.getText());
									orderTransfer.setApptScheduled(true);
									
							try {
								ScheduleOrder(orderTransfer);
							} catch (SQLException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							
							try {
								ViewUnScheduledAppts();
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}//end if
						
					}//end Action
				};//end appointmentListener
				
				btnNewAppointment.addActionListener(appointmentListener);
				
		subActionPanel.add(unscheduledOrdersPanel);
	}//end viewUnscheduledAppts
	
	public void ChecKIn(String appt, String apptRoom) {
		
		
	}//end CheckIn
	
	//Opens view for Search Patient and Search Order
	public void Search() {
		
		subActionPanel.removeAll();
		subActionPanel.add(actionPanel);
		
		// the first panel that is added when the search patient button is clicked 
				// The receptionist can use this interface to look up patients that come in 
				// ideally searching for a patient shows all scheduled and unscheduled orders 
				
				Panel SearchPatientPanel = new Panel();
				SearchPatientPanel.setBounds(255, 33, 913, 420);
				SearchPatientPanel.setLayout(null);
				
				txtsearchPatientFirstName = new JTextField();
				txtsearchPatientFirstName.setBounds(106, 74, 164, 20);
				SearchPatientPanel.add(txtsearchPatientFirstName);
				txtsearchPatientFirstName.setColumns(10);
				
				Label lblsearchPatientFirstName = new Label("First Name ");
				lblsearchPatientFirstName.setBounds(28, 72, 62, 22);
				SearchPatientPanel.add(lblsearchPatientFirstName);
				
				txtsearchPatientLastName = new JTextField();
				txtsearchPatientLastName.setBounds(106, 115, 164, 20);
				SearchPatientPanel.add(txtsearchPatientLastName);
				txtsearchPatientLastName.setColumns(10);
				
				Label lblsearchPatientLastName = new Label("Last Name");
				lblsearchPatientLastName.setBounds(28, 113, 74, 22);
				SearchPatientPanel.add(lblsearchPatientLastName);
				
				txtsearchPatientDoB = new JTextField();
				txtsearchPatientDoB.setBounds(106, 160, 164, 20);
				SearchPatientPanel.add(txtsearchPatientDoB);
				txtsearchPatientDoB.setColumns(10);
				
				Label lblsearchPatientDoB = new Label("Date of Birth");
				lblsearchPatientDoB.setBounds(28, 158, 74, 22);
				SearchPatientPanel.add(lblsearchPatientDoB);
				
				txtsearchPatientEmail = new JTextField();
				txtsearchPatientEmail.setBounds(106, 210, 164, 20);
				SearchPatientPanel.add(txtsearchPatientEmail);
				txtsearchPatientEmail.setColumns(10);
				
				Label lblsearchPatientEmail = new Label("E-mail");
				lblsearchPatientEmail.setBounds(28, 208, 74, 22);
				SearchPatientPanel.add(lblsearchPatientEmail);
				
				JButton searchPatientSearchButton = new JButton("Search Patient");
				searchPatientSearchButton.setBounds(70, 250, 164, 23);
				SearchPatientPanel.add(searchPatientSearchButton);
				
				JLabel lblviewOrderInfo = new JLabel("Order Information");
				lblviewOrderInfo.setBounds(70, 290, 134, 14);
				SearchPatientPanel.add(lblviewOrderInfo);
				
				JLabel lblviewOrderOrderIdNumber = new JLabel("Order ID Number");
				lblviewOrderOrderIdNumber.setBounds(28, 335, 101, 14);
				SearchPatientPanel.add(lblviewOrderOrderIdNumber);
				
				txtviewOrderOrderId = new JTextField();
				txtviewOrderOrderId.setBounds(130, 335, 129, 20);
				SearchPatientPanel.add(txtviewOrderOrderId);
				txtviewOrderOrderId.setColumns(10);
				
				JButton searchOrderSearchButton = new JButton("Search Order");
				searchOrderSearchButton.setBounds(70, 380, 164, 23);
				SearchPatientPanel.add(searchOrderSearchButton);
				
				JLabel searchPatientinstructionsLabel = new JLabel("Patient Information");
				searchPatientinstructionsLabel.setBounds(70, 26, 134, 14);
				SearchPatientPanel.add(searchPatientinstructionsLabel);
				
				JLabel searchPatientsResultsLabel = new JLabel("Search Results");
				searchPatientsResultsLabel.setBounds(596, 26, 89, 14);
				SearchPatientPanel.add(searchPatientsResultsLabel);
				
				JLabel fNameResult = new JLabel();
				fNameResult.setBounds(500, 50, 165, 20);
				SearchPatientPanel.add(fNameResult);
				fNameResult.setVisible(false);
				
				JLabel lNameResult = new JLabel();
				lNameResult.setBounds(700, 50, 165, 20);
				SearchPatientPanel.add(lNameResult);
				lNameResult.setVisible(false);
				
				JLabel emailResult = new JLabel();
				emailResult.setBounds(500, 100, 165, 20);
				SearchPatientPanel.add(emailResult);
				emailResult.setVisible(false);
				
				JLabel phoneNumResult = new JLabel();
				phoneNumResult.setBounds(700, 100, 165, 20);
				SearchPatientPanel.add(phoneNumResult);
				phoneNumResult.setVisible(false);
				
				JLabel allergyResult = new JLabel();//Might need changed too, text area
				allergyResult.setBounds(500, 150, 365, 20);
				SearchPatientPanel.add(allergyResult);
				allergyResult.setVisible(false);
				
				JLabel notesResult = new JLabel();//Change this to a text area or something
				notesResult.setBounds(500, 200, 365, 20);
				SearchPatientPanel.add(notesResult);
				notesResult.setVisible(false);
				
				JLabel orderStatusResult = new JLabel();
				orderStatusResult.setBounds(500, 100, 165, 20);
				SearchPatientPanel.add(orderStatusResult);
				orderStatusResult.setVisible(false);
				
				JLabel modalityResult = new JLabel();
				modalityResult.setBounds(700, 100, 165, 20);
				SearchPatientPanel.add(modalityResult);
				modalityResult.setVisible(false);
				
				JLabel apptScheduledResult = new JLabel();
				apptScheduledResult.setBounds(500, 150, 250, 20);
				SearchPatientPanel.add(apptScheduledResult);
				apptScheduledResult.setVisible(false);
				
				JLabel imagingOrderResult = new JLabel();
				imagingOrderResult.setBounds(500, 200, 165, 20);
				SearchPatientPanel.add(imagingOrderResult);
				imagingOrderResult.setVisible(false);
				
				JLabel imagingOrderStatusResult = new JLabel();
				imagingOrderStatusResult.setBounds(700, 200, 165, 20);
				SearchPatientPanel.add(imagingOrderStatusResult);
				imagingOrderStatusResult.setVisible(false);

				JLabel visitReasonResult = new JLabel();//Change this to a text area or something
				visitReasonResult.setBounds(500, 250, 365, 20);
				SearchPatientPanel.add(visitReasonResult);
				visitReasonResult.setVisible(false);
				
				ActionListener searchListener = new ActionListener() {
					
					public void actionPerformed(ActionEvent click) {
						Boolean resultFound = false;
						
						if (click.getSource() == searchPatientSearchButton) {
							//Take in search parameters FirstName, LastName, EMail, DoB use SQL to make comparisons until proper patient found
							//Return all patient Info from matching patient, phone number, notes, allergies
							String fName = txtsearchPatientFirstName.getText();
							String lName = txtsearchPatientLastName.getText();
							
							//Remove results from any previous  order searches
							orderStatusResult.setVisible(false);
							visitReasonResult.setVisible(false);
							modalityResult.setVisible(false);
							apptScheduledResult.setVisible(false);
							imagingOrderResult.setVisible(false);
							imagingOrderStatusResult.setVisible(false);
							
							//SQL HERE******************************
							try {
								Patient foundPatient = ReturnPatient(fName, lName);							
							
							if(foundPatient  != null) {
									fNameResult.setText("First Name:  " + fName);
									fNameResult.setVisible(true);
									lNameResult.setText("Last Name:  " + lName);	
									lNameResult.setVisible(true);
									emailResult.setText("Email:  " + foundPatient.getEmail());	
									emailResult.setVisible(true);
									phoneNumResult.setText("Phone #:  " + foundPatient.getPhoneNumber());
									phoneNumResult.setVisible(true);
									allergyResult.setText("Allergies:  " + foundPatient.getAllergy());	
									allergyResult.setVisible(true);
									notesResult.setText("Notes:  " + foundPatient.getNotes());
									notesResult.setVisible(true);
									
									resultFound = true;
									
									SearchPatientPanel.revalidate();
									SearchPatientPanel.repaint();
									
								}//end if
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
								
							if (resultFound == false) {
								fNameResult.setText("");
								lNameResult.setText("");									
								emailResult.setText("");									
								phoneNumResult.setText("");									
								allergyResult.setText("");									
								notesResult.setText("No results, please check input");
								notesResult.setVisible(true);
								
								SearchPatientPanel.revalidate();
								SearchPatientPanel.repaint();
							}//end if
						
						}//end source if
						
						else if (click.getSource() == searchOrderSearchButton) {
							
							//Remove results from any previous patient searches
							emailResult.setVisible(false);
							phoneNumResult.setVisible(false);
							allergyResult.setVisible(false);
							notesResult.setVisible(false);
							
							//MORE SQL HERE*************************************
							try {
								Order foundOrder = ReturnOrder(Integer.valueOf(txtviewOrderOrderId.getText()));
											
									fNameResult.setText("First Name:  " + foundOrder.getPatient().getFirstName());	
									fNameResult.setVisible(true);
									lNameResult.setText("Last Name:  " + foundOrder.getPatient().getLastName());		
									lNameResult.setVisible(true);
									orderStatusResult.setText("Order Status:  " + foundOrder.getOrderStatus());	
									orderStatusResult.setVisible(true);
									
									if (!foundOrder.getOrderStatus().equalsIgnoreCase("Unscheduled")) {
										apptScheduledResult.setText("Appointment:  " + String.valueOf(foundOrder.getApptDay()));
										apptScheduledResult.setVisible(true);
									}//end if
									else {
										apptScheduledResult.setText("Appointment:  " + "No appointment scheduled");
										apptScheduledResult.setVisible(true);
									}//end else
										
									//modalityResult.setText("Modality:  " + foundOrder.getModality()); Broken
									//modalityResult.setVisible(true); Broken
									
									imagingOrderResult.setText("Imaging Ordered:  " + foundOrder.getImagingOrder());
									imagingOrderResult.setVisible(true);
									imagingOrderStatusResult.setText("Imaging Status:  " + foundOrder.getImagingOrderStatus());
									imagingOrderStatusResult.setVisible(true);
									visitReasonResult.setText("Reason for Visit:  " + foundOrder.getVisitReason());
									visitReasonResult.setVisible(true);
									
									resultFound = true;
									
									SearchPatientPanel.revalidate();
									SearchPatientPanel.repaint();
								
							} catch (NumberFormatException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								fNameResult.setText("");
								lNameResult.setText("");									
								emailResult.setText("");									
								phoneNumResult.setText("");									
								allergyResult.setText("");									
								notesResult.setText("No results, please check input");
								notesResult.setVisible(true);
								
								SearchPatientPanel.revalidate();
								SearchPatientPanel.repaint();
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								fNameResult.setText("");
								lNameResult.setText("");									
								emailResult.setText("");									
								phoneNumResult.setText("");									
								allergyResult.setText("");									
								notesResult.setText("No results, please check input");
								notesResult.setVisible(true);
								
								SearchPatientPanel.revalidate();
								SearchPatientPanel.repaint();
							}
								
							
							
							if (resultFound == false) {
								fNameResult.setText("");
								lNameResult.setText("");									
								emailResult.setText("");									
								phoneNumResult.setText("");									
								allergyResult.setText("");									
								notesResult.setText("No results, please check input");
								notesResult.setVisible(true);
								
								SearchPatientPanel.revalidate();
								SearchPatientPanel.repaint();
							}//end if
							
						}//end source else if
						
					}//end Action
				};//end searchListner
				
				searchPatientSearchButton.addActionListener(searchListener);
				searchOrderSearchButton.addActionListener(searchListener);
				
		subActionPanel.add(SearchPatientPanel);	
	}//end SearchPatient
	
	public void CheckIn (String patientFirstName, String patientLastName) throws SQLException {
		
		// panel for checking in a patient. use choice boxes to select an open technician, modality / equipment / room
				// Ideally this panel is called when an appointment is selected from "Todays Appointment" and uses the order and patient 
				// object to send to this panel to be sent to the proper equipment IE checking in. 
				
				JPanel CheckinPanel = new JPanel();
				CheckinPanel.setBounds(0, 33, 913, 420);
				CheckinPanel.setLayout(null);
				
				JLabel lblCheckInSelectedlabel = new JLabel("OrderID: " + orderTransfer.getOrderID());
				lblCheckInSelectedlabel.setBounds(100, 23, 122, 14);
				CheckinPanel.add(lblCheckInSelectedlabel);
				
				JLabel lblCheckInPatientDisp = new JLabel("Patient:");
				lblCheckInPatientDisp.setBounds(10, 47, 47, 14);
				CheckinPanel.add(lblCheckInPatientDisp);
				
				JLabel lblAppoinmnetCheckInLabel = new JLabel("Appointment: ");
				lblAppoinmnetCheckInLabel.setBounds(10, 75, 79, 14);
				CheckinPanel.add(lblAppoinmnetCheckInLabel);
				
				JLabel lblPatientNameCheckIn = new JLabel(orderTransfer.getPatient().getLastName() + ", " + orderTransfer.getPatient().getFirstName());
				lblPatientNameCheckIn.setBounds(60, 47, 275, 14);
				CheckinPanel.add(lblPatientNameCheckIn);
				
				JLabel lblAppointmentDateCheckIn = new JLabel(String.valueOf(orderTransfer.getApptDay()) + " " + orderTransfer.getApptTime());
				lblAppointmentDateCheckIn.setBounds(90, 75, 260, 14);
				CheckinPanel.add(lblAppointmentDateCheckIn);
				
				JLabel lblImagingRequiredCheckIn = new JLabel("-- Imaging Required --");
				lblImagingRequiredCheckIn.setBounds(100, 100, 122, 14);
				CheckinPanel.add(lblImagingRequiredCheckIn);
				
				JTextArea textAreaCheckIn = new JTextArea();
				textAreaCheckIn.setFont(new Font("Tahoma", Font.PLAIN, 13));
				textAreaCheckIn.setLineWrap(true);
				textAreaCheckIn.setEditable(false);
				textAreaCheckIn.setBounds(10, 125, 315, 181);
				textAreaCheckIn.setText(orderTransfer.getImagingOrder());
				CheckinPanel.add(textAreaCheckIn);
				
				JLabel lblCheckIn = new JLabel("-- Check In Information --");
				lblCheckIn.setBounds(566, 23, 155, 14);
				CheckinPanel.add(lblCheckIn);
				
				JLabel lblSelectOpenRoom = new JLabel("Select An Open Room: ");
				lblSelectOpenRoom.setBounds(430, 75, 135, 14);
				CheckinPanel.add(lblSelectOpenRoom);
				
				Choice choiceAvailableModality = new Choice();				
				choiceAvailableModality.setBounds(566, 75, 250, 20);
				CheckinPanel.add(choiceAvailableModality);
				
				
				//Fill Choice menu
				//MORE SQL HERE****************************************************
				ArrayList<Modality> foundMods = ReturnMod();
				
				for(Modality mod: foundMods) {
				choiceAvailableModality.add(mod.getModalityName());
				}//end for each 
				
				JLabel lblNewLabel = new JLabel("Select Available Team:");
				lblNewLabel.setBounds(430, 115, 135, 14);
				CheckinPanel.add(lblNewLabel);
				
				Choice choiceAvailableTeam = new Choice();
				choiceAvailableTeam.setBounds(566, 115, 250, 20);
				CheckinPanel.add(choiceAvailableTeam);
				
				//Fill choice menu
				//MORE SQL HERE ******************************
				ArrayList<Team> foundTeams = ReturnTeam();
				
				for(Team team: foundTeams) {
				choiceAvailableTeam.add(team.getTeamName());
				}//end for each 
				
				JLabel lblRoomConfirmationdonotchange = new JLabel("Selected Room:");
				lblRoomConfirmationdonotchange.setBounds(430, 200, 135, 14);
				CheckinPanel.add(lblRoomConfirmationdonotchange);
				
				JLabel lblTeamConfirmationdonotchange = new JLabel("Selected Team:");
				lblTeamConfirmationdonotchange.setBounds(430, 240, 135, 14);
				CheckinPanel.add(lblTeamConfirmationdonotchange);
				
				JLabel lblRoomConfirmation = new JLabel();
				lblRoomConfirmation.setBounds(566, 200, 250, 14);
				CheckinPanel.add(lblRoomConfirmation);
				
				JLabel lblTeamConfirmation = new JLabel();
				lblTeamConfirmation.setBounds(566, 240, 250, 14);
				CheckinPanel.add(lblTeamConfirmation);
				
				
				
				ItemListener choiceListener = new ItemListener() {
					
					public void itemStateChanged(ItemEvent choice) {
						
						if (choice.getSource() == choiceAvailableTeam) {
							
							lblTeamConfirmation.setText(choiceAvailableTeam.getSelectedItem());
						}//end if
						
						if (choice.getSource() == choiceAvailableModality) {
							
							lblRoomConfirmation.setText(choiceAvailableModality.getSelectedItem());
						}//end if
					}//end ItemEvent
				};//end itemListener
				
				choiceAvailableTeam.addItemListener(choiceListener);
				choiceAvailableModality.addItemListener(choiceListener);
				
				JButton btnConfirmCheckIn = new JButton("Check-in Patient");
				
				btnConfirmCheckIn.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						// order status = checked-in 
						// SQL ammend modality and team to the selected order 

						orderTransfer.setPatientCheckedIn(true);
						orderTransfer.setApptTeam(choiceAvailableTeam.getSelectedItem());
						orderTransfer.setOrderStatus("Checked-In");
						
						//Iterate through foundTeams to match up with choice, then grab ID to pass to order
						for(int i = 0; i < foundTeams.size(); i++) {
							int ID;
							if(foundTeams.get(i).getTeamName().equals(choiceAvailableTeam.getSelectedItem())) {
								ID = foundTeams.get(i).getTeamID();
								orderTransfer.setTeamID(ID);
								orderTransfer.setTeam(foundTeams.get(i));
								break;
							}//end if
						}//end for
						
						//Iterate through foundMods to match up with choice, then pass to order
						for(int i = 0; i < foundMods.size(); i++) {
							int ID;
							if(foundMods.get(i).getModalityName().equals(choiceAvailableModality.getSelectedItem())) {
								orderTransfer.setModality(foundMods.get(i));
								break;
							}//end if
						}//end for
						
						
						try {
							CheckInOrder(orderTransfer);
						} catch (SQLException e2) {
							// TODO Auto-generated catch block
							e2.printStackTrace();
						}
						
						// consider a success pop-up frame here 
						try {
							ViewAppts();
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						
					}
				});
				btnConfirmCheckIn.setBounds(566, 285, 130, 23);
				CheckinPanel.add(btnConfirmCheckIn);
				todayAppointmentPanel.add(CheckinPanel);
				
				todayAppointmentPanel.revalidate();
				todayAppointmentPanel.repaint();
		
	}//end CheckIn
	
	public static ArrayList<Order> ViewAppointments() throws SQLException {
		/*Searches through orders for current day's appointments
		 * Puts Orders for selected date in arraylist for UIManager to access and display
		 * UIManager displays orderID, appt time, room number, and imaging ordered
		 * User can select appt and press select button to display patient information
		 */		
		
		//Adding parameters for testing
		ArrayList<Order> todaysOrders = new ArrayList<>();
		Patient currPatient ;
		int index = 0;
		Connection conn = getConnection();
		System.out.println(LocalDate.now()) ;
		PreparedStatement statement = conn.prepareStatement("Select * from imagingorder Where orderStatus = ? AND appointment = ? ; " ) ;
		statement.setString(1, "Scheduled");
		statement.setString(2, LocalDate.now().toString());
		
		ResultSet result = statement.executeQuery() ;
		
		while(result.next()) {
			currPatient = ReturnPatient(result.getInt("patientID") ) ;
			todaysOrders.add(index, new Order(result.getInt("orderID")));
			todaysOrders.get(index).setApptDay(result.getString("appointment"));
			todaysOrders.get(index).setImagingOrder(result.getString("imagingNeeded"));
			todaysOrders.get(index).setVisitReason(result.getString("visitReason"));
			todaysOrders.get(index).setOrderStatus(result.getString("orderStatus"));
			todaysOrders.get(index).setApptTime(result.getString("appointment"));
			
			todaysOrders.get(index).setPatient(currPatient);
			
			index++ ;
		}
		
		System.out.println(" Today's appointments found successfully");
		return todaysOrders ;
	}
	
	public static ArrayList<Order> ViewUnscheduledAppointments() throws SQLException {
		/*Searches through orders for unscheduled appointments
		 * Puts Orders in arraylist for UIManager to access and display
		 * UIManager displays orderID, appt time, room number, and imaging ordered
		 * User can select appt and press select button to display patient information
		 */		
		
		
		
		//Adding parameters for testing
		ArrayList<Order> unscheduledOrders = new ArrayList<>();
		Patient currPatient ;
		int index = 0;
		Connection conn = getConnection();
		
		PreparedStatement statement = conn.prepareStatement("Select * from imagingorder Where appointment is null ; " ) ;
		
		
		ResultSet result = statement.executeQuery() ;
		
		while(result.next()) {
			currPatient = ReturnPatient(result.getInt("patientId")) ;
			unscheduledOrders.add(index, new Order(result.getInt("OrderId")));
			unscheduledOrders.get(index).setPatient(currPatient);
			unscheduledOrders.get(index).setApptTime(result.getString("appointment"));
			unscheduledOrders.get(index).setImagingOrder(result.getString("imagingNeeded"));
			unscheduledOrders.get(index).setVisitReason(result.getString("visitReason"));
			unscheduledOrders.get(index).setOrderStatus(result.getString("orderStatus"));
			unscheduledOrders.get(index).setApptTime(result.getString("appointment"));
			
			unscheduledOrders.get(index).setPatient(currPatient);
			
			index++ ;
		}
		
		System.out.println(" Unscheduled appointments found successfully");
		System.out.println(unscheduledOrders.size());
		return unscheduledOrders ;
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
	
	public static Patient ReturnPatient(String fname, String lname) throws SQLException {
		//Finds and returns patient based on name*************************
		
		 ArrayList<Patient> Patients = new ArrayList<>() ;
		 Patient currPatient[] = new Patient[1] ;
		 int index = 0 ;
		 
		 Connection conn = getConnection() ;
		 PreparedStatement statement = conn.prepareStatement("Select * from patient Where firstName  = ? AND lastName = ?  ;") ;
		 
		statement.setString(1, fname );
		statement.setString(2,  lname);
		
		ResultSet result = statement.executeQuery() ;
		
		while(result.next()) {
			 currPatient[0] = new Patient(result.getString("firstName"), result.getString("lastName")) ;
			 currPatient[0].setPatientId(result.getInt("patientID"));
			 currPatient[0].setEmail(result.getString("email"));
			 currPatient[0].setPhoneNumber(result.getString("phoneNumber"));
			 currPatient[0].setAllergyLatex(result.getBoolean("allergyLatex"));
			 currPatient[0].setAllergyMridye(result.getBoolean("allergyMridye"));
			 currPatient[0].setAllergyXraydye(result.getBoolean("allergyXrayDye"));
			 currPatient[0].setNotes(result.getString("notes"));
			 currPatient[0].setDateOfBirth(result.getString("dateOfBirth"));
			
	}
		 
		
		result.close();
		
		return currPatient[0] ;
	}
	
	public static void CheckInOrder(Order order) throws SQLException {
		
		//Updates selected order with checked in status, modality, and appt team *******
		
		String mod = order.getModality().getModalityID();
		int team = order.getTeamID();
		int ID = order.getOrderID();
		
		Connection conn = getConnection() ;

		PreparedStatement statement = conn.prepareStatement("Update imagingorder Set modalityID = ?, teamID = ?, orderStatus = ?   Where orderID  = ?  ;") ;
		 
		statement.setString(1, mod );
		statement.setInt(2, team);
		statement.setString(3, "Checked-In");
		statement.setInt(4, ID);

		statement.executeUpdate() ;
		 

	}
	
public static void ScheduleOrder(Order order) throws SQLException {
		
	//Updates selected order with appt date and time and status**

    String appt = order.getApptDay() + "";
    String time = order.getApptTime();
    int ID = order.getOrderID();

     Connection conn = getConnection() ;
     PreparedStatement statement = conn.prepareStatement("Update imagingorder Set appointment = ?,  apptTime = ? , orderStatus = ?   Where orderID  = ?  ;") ;

    statement.setString(1, appt );
    statement.setString(2, time );
    statement.setString(3, "Scheduled");
    statement.setInt(4, ID);

     statement.executeUpdate() ;


	}
	
	public static Order ReturnOrder(int ID) throws SQLException {
		
		Order currOrder = new Order(ID);
		 Connection conn = getConnection() ;
		 PreparedStatement statement = conn.prepareStatement("Select * from imagingorder Where orderID  = ?  ;") ;
		 
		statement.setInt(1, ID );
		
		ResultSet result = statement.executeQuery() ;
		
		while(result.next()) {
		
		currOrder.setPatientID(result.getInt("patientID"));
		currOrder.setOrderStatus(result.getString("orderStatus"));
		currOrder.setApptDay(result.getString("appointment"));
		currOrder.setVisitReason(result.getString("visitReason"));
		currOrder.setImagingOrder(result.getString("imagingNeeded"));
		
		Patient patientTransfer = ReturnPatient(currOrder.getPatientID());
		
		currOrder.setPatient(patientTransfer);
		}//end while
		
		result.close();
		
		return currOrder;
	}
	
	public static ArrayList<Team> ReturnTeam() throws SQLException {
		
		 ArrayList<Team> currTeam = new ArrayList<>();
		 int index = 0 ;
		 
		 Connection conn = getConnection() ;
		 PreparedStatement statement = conn.prepareStatement("Select * from team ;") ;
		
		ResultSet result = statement.executeQuery() ;
		
		while(result.next()) {
			currTeam.add(index, new Team(result.getInt("teamID"), result.getString( "teamName"))) ;
			index++;
			
	}
		 
		result.close();
		
		return currTeam ;
	}
	
	public static ArrayList<Modality> ReturnMod() throws SQLException {
		
		 ArrayList<Modality> currMod = new ArrayList<>();
		 int index = 0 ;
		 
		 Connection conn = getConnection() ;
		 PreparedStatement statement = conn.prepareStatement("Select * from modality ;") ;
		
		ResultSet result = statement.executeQuery() ;
		
		while(result.next()) {
			currMod.add(index, new Modality(result.getString("modalityID")));
			currMod.get(index).setModalityName(result.getString("modalityName"));
			index++;		
	}
		 
		result.close();
		
		return currMod ;
	}

}
