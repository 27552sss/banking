package bank.management.system;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Pin extends JFrame implements ActionListener {
    JButton b1,b2;
    JPasswordField p1,p2;
    String pin;
    Pin(String pin){
        this.pin =pin;
        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icon/atm2.png"));
        Image i2 = i1.getImage().getScaledInstance(1550,830,Image.SCALE_DEFAULT);
        ImageIcon i3 = new ImageIcon(i2);
        JLabel l3 = new JLabel(i3);
        l3.setBounds(0,0,1550,830);
        add(l3);
        JLabel label1 = new JLabel("CHANGE YOUR PIN");
        label1.setForeground(Color.WHITE);
        label1.setFont(new Font("System", Font.BOLD, 16));
        label1.setBounds(430,180,400,35);
        l3.add(label1);
        JLabel label2 = new JLabel("New PIN: ");
        label2.setForeground(Color.WHITE);
        label2.setFont(new Font("System", Font.BOLD, 16));
        label2.setBounds(430,220,150,35);
        l3.add(label2);
        p1 = new JPasswordField();
        p1.setBackground(new Color(65,125,128));
        p1.setForeground(Color.WHITE);
        p1.setBounds(600,220,180,25);
        p1.setFont(new Font("Raleway", Font.BOLD,22));
        l3.add(p1);
        JLabel label3 = new JLabel("Re-Enter New PIN: ");
        label3.setForeground(Color.WHITE);
        label3.setFont(new Font("System", Font.BOLD, 16));
        label3.setBounds(430,250,400,35);
        l3.add(label3);
        p2 = new JPasswordField();
        p2.setBackground(new Color(65,125,128));
        p2.setForeground(Color.WHITE);
        p2.setBounds(600,255,180,25);
        p2.setFont(new Font("Raleway", Font.BOLD,22));
        l3.add(p2);
        b1 = new JButton("CHANGE");
        b1.setBounds(700,362,150,35);
        b1.setBackground(new Color(65,125,128));
        b1.setForeground(Color.WHITE);
        b1.addActionListener(this);
        l3.add(b1);
        b2 = new JButton("BACK");
        b2.setBounds(700,406,150,35);
        b2.setBackground(new Color(65,125,128));
        b2.setForeground(Color.WHITE);
        b2.addActionListener(this);
        l3.add(b2);
        setSize(1550,1080);
        setLayout(null);
        setLocation(0,0);
        setVisible(true);
    }

    @SuppressWarnings("deprecation")
    @Override
public void actionPerformed(ActionEvent e) {
    try {
        String pin1 = p1.getText();
        String pin2 = p2.getText();

        if (!pin1.equals(pin2)){
            JOptionPane.showMessageDialog(null,"Entered PIN does not match");
            return;
        }
        
        if (e.getSource()==b1){
            if (p1.getText().equals("")){
                JOptionPane.showMessageDialog(null,"Enter New PIN");
                return;
            }
            if (p2.getText().equals("")){
                JOptionPane.showMessageDialog(null,"Re-Enter New PIN");
                return;
            }

            Connn c = new Connn();
            
            // First get the card number and form_no for this PIN
            String getCardQuery = "SELECT card_number, form_no FROM login WHERE pin = ?";
            PreparedStatement psGet = c.getConnection().prepareStatement(getCardQuery);
            psGet.setString(1, pin);
            ResultSet rs = psGet.executeQuery();
            
            if(rs.next()) {
                String cardNumber = rs.getString("card_number");
                String formNo = rs.getString("form_no");
                
                // Now update all tables using both card_number and form_no for accuracy
                String q1 = "UPDATE bank SET pin = ? WHERE pin = ?";
                String q2 = "UPDATE login SET pin = ? WHERE card_number = ? AND form_no = ?";
                String q3 = "UPDATE signupthree SET pin = ? WHERE from_no = ? AND card_number = ?";

                // Update bank table
                PreparedStatement ps1 = c.getConnection().prepareStatement(q1);
                ps1.setString(1, pin1);
                ps1.setString(2, pin);
                ps1.executeUpdate();

                // Update login table
                PreparedStatement ps2 = c.getConnection().prepareStatement(q2);
                ps2.setString(1, pin1);
                ps2.setString(2, cardNumber);
                ps2.setString(3, formNo);
                ps2.executeUpdate();

                // Update signupthree table
                PreparedStatement ps3 = c.getConnection().prepareStatement(q3);
                ps3.setString(1, pin1);
                ps3.setString(2, formNo);
                ps3.setString(3, cardNumber);
                ps3.executeUpdate();

                JOptionPane.showMessageDialog(null,"PIN changed successfully");
                setVisible(false);
                new main_Class(pin1);  // Pass the new PIN

            } else {
                JOptionPane.showMessageDialog(null,"Could not find account. Please try again.");
            }

        } else if (e.getSource()==b2) {
            new main_Class(pin);
            setVisible(false);
        }

    } catch (Exception E) {
        E.printStackTrace();
        JOptionPane.showMessageDialog(null,"Error changing PIN. Please try again.");
    }
}    public static void main(String[] args) {
        new Pin("");
    }
}
