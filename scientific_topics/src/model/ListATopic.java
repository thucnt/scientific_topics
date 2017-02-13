package model;

import java.util.ArrayList;
import java.util.List;

public class ListATopic {
	private List<ATopic> topics = new ArrayList<ATopic>();

	public void add(ATopic topic) {
		this.topics.add(topic);
	}

	public void addAll(List<ATopic> that) {
		this.topics.addAll(that);
	}
}
