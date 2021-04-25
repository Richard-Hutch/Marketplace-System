package csi3471.bearMarket.CurrentlySelling;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

import csi3471.bearMarket.Logging.ProgLogger;
import csi3471.bearMarket.MainScreenFiles.MainScreen;

public class CSTable extends CurrentlySellingWindow {
    public static Vector<CSProduct> csProductVector = new Vector<>();
    
    private static TableCellRenderer tableRenderer;
    
    public static void createTable() {
        ProgLogger.LOGGER.info("Creating Currently Selling Table");
        
        //Building table model
        tableModel = new DefaultTableModel() {
            final private String[] columnNames = {"Product Name","Category","Price","Edit","Delete"};
            
            @Override
            public Class<?> getColumnClass(int columnIndex){
                return switch(columnIndex) {
                    case 2 -> Float.class;
                    case 3, 4 -> JButton.class;
                    default -> String.class;
                };
            }
            
            @Override
            public int getRowCount() {
                return csProductVector.size();
            }
            
            @Override
            public int getColumnCount() {
                return columnNames.length;
            }
            
            @Override
            public String getColumnName(int column) {
                return columnNames[column];
            }
            
            @Override
            public Object getValueAt(int rowIndex, int columnIndex) {
                return switch(columnIndex) {
                    case 0 -> csProductVector.get(rowIndex).getProductName();
                    case 1 -> csProductVector.get(rowIndex).getCategory();
                    case 2 -> csProductVector.get(rowIndex).getPrice();
                    case 3 -> csProductVector.get(rowIndex).getEditButton();
                    case 4 -> csProductVector.get(rowIndex).getDeleteButton();
                    default -> null;
                };
            }
            
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        //Building table
        table = new JTable(tableModel);
        table.getTableHeader().setReorderingAllowed(false); //Prevents column reordering
        table.setDefaultRenderer(JButton.class, new JTableButtonRenderer(tableRenderer));
        table.addMouseListener(new JTableButtonMouseListener(table));
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setAutoCreateRowSorter(true);
        table.getColumnModel().getColumn(0).setPreferredWidth(200);
        ProgLogger.LOGGER.info("Currently Selling table created");
        
        ProgLogger.LOGGER.info("Reading in account currently selling info.");
        //Read in account currently selling information
        final String file = "users/" + MainScreen.currentAccount.getUsername() + ".csv";
        ReadFile.readFile(file, csProductVector);
        ProgLogger.LOGGER.info("Read in file");
    }
}

class ReadFile{
    public static void readFile(String file, Vector<CSProduct> pv) {
        ProgLogger.LOGGER.info("Currently Selling file reader function called.");
        
        try {
            ProgLogger.LOGGER.info("Attempting to open file");
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            br.readLine(); //Skip line with account information
            String line = br.readLine();
            String[] split = line.split(",");
            for(String id : split) {
                CSProduct p = new CSProduct(Integer.parseInt(id));
                pv.add(p);
            }
            br.close();
            ProgLogger.LOGGER.info("Successfully read in file");
        } catch(IOException e) {
            ProgLogger.LOGGER.info("Failed to read in file.");
            e.printStackTrace();
        }
    }
}