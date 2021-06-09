import javax.swing.*;

/**
 * This Class ist the frame of the view.
 *
 * @author Dash11235
 * @version 2021-03-19
 */

public class ViewFrame extends JFrame {

    /**
     * Constuructor of the class - The frame is given a title, a size and some other attributes.
     */

    public ViewFrame(ViewPanel panel){
        super("Calculator");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setSize(400,500);
        this.setResizable(false);
        this.add(panel);
        this.setVisible(true);
    }
}