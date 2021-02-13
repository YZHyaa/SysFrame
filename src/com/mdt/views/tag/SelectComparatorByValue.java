package com.mdt.views.tag;

import java.text.Collator;
import java.util.Comparator;

public class SelectComparatorByValue implements Comparator {

	private boolean asc;
	Comparator comp = Collator.getInstance();

	public SelectComparatorByValue(boolean asc) {
		this.asc = asc;
	}

	public int compare(Object o1, Object o2) {
		if ((o1 != null) && (o2 != null)) {
			if (this.asc) {
				return this.comp.compare(o1, o2);
			}
			return this.comp.compare(o2, o1);
		}
		return -1;
	}

}
