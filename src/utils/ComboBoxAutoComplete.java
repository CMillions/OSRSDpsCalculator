package utils;

import java.util.stream.Stream;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.geometry.Point2D;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tooltip;
import javafx.scene.control.skin.ComboBoxListViewSkin;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Window;

/**
 * 
 * @author wsiqueir
 *
 * @param <T>
 * 
 * https://gist.github.com/jesuino/56de77be529e68384175
 * 
 * Modified tooltip display code from the above github
 * 
 * Adding a skin to the combobox allows us to consume events
 * to allow spaces in the filter string. We can also cancel
 * escape events by consuming the event
 */
public class ComboBoxAutoComplete<T> {

	private ComboBoxListViewSkin<T> cmbSkin;
	private ComboBox<T> cmb;
	String filter = "";
	private ObservableList<T> originalItems;

	public ComboBoxAutoComplete(ComboBox<T> cmb) 
	{
		this.cmb = cmb;
		cmbSkin = new ComboBoxListViewSkin<>(cmb);
		
		cmbSkin.getPopupContent().addEventFilter(KeyEvent.KEY_PRESSED, e -> {
			if(e.getCode() == KeyCode.SPACE)
			{
				filter += " ";
				e.consume();
				
				Window stage = cmb.getScene().getWindow();
				
				Point2D p = cmb.localToScene(0.0, 0.0);
				
				// Modified tooltip display from here: https://stackoverflow.com/questions/13049362/javafx-how-to-set-correct-tooltip-position
				double posX = p.getX() + cmb.getScene().getX() + cmb.getScene().getWindow().getX();
				double posY = p.getY() + cmb.getScene().getY() + cmb.getScene().getWindow().getY();
				
				cmb.getTooltip().setText(filter);
				cmb.getTooltip().show(stage, posX, posY);
			}
			
			if(e.getCode() == KeyCode.ESCAPE)
				e.consume();
		});
		
		this.cmb.setSkin(cmbSkin);
		
		originalItems = FXCollections.observableArrayList(cmb.getItems());
		cmb.setTooltip(new Tooltip());
		cmb.setOnKeyPressed(this::handleOnKeyPressed);
		cmb.setOnHidden(this::handleOnHiding);
	}

	public void handleOnKeyPressed(KeyEvent e) 
	{
		ObservableList<T> filteredList = FXCollections.observableArrayList();
		KeyCode code = e.getCode();
		
		if (code.isLetterKey()) 
		{
			filter += e.getText();
		}
		
		if (code == KeyCode.BACK_SPACE && filter.length() > 0) 
		{
			filter = filter.substring(0, filter.length() - 1);
			cmb.getItems().setAll(originalItems);
		}
		
		if (code == KeyCode.ESCAPE) 
		{
			filter = "";
		}
		
		if (filter.length() == 0) 
		{
			filteredList = originalItems;
			cmb.getTooltip().hide();
		} 
		
		else 
		{
			Stream<T> itens = cmb.getItems().stream();
			String txtUsr = filter.toString().toLowerCase();
			itens.filter(el -> el.toString().toLowerCase().contains(txtUsr)).forEach(filteredList::add);
			cmb.getTooltip().setText(txtUsr);
			Window stage = cmb.getScene().getWindow();
			
			Point2D p = cmb.localToScene(0.0, 0.0);
			
			// Modified tooltip display from here: https://stackoverflow.com/questions/13049362/javafx-how-to-set-correct-tooltip-position
			double posX = p.getX() + cmb.getScene().getX() + cmb.getScene().getWindow().getX();
			double posY = p.getY() + cmb.getScene().getY() + cmb.getScene().getWindow().getY();
			
			cmb.getTooltip().show(stage, posX, posY);
			cmb.show();
		}
		cmb.getItems().setAll(filteredList);
	}

	public void handleOnHiding(Event e) 
	{
		filter = "";
		cmb.getTooltip().hide();
		T s = cmb.getSelectionModel().getSelectedItem();
		cmb.getItems().setAll(originalItems);
		cmb.getSelectionModel().select(s);
	}

}