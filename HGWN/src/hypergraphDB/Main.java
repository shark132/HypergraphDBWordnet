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

import java.awt.Color;
import java.lang.reflect.Field;
import java.net.URL;

public class Main extends Application {
	public static List<HGHandle> NounList;
	public static List<HGHandle> VerbList;
	public static List<HGHandle> AdjList;
	public static List<HGHandle> AdverbList;
	
	public static HyperGraph graph;
	public static WNGraph wnGraph;
	public static HGHandle wordH;
	
	
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
         * ��������� Kind of. ���������� ������ ����� ������ ����� � ����� ����������
         * ��������, ������ - ������� �
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
        
        
        
        
        searchButton.setOnAction(new EventHandler<ActionEvent>() {
	        @Override
	        public void handle(ActionEvent event) {
//	        	wordMeaningArea.clear();
	        	wordMeaningArea.getChildren().clear();
	        	HGHandle wordHandle =  wnGraph.findWord(wordSearchField.getText());
	        	
	        	if (wordHandle != null) {
	        		NounList = wnGraph.getNounSenses(wordHandle);
	        		VerbList = wnGraph.getVerbSenses(wordHandle);
	        		AdjList = wnGraph.getAdjSenses(wordHandle);
	        		AdverbList = wnGraph.getAdverbSenses(wordHandle);
	            	
	        		wordMeaningArea.getChildren().add(NounHeader);

	        		if (!NounList.isEmpty()) {
	        			// ����������� �� ��������
	        			List<Hyperlink> links = new ArrayList<Hyperlink>(); 
	        			
	        			int nounListSize = NounList.size();
		            	for (int i = 0; i < nounListSize; i++) {

		            		HGHandle sense =  NounList.get(i); // �������� �����
		            		/*������ ����������� �������� �����
		            		 * ����� ���� ������ ������������ ��� ��������� ���� ���������
		            		 */
		            		SynsetLink wordSynset = graph.get(sense);
		            		
		                    // ��������� ��������
		            		String senseLineNum = i + 1 + ". ";
		            		Text numeration = new Text(senseLineNum);
		                    numeration.setFont(new Font(12));
		                    numeration.setStyle("-fx-font-weight: bold");
		                    
		                    // ��������� "��������"
		                    Text synonymsLabel = new Text("\n    Synonyms: ");
		                    synonymsLabel.setFont(new Font(12));
		                    synonymsLabel.setStyle("-fx-font-style: italic");
		                    
		                    // ��������� "������ ���� ���������"
		                    Text otherRelationsLabel = new Text("    Other relations: ");
		                    synonymsLabel.setFont(new Font(12));
		                    synonymsLabel.setStyle("-fx-font-style: italic");
		                    
		                    // ����������� ������ �������� �����
		            		wordMeaningArea.getChildren().add(numeration);
		            		// ����� ��������
		            		addNounExamples(wordSynset, wordMeaningArea);
		            		// ����� ���������
		            		addSynonyms(wordSynset, wordMeaningArea, wordSearchField, searchButton, "Noun");
		            		
						/***************************************
						 * ������ ���� ��������� 
						 * ************************************/
			            	// ������� "Other relations"
			            	wordMeaningArea.getChildren().add(otherRelationsLabel);
			            	// ��������� ������� ���� ��������� ����� ��������������� ���������
			        		TextFlow relarionsTextFlow = new TextFlow();
			            	relarionsTextFlow.setMaxSize(0, 0);
			            	relarionsTextFlow.setVisible(false);
			            	
			            	// ��������
			            	Hyperlink kindOfLink = addHyponyms(wordSynset, wordMeaningArea, wordSearchField, searchButton, relarionsTextFlow);
			            	// ��������
			            	Hyperlink partOfLink = addMeronyms(wordSynset, wordMeaningArea, wordSearchField, searchButton, relarionsTextFlow);
	            			
			            	// ���������� ����������� �� ���������(���������, ���������) � ����� ����.
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
	        		 * �������
	        		 * ***********************************/
	        		wordMeaningArea.getChildren().add(VerbHeader);
	        		if (!VerbList.isEmpty()) {
	        			int verbListSize = VerbList.size();
		            	for (int i = 0; i < verbListSize; i++) {
		            		HGHandle sense = VerbList.get(i);
		            		SynsetLink verbSynset = graph.get(sense);

		                 // ��������� ��������
		            		String senseLineNum = i + 1 + ". ";
		            		Text numeration = new Text(senseLineNum);
		                    numeration.setFont(new Font(12));
		                    numeration.setStyle("-fx-font-weight: bold");
		                    
		                    wordMeaningArea.getChildren().add(numeration);
		                    
		                 // ����� ��������
		            		addNounExamples(verbSynset, wordMeaningArea);
		            		// ����� ���������
		            		addSynonyms(verbSynset, wordMeaningArea, wordSearchField, searchButton, "Verb");
		       			}
	        		}
	        		else {
	        			wordMeaningArea.getChildren().add(new Text("Not found\n"));
	        		}
	        		
	        		/****************************************
	        		 * ��������������
	        		 ****************************************/
	        		wordMeaningArea.getChildren().add(AdjHeader);
	        		if (!AdjList.isEmpty()) {
		            	for (HGHandle sense: AdjList) {
		            		wordMeaningArea.getChildren().add(new Text(graph.get(sense).toString() + "\n"));
		       			}
	        		}
	        		else {
	        			wordMeaningArea.getChildren().add(new Text("Not found\n"));
	        		}
	        		
	        		wordMeaningArea.getChildren().add(AdverbHeader);
	        		if (!AdverbList.isEmpty()) {
		            	for (HGHandle sense: AdverbList) {
		            		wordMeaningArea.getChildren().add(new Text(graph.get(sense).toString() + "\n"));
		       			}
	        		}
	        		else {
	        			wordMeaningArea.getChildren().add(new Text("Not found\n"));
	        		}
	        		
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
        meaningHBox.getChildren().add(scrollPane);
        
        vbox.getChildren().add(searchHBox);
        vbox.getChildren().add(meaningHBox);
        
        root.getChildren().add(vbox);
        primaryStage.setScene(new Scene(root, 445, 450));
        primaryStage.setResizable(false);
        primaryStage.show();
    }
	
	public static void main(String[] args) {
		
		if (args.length != 2)
		{
			System.out.println("Usage: HGWordnetLoader dictionaryLocation hypergraphDBLocation");
			System.exit(0);
		}

		try
		{
			// �������� ���������
			graph = HGEnvironment.get(args[1]);
			// ��������� wordnetGraph
			wnGraph = new WNGraph(graph);
				
			launch(args);
			
		}
		catch (Throwable t)
		{
			t.printStackTrace();
		}
	}
	
	public String firstUpperCase(String word){
		if(word == null || word.isEmpty()) return "";//��� return word;
		return word.substring(0, 1).toUpperCase() + word.substring(1);
	}
	
	public void addNounExamples(SynsetLink nounSynset, TextFlow wordMeaningArea) {
		// ������� ��� ��������
        Text examplesHeader = new Text("    Examples: ");
        examplesHeader.setFont(new Font(12));
        examplesHeader.setStyle("-fx-font-style: italic");
        
		// ������ �������� � ��������
		String[] meaningAndExamples = nounSynset.getGloss().split(";");
		// 1� ������� - ��������, ����������� - �������.
		wordMeaningArea.getChildren().add(
				new Text(firstUpperCase(meaningAndExamples[0]) + "\n"));
		
		// ����� ��������
		int end = meaningAndExamples.length;
		// ������� Examples: 
		wordMeaningArea.getChildren().add(examplesHeader);
		// ����� �������� �� �����
		for (int k = 1; k < end; k++) {
			meaningAndExamples[k] = meaningAndExamples[k].replace("\"", "").trim();
			wordMeaningArea.getChildren().add(
					new Text("\"" + firstUpperCase(meaningAndExamples[k]) + "\". "));
		}
	}
	
	public void addSynonyms(SynsetLink synset, TextFlow wordMeaningArea, TextField wordSearchField, Button searchButton, String type) {
		// ��������� "��������"
        Text synonymsLabel = new Text("\n    Synonyms: ");
        synonymsLabel.setFont(new Font(12));
        synonymsLabel.setStyle("-fx-font-style: italic");
        
        // ����� ������� "��������"
		wordMeaningArea.getChildren().add(synonymsLabel);
		
		List<Hyperlink> links = new ArrayList<Hyperlink>(); 
		

	    // ������ �� ���������
		synset.forEach(h -> {
			Word w = graph.get(h); // �����
			List<HGHandle> syns = null;
			
			// �������� ��������� ��� ������������ ���� ���������
			if (type == "Noun"){
				syns = wnGraph.getNounSenses(h);
			}
			else {
				syns = wnGraph.getNounSenses(h);
			}
			
			String hyperlinkTip = "";
			
			// �������� �������� ����� � ���������
			for (HGHandle def: syns) {
				SynsetLink ws = graph.get(def);
				hyperlinkTip +=  firstUpperCase(ws.getGloss()) + "\n";
			}
			// �������� ����������� ��������
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
		
		// ����� ���������
    	int linksSize = links.size();
    	int dotIndex = linksSize - 1; // ����� ��� ����� ��������� �����
    	for (int j = 0; j < linksSize; j++) {
    		wordMeaningArea.getChildren().add(links.get(j));
    		if (j != dotIndex) {
    			wordMeaningArea.getChildren().add(new Text(","));
    		}
    		else {
    			wordMeaningArea.getChildren().add(new Text(".\n"));
    		}
    	}
    	links.clear(); // ������� ����������� �� ��������
	}
	
	public Hyperlink addHyponyms(SynsetLink nounSynset, TextFlow wordMeaningArea, TextField wordSearchField, Button searchButton, TextFlow relarionsTextFlow) {

    	Hyperlink kindOfLink = new Hyperlink();
    	kindOfLink.setText("Hyponymy");
    	kindOfLink.setStyle("-fx-text-fill:blue");
		kindOfLink.setOnAction(new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent e) {
		    	int relationTextFlowSize = relarionsTextFlow.getChildren().size() - 1 ;
		    	// ������� ���������� ���� ��� ������� ������ ��������� (�� ��������!!!)
		    	relarionsTextFlow.getChildren().removeAll(new Hyperlink());
		    	
		    	List<KindOf> hyponymsList = graph.getAll(hg.and(hg.type(KindOf.class),
    	        		hg.incident(graph.getHandle(nounSynset))));
    	        
    	        // ��������� ������ ���������
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
    			            	relarionsTextFlow.getChildren().add(hlink);
    	            		});
    	        		}
    	        		
    	        	}
    	        }
    	        relarionsTextFlow.setVisible(true);
    	        // ����������� ������ ���� �� ��������� ���������
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
    	
		// ��������� ��������� (�����-�����)
    	Hyperlink partOfLink = new Hyperlink();
    	partOfLink.setText("Meronymy");
    	partOfLink.setStyle("-fx-text-fill:blue");
    	partOfLink.setOnAction(new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent e) {
		    	int relationTextFlowSize = relarionsTextFlow.getChildren().size() - 1 ;
		    	relarionsTextFlow.getChildren().removeAll(new Hyperlink());
		    	
		    	List<PartOf> meronymyList = graph.getAll(hg.and(hg.type(PartOf.class),
    	        		hg.incident(graph.getHandle(nounSynset))));
    	        
    	        // ��������� ������ ��������� � ����� �� �� �����
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
    			            	relarionsTextFlow.getChildren().add(hlink);
    	            		});
    	        		}
    	        		
    	        	}
    	        }
    	        
    	        // ����������� textflow � ����������
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