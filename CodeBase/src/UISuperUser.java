package XamineRIS;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.SwingConstants;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JTextField;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import java.awt.Font;

//ToDO
//Password stuff, gonna wait for SQL
//Add a setTeam function
//Add users through SQL,  permissions not transferring
//UserID needs added to database
//Can't delete drs due to being foreign key in patient table


public class UISuperUser extends JFrame {

	private JFrame frame;

	protected ArrayList<JRadioButton> buttonTracker = new ArrayList<JRadioButton>();
	protected ArrayList<User> userTracker = new ArrayList<User>();
	protected ArrayList<User> userList = new ArrayList<User>();
	
	private JPanel viewPanel;
	private JPanel actionPanel;
	private JPanel subActionPanel;
	
	private JButton addUserButton;
	private JButton homeButton;
	private JButton editUserButton;
	private JButton removeUserButton;
	
	private ActionListener actionPanelListener;
	private ActionListener subActionPanelListener;
	private ActionListener radioListener;
	
	ButtonGroup radioButtonGroup;
	
	private User currentUser;
	private User user1;
	private User user2;
	private User user3;
	private User user4;
	private User user5;
	private User userTransfer;
	
	private Permission doctor;
	private Permission receptionist;
	private Permission tech;
	private Permission radio;
	private Permission superUser;
	
	private JTextField usernameTextField;
	private JTextField passwordTextField;
	private JTextField userIDTextField;
	private JTextField firstNameTextField;
	private JTextField lastNameTextField;
	private JTextField emailTextField;
	private JLabel Userlbl;
	private JLabel UserOut;
	private JLabel fname;
	private JLabel fnameOut;
	private JLabel lname;
	private JLabel lnameOut;
	private JLabel email;
	private JLabel emailOut;
	private JLabel username;
	private JLabel usernameOut;
	private JLabel password;
	private JLabel passwordOut;
	private JLabel userIDlbl;
	private JLabel fnamelbl;
	private JLabel lnamelbl;
	private JLabel emaillbl;
	private JLabel usernamelbl;
	private JLabel passwordlbl;
	private JLabel perlbl;
	private JTextField userIDOut;
	private JTextField fnameoutlbl;
	private JTextField lnameoutlbl;
	private JTextField emailoutlbl;
	private JTextField usernameoutlbl;
	private JTextField passwordoutlbl;
	
	private static int tempID = 1; //Just to track userIDs until we get the database set up correctly
	/**
	 * Launch the application.
	 */
	public static void main(User user) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UISuperUser window = new UISuperUser(user);
					window.frame.setVisible(true);
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
	 * Create the application.
	 * @throws SQLException 
	 */
	public UISuperUser(User user) throws SQLException {
		currentUser = user;
		
		initialize();
		InitializeListeners();
		GenerateUsers();
	}

	/**
	 * Initialize the contents of the frame.
	 * @throws SQLException 
	 */
	private void initialize() throws SQLException {
		frame = new JFrame();
		frame.setBounds(100, 100, 1204, 512);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JPanel statusBar = new JPanel();
		statusBar.setBackground(UIManager.getColor("CheckBoxMenuItem.selectionBackground"));
		statusBar.setBounds(0, 0, 1178, 25);
		frame.getContentPane().add(statusBar);
		statusBar.setLayout(null);
		
		JLabel versionText = new JLabel("Admin Portal");
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

		actionPanel = new JPanel();
		actionPanel.setBounds(10, 37, 239, 212);
		frame.getContentPane().add(actionPanel);
		actionPanel.setLayout(null);
		
		addUserButton = new JButton("Add User");
		addUserButton.setBounds(10, 115, 219, 37);
		actionPanel.add(addUserButton);		
		
		homeButton = new JButton("Home");
		homeButton.setBounds(10, 50, 219, 37);
		actionPanel.add(homeButton);
		
		viewPanel = new JPanel();
		viewPanel.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		viewPanel.setBounds(259, 36, 919, 426);
		frame.getContentPane().add(viewPanel);
		viewPanel.setLayout(null);		

		//Button group for all generated radio buttons
		radioButtonGroup = new ButtonGroup(); 
		
		subActionPanel = new JPanel();
		subActionPanel.setLayout(null);
		subActionPanel.setBounds(10, 250, 239, 212);
		frame.getContentPane().add(subActionPanel);
		
	}
	
