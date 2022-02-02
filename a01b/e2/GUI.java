package a01b.e2;

import javax.swing.*;
import java.util.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class GUI extends JFrame {

	private static final long serialVersionUID = -6218820567019985015L;
	private final Map<JButton, Pair<Integer, Integer>> cells;
	private final Logic logic;

	public GUI(int size) {
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setSize(50 * size, 50 * size);
		this.cells = new HashMap<>();
		this.logic = new LogicImpl(size);

		JPanel panel = new JPanel(new GridLayout(size, size));
		this.getContentPane().add(panel);

		ActionListener al = e -> {
			var button = (JButton) e.getSource();
			logic.hit(cells.get(button));
			refreshGrid();
			if (logic.isOver()) {
				cells.forEach((b, p) -> {
					b.setEnabled(false);
				});
				JOptionPane.showMessageDialog(panel, "Game Over");
			}
		};

		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				final JButton jb = new JButton(" ");
				this.cells.put(jb, new Pair<>(j, i));
				jb.addActionListener(al);
				panel.add(jb);
			}
		}
		refreshGrid();
		this.setVisible(true);
	}

	private void refreshGrid() {
		cells.forEach((b, p) -> {
			b.setText(logic.getGrid().get(p));
		});

	}

}
