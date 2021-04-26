package csi3471.bearMarket;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Vector;

import csi3471.bearMarket.AccountFiles.Account;
import csi3471.bearMarket.CurrentlySelling.CSProduct;
import csi3471.bearMarket.Logging.ProgLogger;
import csi3471.bearMarket.MainScreenFiles.MainScreen;
import csi3471.bearMarket.MainScreenFiles.ProductTable;
import csi3471.bearMarket.ProductFiles.Product;
import csi3471.bearMarket.PurchaseHistory.PurchaseProduct;

public class AccountInformation {
    private Account account;
    private Vector<CSProduct> currentlySellingVector;
    public Vector<Product> currentlySellingProductVector;
    private Vector<PurchaseProduct> purchaseHistoryVector;
    public Vector<Product> purchaseHistoryProductVector;
    private String file;
    
    public AccountInformation(Account ac) {
        account = ac;
        currentlySellingVector = new Vector<>();
        currentlySellingProductVector = new Vector<>();
        purchaseHistoryVector = new Vector<>();
        purchaseHistoryProductVector = new Vector<>();
        file = "users/" + ac.getUsername() + ".csv";
    }
    
    public void updateFileName(Account ac) {
        account = ac;
        file = "users/" + ac.getUsername() + ".csv";
    }
    
    public void readFile() {
        ProgLogger.LOGGER.info("Reading in currently selling and purchase history for account.");
        
        try {
            ProgLogger.LOGGER.info("Attempting to open file");
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            br.readLine();
            String csS = br.readLine();
            String phS = br.readLine();

            try {
                if(csS == null) {
                    throw new NullPointerException();
                }
                
                String[] currentlySelling = csS.split(",");
    
                for(String id : currentlySelling) {
                    CSProduct p = new CSProduct(Integer.parseInt(id));
                    currentlySellingVector.add(p);
                    currentlySellingProductVector.add(ProductTable.productMap.get(Integer.parseInt(id)));
                }
            } catch(NullPointerException e) {
                ProgLogger.LOGGER.info("User does not have any currently selling items");
            } catch(NumberFormatException e){
                ProgLogger.LOGGER.info("User does not have any currently selling items");
            }
            
            try {
                if(phS == null || phS == "") {
                    throw new NullPointerException();
                }
                
                String[] purchaseHistory = phS.split(",");
    
                for(String id : purchaseHistory) {
                    PurchaseProduct p = new PurchaseProduct(Integer.parseInt(id));
                    purchaseHistoryVector.add(p);
                    purchaseHistoryProductVector.add(ProductTable.productMap.get(Integer.parseInt(id)));
                }
            } catch(NullPointerException e) {
                ProgLogger.LOGGER.info("User does not have any purchase history");
            } catch(NumberFormatException e){
                ProgLogger.LOGGER.info("User does not have any purchase history");
            }

            ProgLogger.LOGGER.info("Successfully read in file");
        } catch(IOException e) {
            ProgLogger.LOGGER.info("Failed to read in file.");
            e.printStackTrace();
        }
    }
    
    public void saveFile() {
        //TODO: Add save functionality
    }
    
    public Account getAccount() {
        return account;
    }
    public void setAccount(Account account) {
        this.account = account;
    }
    public Vector<CSProduct> getCurrentlySellingVector() {
        return currentlySellingVector;
    }
    public void setCurrentlySellingVector(Vector<CSProduct> currentlySellingVector) {
        this.currentlySellingVector = currentlySellingVector;
    }
    public Vector<PurchaseProduct> getPurchaseHistoryVector() {
        return purchaseHistoryVector;
    }
    public void setPurchaseHistoryVector(Vector<PurchaseProduct> purchaseHistoryVector) {
        this.purchaseHistoryVector = purchaseHistoryVector;
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((account == null) ? 0 : account.hashCode());
        result = prime * result + ((currentlySellingVector == null) ? 0 : currentlySellingVector.hashCode());
        result = prime * result + ((purchaseHistoryVector == null) ? 0 : purchaseHistoryVector.hashCode());
        return result;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AccountInformation other = (AccountInformation) obj;
        if (account == null) {
            if (other.account != null)
                return false;
        } else if (!account.equals(other.account))
            return false;
        if (currentlySellingVector == null) {
            if (other.currentlySellingVector != null)
                return false;
        } else if (!currentlySellingVector.equals(other.currentlySellingVector))
            return false;
        if (purchaseHistoryVector == null) {
            if (other.purchaseHistoryVector != null)
                return false;
        } else if (!purchaseHistoryVector.equals(other.purchaseHistoryVector))
            return false;
        return true;
    }
}