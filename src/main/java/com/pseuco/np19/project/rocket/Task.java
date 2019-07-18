package com.pseuco.np19.project.rocket;

import java.util.LinkedList;
import java.util.List;

import com.pseuco.np19.project.launcher.Configuration;
import com.pseuco.np19.project.launcher.breaker.item.Item;
import com.pseuco.np19.project.launcher.cli.Unit;
import com.pseuco.np19.project.launcher.render.Renderable;
import com.pseuco.np19.project.slug.tree.block.BlockElement;

public class Task implements Runnable {
	private BlockElement element;

	private int currentSegment, currentIndex;

	private final Unit unit;

	private final Configuration configuration;

	private final List<Item<Renderable>> items = new LinkedList<>();

	private boolean unableToBreak = false;

	public Task(BlockElement element, int currentSegment, int currentIndex, Unit unit, Configuration configuration) {
		super();
		this.element = element;
		this.currentSegment = currentSegment;
		this.currentIndex = currentIndex;
		this.unit = unit;
		this.configuration = configuration;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		
		//accept + visit
		/*document.getCurrentElement().accept(this);
		if (this.unableToBreak) {
			this.unit.getPrinter().printErrorPage();
			this.unit.getPrinter().finishDocument();
			return;
		}*/
		
		// falls voll, starte segment runnable
	}

	
}
