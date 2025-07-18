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
        long alertID = alert.getAlertID();
        boolean isSAR = alert.isSAR();
        
        double randomNumber = Math.random();
        
        if (randomNumber > 0.5) { // Giving a 50/50 chance to see if any transactions are made this step
        int last_orig_index = members.size() / 2;  // The first half accounts are originators

        int randomNumber2 = random.nextInt((2 - 1) + 1) + 1; // Randomly specifying if 1 or 2 transactions will occur 

        for (int i = 0; i < randomNumber2; i++) {
            int random_orig = random.nextInt((last_orig_index - 0) + 1) + 0;
            int random_bene = random.nextInt(((members.size()-1)-(last_orig_index+1)) + 1) + last_orig_index + 1;

            Account orig = members.get(random_orig);
            Account bene = members.get(random_bene);

            TargetedTransactionAmount transactionAmount = getTransactionAmount(1, orig.getBalance());

            makeTransaction(step, transactionAmount.doubleValue(), orig, bene, isSAR, alertID);

        }

        // for (int i = 0; i < last_orig_index; i++) {
        //     Account orig = members.get(i);
        //     if (!orig.getID().equals(acct.getID())) {
        //         continue;
        //     }

        //     TargetedTransactionAmount transactionAmount = getTransactionAmount(members.size() - last_orig_index,
        //             orig.getBalance());

        //     for (int j = last_orig_index; j < members.size(); j++) {
        //         Account bene = members.get(j); // The latter half accounts are beneficiaries
        //         makeTransaction(step, transactionAmount.doubleValue(), orig, bene, isSAR, alertID);
        //     }}
        } else {}
    }


    private TargetedTransactionAmount getTransactionAmount(int numBene, double origBalance) {
        if (numBene == 0) {
            return new TargetedTransactionAmount(0, random);
        }
        return new TargetedTransactionAmount(origBalance / numBene, random);
    }
}
