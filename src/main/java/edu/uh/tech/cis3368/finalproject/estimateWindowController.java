package edu.uh.tech.cis3368.finalproject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import java.text.DecimalFormat;
/**
 * Controller for EstimateWindow that will add up totals and will validate costs.
 */

public class estimateWindowController {

    @FXML
    private ComboBox<String> item3menu;
    @FXML
    private TextField totalItem1;
    @FXML
    private TextField totalItem2;
    @FXML
    private CheckBox ShippingCheck;
    @FXML
    private TextField TotalItem3;
    @FXML
    private TextField item2amount;
    @FXML
    private TextField WholesaleTotal;
    @FXML
    private ComboBox<String> item2menu;
    @FXML
    private TextField item3amount;
    @FXML
    private TextField item1amount;
    @FXML
    private ComboBox<String> item1menu;
    @FXML
    private Button CalculateButton;
    @FXML
    private ComboBox<String> shippingOptions;
    @FXML
    private TextField CostShippingField;
    @FXML
    private TextField markup;
    @FXML
    private TextField tax;
    @FXML
    private TextField GrandTotal;

    /**
     * Initialize will set the options for the drop menu the user can use to select an item
     */
    public void initialize() {
        shippingOptions.getItems().removeAll(shippingOptions.getItems());
        shippingOptions.getItems().addAll("Ground Shipping  5.99", "2-day Shipping  7.99", "Overnight Shipping  14.99");
        item1menu.getItems().addAll("Iron  1.99", "Wood  2.95", "Brick  1.99", "Leather  7.15", "Diamond  99.95");
        item2menu.getItems().addAll("Iron  1.99", "Wood  2.95", "Brick  1.99", "Leather  7.15", "Diamond  99.95");
        item3menu.getItems().addAll("Iron  1.99", "Wood  2.95", "Brick  1.99", "Leather  7.15", "Diamond  99.95");

    }

    public String getShippingOptions() {
        return shippingOptions.getSelectionModel().getSelectedItem();
    }

    /**
     * Calculate will add up all the totals, calculate tax, markup, and shipping costs.
     * @param event
     */
    @FXML
    void calculate(ActionEvent event) {
        double cost1 = getFromMenu(item1menu);
        double amount1 = Double.parseDouble(item1amount.getText());
        double total1 = cost1*amount1;
        totalItem1.setText(Double.toString(total1));

        double cost2 = getFromMenu(item2menu);
        double amount2 = Double.parseDouble(item2amount.getText());
        double total2 = cost2*amount2;
        totalItem2.setText(Double.toString(total2));

        double cost3 = getFromMenu(item3menu);
        double amount3 = Double.parseDouble(item3amount.getText());
        double total3 = cost3*amount3;
        TotalItem3.setText(Double.toString(total3));

        double shippingCost = 0;
        String shippingSelected = getShippingOptions();
        if (shippingSelected == "Options"){
            shippingCost = 0;
        }
        else if (shippingSelected == "Ground Shipping  5.99") {
            shippingCost = 5.99;
        }
        else if (shippingSelected == "2-day Shipping  7.99") {
            shippingCost = 7.99;
        }
        else if (shippingSelected == "Overnight Shipping  14.99") {
            shippingCost = 14.99;
        }
        if(!ShippingCheck.isSelected())
            shippingCost = 0;

        CostShippingField.setText(Double.toString(shippingCost));

        double wholesale = total1 + total2 + total3;
        double markupAmount = wholesale * 0.15;
        double taxAmount = (wholesale+markupAmount)*0.034;
        double Totaltotal = wholesale + taxAmount + markupAmount + shippingCost;


        DecimalFormat dc = new DecimalFormat("0.00");
        String formattedText = dc.format(wholesale);
        WholesaleTotal.setText(formattedText);

        formattedText = dc.format(taxAmount);
        tax.setText(formattedText);

        formattedText = dc.format(markupAmount);
        markup.setText(formattedText);

        formattedText = dc.format(Totaltotal);
        GrandTotal.setText(formattedText);
    }

    Double getFromMenu(ComboBox<String> menu){
        String selected =  menu.getSelectionModel().getSelectedItem();
        if (selected == "Wood  2.95")
            return 2.95;
        else if (selected == "Iron  1.99")
            return 1.99;
        else if (selected == "Leather  7.15")
            return 7.15;
        else if (selected == "Diamond  99.95")
            return 99.95;
        else if (selected == "Brick  1.99")
            return 1.99;

        return 0.0;
    }

    /**
     * If shipping is checked, the grand total will add an amount to cover for shipping costs
     * @param event
     */
    @FXML
    void shippingChecked(ActionEvent event) {
        if (ShippingCheck.isSelected()){
            shippingOptions.setDisable(false);
        }
        else {
            shippingOptions.setDisable(true);
        }
    }

}
