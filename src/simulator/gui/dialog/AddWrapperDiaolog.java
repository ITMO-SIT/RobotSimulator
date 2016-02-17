package simulator.gui.dialog;

import javax.swing.*;
import java.awt.*;

public class AddWrapperDiaolog extends JDialog{

    public AddWrapperDiaolog() {
        super();
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setModal(true);

        setMinimumSize(new Dimension(450, 300));
        setPreferredSize(new Dimension(450, 300));
        setMaximumSize(new Dimension(450, 300));

        setVisible(true);
    }

}
