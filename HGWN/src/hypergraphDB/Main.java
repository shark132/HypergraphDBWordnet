package hypergraphDB;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javafx.application.Application;
import org.hypergraphdb.HGConfiguration;
import org.hypergraphdb.HGEnvironment;
import org.hypergraphdb.HGQuery.hg;
import org.hypergraphdb.HyperGraph;
import org.hypergraphdb.algorithms.DefaultALGenerator;
import org.hypergraphdb.algorithms.HGALGenerator;
import org.hypergraphdb.algorithms.HGBreadthFirstTraversal;
import org.hypergraphdb.algorithms.HGTraversal;
import org.hypergraphdb.app.wordnet.HGWordNetLoader;
import org.hypergraphdb.app.wordnet.SemTools;
import org.hypergraphdb.HGHandle;
import org.hypergraphdb.HGPlainLink;
import org.hypergraphdb.app.wordnet.WNGraph;
import org.hypergraphdb.app.wordnet.data.*;
import org.hypergraphdb.app.wordnet.ext.ConceptualDensity;
import org.hypergraphdb.util.Pair;
import static java.nio.charset.StandardCharsets.*;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.text.TextFlow;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;

import java.awt.Color;
import java.lang.reflect.Field;
import java.net.URL;

public class Main extends Application {
	public static List<HGHandle> NounList;
	public static List<HGHandle> VerbList;
	public static List<HGHandle> AdjList;
	public static List<HGHandle> AdverbList;
	
	public static HyperGraph graph;
	public static HyperGraph[] graphArray = new HyperGraph[2];
	public static WNGraph wnGraph;
	public static WNGraph[] wnGraphArray = new WNGraph[2];
	public static HGHandle wordH;
	public static HGHandle reqWord;
	public static int wordnetNum = 0;  // Номер используемого ворднета (английский - 0, русский - 1)
	
	
	@Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("WordNet Editor");
        
        TextField wordSearchField = new TextField();
        wordSearchField.setPrefSize(150, 20);
        
        TextFlow wordMeaningArea = new TextFlow();
        wordMeaningArea.setMaxSize(400,400);
        wordMeaningArea.setPrefSize(400,400);
//        wordMeaningArea.setVisible(false);
//        wordMeaningArea.setEditable(false);
//        wordMeaningArea.setWrapText(true);
//        
        Button searchButton = new Button();
        searchButton.setText("Search");
        Button enWordnetButton = new Button();
        enWordnetButton.setText("EnWordNet");
        Button ruWordnetButton = new Button();
        ruWordnetButton.setText("RuWordnet");
        
        Text NounHeader = new Text("Noun:\n");
        NounHeader.setFont(new Font(14));
        NounHeader.setStyle("-fx-font-weight: bold");
        
        Text VerbHeader = new Text("Verb: \n");
        VerbHeader.setFont(new Font(14));
        VerbHeader.setStyle("-fx-font-weight: bold");
        
        Text AdjHeader = new Text("Adjecive: \n");
        AdjHeader.setFont(new Font(14));
        AdjHeader.setStyle("-fx-font-weight: bold");
        
        Text AdverbHeader = new Text("Adverb: \n");
        AdverbHeader.setFont(new Font(14));
        AdverbHeader.setStyle("-fx-font-weight: bold");
        
