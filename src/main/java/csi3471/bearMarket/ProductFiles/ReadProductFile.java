package csi3471.bearMarket.ProductFiles;

import csi3471.bearMarket.Logging.ProgLogger;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Vector;

/**Open the product file and read in products, creating Product items and pushing to list of products
 * @author Richard Hutcheson
 */
public class ReadProductFile {
    /**
     * Open the product file and read in products, creating Product items and pushing to list of products
     * @param file String containing the path for the product file
     * @param productVector Vector<Product> that holds all of the products read in
     * @param productMap Map<Integer, Product> holds Product and associates the id of the product with the product
     */
    public static void readFile(String file, Vector<Product> productVector, Map<Integer, Product> productMap){

        ProgLogger.LOGGER.info("Product readFile function called");
        try{
            ProgLogger.LOGGER.info("attempting to open and read Product file");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
            bufferedReader.readLine(); //waste first line of csv
            String line = "";
            while ((line = bufferedReader.readLine()) != null){
                Product p = new Product(line.split("\t"));
                productMap.put(p.getID(), p);
                productVector.add(p);
            }
            bufferedReader.close(); //close file
            ProgLogger.LOGGER.info("Product file read in");
        }catch(IOException e){
            ProgLogger.LOGGER.severe("IO exception, error reading in product file");
            //System.out.println("ERROR while opening or reading'" + file + "'");
            e.printStackTrace();
        }
    }
}
