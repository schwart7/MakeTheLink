package MakeTheLink.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

import MakeTheLink.core.Question;

public class CluesRunnable implements Runnable {

	private volatile boolean running = true;
	private Question thisQuestion;
	private Table clues_list;

	public CluesRunnable(Question thisQuestion, Table clues_list) {
		this.thisQuestion = thisQuestion;
		this.clues_list = clues_list;
	}

	public void terminate() {
		running = false;
	}

	@Override
	public void run() {
		Display display = Display.getDefault();
		for (int j = 1; j < thisQuestion.getHintsList().length; j++) {

			try {
				Thread.sleep(1000 * 60 / (thisQuestion.getHintsList().length));

			} catch (InterruptedException e) {
				running = false;
			}
			if (!running)
				return;

			final String hint = thisQuestion.getHintsList()[j];

			display.asyncExec(new Runnable() {
				public void run() {
					TableItem clue_item = new TableItem(clues_list, SWT.NONE);
					clue_item.setText(hint);
					clues_list.redraw();
				}
			});
		}
	}

}
