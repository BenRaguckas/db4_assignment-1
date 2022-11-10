package com.ben.view;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.jdbc.JDBCCategoryDataset;

import java.awt.*;
import java.awt.event.*;
import java.io.FileWriter;
import java.io.PrintWriter;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Vector;
import java.util.stream.Collectors;

public class JDBCMainWindowContent extends JInternalFrame implements ActionListener {
    private final int
            INPUT_WIDTH = 14,
            EXPORT_WIDTH = 12;
    private final Dimension
            CRUD_DIM = new Dimension(360, 400),
            EXPORT_DIM = new Dimension(500, 200),
            CONTENT_DIM = new Dimension(1000, 400),
            BUTTON_SIZE = new Dimension(100, 30);
    private final GridLayout
            CRUD_LAYOUT = new GridLayout(13, 2),
            EXPORT_LAYOUT = new GridLayout(4, 2);

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
            exportTF = new JTextField("android_apps.csv",EXPORT_WIDTH),
            ContentTF = new JTextField(INPUT_WIDTH),
            GenresTF = new JTextField(INPUT_WIDTH),
            CVersionTF = new JTextField(INPUT_WIDTH),
            listGenresTF = new JTextField(INPUT_WIDTH),
            listCategoriesTF = new JTextField(INPUT_WIDTH),
            listContentTF = new JTextField(INPUT_WIDTH),
            AVersionTF = new JTextField(INPUT_WIDTH);


    private static QueryTableModel TableModel = new QueryTableModel();
    //Add the models to JTabels
    private JTable TableofDBContents = new JTable(TableModel);
    private final JButton
            updateButton = new JButton("Update"),
            insertButton = new JButton("Insert"),
            deleteButton = new JButton("Delete"),
            selectButton = new JButton("Select"),
            clearButton = new JButton("Clear"),
            exportButton = new JButton("Export to file"),
            listGenresButton = new JButton("List all genres"),
            listCategoriesButton = new JButton("List all categories"),
            listContentButton = new JButton("List all content ratings"),
            showCharts = new JButton("Show Charts");


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
        exportButtonPanel.add(exportButton);
        exportTF.setHorizontalAlignment(SwingConstants.CENTER);
        exportButtonPanel.add(exportTF);
        exportButtonPanel.add(listGenresButton);
        exportButtonPanel.add(listGenresTF);
        listGenresTF.setHorizontalAlignment(SwingConstants.CENTER);
        exportButtonPanel.add(listCategoriesButton);
        exportButtonPanel.add(listCategoriesTF);
        listCategoriesTF.setHorizontalAlignment(SwingConstants.CENTER);
        exportButtonPanel.add(listContentButton);
        exportButtonPanel.add(listContentTF);
        listContentTF.setHorizontalAlignment(SwingConstants.CENTER);
        exportButtonPanel.setSize(EXPORT_DIM);
        exportButtonPanel.setLocation(3, 400);
        content.add(exportButtonPanel);


        content.add(showCharts);
        showCharts.setLocation(1100, 450);
        showCharts.setSize(300,100);

        insertButton.setSize(BUTTON_SIZE);
        deleteButton.setSize(BUTTON_SIZE);
        updateButton.setSize(BUTTON_SIZE);
        selectButton.setSize(BUTTON_SIZE);
        clearButton.setSize(BUTTON_SIZE);

        insertButton.setLocation(370, 10);
        deleteButton.setLocation(370, 60);
        updateButton.setLocation(370, 110);
        selectButton.setLocation(370, 160);
        clearButton.setLocation(370, 210);

        insertButton.addActionListener(this);
        updateButton.addActionListener(this);
        deleteButton.addActionListener(this);
        selectButton.addActionListener(this);
        clearButton.addActionListener(this);

        listCategoriesButton.addActionListener(this);
        exportButton.addActionListener(this);
        listGenresButton.addActionListener(this);
        listContentButton.addActionListener(this);
        showCharts.addActionListener(this);


