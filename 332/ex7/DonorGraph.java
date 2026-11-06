import java.util.*;

public class DonorGraph {
    public List<List<Match>> adjList;

    // The donatingTo array indicates which repient each donor is
    // affiliated with. Specifically, the donor at index i has volunteered
    // to donate a kidney on behalf of recipient donatingTo[i].
    // The matchScores 2d array gives the match scores associated with each
    // donor-recipient pair. Specificically, matchScores[x][y] gives the
    // HLA score for donor x and reciplient y.
    public DonorGraph(int[] donorToBenefit, int[][] matchScores) {
        int numRecipients = matchScores[0].length;
        adjList = new ArrayList<>();

        for (int i = 0; i < numRecipients; i++) {
            adjList.add(new LinkedList<>());
        }

        for (int donor = 0; donor < matchScores.length; donor++) {
            for (int recipient = 0; recipient < numRecipients; recipient ++) {
                int beneficiary = donorToBenefit[donor];
                if (matchScores[donor][recipient] >= 60) {
                    adjList.get(beneficiary).add(new Match(donor, beneficiary, recipient));
                }
            }
        }
    }

    // Will be used by the autograder to verify your graph's structure.
    // It's probably also going to helpful for your debugging.
    public boolean isAdjacent(int start, int end) {
        for (Match m : adjList.get(start)) {
            if (m.recipient == end)
                return true;
        }
        return false;
    }

    // Will be used by the autograder to verify your graph's structure.
    // It's probably also going to helpful for your debugging.
    public int getDonor(int beneficiary, int recipient) {
        for (Match m : adjList.get(beneficiary)) {
            if (m.recipient == recipient)
                return m.donor;
        }
        return -1;
    }


    // returns a chain of matches to make a donor cycle
    // which includes the given recipient.
    // Returns an empty list if no cycle exists.
    public List<Match> findCycle(int recipient) {
        
        
        return new ArrayList<>();
    }


    private boolean hasCycle(int recipient, boolean[] markedDone, boolean[] markedVisited) {
        markedVisited[recipient] = true;
        boolean cycleFound = false;

        for (Match m :  adjList.get(recipient)) {
            if (markedVisited[m.recipient] && !markedDone[m.recipient]) {
                cycleFound = true;
            } else if (!markedVisited[m.recipient] && !cycleFound) {
                cycleFound = hasCycle(recipient);
            }
        }
        markedDone[recipient] = true;
        return cycleFound;
    }

    // returns true or false to indicate whether there
    // is some cycle which includes the given recipient.
    public boolean hasCycle(int recipient) {
        boolean[] markedDone = new boolean[adjList.size()];
        boolean[] markedVisited = new boolean[adjList.size()];

        return hasCycle(recipient, markedDone, markedVisited);
    }
}
