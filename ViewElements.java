package hypergraphDB;

import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.scene.control.Hyperlink;

public class ViewElements {

	/**������� ������� � ��������� ��������*/
	public void addExamplesLabel(int i, TextFlow wordMeaningArea) {
		// ��������� ��������
		String senseLineNum = i + 1 + ". ";
		Text numeration = new Text(senseLineNum);
        numeration.setFont(new Font(12));
        numeration.setStyle("-fx-font-weight: bold");
        
        // ����������� ������ �������� �����
		wordMeaningArea.getChildren().add(numeration);

        
	}
	
	/**���������� ������� "������ ���������"*/
	public TextFlow addOtherRelationsLabel(TextFlow wordMeaningArea, int languageNum) {
		// ��������� "������ ���� ���������"
        Text[] otherRelationsLabel = {new Text("    Other relations: "), new Text("    ������ ���������: ")};
    	// ������� "Other relations"
    	wordMeaningArea.getChildren().add(otherRelationsLabel[languageNum]);
    	// ��������� ������� ���� ��������� ����� ��������������� ���������
		TextFlow relationsTextFlow = new TextFlow();
    	relationsTextFlow.setMaxSize(0, 0);
    	relationsTextFlow.setVisible(false);
        return relationsTextFlow;
	}
	
	/**���������� ����������� ��� ������ ���������*/
	public void addOtherRelationsHyperlinks(TextFlow wordMeaningArea, Hyperlink[] hyperlinks, TextFlow relationsTextFlow) {
		int length = hyperlinks.length;
		for (int i = 0; i < length; i++) {
			wordMeaningArea.getChildren().add(hyperlinks[i]);
			if (i != length - 1) 
				wordMeaningArea.getChildren().add(new Text(", "));
			else
				wordMeaningArea.getChildren().add(new Text(".\n"));
		}
		wordMeaningArea.getChildren().add(relationsTextFlow);  // ������� ������������ ����� ����
		wordMeaningArea.getChildren().add(new Text("\n"));
	}
	
	/**�������� ���������� ��� ������ ����*/
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
