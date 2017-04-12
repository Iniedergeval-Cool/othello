package Game.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.HBox;

/**
 * Created by peterzen on 2017-04-12.
 * Part of the othello project.
 */
public class ControlsController {
    private @FXML HBox controlsBox;

    private boolean isBotPlaying;

    @FXML
    private CheckBox chkPlayAsBot;

    public void toggleBotPlaying(ActionEvent event) {
        isBotPlaying = chkPlayAsBot.isSelected();
    }

    public boolean isBotPlaying() {
        return isBotPlaying;
    }



    /**
     * Disable or enable all the controls
     */
    public void disableControls() {
        if (!controlsBox.isDisable()) {
            controlsBox.setDisable(true);
        }
    }

    public void enableControls() {
        if (controlsBox.isDisable()) {
            controlsBox.setDisable(false);
        }
    }

}
