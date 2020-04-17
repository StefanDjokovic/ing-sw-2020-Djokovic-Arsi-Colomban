package it.polimi.ingsw.messages;

import java.util.ArrayList;

public class OptionSelection {

    ArrayList<ArrayList<Integer>> comb = new ArrayList<>();


    public void setOptions(ArrayList<Integer> positions) {
        comb.add(positions);
    }

    public void fuseOptions(OptionSelection opt) {
        this.comb.addAll(opt.comb);
    }

    public ArrayList<ArrayList<Integer>> getComb() {
        return this.comb;
    }

    @Override
    public String toString() {
        StringBuilder p = new StringBuilder();
        int count = 1;
        for (ArrayList<Integer> workerOptions : comb) {
            p.append("Worker ").append(count).append(" at");
            for (int j = 0; j < workerOptions.size(); j++) {
                p.append(" ").append(workerOptions.get(j));
                if (j == 1) {
                    p.append(" Options:");
                }
                else if (j % 2 == 1)
                    p.append(" ;");
            }
            p.append("\n");
            count++;
        }
        return p.toString();
    }
}
