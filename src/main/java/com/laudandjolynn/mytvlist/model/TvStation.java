package com.laudandjolynn.mytvlist.model;

/**
 * @author: Laud
 * @email: htd0324@gmail.com
 * @date: 2015年3月25日 下午4:40:59
 * @copyright: www.laudandjolynn.com
 */
public class TvStation {
	private int id;
	private String name;
	private String classify;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getClassify() {
		return classify;
	}

	public void setClassify(String classify) {
		this.classify = classify;
	}

	@Override
	public String toString() {
		return "TvStation [id=" + id + ", name=" + name + ", classify="
				+ classify + "]";
	}

}
