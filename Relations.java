package hypergraphDB;
import javafx.scene.text.TextFlow;

import java.util.ArrayList;
import java.util.List;

import javax.security.auth.callback.LanguageCallback;

import org.hypergraphdb.HGHandle;
import org.hypergraphdb.app.wordnet.data.KindOf;
import org.hypergraphdb.app.wordnet.data.PartOf;
import org.hypergraphdb.app.wordnet.data.SynsetLink;
import org.hypergraphdb.app.wordnet.data.Word;
import org.hypergraphdb.app.wordnet.WNGraph;
import org.hypergraphdb.HyperGraph;
import org.hypergraphdb.HGQuery.hg;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.text.Font;
import javafx.scene.text.Text;


public class Relations {
	
	/**Добавление примеров употребления слов. Нумерация значений*/
	public void addExamples(int i, SynsetLink nounSynset, TextFlow wordMeaningArea, int languageNum) {
		ViewElements viewElements = new ViewElements();
		// Нумерация значений
		String senseLineNum = i + 1 + ". ";
		Text numeration = new Text(senseLineNum);
        numeration.setFont(new Font(12));
        numeration.setStyle("-fx-font-weight: bold");
        
        // Отображение номера значения слова
		wordMeaningArea.getChildren().add(numeration);
		
		// Заголок для примеров
		Text[][] examplesHeader = {{new Text("    Examples: "), new Text("    Примеры: ")}};
		String style = "-fx-font-style: italic";
		viewElements.createHeader(examplesHeader, style);

        
		// Список значений и примеров
		String[] meaningAndExamples = nounSynset.getGloss().split(";");
		// 1й элемент - значение, последующие - примеры.
		wordMeaningArea.getChildren().add(
				new Text(firstUpperCase(meaningAndExamples[0]) + "\n"));
		
		// Вывод примеров
		int end = meaningAndExamples.length;
		// Надпись Examples: 
		wordMeaningArea.getChildren().add(examplesHeader[0][languageNum]);
		// Вывод примеров на экран
		for (int k = 1; k < end; k++) {
			meaningAndExamples[k] = meaningAndExamples[k].replace("\"", "").trim();
			wordMeaningArea.getChildren().add(
					new Text("\"" + firstUpperCase(meaningAndExamples[k]) + "\". "));
		}
	}
	
