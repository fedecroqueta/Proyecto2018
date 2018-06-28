package vistas;

import java.awt.EventQueue;

import javax.swing.JFrame;

public class BajaAdmin {

	private JFrame frmAltaAdministrador;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					BajaAdmin window = new BajaAdmin();
					window.frmAltaAdministrador.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public BajaAdmin() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmAltaAdministrador = new JFrame();
		frmAltaAdministrador.setTitle("Baja Administrador");
		frmAltaAdministrador.setBounds(100, 100, 450, 300);
		frmAltaAdministrador.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmAltaAdministrador.setLocationRelativeTo(null);	
	}

}
