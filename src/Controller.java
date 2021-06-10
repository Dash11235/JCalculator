import javax.swing.*;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Controller implements ActionListener, MouseListener {

    private ViewFrame viewframe;
    private ViewPanel viewpanel;
    private Model model;

    int lastFastMenuPosX = 0;
    int lastFastMenuPosY = 0;

    public static void main(String[] args) {
        new Controller();
    }

    public Controller() {
        viewpanel = new ViewPanel(this);
        viewframe = new ViewFrame(viewpanel);
        model = new Model();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object o = e.getActionCommand();

        String numbers = "1234567890.+-*/%()";
        String[] numberArray = numbers.split("");


        if (!viewpanel.getInput().equals("error") && !viewpanel.getInput().equals("Infinity")) {
            for (int i = 0; i < numberArray.length; i++) {
                if (o.equals(numberArray[i])) {
                    String str = model.addNum(viewpanel.getInput(), numberArray[i]);
                    viewpanel.setLabelText(str);
                }
            }

            if (o.equals("=") || o.equals("calculate")) {
                String str = "";
                if(!viewpanel.getInput().equals("")) {
                    str = model.calculate(viewpanel.getInput());
                }
                if(!str.equals("error")) {
                    try {
                        model.addHistory("src/cache/cache.txt", viewpanel.getInput() + "=" + str);
                        model.cacheControl("src/cache/cache.txt");
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
                viewpanel.setLabelText(str);
            }

            if (o.equals("<------")) {
                String str = model.delete(viewpanel.getInput());
                viewpanel.setLabelText(str);
            }

            if (o.equals("History")) {
                try {
                    viewpanel.fastMenuHistory(lastFastMenuPosX, lastFastMenuPosY, model.readHistory("src/cache/cache.txt"));
                } catch (FileNotFoundException fileNotFoundException) {
                    fileNotFoundException.printStackTrace();
                }
            }

            try {
                for (int i = 0; i < model.readHistory("src/cache/cache.txt").size(); i++) {
                    if (o.equals("history" + i)) {
                        viewpanel.setLabelText(model.historyItemAtIndex(i, "src/cache/cache.txt"));
                    }
                }
            } catch (FileNotFoundException fileNotFoundException) {
                fileNotFoundException.printStackTrace();
            }
        }

        if (o.equals("c") || o.equals("clear")) {
            String str = model.clear(viewpanel.getInput());
            viewpanel.setLabelText(str);
        }

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (SwingUtilities.isRightMouseButton(e)) {
            viewpanel.showFastMenu(e.getX(), e.getY());
        }
        lastFastMenuPosX = e.getX();
        lastFastMenuPosY = e.getY();
    }

    @Override
    public void mousePressed(MouseEvent e) { }

    @Override
    public void mouseReleased(MouseEvent e) { }

    @Override
    public void mouseEntered(MouseEvent e) { }

    @Override
    public void mouseExited(MouseEvent e) { }
}