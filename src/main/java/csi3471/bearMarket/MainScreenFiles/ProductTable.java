package csi3471.bearMarket.MainScreenFiles;

import csi3471.bearMarket.Logging.ProgLogger;
import csi3471.bearMarket.ProductFiles.Product;
import csi3471.bearMarket.ProductFiles.ReadProductFile;
import csi3471.bearMarket.ProductReview.ReadReviews;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 * Class handles creation of the JTableModel and JTable and handles all table setup such as
 * reading in the product file and reading in reviews from file
 * @author Richard Hutcheson
 */
public class ProductTable extends MainScreen{

    public static Map<Integer, Product> productMap = new HashMap<>();
    public static Vector<Product> productVector = new Vector<>();
    private static TableCellRenderer tableRenderer;

    /**
     * handles creation of the JTableModel and JTable and handles all table setup such as
     * reading in the product file and reading in reviews from file
     */
    public static void createTable(){
        ProgLogger.LOGGER.info("Creating Product Table Function Called");

        tableModel = new DefaultTableModel(){
            final private String[] columnNames = {"Product Name", "Category", "Quantity", "Rating (x/10)", "Price ($)", "Description", "Purchase", "Reviews"};

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return switch(columnIndex){
                    case 2 -> Integer.class;
                    case 3, 4 -> Float.class;
                    case 5, 6, 7 -> JButton.class;
                    default -> String.class;
                };
            }
            @Override
            public int getRowCount() {
                return productVector.size();
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
                return switch (columnIndex) {
                    case 0 -> productVector.get(rowIndex).getProductName();
                    case 1 -> productVector.get(rowIndex).getCategory();
                    case 2 -> productVector.get(rowIndex).getQuantity();
                    case 3 -> productVector.get(rowIndex).getRating();
                    case 4 -> productVector.get(rowIndex).getPrice();
                    case 5 -> productVector.get(rowIndex).getDescButton();
                    case 6 -> productVector.get(rowIndex).getPurchase();
                    case 7 -> productVector.get(rowIndex).getReviewsButton();
                    default -> null;
                };
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.getTableHeader().setReorderingAllowed(false); //prevent column reordering
        table.setDefaultRenderer(JButton.class, new JTableButtonRenderer(tableRenderer));
        table.addMouseListener(new JTableButtonMouseListener(table));
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setAutoCreateRowSorter(true);
        table.getColumnModel().getColumn(0).setPreferredWidth(200);
        table.getColumnModel().getColumn(5).setPreferredWidth(85);
        ProgLogger.LOGGER.info("Table and Table Model created");

        ProgLogger.LOGGER.info("Reading in Product file");
        //read in product file
        final String file = "src/main/resources/product_list.tsv";
        ReadProductFile.readFile(file, productVector, productMap);
        ProgLogger.LOGGER.info("Reading in Product Review File");
        ReadReviews.readInReviews(productMap);

        scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(900, 400));
    }

    /**
     * adds Product item to JTable via product vector
     * @param add Product item to be added to product vector, which then gets put into JTable
     * @author Austin Blanchard
     */
    public static void addItem(Product add) {
        ProgLogger.LOGGER.info("Attempting to add user's product to table");
        productVector.add(add);
        productMap.put(add.getID(), add);
        tableModel.fireTableDataChanged();
        //productMap.put(add.hashCode(), add);
        //tableModel.addRow(add.returnObjects());
    }

    /**
     * edits a currently listed product on the market place main screen
     * @param edit Product that has been edited and to be add to the table
     * @param removeNdx the index to remove the item from the productVector and remove from table
     * @author Austin Blanchard
     */
    public static void editItem(Product edit, int removeNdx) {
        productVector.remove(removeNdx);
        productVector.insertElementAt(edit, removeNdx);
        productMap.put(edit.getID(), edit);
    }

    /**
     * deletes currently listed item from the main table
     * @param del product to delete from the main table
     * @param removeNdx the index of the vector to remove the item from
     *        the table
     * @author Austin Blanchard
     */
    public static void deleteItem(Product del, int removeNdx) {
        productVector.remove(removeNdx);
        productMap.remove(del.getID(), del);
    }
    
    /**
     * Function that saves the product vector to the file.
     */
    public static void saveToFile() {
        ProgLogger.LOGGER.info("Saving product table to file");
        
        String filePath = "src/main/resources/product_list.tsv";
        
        File productFile = new File(filePath);
        
        try {
            DataOutputStream out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(productFile)));
            out.close();
            out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(productFile)));
            out.write("Product\tCategory\tDescription\tQuantity (Stock)\tRating (x/10)\tPrice\tID\n".getBytes(StandardCharsets.UTF_8));
            for(Product p : productVector) {
                out.write(p.getProductName().getBytes(StandardCharsets.UTF_8));
                out.write("\t".getBytes(StandardCharsets.UTF_8));
                out.write(p.getCategory().getBytes(StandardCharsets.UTF_8));
                out.write("\t".getBytes(StandardCharsets.UTF_8));
                out.write(p.getDescription().getBytes(StandardCharsets.UTF_8));
                out.write("\t".getBytes(StandardCharsets.UTF_8));
                String temp = String.valueOf(p.getQuantity()) + "\t";
                out.write(temp.getBytes(StandardCharsets.UTF_8));
                temp = String.valueOf(p.getRating()) + "\t";
                out.write(temp.getBytes(StandardCharsets.UTF_8));
                temp = "$" + String.valueOf(p.getPrice()) + "\t";
                out.write(temp.getBytes(StandardCharsets.UTF_8));
                temp = String.valueOf(p.getID()) + "\n";
                out.write(temp.getBytes(StandardCharsets.UTF_8));
            }
            
            out.close();
        } catch(IOException e) {
            
        }
    }
}