        extText exampHeader = new extText("    Examples: ");
        exampHeader.setFont(new Font(12));
        exampHeader.setStyle("-fx-font-style: italic");
        
        
        
//        //Getting and printing synonyms of the word "dog".
//        List<String> synonyms = new ArrayList<String>();
//        String synString = null; // String of synonyms in one line
//        HGHandle dogHandle = wnGraph.findWord("dog");
//        Word dogWord = graph.get(dogHandle); // Target word 
//        List<SynsetLink> dogSenses = hg.getAll(graph,hg.and(hg.type(NounSynsetLink.class),
//        		hg.incident(dogHandle)));
//        
//        for (SynsetLink dogSense : dogSenses) {
//        	System.out.println("Synonyms that mean '" + dogSense.getGloss() + "':");
//        	dogSense.forEach(h -> {
//        		Word tmpWord = graph.get(h);
//        		if (!tmpWord.equals(dogWord))
//        			synonyms.add(tmpWord.getLemma());
//        	});
//        	synString = String.join(",", synonyms) + ".";
//        	System.out.println(synString);
//        }
        

//        // Finding the word "big"
//        HGHandle bigWord = wnGraph.findWord("big");
//        // Getting all senses of the word
//        List<SynsetLink> adjSenseList = hg.getAll(graph,hg.and(hg.type(AdjSynsetLink.class),
//        		hg.incident(bigWord)));
//        // Getting the only meaning that has antonyms. It is number 9 (according to tests)
//    	SynsetLink senseWithAntonyms = adjSenseList.get(9); 
//    	// Getting the list of antonyms
//        List<Antonym> antonymList = graph.getAll(hg.and(hg.type(Antonym.class),
//        		hg.incident(graph.getHandle(senseWithAntonyms))));
//        System.out.println("Adjective's meaning: " + senseWithAntonyms.getGloss());
//        
//        // Getting and printing antonyms
//        int antonymListSize = antonymList.size();
//        for (int i = 0; i < antonymListSize; i++) {
//        	Antonym antonym = antonymList.get(i);
//        	for (int k = 0; k < antonym.getArity(); k++) {
//        		HGHandle handle = antonym.getTargetAt(k);
//        		SynsetLink sen = graph.get(handle);
//        		if (!sen.equals(senseWithAntonyms)) {
//        			sen.forEach(c -> {
//            			Word tempWord = graph.get(c);
//            			if (!tempWord.equals(bigWord)) {
//            				System.out.println(tempWord.getLemma());
//            			}
//            		});
//        		}
//        		
//        	}
//        }
        
        
//        
//        
        /**
         * Отношение Kind of. Отображает связть более общего слова с более конкретным
         * Например, собака - овчарка и
        */
       
