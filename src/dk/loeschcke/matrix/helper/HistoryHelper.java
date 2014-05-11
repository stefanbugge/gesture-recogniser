package dk.loeschcke.matrix.helper;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: sbugge
 * Date: 16/08/13
 * Time: 13.59
 * To change this template use File | Settings | File Templates.
 */
public class HistoryHelper<E> {

    private int historyAmount;
    private List<E> history = new LinkedList<E>();

    public HistoryHelper(int historyAmount) {
        this.historyAmount = historyAmount;
    }

    public void append(E entity) {
        history.add(0, entity);
        if (history.size() > historyAmount) {
            history.remove(history.size()-1);
        }
    }

    public boolean isFull() {
        return history.size() == historyAmount;
    }

    public void reset() {
        history.clear();
    }

    public List<E> get() {
        return history;
    }

    public int size() {
        return history.size();
    }
}
