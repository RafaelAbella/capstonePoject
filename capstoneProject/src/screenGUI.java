import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class screenGUI extends historyGUI{

    private JLabel guiLabel;
    private JPanel screenPanel;
    private JButton playGameButton;
    private JButton gameHistoryButton;

    public screenGUI(){
        playGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new gameGUI();
            }
        });


        //not yet finished demo only
        gameHistoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String msg = null;
                int gameHistory=0;
                if(gameHistory==0){
                    msg="No games played yet";
                    JOptionPane.showMessageDialog(null,msg,"Error",JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    public JPanel getScreenPanel(){
        return screenPanel;
    }
}



