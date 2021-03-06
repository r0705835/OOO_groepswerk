package ui.controller;

import domain.model.PartyItem;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ui.view.RepairView;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

class RepairController {

    private RepairView view;
    private Stage primaryStage;
    private MainController mainController;

    RepairController() {
        this.view = new RepairView();
    }

    void launch(MainController controller) {
        this.mainController = controller;
        this.primaryStage = mainController.getPrimaryStage();
        view.setCancelListener(() -> mainController.launch(primaryStage));
        view.setConfirmListener(this::handleInput);
        showAllPartyItems();
        Scene addScene = new Scene(view.getRootView(), 500, 600);
        primaryStage.setScene(addScene);
        primaryStage.show();
    }

    private void handleInput(String number) {
        List<PartyItem> partyItems = getPartyItemsSorted();
        try {
            // TODO does this minus one logic belong here?
            PartyItem item = partyItems.get(Integer.parseInt(number) - 1);
            item.repair();
            mainController.launch(primaryStage);
        } catch (NumberFormatException e) {
            view.clearTextField();
        } catch (IllegalStateException e) {
            view.setChoiceTextField("Can't repair undamaged item.");
        }
    }

    private void showAllPartyItems() {
        List<PartyItem> partyItems = getPartyItemsSorted();
        view.setPartyItems(partyItems);
    }

    private List<PartyItem> getPartyItemsSorted() {
        List<PartyItem> partyItems = mainController.getPartyItemDb().getAll();
        return partyItems.stream().sorted(Comparator.comparing(PartyItem::getName)).collect(Collectors.toList());
    }
}
