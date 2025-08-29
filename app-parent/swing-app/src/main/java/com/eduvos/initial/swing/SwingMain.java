package com.eduvos.initial.swing;

import com.eduvos.initial.core.GreetingService;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;

public class SwingMain {
	public static void main(String[] args) {
		GreetingService service = new GreetingService();
		String message = service.greet(null);

		JFrame frame = new JFrame("Eduvos Initial Project");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JLabel label = new JLabel(message, SwingConstants.CENTER);
		frame.getContentPane().add(label, BorderLayout.CENTER);
		frame.setSize(400, 200);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
}
