package dk.loeschcke.matrix.view;

import javax.swing.*;
import java.beans.PropertyChangeEvent;

/**
 * Created with IntelliJ IDEA.
 * User: sbugge
 * Date: 18/08/13
 * Time: 16.26
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractViewPanel extends JPanel {

    /**
     * Called by the controller when it needs to pass along a property change
     * from a model.
     *
     * @param evt The property change event from the model
     */

    public abstract void modelPropertyChange(PropertyChangeEvent evt);


}
