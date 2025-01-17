import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonClickCounter {

    public static void main(String[] args) {
        // Create the frame for the window
        JFrame frame = new JFrame("Button Click Counter");
        frame.setSize(300, 200); // Set the size of the window
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

        // Create a button and a label to display the click count
        JButton button = new JButton("Click Me!");
        button.setBounds(100, 50, 100, 30); // Set the position and size of the button
        
        JLabel label = new JLabel("Button clicked: 0 times");
        label.setBounds(80, 100, 200, 30); // Set the position of the label

        // Create a counter variable
        final int[] clickCount = {0};

        // Add an ActionListener to the button
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clickCount[0]++; // Increment the click counter
                label.setText("Button clicked: " + clickCount[0] + " times"); // Update the label text
            }
        });

        // Add the button and label to the frame
        frame.add(button);
        frame.add(label);

        // Make the frame visible
        frame.setVisible(true);
    }
}
