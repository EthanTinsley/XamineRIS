package XamineRIS;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.EventQueue;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
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

//Nick's changes, added static frame for added logout functionality, added User parameter for displaying username, *******
public class UIReferringDoctor extends JFrame {

	private JPanel contentPane;
	private JTextField txtsearchPatientFirstName;
	private JTextField txtsearchPatientLastName;
	private JTextField txtsearchPatientDoB;
	private JTextField txtsearchPatientEmail;
	private JTextField txtviewOrderOrderId;
	private JTextField txtviewOrderFirstName;
	private JTextField txtviewOrderDateOfBirth;
	private JTextField txtviewOrderEmail;
	private JTextField txtnewPatientEMail;
	private JTextField txtnewPatientDoB;
	private JTextField txtnewPatientLastName;
	private JTextField txtnewPatientGender;
	private JTextField txtnewPatientPhoneNumber;
	private JTextField txtnewOrderLastName;
	private JTextField txtnewOrderDoB;
	private JTextField txtnewOrderEMail;
	private JTextField txtnewOrderFirstName;
	private static UIReferringDoctor frame;
	private User currentUser;
	private Patient currPatient ;
	private Patient[] Patients = new Patient[5] ;
	
	protected ArrayList<JRadioButton> buttonTracker = new ArrayList<>();
	protected ArrayList<Order> orderTracker = new ArrayList<>();
	protected ArrayList<Order> orderList = new ArrayList<>();
	
	private Order orderTransfer;
	
	private ButtonGroup radioButtonGroup = new ButtonGroup();
	private ActionListener radioListener;
	
	private int currentIndex = 0;

