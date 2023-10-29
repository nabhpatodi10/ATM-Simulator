import java.sql.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class application {

    private static String jdbcURL = "jdbc:mysql://localhost:3306/";
    private static String dbName = "bank";
    private static String dbUser = "root";
    private static String dbPassword = "nabh1005";
    private static Connection connection;
    private static String account_number;

    private static JFrame frame = new JFrame("ATM");

    private static void createDatabase(Connection connection, String dbName) throws Exception {
        Statement createDbStatement = connection.createStatement();
        String createDbQuery = "CREATE DATABASE IF NOT EXISTS " + dbName;
        createDbStatement.executeUpdate(createDbQuery);
    }

    private static void createLoginTable(Connection connection) throws Exception {
        Statement createTableStatement = connection.createStatement();
        String createTableQuery = "CREATE TABLE IF NOT EXISTS login ( Account_Number VARBINARY(15) PRIMARY KEY, Pin VARCHAR(20) BINARY NOT NULL)";
        createTableStatement.executeUpdate(createTableQuery);
    }

    private static void createDetailsTable(Connection connection) throws Exception {
        Statement createTableStatement = connection.createStatement();
        String createTableQuery = "CREATE TABLE IF NOT EXISTS details ( Account_Number VARBINARY(15), Name VARCHAR(50), Address VARCHAR(300), Aadhar VARCHAR(12), FOREIGN KEY (Account_Number) REFERENCES login(Account_Number) )";
        createTableStatement.executeUpdate(createTableQuery);
    }

    private static void createTransactionsTable(Connection connection) throws Exception {
        Statement createTableStatement = connection.createStatement();
        String createTableQuery = "CREATE TABLE IF NOT EXISTS transactions ( Account_Number VARBINARY(15), Action VARCHAR(8), Amount int, FOREIGN KEY (Account_Number) REFERENCES login(Account_Number) )";
        createTableStatement.executeUpdate(createTableQuery);
    }

    private static void createBalanceTable(Connection connection) throws Exception {
        Statement createTableStatement = connection.createStatement();
        String createTableQuery = "CREATE TABLE IF NOT EXISTS account_balance ( Account_Number VARBINARY(15), Balance int, FOREIGN KEY (Account_Number) REFERENCES login(Account_Number) )";
        createTableStatement.executeUpdate(createTableQuery);
    }

    private static void connectionfunc(){
        try{
            connection = DriverManager.getConnection(jdbcURL, dbUser, dbPassword);
            createDatabase(connection, dbName);
            connection.setCatalog(dbName);
            createLoginTable(connection);
            createDetailsTable(connection);
            createTransactionsTable(connection);
            createBalanceTable(connection);
        }catch(SQLException e){
            e.printStackTrace();
        }catch(Exception e1){
            e1.printStackTrace();
        }
    }

    private static boolean isLoginTableEmpty(Connection connection) throws Exception {
        String query = "SELECT COUNT(*) FROM login";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        resultSet.next();
        int count = resultSet.getInt(1);
        return count == 0;
    }

    private static void details_page(){

        frame.getContentPane().removeAll();
        frame.setLayout(null);
        frame.validate();
        frame.repaint();

        JLabel heading = new JLabel("ATM");
        heading.setVerticalAlignment(JLabel.CENTER);
        heading.setHorizontalAlignment(JLabel.CENTER);
        heading.setFont(new Font("Franklin Gothic Book", Font.PLAIN, 50));
        heading.setBounds(0, 0, 1000, 60);

        JLabel name = new JLabel("Name:");
        name.setVerticalAlignment(JLabel.CENTER);
        name.setHorizontalAlignment(JLabel.RIGHT);
        name.setFont(new Font("Franklin Gothic Book", Font.PLAIN, 20));
        name.setBounds(0, 130, 495, 25);

        JLabel address = new JLabel("Address:");
        address.setVerticalAlignment(JLabel.CENTER);
        address.setHorizontalAlignment(JLabel.RIGHT);
        address.setFont(new Font("Franklin Gothic Book", Font.PLAIN, 20));
        address.setBounds(0, 170, 495, 25);

        JLabel aadhar = new JLabel("Aadhar:");
        aadhar.setVerticalAlignment(JLabel.CENTER);
        aadhar.setHorizontalAlignment(JLabel.RIGHT);
        aadhar.setFont(new Font("Franklin Gothic Book", Font.PLAIN, 20));
        aadhar.setBounds(0, 210, 495, 25);

        JLabel accno = new JLabel("Account Number:");
        accno.setVerticalAlignment(JLabel.CENTER);
        accno.setHorizontalAlignment(JLabel.RIGHT);
        accno.setFont(new Font("Franklin Gothic Book", Font.PLAIN, 20));
        accno.setBounds(0, 250, 495, 25);

        JLabel accbal = new JLabel("Account Balance:");
        accbal.setVerticalAlignment(JLabel.CENTER);
        accbal.setHorizontalAlignment(JLabel.RIGHT);
        accbal.setFont(new Font("Franklin Gothic Book", Font.PLAIN, 20));
        accbal.setBounds(0, 290, 495, 25);

        JLabel pin = new JLabel("Pin:");
        pin.setVerticalAlignment(JLabel.CENTER);
        pin.setHorizontalAlignment(JLabel.RIGHT);
        pin.setFont(new Font("Franklin Gothic Book", Font.PLAIN, 20));
        pin.setBounds(0, 330, 495, 25);

        JTextField nametext = new JTextField();
        nametext.setFont(new Font("Franklin Gothic Book", Font.PLAIN, 20));
        nametext.setEditable(false);
        nametext.setBounds(510, 130, 200, 25);

        JTextField addresstext = new JTextField();
        addresstext.setFont(new Font("Franklin Gothic Book", Font.PLAIN, 20));
        addresstext.setEditable(false);
        addresstext.setBounds(510, 170, 200, 25);

        JTextField aadhartext = new JTextField();
        aadhartext.setFont(new Font("Franklin Gothic Book", Font.PLAIN, 20));
        aadhartext.setEditable(false);
        aadhartext.setBounds(510, 210, 200, 25);

        JTextField accnotext = new JTextField();
        accnotext.setFont(new Font("Franklin Gothic Book", Font.PLAIN, 20));
        accnotext.setEditable(false);
        accnotext.setBounds(510, 250, 200, 25);

        JTextField accbaltext = new JTextField();
        accbaltext.setFont(new Font("Franklin Gothic Book", Font.PLAIN, 20));
        accbaltext.setEditable(false);
        accbaltext.setBounds(510, 290, 200, 25);

        JTextField pintext = new JTextField();
        pintext.setFont(new Font("Franklin Gothic Book", Font.PLAIN, 20));
        pintext.setEditable(false);
        pintext.setBounds(510, 330, 200, 25);

        try{
            String query = "SELECT * FROM login NATURAL JOIN details NATURAL JOIN account_balance WHERE Account_Number = '" + account_number + "'";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            resultSet.next();
            nametext.setText(resultSet.getString("Name"));
            addresstext.setText(resultSet.getString("Address"));
            aadhartext.setText(resultSet.getString("Aadhar"));
            accnotext.setText(resultSet.getString("Account_Number"));
            accbaltext.setText(resultSet.getString("Balance"));
            pintext.setText(resultSet.getString("Pin"));
        }catch(SQLException e1){
            e1.printStackTrace();
            JOptionPane.showMessageDialog(null, "Details not Found!!!", "Error!", JOptionPane.ERROR_MESSAGE);
        }catch(Exception e2){
            e2.printStackTrace();
            JOptionPane.showMessageDialog(null, "Details not Found!!!", "Error!", JOptionPane.ERROR_MESSAGE);
        }

        JButton signupbutton = new JButton("Home Page");
        signupbutton.setFocusable(false);
        signupbutton.setFont(new Font("Franklin Gothic Book", Font.PLAIN, 20));
        signupbutton.setBounds(415, 390, 170, 25);
        signupbutton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                home_page();
            }
        });

        frame.add(heading);
        frame.add(name);
        frame.add(address);
        frame.add(aadhar);
        frame.add(accno);
        frame.add(accbal);
        frame.add(pin);
        frame.add(nametext);
        frame.add(addresstext);
        frame.add(aadhartext);
        frame.add(accnotext);
        frame.add(accbaltext);
        frame.add(pintext);
        frame.add(signupbutton);

        frame.setVisible(true);

        frame.validate();
        frame.repaint();

    }

    private static void change_pin_page(){

        frame.getContentPane().removeAll();
        frame.setLayout(null);
        frame.validate();
        frame.repaint();

        JLabel heading = new JLabel("ATM");
        heading.setVerticalAlignment(JLabel.CENTER);
        heading.setHorizontalAlignment(JLabel.CENTER);
        heading.setFont(new Font("Franklin Gothic Book", Font.PLAIN, 50));
        heading.setBounds(0, 0, 1000, 60);

        JLabel oldpin = new JLabel("Old Pin:");
        oldpin.setVerticalAlignment(JLabel.CENTER);
        oldpin.setHorizontalAlignment(JLabel.RIGHT);
        oldpin.setFont(new Font("Franklin Gothic Book", Font.PLAIN, 20));
        oldpin.setBounds(0, 170, 495, 25);

        JLabel newpin = new JLabel("New Pin:");
        newpin.setVerticalAlignment(JLabel.CENTER);
        newpin.setHorizontalAlignment(JLabel.RIGHT);
        newpin.setFont(new Font("Franklin Gothic Book", Font.PLAIN, 20));
        newpin.setBounds(0, 210, 495, 25);

        JLabel confirmpin = new JLabel("Confirm Pin:");
        confirmpin.setVerticalAlignment(JLabel.CENTER);
        confirmpin.setHorizontalAlignment(JLabel.RIGHT);
        confirmpin.setFont(new Font("Franklin Gothic Book", Font.PLAIN, 20));
        confirmpin.setBounds(0, 250, 495, 25);

        JPasswordField oldpintext = new JPasswordField();
        oldpintext.setFont(new Font("Franklin Gothic Book", Font.PLAIN, 20));
        oldpintext.setEchoChar('*');
        oldpintext.setBounds(510, 170, 200, 25);

        JPasswordField newpintext = new JPasswordField();
        newpintext.setFont(new Font("Franklin Gothic Book", Font.PLAIN, 20));
        newpintext.setEchoChar('*');
        newpintext.setBounds(510, 210, 200, 25);

        JPasswordField confirmpintext = new JPasswordField();
        confirmpintext.setFont(new Font("Franklin Gothic Book", Font.PLAIN, 20));
        confirmpintext.setEchoChar('*');
        confirmpintext.setBounds(510, 250, 200, 25);

        JButton changepin = new JButton("Change PIN");
        changepin.setFocusable(false);
        changepin.setFont(new Font("Franklin Gothic Book", Font.PLAIN, 20));
        changepin.setBounds(415, 310, 170, 25);
        changepin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                try{

                    String query = "SELECT * FROM login WHERE Account_Number = '" + account_number + "' AND Pin = '" + String.valueOf(oldpintext.getPassword()) + "'";
                    Statement statement = connection.createStatement();
                    ResultSet resultSet = statement.executeQuery(query);
                    int count = 0;
                    while (resultSet.next()) {
                        count++;
                    }
                    if(count!=0){
                        if(String.valueOf(newpintext.getPassword()).equals(String.valueOf(confirmpintext.getPassword()))){
                            String updatepin = "UPDATE login SET Pin = '" + String.valueOf(newpintext.getPassword()) + "'";
                            statement.execute(updatepin);
                            JOptionPane.showMessageDialog(null, "Pin Changed", "Information", JOptionPane.INFORMATION_MESSAGE);
                            home_page();
                        }else{
                            JOptionPane.showMessageDialog(null, "Check the New Pin!!!", "Error!", JOptionPane.ERROR_MESSAGE);
                        }
                    }else{
                        JOptionPane.showMessageDialog(null, "Incorrect Credintials!!!", "Error!", JOptionPane.ERROR_MESSAGE);
                    }
                }catch(SQLException e1){
                    e1.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Could not Change the Pin", "Error!", JOptionPane.ERROR_MESSAGE);
                }catch(Exception e2){
                    e2.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Could not Change the Pin", "Error!", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        frame.add(heading);
        frame.add(oldpin);
        frame.add(newpin);
        frame.add(confirmpin);
        frame.add(oldpintext);
        frame.add(newpintext);
        frame.add(confirmpintext);
        frame.add(changepin);

        frame.setVisible(true);

        frame.validate();
        frame.repaint();

    }

    private static void withdraw_page(){

        frame.getContentPane().removeAll();
        frame.setLayout(null);
        frame.validate();
        frame.repaint();

        JLabel heading = new JLabel("ATM");
        heading.setVerticalAlignment(JLabel.CENTER);
        heading.setHorizontalAlignment(JLabel.CENTER);
        heading.setFont(new Font("Franklin Gothic Book", Font.PLAIN, 50));
        heading.setBounds(0, 0, 1000, 60);

        JLabel ammount = new JLabel("Ammount:");
        ammount.setVerticalAlignment(JLabel.CENTER);
        ammount.setHorizontalAlignment(JLabel.RIGHT);
        ammount.setFont(new Font("Franklin Gothic Book", Font.PLAIN, 20));
        ammount.setBounds(0, 170, 495, 25);

        JLabel pin = new JLabel("Pin:");
        pin.setVerticalAlignment(JLabel.CENTER);
        pin.setHorizontalAlignment(JLabel.RIGHT);
        pin.setFont(new Font("Franklin Gothic Book", Font.PLAIN, 20));
        pin.setBounds(0, 210, 495, 25);

        JTextField ammounttext = new JTextField();
        ammounttext.setFont(new Font("Franklin Gothic Book", Font.PLAIN, 20));
        ammounttext.setBounds(510, 170, 200, 25);

        JPasswordField pintext = new JPasswordField();
        pintext.setFont(new Font("Franklin Gothic Book", Font.PLAIN, 20));
        pintext.setEchoChar('*');
        pintext.setBounds(510, 210, 200, 25);

        JButton withdraw = new JButton("Withdraw");
        withdraw.setFocusable(false);
        withdraw.setFont(new Font("Franklin Gothic Book", Font.PLAIN, 20));
        withdraw.setBounds(415, 300, 170, 25);
        withdraw.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){

                try{

                    String query = "SELECT * FROM login WHERE Account_Number = '" + account_number + "' AND Pin = '" + String.valueOf(pintext.getPassword()) + "'";
                    Statement statement = connection.createStatement();
                    ResultSet resultSet = statement.executeQuery(query);
                    int count = 0;
                    while (resultSet.next()) {
                        count++;
                    }
                    if(count!=0){
                        String checkquery = "SELECT Balance from account_balance WHERE Account_Number = '" + account_number + "'";
                        ResultSet checkResultSet = statement.executeQuery(checkquery);
                        checkResultSet.next();
                        if(checkResultSet.getInt("Balance")>=Integer.valueOf(ammounttext.getText())){
                            String transactionsquery = "INSERT INTO transactions VALUES ('" + account_number + "', 'Withdraw', " + ammounttext.getText() + ", CURDATE(), CURTIME() )";
                            statement.execute(transactionsquery);
                            String balancequery = "SELECT Balance from account_balance WHERE Account_Number = '" + account_number + "'";
                            ResultSet balancResultSet = statement.executeQuery(balancequery);
                            balancResultSet.next();
                            int currbalance = balancResultSet.getInt("Balance");
                            String updatebalance = "UPDATE account_balance SET Balance = " + (currbalance - Integer.valueOf(ammounttext.getText())) + " WHERE Account_Number = '" + account_number + "'";
                            statement.execute(updatebalance);
                            JOptionPane.showMessageDialog(null, "Transaction Processed", "Information", JOptionPane.INFORMATION_MESSAGE);
                            home_page();
                        }else{
                            JOptionPane.showMessageDialog(null, "Insufficient Balance!!!", "Error!", JOptionPane.ERROR_MESSAGE);
                        }
                    }else{
                        JOptionPane.showMessageDialog(null, "Incorrect Credintials!!!", "Error!", JOptionPane.ERROR_MESSAGE);
                    }
                }catch(SQLException e1){
                    e1.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Could not process the Transaction", "Error!", JOptionPane.ERROR_MESSAGE);
                }catch(Exception e2){
                    e2.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Could not process the Transaction", "Error!", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        frame.add(heading);
        frame.add(ammount);
        frame.add(pin);
        frame.add(ammounttext);
        frame.add(pintext);
        frame.add(withdraw);

        frame.setVisible(true);

        frame.validate();
        frame.repaint();

    }

    private static void deposit_page(){

        frame.getContentPane().removeAll();
        frame.setLayout(null);
        frame.validate();
        frame.repaint();

        JLabel heading = new JLabel("ATM");
        heading.setVerticalAlignment(JLabel.CENTER);
        heading.setHorizontalAlignment(JLabel.CENTER);
        heading.setFont(new Font("Franklin Gothic Book", Font.PLAIN, 50));
        heading.setBounds(0, 0, 1000, 60);

        JLabel ammount = new JLabel("Ammount:");
        ammount.setVerticalAlignment(JLabel.CENTER);
        ammount.setHorizontalAlignment(JLabel.RIGHT);
        ammount.setFont(new Font("Franklin Gothic Book", Font.PLAIN, 20));
        ammount.setBounds(0, 170, 495, 25);

        JLabel pin = new JLabel("Pin:");
        pin.setVerticalAlignment(JLabel.CENTER);
        pin.setHorizontalAlignment(JLabel.RIGHT);
        pin.setFont(new Font("Franklin Gothic Book", Font.PLAIN, 20));
        pin.setBounds(0, 210, 495, 25);

        JTextField ammounttext = new JTextField();
        ammounttext.setFont(new Font("Franklin Gothic Book", Font.PLAIN, 20));
        ammounttext.setBounds(510, 170, 200, 25);

        JPasswordField pintext = new JPasswordField();
        pintext.setFont(new Font("Franklin Gothic Book", Font.PLAIN, 20));
        pintext.setEchoChar('*');
        pintext.setBounds(510, 210, 200, 25);

        JButton deposit = new JButton("Deposit");
        deposit.setFocusable(false);
        deposit.setFont(new Font("Franklin Gothic Book", Font.PLAIN, 20));
        deposit.setBounds(415, 300, 170, 25);
        deposit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){

                try{

                    String query = "SELECT * FROM login WHERE Account_Number = '" + account_number + "' AND Pin = '" + String.valueOf(pintext.getPassword()) + "'";
                    Statement statement = connection.createStatement();
                    ResultSet resultSet = statement.executeQuery(query);
                    int count = 0;
                    while (resultSet.next()) {
                        count++;
                    }
                    if(count!=0){
                        String transactionsquery = "INSERT INTO transactions VALUES ('" + account_number + "', 'Deposit', " + ammounttext.getText() + ", CURDATE(), CURTIME() )";
                        statement.execute(transactionsquery);
                        String balancequery = "SELECT Balance from account_balance WHERE Account_Number = '" + account_number + "'";
                        ResultSet balancResultSet = statement.executeQuery(balancequery);
                        balancResultSet.next();
                        int currbalance = balancResultSet.getInt("Balance");
                        String updatebalance = "UPDATE account_balance SET Balance = " + (currbalance + Integer.valueOf(ammounttext.getText())) + " WHERE Account_Number = '" + account_number + "'";
                        statement.execute(updatebalance);
                        JOptionPane.showMessageDialog(null, "Transaction Processed", "Information", JOptionPane.INFORMATION_MESSAGE);
                        home_page();
                    }else{
                        JOptionPane.showMessageDialog(null, "Incorrect Credintials!!!", "Error!", JOptionPane.ERROR_MESSAGE);
                    }
                }catch(SQLException e1){
                    e1.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Could not process the Transaction", "Error!", JOptionPane.ERROR_MESSAGE);
                }catch(Exception e2){
                    e2.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Could not process the Transaction", "Error!", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        frame.add(heading);
        frame.add(ammount);
        frame.add(pin);
        frame.add(ammounttext);
        frame.add(pintext);
        frame.add(deposit);

        frame.setVisible(true);

        frame.validate();
        frame.repaint();
        
    }

    private static void home_page(){

        frame.getContentPane().removeAll();
        frame.setLayout(null);
        frame.validate();
        frame.repaint();

        JLabel heading = new JLabel("ATM");
        heading.setVerticalAlignment(JLabel.CENTER);
        heading.setHorizontalAlignment(JLabel.CENTER);
        heading.setFont(new Font("Franklin Gothic Book", Font.PLAIN, 50));
        heading.setBounds(0, 0, 1000, 60);

        JButton deposit = new JButton("Deposit");
        deposit.setFocusable(false);
        deposit.setFont(new Font("Franklin Gothic Book", Font.PLAIN, 20));
        deposit.setBounds(415, 150, 170, 25);
        deposit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                deposit_page();
            }
        });

        JButton withdraw = new JButton("Withdraw");
        withdraw.setFocusable(false);
        withdraw.setFont(new Font("Franklin Gothic Book", Font.PLAIN, 20));
        withdraw.setBounds(415, 190, 170, 25);
        withdraw.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                withdraw_page();
            }
        });

        JButton changepin = new JButton("Change PIN");
        changepin.setFocusable(false);
        changepin.setFont(new Font("Franklin Gothic Book", Font.PLAIN, 20));
        changepin.setBounds(415, 230, 170, 25);
        changepin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                change_pin_page();
            }
        });

        JButton accountdetails = new JButton("Account Details");
        accountdetails.setFocusable(false);
        accountdetails.setFont(new Font("Franklin Gothic Book", Font.PLAIN, 20));
        accountdetails.setBounds(415, 270, 170, 25);
        accountdetails.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                details_page();
            }
        });

        JButton logout = new JButton("Logout");
        logout.setFocusable(false);
        logout.setFont(new Font("Franklin Gothic Book", Font.PLAIN, 20));
        logout.setBounds(415, 310, 170, 25);
        logout.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                login_page();
            }
        });

        frame.add(heading);
        frame.add(deposit);
        frame.add(withdraw);
        frame.add(changepin);
        frame.add(accountdetails);
        frame.add(logout);

        frame.setVisible(true);

        frame.validate();
        frame.repaint();

    }

    private static void signup_page(){

        frame.getContentPane().removeAll();
        frame.setLayout(null);
        frame.validate();
        frame.repaint();

        JLabel heading = new JLabel("ATM");
        heading.setVerticalAlignment(JLabel.CENTER);
        heading.setHorizontalAlignment(JLabel.CENTER);
        heading.setFont(new Font("Franklin Gothic Book", Font.PLAIN, 50));
        heading.setBounds(0, 0, 1000, 60);

        JLabel name = new JLabel("Name:");
        name.setVerticalAlignment(JLabel.CENTER);
        name.setHorizontalAlignment(JLabel.RIGHT);
        name.setFont(new Font("Franklin Gothic Book", Font.PLAIN, 20));
        name.setBounds(0, 130, 495, 25);

        JLabel address = new JLabel("Address:");
        address.setVerticalAlignment(JLabel.CENTER);
        address.setHorizontalAlignment(JLabel.RIGHT);
        address.setFont(new Font("Franklin Gothic Book", Font.PLAIN, 20));
        address.setBounds(0, 170, 495, 25);

        JLabel aadhar = new JLabel("Aadhar:");
        aadhar.setVerticalAlignment(JLabel.CENTER);
        aadhar.setHorizontalAlignment(JLabel.RIGHT);
        aadhar.setFont(new Font("Franklin Gothic Book", Font.PLAIN, 20));
        aadhar.setBounds(0, 210, 495, 25);

        JLabel accno = new JLabel("Account Number:");
        accno.setVerticalAlignment(JLabel.CENTER);
        accno.setHorizontalAlignment(JLabel.RIGHT);
        accno.setFont(new Font("Franklin Gothic Book", Font.PLAIN, 20));
        accno.setBounds(0, 250, 495, 25);

        JLabel pin = new JLabel("Pin:");
        pin.setVerticalAlignment(JLabel.CENTER);
        pin.setHorizontalAlignment(JLabel.RIGHT);
        pin.setFont(new Font("Franklin Gothic Book", Font.PLAIN, 20));
        pin.setBounds(0, 290, 495, 25);

        JTextField nametext = new JTextField();
        nametext.setFont(new Font("Franklin Gothic Book", Font.PLAIN, 20));
        nametext.setBounds(510, 130, 200, 25);

        JTextField addresstext = new JTextField();
        addresstext.setFont(new Font("Franklin Gothic Book", Font.PLAIN, 20));
        addresstext.setBounds(510, 170, 200, 25);

        JTextField aadhartext = new JTextField();
        aadhartext.setFont(new Font("Franklin Gothic Book", Font.PLAIN, 20));
        aadhartext.setBounds(510, 210, 200, 25);

        JTextField accnotext = new JTextField();
        accnotext.setFont(new Font("Franklin Gothic Book", Font.PLAIN, 20));
        accnotext.setBounds(510, 250, 200, 25);

        JPasswordField pintext = new JPasswordField();
        pintext.setFont(new Font("Franklin Gothic Book", Font.PLAIN, 20));
        pintext.setEchoChar('*');
        pintext.setBounds(510, 290, 200, 25);

        JButton signupbutton = new JButton("Sign Up");
        signupbutton.setFocusable(false);
        signupbutton.setFont(new Font("Franklin Gothic Book", Font.PLAIN, 20));
        signupbutton.setBounds(450, 350, 100, 25);
        signupbutton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                if(nametext.getText().length()!=0 && addresstext.getText().length()!=0 && aadhartext.getText().length()!=0 && accnotext.getText().length()!=0 && pintext.getPassword().length!=0){
                    try{
                        String query = "INSERT INTO login VALUES ('" + accnotext.getText() + "', '" + String.valueOf(pintext.getPassword()) +"')";
                        Statement statement = connection.createStatement();
                        statement.execute(query);
                        String detailsquery = "INSERT INTO details VALUES ('" + accnotext.getText() + "', '" + nametext.getText() + "', '" + addresstext.getText() + "', '" + aadhartext.getText() + "')";
                        statement.execute(detailsquery);
                        String balancequery = "INSERT INTO account_balance VALUES ('" + accnotext.getText() + "', 0)";
                        statement.execute(balancequery);
                        JOptionPane.showMessageDialog(null, "Details Added", "Information", JOptionPane.INFORMATION_MESSAGE);
                        login_page();
                    }catch(SQLException e1){
                        JOptionPane.showMessageDialog(null, "Could not add Details!!!", "Error!", JOptionPane.ERROR_MESSAGE);
                    }catch(Exception e2){
                        JOptionPane.showMessageDialog(null, "Could not add Details!!!", "Error!", JOptionPane.ERROR_MESSAGE);
                    }
                }else{
                    JOptionPane.showMessageDialog(null, "Enter Valid Details!!!", "Error!", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        frame.add(heading);
        frame.add(name);
        frame.add(address);
        frame.add(aadhar);
        frame.add(accno);
        frame.add(pin);
        frame.add(nametext);
        frame.add(addresstext);
        frame.add(aadhartext);
        frame.add(accnotext);
        frame.add(pintext);
        frame.add(signupbutton);

        frame.setVisible(true);

        frame.validate();
        frame.repaint();

    }

    private static void login_page(){

        frame.getContentPane().removeAll();
        frame.setLayout(null);
        frame.validate();
        frame.repaint();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setSize(1000, 750);

        JLabel heading = new JLabel("ATM");
        heading.setVerticalAlignment(JLabel.CENTER);
        heading.setHorizontalAlignment(JLabel.CENTER);
        heading.setFont(new Font("Franklin Gothic Book", Font.PLAIN, 50));
        heading.setBounds(0, 0, 1000, 60);

        JLabel accno = new JLabel("Account Number:");
        accno.setVerticalAlignment(JLabel.CENTER);
        accno.setHorizontalAlignment(JLabel.RIGHT);
        accno.setFont(new Font("Franklin Gothic Book", Font.PLAIN, 20));
        accno.setBounds(0, 250, 495, 25);

        JLabel pin = new JLabel("Pin:");
        pin.setVerticalAlignment(JLabel.CENTER);
        pin.setHorizontalAlignment(JLabel.RIGHT);
        pin.setFont(new Font("Franklin Gothic Book", Font.PLAIN, 20));
        pin.setBounds(0, 290, 495, 25);

        JTextField accnotext = new JTextField();
        accnotext.setFont(new Font("Franklin Gothic Book", Font.PLAIN, 20));
        accnotext.setBounds(510, 250, 200, 25);

        JPasswordField pintext = new JPasswordField();
        pintext.setFont(new Font("Franklin Gothic Book", Font.PLAIN, 20));
        pintext.setEchoChar('*');
        pintext.setBounds(510, 290, 200, 25);

        JButton loginbutton = new JButton("Login");
        loginbutton.setFocusable(false);
        loginbutton.setFont(new Font("Franklin Gothic Book", Font.PLAIN, 20));
        loginbutton.setBounds(515, 350, 100, 25);
        loginbutton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){

                try{

                    if(!isLoginTableEmpty(connection)){

                        String query = "SELECT * FROM login WHERE Account_Number = '" + accnotext.getText() + "' AND Pin = '" + String.valueOf(pintext.getPassword()) + "'";
                        Statement statement = connection.createStatement();
                        ResultSet resultSet = statement.executeQuery(query);
                        int count = 0;
                        while (resultSet.next()) {
                            count++;
                        }
                        if(count!=0){
                            account_number = accnotext.getText();
                            home_page();
                        }else{
                            JOptionPane.showMessageDialog(null, "Incorrect Credintials!!!", "Error!", JOptionPane.ERROR_MESSAGE);
                        }
                    }else{
                        JOptionPane.showMessageDialog(null, "No User Found!!!", "Error!", JOptionPane.ERROR_MESSAGE);
                    }
                }catch(SQLException e1){
                    e1.printStackTrace();
                    JOptionPane.showMessageDialog(null, "No User Found!!!", "Error!", JOptionPane.ERROR_MESSAGE);
                }catch(Exception e2){
                    e2.printStackTrace();
                    JOptionPane.showMessageDialog(null, "No User Found!!!", "Error!", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JButton signupbutton = new JButton("Sign Up");
        signupbutton.setFocusable(false);
        signupbutton.setFont(new Font("Franklin Gothic Book", Font.PLAIN, 20));
        signupbutton.setBounds(385, 350, 100, 25);
        signupbutton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                signup_page();
            }
        });

        frame.add(heading);
        frame.add(accno);
        frame.add(pin);
        frame.add(accnotext);
        frame.add(pintext);
        frame.add(loginbutton);
        frame.add(signupbutton);

        frame.setVisible(true);

        frame.validate();
        frame.repaint();

    }

    protected void finalize(){
        try{
            connection.close();
        }catch(SQLException e){
            e.printStackTrace();
        }catch(Exception e1){
            e1.printStackTrace();
        }
    }

    public static void main(String[] args){
        connectionfunc();
        login_page();
    }
    
}