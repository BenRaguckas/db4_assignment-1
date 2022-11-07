package com.ben.view;

import java.awt.*;
import java.awt.event.*;
import java.io.FileWriter;
import java.io.PrintWriter;
import javax.swing.*;
import javax.swing.border.*;
import java.sql.*;

public class JDBCMainWindowContent extends JInternalFrame implements ActionListener {
    private final int
            INPUT_WIDTH = 1,
            EXPORT_WIDTH = 12;
    private final Dimension
            CRUD_DIM = new Dimension(360, 400),
            EXPORT_DIM = new Dimension(500, 200),
            CONTENT_DIM = new Dimension(1000, 400);
    private final GridLayout
            CRUD_LAYOUT = new GridLayout(13, 2),
            EXPORT_LAYOUT = new GridLayout(3, 2);

    String cmd = null;
    private Connection con = null;
    private Statement stmt = null;
    private ResultSet rs = null;
    private Container content;
    private JPanel detailsPanel, exportButtonPanel;
    //private JPanel exportConceptDataPanel;
    private JScrollPane dbContentsPanel;

    private final JTextField
            IDTF = new JTextField(INPUT_WIDTH),
            AppNameTF = new JTextField(INPUT_WIDTH),
            CategoryTF = new JTextField(INPUT_WIDTH),
            RatingTF = new JTextField(INPUT_WIDTH),
            ReviewsTF = new JTextField(INPUT_WIDTH),
            SizeTF = new JTextField(INPUT_WIDTH),
            InstallsTF = new JTextField(INPUT_WIDTH),
            TypeTF = new JTextField(INPUT_WIDTH),
            PriceTF = new JTextField(INPUT_WIDTH),
            NumLecturesTF = new JTextField(EXPORT_WIDTH),
            avgAgeDepartmentTF = new JTextField(EXPORT_WIDTH),
            ContentTF = new JTextField(INPUT_WIDTH),
            GenresTF = new JTextField(INPUT_WIDTH),
            CVersionTF = new JTextField(INPUT_WIDTH),
            AVersionTF = new JTextField(INPUT_WIDTH);


    private static QueryTableModel TableModel = new QueryTableModel();
    //Add the models to JTabels
    private JTable TableofDBContents = new JTable(TableModel);
    private final JButton
            updateButton = new JButton("Update"),
            insertButton = new JButton("Insert"),
            exportButton = new JButton("Export"),
            deleteButton = new JButton("Delete"),
            clearButton = new JButton("Clear"),
            NumLectures = new JButton("NumLecturesForDepartment:"),
            avgAgeDepartment = new JButton("AvgAgeForDepartment"),
            ListAllDepartments = new JButton("ListAllDepartments"),
            ListAllPositions = new JButton("ListAllPositions");