	/**
	 * Launch the application.
	 */
	public static void main(User user) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame = new UIReferringDoctor(user);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public UIReferringDoctor(User user) {
		currentUser = user;
		Doctor jdoctor = new Doctor(user.getFirstName() , user.getLastName(), user.getEmail() , user.getUserName()) ;
		
		
		
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
		
		JLabel versionText = new JLabel("Doctor Portal");
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
		
		JPanel subActionPanel = new JPanel();
		subActionPanel.setLayout(null);
		subActionPanel.setBounds(10, 248, 239, 214);
		getContentPane().add(subActionPanel);
		
		JPanel actionPanel = new JPanel();
		subActionPanel.add(actionPanel);
		actionPanel.setBounds(10, 33, 239, 420);
		actionPanel.setLayout(null);
		
		
		//Begining of the Search Patient Panel
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
		
		Label lblsearchPatientLastName = new Label("Last Name*");
		lblsearchPatientLastName.setBounds(28, 113, 74, 22);
		SearchPatientPanel.add(lblsearchPatientLastName);
		
		txtsearchPatientDoB = new JTextField();
		txtsearchPatientDoB.setText("YYYY-MM-DD");
		txtsearchPatientDoB.setBounds(106, 160, 164, 20);
		SearchPatientPanel.add(txtsearchPatientDoB);
		txtsearchPatientDoB.setColumns(10);
		
		Label lblsearchPatientDoB = new Label("Date of Birth*");
		lblsearchPatientDoB.setBounds(28, 158, 74, 22);
		SearchPatientPanel.add(lblsearchPatientDoB);
		
		txtsearchPatientEmail = new JTextField();
		txtsearchPatientEmail.setBounds(106, 210, 164, 20);
		SearchPatientPanel.add(txtsearchPatientEmail);
		txtsearchPatientEmail.setColumns(10);
		
		Label lblsearchPatientEmail = new Label("E-mail");
		lblsearchPatientEmail.setBounds(28, 208, 74, 22);
		SearchPatientPanel.add(lblsearchPatientEmail);
		
		JLabel searchPatientinstructionsLabel = new JLabel("Patient Information");
		searchPatientinstructionsLabel.setBounds(125, 26, 134, 14);
		SearchPatientPanel.add(searchPatientinstructionsLabel);
		
		JLabel searchPatientsResultsLabel = new JLabel("Search Results");
		searchPatientsResultsLabel.setHorizontalAlignment(SwingConstants.CENTER);
		searchPatientsResultsLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		searchPatientsResultsLabel.setBounds(520, 0, 120, 14);
		SearchPatientPanel.add(searchPatientsResultsLabel);
		
		JLabel lblPatientId = new JLabel("Patient ID");
		lblPatientId.setHorizontalAlignment(SwingConstants.CENTER);
		lblPatientId.setBounds(277, 30, 70, 14);
		SearchPatientPanel.add(lblPatientId);
		
		JLabel PatientIDLabel01 = new JLabel("");
		PatientIDLabel01.setHorizontalAlignment(SwingConstants.CENTER);
		PatientIDLabel01.setBounds(289, 75, 48, 14);
		SearchPatientPanel.add(PatientIDLabel01);
		
		JLabel PatientIDLabel02 = new JLabel("");
		PatientIDLabel02.setHorizontalAlignment(SwingConstants.CENTER);
		PatientIDLabel02.setBounds(289, 155, 48, 14);
		SearchPatientPanel.add(PatientIDLabel02);
		
		JLabel PatientIDLabel03 = new JLabel("");
		PatientIDLabel03.setHorizontalAlignment(SwingConstants.CENTER);
		PatientIDLabel03.setBounds(289, 115, 48, 14);
		SearchPatientPanel.add(PatientIDLabel03);
		
		JLabel PatientIDLabel04 = new JLabel("");
		PatientIDLabel04.setHorizontalAlignment(SwingConstants.CENTER);
		PatientIDLabel04.setBounds(289, 195, 48, 14);
		SearchPatientPanel.add(PatientIDLabel04);
		
		JLabel PatientIDLabel05 = new JLabel("");
		PatientIDLabel05.setHorizontalAlignment(SwingConstants.CENTER);
		PatientIDLabel05.setBounds(289, 235, 48, 14);
		SearchPatientPanel.add(PatientIDLabel05);
		
		JLabel PatientFirstNameLabel01 = new JLabel("");
		PatientFirstNameLabel01.setHorizontalAlignment(SwingConstants.CENTER);
		PatientFirstNameLabel01.setBounds(347, 75, 108, 14);
		SearchPatientPanel.add(PatientFirstNameLabel01);
		
		JLabel PatientFirstNameLabel02 = new JLabel("");
		PatientFirstNameLabel02.setHorizontalAlignment(SwingConstants.CENTER);
		PatientFirstNameLabel02.setBounds(347, 115, 108, 14);
		SearchPatientPanel.add(PatientFirstNameLabel02);
		
		JLabel PatientFirstNameLabel03 = new JLabel("");
		PatientFirstNameLabel03.setHorizontalAlignment(SwingConstants.CENTER);
		PatientFirstNameLabel03.setBounds(347, 155, 108, 14);
		SearchPatientPanel.add(PatientFirstNameLabel03);
		
		JLabel PatientFirstNameLabel04 = new JLabel("");
		PatientFirstNameLabel04.setHorizontalAlignment(SwingConstants.CENTER);
		PatientFirstNameLabel04.setBounds(347, 195, 108, 14);
		SearchPatientPanel.add(PatientFirstNameLabel04);
		
		JLabel PatientFirstNameLabel05 = new JLabel("");
		PatientFirstNameLabel05.setHorizontalAlignment(SwingConstants.CENTER);
		PatientFirstNameLabel05.setBounds(347, 235, 108, 14);
		SearchPatientPanel.add(PatientFirstNameLabel05);
		
		JLabel PatientLastNameLabel01 = new JLabel("");
		PatientLastNameLabel01.setHorizontalAlignment(SwingConstants.CENTER);
		PatientLastNameLabel01.setBounds(465, 75, 108, 14);
		SearchPatientPanel.add(PatientLastNameLabel01);
		
		JLabel PatientLastNameLabel02 = new JLabel("");
		PatientLastNameLabel02.setHorizontalAlignment(SwingConstants.CENTER);
		PatientLastNameLabel02.setBounds(465, 115, 108, 14);
		SearchPatientPanel.add(PatientLastNameLabel02);
		
		JLabel PatientLastNameLabel03 = new JLabel("");
		PatientLastNameLabel03.setHorizontalAlignment(SwingConstants.CENTER);
		PatientLastNameLabel03.setBounds(465, 155, 108, 14);
		SearchPatientPanel.add(PatientLastNameLabel03);
		
		JLabel PatientLastNameLabel04 = new JLabel("");
		PatientLastNameLabel04.setHorizontalAlignment(SwingConstants.CENTER);
		PatientLastNameLabel04.setBounds(465, 195, 108, 14);
		SearchPatientPanel.add(PatientLastNameLabel04);
		
		JLabel PatientLastNameLabel05 = new JLabel("");
		PatientLastNameLabel05.setHorizontalAlignment(SwingConstants.CENTER);
		PatientLastNameLabel05.setBounds(465, 235, 108, 14);
		SearchPatientPanel.add(PatientLastNameLabel05);
		
		JLabel lblFirstName = new JLabel("First Name");
		lblFirstName.setHorizontalAlignment(SwingConstants.CENTER);
		lblFirstName.setBounds(347, 30, 108, 14);
		SearchPatientPanel.add(lblFirstName);
		
		JLabel lblLastName = new JLabel("Last Name");
		lblLastName.setHorizontalAlignment(SwingConstants.CENTER);
		lblLastName.setBounds(465, 30, 108, 14);
		SearchPatientPanel.add(lblLastName);
		
		JLabel lblDateOfBirth = new JLabel("Date of Birth");
		lblDateOfBirth.setHorizontalAlignment(SwingConstants.CENTER);
		lblDateOfBirth.setBounds(573, 30, 108, 14);
		SearchPatientPanel.add(lblDateOfBirth);
		
		JLabel PatientDateOfBirth01 = new JLabel("");
		PatientDateOfBirth01.setHorizontalAlignment(SwingConstants.CENTER);
		PatientDateOfBirth01.setBounds(573, 75, 108, 14);
		SearchPatientPanel.add(PatientDateOfBirth01);
		
		JLabel PatientDateOfBirth02 = new JLabel("");
		PatientDateOfBirth02.setHorizontalAlignment(SwingConstants.CENTER);
		PatientDateOfBirth02.setBounds(573, 115, 108, 14);
		SearchPatientPanel.add(PatientDateOfBirth02);
		
		JLabel PatientDateOfBirth03 = new JLabel("");
		PatientDateOfBirth03.setHorizontalAlignment(SwingConstants.CENTER);
		PatientDateOfBirth03.setBounds(573, 155, 108, 14);
		SearchPatientPanel.add(PatientDateOfBirth03);
		
		JLabel PatientDateOfBirth04 = new JLabel("");
		PatientDateOfBirth04.setHorizontalAlignment(SwingConstants.CENTER);
		PatientDateOfBirth04.setBounds(573, 195, 108, 14);
		SearchPatientPanel.add(PatientDateOfBirth04);
		
		JLabel PatientDateOfBirth05 = new JLabel("");
		PatientDateOfBirth05.setHorizontalAlignment(SwingConstants.CENTER);
		PatientDateOfBirth05.setBounds(573, 235, 108, 14);
		SearchPatientPanel.add(PatientDateOfBirth05);
		
		JLabel lblSearchPatientEmail = new JLabel("E-Mail");
		lblSearchPatientEmail.setHorizontalAlignment(SwingConstants.CENTER);
		lblSearchPatientEmail.setBounds(680, 30, 108, 14);
		SearchPatientPanel.add(lblSearchPatientEmail);
		
		JLabel PatientEmail01 = new JLabel("");
		PatientEmail01.setHorizontalAlignment(SwingConstants.CENTER);
		PatientEmail01.setBounds(680, 75, 108, 14);
		SearchPatientPanel.add(PatientEmail01);
		
		JLabel PatientEmail02 = new JLabel("");
		PatientEmail02.setHorizontalAlignment(SwingConstants.CENTER);
		PatientEmail02.setBounds(680, 115, 108, 14);
		SearchPatientPanel.add(PatientEmail02);
		
		JLabel PatientEmail03 = new JLabel("");
		PatientEmail03.setHorizontalAlignment(SwingConstants.CENTER);
		PatientEmail03.setBounds(680, 155, 108, 14);
		SearchPatientPanel.add(PatientEmail03);
		
		JLabel PatientEmail04 = new JLabel("");
		PatientEmail04.setHorizontalAlignment(SwingConstants.CENTER);
		PatientEmail04.setBounds(680, 195, 108, 14);
		SearchPatientPanel.add(PatientEmail04);
		
		JLabel PatientEmail05 = new JLabel("");
		PatientEmail05.setHorizontalAlignment(SwingConstants.CENTER);
		PatientEmail05.setBounds(680, 235, 108, 14);
		SearchPatientPanel.add(PatientEmail05);
		
		JLabel lblCurrentPatient = new JLabel("");
		lblCurrentPatient.setBounds(10, 351, 181, 14);
		actionPanel.add(lblCurrentPatient);
		
		//ViewResultsPanel
		Panel ViewResultsPanel = new Panel();
		ViewResultsPanel.setBounds(255, 33, 913, 420);
		ViewResultsPanel.setLayout(null);
		
		//detailsPanel
		Panel detailsPanel = new Panel();
		detailsPanel.setBounds(255, 33, 913, 420);
		detailsPanel.setLayout(null);
		
	
		// These buttons select a patient from the results category and make them the current patient to start new orders with
		JButton btnPatientId01 = new JButton("Select ");
		btnPatientId01.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				currPatient = Patients[0] ;
				lblCurrentPatient.setText(currPatient.getFirstName() + currPatient.getLastName() + currPatient.getDateOfBirth() ) ;
			}
		});
		
		
		JButton btnPatientId02 = new JButton("Select");
		btnPatientId02.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				currPatient = Patients[1] ;
				lblCurrentPatient.setText(currPatient.getFirstName() + currPatient.getLastName() + currPatient.getDateOfBirth() ) ;
			}
		});
		
		
		JButton btnPatientId03 = new JButton("Select");
		btnPatientId03.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				currPatient = Patients[2] ;
				lblCurrentPatient.setText(currPatient.getFirstName() + currPatient.getLastName() + currPatient.getDateOfBirth() ) ;
			}
		});
		
		JButton btnPatientId04 = new JButton("Select");
		btnPatientId04.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				currPatient = Patients[3] ;
				lblCurrentPatient.setText(currPatient.getFirstName() + currPatient.getLastName() + currPatient.getDateOfBirth() ) ;
			}
		});
		
		JButton btnPatientId05 = new JButton("Select");
		btnPatientId05.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				currPatient = Patients[4] ;
				lblCurrentPatient.setText(currPatient.getFirstName() + currPatient.getLastName() + currPatient.getDateOfBirth() ) ;
			
			}
		});
		
		// When user types in search parameters and clicks the "search" button we want to use this listener
		JButton searchPatientSearchButton = new JButton("Search");
		searchPatientSearchButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
	
				String PatientFirstName = txtsearchPatientFirstName.getText();
				String PatientLastName = txtsearchPatientLastName.getText() ;
				String PatientDoB = txtsearchPatientDoB.getText() ;
				String PatientEmail = txtsearchPatientEmail.getText() ;
				String IdNum = "" ;
				
				try {
					Patients = jdoctor.returnPatient(PatientFirstName, PatientLastName , PatientDoB ,PatientEmail ) ;
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				if (Patients[0] != null) {
					PatientIDLabel01.setText(IdNum + Patients[0].getPatientId());
					PatientFirstNameLabel01.setText(Patients[0].getFirstName());
					PatientLastNameLabel01.setText(Patients[0].getLastName());
					PatientDateOfBirth01.setText(Patients[0].getDateOfBirth()) ;
					PatientEmail01.setText(Patients[0].getEmail());
					
					btnPatientId01.setBounds(798, 75, 89, 20);
					SearchPatientPanel.add(btnPatientId01);
				}
				if (Patients[1] != null) {
					PatientIDLabel02.setText(IdNum + Patients[1].getPatientId());
					PatientFirstNameLabel02.setText(Patients[1].getFirstName());
					PatientLastNameLabel02.setText(Patients[1].getLastName());
					PatientDateOfBirth02.setText(Patients[1].getDateOfBirth()) ;
					PatientEmail02.setText(Patients[1].getEmail());
					
					btnPatientId02.setBounds(798, 111, 89, 20);
					SearchPatientPanel.add(btnPatientId02);
				}
				if (Patients[2] != null) {
					PatientIDLabel03.setText(IdNum + Patients[2].getPatientId());
					PatientFirstNameLabel03.setText(Patients[2].getFirstName());
					PatientLastNameLabel03.setText(Patients[2].getLastName());
					PatientDateOfBirth03.setText(Patients[2].getDateOfBirth()) ;
					PatientEmail03.setText(Patients[2].getEmail());
					
					btnPatientId03.setBounds(798, 151, 89, 20);
					SearchPatientPanel.add(btnPatientId03);
				}
				if (Patients[3] != null) {
					PatientIDLabel04.setText(IdNum + Patients[3].getPatientId());
					PatientFirstNameLabel04.setText(Patients[3].getFirstName());
					PatientLastNameLabel04.setText(Patients[3].getLastName());
					PatientDateOfBirth04.setText(Patients[3].getDateOfBirth()) ;
					PatientEmail04.setText(Patients[3].getEmail());
					
					btnPatientId04.setBounds(798, 191, 89, 20);
					SearchPatientPanel.add(btnPatientId04);
				}
				if (Patients[4] != null) {
					PatientIDLabel05.setText(IdNum + Patients[4].getPatientId());
					PatientFirstNameLabel05.setText(Patients[4].getFirstName());
					PatientLastNameLabel05.setText(Patients[4].getLastName());
					PatientDateOfBirth05.setText(Patients[4].getDateOfBirth()) ;
					PatientEmail05.setText(Patients[4].getEmail());
					
					btnPatientId05.setBounds(798, 231, 89, 20);
					SearchPatientPanel.add(btnPatientId05);
				}
				
			}
		});
		searchPatientSearchButton.setBounds(141, 269, 89, 23);
		SearchPatientPanel.add(searchPatientSearchButton);
		
		
		
		// Panel to view/search all orders associtated to a patient 
		//
		//
		//
		//
		Panel viewOpenOrdersPanel = new Panel();
		viewOpenOrdersPanel.setBounds(255, 33, 913, 420);
		viewOpenOrdersPanel.setLayout(null);
		
		JLabel lblviewOrderInfo = new JLabel("Order Information");
		lblviewOrderInfo.setBounds(93, 35, 148, 14);
		viewOpenOrdersPanel.add(lblviewOrderInfo);
		
		JLabel lblviewOrderOrderIdNumber = new JLabel("Order ID Number");
		lblviewOrderOrderIdNumber.setBounds(27, 70, 101, 14);
		viewOpenOrdersPanel.add(lblviewOrderOrderIdNumber);
		
		txtviewOrderOrderId = new JTextField();
		txtviewOrderOrderId.setBounds(138, 67, 129, 20);
		viewOpenOrdersPanel.add(txtviewOrderOrderId);
		txtviewOrderOrderId.setColumns(10);
		
		JLabel lblviewOrderPatient = new JLabel("Patient Information");
		lblviewOrderPatient.setBounds(93, 108, 148, 14);
		viewOpenOrdersPanel.add(lblviewOrderPatient);
		
		JLabel lblviewOrderFirstName = new JLabel("First Name");
		lblviewOrderFirstName.setBounds(27, 146, 75, 14);
		viewOpenOrdersPanel.add(lblviewOrderFirstName);
		
		txtviewOrderFirstName = new JTextField();
		txtviewOrderFirstName.setBounds(138, 143, 129, 20);
		viewOpenOrdersPanel.add(txtviewOrderFirstName);
		txtviewOrderFirstName.setColumns(10);
		
		JLabel lblviewOrderLastName = new JLabel("Last Name*");
		lblviewOrderLastName.setBounds(27, 190, 75, 24);
		viewOpenOrdersPanel.add(lblviewOrderLastName);
		
		TextField txtviewOrderLastName = new TextField();
		txtviewOrderLastName.setBounds(138, 192, 129, 22);
		viewOpenOrdersPanel.add(txtviewOrderLastName);
		
		JLabel lblviewOrderDateOfBirth = new JLabel("Date Of Birth*");
		lblviewOrderDateOfBirth.setBounds(27, 247, 87, 14);
		viewOpenOrdersPanel.add(lblviewOrderDateOfBirth);
		
		txtviewOrderDateOfBirth = new JTextField();
		txtviewOrderDateOfBirth.setText("YYYY-MM-DD");
		txtviewOrderDateOfBirth.setBounds(138, 244, 129, 20);
		viewOpenOrdersPanel.add(txtviewOrderDateOfBirth);
		txtviewOrderDateOfBirth.setColumns(10);
		
		txtviewOrderEmail = new JTextField();
		txtviewOrderEmail.setBounds(138, 304, 129, 20);
		viewOpenOrdersPanel.add(txtviewOrderEmail);
		txtviewOrderEmail.setColumns(10);
		
		JLabel lblviewOrderEMail = new JLabel("E mail");
		lblviewOrderEMail.setBounds(27, 307, 75, 14);
		viewOpenOrdersPanel.add(lblviewOrderEMail);
		
		JLabel searchOrderResultsLabel = new JLabel("Search Results");
		searchOrderResultsLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		searchOrderResultsLabel.setBounds(573, 0, 100, 20);
		viewOpenOrdersPanel.add(searchOrderResultsLabel);
		
		JLabel lblOrderPatientId = new JLabel("Patient ID");
		lblOrderPatientId.setHorizontalAlignment(SwingConstants.CENTER);
		lblOrderPatientId.setBounds(277, 30, 70, 14);
		viewOpenOrdersPanel.add(lblOrderPatientId);
		
		JLabel OrderPatientIDLabel01 = new JLabel("");
		OrderPatientIDLabel01.setHorizontalAlignment(SwingConstants.CENTER);
		OrderPatientIDLabel01.setBounds(289, 75, 48, 14);
		viewOpenOrdersPanel.add(OrderPatientIDLabel01);
		
		JLabel OrderPatientIDLabel02 = new JLabel("");
		OrderPatientIDLabel02.setHorizontalAlignment(SwingConstants.CENTER);
		OrderPatientIDLabel02.setBounds(289, 115, 48, 14);
		viewOpenOrdersPanel.add(OrderPatientIDLabel02);
		
		JLabel OrderPatientIDLabel03 = new JLabel("");
		OrderPatientIDLabel03.setHorizontalAlignment(SwingConstants.CENTER);
		OrderPatientIDLabel03.setBounds(289, 115, 48, 14);
		viewOpenOrdersPanel.add(OrderPatientIDLabel03);
		
		JLabel OrderPatientIDLabel04 = new JLabel("");
		OrderPatientIDLabel04.setHorizontalAlignment(SwingConstants.CENTER);
		OrderPatientIDLabel04.setBounds(289, 195, 48, 14);
		viewOpenOrdersPanel.add(OrderPatientIDLabel04);
		
		JLabel OrderPatientIDLabel05 = new JLabel("");
		OrderPatientIDLabel05.setHorizontalAlignment(SwingConstants.CENTER);
		OrderPatientIDLabel05.setBounds(289, 235, 48, 14);
		viewOpenOrdersPanel.add(OrderPatientIDLabel05);
		
		JLabel OrderPatientFirstNameLabel01 = new JLabel("");
		OrderPatientFirstNameLabel01.setHorizontalAlignment(SwingConstants.CENTER);
		OrderPatientFirstNameLabel01.setBounds(347, 75, 108, 14);
		viewOpenOrdersPanel.add(OrderPatientFirstNameLabel01);
		
		JLabel OrderPatientFirstNameLabel02 = new JLabel("");
		OrderPatientFirstNameLabel02.setHorizontalAlignment(SwingConstants.CENTER);
		OrderPatientFirstNameLabel02.setBounds(347, 115, 108, 14);
		viewOpenOrdersPanel.add(OrderPatientFirstNameLabel02);
		
		JLabel OrderPatientFirstNameLabel03 = new JLabel("");
		OrderPatientFirstNameLabel03.setHorizontalAlignment(SwingConstants.CENTER);
		OrderPatientFirstNameLabel03.setBounds(347, 155, 108, 14);
		viewOpenOrdersPanel.add(OrderPatientFirstNameLabel03);
		
		JLabel OrderPatientFirstNameLabel04 = new JLabel("");
		OrderPatientFirstNameLabel04.setHorizontalAlignment(SwingConstants.CENTER);
		OrderPatientFirstNameLabel04.setBounds(347, 195, 108, 14);
		viewOpenOrdersPanel.add(OrderPatientFirstNameLabel04);
		
		JLabel OrderPatientFirstNameLabel05 = new JLabel("");
		OrderPatientFirstNameLabel05.setHorizontalAlignment(SwingConstants.CENTER);
		OrderPatientFirstNameLabel05.setBounds(347, 235, 108, 14);
		viewOpenOrdersPanel.add(OrderPatientFirstNameLabel05);
		
		JLabel OrderPatientLastNameLabel01 = new JLabel("");
		OrderPatientLastNameLabel01.setHorizontalAlignment(SwingConstants.CENTER);
		OrderPatientLastNameLabel01.setBounds(465, 75, 108, 14);
		viewOpenOrdersPanel.add(OrderPatientLastNameLabel01);
		
		JLabel OrderPatientLastNameLabel02 = new JLabel("");
		OrderPatientLastNameLabel02.setHorizontalAlignment(SwingConstants.CENTER);
		OrderPatientLastNameLabel02.setBounds(465, 115, 108, 14);
		viewOpenOrdersPanel.add(OrderPatientLastNameLabel02);
		
		JLabel OrderPatientLastNameLabel03 = new JLabel("");
		OrderPatientLastNameLabel03.setHorizontalAlignment(SwingConstants.CENTER);
		OrderPatientLastNameLabel03.setBounds(465, 155, 108, 14);
		viewOpenOrdersPanel.add(OrderPatientLastNameLabel03);
		
		JLabel OrderPatientLastNameLabel04 = new JLabel("");
		OrderPatientLastNameLabel04.setHorizontalAlignment(SwingConstants.CENTER);
		OrderPatientLastNameLabel04.setBounds(465, 195, 108, 14);
		viewOpenOrdersPanel.add(OrderPatientLastNameLabel04);
		
		JLabel OrderPatientLastNameLabel05 = new JLabel("");
		OrderPatientLastNameLabel05.setHorizontalAlignment(SwingConstants.CENTER);
		OrderPatientLastNameLabel05.setBounds(465, 235, 108, 14);
		viewOpenOrdersPanel.add(OrderPatientLastNameLabel05);
		
		JLabel OrderlblFirstName = new JLabel("First Name");
		OrderlblFirstName.setHorizontalAlignment(SwingConstants.CENTER);
		OrderlblFirstName.setBounds(347, 30, 108, 14);
		viewOpenOrdersPanel.add(OrderlblFirstName);
		
		JLabel OrderlblLastName = new JLabel("Last Name");
		OrderlblLastName.setHorizontalAlignment(SwingConstants.CENTER);
		OrderlblLastName.setBounds(465, 30, 108, 14);
		viewOpenOrdersPanel.add(OrderlblLastName);
		
		JLabel OrderlblDateOfBirth = new JLabel("Date of Birth");
		OrderlblDateOfBirth.setHorizontalAlignment(SwingConstants.CENTER);
		OrderlblDateOfBirth.setBounds(573, 30, 108, 14);
		viewOpenOrdersPanel.add(OrderlblDateOfBirth);
		
		JLabel OrderPatientDateOfBirth01 = new JLabel("");
		OrderPatientDateOfBirth01.setHorizontalAlignment(SwingConstants.CENTER);
		OrderPatientDateOfBirth01.setBounds(573, 75, 108, 14);
		viewOpenOrdersPanel.add(OrderPatientDateOfBirth01);
		
		JLabel OrderPatientDateOfBirth02 = new JLabel("");
		OrderPatientDateOfBirth02.setHorizontalAlignment(SwingConstants.CENTER);
		OrderPatientDateOfBirth02.setBounds(573, 115, 108, 14);
		viewOpenOrdersPanel.add(OrderPatientDateOfBirth02);
		
		JLabel OrderPatientDateOfBirth03 = new JLabel("");
		OrderPatientDateOfBirth03.setHorizontalAlignment(SwingConstants.CENTER);
		OrderPatientDateOfBirth03.setBounds(573, 155, 108, 14);
		viewOpenOrdersPanel.add(OrderPatientDateOfBirth03);
		
		JLabel OrderPatientDateOfBirth04 = new JLabel("");
		OrderPatientDateOfBirth04.setHorizontalAlignment(SwingConstants.CENTER);
		OrderPatientDateOfBirth04.setBounds(573, 195, 108, 14);
		viewOpenOrdersPanel.add(OrderPatientDateOfBirth04);
		
		JLabel OrderPatientDateOfBirth05 = new JLabel("");
		OrderPatientDateOfBirth05.setHorizontalAlignment(SwingConstants.CENTER);
		OrderPatientDateOfBirth05.setBounds(573, 235, 108, 14);
		viewOpenOrdersPanel.add(OrderPatientDateOfBirth05);
		
		JLabel OrderlblOrderStatus = new JLabel("Order Status");
		OrderlblOrderStatus.setHorizontalAlignment(SwingConstants.CENTER);
		OrderlblOrderStatus.setBounds(680, 30, 108, 14);
		viewOpenOrdersPanel.add(OrderlblOrderStatus);
		
		JLabel OrderPatientOrderStatus01 = new JLabel("");
		OrderPatientOrderStatus01.setHorizontalAlignment(SwingConstants.CENTER);
		OrderPatientOrderStatus01.setBounds(680, 75, 108, 14);
		viewOpenOrdersPanel.add(OrderPatientOrderStatus01);
		
		JLabel OrderPatientOrderStatus02 = new JLabel("");
		OrderPatientOrderStatus02.setHorizontalAlignment(SwingConstants.CENTER);
		OrderPatientOrderStatus02.setBounds(680, 115, 108, 14);
		viewOpenOrdersPanel.add(OrderPatientOrderStatus02);
		
		JLabel OrderPatientOrderStatus03 = new JLabel("");
		OrderPatientOrderStatus03.setHorizontalAlignment(SwingConstants.CENTER);
		OrderPatientOrderStatus03.setBounds(680, 155, 108, 14);
		viewOpenOrdersPanel.add(OrderPatientOrderStatus03);
		
		JLabel OrderPatientOrderStatus04 = new JLabel("");
		OrderPatientOrderStatus04.setHorizontalAlignment(SwingConstants.CENTER);
		OrderPatientOrderStatus04.setBounds(680, 195, 108, 14);
		viewOpenOrdersPanel.add(OrderPatientOrderStatus04);
		
		JLabel OrderPatientOrderStatus05 = new JLabel("");
		OrderPatientOrderStatus05.setHorizontalAlignment(SwingConstants.CENTER);
		OrderPatientOrderStatus05.setBounds(680, 235, 108, 14);
		viewOpenOrdersPanel.add(OrderPatientOrderStatus05);
		
		JButton OrderbtnOrderId01 = new JButton("");
		OrderbtnOrderId01.setBounds(798, 75, 89, 20);
		
		
		JButton OrderbtnOrderId02 = new JButton("");
		OrderbtnOrderId02.setBounds(798, 111, 89, 20);
		
		
		JButton OrderbtnOrderId03 = new JButton("");
		OrderbtnOrderId03.setBounds(798, 151, 89, 20);
		
		
		JButton OrderbtnOrderId04 = new JButton("");
		OrderbtnOrderId04.setBounds(798, 191, 89, 20);
		
		
		JButton OrderbtnOrderId05 = new JButton("");
		OrderbtnOrderId05.setBounds(798, 231, 89, 20);
		
		
		JButton btnSearchButton = new JButton("Search");
		btnSearchButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String PatientFirstName = txtviewOrderFirstName.getText();
				String PatientLastName = txtviewOrderLastName.getText() ;
				String PatientDoB = txtviewOrderDateOfBirth.getText() ;
				String PatientEmail = txtviewOrderEmail.getText() ;
				String IdNum = "" ;
				String OrderId = "Order:" ;
				Order Orders[] = new Order[25] ;
				
				try {
					Orders = jdoctor.returnOrders(PatientFirstName, PatientLastName , PatientDoB ,PatientEmail ) ;
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				
				
				if (Orders[0] != null) {
					OrderPatientFirstNameLabel01.setText(Orders[0].getPatient().getFirstName());
					OrderPatientLastNameLabel01.setText(Orders[0].getPatient().getLastName());
					OrderPatientDateOfBirth01.setText(Orders[0].getPatient().getDateOfBirth());
					OrderPatientOrderStatus01.setText(Orders[0].getOrderStatus());
					IdNum += Orders[0].getOrderID() ;
					OrderPatientIDLabel01.setText(IdNum);
					OrderId += Orders[0].getOrderID() ;
					OrderbtnOrderId01.setText(OrderId);
					viewOpenOrdersPanel.add(OrderbtnOrderId01);
				}
				IdNum = "" ;
				OrderId = "Order:" ;
				if (Orders[1] != null) {
					OrderPatientFirstNameLabel02.setText(Orders[1].getPatient().getFirstName());
					OrderPatientLastNameLabel02.setText(Orders[1].getPatient().getLastName());
					OrderPatientDateOfBirth02.setText(Orders[1].getPatient().getDateOfBirth());
					OrderPatientOrderStatus02.setText(Orders[1].getOrderStatus());
					IdNum += Orders[1].getOrderID() ;
					OrderPatientIDLabel02.setText(IdNum);
					OrderId += Orders[1].getOrderID() ;
					OrderbtnOrderId02.setText(OrderId);
					viewOpenOrdersPanel.add(OrderbtnOrderId02);
					}
				IdNum = "" ;
				OrderId = "Order:" ;
				if (Orders[2] != null) {
					OrderPatientFirstNameLabel03.setText(Orders[2].getPatient().getFirstName());
					OrderPatientLastNameLabel03.setText(Orders[2].getPatient().getLastName());
					OrderPatientDateOfBirth03.setText(Orders[2].getPatient().getDateOfBirth());
					OrderPatientOrderStatus03.setText(Orders[2].getOrderStatus());
					IdNum += Orders[2].getOrderID() ;
					OrderPatientIDLabel03.setText(IdNum);
					OrderId += Orders[2].getOrderID() ;
					OrderbtnOrderId03.setText(OrderId);
					viewOpenOrdersPanel.add(OrderbtnOrderId03);
					}
				IdNum = "" ;
				OrderId = "Order:" ;
				if (Orders[3] != null) {
					OrderPatientFirstNameLabel04.setText(Orders[3].getPatient().getFirstName());
					OrderPatientLastNameLabel04.setText(Orders[3].getPatient().getLastName());
					OrderPatientDateOfBirth04.setText(Orders[3].getPatient().getDateOfBirth());
					OrderPatientOrderStatus04.setText(Orders[3].getOrderStatus());
					IdNum += Orders[3].getOrderID() ;
					OrderPatientIDLabel04.setText(IdNum);
					OrderId += Orders[3].getOrderID() ;
					OrderbtnOrderId04.setText(OrderId);
					viewOpenOrdersPanel.add(OrderbtnOrderId04);
					}
				IdNum = "" ;
				OrderId = "Order:" ;
				if (Orders[4] != null) {
					OrderPatientFirstNameLabel05.setText(Orders[4].getPatient().getFirstName());
					OrderPatientLastNameLabel05.setText(Orders[4].getPatient().getLastName());
					OrderPatientDateOfBirth05.setText(Orders[4].getPatient().getDateOfBirth());
					OrderPatientOrderStatus05.setText(Orders[4].getOrderStatus());
					IdNum += Orders[4].getOrderID() ;
					OrderPatientIDLabel05.setText(IdNum);
					OrderId += Orders[4].getOrderID() ;
					OrderbtnOrderId05.setText(OrderId);
					viewOpenOrdersPanel.add(OrderbtnOrderId05);
					}
				
				
				
				
			}});
		btnSearchButton.setBounds(138, 352, 129, 23);
		viewOpenOrdersPanel.add(btnSearchButton);
		
