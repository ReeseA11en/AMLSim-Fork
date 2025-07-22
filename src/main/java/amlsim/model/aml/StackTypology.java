//
// Note: No specific bank models are used for this AML typology model class.
//

package amlsim.model.aml;

import java.util.Random;

import amlsim.AMLSim;
import amlsim.Account;
import amlsim.TargetedTransactionAmount;

/**
 * Stacked bipartite transactions
 */
public class StackTypology extends AMLTypology {

    private Random random = AMLSim.getRandom();
    
    @Override
    public void setParameters(int modelID) {
    }

//    @Override
//    public int getNumTransactions() {
//        int total_members = alert.getMembers().size();
//        int orig_members = total_members / 3;  // First 1/3 accounts are originator accounts
//        int mid_members = orig_members;  // Second 1/3 accounts are intermediate accounts
//        int bene_members = total_members - orig_members * 2;  // Rest of accounts are beneficiary accounts
//        return orig_members * mid_members + mid_members + bene_members;
//    }

    StackTypology(double minAmount, double maxAmount, int minStep, int maxStep) {
        super(minAmount, maxAmount, minStep, maxStep);
    }

    @Override
    public String getModelName() {
        return "StackTypology";
    }

    @Override
    public void sendTransactions(long step, Account acct) {

        int total_members = alert.getMembers().size();
        int orig_members = total_members / 3;  // First 1/3 accounts are originator accounts
        int mid_members = orig_members;  // Second 1/3 accounts are intermediate accounts
        int bene_members = total_members - orig_members * 2;  // Rest of accounts are beneficiary accounts
        long alertID = alert.getAlertID();
        boolean isSAR = alert.isSAR();

        double randomNumber = Math.random();

        if (randomNumber < 0.4) { // Giving a 40% chance for transactions

        double randomNum = Math.random(); // Randomly determining the number of transactions to occur 
        int transactions = 0; // Setting a placeholder for the number of transactions

        if (randomNum < 0.6) {transactions = 1;} else if (randomNum < 0.9) {transactions = 2;} else {transactions = 3;} // Determining the number of transactions

        for (int i = 0; i < transactions; i++) {
            int random_orig = random.nextInt(orig_members-1); // Choosing a random node from the orig pool
            int random_bene = random.nextInt(mid_members-1) + orig_members; //Choosing a random node from the mid pool

            // Accessing the node information
            Account orig = alert.getMembers().get(random_orig);
            Account bene = alert.getMembers().get(random_bene);

            TargetedTransactionAmount transactionAmount = getTransactionAmount(1, orig.getBalance()); // Finding the transaction amount

            makeTransaction(step, transactionAmount.doubleValue(), orig, bene, isSAR, alertID); // Completing the transaction

        }} else {}

        double randomNumber2 = Math.random(); // Generating a random value to determine number of transactions

        if (randomNumber2 < 0.4) { // Giving a 40% chance for transactions

        double randomNum = Math.random(); // Randomly determining the number of transactions to occur 
        int transactions = 0; // Setting a placeholder for the number of transactions

        if (randomNum < 0.6) {transactions = 1;} else if (randomNum < 0.9) {transactions = 2;} else {transactions = 3;} // Determining the number of transactions

        for (int i = 0; i < transactions; i++) {
            int random_orig = random.nextInt(mid_members-1) + orig_members; // Choosing a random node from the mid pool
            int random_bene = random.nextInt(bene_members-1) + orig_members + mid_members; // Choosing a random node from the bene pool
            
            // Accessing the node information
            Account orig = alert.getMembers().get(random_orig);
            Account bene = alert.getMembers().get(random_bene);

            TargetedTransactionAmount transactionAmount = getTransactionAmount(1, orig.getBalance()); // Finding the transaction amount

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
