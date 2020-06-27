package it.polimi.ingsw.messages;

import javax.swing.text.html.Option;
import java.io.Serializable;
import java.util.ArrayList;

public class OptionSelection implements Serializable {

    private ArrayList<ArrayList<Integer>> values = new ArrayList<>();


    public void setOptions(ArrayList<Integer> positions) {
        values.add(positions);
    }

    public void addWorkerOptions(ArrayList<Integer> optWorker) {
        this.values.add(optWorker);
    }

    public ArrayList<ArrayList<Integer>> getValues() {
        return this.values;
    }

    public void removeXAndY(OptionSelection opt, int x, int y) {
        for (ArrayList<Integer> comb: opt.getValues()) {
            for (int i = 2; i < comb.size(); i += 2) {
                if (comb.get(i) == x && comb.get(i + 1) == y) {
                    comb.remove(i+1);
                    comb.remove(i);
                    i -= 2;
                }
            }
        }
    }

    public OptionSelection singleOption(int x, int y) {
        OptionSelection newOpt = new OptionSelection();
        for (ArrayList<Integer> a: this.getValues()) {
            if (a.get(0) == x && a.get(1) == y) {
                newOpt.setOptions(a);
                break;
            }
        }
        return newOpt;
    }

    public void addWorkerAsOption() {
        values.get(0).add(values.get(0).get(0));
        values.get(0).add(values.get(0).get(1));
    }

    public OptionSelection removeOptionsPerimeter() {
        OptionSelection newOpt = new OptionSelection();
        ArrayList<Integer> opt = new ArrayList<>();
        System.out.println("Here is opt again");
        System.out.println(this);
        ArrayList<Integer> oldOpt = this.getValues().get(0);
        opt.add(oldOpt.get(0));
        opt.add(oldOpt.get(1));
        for (int i = 2; i < oldOpt.size(); i += 2) {
            if (!(oldOpt.get(i) == 0 || oldOpt.get(i) == 4 || oldOpt.get(i + 1) == 0 || oldOpt.get(i + 1) == 4)) {
                opt.add(oldOpt.get(i));
                opt.add(oldOpt.get(i + 1));
            }
        }
        newOpt.addWorkerOptions(opt);

        return newOpt;
    }


    /**
     * Indicates whether a player has options for his turn or not
     * @return true if the player has playable options, false if not
     */
    public boolean hasOptions() {
        for (ArrayList<Integer> o: this.getValues()) {
            if (o.size() > 2) {
                System.out.println("These are the options");
                System.out.println(o);
                return true;
            }
        }
        return false;
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