	public void addSynonyms(WNGraph wnGraph, HyperGraph graph, SynsetLink synset,
			TextFlow wordMeaningArea, TextField wordSearchField, Button searchButton, String type, int languageNum) {
		
		// Заголовок "Синонимы"
		ViewElements viewElements = new ViewElements();
        Text[][] synonymsLabel = {{new Text("\n    Synonyms: "), new Text("\n    Синонимы: ")}};
		String style = "-fx-font-style: italic";
		viewElements.createHeader(synonymsLabel, style);
        
        
        // Вывод надписи "Синонимы"
		wordMeaningArea.getChildren().add(synonymsLabel[0][languageNum]);
		
		List<Hyperlink> links = new ArrayList<Hyperlink>(); 
		
	    // Проход по синонимам
		synset.forEach(h -> {
			Word w = graph.get(h); // Слово
			
			List<HGHandle> syns = null;
			
			// Значения синонимов для всплывающего окна подсказки
			if (type == "Noun"){
				syns = wnGraph.getNounSenses(h);
			}
			else if (type == "Verb") {
				syns = wnGraph.getVerbSenses(h);
			}
			else if (type == "Adjective") {
				syns = wnGraph.getAdjSenses(h);
			}
			else {
				syns = wnGraph.getAdverbSenses(h);
			}
			
			String hyperlinkTip = "";
			
			// Загрузка значений слова в подсказку
			for (HGHandle def: syns) {
				SynsetLink ws = graph.get(def);
				hyperlinkTip +=  firstUpperCase(ws.getGloss()) + "\n";
			}
			// Создание гиперссылки синонима
			Hyperlink link = new Hyperlink();
			link.setOnAction(new EventHandler<ActionEvent>() {
			    @Override
			    public void handle(ActionEvent e) {
			       wordSearchField.setText(link.getText());
			       searchButton.fire();
			    }
			});
			
    		link.setText(w.getLemma());
    		link.setTooltip(new Tooltip(hyperlinkTip));
    		links.add(link);
		});
		
		// Вывод синонимов
    	int linksSize = links.size();
    	int dotIndex = linksSize - 1; // Место где нужно поставить точку
    	for (int j = 0; j < linksSize; j++) {
    		wordMeaningArea.getChildren().add(links.get(j));
    		if (j != dotIndex) {
    			wordMeaningArea.getChildren().add(new Text(","));
    		}
    		else {
    			wordMeaningArea.getChildren().add(new Text(".\n"));
    		}
    	}
    	links.clear(); // Очистка гиперссылок на синонимы
	}
	
	
	public Hyperlink addHyponyms(HyperGraph graph, SynsetLink nounSynset, TextFlow wordMeaningArea, TextField wordSearchField, Button searchButton, TextFlow relationsTextFlow , int languageNum) {

    	Hyperlink kindOfLink = new Hyperlink();
    	String[] relationName = {"Hyponymy", "Гипонимия"};
    	kindOfLink.setText(relationName[languageNum]);
    	kindOfLink.setStyle("-fx-text-fill:blue");
		kindOfLink.setOnAction(new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent e) {
		    	int relationTextFlowSize = relationsTextFlow.getChildren().size() - 1 ;
		    	// Очистка текстового поля под другими видами отношений (не работает!!!)
		    	relationsTextFlow.getChildren().removeAll(new Hyperlink());
		    	
		    	List<KindOf> hyponymsList = graph.getAll(hg.and(hg.type(KindOf.class),
    	        		hg.incident(graph.getHandle(nounSynset))));
    	        
    	        // Получение списка гипонимов
    	        int hyponymListSize = hyponymsList.size();
    	        for (int p = 0; p < hyponymListSize; p++) {
    	        	KindOf hyponym = hyponymsList.get(p);
    	        	for (int z = 0; z < hyponym.getArity(); z++) {
    	        		HGHandle handle = hyponym.getTargetAt(z);
    	        		SynsetLink sen = graph.get(handle);
    	        		if (!sen.equals(nounSynset)) {
    	        			sen.forEach(c -> {
    	            			Word tempWord = graph.get(c);
        	            		Hyperlink hlink = new Hyperlink();
    			            	hlink.setText(tempWord.getLemma() + " ");
    			            	hlink.setOnAction(new EventHandler<ActionEvent>() {
    	            			    @Override
    	            			    public void handle(ActionEvent e) {
    	            			    	wordSearchField.setText(tempWord.getLemma());
 		            			        searchButton.fire();
    	            			    }
    			            	});
    			            	relationsTextFlow.getChildren().add(hlink);
    	            		});
    	        		}
    	        		
    	        	}
    	        }
    	        relationsTextFlow.setVisible(true);
    	        // Зависимость высоты окна от количесва гипонимов
    	        int height = 150 - 120 / (hyponymListSize + 1);
    	        relationsTextFlow.setMaxSize(400, height);
		     }
		});
		
		return kindOfLink;
	}
	
	public Hyperlink addMeronyms(HyperGraph graph, SynsetLink nounSynset, TextFlow wordMeaningArea, TextField wordSearchField, Button searchButton, TextFlow relationsTextFlow, int languageNum) {
		
    	
		// Отношение меронимии (часть-целое)
    	Hyperlink partOfLink = new Hyperlink();
    	String[] relationName = {"Meronymy", "Меронимия"};
    	partOfLink.setText(relationName[languageNum]);
    	partOfLink.setStyle("-fx-text-fill:blue");
    	partOfLink.setOnAction(new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent e) {
		    	int relationTextFlowSize = relationsTextFlow.getChildren().size() - 1 ;
		    	relationsTextFlow.getChildren().removeAll(new Hyperlink());
		    	
		    	List<PartOf> meronymyList = graph.getAll(hg.and(hg.type(PartOf.class),
    	        		hg.incident(graph.getHandle(nounSynset))));
    	        
    	        // Получение списка меронимов и вывод их на экран
    	        int meronymyListSize = meronymyList.size();
    	        for (int p = 0; p < meronymyListSize; p++) {
    	        	PartOf meronym = meronymyList.get(p);
    	        	for (int z = 0; z < meronym.getArity(); z++) {
    	        		HGHandle handle = meronym.getTargetAt(z);
    	        		SynsetLink sen = graph.get(handle);
    	        		if (!sen.equals(nounSynset)) {
    	        			sen.forEach(c -> {
    	            			Word tempWord = graph.get(c);
        	            		Hyperlink hlink = new Hyperlink();
    			            	hlink.setText(tempWord.getLemma() + " ");
    			            	hlink.setOnAction(new EventHandler<ActionEvent>() {
    	            			    @Override
    	            			    public void handle(ActionEvent e) {
    	            			    	wordSearchField.setText(tempWord.getLemma());
 		            			        searchButton.fire();
    	            			    }
    			            	});
    			            	relationsTextFlow.getChildren().add(hlink);
    	            		});
    	        		}
    	        		
    	        	}
    	        }
    	        
    	        // Отображение textflow с меронимами
    	        relationsTextFlow.setVisible(true);
    	        if (meronymyListSize == 0) {
    	        	Text notFoundText = new Text("    Not found");
    	        	notFoundText.setStyle("-fx-font-weight:bold;");
    	        	relationsTextFlow.getChildren().add(notFoundText);
    	        	relationsTextFlow.setMaxSize(400, 40);
    	        }
    	        else {
    	        	int height = 150 - 120 / (meronymyListSize + 1);
        	        relationsTextFlow.setMaxSize(400, height);
    	        }
        		
    	        
		     }
		});
    	
    	return partOfLink;
	}
	public String firstUpperCase(String word){
		if(word == null || word.isEmpty()) return "";//или return word;
		return word.substring(0, 1).toUpperCase() + word.substring(1);
	}
}