    public JDBCMainWindowContent(String aTitle) {
        //setting up the GUI
        super(aTitle, false, false, false, false);
        setEnabled(true);

        //add the 'main' panel to the Internal Frame
        content = getContentPane();
        content.setLayout(null);
        content.setBackground(Color.lightGray);
        Border lineBorder = BorderFactory.createEtchedBorder(15, Color.red, Color.black);

        //setup details panel and add the components to it
        detailsPanel = new JPanel();
        detailsPanel.setLayout(CRUD_LAYOUT);
        detailsPanel.setBackground(Color.lightGray);
        detailsPanel.setBorder(BorderFactory.createTitledBorder(lineBorder, "CRUD Actions"));

        JLabel IDLabel = new JLabel("ID:");
        detailsPanel.add(IDLabel);
        detailsPanel.add(IDTF);
        JLabel firstNameLabel = new JLabel("App Name:");
        detailsPanel.add(firstNameLabel);
        detailsPanel.add(AppNameTF);
        JLabel lastNameLabel = new JLabel("Category:");
        detailsPanel.add(lastNameLabel);
        detailsPanel.add(CategoryTF);
        JLabel ageLabel = new JLabel("Rating:");
        detailsPanel.add(ageLabel);
        detailsPanel.add(RatingTF);
        JLabel genderLabel = new JLabel("Reviews:");
        detailsPanel.add(genderLabel);
        detailsPanel.add(ReviewsTF);
        JLabel positionLabel = new JLabel("Size:");
        detailsPanel.add(positionLabel);
        detailsPanel.add(SizeTF);
        JLabel departmentLabel = new JLabel("Installs:");
        detailsPanel.add(departmentLabel);
        detailsPanel.add(InstallsTF);
        JLabel rateLabel = new JLabel("Type:");
        detailsPanel.add(rateLabel);
        detailsPanel.add(TypeTF);
        JLabel hoursLabel = new JLabel("Price:");
        detailsPanel.add(hoursLabel);
        detailsPanel.add(PriceTF);
        JLabel label1 = new JLabel("Content Rating:");
        detailsPanel.add(label1);
        detailsPanel.add(ContentTF);
        JLabel label2 = new JLabel("Genres:");
        detailsPanel.add(label2);
        detailsPanel.add(GenresTF);
        JLabel label3 = new JLabel("Current Version:");
        detailsPanel.add(label3);
        detailsPanel.add(CVersionTF);
        JLabel label4 = new JLabel("Android Version:");
        detailsPanel.add(label4);
        detailsPanel.add(AVersionTF);

        detailsPanel.setSize(CRUD_DIM);
        detailsPanel.setLocation(3, 0);

        //setup details panel and add the components to it
        exportButtonPanel = new JPanel();
        exportButtonPanel.setLayout(EXPORT_LAYOUT);
        exportButtonPanel.setBackground(Color.lightGray);
        exportButtonPanel.setBorder(BorderFactory.createTitledBorder(lineBorder, "Export Data"));
        exportButtonPanel.add(NumLectures);
        exportButtonPanel.add(NumLecturesTF);
        exportButtonPanel.add(avgAgeDepartment);
        exportButtonPanel.add(avgAgeDepartmentTF);
        exportButtonPanel.add(ListAllDepartments);
        exportButtonPanel.add(ListAllPositions);
        exportButtonPanel.setSize(EXPORT_DIM);
        exportButtonPanel.setLocation(3, 400);
        content.add(exportButtonPanel);

        insertButton.setSize(100, 30);
        deleteButton.setSize(100, 30);
        updateButton.setSize(100, 30);
        exportButton.setSize(100, 30);
        clearButton.setSize(100, 30);

        insertButton.setLocation(370, 10);
        deleteButton.setLocation(370, 60);
        updateButton.setLocation(370, 110);
        exportButton.setLocation(370, 160);
        clearButton.setLocation(370, 210);

        insertButton.addActionListener(this);
        updateButton.addActionListener(this);
        exportButton.addActionListener(this);
        deleteButton.addActionListener(this);
        clearButton.addActionListener(this);

        this.ListAllDepartments.addActionListener(this);
        this.NumLectures.addActionListener(this);


        content.add(insertButton);
        content.add(updateButton);
        content.add(exportButton);
        content.add(deleteButton);
        content.add(clearButton);


//        TableofDBContents.setPreferredScrollableViewportSize(new Dimension(1100, 300));
        //  Add horizontal scroll and resize for content
        dbContentsPanel = new JScrollPane(TableofDBContents, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        TableofDBContents.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        dbContentsPanel.setBackground(Color.lightGray);
        dbContentsPanel.setBorder(BorderFactory.createTitledBorder(lineBorder, "Database Content"));

        dbContentsPanel.setSize(CONTENT_DIM);
        dbContentsPanel.setLocation(477, 0);

        content.add(detailsPanel);
        content.add(dbContentsPanel);

        setSize(1182, 745);
        setVisible(true);

        initiate_db_conn();
        TableModel.refreshFromDB(stmt);
    }

    public void initiate_db_conn() {
        try {
            //  Not needed
//            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/A00276965";
            con = DriverManager.getConnection(url, "root", "");
            stmt = con.createStatement();
        } catch (Exception e) {
            errorMessage(e, "Failed to connect to database");
        }
    }

    //event handling
    public void actionPerformed(ActionEvent e) {
        Object target = e.getSource();
        //	Clear button
        if (target == clearButton) clearFields();

        if (target == insertButton || target == deleteButton || target == updateButton) {
            try {
                String update;
                if (target == insertButton) {
                    update = "";

                } else if (target == deleteButton) {
                    update = "";

                } else {
                    update = "";

                }
                stmt.executeUpdate(update);
            } catch (SQLException exception) {
                System.err.println(exception);
            } finally {
                TableModel.refreshFromDB(stmt);
            }
        }

        if (target == insertButton) {
            try {
                String updateTemp = "INSERT INTO details VALUES(" +
                        null + ",'" + AppNameTF.getText() + "','" + CategoryTF.getText() + "'," + RatingTF.getText() + ",'" + ReviewsTF.getText() + "','"
                        + SizeTF.getText() + "','" + InstallsTF.getText() + "'," + TypeTF.getText() + "," + PriceTF.getText() + ");";

                stmt.executeUpdate(updateTemp);

            } catch (SQLException sqle) {
                System.err.println("Error with  insert:\n" + sqle.toString());
            } finally {
                TableModel.refreshFromDB(stmt);
            }
        }
        if (target == deleteButton) {

            try {
                String updateTemp = "DELETE FROM details WHERE id = " + IDTF.getText() + ";";
                stmt.executeUpdate(updateTemp);

            } catch (SQLException sqle) {
                System.err.println("Error with delete:\n" + sqle.toString());
            } finally {
                TableModel.refreshFromDB(stmt);
            }
        }
        if (target == updateButton) {
            try {
                String updateTemp = "UPDATE details SET " +
                        "firstName = '" + AppNameTF.getText() +
                        "', lastName = '" + CategoryTF.getText() +
                        "', age = " + RatingTF.getText() +
                        ", gender ='" + ReviewsTF.getText() +
                        "', position = '" + SizeTF.getText() +
                        "', department = '" + InstallsTF.getText() +
                        "', rate = " + TypeTF.getText() +
                        ", hours = " + PriceTF.getText() +
                        " where id = " + IDTF.getText();


                stmt.executeUpdate(updateTemp);
                //these lines do nothing but the table updates when we access the db.
                rs = stmt.executeQuery("SELECT * from details ");
                rs.next();
                rs.close();
            } catch (SQLException sqle) {
                System.err.println("Error with  update:\n" + sqle.toString());
            } finally {
                TableModel.refreshFromDB(stmt);
            }
        }

        /////////////////////////////////////////////////////////////////////////////////////
        //I have only added functionality of 2 of the button on the lower right of the template
        ///////////////////////////////////////////////////////////////////////////////////

        if (target == this.ListAllDepartments) {

            cmd = "select distinct department from details;";

            try {
                rs = stmt.executeQuery(cmd);
                writeToFile(rs);
            } catch (Exception e1) {
                e1.printStackTrace();
            }

        }

        if (target == this.NumLectures) {
            String deptName = this.NumLecturesTF.getText();

            cmd = "select department, count(*) " + "from details " + "where department = '" + deptName + "';";

            System.out.println(cmd);
            try {
                rs = stmt.executeQuery(cmd);
                writeToFile(rs);
            } catch (Exception e1) {
                e1.printStackTrace();
            }

        }

    }


    private void clearFields() {
        IDTF.setText("");
        AppNameTF.setText("");
        CategoryTF.setText("");
        RatingTF.setText("");
        ReviewsTF.setText("");
        SizeTF.setText("");
        InstallsTF.setText("");
        TypeTF.setText("");
        PriceTF.setText("");
    }

    private void insertData() {

    }


    private void writeToFile(ResultSet rs) {
        try {
            System.out.println("In writeToFile");
            FileWriter outputFile = new FileWriter("Sheila.csv");
            PrintWriter printWriter = new PrintWriter(outputFile);
            ResultSetMetaData rsmd = rs.getMetaData();
            int numColumns = rsmd.getColumnCount();

            for (int i = 0; i < numColumns; i++) {
                printWriter.print(rsmd.getColumnLabel(i + 1) + ",");
            }
            printWriter.print("\n");
            while (rs.next()) {
                for (int i = 0; i < numColumns; i++) {
                    printWriter.print(rs.getString(i + 1) + ",");
                }
                printWriter.print("\n");
                printWriter.flush();
            }
            printWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void infoMessage(String message, String title) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    private void errorMessage(Exception exception, String message, String title) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.ERROR_MESSAGE);
        System.err.println(exception.getMessage());
    }

    private void errorMessage(Exception exception, String title) {
        errorMessage(exception, "Check console for more info", title);
    }

    private void errorMessage(Exception exception) {
        errorMessage(exception, "An error has occurred");
    }

}
