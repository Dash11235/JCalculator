import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * The Panel of the View part.
 *
 * @author Dash11235
 * @version 2021-03-19
 */

public class ViewPanel extends JPanel {

    // All the needes objects are getting declared.
    private Controller control;

    private JButton zero;
    private JButton one;
    private JButton two;
    private JButton three;
    private JButton four;
    private JButton five;
    private JButton six;
    private JButton seven;
    private JButton eight;
    private JButton nine;
    private JButton add;
    private JButton sub;
    private JButton mult;
    private JButton divid;
    private JButton modulo;
    private JButton dot;
    private JButton delete;
    private JButton calculate;
    private JButton clear;
    private JButton bracketOpen;
    private JButton bracketClose;

    private JPanel numberPad;
    private JPanel inputField;

    private JTextField input;

    private Border grayBorder;
    private Border darkGrayBorder;

    private JPopupMenu fastMenu;

    /**
     * Constructor of the class; All the needed components are getting added to the Panel.
     *
     * @param control The object of Control, use fo the ActionListener.
     */

    public ViewPanel(Controller control){

        // control and the layout are getting set.
        this.control = control;
        this.setLayout(new BorderLayout());
        this.addMouseListener(this.control);

        fastMenu = new JPopupMenu("Fast Menu");
        fastMenu.setBackground(Color.DARK_GRAY);
        fastMenu.setForeground(Color.DARK_GRAY);

        // Some of the needed elements are getting initialized.
        inputField = new JPanel(new GridBagLayout());
        inputField.addMouseListener(this.control);
        input = new JTextField("");

        input.setPreferredSize(new Dimension(350, 40));
        input.setForeground(Color.WHITE);
        input.setBackground(Color.DARK_GRAY);
        input.setBorder(null);
        input.setFont(new Font("Arial", Font.PLAIN, 17));
        input.addMouseListener(this.control);

        // ARRANGE HISTORY && INPUT ????

        inputField.add(input);

        inputField.setBackground(Color.DARK_GRAY);

        this.add(inputField, BorderLayout.NORTH);

        // A numberpad is getting created and the needed buttons are getting added.
        numberPad = new JPanel(new GridLayout(7,2));
        numberPad.setBackground(Color.DARK_GRAY);
        zero = new JButton("0");
        one = new JButton("1");
        two = new JButton("2");
        three = new JButton("3");
        four = new JButton("4");
        five = new JButton("5");
        six = new JButton("6");
        seven = new JButton("7");
        eight = new JButton("8");
        nine = new JButton("9");
        add = new JButton("+");
        sub = new JButton("-");
        mult = new JButton("*");
        divid = new JButton("/");
        modulo = new JButton("%");
        dot = new JButton(".");
        delete = new JButton("<------");
        clear = new JButton("c");
        calculate = new JButton("=");
        bracketOpen = new JButton("(");
        bracketClose = new JButton(")");

        // Creating some borders for the buttons.
        grayBorder = BorderFactory.createLineBorder(Color.GRAY);
        darkGrayBorder = BorderFactory.createLineBorder(Color.DARK_GRAY);

        // JButton arrys are created to make it more dynamic.
        JButton[] numButtons = {zero,one,two,three,four,five,six,seven,eight,nine};
        JButton[] operatorButtons = {add,sub,dot,delete,clear,calculate,mult,divid,modulo,bracketOpen,bracketClose};

        // Giving every number-button the same style.
        for(int i = 0; i < numButtons.length; i++){
            numButtons[i].setBackground(Color.DARK_GRAY);
            numButtons[i].setForeground(Color.WHITE);
            numButtons[i].setFocusPainted(false);
            numButtons[i].setBorder(grayBorder);
            numButtons[i].setFont(new Font("Arial", Font.PLAIN, 18));
        }

        // Giving every operator button the same style.
        for(int i = 0; i < operatorButtons.length; i++){
            operatorButtons[i].setBackground(Color.GRAY);
            operatorButtons[i].setForeground(Color.WHITE);
            operatorButtons[i].setFocusPainted(false);
            operatorButtons[i].setBorder(darkGrayBorder);
            operatorButtons[i].setFont(new Font("Arial", Font.PLAIN, 18));
        }

        // Some buttons are getting special colors.
        clear.setBackground(Color.decode("#D78909"));
        delete.setBackground(Color.decode("#D78909"));
        calculate.setBackground(Color.decode("#D78909"));

        // Adding the needed buttons onto the numpad and the numpad is getting added to the panel.
        numberPad.add(add);
        numberPad.add(sub);
        numberPad.add(dot);
        numberPad.add(one);
        numberPad.add(two);
        numberPad.add(mult);
        numberPad.add(three);
        numberPad.add(four);
        numberPad.add(divid);
        numberPad.add(five);
        numberPad.add(six);
        numberPad.add(modulo);
        numberPad.add(seven);
        numberPad.add(eight);
        numberPad.add(clear);
        numberPad.add(nine);
        numberPad.add(zero);
        numberPad.add(delete);
        numberPad.add(bracketOpen);
        numberPad.add(bracketClose);
        numberPad.add(calculate);
        numberPad.addMouseListener(this.control);
        this.add(numberPad);

        // Giving every button an ActionListener.
        zero.addActionListener(this.control);
        one.addActionListener(this.control);
        two.addActionListener(this.control);
        three.addActionListener(this.control);
        four.addActionListener(this.control);
        five.addActionListener(this.control);
        six.addActionListener(this.control);
        seven.addActionListener(this.control);
        eight.addActionListener(this.control);
        nine.addActionListener(this.control);
        dot.addActionListener(this.control);
        add.addActionListener(this.control);
        sub.addActionListener(this.control);
        delete.addActionListener(this.control);
        clear.addActionListener(this.control);
        calculate.addActionListener(this.control);
        mult.addActionListener(this.control);
        divid.addActionListener(this.control);
        modulo.addActionListener(this.control);
        bracketOpen.addActionListener(this.control);
        bracketClose.addActionListener(this.control);

    }

    /**
     * This method returns the input-value of the inputFied.
     *
     * @return The input-value in the inputField.
     */

    public String getInput(){
        return this.input.getText();
    }

    /**
     * This method sets the value of the output label.
     */

    public void setLabelText(String str){
        this.input.setText(str);
    }

    /**
     *
     * @param x
     * @param y
     */

    public void showFastMenu(int x, int y){
        fastMenu.removeAll();

        JMenuItem historyItem = new JMenuItem("History");
        JMenuItem historyItem1 = new JMenuItem("clear");
        JMenuItem historyItem2 = new JMenuItem("calculate");
        historyItem.addActionListener(this.control);
        historyItem1.addActionListener(this.control);
        historyItem2.addActionListener(this.control);
        fastMenu.add(historyItem);
        fastMenu.add(historyItem1);
        fastMenu.add(historyItem2);

        fastMenu.show(this,x,y);
    }

    /**
     *
     * @param x
     * @param y
     * @param menuContent
     */

    public void fastMenuHistory(int x, int y, ArrayList<String> menuContent){
        fastMenu.removeAll();

        int loc = 0;

        for(String s : menuContent){
            JMenuItem historyItem = new JMenuItem(s);
            historyItem.addActionListener(this.control);
            historyItem.setActionCommand("history"+loc);

            fastMenu.add(historyItem);
            loc++;
        }
        fastMenu.show(this,x,y);
    }

}