        content.add(insertButton);
        content.add(updateButton);
        content.add(deleteButton);
        content.add(selectButton);
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

        if (target == clearButton) clearFields();
        if (target == insertButton) insertData();
        if (target == selectButton) selectRow();
        if (target == updateButton) updateItem();
        if (target == deleteButton) deleteItem();
        if (target == exportButton) writeToFile();
        if (target == listGenresButton) {
            String s = "SELECT * FROM genres_list";
            if (listGenresTF.getText().isEmpty())
                showPopupTable(s);
            else
                writeToFile(s, listGenresTF.getText());
        }
        if (target == listCategoriesButton) {
            String s = "SELECT * FROM category_list";
            if (listCategoriesTF.getText().isEmpty())
                showPopupTable(s);
            else
                writeToFile(s, listCategoriesTF.getText());
        }
        if (target == listContentButton) {
            String s = "SELECT * FROM content_list";
            if (listContentTF.getText().isEmpty())
                showPopupTable(s);
            else
                writeToFile(s, listContentTF.getText());
        }
        if (target == showCharts) showChart();


    }

    private void showChart() {
        var window = new ChartWindow(con);
        window.setVisible(true);
    }

    private void deleteItem() {
        if (!IDTF.getText().isEmpty() && IDTF.getText().matches("\\d+")) {
            int input = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to delete item with ID:" + IDTF.getText(),
                    "Confirm deletion",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );
            if (input == 0) {
                try {
                    PreparedStatement ps = con.prepareStatement("DELETE FROM android_apps WHERE id=?");
                    ps.setString(1, IDTF.getText());
                    int updates = ps.executeUpdate();
                    ps.close();
                    TableModel.refreshFromDB(stmt);
                    if (updates > 0)
                        infoMessage("Item ID:" + IDTF.getText() + " deleted successfully.", "Deleted successfully");
                    else
                        infoMessage("Unable to delete item ID:" + IDTF.getText(), "Delete failed");
                } catch (Exception e) {
                    errorMessage(e, "Error while deleting");
                }
            }
        } else {
            infoMessage("Please enter item ID for deletion.","No ID specified");
        }
    }

    private void updateItem() {
        try {
            CallableStatement cs = con.prepareCall("{ CALL update_item(?,?,?,?,?,?,?,?,?,?,?,?,?) }");
            cs.setString(1, IDTF.getText());
            cs.setString(2, AppNameTF.getText());
            cs.setString(3, CategoryTF.getText());
            cs.setString(4, RatingTF.getText());
            cs.setString(5, ReviewsTF.getText());
            cs.setString(6, SizeTF.getText());
            cs.setString(7, InstallsTF.getText());
            cs.setString(8, TypeTF.getText());
            cs.setString(9, PriceTF.getText());
            cs.setString(10, ContentTF.getText());
            cs.setString(11, GenresTF.getText());
            cs.setString(12, CVersionTF.getText());
            cs.setString(13, AVersionTF.getText());
            cs.execute();
            TableModel.refreshFromDB(stmt);
            infoMessage("Updated item ID:" + IDTF.getText(), "Updated successfully");
        } catch (Exception e) {
            errorMessage(e, "Error while updating");
        }

    }

    private void selectRow() {
        int row = TableofDBContents.getSelectedRow();
        if (row == -1)
            infoMessage("Highlight a row for selection.","No row highlighted");
        else{
            IDTF.setText(Objects.requireNonNullElse(TableModel.getValueAt(row, 0),"").toString());
            AppNameTF.setText(Objects.requireNonNullElse(TableModel.getValueAt(row, 1),"").toString());
            CategoryTF.setText(Objects.requireNonNullElse(TableModel.getValueAt(row, 2),"").toString());
            RatingTF.setText(Objects.requireNonNullElse(TableModel.getValueAt(row, 3),"").toString());
            ReviewsTF.setText(Objects.requireNonNullElse(TableModel.getValueAt(row, 4),"").toString());
            SizeTF.setText(Objects.requireNonNullElse(TableModel.getValueAt(row, 5),"").toString());
            InstallsTF.setText(Objects.requireNonNullElse(TableModel.getValueAt(row, 6),"").toString());
            TypeTF.setText(Objects.requireNonNullElse(TableModel.getValueAt(row, 7),"").toString());
            PriceTF.setText(Objects.requireNonNullElse(TableModel.getValueAt(row, 8),"").toString());
            ContentTF.setText(Objects.requireNonNullElse(TableModel.getValueAt(row, 9),"").toString());
            GenresTF.setText(Objects.requireNonNullElse(TableModel.getValueAt(row, 10),"").toString());
            CVersionTF.setText(Objects.requireNonNullElse(TableModel.getValueAt(row, 12),"").toString());
            AVersionTF.setText(Objects.requireNonNullElse(TableModel.getValueAt(row, 13),"").toString());
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
        ContentTF.setText("");
        GenresTF.setText("");
        CVersionTF.setText("");
        AVersionTF.setText("");
    }

    private void insertData() {
        try {
            //  Call prepared procedure for inserting
            CallableStatement cs = con.prepareCall("{ CALL insert_item(?,?,?,?,?,?,?,?,?,?,?,?,?) }");
            cs.setString(1, AppNameTF.getText());
            cs.setString(2, CategoryTF.getText());
            cs.setString(3, RatingTF.getText());
            cs.setString(4, ReviewsTF.getText());
            cs.setString(5, SizeTF.getText());
            cs.setString(6, InstallsTF.getText());
            cs.setString(7, TypeTF.getText());
            cs.setString(8, PriceTF.getText());
            cs.setString(9, ContentTF.getText());
            cs.setString(10, GenresTF.getText());
            cs.setString(11, CVersionTF.getText());
            cs.setString(12, AVersionTF.getText());
            cs.registerOutParameter(13, Types.INTEGER);
            cs.execute();
            TableModel.refreshFromDB(stmt);
            infoMessage("Inserted with ID:" + cs.getInt(13), "Inserted successfully");
        } catch (Exception e) {
            errorMessage(e, "Error while inserting");
        }
    }

    private void writeToFile() {
        try {
            String file_name = exportTF.getText();
            PrintWriter pw = new PrintWriter(new FileWriter("./output/" + file_name));
            pw.println(Arrays.stream(TableModel.headers).collect(Collectors.joining("\t")));
            TableModel.modelData.stream().map(row -> Arrays.stream((String[])row).collect(Collectors.joining("\t"))).forEach(pw::println);
            pw.close();
            infoMessage("Exported to a file:" + file_name + "\nUsing \"\\t\" as delimiter.","Export success");
        } catch (Exception e) {
            errorMessage(e, "Error while exporting");
        }
    }

    private void writeToFile(String query, String fileName) {
        try {
            PrintWriter pw = new PrintWriter(new FileWriter("./output/" + fileName));
            rs = stmt.executeQuery(query);
            pw.println(rs.getMetaData().getColumnName(1));
            while (rs.next())
                pw.println(rs.getString(1));
            pw.close();
            infoMessage("Exported to a file:" + fileName + "\nUsing \"\\t\" as delimiter.","Export success");
        } catch (Exception e) {
            errorMessage(e, "Failed to export data");
        }
    }

    private void showPopupTable(String query) {
        try {
            rs = stmt.executeQuery(query);
            String[] columns = new String[]{rs.getMetaData().getColumnName(1)};
            ArrayList<String[]> dataList = new ArrayList<>();
            while (rs.next())
                dataList.add(new String[]{rs.getString(1)});
            String[][] data = new String[dataList.size()][];
            for (int i = 0; i < data.length; i++)
                data[i] = dataList.get(i);
            JTable table = new JTable(data, columns);
            JOptionPane.showMessageDialog(this, table, "List of all " + columns[0], JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            System.out.println(e.getMessage());
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
