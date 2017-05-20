package hypergraphDB;

import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.scene.control.Hyperlink;

public class ViewElements {

	/**Надпись примеры и нумерация значений*/
	public void addExamplesLabel(int i, TextFlow wordMeaningArea) {
		// Нумерация значений
		String senseLineNum = i + 1 + ". ";
		Text numeration = new Text(senseLineNum);
        numeration.setFont(new Font(12));
        numeration.setStyle("-fx-font-weight: bold");
        
        // Отображение номера значения слова
		wordMeaningArea.getChildren().add(numeration);

        
	}
	
	/**Добавление надписи "Другие отношения"*/
	public TextFlow addOtherRelationsLabel(TextFlow wordMeaningArea, int languageNum) {
		// Заголовок "Другие типы отношений"
        Text[] otherRelationsLabel = {new Text("    Other relations: "), new Text("    Другие отношения: ")};
    	// Надпись "Other relations"
    	wordMeaningArea.getChildren().add(otherRelationsLabel[languageNum]);
    	// Текстовая область куда выводятся слова удовлетворяющие отношению
		TextFlow relationsTextFlow = new TextFlow();
    	relationsTextFlow.setMaxSize(0, 0);
    	relationsTextFlow.setVisible(false);
        return relationsTextFlow;
	}
	
	/**Добавление гиперссылок для других отношений*/
	public void addOtherRelationsHyperlinks(TextFlow wordMeaningArea, Hyperlink[] hyperlinks, TextFlow relationsTextFlow) {
		int length = hyperlinks.length;
		for (int i = 0; i < length; i++) {
			wordMeaningArea.getChildren().add(hyperlinks[i]);
			if (i != length - 1) 
				wordMeaningArea.getChildren().add(new Text(", "));
			else
				wordMeaningArea.getChildren().add(new Text(".\n"));
		}
		wordMeaningArea.getChildren().add(relationsTextFlow);  // Смещает отображаемые слова вниз
		wordMeaningArea.getChildren().add(new Text("\n"));
	}
	
	/**Создание заголовков для частей речи*/
	public void createHeader(Text[][] headers, String style) {
		int fontSize = 14;
		if (style.contains("italic")) {
			fontSize = 12;
		}
		int headersLength = headers.length;
		for (int i = 0; i < headersLength; i++) {
			for (int j = 0; j < 2; j++) {
	            headers[i][j].setFont(new Font(fontSize));
	            headers[i][j].setStyle(style);
	        }
		}
        
	}
	
}
