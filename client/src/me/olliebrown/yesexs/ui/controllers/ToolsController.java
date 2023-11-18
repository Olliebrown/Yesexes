package me.olliebrown.yesexs.ui.controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import them.mdbell.javafx.control.FormattedTableCell;
import me.olliebrown.yesexs.core.Debugger;
import me.olliebrown.yesexs.core.MemoryInfo;
import me.olliebrown.yesexs.core.MemoryType;
import me.olliebrown.yesexs.core.Result;
import me.olliebrown.yesexs.misc.ExpressionEvaluator;
import me.olliebrown.yesexs.ui.menus.MemoryInfoContextMenu;
import me.olliebrown.yesexs.ui.models.MemoryInfoTableModel;
import them.mdbell.util.HexUtils;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;

public class ToolsController implements IController {

    public TextField expression;
    public TextField expressionResult;
    private MainController mc;

    @FXML
    ListView<Long> pidList;
    @FXML
    TableView<MemoryInfoTableModel> memInfoTable;
    @FXML
    TableColumn<MemoryInfoTableModel, String> memInfoName;
    @FXML
    TableColumn<MemoryInfoTableModel, Number> memInfoAddr;
    @FXML
    TableColumn<MemoryInfoTableModel, Number> memInfoSize;
    @FXML
    TableColumn<MemoryInfoTableModel, MemoryType> memInfoType;
    @FXML
    TableColumn<MemoryInfoTableModel, Number> memInfoPerm;

    @FXML
    VBox toolsTabPage;

    @FXML
    Label toolsTitleId;

    private ObservableList<MemoryInfoTableModel> memoryInfoList;

    private final Map<String, Long> vars = new ConcurrentHashMap<>();

    private final ExpressionEvaluator evaluator = new ExpressionEvaluator(new ExpressionEvaluator.VariableProvider() {
        @Override
        public long get(String name) {
            return vars.get(name);
        }

        @Override
        public boolean containsVar(String value) {
            return vars.containsKey(value);
        }
    }, addr -> {
        Debugger debugger = mc.getDebugger();
        if (!debugger.connected()) {
            return 0;
        }
        return debugger.peek64(addr);
    });

