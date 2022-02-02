package a01a.e2;

import javax.swing.*;
import java.util.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class GUI extends JFrame {

	private static final long serialVersionUID = -6218820567019985015L;
	private final Map<JButton, Pair<Integer, Integer>> cells = new HashMap<>();
	private final Logic logic;

	public GUI(int size) {
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setSize(size * 50, 50 * size);
		this.logic = new LogicImpl(size);

		JPanel panel = new JPanel(new GridLayout(size, size));
		this.getContentPane().add(panel);

		ActionListener al = e -> {
			var button = (JButton) e.getSource();
			System.out.println("" + cells.get(button));
			logic.pressed(cells.get(button));
			refreshGrid();
			if (logic.isOver()) {
				cells.forEach((b, p) -> {
					b.setEnabled(false);
				});
				JOptionPane.showMessageDialog(panel, "End of game");
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
			b.setText(logic.getStars().get(p));
		});
	}
}
