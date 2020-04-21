package it.polimi.ingsw.messages;

import java.util.ArrayList;

public class OptionSelection {

    private ArrayList<ArrayList<Integer>> values = new ArrayList<>();


    public void setOptions(ArrayList<Integer> positions) {
        values.add(positions);
    }

    public void fuseOptions(OptionSelection opt) {
        this.values.addAll(opt.values);
    }

    public ArrayList<ArrayList<Integer>> getValues() {
        return this.values;
    }

    @Override
    public String toString() {
        StringBuilder p = new StringBuilder();
        int count = 1;
        for (ArrayList<Integer> workerOptions : values) {
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