    @Override
    public void initialize(URL url, ResourceBundle bundle) {
        memoryInfoList = FXCollections.observableArrayList();

        for (TableColumn<?,?> c : memInfoTable.getColumns()) {
            c.setReorderable(false);
        }
        memInfoTable.setItems(new SortedList<>(memoryInfoList, (info1, info2) -> {
            long addr1 = info1.getAddr();
            long addr2 = info2.getAddr();
            return Long.compare(addr1, addr2);
        }));

        memInfoName.setCellValueFactory(param -> param.getValue().nameProperty());
        memInfoAddr.setCellValueFactory(param -> param.getValue().addrProperty());
        memInfoSize.setCellValueFactory(param -> param.getValue().sizeProperty());
        memInfoType.setCellValueFactory(param -> param.getValue().typeProperty());
        memInfoPerm.setCellValueFactory(param -> param.getValue().accessProperty());

        memInfoAddr.setCellFactory(param -> new FormattedTableCell<>(addr -> HexUtils.formatAddress(addr.longValue())));
        memInfoSize.setCellFactory(param -> new FormattedTableCell<>(size -> HexUtils.formatSize(size.longValue())));
        memInfoPerm.setCellFactory(param -> new FormattedTableCell<>(access -> HexUtils.formatAccess(access.intValue())));

        pidList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                return;
            }
            displayTitleId(mc.getDebugger().getTitleId(newValue));
        });

        MemoryInfoContextMenu cm = new MemoryInfoContextMenu(bundle, () -> mc, memInfoTable);
        memInfoTable.contextMenuProperty().setValue(cm);
    }

    public void setMainController(MainController c) {
        this.mc = c;
    }

    @Override
    public void onConnect() {
        toolsTabPage.setDisable(false);
    }

    @Override
    public void onDisconnect() {
        toolsTabPage.setDisable(true);
    }

    public void setPidsList() {
        pidList.getItems().clear();
        Debugger debugger = mc.getDebugger();
        if (debugger.connected()) {
            for (long pid : debugger.getPids()) {
                pidList.getItems().add(pid);
            }
        } else {
            MainController.showMessage("You are not currently connected.", Alert.AlertType.WARNING);
        }
    }

    public void detachProcess() {
        Debugger conn = mc.getDebugger();
        if (conn.connected() && conn.attached()) {
            Result result = conn.detach();
            if (result.succeeded()) {
                mc.setTitle(null);
                mc.setStatus("Detached from process");
                memoryInfoList.clear();
            } else {
                MainController.showMessage("Detach failed.", result, Alert.AlertType.ERROR);
                mc.setStatus("Error");
            }
        } else {
            MainController.showMessage("Not attached.", Alert.AlertType.WARNING);
        }
    }

    public void attachProcess() {
        Debugger conn = mc.getDebugger();
        if (conn.connected()) {
            long selectedPid = pidList.getSelectionModel().getSelectedItem();

            if (selectedPid != 0) {
                Result attached = conn.attach(selectedPid);
                if (attached.failed()) {
                    MainController.showMessage("Attach failed.", attached, Alert.AlertType.ERROR);
                } else {
                    mc.setStatus("Attached to process");
                    mc.setTitle("Attached to: 0x" + HexUtils.formatTitleId(conn.getTitleId(selectedPid)));
                    mc.memory().populateMemory();
                }
            } else {
                MainController.showMessage("Invalid pid selected.", Alert.AlertType.ERROR);
            }
        } else {
            MainController.showMessage("You are not currently connected.", Alert.AlertType.WARNING);
        }
    }

    public void ReattachProcess() {
        Debugger conn = mc.getDebugger();
        if (conn.connected()) {
            long selectedPid = pidList.getSelectionModel().getSelectedItem();

            if (selectedPid != 0) {
                Result attached = conn.attach(selectedPid);
                if (attached.failed()) {
                    MainController.showMessage("Attach failed.", attached, Alert.AlertType.ERROR);
                } else {
                    mc.setStatus("Attached to process");
                    mc.setTitle("Attached to: 0x" + HexUtils.formatTitleId(conn.getTitleId(selectedPid)));
                    mc.memory().populateMemory();
                }
            } else {
                MainController.showMessage("Invalid pid selected.", Alert.AlertType.ERROR);
            }
        } else {
            MainController.showMessage("You are not currently connected.", Alert.AlertType.WARNING);
        }
    }

    public void displayTitleId(long titleId) {
        Platform.runLater(() -> toolsTitleId.setText("Title Id:" + (titleId == -1 ? "N/A" : HexUtils.formatTitleId(titleId))));
    }

    public void updateMemoryInfo(MemoryInfo[] info) {
        memoryInfoList.clear();
        int moduleCount = 0;

        for (MemoryInfo m : info) {
            if (m.getType() == MemoryType.CODE_STATIC && m.getPerm() == 0b101) {
                moduleCount++;
            }
        }
        int mod = 0;
        boolean heap = false;
        for (MemoryInfo m : info) {
            String name = "-";
            if (m.getType() == MemoryType.HEAP && !heap) {
                heap = true;
                name = "heap";
            }
            if (m.getType() == MemoryType.CODE_STATIC && m.getPerm() == 0b101) {
                if (mod == 0 && moduleCount == 1) {
                    name = "main";
                }
                if (moduleCount > 1) {
                    name = switch (mod) {
                        case 0 -> "rtld";
                        case 1 -> "main";
                        case 2 -> "sdk";
                        default -> "subsdk" + (mod - 2);
                    };
                }
                mod++;
            }
            if (!name.equals("-")) {
                vars.put(name, m.getAddress());
            }
            if (m.getType() != MemoryType.UNMAPPED) {
                memoryInfoList.add(new MemoryInfoTableModel(name, m));
            }
        }
    }

    public ExpressionEvaluator getEvaluator() {
        return evaluator;
    }

    public void onParse() {
        try {
            String str = expression.getText();
            expressionResult.setText(HexUtils.formatAddress(evaluator.eval(str)));
        } catch (Exception e) {
            MainController.showMessage(e);
        }
    }
}