        /*HGHandle bigWord = wnGraph.findWord("animal");
        // Getting all senses of the word
        List<SynsetLink> adjSenseList = hg.getAll(graph,hg.and(hg.type(NounSynsetLink.class),
        		hg.incident(bigWord)));
        // Getting the only meaning that has antonyms. It is number 9 (according to tests)
    	SynsetLink senseWithAntonyms = adjSenseList.get(0); 
    	// Getting the list of antonyms
        List<KindOf> antonymList = graph.getAll(hg.and(hg.type(KindOf.class),
        		hg.incident(graph.getHandle(senseWithAntonyms))));
        System.out.println("Adjective's meaning: " + senseWithAntonyms.getGloss());
        
        // Getting and printing antonyms
        int antonymListSize = antonymList.size();
        for (int i = 0; i < antonymListSize; i++) {
        	KindOf antonym = antonymList.get(i);
        	for (int k = 0; k < antonym.getArity(); k++) {
        		HGHandle handle = antonym.getTargetAt(k);
        		SynsetLink sen = graph.get(handle);
        		if (!sen.equals(senseWithAntonyms)) {
        			sen.forEach(c -> {
            			Word tempWord = graph.get(c);
            			if (!tempWord.equals(bigWord)) {
            				System.out.println(tempWord.getLemma());
            			}
            		});
        		}
        		
        	}
        }
        */
        
//        HGHandle bigWord = wnGraph.findWord("chair");
//        // Getting all senses of the word
//        List<SynsetLink> adjSenseList = hg.getAll(graph,hg.and(hg.type(NounSynsetLink.class),
//        		hg.incident(bigWord)));
//        // Getting the only meaning that has antonyms. It is number 9 (according to tests)
//    	SynsetLink senseWithAntonyms = adjSenseList.get(3); 
//    	// Getting the list of antonyms
//        List<PartOf> antonymList = graph.getAll(hg.and(hg.type(PartOf.class),
//        		hg.incident(graph.getHandle(senseWithAntonyms))));
//        System.out.println("Adjective's meaning: " + senseWithAntonyms.getGloss());
//        
//        // Getting and printing antonyms
//        int antonymListSize = antonymList.size();
//        System.out.println(antonymListSize);
//        for (int i = 0; i < antonymListSize; i++) {
//        	PartOf antonym = antonymList.get(i);
//        	for (int k = 0; k < antonym.getArity(); k++) {
//        		HGHandle handle = antonym.getTargetAt(k);
//        		SynsetLink sen = graph.get(handle);
//        		if (!sen.equals(senseWithAntonyms)) {
//        			sen.forEach(c -> {
//            			Word tempWord = graph.get(c);
//            			if (!tempWord.equals(bigWord)) {
//            				System.out.println(tempWord.getLemma());
//            			}
//            		});
//        		}
//        		
//        	}
//        }
        
        
     // Finding the word "big"
//      HGHandle bigWord = wnGraph.findWord("animal");
//      // Getting all senses of the word
//      List<SynsetLink> adjSenseList = hg.getAll(graph,hg.and(hg.type(NounSynsetLink.class),
//      		hg.incident(bigWord)));
      // Getting the only meaning that has antonyms. It is number 9 (according to tests)

//  	SynsetLink senseWithAntonyms = adjSenseList.get(0); 
//  	// Getting the list of antonyms
//      List<Isa> antonymList = graph.getAll(hg.and(hg.type(Isa.class),
//      		hg.incident(graph.getHandle(senseWithAntonyms))));
//      System.out.println("Adjective's meaning: " + senseWithAntonyms.getGloss());
////      
//      
//      System.out.println(antonymList.size());
////      // Getting and printing antonyms
//      int antonymListSize = antonymList.size();
//      for (int i = 0; i < antonymListSize; i++) {
//      	Isa antonym = antonymList.get(i);
//      	for (int k = 0; k < antonym.getArity(); k++) {
//      		HGHandle handle = antonym.getTargetAt(k);
//      		SynsetLink sen = graph.get(handle);
//      		if (!sen.equals(senseWithAntonyms)) {
//      			sen.forEach(c -> {
//          			Word tempWord = graph.get(c);
//          			if (!tempWord.equals(bigWord)) {
//          				System.out.println(tempWord.getLemma());
//          			}
//          		});
//      		}
//      		
//      	}
//      }
        
        
        
        
        //        SynsetLink f = antonyms2.get(0);
//        f.forEach(h -> {
//    		Word tmpWord = graph.get(h);
//    		System.out.println(tmpWord.getLemma());
//    	});
//        int listSize = antonyms.size();
//        for (int i = 0; i < listSize; i++) {
//        	SemanticLink meaning = antonyms.get(i);
//        	for (int j = 0; j < meaning.getArity(); j ++) {
//        		HGHandle h = meaning.getTargetAt(j);
//        		if (!graph.get(h).equals(adjSense)) {
//        			System.out.println(graph.get(h).toString());        			
//        		}
//        		
//        		
//        	}
//        	
////        	System.out.println(graph.get(g).toString());
//        }
//        
        
        
        
       
        enWordnetButton.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				wordnetNum = 0;
				
			}
		});
        
        ruWordnetButton.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				wordnetNum = 1;
				
			}
		});
        
        searchButton.setOnAction(new EventHandler<ActionEvent>() {
	        @Override
	        public void handle(ActionEvent event) {
//	        	wordMeaningArea.clear();
	        	wordMeaningArea.getChildren().clear();

	        	HGHandle wordHandle =  wnGraphArray[wordnetNum].findWord(wordSearchField.getText());
	        	System.out.println(wordHandle);
	        	if (wordHandle != null) {

	        		NounList = wnGraphArray[wordnetNum].getNounSenses(wordHandle);

	        		VerbList = wnGraphArray[wordnetNum].getVerbSenses(wordHandle);
	        		AdjList = wnGraphArray[wordnetNum].getAdjSenses(wordHandle);

//	        		AdverbList = wnGraph.getAdverbSenses(wordHandle);
	            	
	        		wordMeaningArea.getChildren().add(NounHeader);

	        		if (!NounList.isEmpty()) {
	        			// Гиперссылки на синонимы
	        			List<Hyperlink> links = new ArrayList<Hyperlink>(); 
	        			
	        			int nounListSize = NounList.size();
		            	for (int i = 0; i < nounListSize; i++) {

		            		HGHandle sense =  NounList.get(i); // Значение слова
		            		System.out.println(sense);
		            		/*Синсет конкретного значения слова
		            		 * Далее этот синсет используется для получения всех отношений
		            		 */
		            		SynsetLink wordSynset = graphArray[wordnetNum].get(sense);
		            		
		                    // Нумерация значений
		            		String senseLineNum = i + 1 + ". ";
		            		Text numeration = new Text(senseLineNum);
		                    numeration.setFont(new Font(12));
		                    numeration.setStyle("-fx-font-weight: bold");
		                    
		                    // Заголовок "Синонимы"
		                    Text synonymsLabel = new Text("\n    Synonyms: ");
		                    synonymsLabel.setFont(new Font(12));
		                    synonymsLabel.setStyle("-fx-font-style: italic");
		                    
		                    // Заголовок "Другие типы отношений"
		                    Text otherRelationsLabel = new Text("    Other relations: ");
		                    synonymsLabel.setFont(new Font(12));
		                    synonymsLabel.setStyle("-fx-font-style: italic");
		                    
		                    // Отображение номера значения слова
		            		wordMeaningArea.getChildren().add(numeration);
		            		// Вывод примеров
		            		addNounExamples(wordSynset, wordMeaningArea);
		            		// Вывод синонимов
		            		addSynonyms(wordSynset, wordMeaningArea, wordSearchField, searchButton, "Noun");
		            		
						/***************************************
						 * Другие типы отношений 
						 * ************************************/
			            	// Надпись "Other relations"
			            	wordMeaningArea.getChildren().add(otherRelationsLabel);
			            	// Текстовая область куда выводятся слова удовлетворяющие отношению
			        		TextFlow relarionsTextFlow = new TextFlow();
			            	relarionsTextFlow.setMaxSize(0, 0);
			            	relarionsTextFlow.setVisible(false);
			            	
			            	// Гипонимы
			            	Hyperlink kindOfLink = addHyponyms(wordSynset, wordMeaningArea, wordSearchField, searchButton, relarionsTextFlow);
			            	// Меронимы
			            	Hyperlink partOfLink = addMeronyms(wordSynset, wordMeaningArea, wordSearchField, searchButton, relarionsTextFlow);
	            			
			            	// Добавление гиперссылок на отношения(гипонимии, меронимии) в общее окно.
			            	wordMeaningArea.getChildren().add(kindOfLink);
			            	wordMeaningArea.getChildren().add(new Text(", "));
	            			wordMeaningArea.getChildren().add(partOfLink);
	            			wordMeaningArea.getChildren().add(new Text(".\n"));
	            			wordMeaningArea.getChildren().add(relarionsTextFlow);
	            			wordMeaningArea.getChildren().add(new Text("\n"));
		       			}
	        		}
	        		else {
	        			wordMeaningArea.getChildren().add(new Text("Not found\n"));
	        		}
	        		
	        		/*************************************
	        		 * Глаголы
	        		 * ***********************************/
	        		wordMeaningArea.getChildren().add(VerbHeader);
	        		if (!VerbList.isEmpty()) {
	        			int verbListSize = VerbList.size();
		            	for (int i = 0; i < verbListSize; i++) {
		            		HGHandle sense = VerbList.get(i);
		            		SynsetLink verbSynset = graph.get(sense);

		                 // Нумерация значений
		            		String senseLineNum = i + 1 + ". ";
		            		Text numeration = new Text(senseLineNum);
		                    numeration.setFont(new Font(12));
		                    numeration.setStyle("-fx-font-weight: bold");
		                    
		                    wordMeaningArea.getChildren().add(numeration);
		                    
		                 // Вывод примеров
		            		addNounExamples(verbSynset, wordMeaningArea);
		            		// Вывод синонимов
		            		addSynonyms(verbSynset, wordMeaningArea, wordSearchField, searchButton, "Verb");
		       			}
	        		}
	        		else {
	        			wordMeaningArea.getChildren().add(new Text("Not found\n"));
	        		}
	        		
	        		/****************************************
	        		 * Прилагательные
	        		 ****************************************/
	        		wordMeaningArea.getChildren().add(AdjHeader);
	        		if (!AdjList.isEmpty()) {
		            	for (HGHandle sense: AdjList) {
		            		wordMeaningArea.getChildren().add(new Text(graphArray[wordnetNum].get(sense).toString() + "\n"));
		       			}
	        		}
	        		else {
	        			wordMeaningArea.getChildren().add(new Text("Not found\n"));
	        		}
//	        		
//	        		wordMeaningArea.getChildren().add(AdverbHeader);
//	        		if (!AdverbList.isEmpty()) {
//		            	for (HGHandle sense: AdverbList) {
//		            		wordMeaningArea.getChildren().add(new Text(graph.get(sense).toString() + "\n"));
//		       			}
//	        		}
//	        		else {
//	        			wordMeaningArea.getChildren().add(new Text("Not found\n"));
//	        		}
	        		
	        	}

	        }
        });
 
        
        wordSearchField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent ke) {
            	if (ke.getCode() == KeyCode.ENTER) {
            		searchButton.fire();
            	}
                
            }
        });
        Group root = new Group();
        HBox searchHBox = new HBox();
        HBox meaningHBox = new HBox();
        VBox vbox = new VBox();
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setPrefSize(450, 428);
        
        scrollPane.setContent(wordMeaningArea);
        searchHBox.getChildren().add(wordSearchField);
        searchHBox.getChildren().add(searchButton);
        searchHBox.getChildren().add(enWordnetButton);
        searchHBox.getChildren().add(ruWordnetButton);
        meaningHBox.getChildren().add(scrollPane);
        
        vbox.getChildren().add(searchHBox);
        vbox.getChildren().add(meaningHBox);
        
        root.getChildren().add(vbox);
        primaryStage.setScene(new Scene(root, 445, 450));
        primaryStage.setResizable(false);
        primaryStage.show();
    }
	
	public static void main(String[] args) {
		
//		loadWordnet(args);
		try
		{
			// Загрузка гипеграфа
//			graphArray[0] = HGEnvironment.get(args[1]);  // EnWordnet
//			graphArray[1] = HGEnvironment.get(args[2]); // RuWordnet
			
			graphArray[0] = new HyperGraph(args[1]);
//			graphArray[1] = new HyperGraph(args[2]);

//			graphArray[0].add(new SynsetLink())
			
			// Получение wordnetGraph
			wnGraphArray[0] = new WNGraph(graphArray[0]);
//			wnGraphArray[1] = new WNGraph(graphArray[1]);
				
//		
			HGHandle perfect = wnGraphArray[0].findWord("perfect");
			HGHandle little = wnGraphArray[0].findWord("little");
			List<HGHandle> perfList = wnGraphArray[0].getNounSenses(perfect);
			List<HGHandle> littleList = wnGraphArray[0].getNounSenses(little);
			HGHandle perfSense = perfList.get(0); 
			HGHandle littleSense = littleList.get(0); 
			
			HGHandle[] targ = new HGHandle[2];
			targ[0] = perfSense;
			targ[1] = littleSense;
//			
			graphArray[0].add(new SynsetLink(targ));
			wnGraphArray[0] = new WNGraph(graphArray[0]);
			
			launch(args);
			
		}
		catch (Throwable t)
		{
			t.printStackTrace();
		}
		
		
		
	}
	
	public static void loadWordnet(String args[]) {
		HGWordNetLoader loader = new HGWordNetLoader();
	loader.setDictionaryLocation(args[0]);
	try
	{
	    HGConfiguration config = new HGConfiguration();
//	    BDBConfig bdbConfig = (BDBConfig)config.getStoreImplementation().getConfiguration();
	    // Change the storage cache from the 20MB default to 500MB
//	    bdbConfig.getEnvironmentConfig().setCacheSize(1000*1024*1024);
	    config.setTransactional(false);
		HyperGraph graph = HGEnvironment.get(args[1]);
		System.out.println("Loading WordNet to " + args[1] + " from dictionary " + args[0]);
		System.out.println("Atom count before : " + hg.count(graph, hg.all()));
		long startTime = System.currentTimeMillis();			
		loader.loadWordNet(graph);
		long endTime = System.currentTimeMillis();
		System.out.println("WordNet sucessfully loaded - " + (endTime - startTime)/1000/60 + " minutes.");
		System.out.println("Atom count after : " + hg.count(graph, hg.all()));
	}
	catch (Throwable t)
	{
		t.printStackTrace();
	}

	
	}
	
	public String firstUpperCase(String word){
		if(word == null || word.isEmpty()) return "";//или return word;
		return word.substring(0, 1).toUpperCase() + word.substring(1);
	}
	
	public void addNounExamples(SynsetLink nounSynset, TextFlow wordMeaningArea) {
		// Заголок для примеров
        Text examplesHeader = new Text("    Examples: ");
        examplesHeader.setFont(new Font(12));
        examplesHeader.setStyle("-fx-font-style: italic");
        
		// Список значений и примеров
		String[] meaningAndExamples = nounSynset.getGloss().split(";");
		// 1й элемент - значение, последующие - примеры.
		wordMeaningArea.getChildren().add(
				new Text(firstUpperCase(meaningAndExamples[0]) + "\n"));
		
		// Вывод примеров
		int end = meaningAndExamples.length;
		// Надпись Examples: 
		wordMeaningArea.getChildren().add(examplesHeader);
		// Вывод примеров на экран
		for (int k = 1; k < end; k++) {
			meaningAndExamples[k] = meaningAndExamples[k].replace("\"", "").trim();
			wordMeaningArea.getChildren().add(
					new Text("\"" + firstUpperCase(meaningAndExamples[k]) + "\". "));
		}
	}
	
	public void addSynonyms(SynsetLink synset, TextFlow wordMeaningArea, TextField wordSearchField, Button searchButton, String type) {
		// Заголовок "Синонимы"
        Text synonymsLabel = new Text("\n    Synonyms: ");
        synonymsLabel.setFont(new Font(12));
        synonymsLabel.setStyle("-fx-font-style: italic");
        
        // Вывод надписи "Синонимы"
		wordMeaningArea.getChildren().add(synonymsLabel);
		
		List<Hyperlink> links = new ArrayList<Hyperlink>(); 
		
	    // Проход по синонимам
		synset.forEach(h -> {
			Word w = graphArray[wordnetNum].get(h); // Слово
			
			List<HGHandle> syns = null;
			
			// Значения синонимов для всплывающего окна подсказки
			if (type == "Noun"){
				syns = wnGraphArray[wordnetNum].getNounSenses(h);
			}
			else {
				syns = wnGraphArray[wordnetNum].getNounSenses(h);
			}
			
			String hyperlinkTip = "";
			
			// Загрузка значений слова в подсказку
			for (HGHandle def: syns) {
				SynsetLink ws = graphArray[wordnetNum].get(def);
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
	
	public Hyperlink addHyponyms(SynsetLink nounSynset, TextFlow wordMeaningArea, TextField wordSearchField, Button searchButton, TextFlow relarionsTextFlow) {

    	Hyperlink kindOfLink = new Hyperlink();
    	kindOfLink.setText("Hyponymy");
    	kindOfLink.setStyle("-fx-text-fill:blue");
		kindOfLink.setOnAction(new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent e) {
		    	int relationTextFlowSize = relarionsTextFlow.getChildren().size() - 1 ;
		    	// Очистка текстового поля под другими видами отношений (не работает!!!)
		    	relarionsTextFlow.getChildren().removeAll(new Hyperlink());
		    	
		    	List<KindOf> hyponymsList = graphArray[wordnetNum].getAll(hg.and(hg.type(KindOf.class),
    	        		hg.incident(graphArray[wordnetNum].getHandle(nounSynset))));
    	        
    	        // Получение списка гипонимов
    	        int hyponymListSize = hyponymsList.size();
    	        for (int p = 0; p < hyponymListSize; p++) {
    	        	KindOf hyponym = hyponymsList.get(p);
    	        	for (int z = 0; z < hyponym.getArity(); z++) {
    	        		HGHandle handle = hyponym.getTargetAt(z);
    	        		SynsetLink sen = graphArray[wordnetNum].get(handle);
    	        		if (!sen.equals(nounSynset)) {
    	        			sen.forEach(c -> {
    	            			Word tempWord = graphArray[wordnetNum].get(c);
        	            		Hyperlink hlink = new Hyperlink();
    			            	hlink.setText(tempWord.getLemma() + " ");
    			            	hlink.setOnAction(new EventHandler<ActionEvent>() {
    	            			    @Override
    	            			    public void handle(ActionEvent e) {
    	            			    	wordSearchField.setText(tempWord.getLemma());
 		            			        searchButton.fire();
    	            			    }
    			            	});
    			            	relarionsTextFlow.getChildren().add(hlink);
    	            		});
    	        		}
    	        		
    	        	}
    	        }
    	        relarionsTextFlow.setVisible(true);
    	        // Зависимость высоты окна от количесва гипонимов
    	        int height = 150 - 120 / (hyponymListSize + 1);
    	        relarionsTextFlow.setMaxSize(400, height);
		     }
		});
		
		return kindOfLink;
	}
	
	public Hyperlink addMeronyms(SynsetLink nounSynset, TextFlow wordMeaningArea, TextField wordSearchField, Button searchButton, TextFlow relarionsTextFlow) {
		
//		TextFlow relarionsTextFlow = new TextFlow();
//    	relarionsTextFlow.setMaxSize(0, 0);
//    	relarionsTextFlow.setVisible(false);
    	
		// Отношение меронимии (часть-целое)
    	Hyperlink partOfLink = new Hyperlink();
    	partOfLink.setText("Meronymy");
    	partOfLink.setStyle("-fx-text-fill:blue");
    	partOfLink.setOnAction(new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent e) {
		    	int relationTextFlowSize = relarionsTextFlow.getChildren().size() - 1 ;
		    	relarionsTextFlow.getChildren().removeAll(new Hyperlink());
		    	
		    	List<PartOf> meronymyList = graphArray[wordnetNum].getAll(hg.and(hg.type(PartOf.class),
    	        		hg.incident(graphArray[wordnetNum].getHandle(nounSynset))));
    	        
    	        // Получение списка меронимов и вывод их на экран
    	        int meronymyListSize = meronymyList.size();
    	        for (int p = 0; p < meronymyListSize; p++) {
    	        	PartOf meronym = meronymyList.get(p);
    	        	for (int z = 0; z < meronym.getArity(); z++) {
    	        		HGHandle handle = meronym.getTargetAt(z);
    	        		SynsetLink sen = graphArray[wordnetNum].get(handle);
    	        		if (!sen.equals(nounSynset)) {
    	        			sen.forEach(c -> {
    	            			Word tempWord = graphArray[wordnetNum].get(c);
        	            		Hyperlink hlink = new Hyperlink();
    			            	hlink.setText(tempWord.getLemma() + " ");
    			            	hlink.setOnAction(new EventHandler<ActionEvent>() {
    	            			    @Override
    	            			    public void handle(ActionEvent e) {
    	            			    	wordSearchField.setText(tempWord.getLemma());
 		            			        searchButton.fire();
    	            			    }
    			            	});
    			            	relarionsTextFlow.getChildren().add(hlink);
    	            		});
    	        		}
    	        		
    	        	}
    	        }
    	        
    	        // Отображение textflow с меронимами
    	        relarionsTextFlow.setVisible(true);
    	        if (meronymyListSize == 0) {
    	        	Text notFoundText = new Text("    Not found");
    	        	notFoundText.setStyle("-fx-font-weight:bold;");
    	        	relarionsTextFlow.getChildren().add(notFoundText);
    	        	relarionsTextFlow.setMaxSize(400, 40);
    	        }
    	        else {
    	        	int height = 150 - 120 / (meronymyListSize + 1);
        	        relarionsTextFlow.setMaxSize(400, height);
    	        }
        		
    	        
		     }
		});
    	
    	return partOfLink;
//    	
//		wordMeaningArea.getChildren().add(partOfLink);
//		wordMeaningArea.getChildren().add(new Text(".\n"));
//		wordMeaningArea.getChildren().add(relarionsTextFlow);
//		wordMeaningArea.getChildren().add(new Text("\n"));
	}
	
	public static class extText extends Text implements Cloneable{
		 @Override
	     public extText clone() throws CloneNotSupportedException{
			 extText obj = (extText)super.clone();
	         return obj;
	     }
		 
		 public extText(String text) {
			 super(text);
		 }
	}
	
}