package XamineRIS;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.FlowLayout;
import java.awt.Graphics;

import javax.swing.JLabel;

public class ImageViewer extends JFrame {

	JPanel contentPane;
	JPanel viewImagePanel = new JPanel();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ImageViewer frame = new ImageViewer();
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
	public ImageViewer() {
		setDefaultCloseOperation(ImageViewer.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 1204, 512);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		viewImagePanel.setBounds(234, 0, 410, 420);
		contentPane.add(viewImagePanel);
		viewImagePanel.setLayout(null);
		

		

	}
	
	public void paintComponent(Graphics g) {}
}
