package common;

import gui.base.MainGUIController;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import main.Main;

/**
 * Created by no-one on 14.12.16.
 */
public final class CommonTask{

    public static EventHandler<WorkerStateEvent> onSuccessSimpleError(Task t, String suc, String fail){
        return new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                Error e = (Error) t.getValue();
                if(e != null)
                    Main.gui.showDialog("Błąd",fail, e.getMessage(), Alert.AlertType.ERROR);
                else
                    Main.gui.showDialog("Info",suc,"", Alert.AlertType.INFORMATION);
            }
        };
    }

}
