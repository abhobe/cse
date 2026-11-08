
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



    public List<Match> findCycle(int first, int recipient, boolean[] markedDone, boolean[] markedVisited) {
        markedVisited[recipient] = true;
        boolean cycleFound = false;

        List<Match> ret = new LinkedList<>();

        for (Match match : adjList.get(recipient)) {
            if (markedVisited[match.recipient] && !markedDone[match.recipient]) {
                if (match.recipient == first) {
                    cycleFound = true;
                    ret.add(match);
                    break;
                }
            } else if (!markedVisited[match.recipient] && !cycleFound) {
                ret = findCycle(first, match.recipient, markedDone, markedVisited);
                if (ret.size() > 0) {
                    cycleFound = true;
                    ret.add(0, match);
                    break;
                }
            }
        }
        markedDone[recipient] = true;
        return ret;
    }

    // returns a chain of matches to make a donor cycle
    // which includes the given recipient.
    // Returns an empty list if no cycle exists.
    public List<Match> findCycle(int recipient) {
        boolean[] markedDone = new boolean[adjList.size()];
        for (int i = 0; i < markedDone.length; i++) {
            markedDone[i] = false;
        }
        boolean[] markedVisited = new boolean[adjList.size()];
        for (int i = 0; i < markedVisited.length; i++) {
            markedVisited[i] = false;
        }

        return findCycle(recipient, recipient, markedDone, markedVisited);
    }


    private boolean hasCycle(int first, int recipient, boolean[] markedDone, boolean[] markedVisited) {
        markedVisited[recipient] = true;
        boolean cycleFound = false;

        for (Match match : adjList.get(recipient)) {
            if (markedVisited[match.recipient] && !markedDone[match.recipient]) {
                if (match.recipient == first) {
                    cycleFound = true;
                }
            } else if (!markedVisited[match.recipient] && !cycleFound) {
                cycleFound = hasCycle(first, match.recipient, markedDone, markedVisited);
            }
        }
        markedDone[recipient] = true;
        return cycleFound;
    }

    // returns true or false to indicate whether there
    // is some cycle which includes the given recipient.
    public boolean hasCycle(int recipient) {
        boolean[] markedDone = new boolean[adjList.size()];
        for (int i = 0; i < markedDone.length; i++) {
            markedDone[i] = false;
        }
        boolean[] markedVisited = new boolean[adjList.size()];
        for (int i = 0; i < markedVisited.length; i++) {
            markedVisited[i] = false;
        }

        return hasCycle(recipient, recipient, markedDone, markedVisited);
    }
}

