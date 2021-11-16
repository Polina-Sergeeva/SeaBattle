package battleShipClient.client.gui;

import battleShipClient.client.*;
import javax.swing.*;

public class GamePanel {
    private final JPanel panel;
    private final SidePanel ownSidePanel;
    private final SidePanel enemySidePanel;
    
    public GamePanel(ClientAction clientAction, ClientReaction clientReaction) {
        clientReaction.setGamePanel(this);
        
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setOpaque(false);
        
        ownSidePanel = new SidePanel(Text.YOUR_BOARD, false, null);
        enemySidePanel = new SidePanel(Text.ENEMY_BOARD, true, clientAction);
        
        panel.add(Box.createHorizontalGlue());
        panel.add(enemySidePanel.getPanel());
        panel.add(Box.createHorizontalGlue());
        panel.add(ownSidePanel.getPanel());
        panel.add(Box.createHorizontalGlue());
        panel.setVisible(true);
    }
    
    public JPanel getPanel() {
        return panel;
    }
    
    public SidePanel getOwnSidePanel() {
        return ownSidePanel;
    }
    
    public SidePanel getEnemySidePanel() {
        return enemySidePanel;
    }
}