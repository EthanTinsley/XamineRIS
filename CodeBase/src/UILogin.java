package XamineRIS;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import javax.swing.SwingConstants;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
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
import java.util.Arrays;

import javax.swing.JTextField;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.JPasswordField;

public class UILogin extends JFrame {

	/** TODO
	 * Need to figure out how to close window
	 * Need to fix password char array to deal with blank indexes
	 */
	private static final long serialVersionUID = 1L;
	private JFrame frame;
	private JTextField usernameIn;
	private JPasswordField passwordIn;
	private UILogin window;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UILogin window = new UILogin();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public UILogin() {
		

		initialize();
		
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 375, 385);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel usernameLabel = new JLabel("Username:");
		usernameLabel.setHorizontalAlignment(SwingConstants.CENTER);
		usernameLabel.setBounds(25, 100, 117, 23);
		frame.getContentPane().add(usernameLabel);
		
		usernameIn = new JTextField();
		usernameIn.setBounds(180, 100, 143, 20);
		frame.getContentPane().add(usernameIn);
		usernameIn.setColumns(10);
		
		JLabel passwordLabel = new JLabel("Password:");
		passwordLabel.setHorizontalAlignment(SwingConstants.CENTER);
		passwordLabel.setBounds(25, 150, 117, 23);
		frame.getContentPane().add(passwordLabel);
		
		JButton logInButton = new JButton("Log in");
		logInButton.setBounds(125, 215, 89, 23);
		frame.getContentPane().add(logInButton);
		
		JPanel statusPanel = new JPanel();
		statusPanel.setBackground(UIManager.getColor("CheckBoxMenuItem.selectionBackground"));
		statusPanel.setBounds(0, 0, 359, 25);
		frame.getContentPane().add(statusPanel);
		statusPanel.setLayout(null);
		
		JLabel versionLabel = new JLabel("Xamine V.2382777");
		versionLabel.setBounds(0, 0, 125, 23);
		statusPanel.add(versionLabel);
		versionLabel.setHorizontalAlignment(SwingConstants.CENTER);
		
		passwordIn = new JPasswordField(10);
		passwordIn.setBounds(180, 151, 143, 20);
		frame.getContentPane().add(passwordIn);
		
		
		JLabel testLabel = new JLabel();
		testLabel.setBounds(50, 267, 216, 14);
		frame.getContentPane().add(testLabel);
		
		ActionListener loginListener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent login) {
				String usernameValue = usernameIn.getText();
				char[] passwordValue = new char[8];
				passwordValue = passwordIn.getPassword();
				int permissionLvl = -1;//Set to -1 since this permission does not exist
				
				User user = new User() ;
				try {
					user = getAccess(usernameValue, passwordValue);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if (user != null) permissionLvl = user.getUserPermission().getAccessLvl();
				else testLabel.setText("Invalid Login");
				
				
				switch(permissionLvl) {
				case 0:{
					UISuperUser.main(user);
					testLabel.setText(String.valueOf(permissionLvl));
					frame.dispose();
					break;
				}
				case 1:{
					UIReferringDoctor.main(user);
					frame.dispose();
					break;
				}
				case 2:{
					UIReceptionist.main(user);
					frame.dispose();
					break;
				}
				case 3:{
					UITechnician.main(user);
					frame.dispose();
					break;
				}
				case 4:{
					UIRadiologist.main(user);
					frame.dispose();
					break;
					
				}
			
				
				}
			}
			
			
		};
		
		logInButton.addActionListener(loginListener);
	}

	protected void close() {
		// TODO Auto-generated method stub
		window.close();
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
			   System.out.println("");
			   return conn;
			  } 
		 catch(Exception e){
			 System.out.println(e);
		}	  
		return null ;
	}
	
	public String getPassword (String username) throws SQLException {
		
		String password = "" ;
		Connection conn = getConnection() ;
		
		PreparedStatement statement = conn.prepareStatement("Select password from user where username = ? ") ;
		
		statement.setString(1, username ) ;
		
		ResultSet result = statement.executeQuery() ;
		
		while(result.next()) {
			password = result.getString("password") ;
		}
		
		System.out.println("password retrieval");
	return password ;	
		
	} 
	
	public Permission getUserPermission(User user) throws SQLException {
		
		Permission permission = null ;
		Connection conn = getConnection() ;
		
		PreparedStatement statement = conn.prepareStatement("select programName from permission where accesslvl in ( Select accesslvl from user where userName = ? ) ;") ;
		
		statement.setString(1, user.getUserName() ) ;
		
		ResultSet result = statement.executeQuery() ;
		
		while(result.next()) {
			permission = new Permission(result.getString("programName"));
		}
		
		System.out.println("permission in the clear ");
		return permission ;
	}
	
	public User getAccess(String username, char[] password ) throws SQLException {
		
		User user = new User() ;
		String Password = String(password) ;
		
		System.out.println(Password);
		if (!(Password.equals(getPassword(username)))) {System.out.println("password failure");System.out.println(getPassword(username)) ; return null ;  }
		
		Connection conn = getConnection() ;
		
		PreparedStatement statement = conn.prepareStatement("Select * from user where username = ? ") ;
		
		statement.setString(1, username ) ;
		
		ResultSet result = statement.executeQuery() ;
		
		while(result.next()) {
			user = new User(result.getString("firstName"),result.getString("lastName"), result.getString("email"), result.getString("userName")) ;		
		}
		
		result.close();
		
		user.setUserPermission(getUserPermission(user));
		
		System.out.println("Everything worked In the back");
		
		return user ;
		
	}
	
	private String String(char[] password) {
		
		String result = "" ;
		
		for (int x = 0 ; x < password.length ; x++) {
			result += password[x] ;
		}
		
		
		return result ;
	}
	
}
