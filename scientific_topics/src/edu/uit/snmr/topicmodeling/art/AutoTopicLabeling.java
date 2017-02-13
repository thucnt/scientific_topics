/**
 * 
 */
package edu.uit.snmr.topicmodeling.art;

import java.io.File;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import model.ATopic;

import edu.uit.snmr.math.similarity.AbstractStringMetric;
import edu.uit.snmr.math.similarity.CosineSimilarity;
import edu.uit.snmr.math.similarity.DiceSimilarity;
import edu.uit.snmr.math.similarity.JaccardSimilarity;
import edu.uit.snmr.math.similarity.MutualSimilarity;
import edu.uit.snmr.math.similarity.OverlapCoefficient;
import edu.uit.snmr.math.similarity.TanimotoCoefficientSimilarity;

/**
 * @author muonnv
 *
 */
public class AutoTopicLabeling {

	private List<ATopic> trainingTopics;
	private TopicTokenizer topicLoader = new TopicTokenizer();

	public AutoTopicLabeling(File trainedTopicFolder) throws FileNotFoundException {

		trainingTopics = topicLoader.loadTrainedTopics(trainedTopicFolder);
	}

	public AutoTopicLabeling(String xmltrainedTopicFileName) throws FileNotFoundException {

		trainingTopics = topicLoader.loadTrainedTopicsFromXml(xmltrainedTopicFileName);
	}

	/**
	 * @param trainingTopics
	 */
	public AutoTopicLabeling(List<ATopic> trainingTopics) {
		this.trainingTopics = trainingTopics;
	}

	public void setTrainingTopics(List<ATopic> trainingTopics) {
		this.trainingTopics = trainingTopics;
	}

	public List<ATopic> getTrainingTopics() {
		return trainingTopics;
	}


	public Map<String, Float> calSimilarity(ATopic topic, AbstractStringMetric similarityMeasure) {

		Map<String, Float> mimilarityMap = new HashMap<String, Float>();
		for (ATopic each : trainingTopics) {
			float similarity = similarityMeasure.getSimilarity(topic.getWordSet(), each.getWordSet());
			mimilarityMap.put(each.getLabel(), similarity);
		}
		return mimilarityMap;
	}

	public List<Map<String, Float>> calSimilarityMetricHelper(ATopic topic) {
		List<Map<String, Float>> metric = new ArrayList<Map<String,Float>>();

		//cosine
		AbstractStringMetric similarityMeasure = new CosineSimilarity();
		Map<String, Float> cosineSimilarityMap = this.calSimilarity(topic, similarityMeasure);
		metric.add(cosineSimilarityMap);
			
		
		//Overlap
		similarityMeasure = new OverlapCoefficient();
		Map<String, Float> overlapSimilarityMap = this.calSimilarity(topic, similarityMeasure);
		metric.add(overlapSimilarityMap);

		//mutual
		similarityMeasure = new MutualSimilarity();
		Map<String, Float> mutualSimilarityMap = this.calSimilarity(topic, similarityMeasure);
		metric.add(mutualSimilarityMap);

		//dice
		similarityMeasure = new DiceSimilarity();
		Map<String, Float> diceSimilarityMap = this.calSimilarity(topic, similarityMeasure);
		metric.add(diceSimilarityMap);

		//jaccard
		similarityMeasure = new JaccardSimilarity();
		Map<String, Float> jaccardSimilarityMap = this.calSimilarity(topic, similarityMeasure);
		metric.add(jaccardSimilarityMap);

		//tanimoto
		similarityMeasure = new TanimotoCoefficientSimilarity();
		Map<String, Float> tanimotoSimilarityMap = this.calSimilarity(topic, similarityMeasure);
		metric.add(tanimotoSimilarityMap);
		
		return metric;
	}

	public Map<String, List<Float>> calSimilarityMetric(ATopic topic) {
		List<Map<String, Float>> metric = calSimilarityMetricHelper(topic);
		Map<String, List<Float>> result = new HashMap<String, List<Float>>();
		for (Map<String, Float> map : metric) {

			Set<Entry<String, Float>> entries = map.entrySet();

			for (Entry<String, Float> entry : entries) {
				List<Float> acc = result.get(entry.getKey());
				if (acc == null) {
					acc = new ArrayList<Float>();
				}
				acc.add(entry.getValue());
				result.put(entry.getKey(), acc);
			}
		}
		return result;
	}
	
	public String findLabelFoRTopic(ATopic topic) {
		List<Map<String, Float>> metric = calSimilarityMetricHelper(topic);
		String label = "";
		for (Map<String, Float> map : metric) {
			String temName = this.findMaxValue(map);
			if (label == "") {
				label = temName;
			} else if (!label.equalsIgnoreCase(temName)) {
				label = "";
				break;
			}
		}
		return label;
	}
	

	private String findMaxValue(Map<String, Float> similarMap) {
		String topicName = "";
		Float maxValue = 0.0f;
		Set<Entry<String, Float>> entries = similarMap.entrySet();
		for (Entry<String, Float> entry : entries) {
			if (maxValue < entry.getValue()) {
				maxValue = entry.getValue();
				topicName = entry.getKey();
			}
		}
		return topicName;
	}

	public Map<String, String> doLabelingForTopics(File extractedTopicFolder) throws FileNotFoundException {
		Map<String, String> result = new HashMap<String, String>();
		List<ATopic> extractedTopics = topicLoader.loadExtractedTopics(extractedTopicFolder);

		for (ATopic aTopic : extractedTopics) {
			String topicLabel = findLabelFoRTopic(aTopic);
			System.out.println("Topic " + aTopic.getLabel());
			if (topicLabel != null && !topicLabel.trim().isEmpty()) {
				result.put(aTopic.getLabel(), topicLabel);
			}
			toString(calSimilarityMetric(aTopic));
			//calSimilarityMetric(aTopic);
			System.out.println("----------------------------------------------");
		}
		System.out.println(result);
		return result;
	}

	public void toString(Map<String, List<Float>> metric) {
		for (String key : metric.keySet()) {
			String acc = key + ": \t\t\t\t";
			for (Float  each : metric.get(key)) {
				acc += String.format("%.3g", each) + "\t\t";
			}
			System.out.println(acc);
		}
	}

	public static void main(String[] args) {
		try {
			AutoTopicLabeling autoTopicLabeling = new AutoTopicLabeling("tree.xml");
			autoTopicLabeling.doLabelingForTopics(new File("C:/data/test/topics"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