//		JButton OrderbtnNextPage = new JButton("Next Page");
//		OrderbtnNextPage.setBounds(633, 301, 120, 23);
//		viewOpenOrdersPanel.add(OrderbtnNextPage);
//		
//		JButton OrderbtnPreviousPage = new JButton("Previous Page");
//		OrderbtnPreviousPage.setBounds(465, 301, 120, 23);
//		viewOpenOrdersPanel.add(OrderbtnPreviousPage);
		
		
		// Panel to create a new Patient 
		//
		//
		//
		//
		Panel newPatientPanel = new Panel();
		newPatientPanel.setBounds(255, 33, 913, 420);
		newPatientPanel.setLayout(null);
		
		JLabel lblNewPatient = new JLabel("New Patient Information ");
		lblNewPatient.setBounds(46, 11, 179, 14);
		newPatientPanel.add(lblNewPatient);
		
		JLabel NewPatinetFirstName = new JLabel("First Name **");
		NewPatinetFirstName.setBounds(46, 66, 79, 14);
		newPatientPanel.add(NewPatinetFirstName);
		
		Label NewPatientLastName = new Label("Last Name **");
		NewPatientLastName.setBounds(46, 100, 79, 22);
		newPatientPanel.add(NewPatientLastName);
		
		JLabel lblDateOfBirth_1 = new JLabel("Date of Birth **");
		lblDateOfBirth_1.setBounds(46, 148, 99, 14);
		newPatientPanel.add(lblDateOfBirth_1);
		
		JLabel lblEmail = new JLabel("E-Mail **");
		lblEmail.setBounds(46, 196, 48, 14);
		newPatientPanel.add(lblEmail);
		
		txtnewPatientEMail = new JTextField();
		txtnewPatientEMail.setBounds(155, 193, 128, 20);
		newPatientPanel.add(txtnewPatientEMail);
		txtnewPatientEMail.setColumns(10);
		
		txtnewPatientDoB = new JTextField();
		txtnewPatientDoB.setBounds(155, 145, 128, 20);
		newPatientPanel.add(txtnewPatientDoB);
		txtnewPatientDoB.setColumns(10);
		
		txtnewPatientLastName = new JTextField();
		txtnewPatientLastName.setBounds(155, 102, 128, 20);
		newPatientPanel.add(txtnewPatientLastName);
		txtnewPatientLastName.setColumns(10);
		
		TextField txtnewPatientFirstName = new TextField();
		txtnewPatientFirstName.setBounds(155, 58, 128, 22);
		newPatientPanel.add(txtnewPatientFirstName);
		
		Label lblnewPatientGender = new Label("Preferred Gender");
		lblnewPatientGender.setBounds(42, 236, 103, 22);
		newPatientPanel.add(lblnewPatientGender);
		
		txtnewPatientGender = new JTextField();
		txtnewPatientGender.setBounds(155, 238, 128, 20);
		newPatientPanel.add(txtnewPatientGender);
		txtnewPatientGender.setColumns(10);
		
		JLabel lblnewPatientPhoneNumber = new JLabel("Phone Number");
		lblnewPatientPhoneNumber.setBounds(46, 292, 99, 14);
		newPatientPanel.add(lblnewPatientPhoneNumber);
		
		txtnewPatientPhoneNumber = new JTextField();
		txtnewPatientPhoneNumber.setBounds(155, 289, 128, 20);
		newPatientPanel.add(txtnewPatientPhoneNumber);
		txtnewPatientPhoneNumber.setColumns(10);
		
		JLabel lblnewPatientAllergies = new JLabel("Allergies");
		lblnewPatientAllergies.setBounds(394, 11, 148, 14);
		newPatientPanel.add(lblnewPatientAllergies);
		
		JLabel lblPatientNotes = new JLabel("Patient Notes");
		lblPatientNotes.setBounds(707, 11, 196, 14);
		newPatientPanel.add(lblPatientNotes);
		
		JTextArea textArea = new JTextArea();
		textArea.setLineWrap(true);
		textArea.setBounds(602, 45, 283, 276);
		newPatientPanel.add(textArea);
		
		JRadioButton rdbtnnewPatientLatex = new JRadioButton("Latex");
		rdbtnnewPatientLatex.setBounds(394, 62, 109, 23);
		newPatientPanel.add(rdbtnnewPatientLatex);
		
		JRadioButton rdbtnnewPatientMridye = new JRadioButton("Mridye");
		rdbtnnewPatientMridye.setBounds(394, 99, 109, 23);
		newPatientPanel.add(rdbtnnewPatientMridye);
		
		JRadioButton rdbtnnewPatientAsthma = new JRadioButton("Asthma");
		rdbtnnewPatientAsthma.setBounds(394, 139, 109, 23);
		newPatientPanel.add(rdbtnnewPatientAsthma);
		
		JRadioButton rdbtnnewPatientXrayDye = new JRadioButton("X-Ray Dye");
		rdbtnnewPatientXrayDye.setBounds(394, 180, 109, 23);
		newPatientPanel.add(rdbtnnewPatientXrayDye);
		
		JLabel lblRequiredFields = new JLabel("** required fields");
		lblRequiredFields.setBounds(46, 27, 128, 14);
		newPatientPanel.add(lblRequiredFields);
		
		JButton btnCreateNewPatient = new JButton("Create New Patient");
		btnCreateNewPatient.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String FirstName = txtnewPatientFirstName.getText() ;
				String LastName = txtnewPatientLastName.getText() ;
				String Email = txtnewPatientEMail.getText() ;
				String DoB = txtnewPatientDoB.getText() ;
				String gender = txtnewPatientGender.getText() ;
				String Phone = txtnewPatientPhoneNumber.getText() ;
				
				boolean latex = rdbtnnewPatientLatex.isSelected() ;
				boolean xraydye = rdbtnnewPatientXrayDye.isSelected() ;
				boolean mridye = rdbtnnewPatientMridye.isSelected() ;
				
				String Notes = textArea.getText() ;
				
				try {
					jdoctor.newPatient(FirstName, LastName, DoB, Email, gender, Phone, latex, xraydye, mridye, Notes);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				try {
					Patients = jdoctor.returnPatient(FirstName, LastName, DoB, Email) ;
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				currPatient = Patients[0] ;
				lblCurrentPatient.setText(currPatient.getFirstName() + currPatient.getLastName() + currPatient.getDateOfBirth() ) ;
			
			}
		});
		btnCreateNewPatient.setBounds(380, 347, 179, 23);
		newPatientPanel.add(btnCreateNewPatient);
		
		
		// panel to create a new order 
		//
		//
		//
		//
		JPanel newOrderPanel = new JPanel();
		newOrderPanel.setBounds(255, 33, 913, 420);
		newOrderPanel.setLayout(null);
				
		JLabel lblSelectThePatient = new JLabel("First Select a Patient");
		lblSelectThePatient.setHorizontalAlignment(SwingConstants.CENTER);
		lblSelectThePatient.setBounds(43, 63, 150, 20);
		newOrderPanel.add(lblSelectThePatient);
				
		JTextArea newOrderReasonForVisit = new JTextArea();
		newOrderReasonForVisit.setLineWrap(true);
		newOrderReasonForVisit.setBounds(620, 66, 283, 284);
		newOrderPanel.add(newOrderReasonForVisit);
				
		JLabel lblReasonForDoctor = new JLabel("Reason For Doctor Visit");
		lblReasonForDoctor.setBounds(708, 33, 139, 14);
		newOrderPanel.add(lblReasonForDoctor);
				
		JLabel lblImagingRequired = new JLabel("Imaging Required");
		lblImagingRequired.setBounds(389, 33, 116, 14);
		newOrderPanel.add(lblImagingRequired);
				
		JLabel lblnewOrder = new JLabel("Create a New Order");
		lblnewOrder.setHorizontalAlignment(SwingConstants.CENTER);
		lblnewOrder.setBounds(43, 33, 143, 14);
		newOrderPanel.add(lblnewOrder);
				
				
		JTextArea txtimagesneeded = new JTextArea();
		txtimagesneeded.setLineWrap(true);
		txtimagesneeded.setBounds(294, 66, 283, 284);
		newOrderPanel.add(txtimagesneeded);
		
		JLabel lblSelectedPatient = new JLabel("Selected Patient:");
		lblSelectedPatient.setHorizontalAlignment(SwingConstants.CENTER);
		lblSelectedPatient.setBounds(43, 172, 150, 14);
		newOrderPanel.add(lblSelectedPatient);
		
		JLabel lblNewOrderName = new JLabel("First Name:");
		lblNewOrderName.setHorizontalAlignment(SwingConstants.TRAILING);
		lblNewOrderName.setBounds(0, 200, 78, 14);
		newOrderPanel.add(lblNewOrderName);
		
		JLabel lblNeworderdob = new JLabel("Date of Birth:");
		lblNeworderdob.setHorizontalAlignment(SwingConstants.TRAILING);
		lblNeworderdob.setBounds(0, 260, 78, 14);
		newOrderPanel.add(lblNeworderdob);
		
		JLabel lblNewOrderLastName = new JLabel("Last Name:");
		lblNewOrderLastName.setHorizontalAlignment(SwingConstants.TRAILING);
		lblNewOrderLastName.setBounds(0, 230, 78, 14);
		newOrderPanel.add(lblNewOrderLastName);
		
		JLabel lblNewOrderEmail = new JLabel("Email:");
		lblNewOrderEmail.setHorizontalAlignment(SwingConstants.TRAILING);
		lblNewOrderEmail.setBounds(0, 290, 78, 14);
		newOrderPanel.add(lblNewOrderEmail);
		
		JLabel lblNewOrderPatietFirstName = new JLabel("");
		lblNewOrderPatietFirstName.setBounds(88, 200, 150, 14);
		newOrderPanel.add(lblNewOrderPatietFirstName);
		
		JLabel lblNewOrderPatientLastName = new JLabel("");
		lblNewOrderPatientLastName.setBounds(88, 230, 150, 14);
		newOrderPanel.add(lblNewOrderPatientLastName);
		
		JLabel lblNewOrderPatientDoB = new JLabel("");
		lblNewOrderPatientDoB.setBounds(88, 260, 150, 14);
		newOrderPanel.add(lblNewOrderPatientDoB);
		
		JLabel lblNewOrderPatientEmail = new JLabel("");
		lblNewOrderPatientEmail.setBounds(88, 290, 150, 14);
		newOrderPanel.add(lblNewOrderPatientEmail);
		
		Button buttoncreateorder = new Button("Create Order");
		buttoncreateorder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String NeededImages = txtimagesneeded.getText() ;
				String Notes = newOrderReasonForVisit.getText() ;
				
				try {
					jdoctor.newOrder(currPatient, NeededImages, Notes);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		buttoncreateorder.setBounds(560, 388, 80, 22);
		newOrderPanel.add(buttoncreateorder);
		
		
		JButton newPatient = new JButton("New Patient");
		newPatient.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				subActionPanel.remove(SearchPatientPanel);
				subActionPanel.remove(viewOpenOrdersPanel);
				subActionPanel.remove(newOrderPanel);
				subActionPanel.remove(detailsPanel);
				subActionPanel.remove(ViewResultsPanel);
				subActionPanel.add(newPatientPanel);
			}
		});
		newPatient.setBounds(10, 96, 203, 23);
		actionPanel.add(newPatient);
		
		JButton viewOrdersButton = new JButton("View Open Orders");
		viewOrdersButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				subActionPanel.remove(SearchPatientPanel);
				subActionPanel.remove(newPatientPanel);
				subActionPanel.remove(newOrderPanel);
				subActionPanel.remove(detailsPanel);
				subActionPanel.remove(ViewResultsPanel);
				subActionPanel.add(viewOpenOrdersPanel);
				
			}
		});
		viewOrdersButton.setBounds(10, 62, 203, 23);
		actionPanel.add(viewOrdersButton);
		
		JButton searchPatient = new JButton("Search Patients");
		searchPatient.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				subActionPanel.remove(viewOpenOrdersPanel);
				subActionPanel.remove(newPatientPanel);
				subActionPanel.remove(newOrderPanel);
				subActionPanel.remove(detailsPanel);
				subActionPanel.remove(ViewResultsPanel);
				subActionPanel.add(SearchPatientPanel);
				
			}
		});
		searchPatient.setBounds(10, 28, 203, 23);
		actionPanel.add(searchPatient);
		
		JButton newOrder = new JButton("New Order");
		newOrder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				subActionPanel.remove(viewOpenOrdersPanel);
				subActionPanel.remove(newPatientPanel);
				subActionPanel.remove(SearchPatientPanel);
				subActionPanel.remove(detailsPanel);
				subActionPanel.remove(ViewResultsPanel);
				subActionPanel.add(newOrderPanel);
				
				lblNewOrderPatietFirstName.setText(currPatient.getFirstName());
				lblNewOrderPatientLastName.setText(currPatient.getLastName());
				lblNewOrderPatientEmail.setText(currPatient.getEmail());
				lblNewOrderPatientDoB.setText(currPatient.getDateOfBirth());
				
			}
		});
		newOrder.setBounds(10, 130, 203, 23);
		actionPanel.add(newOrder);
		
		//Listener for buttons in resutls portal
		 radioListener = new ActionListener() {
				
				public void actionPerformed(ActionEvent selection) {
					
					Object selected = selection.getSource();
					int selectedIndex = buttonTracker.indexOf(selected);
					orderTransfer = orderTracker.get(selectedIndex);
					try {
						orderTransfer.setImages(jdoctor.getOrderImages(orderTransfer.getOrderID()));
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					JButton detailsButton = new JButton("Details");
					detailsButton.setBounds(300, 310, 219, 37);
					ViewResultsPanel.add(detailsButton);
					ViewResultsPanel.repaint();
					ViewResultsPanel.revalidate();
					
					ActionListener detailListener = new ActionListener() {
						
						public void actionPerformed(ActionEvent click) {
							
							subActionPanel.remove(viewOpenOrdersPanel);
							subActionPanel.remove(newPatientPanel);
							subActionPanel.remove(SearchPatientPanel);
							subActionPanel.remove(newOrderPanel);
							subActionPanel.add(detailsPanel);
							subActionPanel.remove(ViewResultsPanel);
							
							//add resultsDetail view here
							JLabel orderIDLabel = new JLabel("Order ID:");
							orderIDLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
							orderIDLabel.setHorizontalAlignment(SwingConstants.RIGHT);
							orderIDLabel.setBounds(359, 25, 100, 14);
							detailsPanel.add(orderIDLabel);
							
							JLabel orderIDOut = new JLabel(String.valueOf(orderTransfer.getOrderID()));
							orderIDOut.setFont(new Font("Tahoma", Font.PLAIN, 18));
							orderIDOut.setBounds(469, 25, 100, 14);
							detailsPanel.add(orderIDOut);
							
							JLabel patientNameLabel = new JLabel("Patient:");
							patientNameLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
							patientNameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
							patientNameLabel.setBounds(95, 100, 100, 24);
							detailsPanel.add(patientNameLabel);
							
							JLabel patientNameOut = new JLabel(orderTransfer.getPatient().getFirstName() + " " + orderTransfer.getPatient().getLastName());
							patientNameOut.setFont(new Font("Tahoma", Font.PLAIN, 14));
							patientNameOut.setBounds(205, 100, 140, 24);
							
							detailsPanel.add(patientNameOut);
							
							JLabel orderStatusLabel = new JLabel("Order Status:");
							orderStatusLabel.setHorizontalAlignment(SwingConstants.RIGHT);
							orderStatusLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
							orderStatusLabel.setBounds(95, 160, 100, 24);
							detailsPanel.add(orderStatusLabel);
							
							JLabel orderStatusOut = new JLabel(orderTransfer.getOrderStatus());
							orderStatusOut.setFont(new Font("Tahoma", Font.PLAIN, 14));
							orderStatusOut.setBounds(205, 160, 140, 24);
							detailsPanel.add(orderStatusOut);
							
							JLabel imagingLabel = new JLabel("Imaging:");
							imagingLabel.setHorizontalAlignment(SwingConstants.RIGHT);
							imagingLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
							imagingLabel.setBounds(95, 224, 100, 24);
							detailsPanel.add(imagingLabel);
							
							JLabel imagingOut = new JLabel(orderTransfer.getImagingOrder());
							imagingOut.setFont(new Font("Tahoma", Font.PLAIN, 14));
							imagingOut.setBounds(205, 224, 140, 24);
							detailsPanel.add(imagingOut);
							
							JTextArea analysisTextArea = new JTextArea();
							analysisTextArea.setText(orderTransfer.getRadioAnalysis());
							analysisTextArea.setBounds(446, 62, 292, 290);
							detailsPanel.add(analysisTextArea);
							analysisTextArea.setColumns(10);
							
							JLabel analysis = new JLabel("Analysis");
							analysis.setBounds(502, 23, 174, 31);
							detailsPanel.add(analysis);
							analysis.setHorizontalAlignment(SwingConstants.CENTER);
							analysis.setFont(new Font("Lucida Grande", Font.BOLD, 17));
							
							JLabel filesLabel = new JLabel("Files on Order:");
							filesLabel.setHorizontalAlignment(SwingConstants.RIGHT);
							filesLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
							filesLabel.setBounds(40, 288, 100, 24);
							detailsPanel.add(filesLabel);
							
							JLabel filesOut = new JLabel();
							filesOut.setFont(new Font("Tahoma", Font.PLAIN, 14));
							filesOut.setBounds(150, 288, 508, 24);
							detailsPanel.add(filesOut);
							
							if (orderTransfer.getImages().isEmpty() != true) {
								
								for(int i = 0; i < orderTransfer.getImages().size(); i++) {
									filesOut.setText(filesOut.getText() + orderTransfer.getImages().get(i).getLabel() + "   ");
								}//end for
								
								JButton viewImageButton = new JButton("View Image");
								viewImageButton.setBounds(125, 352, 150, 25);
								detailsPanel.add(viewImageButton);	
								
								
								 ActionListener imageListener = new ActionListener() {
									
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
							}//end if
						}//end Action
					};//end detailListener
					
					detailsButton.addActionListener(detailListener);
				}//end Action
			};//end radioListener
		
		JButton viewResults = new JButton("View Results");
		viewResults.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				subActionPanel.remove(viewOpenOrdersPanel);
				subActionPanel.remove(newPatientPanel);
				subActionPanel.remove(SearchPatientPanel);
				subActionPanel.remove(newOrderPanel);
				subActionPanel.remove(detailsPanel);
				subActionPanel.add(ViewResultsPanel);
				
				buttonTracker.clear();					
				orderTracker.clear();
				
				//CALL SQL HERE, SIMLILAR TO TEAMS MODALITIES ETC.
				try {
					orderList = Doctor.ViewResults();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				int yValue = 10;
				int xValue = 20;					
				
				//fills out view panel with radio buttons for each order that has not been completed.
				for(int i = 0; i < orderList.size(); i++) {
					
					JRadioButton orderRdButton = new JRadioButton( orderList.get(i).getOrderID() + ": " + orderList.get(i).getPatient().getFirstName() + " " + orderList.get(i).getPatient().getLastName());
					orderRdButton.setBounds(xValue, yValue, 250, 15);
					ViewResultsPanel.add(orderRdButton);
					orderRdButton.addActionListener(radioListener);
					radioButtonGroup.add(orderRdButton);
					
					buttonTracker.add(orderRdButton);
					orderTracker.add(orderList.get(i));
					
					yValue += 25;
					
				}//end for
				
			}
		});
		
		
		viewResults.setBounds(10, 166, 203, 23);
		actionPanel.add(viewResults);
		
		
		JButton newpatientbutton = new JButton("New Patient");
		newpatientbutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				subActionPanel.remove(SearchPatientPanel);
				subActionPanel.remove(viewOpenOrdersPanel);
				subActionPanel.remove(newOrderPanel);
				subActionPanel.add(newPatientPanel);
			}
		});
		newpatientbutton.setBounds(43, 128, 150, 23);
		newOrderPanel.add(newpatientbutton);
		
		JButton newSelectPatient = new JButton("Search for Patient");
		newSelectPatient.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				subActionPanel.remove(viewOpenOrdersPanel);
				subActionPanel.remove(newPatientPanel);
				subActionPanel.remove(newOrderPanel);
				subActionPanel.add(SearchPatientPanel);
			}
		});
		newSelectPatient.setBounds(43, 94, 150, 23);
		newOrderPanel.add(newSelectPatient);
	
		//subActionPanel.remove(newPatientPanel);
		//subActionPanel.remove(SearchPatientPanel);
		//subActionPanel.add(newOrderPanel);
		//subActionPanel.add(viewOpenOrdersPanel);
	}
	
	
	

}