	private void InitializeListeners() {
		
		//First Listener for radio button selection, selecting a radio button for either orders or appointments will generate a "Details" button along with details listener
		 radioListener = new ActionListener() {
			
			public void actionPerformed(ActionEvent selection) {
				
				Object selected = selection.getSource();
				int selectedIndex = buttonTracker.indexOf(selected);
				userTransfer = userTracker.get(selectedIndex);
				
				JButton detailsButton = new JButton("Details");
				detailsButton.setBounds(10, 30, 219, 37);
				subActionPanel.add(detailsButton);
				subActionPanel.repaint();
				subActionPanel.revalidate();
				
				ActionListener detailListener = new ActionListener() {
					
					public void actionPerformed(ActionEvent click) {
						
						viewUser(userTransfer);
					}//end Action
				};//end detailListener
				
				detailsButton.addActionListener(detailListener);
			}//end Action
		};//end radioListener
		
		actionPanelListener = new ActionListener() {
			
			public void actionPerformed(ActionEvent click) {
				
				if(click.getSource() == homeButton) {
					try {
						GenerateUsers();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}//end if
				
				if(click.getSource() == addUserButton) {
					AddUsers();
				}//end if
				
			}//end Action
		};//end actionPanelListener
		
		homeButton.addActionListener(actionPanelListener);
		addUserButton.addActionListener(actionPanelListener);
		
		subActionPanelListener = new ActionListener() {
			
			public void actionPerformed(ActionEvent click) {
				
				if (click.getSource() == editUserButton) {
					EditUser(userTransfer);
				}//end if
				
				if (click.getSource() == removeUserButton) {
					try {
						DeleteUser(userTransfer);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}//end if
				
			}//end Action
			
		};//end subActionPanelListener
		
	}//end InitializeListeners
	
	private void GenerateUsers() throws SQLException {
	//Setup Users radio list on portal generation and Home Button click
					
					subActionPanel.removeAll();
					viewPanel.removeAll();
					
					buttonTracker.clear();					
					userTracker.clear();
					
					//CALL SQL HERE, SIMLILAR TO TEAMS MODALITIES ETC.
					userList = ViewUsers();
					
					int yValue = 10;
					int xValue = 20;					
					
					//fills out view panel with radio buttons for each order that has not been completed.
					for(int i = 0; i < userList.size(); i++) {
						
						JRadioButton userRdButton = new JRadioButton( userList.get(i).getUserPermission().getProgramName() + ": " + userList.get(i).getFirstName() + " " + userList.get(i).getLastName());
						userRdButton.setBounds(xValue, yValue, 250, 15);
						viewPanel.add(userRdButton);
						userRdButton.addActionListener(radioListener);
						radioButtonGroup.add(userRdButton);
						
						buttonTracker.add(userRdButton);
						userTracker.add(userList.get(i));
						
						yValue += 25;
						
					}//end for
					
					subActionPanel.repaint();
					subActionPanel.revalidate();
					viewPanel.repaint();
					viewPanel.revalidate();

		
	}//end GenerateUsers
	
	private void viewUser(User user) {
		
		subActionPanel.removeAll();
		viewPanel.removeAll();
		
		JLabel firstNameLabel = new JLabel("First Name: ");
		firstNameLabel.setHorizontalAlignment(SwingConstants.TRAILING);
		firstNameLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
		firstNameLabel.setBounds(315, 100, 100, 24);
		viewPanel.add(firstNameLabel);
		
		JLabel firstNameCurrentLabel= new JLabel(user.getFirstName());
		firstNameCurrentLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
		firstNameCurrentLabel.setBounds(485, 100, 100, 24);
		viewPanel.add(firstNameCurrentLabel);
		
		JLabel lastNameLabel = new JLabel("Last Name: ");
		lastNameLabel.setHorizontalAlignment(SwingConstants.TRAILING);
		lastNameLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
		lastNameLabel.setBounds(315, 146, 100, 24);
		viewPanel.add(lastNameLabel);
		
		JLabel lastNameCurrentLabel = new JLabel(user.getLastName());
		lastNameCurrentLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
		lastNameCurrentLabel.setBounds(485, 146, 100, 24);
		viewPanel.add(lastNameCurrentLabel);
		
		JLabel emailLabel = new JLabel("Email: ");
		emailLabel.setHorizontalAlignment(SwingConstants.TRAILING);
		emailLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
		emailLabel.setBounds(315, 191, 100, 24);
		viewPanel.add(emailLabel);
		
		JLabel emailCurrentLabel = new JLabel(user.getEmail());
		emailCurrentLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
		emailCurrentLabel.setBounds(485, 191, 100, 24);
		viewPanel.add(emailCurrentLabel);
		
		JLabel usernameLabel = new JLabel("Username: ");
		usernameLabel.setHorizontalAlignment(SwingConstants.TRAILING);
		usernameLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
		usernameLabel.setBounds(315, 240, 100, 24);
		viewPanel.add(usernameLabel);
		
		JLabel usernameCurrentLabel = new JLabel(user.getUserName());
		usernameCurrentLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
		usernameCurrentLabel.setBounds(485, 240, 100, 24);
		viewPanel.add(usernameCurrentLabel);
		
		JLabel passwordLabel = new JLabel("Password: ");
		passwordLabel.setHorizontalAlignment(SwingConstants.TRAILING);
		passwordLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
		passwordLabel.setBounds(315, 286, 100, 24);
		viewPanel.add(passwordLabel);
		
		JLabel passwordCurrentLabel = new JLabel("Hidden");
		passwordCurrentLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
		passwordCurrentLabel.setBounds(485, 286, 100, 24);
		viewPanel.add(passwordCurrentLabel);
		
		editUserButton = new JButton("Edit User");
		editUserButton.setBounds(10, 30, 219, 37);
		subActionPanel.add(editUserButton);
		editUserButton.addActionListener(subActionPanelListener);
		
		removeUserButton = new JButton("Remove User");
		removeUserButton.setBounds(10, 90, 219, 37);
		subActionPanel.add(removeUserButton);
		removeUserButton.addActionListener(subActionPanelListener);
		
		subActionPanel.repaint();
		subActionPanel.revalidate();
		viewPanel.repaint();
		viewPanel.revalidate();
	}//end viewUser
	
	private void AddUsers() {
		
		subActionPanel.removeAll();
		viewPanel.removeAll();
		
		JLabel permissionLabel = new JLabel("Permission:");
		permissionLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
		permissionLabel.setBounds(587, 50, 100, 23);
		viewPanel.add(permissionLabel);
		
		JRadioButton refDrRdButton = new JRadioButton("01 Referring Doctor");
		refDrRdButton.setFont(new Font("Tahoma", Font.PLAIN, 12));
		refDrRdButton.setBounds(677, 52, 201, 23);
		viewPanel.add(refDrRdButton);
		
		JRadioButton recepRdButton = new JRadioButton("02 Receptionist");
		recepRdButton.setFont(new Font("Tahoma", Font.PLAIN, 12));
		recepRdButton.setBounds(677, 99, 201, 23);
		viewPanel.add(recepRdButton);
		
		JLabel firstNameLabel = new JLabel("First Name:");
		firstNameLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
		firstNameLabel.setBounds(80, 98, 100, 23);
		viewPanel.add(firstNameLabel);
		
		firstNameTextField = new JTextField();
		firstNameTextField.setColumns(10);
		firstNameTextField.setBounds(173, 101, 201, 20);
		viewPanel.add(firstNameTextField);
		
		JRadioButton techRdButton = new JRadioButton("03 Technician");
		techRdButton.setFont(new Font("Tahoma", Font.PLAIN, 12));
		techRdButton.setBounds(677, 146, 201, 23);
		viewPanel.add(techRdButton);
		
		JRadioButton radRdButton = new JRadioButton("04 Radiologist");
		radRdButton.setFont(new Font("Tahoma", Font.PLAIN, 12));
		radRdButton.setBounds(677, 182, 201, 23);
		viewPanel.add(radRdButton);
		
		JLabel lastNameLabel = new JLabel("Last Name:");
		lastNameLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
		lastNameLabel.setBounds(80, 132, 100, 23);
		viewPanel.add(lastNameLabel);
		
		lastNameTextField = new JTextField();
		lastNameTextField.setColumns(10);
		lastNameTextField.setBounds(173, 135, 201, 20);
		viewPanel.add(lastNameTextField);
		
		JRadioButton supRdButton = new JRadioButton("05 Super User");
		supRdButton.setFont(new Font("Tahoma", Font.PLAIN, 12));
		supRdButton.setBounds(677, 222, 201, 23);
		viewPanel.add(supRdButton);
		
		JLabel emailLabel = new JLabel("Email:");
		emailLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
		emailLabel.setBounds(80, 181, 100, 23);
		viewPanel.add(emailLabel);
		
		emailTextField = new JTextField();
		emailTextField.setColumns(10);
		emailTextField.setBounds(173, 184, 201, 20);
		viewPanel.add(emailTextField);
		
		JLabel usernameLabel = new JLabel("Username:");
		usernameLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
		usernameLabel.setBounds(80, 240, 100, 23);
		viewPanel.add(usernameLabel);
		
		usernameTextField = new JTextField();
		usernameTextField.setColumns(10);
		usernameTextField.setBounds(173, 243, 201, 20);
		viewPanel.add(usernameTextField);
		
		JLabel passwordLabel = new JLabel("Password:");
		passwordLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
		passwordLabel.setBounds(80, 294, 100, 23);
		viewPanel.add(passwordLabel);
		
		passwordTextField = new JTextField();
		passwordTextField.setColumns(10);
		passwordTextField.setBounds(173, 297, 201, 20);
		viewPanel.add(passwordTextField);
		
		JButton submitButton = new JButton("Submit Changes");
		submitButton.setBounds(626, 296, 252, 23);
		viewPanel.add(submitButton);
		
		ActionListener submitNewButton = new ActionListener() {
			
			public void actionPerformed (ActionEvent click) {
				
				User newUser = new User(firstNameTextField.getText(), lastNameTextField.getText(), emailTextField.getText(), usernameTextField.getText());
				 int passwordlength = passwordTextField.getText().length() ;
				newUser.setPassword(passwordTextField.getText(), passwordlength);
				//newUser.setUserId(Integer.valueOf(userIDTextField.getText()));
				
				ArrayList<Permission> permissions;
				try {
					permissions = ReturnPermission();
				
				
				for(int i = 0; i < permissions.size(); i++) {
					switch(permissions.get(i).getAccessLvl()) {
					case 1:{
						doctor = permissions.get(i);
						System.out.println(doctor.getAccessLvl() + " " + doctor.getCodeName()) ;
						break;
					}
					case 2:{
						receptionist = permissions.get(i);
						break;
					}
					case 3:{
						tech = permissions.get(i);
						break;
					}
					case 4:{
						radio = permissions.get(i);
						break;
					}
					case 5:{
						superUser = permissions.get(i);
						break;
					}
					}
					
				}//end for loop
				
				} catch (SQLException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				
				
				if (refDrRdButton.isSelected()) 
					newUser.setUserPermission(doctor); 
				else if(recepRdButton.isSelected())
					newUser.setUserPermission(receptionist);
				else if(techRdButton.isSelected())
					newUser.setUserPermission(tech);
				else if(radRdButton.isSelected())
					newUser.setUserPermission(radio);
				else if(supRdButton.isSelected())
					newUser.setUserPermission(superUser);
				
				
				//SQL Update method here
				try {
					InsertUser(newUser);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				try {
					GenerateUsers();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}//end Action
			
		};//end submitNewListener
		
		submitButton.addActionListener(submitNewButton);
		
		subActionPanel.repaint();
		subActionPanel.revalidate();
		viewPanel.repaint();
		viewPanel.revalidate();

		
		
	}//end AddUsers
	
	private void EditUser(User user) {
		
		String permission = user.getUserPermission().getProgramName();
		String userFormer = user.getUserName();
		
		subActionPanel.removeAll();
		viewPanel.removeAll();
		
		JLabel permissionLabel = new JLabel("Permission:");
		permissionLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
		permissionLabel.setBounds(587, 50, 100, 23);
		viewPanel.add(permissionLabel);
		
		JRadioButton refDrRdButton = new JRadioButton("01 Referring Doctor");
		refDrRdButton.setFont(new Font("Tahoma", Font.PLAIN, 12));
		refDrRdButton.setBounds(677, 52, 201, 23);
		viewPanel.add(refDrRdButton);
		
		JRadioButton recepRdButton = new JRadioButton("02 Receptionist");
		recepRdButton.setFont(new Font("Tahoma", Font.PLAIN, 12));
		recepRdButton.setBounds(677, 99, 201, 23);
		viewPanel.add(recepRdButton);
		
		JLabel firstNameLabel = new JLabel("First Name:");
		firstNameLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
		firstNameLabel.setBounds(80, 98, 100, 23);
		viewPanel.add(firstNameLabel);
		
		firstNameTextField = new JTextField();
		firstNameTextField.setColumns(10);
		firstNameTextField.setBounds(173, 101, 201, 20);
		viewPanel.add(firstNameTextField);
		
		JRadioButton techRdButton = new JRadioButton("03 Technician");
		techRdButton.setFont(new Font("Tahoma", Font.PLAIN, 12));
		techRdButton.setBounds(677, 146, 201, 23);
		viewPanel.add(techRdButton);
		
		JRadioButton radRdButton = new JRadioButton("04 Radiologist");
		radRdButton.setFont(new Font("Tahoma", Font.PLAIN, 12));
		radRdButton.setBounds(677, 182, 201, 23);
		viewPanel.add(radRdButton);
		
		JLabel lastNameLabel = new JLabel("Last Name:");
		lastNameLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
		lastNameLabel.setBounds(80, 132, 100, 23);
		viewPanel.add(lastNameLabel);
		
		lastNameTextField = new JTextField();
		lastNameTextField.setColumns(10);
		lastNameTextField.setBounds(173, 135, 201, 20);
		viewPanel.add(lastNameTextField);
		
		JRadioButton supRdButton = new JRadioButton("05 Super User");
		supRdButton.setFont(new Font("Tahoma", Font.PLAIN, 12));
		supRdButton.setBounds(677, 222, 201, 23);
		viewPanel.add(supRdButton);
		
		JLabel emailLabel = new JLabel("Email:");
		emailLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
		emailLabel.setBounds(80, 181, 100, 23);
		viewPanel.add(emailLabel);
		
		emailTextField = new JTextField();
		emailTextField.setColumns(10);
		emailTextField.setBounds(173, 184, 201, 20);
		viewPanel.add(emailTextField);
		
		JLabel usernameLabel = new JLabel("Username:");
		usernameLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
		usernameLabel.setBounds(80, 240, 100, 23);
		viewPanel.add(usernameLabel);
		
		usernameTextField = new JTextField();
		usernameTextField.setColumns(10);
		usernameTextField.setBounds(173, 243, 201, 20);
		viewPanel.add(usernameTextField);
		
		JLabel passwordLabel = new JLabel("Password:");
		passwordLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
		passwordLabel.setBounds(80, 294, 100, 23);
		viewPanel.add(passwordLabel);
		
		passwordTextField = new JTextField();
		passwordTextField.setColumns(10);
		passwordTextField.setBounds(173, 297, 201, 20);
		viewPanel.add(passwordTextField);
		
		
		//Makes sure proper permission is selected on view generation
		switch(permission) {
		case "ReferringDr":{
			refDrRdButton.setSelected(true);
			break;
		}
		case "Receptionist":{
			recepRdButton.setSelected(true);
			break;
		}
		case "Technician":{
			techRdButton.setSelected(true);
			break;
		}
		case "Radiologist":{
			radRdButton.setSelected(true);
			break;
		}
		case "SuperUser":{
			supRdButton.setSelected(true);
			break;
		}
		}//end switch

		
		JButton submitButton = new JButton("Submit Changes");
		submitButton.setBounds(626, 296, 252, 23);
		viewPanel.add(submitButton);
		
		ActionListener submitEditListener = new ActionListener() {
			
			public void actionPerformed (ActionEvent click) {
				
				user.setFirstName(firstNameTextField.getText());
				user.setLastName(lastNameTextField.getText());
				user.setEmail(emailTextField.getText());
				user.setUserName(usernameTextField.getText());
				int passwordLength = passwordTextField.getText().length() ;
				user.setPassword(passwordTextField.getText(), passwordLength);
				//user.setUserId(Integer.valueOf(userIDTextField.getText()));
				
				ArrayList<Permission> permissions;
				try {
					permissions = ReturnPermission();
				
				
				for(int i = 0; i < permissions.size(); i++) {
					switch(permissions.get(i).getAccessLvl()) {
					case 1:{
						doctor = permissions.get(i);
						System.out.println(doctor.getAccessLvl() + " " + doctor.getCodeName()) ;
						break;
					}
					case 2:{
						receptionist = permissions.get(i);
						break;
					}
					case 3:{
						tech = permissions.get(i);
						break;
					}
					case 4:{
						radio = permissions.get(i);
						break;
					}
					case 5:{
						superUser = permissions.get(i);
						break;
					}
					}
					
				}//end for loop
				
				} catch (SQLException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				
				if (refDrRdButton.isSelected()) 
					user.setUserPermission(doctor); 
				else if(recepRdButton.isSelected())
					user.setUserPermission(receptionist);
				else if(techRdButton.isSelected())
					user.setUserPermission(tech);
				else if(radRdButton.isSelected())
					user.setUserPermission(radio);
				else if(supRdButton.isSelected())
					user.setUserPermission(superUser);
				
				try {
					UpdateUser(user, userFormer);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					GenerateUsers();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}//end Action
			
		};//end submitNewListener
		
		submitButton.addActionListener(submitEditListener);
		
		subActionPanel.repaint();
		subActionPanel.revalidate();
		viewPanel.repaint();
		viewPanel.revalidate();
		
	}//end EditUser
	
	private void DeleteUser(User user) throws SQLException {
		
		//Removes user after asking for confirmation
		int index = userList.indexOf(user);
		RemoveUser(userList.get(index));
		GenerateUsers();
	}//end DeleteUser
	
	//START SQL HERE
	
	public static ArrayList<User> ViewUsers() throws SQLException {
		/*Searches through orders for current day's appointments
		 * Puts Orders for selected date in arraylist for UIManager to access and display
		 * UIManager displays orderID, appt time, room number, and imaging ordered
		 * User can select appt and press select button to display patient information
		 */		
		
		//Adding parameters for testing
		ArrayList<User> allUsers = new ArrayList<>();
		int index = 0;
		ArrayList<Permission> permissions = ReturnPermission();
		Connection conn = getConnection();
		
		PreparedStatement statement = conn.prepareStatement("Select * FROM user order by accesslvl asc;" ) ;
		
		ResultSet result = statement.executeQuery() ;
		
		while(result.next()) {
			allUsers.add(index, new User(result.getString("firstName"), result.getString("lastName"), 
					result.getString("email"), result.getString("userName")));
			allUsers.get(index).setActive(result.getBoolean("isActive"));
			allUsers.get(index).setStaff(result.getBoolean("isStaff"));
			allUsers.get(index).setSuperUser(result.getBoolean("isSuperUser"));
			allUsers.get(index).setPassword(result.getString("password"), result.getString("password").length());
			allUsers.get(index).setUserId(tempID);
			tempID++;
			
			//Find permission matching this user
			for(int i = 0; i < permissions.size(); i++) {
				if(permissions.get(i).getAccessLvl() == result.getInt("accesslvl")) {
					allUsers.get(index).setUserPermission(permissions.get(i));
					break;
				}//end if
			}//end for
			
			
			index++ ;
		}
		
		System.out.println(" Users found successfully");
		return allUsers ;
	}
	
	public static ArrayList<Permission> ReturnPermission() throws SQLException {
		
		 ArrayList<Permission> currPermission = new ArrayList<>();
		 int index = 0 ;
		 
		 Connection conn = getConnection() ;
		 PreparedStatement statement = conn.prepareStatement("Select * from permission ;") ;
		
		ResultSet result = statement.executeQuery() ;
		
		while(result.next()) {
			currPermission.add(index, new Permission(result.getString("programName")));
			currPermission.get(index).setAccessLvl(result.getInt("accesslvl"));
			currPermission.get(index).setCodeName(result.getString("codeName"));
			index++;		
	}
		 
		result.close();
		
		return currPermission ;
	}
	
	public void InsertUser(User user) throws SQLException {
		
		String userName = user.getUserName();
		int accesslvl = user.getUserPermission().getAccessLvl();
		System.out.println(accesslvl) ;
		String fname = user.getFirstName();
		String lname = user.getLastName();
		String email = user.getEmail();
		char[] password = user.getPassword();
		
		//converts char array to string
		String passwordString = new String(password);
		
		 Connection conn = getConnection() ;
		 PreparedStatement statement = conn.prepareStatement("INSERT INTO user (userName, accesslvl, firstName, lastName, password, email)\r\n"
		 		+ "VALUES (?, ?, ?, ?, ?, ?);") ;
		 
		statement.setString(1, userName );
		statement.setInt(2, accesslvl);
		statement.setString(3, fname);
		statement.setString(4, lname);
		statement.setString(5,  passwordString);
		statement.setString(6,  email);
		
		statement.execute();
		
	}
	
	public void UpdateUser(User user, String formerUser) throws SQLException {
		
		String userName = user.getUserName();
		int accesslvl = user.getUserPermission().getAccessLvl();
		String fname = user.getFirstName();
		String lname = user.getLastName();
		String email = user.getEmail();
		char[] password = user.getPassword();
		
		//converts char array to string
		String passwordString = new String(password);
		
		 Connection conn = getConnection() ;
		 PreparedStatement statement = conn.prepareStatement("Update user "
		 		+ "SET userName = ?, accesslvl = ?, firstName = ?, lastName = ?, password = ?, email = ?"
		 		+ "WHERE userName = ?;") ;
		 
		statement.setString(1, userName );
		statement.setInt(2, accesslvl);
		statement.setString(3, fname);
		statement.setString(4, lname);
		statement.setString(5,  passwordString);
		statement.setString(6,  email);
		statement.setString(7, formerUser );
		
		statement.executeUpdate();
		
	}
	
	public void RemoveUser(User user) throws SQLException {
	
		String userName = user.getUserName();
	
		Connection conn = getConnection() ;
		PreparedStatement statement = conn.prepareStatement("DELETE FROM user WHERE userName = ?;") ;

		statement.setString(1, userName );
		statement.executeUpdate();
	
	}
	
	
}
