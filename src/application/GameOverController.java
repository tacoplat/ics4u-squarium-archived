package application;

import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class GameOverController extends Controller {

    // FXML elements
    @FXML
    private Text gameOverScore;

    /**
     * Default JavaFX initialize method.
     */
    public void initialize() {
        gameOverScore.setText("Score: " + GameController.getStaticScore());
    }
}
