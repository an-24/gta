package biz.gelicon.gta.utils;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
public class Pair<L, R> implements Serializable{
	private static final long serialVersionUID = 3996805773523214809L;
	private L left;
	private R right;

	public Pair() {
	}

	public Pair(L left, R right) {
		this.left = left;
		this.right = right;
	}

	public L getLeft() {
		return left;
	}

	public R getRight() {
		return right;
	}

	public void setLeft(L left) {
		this.left = left;
	}

	public void setRight(R right) {
		this.right = right;
	}

}
