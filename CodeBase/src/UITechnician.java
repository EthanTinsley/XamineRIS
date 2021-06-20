package XamineRIS ;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import javax.swing.SwingConstants;

import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.GridBagConstraints;
import javax.swing.JButton;
import java.awt.Insets;
import javax.swing.JTextField;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JCheckBox;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.ImageIcon;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JScrollPane;
import javax.swing.JRadioButton;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.Container;

import javax.swing.JComboBox;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

import javax.swing.Action;
import java.awt.Font;

import java.sql.Date;
import java.sql.SQLException;

//TODO
//Send order method is finished but has bug that should be fixed by SQL introduction
//Implement allergy conflict
//Fix details view for absolute layout

public class UITechnician extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JFrame frame;
	protected Order selectedOrder; //For use in details view
	
	protected ArrayList<JRadioButton> buttonTracker = new ArrayList<>();
	protected ArrayList<Order> orderTracker = new ArrayList<>();
	private static UITechnician window;
	private User currentUser;
	private int currentIndex = 0;
	private int imageID = 0;

	/**
	 * Launch the application.
	 */
	public static void main(User user) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					window = new UITechnician(user);
					window.frame.setVisible(true);
				}//end try
				catch (Exception e) {
					e.printStackTrace();
				}//end catch
			}//end run
		}//end Runnable
		);
	}//end main

	/**
	 * Create the application.
	 */
	public UITechnician(User user) {
		currentUser = user;
		initialize();
	}//end UITechnician

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		frame = new JFrame();
		frame.setBounds(100, 100, 1204, 512);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JPanel statusBar = new JPanel();
		statusBar.setBackground(UIManager.getColor("CheckBoxMenuItem.selectionBackground"));
		statusBar.setBounds(0, 0, 1178, 25);
		frame.getContentPane().add(statusBar);
		statusBar.setLayout(null);
		
		JLabel versionText = new JLabel("Technician Portal");
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
		
		JButton notifyButton = new JButton("Notifications");
		notifyButton.setBounds(928, 3, 105, 20);
		statusBar.add(notifyButton);
		
		JPanel actionPanel = new JPanel();
		actionPanel.setBounds(10, 37, 239, 214);
		frame.getContentPane().add(actionPanel);
		actionPanel.setLayout(null);
		
		JButton viewButton = new JButton("View Today's Appointments");
		viewButton.setBounds(10, 75, 203, 23);
		actionPanel.add(viewButton);
		
		JButton viewOrdersButton = new JButton("View Open Orders");
		viewOrdersButton.setBounds(10, 109, 203, 23);
		actionPanel.add(viewOrdersButton);
		
		JLabel testLabel = new JLabel();
		testLabel.setBounds(10, 164, 203, 14);
		actionPanel.add(testLabel);
		
		JButton homeButton = new JButton("Home");
		homeButton.setBounds(10, 41, 203, 23);
		
		JPanel subActionPanel = new JPanel();
		subActionPanel.setLayout(null);
		subActionPanel.setBounds(10, 248, 239, 214);
		frame.getContentPane().add(subActionPanel);
		
		JPanel viewPanel = new JPanel();
		viewPanel.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		viewPanel.setBounds(259, 36, 919, 426);
		frame.getContentPane().add(viewPanel);
		viewPanel.setLayout(null);
		
		//Button group for all generated radio buttons
		ButtonGroup radioButtonGroup = new ButtonGroup(); 
		
		
		
		
		
		//End initial setup-------------------------------------------------------------------------------------------------
		
		//First Listener for radio button selection, selecting a radio button for either orders or appointments will generate a "Details" button along with details listener
		ActionListener radioListener = new ActionListener() {
			
			public void actionPerformed(ActionEvent selection) {
				
				Object selected = selection.getSource();
				int selectedIndex = buttonTracker.indexOf(selected);
				Order orderTransfer = orderTracker.get(selectedIndex);
				
				JButton detailsButton = new JButton("Details");
				detailsButton.setBounds(10, 35, 203, 23);
				subActionPanel.add(detailsButton);
				subActionPanel.repaint();
				subActionPanel.revalidate();
				
				ActionListener detailsListener = new ActionListener() {
					
					public void actionPerformed(ActionEvent click) {
						
						//Setup Details view
						subActionPanel.removeAll();
						viewPanel.removeAll();
						
						//Patient info (Allergies, name, gender, age), checked-in, Appt ID, Date, Time, Order ID, Order Status, Modality, Imaging, Images on order
						//Generate Details View
						JLabel orderIDLabel = new JLabel("Order ID:");
						orderIDLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
						orderIDLabel.setHorizontalAlignment(SwingConstants.RIGHT);
						orderIDLabel.setBounds(359, 25, 100, 14);
						viewPanel.add(orderIDLabel);
						
						JLabel orderIDOut = new JLabel(String.valueOf(orderTransfer.getOrderID()));
						orderIDOut.setFont(new Font("Tahoma", Font.PLAIN, 18));
						orderIDOut.setBounds(469, 25, 100, 14);
						viewPanel.add(orderIDOut);
						
						JLabel patientNameLabel = new JLabel("Patient:");
						patientNameLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
						patientNameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
						patientNameLabel.setBounds(95, 100, 100, 24);
						viewPanel.add(patientNameLabel);
						
						JLabel patientNameOut = new JLabel(orderTransfer.getPatient().getFirstName() + " " + orderTransfer.getPatient().getLastName());
						patientNameOut.setFont(new Font("Tahoma", Font.PLAIN, 14));
						patientNameOut.setBounds(205, 100, 140, 24);
						
						viewPanel.add(patientNameOut);
						
						JLabel ageLabel = new JLabel("Age:");
						ageLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
						ageLabel.setHorizontalAlignment(SwingConstants.RIGHT);
						ageLabel.setBounds(95, 160, 100, 24);
						viewPanel.add(ageLabel);
						
						JLabel ageOut = new JLabel(String.valueOf(orderTransfer.getPatient().getAge()));
						ageOut.setFont(new Font("Tahoma", Font.PLAIN, 14));
						ageOut.setBounds(205, 160, 140, 24);
						viewPanel.add(ageOut);
						
						JLabel allergiesLabel = new JLabel("Known Allergies:");
						allergiesLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
						allergiesLabel.setHorizontalAlignment(SwingConstants.RIGHT);
						allergiesLabel.setBounds(95, 224, 100, 24);
						viewPanel.add(allergiesLabel);
						
						JLabel allergiesOut = new JLabel(orderTransfer.getPatient().getAllergy());
						allergiesOut.setFont(new Font("Tahoma", Font.PLAIN, 14));
						allergiesOut.setBounds(205, 224, 140, 24);
						viewPanel.add(allergiesOut);
						
						JLabel orderStatusLabel = new JLabel("Order Status:");
						orderStatusLabel.setHorizontalAlignment(SwingConstants.RIGHT);
						orderStatusLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
						orderStatusLabel.setBounds(537, 100, 100, 24);
						viewPanel.add(orderStatusLabel);
						
						JLabel orderStatusOut = new JLabel(orderTransfer.getOrderStatus());
						orderStatusOut.setFont(new Font("Tahoma", Font.PLAIN, 14));
						orderStatusOut.setBounds(645, 100, 140, 24);
						viewPanel.add(orderStatusOut);
						
						JLabel modalityLabel = new JLabel("Modality:");
						modalityLabel.setHorizontalAlignment(SwingConstants.RIGHT);
						modalityLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
						modalityLabel.setBounds(537, 160, 100, 24);
						viewPanel.add(modalityLabel);
						
						//JLabel modalityOut = new JLabel(orderTransfer.getModality().getModalityName());
						//modalityOut.setFont(new Font("Tahoma", Font.PLAIN, 14));
						//modalityOut.setBounds(645, 160, 140, 24);
						//viewPanel.add(modalityOut);
						
						JLabel imagingLabel = new JLabel("Imaging:");
						imagingLabel.setHorizontalAlignment(SwingConstants.RIGHT);
						imagingLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
						imagingLabel.setBounds(537, 224, 108, 24);
						viewPanel.add(imagingLabel);
						
						JLabel imagingOut = new JLabel(orderTransfer.getImagingOrder());
						imagingOut.setFont(new Font("Tahoma", Font.PLAIN, 14));
						imagingOut.setBounds(655, 224, 140, 24);
						viewPanel.add(imagingOut);
						
						JLabel filesLabel = new JLabel("Files on Order:");
						filesLabel.setHorizontalAlignment(SwingConstants.RIGHT);
						filesLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
						filesLabel.setBounds(261, 309, 100, 24);
						viewPanel.add(filesLabel);
						
						JLabel filesOut = new JLabel();
						filesOut.setFont(new Font("Tahoma", Font.PLAIN, 14));
						filesOut.setBounds(371, 316, 508, 14);
						viewPanel.add(filesOut);
						
						if (orderTransfer.getImages().isEmpty() != true) {
							
							for(int i = 0; i < orderTransfer.getImages().size(); i++) {
								filesOut.setText(filesOut.getText() + orderTransfer.getImages().get(i).getLabel() + "   ");
							}//end for
						}//end if
						
						//Generate Sub Action menu buttons
						
						JButton addImageButton = new JButton("Add Images");
						addImageButton.setBounds(10, 35, 203, 23);
						subActionPanel.add(addImageButton);
						
						JButton viewImageButton = new JButton("View Images");
						viewImageButton.setBounds(10, 80, 203, 23);
						subActionPanel.add(viewImageButton);
						
						JButton deleteImageButton = new JButton("Delete Image");
						deleteImageButton.setBounds(10, 125, 203, 23);
						subActionPanel.add(deleteImageButton);
						
						JButton submitOrderButton = new JButton("Submit Order");
						submitOrderButton.setBounds(10, 170, 203, 23);
						subActionPanel.add(submitOrderButton);
						
						subActionPanel.repaint();
						subActionPanel.revalidate();
						viewPanel.repaint();
						viewPanel.revalidate();
						
						ActionListener imageListener = new ActionListener() {
							
							public void actionPerformed(ActionEvent click) {
								
								if (click.getSource() == addImageButton) {
									
									JFileChooser chooser= new JFileChooser();

									int choice = chooser.showOpenDialog(addImageButton);

									if (choice != JFileChooser.APPROVE_OPTION) return;

									File chosenFile = chooser.getSelectedFile();
									
									BufferedImage chosenImage = null;
									try {
										chosenImage = ImageIO.read(chosenFile);
									}//end try 
									catch (IOException e) {
										
										e.printStackTrace();
									}//end catch
									
									ImageFile newImage = new ImageFile(imageID, chosenImage,chosenFile.getAbsolutePath(), chosenFile.getName(), currentUser.getUserName() );
									imageID++;
									orderTransfer.getImages().add(newImage);
									
									
									
									int index = orderTransfer.getImages().indexOf(newImage);
									
									//SQL HERE UPDATEIMAGES
									try {
										Technician.UpdateImages(orderTransfer, index);
									} catch (SQLException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									
									filesOut.setText(filesOut.getText() + "   " + orderTransfer.getImages().get(index).getLabel());
									
									//Create method for each event, easier to call on several which may be what is needed to function properly
								}//end if
								
								else if (click.getSource() == viewImageButton || click.getSource() == deleteImageButton) {
									
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
										
										JButton deleteButton = new JButton("Delete Image");
										deleteButton.setBounds(374, 431, 130, 23);
										imageFrame.contentPane.add(deleteButton);
										
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
												
												else if (click.getSource() == deleteButton) {
													
													if (orderTransfer.getImages().size() <= 1) {
														//SQL HERE TO REMOVE IMAGE
														orderTransfer.getImages().clear();
														imageLabel.setIcon(null);
														imageLabel.setText("No images");
														imageFrame.viewImagePanel.repaint();
														imageFrame.viewImagePanel.revalidate();
													}//end nested if
													
													else {
														//SQL HERE TO REMOVE IMAGE
														orderTransfer.getImages().remove(currentIndex);
														currentIcon = new ImageIcon(orderTransfer.getImages().get(currentIndex).getPath()); 
														imageLabel.setIcon(currentIcon);
														imageFrame.viewImagePanel.repaint();
														imageFrame.viewImagePanel.revalidate();
													}//end else
												}//end else if
											}//end Action
										};//end imageFrameListener
										
										
										nextButton.addActionListener(imageFrameListener);
										prevButton.addActionListener(imageFrameListener);
										deleteButton.addActionListener(imageFrameListener);
										
										
									}//end if
										
								}//end else if
							}//end Action
								
							};//end imageListener
							
							ActionListener submitListener = new ActionListener() {

							
								public void actionPerformed(ActionEvent click) {
									
									if (click.getSource() == submitOrderButton) {
										
										if(orderTransfer.getImages().size() > 0) {
											//SQL HERE TO UPDATE STATUS
											try {
												Technician.SubmitOrder(orderTransfer);
											} catch (SQLException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
											orderTransfer.setOrderStatus("Imaging Complete");
											orderStatusOut.setText(orderTransfer.getOrderStatus());
											
										}//end nested if
										
										else if(orderTransfer.getImages().size() == 0) {
											
											testLabel.setText("No images on order");
										}//end nested else
										
									}//end if
									
								}//end Action
								
								
							};//end submitListener
						
						addImageButton.addActionListener(imageListener);
						viewImageButton.addActionListener(imageListener);
						deleteImageButton.addActionListener(imageListener);
						submitOrderButton.addActionListener(submitListener);
						
					}//end Action
				};//end detailsListener
				
				detailsButton.addActionListener(detailsListener);
				
			}//end Action
			
		};//end radioListener
		
		//Adds event listener for all action panel buttons, all used to change view panel and subAction panel contents. STILL NEED TO VIEW ORDERS FUNCTIONALITY
		ActionListener actionPanelListener = new ActionListener() {
			
			public void actionPerformed(ActionEvent click) {
				if (click.getSource() == homeButton) {
					//Setup home view, default
					
					subActionPanel.removeAll();
					viewPanel.removeAll();
					subActionPanel.repaint();
					subActionPanel.revalidate();
					viewPanel.repaint();
					viewPanel.revalidate();
					
				}//end homeButton source
				
				else if(click.getSource() == viewButton) {
					//Setup appointment view
					
					subActionPanel.removeAll();
					viewPanel.removeAll();
					
					buttonTracker.clear();
					orderTracker.clear();
					
					ArrayList<Order> appts = new ArrayList<Order>();
					appts.clear();
					
					try {
						appts.addAll(Technician.ViewAppointments());
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					int yValue = 10;
					int xValue = 20;
					
					//CREATE ARRAY FOR BUTTONS HERE
					
					//fills out view panel with radio buttons for each order with an appointment set for today's date.
					for(int i = 0; i < appts.size(); i++) {
						
						
						JRadioButton apptRdButton = new JRadioButton("Appt " + (i+1) + ": " + appts.get(i).getApptTime() + " Room: " + appts.get(i).getApptRoom() 
								+ " " + appts.get(i).getImagingOrder());
						
						buttonTracker.add(apptRdButton);
						orderTracker.add(appts.get(i));
						
						apptRdButton.setBounds(xValue, yValue, 250, 15);
						viewPanel.add(apptRdButton);
						apptRdButton.addActionListener(radioListener);
						radioButtonGroup.add(apptRdButton);
						
						yValue += 20;
						
					}//end for
					
					subActionPanel.repaint();
					subActionPanel.revalidate();
					viewPanel.repaint();
					viewPanel.revalidate();
					
					appts.clear();
					
				}//end viewButton source
				
				else if(click.getSource() == viewOrdersButton) {
					//Setup Order view
					
					subActionPanel.removeAll();
					viewPanel.removeAll();
					
					buttonTracker.clear();
					orderTracker.clear();
					
					ArrayList<Order> orders = new ArrayList<Order>();
					orders.clear();
					
					try {
						orders.addAll(Technician.ViewOrders());
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					int yValue = 10;
					int xValue = 20;					
					
					//fills out view panel with radio buttons for each order that has not been completed.
					for(int i = 0; i < orders.size(); i++) {
						
						JRadioButton orderRdButton = new JRadioButton("Order " + ": " + orders.get(i).getOrderID() + " Room: " + orders.get(i).getApptRoom() + " " + orders.get(i).getImagingOrder());
						orderRdButton.setBounds(xValue, yValue, 250, 15);
						viewPanel.add(orderRdButton);
						orderRdButton.addActionListener(radioListener);
						radioButtonGroup.add(orderRdButton);
						
						buttonTracker.add(orderRdButton);
						orderTracker.add(orders.get(i));
						
						yValue += 20;
						
					}//end for
					
					subActionPanel.repaint();
					subActionPanel.revalidate();
					viewPanel.repaint();
					viewPanel.revalidate();
					
					orders.clear();

				}//end viewOrdersButton source
				
			}//end actionPerformed
		};//end actionPanelListener
		
		homeButton.addActionListener(actionPanelListener);
		viewButton.addActionListener(actionPanelListener);
		viewOrdersButton.addActionListener(actionPanelListener);

	}//end initialize
}
