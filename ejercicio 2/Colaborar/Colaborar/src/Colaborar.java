package src;


public class Colaborar {

	public static void main(String[] args) {
		Process nuevoProceso;
		try {
			for (int i = 1; i <= 11; i++) {
				// palabras y fichero jar que ejecuto
				nuevoProceso = Runtime.getRuntime()
						.exec("java -jar " + "Lenguaje.jar " + (i * 10) + " " + "miFicheroDeLenguaje.txt");
				System.out.println("proceso creado " + nuevoProceso);
			}

		} catch (SecurityException ex) {
			System.err.println("Ha ocurrido un error de Seguridad."
					+ "No se ha podido crear el proceso por falta de permisos.");
		} catch (Exception ex) {
			System.err.println("Ha ocurrido un error, descripción: " + ex.toString());
		}

	}

}
