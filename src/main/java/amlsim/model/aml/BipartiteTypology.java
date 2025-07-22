//
// Note: No specific bank models are used for this AML typology model class.
//

package amlsim.model.aml;

import amlsim.AMLSim;
import amlsim.Account;
import amlsim.TargetedTransactionAmount;

import java.util.*;

/**
 * Bipartite transaction model
 * Some accounts send money to a different account set
 */
public class BipartiteTypology extends AMLTypology {

    private Random random = AMLSim.getRandom();

    @Override
    public void setParameters(int modelID) {

    }
    
    public BipartiteTypology(double minAmount, double maxAmount, int minStep, int maxStep) {
        super(minAmount, maxAmount, minStep, maxStep);
    }

    @Override
    public String getModelName() {
        return "BipartiteTypology";
    }

    @Override
    public void sendTransactions(long step, Account acct) {
        List<Account> members = alert.getMembers();  // All members
        long alertID = alert.getAlertID(); // Alert ID
        boolean isSAR = alert.isSAR(); // Checking for the SAR flag
        
        double randomNumber = Math.random();
        
        if (randomNumber < 0.4) { // Giving a 40% chance to see if any transactions are made this step

        int last_orig_index = members.size() / 2;  // The first half accounts are originators
        double randomNumber2 = Math.random(); // Randomly determining the number of transactions to occur 
        int transactions = 0; // Setting a placeholder for the number of transactions

        if (randomNumber2 < 0.6) {transactions = 1;} else if (randomNumber2 < 0.9) {transactions = 2;} else {transactions = 3;}

        for (int i = 0; i < transactions; i++) {
            int random_orig = random.nextInt(last_orig_index - 1); // Generating a random index from the orig node pool
            int random_bene = random.nextInt(members.size()- last_orig_index - 1) + last_orig_index; // Randomly choosing a node from the bene pool
            
            // Accessing the nodes
            Account orig = members.get(random_orig);
            Account bene = members.get(random_bene);

            TargetedTransactionAmount transactionAmount = getTransactionAmount(1, orig.getBalance()); // Calculating the transaction amount

            makeTransaction(step, transactionAmount.doubleValue(), orig, bene, isSAR, alertID); // Completing the transaction

        }} else {}
    }


    private TargetedTransactionAmount getTransactionAmount(int numBene, double origBalance) {
        if (numBene == 0) {
            return new TargetedTransactionAmount(0, random);
        }
        return new TargetedTransactionAmount(origBalance / numBene, random);
    }
}
