package javatrek.panels;

import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

/**
 * This panel window displays instructions on various aspects of the game.
 * 
 * <UL>
 * <LI>Version 1.0 - 10/19/2003 - the original class
 * <LI>Version 2.0 - 11/09/2004 - moved the data from a class named "Instructions" to text files stored in the JAR file
 * </UL>
 * 
 * @author Andrew Killam
 * @version 2.0 - 11/09/2004
 */

public class InstructionsPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	public InstructionsPanel() {
		super(new BorderLayout());

		// add a tabbed pane to display the instructions
		JTabbedPane tp = new JTabbedPane();
		add(tp, BorderLayout.CENTER);

		// TODO: the two stanzas below are almost identical; replace with a proper method

		// create the energy weapon instructions
		try {
			StringBuilder sb = new StringBuilder();
			InputStream in = getClass().getResourceAsStream("/data/instructions_energy_weapons.txt");
			BufferedReader input = new BufferedReader(new InputStreamReader(in));
			Scanner scanner = new Scanner(input);
			scanner.useDelimiter("\n");
			while (scanner.hasNext())
				sb.append(scanner.next());
			scanner.close();
			input.close();
			in.close();

			JTextArea ta_energy_weapons = new JTextArea(sb.toString());
			ta_energy_weapons.setLineWrap(true);
			ta_energy_weapons.setWrapStyleWord(true);
			ta_energy_weapons.setEditable(false);
			tp.addTab("Energy Weapons", new JScrollPane(ta_energy_weapons));
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}

		// create the projectile weapon instructions
		try {
			StringBuilder sb = new StringBuilder();
			InputStream in = getClass().getResourceAsStream("/data/instructions_projectile_weapons.txt");
			BufferedReader input = new BufferedReader(new InputStreamReader(in));
			Scanner scanner = new Scanner(input);
			scanner.useDelimiter("\n");
			while (scanner.hasNext())
				sb.append(scanner.next());
			scanner.close();
			input.close();
			in.close();

			JTextArea ta_projectile_instructions = new JTextArea(sb.toString());
			ta_projectile_instructions.setLineWrap(true);
			ta_projectile_instructions.setWrapStyleWord(true);
			ta_projectile_instructions.setEditable(false);
			tp.addTab("Energy Weapons", new JScrollPane(ta_projectile_instructions));
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}

	}

}
